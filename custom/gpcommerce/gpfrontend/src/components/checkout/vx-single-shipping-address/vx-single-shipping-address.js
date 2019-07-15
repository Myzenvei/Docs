import vxAddEditAddress from '../../common/vx-add-edit-address/vx-add-edit-address.vue';
import vxDropdownPrimary from '../../common/vx-dropdown-primary/vx-dropdown-primary.vue';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import globals from '../../common/globals';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import vxProductTile from '../../common/vx-product-tile/vx-product-tile.vue';
import flyoutBannerMixin from '../../common/vx-flyout-banner/vx-flyout-banner-mixin';
import CheckoutService from '../../common/services/checkout-service';
import detectDeviceMixin from '../../common/mixins/detect-device-mixin';

import {
  myAccountAddressStatus,
} from '../../common/mixins/vx-enums';
import {
  checkoutEventBus,
  eventBus,
  globalEventBus,
} from '../../../modules/event-bus';

export default {
  name: 'vx-single-shipping-address',
  mixins: [flyoutBannerMixin, detectDeviceMixin],
  components: {
    vxAddEditAddress,
    vxDropdownPrimary,
    vxModal,
    vxSpinner,
    vxProductTile,
  },
  props: {
    userAddresses: {
      type: Array,
    },
    i18n: Object,
    isGuest: Boolean,
    visibleEntries: {
      type: Array,
    },
    userLevel: Boolean,
    palletShipment: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      globals,
      checkoutService: new CheckoutService(),
      savedAddress: {},
      dropdownError: false,
      currentAvailableAddresses: [],
      allUserSavedAddresses: [],
      deleteRestrictedProductCode: '',
      shippingDetails: [],
      shippingAddress: [],
      addressAvailable: true,
      restrictedItems: [],
      formattedEntries: {},
      isSaved: false,
      isEditable: true,
      saveAddressResponse: {},
      restrictionError: false,
      contactUsUrl: '',
      existingAddresses: [],
      useAddressClicked: false,
      modalScrollable: true,
      defaultAddress: {},
      deliveryId: '',
    };
  },
  computed: {},
  mounted() {
    this.$refs.addressDropdown.setDropdownLabel(this.i18n.shippingAddress.dropdownLabel);
    this.formattedEntries = this.arrayToObject(this.visibleEntries, 'code');
    // need to see if any address is available for user or not
    if (this.userAddresses && this.visibleEntries && this.userAddresses.length !== 0) {
      this.allUserSavedAddresses = this.formAddressValues(this.userAddresses);
      if (this.defaultAddress && this.defaultAddress.value && this.defaultAddress.label) {
        this.savedAddress = this.defaultAddress;
        this.$refs.addressDropdown.setDropdownLabel(this.savedAddress.label);
      }
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
    // selected shipping address of the user
    selectedAddress(data) {
      this.savedAddress = data;
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
    // form address object with label as address string and value as address object
    formAddressValues(addressArray) {
      this.currentAvailableAddresses = [];
      let addressObject = {};
      if (addressArray && addressArray.length !== 0) {
        for (let i = 0; i < addressArray.length; i += 1) {
          addressObject = {
            label: this.formAddressString(addressArray[i]),
            value: addressArray[i],
          };
          this.currentAvailableAddresses.push(addressObject);
        }
        this.currentAvailableAddresses.sort((a, b) => {
          let x = a.value.line1.toLowerCase();
          let y = b.value.line1.toLowerCase();
          if (x < y) {
            return -1;
          } else if (x > y) {
            return 1;
          } else {
            return 0
          };
        });
        for (let j = 0; j < this.currentAvailableAddresses.length; j += 1) {
          if (this.currentAvailableAddresses && this.currentAvailableAddresses.length !== 0 && this.currentAvailableAddresses[j].value && this.currentAvailableAddresses[j].value.defaultAddress) {
            this.defaultAddress = this.currentAvailableAddresses[j];
            this.currentAvailableAddresses.splice(this.currentAvailableAddresses.indexOf(this.defaultAddress), 1);
            this.currentAvailableAddresses.unshift(this.defaultAddress);
            break;
          }
        }
      }
      return this.currentAvailableAddresses;
    },

    // address string formation to display in dropdown
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
    addNewAddress(event) {
      this.$refs.addNewAddressModal.open(event);
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
      this.dropdownError = false;
      this.deliveryId = response.data.id;
      if (!this.isGuest) {
        this.$emit('call-get-addresses');
      } else {
        this.$emit('call-guest-get-addresses');
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
        this.$nextTick(() => {
          this.$refs.addressDropdown.setDropdownLabel(this.savedAddress.label);
          this.deliveryId = '';
        });
      }
      this.allUserSavedAddresses = this.formAddressValues(this.userAddresses);
    },
    toggleEditMode() {
      this.isSaved = false;
      this.isEditable = true;
      this.dropdownError = false;
      this.shippingAddress = [];
      this.shippingDetails = [];
    },
    populateStoredAddress() {
      if (this.savedAddress && this.savedAddress.label) {
        this.$refs.addressDropdown.setDropdownLabel(this.savedAddress.label);
      }
    },
    toggleViewMode(data, event) {
      if (data.label && data.value) {
        this.dropdownError = false;
        this.savedAddress = data;

        this.checkoutService.shippingRestriction({}, this.handleUseAddressResponse, this.handleUseAddressError, this.saveRequestData());


        if (event && this.visibleEntries[0].splitEntries[0] && this.visibleEntries[0].splitEntries[0].deliveryAddress) {
          this.useAddressClicked = true;
        } else {
          this.useAddressClicked = false;
        }
        this.$refs.spinner.showSpinner();
      } else {
        this.dropdownError = true;
        globalEventBus.$emit('announce', this.i18n.shippingAddress.enterAddress);
      }
    },
    // Request Data Formation
    saveRequestData() {
      // shipping details request
      this.shippingDetails = [];
      this.shippingAddress = [];
      const splitEntry = [];
      for (let i = 0; i < this.visibleEntries.length; i += 1) {
        this.shippingDetails.push({
          productCode: this.visibleEntries[i].product.code,
          country: this.savedAddress.value.country,
          region: this.savedAddress.value.region,
        });

        // shipping addresses request

        splitEntry.push({
          entryNumber: this.visibleEntries[i].entryNumber,
          splitEntry: [{
            deliveryAddress: {
              id: this.savedAddress.value.id,
            },
            qty: this.visibleEntries[i].quantity,
            productCode: this.visibleEntries[i].product.code,
            price: this.visibleEntries[i].basePrice.value,
          }, ],
        });
      }
      this.shippingAddress.push({
        splitEntry,
      });
      const requestdata = {
        shippingDetails: this.shippingDetails,
        shippingAddress: this.shippingAddress,
      };
      return requestdata;
    },
    switchToSaveMode(data) {
      if (data.label !== '' && data.value) {
        this.savedAddress = data;
        this.isSaved = true;
        this.isEditable = false;
        const savedDataFlags = {
          isSaved: this.isSaved,
          isEditable: this.isEditable,
        };
        this.$emit('addressSaved', savedDataFlags);
      }
    },

    switchToMultiAddressSaveMode(data) {
      this.existingAddresses = data;
      this.isSaved = true;
      this.isEditable = false;
      const savedDataFlags = {
        isSaved: this.isSaved,
        isEditable: this.isEditable,
      };
      this.$emit('addressSaved', savedDataFlags);
    },
    // On successful response of saving the address we need to switch to read only mode
    handleUseAddressResponse(response) {
      this.$refs.spinner.hideSpinner();
      this.restrictedItems = response.data.shippingDetails;
      this.existingAddresses = [];
      this.existingAddresses.push(this.savedAddress);
      if (this.restrictedItems.length === 0) {
        this.dismissFlyout();
        this.switchToSaveMode(this.savedAddress);
        if (response && this.useAddressClicked) {
          checkoutEventBus.$emit('use-this-address-clicked');
        }
        checkoutEventBus.$emit('update-cart');
      } else {
        this.restrictionError = true;
        this.showFlyout(
          'error',
          `${this.i18n.shippingAddress.flyoutRestrictionErrorPart1} ${
          this.restrictedItems[0].country.isocode
          }/${this.restrictedItems[0].region.isocode} ${
          this.i18n.shippingAddress.flyoutRestrictionErrorPart2
          }`,
          false,
        );
      }
      checkoutEventBus.$emit('restriction-error', this.restrictedItems);
      this.$emit('is-shipping-multiple', false);
    },
    // On Error we need to display that error
    handleUseAddressError() {
      this.$refs.spinner.hideSpinner();
    },
    navigateToContactUsPage(event) {
      this.globals.navigateToUrl('contactUs');
    },
    arrayToObject(array, keyField) {
      const objt = array.reduce((obj, item) => {
        obj[item.product[keyField]] = item;
        return obj;
      }, {});
      return objt;
    },
    showExistingAddresses(data) {
      this.existingAddresses = this.formAddressValues(data);
      // For b2c if user deletes saved address
      const self = this;
      const addressAvailable = self.userAddresses.filter(function(address) {
        return address.id === self.existingAddresses[0].value.id;
      });

      if (this.existingAddresses.length === 1 && this.existingAddresses[0].value.approvalStatus.toUpperCase() === myAccountAddressStatus.active && addressAvailable.length !== 0) {
        this.toggleViewMode(this.existingAddresses[0]);
        this.switchToSaveMode(this.existingAddresses[0]);
        checkoutEventBus.$emit('shipping-single');
        this.$emit('is-shipping-multiple', false);
      } else if (this.existingAddresses.length === 1 && (this.existingAddresses[0].value.approvalStatus.toUpperCase() !== myAccountAddressStatus.active || addressAvailable.length === 0)) {
        this.switchToSaveMode(this.existingAddresses);
        checkoutEventBus.$emit('shipping-single');
        this.$emit('is-shipping-multiple', false);
      } else {
        // In multiple shipping if user deletes selected address will switch back to edit mode.
        let addressInactive = false;
        for (let i = 0; i < this.existingAddresses.length; i += 1) {
          addressInactive = false;
          if (this.existingAddresses[i].value.approvalStatus.toUpperCase() !== myAccountAddressStatus.active) {
            addressInactive = true;
            break;
          }
        }
        checkoutEventBus.$emit('shipping-multiple');
        if (!addressInactive) {
          this.switchToMultiAddressSaveMode(this.existingAddresses);
        } else {
          this.toggleEditMode();
        }
      }
    },
  },
  watch: {
    visibleEntries() {
      this.formattedEntries = this.arrayToObject(this.visibleEntries, 'code');
    },
  },
};
