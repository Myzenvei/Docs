import globals from '../globals';
import {
  ApplePay,
} from '../../common/mixins/vx-enums';
import {
  checkoutEventBus,
} from '../../../modules/event-bus';
import ApplePayService from '../services/apple-pay-service';


/* global ApplePaySession:true */
/* eslint no-undef: "error" */
const applePayMixin = {
  data() {
    return {
      isApplePayAvailable: false,
      applePayRequest: {},
      applePaySession: {},
      ApplePay,
      globals,
      applePayService: new ApplePayService(),
    };
  },
  computed: {
    /**
     * prepareApplePayLineItems pull required line items
     * infomration from chekcout data
     * @return {Object} Returns array of amounts for apply pay line items
     */
    prepareApplePayLineItems() {
      let lineItems = [];
      if (this.checkoutData.subTotal &&
        this.checkoutData.deliveryCost &&
        this.checkoutData.totalTax) {
        lineItems = [{
          label: this.ApplePay.labels.cartSubtotal,
          amount: this.checkoutData.subTotal.value,
        }, {
          label: this.ApplePay.labels.shipping,
          amount: this.checkoutData.deliveryCost.value,
        }, {
          label: this.ApplePay.labels.tax,
          amount: this.checkoutData.totalTax.value,
        }];
      }
      return lineItems;
    },
  },
  methods: {

    /**
     * Validates ApplePay availability and assings flag value to isApplePayAvailable
     */
    checkApplePayAvailability() {
      if (window.ApplePaySession) {
        this.isApplePayAvailable = ApplePaySession.canMakePayments();
      }
    },

    /**
     * Creates apple pay session object
     * @return {Object} Apple Pay request object
     */
    getApplePaySessionObj() {
      const applePayRequest = {
        countryCode: this.ApplePay.countryCode,
        currencyCode: this.ApplePay.currencyCode,
        supportedNetworks: this.ApplePay.supportedNetworks,
        merchantCapabilities: this.ApplePay.merchantCapabilities,
        requiredBillingContactFields: this.ApplePay.requiredBillingContactFields,
        total: {
          label: this.globals.getApplePayMerchantName(),
          amount: this.checkoutData.totalPriceWithTax.value,
          type: this.ApplePay.labels.paymentType,
        },
        lineItems: this.prepareApplePayLineItems, // Takes function
      };

      return applePayRequest;
    },

    /**
     * Begins Apple Pay session
     */
    startAppleSession() {
      // Create a ApplePay session
      this.applePaySession = new ApplePaySession(this.ApplePay.APIVersion, this.getApplePaySessionObj());
      // Begin the process
      try {
        this.applePaySession.begin();
      } catch (e) {
        checkoutEventBus.$emit('apple-pay-status', false);
      }

      this.paymentType = this.allPaymentTypes.applePay;
      // Hiding the Place Order button on apple pay initiation
      checkoutEventBus.$emit('update-payment-type', this.paymentType);
    },

    /**
     * Validates merchant infomration via hybris service call
     */
    validateMerchant() {
      // Validate merchant
      this.applePaySession.onvalidatemerchant = (event) => {
        // console.log('Validate merchant event Data', event);
        if (event.validationURL) {
          // service call to hybris
          // service needs to be updated
          this.applePayService.validateMerchant({}, (data) => {
            try {
              if (data.data) {
                // Pass token data recieved
                console.log('starting Payment session');
                this.applePaySession.completeMerchantValidation(data.data);
              }
            } catch (e) {
              this.cancelPaymentSession();
            }
          }, () => {
            if (this.applePaySession) {
              this.cancelPaymentSession();
            }
          }, event.validationURL);
        } else {
          this.cancelPaymentSession();
        }
      };
    },

    /**
     * Authorize ApplePay
     */
    authorizeApplePay() {
      // Authorize payment and completePayment
      this.applePaySession.onpaymentauthorized = (event) => {
        if (event.payment) {
          const authorisePaymentObj = event.payment;
          const requestConfig = {};
          // sending paymentToken with billing address as well
          requestConfig.data = authorisePaymentObj;
          if (requestConfig.data.token && requestConfig.data.token.paymentData && btoa) {
            requestConfig.data.token.paymentData = window.btoa(JSON.stringify(requestConfig.data.token.paymentData));
          } else {
            this.cancelPaymentSession();
            return;
          }
          this.$refs.spinner.showSpinner();
          this.applePayService.saveApplePayResponse(requestConfig, (response) => {
            console.log('Sending payment authorization to Apple pay');
            // response data is returned from hybris, we need to check for success condition in data
            if (response && response.data) {
              this.applePaySession.completePayment(ApplePaySession.STATUS_SUCCESS);
              checkoutEventBus.$emit('apple-pay-status', response);
            } else {
              if (this.applePaySession) {
                this.cancelPaymentSession();
              }
              this.$refs.spinner.hideSpinner();
            }
            this.$refs.spinner.hideSpinner();
          }, (response) => {
            if (response) {
              console.log(response.data);
            }
            this.cancelPaymentSession();
          });
        } else {
          this.cancelPaymentSession();
        }
      };
    },

    /**
     * Cancels Payment and displays Error message
     */
    cancelPaymentSession() {
      this.$refs.spinner.hideSpinner();
      if (this.applePaySession) {
        this.applePaySession.abort();
        checkoutEventBus.$emit('apple-pay-status', false);
        checkoutEventBus.$emit('payment-method-default');
      }
    },

    /**
     * Handles the apple pay cancellation
     */
    handleCancelPayment() {
      this.applePaySession.oncancel = () => {
        checkoutEventBus.$emit('payment-method-default');
      };
    },
    /**
     * Completes ApplePay process methods
     */
    initiateApplePay() {
      // Unselect all other payment methods
      checkoutEventBus.$emit('payment-method-unselect');

      this.startAppleSession();
      this.validateMerchant();
      this.authorizeApplePay();
      this.handleCancelPayment();
    },
  },
  created() {
    this.checkApplePayAvailability();
  },
};

export default applePayMixin;
