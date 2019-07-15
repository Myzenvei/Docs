import _ from 'lodash';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import vxStepperControl from '../../common/vx-stepper-control/vx-stepper-control.vue';
import vxCartProductTile from '../../manage-shopping-cart/vx-cart-product-tile/vx-cart-product-tile.vue';
import vxDropdownPrimary from '../../common/vx-dropdown-primary/vx-dropdown-primary.vue';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import vxAddEditAddress from '../../common/vx-add-edit-address/vx-add-edit-address.vue';
import vxSplitAddress from '../vx-split-address/vx-split-address.vue';
import globals from '../../common/globals';
import flyoutBannerMixin from '../../common/vx-flyout-banner/vx-flyout-banner-mixin';
import vxProductTile from '../../common/vx-product-tile/vx-product-tile.vue';
import mobileMixin from '../../common/mixins/mobile-mixin';
import CheckoutService from '../../common/services/checkout-service';

import {
  checkoutEventBus,
  eventBus
} from '../../../modules/event-bus';
import {
  ProductAvailability,
  discount
} from '../../common/mixins/vx-enums';

export default {
  name: 'vx-multi-shipping-address',
  mixins: [flyoutBannerMixin, mobileMixin],
  components: {
    vxSpinner,
    vxCartProductTile,
    vxDropdownPrimary,
    vxAddEditAddress,
    vxModal,
    vxStepperControl,
    vxSplitAddress,
    vxProductTile,
  },
  props: {
    isShippingMultiple: Boolean,
    i18n: Object,
    userAddresses: Array,
    visibleEntries: {
      type: Array,
    },
    isGuest: Boolean,
    palletShipment: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      globals,
      checkoutService: new CheckoutService(),
      isSplitApplicable: false,
      promotionData: {},
      quantityError: false,
      addressEmptyError: false,
      splitToSameAddressError: false,
      addressAvailable: false,
      isSaved: false,
      isEditable: true,
      splitEntries: [],
      savedAddress: {},
      restrictionError: false,
      currentAvailableAddresses: [],
      uniqueSavedAddresses: [],
      allSelectedAddresses: [],
      shippingDetails: [],
      shippingAddress: [],
      restrictedItems: [],
      splitEntryDataArray: [],
      contactUsUrl: '',
      ProductAvailability,
      discount,
      parentMounted: false,
      useAddressClicked: false,
      allUserSavedAddresses: [],
      modalScrollable: true,
      defaultAddress: {},
      deliveryId: '',
    };
  },
  computed: {},
  mounted() {
    this.parentMounted = true;
    if (globals.isB2B()) {
      this.isSplitApplicable = true;
    }
    if (this.userAddresses && this.userAddresses.length !== 0) {
      this.allUserSavedAddresses = this.formAddressValues(this.userAddresses);
      this.addressAvailable = true;
    } else {
      this.addressAvailable = false;
    }
    this.contactUsUrl = this.globals.getNavigationUrl('contactUs');
    eventBus.$on('removeScrollbar', (isRemoveScrollbar) => {
      this.modalScrollable = !isRemoveScrollbar;
    });
  },
  methods: {
    addNewAddress() {
      this.$refs.addNewAddressModal.open();
    },
    toggleEditMode() {
      this.isSaved = false;
      this.isEditable = true;
      this.allSelectedAddresses = [];
      this.savedAddress = [];
    },

    // Request Data Formation
    saveRequestData() {
      // shipping addresses request
      const requestdata = {
        shippingDetails: _.flattenDeep(this.shippingDetails),
        shippingAddress: [{
          splitEntry: this.splitEntries,
        },],
      };
      return requestdata;
    },
    useThisAddress() {
      if (!this.isGuest) {
        this.$emit('call-get-addresses');
      } else {
        this.$emit('call-guest-get-addresses');
      }
      this.addressAvailable = true;
    },
    setAddressForAllEntries(data) {
      for (let i = 0; i < this.$refs.splitQtyAddress.length; i += 1) {
        this.$refs.splitQtyAddress[i].setAddressDropdownValue(data);
      }
    },
    getAddressValues(data) {
      this.userAddresses = data;
      if (this.deliveryId && this.userAddresses.length !== 0) {
        for (let i = 0; i < this.userAddresses.length; i += 1) {
          if (this.userAddresses[i].id === this.deliveryId) {
            this.savedAddress.label = this.formAddressString(this.userAddresses[i]);
            this.savedAddress.value = this.userAddresses[i];
            break;
          }
        }
        for (let j = 0; j < this.$refs.splitQtyAddress.length; j += 1) {
          this.$refs.splitQtyAddress[j].setAddressDropdownValue(this.savedAddress);
        }
        this.deliveryId = '';
      }
      this.allUserSavedAddresses = this.formAddressValues(this.userAddresses);
    },
    saveThisAddress(data) {
      this.$refs.addNewAddressModal.close();
      this.checkoutService.addAddress({}, this.handleSaveAddressResponse, this.handleSaveAddressError, data.value);
      this.$refs.spinner.showSpinner();
      this.savedAddress = data;
    },
    handleSaveAddressError(error) {
      this.$refs.spinner.hideSpinner();
    },
    handleSaveAddressResponse(response) {
      this.$refs.spinner.hideSpinner();
      this.addressAvailable = true;
      this.deliveryId = response.data.id;
      this.useThisAddress();
    },
    toggleViewMode(event) {
      this.splitEntries = [];
      this.shippingDetails = [];
      this.splitEntryDataArray = [];
      this.quantityError = false;
      this.addressEmptyError = false;
      this.splitToSameAddressError = false;
      for (let i = 0; i < this.$refs.splitQtyAddress.length; i += 1) {
        const splitEntryData = this.$refs.splitQtyAddress[i].productSplitEntryData();

        this.splitEntryDataArray.push(splitEntryData);
      }
      if (this.splitEntryDataArray && this.splitEntryDataArray.length !== 0) {
        for (let j = 0; j < this.splitEntryDataArray.length; j += 1) {
          if (this.splitEntryDataArray[j]) {
            this.shippingDetails.push(this.splitEntryDataArray[j].addressData);
            this.splitEntries.push(this.splitEntryDataArray[j].productData);
            this.allSelectedAddresses.push(this.splitEntryDataArray[j].allAddresses);
          }
        }
      }
      if (this.allSelectedAddresses && this.allSelectedAddresses.length !== 0) {
        this.splitShippingAddresses(this.allSelectedAddresses);
      }
      if (this.splitEntries.length !== 0 && !this.quantityError && !this.addressEmptyError && !this.splitToSameAddressError) {
        this.checkoutService.shippingRestriction({}, this.handleUseAddressResponse, this.handleUseAddressError, this.saveRequestData());

        if (event) {
          this.useAddressClicked = true;
        } else {
          this.useAddressClicked = false;
        }
        this.$refs.spinner.showSpinner();
      }
    },
    checkRestricted(productCode) {
      let isRestricted = false;
      for (let i = 0; i < this.restrictedItems.length; i += 1) {
        if (this.restrictedItems[i].productCode === productCode) {
          isRestricted = true;
          break;
        }
      }
      return isRestricted;
    },
    switchToSaveMode(data) {
      if (data && data.length !== 0) {
        this.isSaved = true;
        this.isEditable = false;
        this.uniqueSavedAddresses = data;
        const savedDataFlags = {
          isSaved: this.isSaved,
          isEditable: this.isEditable,
        };
        this.$emit('addressSaved', savedDataFlags);
      }
    },
    handleUseAddressResponse(response) {
      this.$refs.spinner.hideSpinner();
      this.restrictedItems = response.data.shippingDetails;
      if (this.restrictedItems.length === 0) {
        this.dismissFlyout();
        this.switchToSaveMode(this.uniqueSavedAddresses);
        if (response && this.useAddressClicked) {
          checkoutEventBus.$emit('use-this-address-clicked');
        }
        checkoutEventBus.$emit('update-cart');
      } else {
        this.restrictionError = true;
        this.allSelectedAddresses = [];
        this.showFlyout(
          'error',
          `${this.i18n.shippingAddress.flyoutRestrictionErrorPart1} ${
          this.restrictedItems[0].region.isocode
          }${this.i18n.shippingAddress.flyoutRestrictionErrorPart2}`,
          false,
        );
      }
      checkoutEventBus.$emit('restriction-error', this.restrictedItems);
    },
    handleUseAddressError(error) {
      this.$refs.spinner.hideSpinner();
    },
    // delete an item from cart request and response
    deleteCartItem(itemNumber, productCode, event) {
      this.deleteRestrictedProductCode = productCode;
      this.checkoutService.deleteCartItem({}, this.handleDeleteCartItemResponse, this.handleDeleteCartItemError, itemNumber);
      this.$refs.spinner.showSpinner();
    },
    handleDeleteCartItemResponse() {
      this.$refs.spinner.hideSpinner();
      checkoutEventBus.$emit('update-cart');
      this.restrictedItems.map((item, index) => {
        if (item.productCode === this.deleteRestrictedProductCode) {
          this.restrictedItems.splice(index, 1);
        }
      });
      if (this.restrictedItems.length === 0) {
        this.dismissFlyout();
      }
    },
    handleDeleteCartItemError() {
      this.$refs.spinner.hideSpinner();
    },
    splitShippingAddresses(data) {
      this.allSelectedAddresses = _.flattenDeep(this.allSelectedAddresses);
      this.uniqueSavedAddresses = _.uniqBy(this.allSelectedAddresses, 'id');
    },
    showSplitError(data) {
      if (data && data.quantityError) {
        this.quantityError = true;
        this.showFlyout('error', this.i18n.shippingAddress.quantityError, true);
      } else if (data && data.addressEmptyError) {
        this.addressEmptyError = true;
      } else if (data && data.splitToSameAddressError) {
        this.splitToSameAddressError = true;
        this.showFlyout('error', 'Splitted quantity  cannot be shipped to same address', true);
      }
    },
    // Address string formation
    formAddressValues(addressArray) {
      let addressObject = {};
      this.currentAvailableAddresses = [];
      if (addressArray.length !== 0) {
        for (let i = 0; i < addressArray.length; i += 1) {
          addressObject = {
            label: this.formAddressString(addressArray[i]),
            value: addressArray[i],
          };
          this.currentAvailableAddresses.push(addressObject);
        }
      }
      this.currentAvailableAddresses.sort((a, b) => {
        let x = a.value.line1.toLowerCase();
        let y = b.value.line1.toLowerCase();
        if (x < y) { return -1; }
        else if (x > y) { return 1; }
        else { return 0 };
      });
      for (let j = 0; j < this.currentAvailableAddresses.length; j += 1) {
        if (this.currentAvailableAddresses && this.currentAvailableAddresses.length !== 0 && this.currentAvailableAddresses[j].value && this.currentAvailableAddresses[j].value.defaultAddress) {
          this.defaultAddress = this.currentAvailableAddresses[j];
          this.currentAvailableAddresses.splice(this.currentAvailableAddresses.indexOf(this.defaultAddress), 1);
          this.currentAvailableAddresses.unshift(this.defaultAddress);
          break;
        }
      }
      return this.currentAvailableAddresses;
    },
    formAddressString(address) {
      let userAddress = '';
      let companyName = '';
      const name = address.firstName + ' ' + address.lastName;
      if (address && address.companyName) {
        companyName = address.companyName;
      }
      if (address) {
        userAddress = [
          name,
          companyName,
          address.line1,
          address.line2,
          address.town,
          address.region.isocodeShort,
          address.postalCode,
          address.country.isocode,
        ]
          .filter(Boolean)
          .join(', ');
      }
      return userAddress;
    },
  },
};
