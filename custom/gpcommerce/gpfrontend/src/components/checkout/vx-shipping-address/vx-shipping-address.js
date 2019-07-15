/**
 * Handles the shipping address functionality
 */
import _ from 'lodash';
import vxMultiShippingAddress from '../vx-multi-shipping-address/vx-multi-shipping-address.vue';
import vxSingleShippingAddress from '../vx-single-shipping-address/vx-single-shipping-address.vue';
import globals from '../../common/globals';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import CheckoutService from '../../common/services/checkout-service';

// import savedAddresses from './vx-shipping-address-mock';
// import { cartEventBus } from '../../../modules/event-bus';
import {
  checkoutEventBus
} from '../../../modules/event-bus';

export default {
  name: 'vx-shipping-address',
  components: {
    vxMultiShippingAddress,
    vxSingleShippingAddress,
    vxSpinner,
  },
  props: {
    /**
     * Labels, button and caption texts
     */
    i18n: Object,
    /**
     * Saved addresses of user
     */
    visibleEntries: {
      type: Array,
    },
    userLevel: Boolean,
    sectionIndex: Number,
    /**
     * All the cart entries
     */
    allEntries: {
      type: Array,
    },
  },
  data() {
    return {
      globals,
      checkoutService: new CheckoutService(),
      isSingle: true,
      isMultiple: false,
      isSaved: false,
      isEditable: true,
      savedAddresses: [],
      existingSavedAddresses: [],
      dataLoaded: false,
      loadedflag: false,
      isGuest: false,
      isPalletShipment: false,
      isNewProductAdded: false,
      isShippingMultiple: true,
      disableShipToMultiple: false,
    };
  },
  computed: {
    cartHasBundle() {
      return this.visibleEntries.filter(product => product.hasOwnProperty('rootBundleTemplate')).length;
    },
  },
  // need to remove mock
  mounted() {
    this.loadedflag = true;
    if (this.globals.getIsLoggedIn()) {
      this.getAddressesRequest();
    } else {
      this.isGuest = true;
      this.getGuestAddressesRequest();
    }
    this.palletShipment();
  },
  updated() {
    if (this.loadedflag) {
      this.isAddressExists();
    }
  },
  methods: {
    // get all addresses generate request
    isAddressExists() {
      const splitEntries = [];
      this.existingSavedAddresses = [];
      this.allEntries.map((entry) => {
        if (entry.splitEntries && entry.splitEntries.length === 0) {
          this.isNewProductAdded = true;
          this.isEditable = true;
        }
      });
      for (let i = 0; i < this.visibleEntries.length; i += 1) {
        if (
          this.isNewProductAdded &&
          this.visibleEntries[0] &&
          this.visibleEntries[0].splitEntries &&
          this.visibleEntries[0].splitEntries.length !== 0 &&
          this.visibleEntries[0].splitEntries[0] &&
          this.visibleEntries[0].splitEntries[0].deliveryAddress
        ) {
          const addressToBePopulated = {
            label: this.$refs.singleShipping.formAddressString(
              this.visibleEntries[0].splitEntries[0].deliveryAddress,
            ),
            value: this.visibleEntries[0].splitEntries[0].deliveryAddress,
          };
          this.$refs.singleShipping.selectedAddress(addressToBePopulated);
          this.$refs.singleShipping.populateStoredAddress();
          break;
        }
        for (
          let k = 0; k < this.visibleEntries[i].splitEntries.length; k += 1
        ) {
          splitEntries.push(this.visibleEntries[i].splitEntries[k]);
        }
      }
      if (splitEntries.length === 0) {
        this.isEditable = true;
      } else if (!this.isNewProductAdded) {
        for (let j = 0; j < splitEntries.length; j += 1) {
          this.existingSavedAddresses.push(splitEntries[j].deliveryAddress);
        }
        this.existingSavedAddresses = _.uniqBy(
          this.existingSavedAddresses,
          'id',
        );
        this.$refs.singleShipping.showExistingAddresses(
          this.existingSavedAddresses,
        );
      }
      this.loadedflag = false;
    },
    palletShipment() {
      for (let i = 0; i < this.visibleEntries.length; i += 1) {
        if (
          this.visibleEntries[i].product &&
          this.visibleEntries[i].product.isPallet
        ) {
          this.isPalletShipment = true;
          break;
        } else {
          this.isPalletShipment = false;
        }
      }
    },
    getAddressesRequest() {
      this.checkoutService.getCheckoutAddress({}, this.handleGetAddressResponse, this.handleGetAddressError);
      this.$refs.spinner.showSpinner();
    },
    getGuestAddressesRequest() {
      this.checkoutService.getGuestAddress({}, this.handleGetAddressResponse, this.handleGetAddressError);
      this.$refs.spinner.showSpinner();
    },
    // get all addresses response
    handleGetAddressResponse(response) {
      this.$refs.spinner.hideSpinner();
      this.savedAddresses = response.data.addresses;
      // disable ship to multiple link if the user is L2/L3 and doesnt have any saved address
      if (!this.userLevel && this.savedAddresses.length === 0) {
        this.disableShipToMultiple = true;
      }
      this.dataLoaded = true;
      if (this.dataLoaded) {
        if (this.isMultiple) {
          this.$nextTick(() => {
            this.$refs.multiShipping.getAddressValues(this.savedAddresses);
          });
          checkoutEventBus.$emit('shipping-multiple');
        } else {
          this.$nextTick(() => {
            this.$refs.singleShipping.getAddressValues(this.savedAddresses);
          });
          checkoutEventBus.$emit('shipping-single');
        }
      }
    },
    // Error on get addresses response
    handleGetAddressError(error) {
      this.$refs.spinner.hideSpinner();
    },
    onAddressSaved(data) {
      this.saveMethod(data);
    },
    toggleMethodType(event) {
      this.isSingle = !this.isSingle;
      this.isMultiple = !this.isMultiple;
      if (this.isMultiple) {
        checkoutEventBus.$emit('shipping-multiple');
      }
      if (this.isSingle) {
        checkoutEventBus.$emit('shipping-single');
      }
    },
    editMethod(event) {
      if (this.globals.getIsLoggedIn()) {
        this.getAddressesRequest();
      } else {
        this.isGuest = true;
        this.getGuestAddressesRequest();
      }
      this.isSaved = !this.isSaved;
      this.isEditable = !this.isEditable;
      this.existingAddresses = [];
      checkoutEventBus.$emit('section-edit', 'shippingAddress');
      checkoutEventBus.$emit('shipping-address-edit');
      if (this.isSingle) {
        this.$refs.singleShipping.toggleEditMode();
        checkoutEventBus.$emit('shipping-single');
      } else if (this.isMultiple) {
        this.$refs.multiShipping.toggleEditMode();
        checkoutEventBus.$emit('shipping-multiple');
      }
    },
    saveMethod(data) {
      this.isSaved = data.isSaved;
      this.isEditable = data.isEditable;
      checkoutEventBus.$emit('section-complete', 'shippingAddress');
      checkoutEventBus.$emit('shipping-address-added');
    },
    addNewAddressSingle(event) {
      this.$refs.singleShipping.addNewAddress(event);
    },
    addNewAddressMultiple(event) {
      this.$refs.multiShipping.addNewAddress();
    },
    findShippingType(val) {
      this.isShippingMultiple = val;
    },
  },
};
