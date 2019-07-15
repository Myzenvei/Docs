import CheckOutService from '../../common/services/checkout-service';
import vxDropdownPrimary from '../../common/vx-dropdown-primary/vx-dropdown-primary.vue';
import ManageSubscriptionService from '../../common/services/manage-subscription-service';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import globals from '../../common/globals';
import vxProductTile from '../../common/vx-product-tile/vx-product-tile.vue';
import { ProductAvailability } from '../../common/mixins/vx-enums';

export default {
  name: 'vx-edit-subscription',
  components: {
    vxDropdownPrimary,
    vxSpinner,
    vxProductTile,
  },
  props: {
    i18n: {
      type: Object,
      value: {},
    },
    subscriptionEntry: {
      type: Object,
      value: {},
    },
  },
  data() {
    return {
      checkoutService: new CheckOutService(),
      manageSubscriptionService: new ManageSubscriptionService(),
      savedAddresses: '',
      paymentDetails: '',
      isAddressEditable: true,
      isPaymentEditable: true,
      currentAvailableAddresses: [],
      allUserSavedAddresses: [],
      allUserSavedPayments: [],
      defaultAddress: {},
      currentAvailablePayment: [],
      defaultPayment: {},
      globals,
      ProductAvailability,
      existingAddress: {},
      existingPayment: {},
      selectedAddress: {},
      selectedPayment: {},
    };
  },
  computed: {},
  mounted() {
    this.existingAddress = this.subscriptionEntry.entries[0].splitEntries[0].deliveryAddress;
    this.existingPayment = this.subscriptionEntry.hasOwnProperty('payment')
      ? this.subscriptionEntry.payment
      : {};
  },
  methods: {
    getAddressesRequest() {
      this.checkoutService.getCheckoutAddress(
        {},
        this.handleGetAddressResponse,
        this.handleGetAddressError,
      );
      this.$refs.editSubscriptionSpinner.showSpinner();
    },
    handleGetAddressResponse(response) {
      this.$refs.editSubscriptionSpinner.hideSpinner();
      if (response && response.data) {
        this.savedAddresses = response.data.addresses;
        this.allUserSavedAddresses = this.formAddressValues(
          this.savedAddresses,
        );
        if (
          this.$refs.addressDropdown &&
          this.allUserSavedAddresses &&
          this.allUserSavedAddresses.length > 0 &&
          this.allUserSavedAddresses[0].label
        ) {
          this.$refs.addressDropdown.setDropdownLabel(
            this.allUserSavedAddresses[0].label,
          );
        }
      }
    },
    // Error on get addresses response
    handleGetAddressError(error) {
      this.$refs.editSubscriptionSpinner.hideSpinner();
    },
    getPaymentDetails() {
      this.checkoutService.getPaymentDetails(
        {},
        this.handleGetPaymentDetailsResponse,
        this.handleGetPaymentDetailsError,
      );
      this.$refs.editSubscriptionSpinner.showSpinner();
    },
    handleGetPaymentDetailsResponse(response) {
      this.$refs.editSubscriptionSpinner.hideSpinner();
      if (response && response.data) {
        this.paymentDetails = response.data.payments;
        this.allUserSavedPayments = this.formPaymentValues(this.paymentDetails);
        if (
          this.$refs.paymentDropdown &&
          this.allUserSavedPayments &&
          this.allUserSavedPayments.length > 0 &&
          this.allUserSavedPayments[0].label
        ) {
          if (
            this.existingPayment &&
            this.existingPayment.hasOwnProperty('accountHolderName')
          ) {
            this.$refs.paymentDropdown.setDropdownLabel(
              this.allUserSavedPayments[0].label,
            );
          } else {
            this.$refs.paymentDropdown.setDropdownLabel('Select option');
          }
        }
      }
    },
    handleGetPaymentDetailsError() {
      this.$refs.editSubscriptionSpinner.hideSpinner();
    },
    editAddressMethod(event) {
      this.isAddressEditable = !this.isAddressEditable;
      this.getAddressesRequest();
    },
    editPaymentMethod(event) {
      this.isPaymentEditable = !this.isPaymentEditable;
      this.getPaymentDetails();
    },
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
        this.currentAvailableAddresses.sort((d1, d2) => {
          let x = new Date(d1.value.creationTime).getTime(),
            y = new Date(d2.value.creationTime).getTime();
          return y - x;
        });
        for (let j = 0; j < this.currentAvailableAddresses.length; j += 1) {
          if (
            this.currentAvailableAddresses &&
            this.currentAvailableAddresses.length !== 0 &&
            this.currentAvailableAddresses[j].value &&
            this.currentAvailableAddresses[j].value.defaultAddress
          ) {
            this.defaultAddress = this.currentAvailableAddresses[j];
            this.currentAvailableAddresses.splice(
              this.currentAvailableAddresses.indexOf(this.defaultAddress),
              1,
            );
            this.currentAvailableAddresses.unshift(this.defaultAddress);
            break;
          }
        }
        if (this.existingAddress && this.existingAddress.id) {
          let matchedAddressObj = {};
          this.currentAvailableAddresses.map((address) => {
            if (address.value.id === this.existingAddress.id) {
              matchedAddressObj = address;
            }
          });
          this.currentAvailableAddresses.splice(
            this.currentAvailableAddresses.indexOf(matchedAddressObj),
            1,
          );
          this.currentAvailableAddresses.unshift(matchedAddressObj);
        }
      }
      return this.currentAvailableAddresses;
    },
    formPaymentValues(paymentArray) {
      this.currentAvailablePayment = [];
      let paymentObject = {};
      if (paymentArray && paymentArray.length !== 0) {
        for (let i = 0; i < paymentArray.length; i += 1) {
          paymentObject = {
            label: this.formPaymentString(paymentArray[i]),
            value: paymentArray[i],
          };
          this.currentAvailablePayment.push(paymentObject);
        }
        for (let j = 0; j < this.currentAvailablePayment.length; j += 1) {
          if (
            this.currentAvailablePayment &&
            this.currentAvailablePayment.length !== 0 &&
            this.currentAvailablePayment[j].value &&
            this.currentAvailablePayment[j].value.defaultPayment
          ) {
            this.defaultPayment = this.currentAvailablePayment[j];
            this.currentAvailablePayment.splice(
              this.currentAvailablePayment.indexOf(this.defaultPayment),
              1,
            );
            this.currentAvailablePayment.unshift(this.defaultPayment);
            break;
          }
        }
        if (this.existingPayment && this.existingPayment.id) {
          let matchedPaymentObj = {};
          this.currentAvailablePayment.map((payment) => {
            if (payment.value.id === this.existingPayment.id) {
              matchedPaymentObj = payment;
            }
          });
          this.currentAvailablePayment.splice(
            this.currentAvailablePayment.indexOf(matchedPaymentObj),
            1,
          );
          this.currentAvailablePayment.unshift(matchedPaymentObj);
        }
      }
      return this.currentAvailablePayment;
    },

    formAddressString(address) {
      let userAddress = '';
      let companyName = '';
      const name = `${address.firstName} ${address.lastName}`;
      if (address && address.companyName) {
        companyName = address.companyName;
      }
      const stateZip = `${address.country.isocode} ${address.postalCode}`;
      if (address) {
        userAddress = [name, companyName, address.line1, address.town, stateZip]
          .filter(Boolean)
          .join(',');
      }
      return userAddress;
    },
    formPaymentString(payment) {
      let paymentString = '';
      let cardName = '';
      const cardExpiry = `${payment.expiryMonth}/${payment.expiryYear}`;
      if (payment.cardType && payment.cardType.name) {
        cardName = payment.cardType.name;
      }
      if (payment) {
        paymentString = [
          cardName,
          payment.cardNumber,
          cardExpiry,
          payment.accountHolderName,
        ]
          .filter(Boolean)
          .join(',');
      }
      return paymentString;
    },
    handleChangeAddress(event) {
      this.selectedAddress = event.value;
    },
    handleChangePayment(event) {
      this.selectedPayment = event.value;
    },
    saveChangedSubscription(event) {
      this.$refs.editSubscriptionSpinner.showSpinner();
      const requestObj = {
        code: this.subscriptionEntry.code,
        address: this.selectedAddress,
        paymentDetails: this.selectedPayment,
      };
      const requestConfig = {};
      requestConfig.data = requestObj;
      this.manageSubscriptionService.saveSubscriptionInformation(
        requestConfig,
        this.saveSubscriptionInformationResponse,
        this.saveSubscriptionInformationError,
        this.subscriptionEntry.code,
      );
    },
    saveSubscriptionInformationResponse(response) {
      if (
        this.selectedPayment.hasOwnProperty('id') &&
        this.allUserSavedPayments &&
        this.allUserSavedPayments.length > 0 &&
        this.selectedPayment.id !== this.allUserSavedPayments[0].value.id
      ) {
        const requestConfig = {};
        this.manageSubscriptionService.placeSubscriptionOrder(
          requestConfig,
          this.placeSubscriptionOrderResponse,
          this.placeSubscriptionOrderError,
          this.subscriptionEntry.code,
        );
      } else {
        this.$refs.editSubscriptionSpinner.hideSpinner();
        this.$emit('updated-subscription');
      }
    },

    saveSubscriptionInformationError(error) {
      this.$refs.editSubscriptionSpinner.hideSpinner();
    },

    placeSubscriptionOrderResponse(response) {
      this.$refs.editSubscriptionSpinner.hideSpinner();
      this.$emit('updated-subscription');
    },
    placeSubscriptionOrderError(error) {
      this.$refs.editSubscriptionSpinner.hideSpinner();
    },
  },
};
