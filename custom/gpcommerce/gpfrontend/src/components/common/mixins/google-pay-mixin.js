import { GooglePay } from '../../common/mixins/vx-enums';
import globals from '../globals';
import { checkoutEventBus } from '../../../modules/event-bus';
import GooglePayService from '../services/google-pay-service';

const googlePayMixin = {
  created() {
    // Loading  Library Js script tag
    const googleJS = document.createElement('script');
    googleJS.setAttribute('src', this.globals.googlePayScriptURL);
    googleJS.onload = this.onGooglePayLoaded;
    document.head.appendChild(googleJS);
  },

  mounted() {
    checkoutEventBus.$on('payment-method-edit', () => {
      this.isUsingStoredCard = false;
      this.onGooglePayLoaded();

    });
    checkoutEventBus.$on('payment-method-added', () => {
      this.isUsingStoredCard = true;
    });
  },

  data() {
    return {
      globals,
      isGooglePayAvailable: true,
      googlePayService: new GooglePayService(),
      hybrisResponseData: {},
      GooglePay,
      isUsingStoredCard: false,
      environment: '',
      // TODO: Needs to be dynamically pulled from Hybris
      baseRequest: {
        apiVersion: '',
        apiVersionMinor: '',
        emailRequired: true,
      },
      baseCardPaymentData: {
        type: '',
        parameters: {
          allowedAuthMethods: '',
          allowedCardNetworks: '',
          billingAddressRequired: false,
          billingAddressParameters: {
            format: '',
            phoneNumberRequired: false,
          },
        },
      },
      cardPaymentData: Object.assign(
        {
          tokenizationSpecification: {
            type: '',
            parameters: {
              gateway: '',
              gatewayMerchantId: '',
            },
          },
        },
        {
          type: '',
          parameters: {
            allowedAuthMethods: '',
            allowedCardNetworks: '',
            billingAddressRequired: false,
            billingAddressParameters: {
              format: '',
              phoneNumberRequired: false,
            },
          },
        },
      ),


      paymentsClient: null,

    };
  },

  methods: {
    /**
     * Configure your site's support for payment methods supported by the Google Pay
     * API.
     *
     * Each member of allowedPaymentMethods should contain only the required fields,
     * allowing reuse of this base request when determining a viewer's ability
     * to pay and later requesting a supported payment method
     *
     * @returns {object} Google Pay API version, payment methods supported by the site
     */
    getGoogleIsReadyToPayRequest() {
      return Object.assign({}, this.baseRequest, {
        allowedPaymentMethods: [this.baseCardPaymentData],
      });
    },

    /**
     * Configure support for the Google Pay API
     *
     * @see {@link https://developers.google.com/pay/api/web/reference/object#PaymentDataRequest|PaymentDataRequest}
     * @returns {object} PaymentDataRequest fields
     */
    getGooglePaymentDataRequest() {
      const paymentDataRequest = Object.assign({}, this.baseRequest);
      paymentDataRequest.allowedPaymentMethods = [this.cardPaymentData];
      paymentDataRequest.transactionInfo = this.getGoogleTransactionInfo();
      let merchName = '';
      let merchID = '';

      if (this.hybrisResponseData) {
        if (this.hybrisResponseData.merchantInfo) {
          if (this.hybrisResponseData.merchantInfo.merchantName) {
            merchName = this.hybrisResponseData.merchantInfo.merchantName;
          }
          if (this.hybrisResponseData.merchantInfo.merchantID) {
            merchID = this.hybrisResponseData.merchantInfo.merchantID;
          }
        }

      }
      paymentDataRequest.merchantInfo = {
        // @todo a merchant ID is available for a production environment after approval by Google
        // See {@link https://developers.google.com/pay/api/web/guides/test-and-deploy/integration-checklist|Integration checklist}

        merchantId: merchID,
        merchantName: merchName,
      };
      return paymentDataRequest;
    },

    /**
     * Return an active PaymentsClient or initialize
     *
     * @see {@link https://developers.google.com/pay/api/web/reference/client#PaymentsClient|PaymentsClient constructor}
     * @returns {google.payments.api.PaymentsClient} Google Pay API client
     */
    getGooglePaymentsClient() {
      if (!this.paymentsClient) {
        this.paymentsClient = new google.payments.api.PaymentsClient({
          environment: this.environment,
        });
      }
      return this.paymentsClient;
    },

    /**
     * Initialize Google PaymentsClient after Google-hosted JavaScript has loaded
     *
     * Display a Google Pay payment button after confirmation of the viewer's
     * ability to pay.
     */
    onGooglePayLoaded() {
      const requestConfig = {};
      if (!this.isUsingStoredCard) {
        this.$refs.spinner.showSpinner();
        this.googlePayService.buildGooglePayRequest(requestConfig, hybrisResponse => {
          // data is returned from hybris, populates variables

          if (hybrisResponse.data) {
            let params = {};
            let tokenSpec = {};
            let that = this;
            this.hybrisResponseData = hybrisResponse.data;

            // console.log(this.hybrisResponseData)

            if (this.hybrisResponseData.apiVersion) {
              this.baseRequest.apiVersion = Number(this.hybrisResponseData.apiVersion);
            }
            if (this.hybrisResponseData.apiVersionMinor) {
              this.baseRequest.apiVersionMinor = Number(this.hybrisResponseData.apiVersionMinor);
            }

            if (this.hybrisResponseData.environment) {
              this.environment = this.hybrisResponseData.environment;
            }
            if (this.hybrisResponseData.allowedPaymentMethods) {
              if (this.hybrisResponseData.allowedPaymentMethods.type) {
                this.baseCardPaymentData.type = this.hybrisResponseData.allowedPaymentMethods.type;
                this.cardPaymentData.type = this.hybrisResponseData.allowedPaymentMethods.type;
              }

              if (this.hybrisResponseData.allowedPaymentMethods.parameters) {
                params = this.hybrisResponseData.allowedPaymentMethods.parameters;
                if (params.allowedAuthMethods) {
                  this.baseCardPaymentData.parameters.allowedAuthMethods = params.allowedAuthMethods.split(",");
                  this.cardPaymentData.parameters.allowedAuthMethods = params.allowedAuthMethods.split(",");
                }
                if (params.allowedCardNetworks) {
                  this.baseCardPaymentData.parameters.allowedCardNetworks = this.GooglePay.labels.allowedCardNetworks;
                  this.cardPaymentData.parameters.allowedCardNetworks = this.GooglePay.labels.allowedCardNetworks;
                  //TODO: Needs to be pulled from hybris response
                }
                if (params.billingAddressRequired) {
                  this.baseCardPaymentData.parameters.billingAddressRequired = params.billingAddressRequired;
                  this.cardPaymentData.parameters.billingAddressRequired = params.billingAddressRequired;
                }
                if (params.billingAddressParameters) {
                  if (params.billingAddressParameters.format) {
                    this.baseCardPaymentData.parameters.billingAddressParameters.format = params.billingAddressParameters.format;
                    this.cardPaymentData.parameters.billingAddressParameters.format = params.billingAddressParameters.format;

                  }
                  if (params.billingAddressParameters.phoneNumberRequired) {
                    this.baseCardPaymentData.parameters.billingAddressParameters.phoneNumberRequired = params.billingAddressParameters.phoneNumberRequired;
                    this.cardPaymentData.parameters.billingAddressParameters.phoneNumberRequired = params.billingAddressParameters.phoneNumberRequired;
                  }
                }
              }
              if (this.hybrisResponseData.allowedPaymentMethods.tokenizationSpecification) {
                tokenSpec = this.hybrisResponseData.allowedPaymentMethods.tokenizationSpecification;
                if (tokenSpec.type) {
                  this.cardPaymentData.tokenizationSpecification.type = tokenSpec.type;
                }
                if (tokenSpec.parameters) {
                  if (tokenSpec.parameters.gateway) {
                    this.cardPaymentData.tokenizationSpecification.parameters.gateway = tokenSpec.parameters.gateway;
                  }
                  if (tokenSpec.parameters.gatewayMerchantId) {
                    this.cardPaymentData.tokenizationSpecification.parameters.gatewayMerchantId = tokenSpec.parameters.gatewayMerchantId;
                  }
                }
              }
            };
            this.paymentsClient = this.getGooglePaymentsClient();
            // console.log(JSON.stringify(this.getGoogleIsReadyToPayRequest()))

            this.paymentsClient
              .isReadyToPay(this.getGoogleIsReadyToPayRequest())
              .then(function (response) {
                if (response.result) {
                  that.$refs.spinner.showSpinner();
                  that.addGooglePayButton();
                }
              })
              .catch(function (err) {
                //isReadyToPay returns error (incompatible device)
                // console.log(err);
                checkoutEventBus.$emit('google-pay-payment-sheet', false);
              });
          } else {
            // flyout banner notifying that google pay is not working
            checkoutEventBus.$emit('google-pay-payment-sheet', false);
          }
        });
      } else {
        this.$refs.spinner.hideSpinner();
      }

    },

    /**
     * Add a Google Pay purchase button alongside an existing checkout button
     *
     * @see {@link https://developers.google.com/pay/api/web/reference/object#ButtonOptions|Button options}
     * @see {@link https://developers.google.com/pay/api/web/guides/brand-guidelines|Google Pay brand guidelines}
     */
    addGooglePayButton() {
      let button = null;
      this.paymentsClient = this.getGooglePaymentsClient();
      button = this.paymentsClient.createButton({
        onClick: this.onGooglePaymentButtonClicked, buttonColor: 'black', buttonType: 'short'
      });
      this.$refs.spinner.hideSpinner();
      document.getElementById('google-pay-container').appendChild(button);
    },

    /**
     * Provide Google Pay API with a payment amount, currency, and amount status
     *
     * @see {@link https://developers.google.com/pay/api/web/reference/object#TransactionInfo|TransactionInfo}
     * @returns {object} transaction info, suitable for use as transactionInfo property of PaymentDataRequest
     */
    getGoogleTransactionInfo() {
      let cCode = '';
      let tPriceStatus = '';
      let tPrice = 0;
      if (this.hybrisResponseData) {
        if (this.hybrisResponseData.transactionInfo) {
          if (this.hybrisResponseData.transactionInfo.currencyCode) {
            cCode = this.hybrisResponseData.transactionInfo.currencyCode
          }
          if (this.hybrisResponseData.transactionInfo.totalPriceStatus) {
            tPriceStatus = this.hybrisResponseData.transactionInfo.totalPriceStatus;
          }
          if (this.hybrisResponseData.transactionInfo.totalPrice) {
            tPrice = this.hybrisResponseData.transactionInfo.totalPrice.toString();
          }
          //TODO: 'checkout option' needs to be passed in & validated
        }
      }

      return {
        currencyCode: cCode,
        totalPriceStatus: tPriceStatus,
        // set to cart total

        totalPrice: tPrice,
        //displayItems: this.prepareGooglePayLineItems(),
        totalPriceLabel: this.GooglePay.labels.totalLabel,
        checkoutOption: this.GooglePay.labels.checkoutOption,
      };
    },

    /**
     * Show Google Pay payment sheet when Google Pay payment button is clicked
     */
    onGooglePaymentButtonClicked() {
      if (this.paymentType != this.allPaymentTypes.googlePay) {
        // button is only clickable if google pay is not paymentType

        let paymentDataRequest = {};
        const that = this;

        checkoutEventBus.$emit('payment-method-unselect');
        this.paymentType = this.allPaymentTypes.googlePay;
        // hides the Place Order button upon google pay button click

        checkoutEventBus.$emit('update-payment-type', this.paymentType);
        paymentDataRequest = this.getGooglePaymentDataRequest();
        paymentDataRequest.transactionInfo = this.getGoogleTransactionInfo();
        this.paymentsClient = this.getGooglePaymentsClient();

        this.paymentsClient
          .loadPaymentData(paymentDataRequest)
          .then(function (paymentData) {
            that.processPayment(paymentData);

          })
          .catch(function (err) {
            // change payment type back to credit/debit if any error occurs
            console.log(err)
            if (err.statusCode = "CANCELED") {
              checkoutEventBus.$emit('payment-method-default');
            }
          });
      }

    },

    /**
     * Process payment data returned by the Google Pay API
     */
    processPayment(paymentData) {
      // @todo pass payment token to your gateway to process payment

      const requestConfig = {
        //response data passed to Hybris for Cybersource authentication

        data: {
          apiVersion: paymentData.apiVersion,
          apiVersionMinor: paymentData.apiVersionMinor,
          email: paymentData.email,
          paymentMethodData: paymentData.paymentMethodData,
        },
      };

      this.$refs.spinner.showSpinner();
      // this.googlePayService.saveGooglePayResponse(
      //   //connect to Hybris API

      //   requestConfig,
      //   this.saveGooglePayResponseSuccess(),
      //   error,
      //   paymentData,
      // );

      this.googlePayService.saveGooglePayResponse(requestConfig, (data) => {
        // console.log('Sending payment authorization to Google pay');
        // data is returned from hybris, we need to check for success condition in data
        if (data) {
          console.log(data);
          checkoutEventBus.$emit('google-pay-status', data)
        } else {
        }
        this.$refs.spinner.hideSpinner();
      }, (data) => {
        if (data) {
          checkoutEventBus.$emit('google-pay-status', false)
        }
        this.$refs.spinner.hideSpinner();
      });
    },


    prepareGooglePayLineItems() {
      const displayItems = [
        {
          label: this.GooglePay.labels.subtotalLabel,
          type: this.GooglePay.labels.subtotalType,
          price: this.checkoutData.value.toString(),
        },
        {
          label: this.GooglePay.labels.taxLabel,
          type: this.GooglePay.labels.taxType,
          price: this.checkoutData.value.toString(),
        },
        {
          label: this.GooglePay.labels.shippingLabel,
          type: this.GooglePay.labels.labels.lineItem,
          price: this.checkoutData.deliveryCost.value.toString(),
        }
      ]
      return displayItems;
    },
  },
};

export default googlePayMixin;
