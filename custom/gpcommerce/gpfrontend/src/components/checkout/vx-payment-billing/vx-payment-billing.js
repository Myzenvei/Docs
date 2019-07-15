import vxDropdownPrimary from '../../common/vx-dropdown-primary/vx-dropdown-primary.vue';
import vxRadioButtonGroup from '../../common/vx-radio-button-group/vx-radio-button-group.vue';
import globals from '../../common/globals';
// import paymentDetails from './vx-payment-billing-mock';
import CheckoutService from '../../common/services/checkout-service';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import vxAddPaymentForm from '../../common/vx-add-payment-form/vx-add-payment-form.vue';
import {
  cardNames,
  paymentTypes,
  paypalType,
  flyoutStatus,
  paypalResponseStatus,
  paymentStatus,
} from '../../common/mixins/vx-enums';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import { checkoutEventBus } from '../../../modules/event-bus';
import flyoutBannerMixin from '../../common/vx-flyout-banner/vx-flyout-banner-mixin';
import mobileMixin from '../../common/mixins/mobile-mixin';
import applePayMixin from '../../common/mixins/apple-pay-mixin';
import googlePayMixin from '../../common/mixins/google-pay-mixin';

export default {
  name: 'vx-payment-billing',
  mixins: [flyoutBannerMixin, mobileMixin, applePayMixin, googlePayMixin],
  components: {
    vxDropdownPrimary,
    vxRadioButtonGroup,
    vxSpinner,
    vxAddPaymentForm,
    vxModal,
  },
  props: {
    i18n: Object,
    selectedCard: Object,
    shippingAddress: Object,
    sectionIndex: Number,
    guestEmail: String,
    checkoutData: Object,
    disableButton: Boolean,
  },
  data() {
    return {
      isEditable: true,
      cardRadioButton: [
        {
          label: this.i18n.paymentBilling.cardRadioButton,
          value: this.i18n.paymentBilling.cardRadioButton,
        },
      ],
      cardSelected: {
        label: '',
        cardNumber: '',
        cardType: '',
        paymentId: '',
        address: '',
        accountHolderName: '',
      },
      globals,
      paymentDetails: {},
      savedCards: [],
      checkoutService: new CheckoutService(),
      dataLoaded: true,
      isButtonDisabled: true,
      addNewCard: true,
      guest: false,
      cardImage: '',
      addedCard: {},
      newCardInProcess: false,
      userAddress: {},
      paymentType: '',
      allPaymentTypes: paymentTypes,
      paypalType,
      paypalNavigation: '',
      isPaypalResponse: false,
      arrangeHorizontal: Boolean,
    };
  },
  watch: {
    'cardSelected.cardType': function() {
      if (this.cardSelected.cardType === cardNames.visa) {
        this.cardImage = `${
          this.globals.assetsPath
        }images/creditcardicons/Visa.svg`;
      } else if (this.cardSelected.cardType === cardNames.masterCard) {
        this.cardImage = `${
          this.globals.assetsPath
        }images/creditcardicons/MasterCard.svg`;
      } else if (this.cardSelected.cardType === cardNames.discover) {
        this.cardImage = `${
          this.globals.assetsPath
        }images/creditcardicons/Discover.svg`;
      } else if (this.cardSelected.cardType === cardNames.americanExpress) {
        this.cardImage = `${
          this.globals.assetsPath
        }images/creditcardicons/AmericanExpress.svg`;
      } else if (this.cardSelected.cardType === cardNames.diner) {
        this.cardImage = `${
          this.globals.assetsPath
        }images/creditcardicons/DinersClub.svg`;
      } else if (this.cardSelected.cardType === cardNames.googlePay) {
        this.cardImage = `${
          this.globals.assetsPath
        }images/creditcardicons/GooglePay.svg`;
      }
    },
  },
  computed: {},
  mounted() {
    const self = this;
    /**
     * Rests paymethods options on the checkout page
     */
    checkoutEventBus.$on('payment-method-unselect', () => {
      this.deselectDefualtPaymentSelection();
    });
    /**
     * Configures default payment card on the apple pay popup
     */
    checkoutEventBus.$on('payment-method-default', () => {
      this.setDefualtPaymentMethod();
    });
    paypal.Button.render(
      {
        env: globals.paypalEnv,
        style: {
          layout: 'horizontal',
          size: 'responsive',
          tagline: false,
          label: 'paypal',
        },
        commit: true,
        funding: {
          allowed: self.globals.siteConfig.paypalCreditEnabled
            ? [paypal.FUNDING.CREDIT]
            : false,
        },
        payment(resolve, reject) {
          return self.checkoutService.paypalRequest(
            {},
            (response) => {
              if (!response.data) {
                self.showFlyout(
                  flyoutStatus.error,
                  `${self.i18n.paypal.paymentError}`,
                  false,
                );
              }
              resolve(response.data);
            },
            self.handlePaypalError,
            paypalType.nonCredit,
          );
        },
        onAuthorize(data, actions) {
          self.savePaypalResposne(data);
        },
        onCancel(data, actions) {
          self.paymentType = self.cardRadioButton[0].value;
          self.$refs.cardRadioButton.setSelectedByValue(
            self.cardRadioButton[0].value,
          );
          checkoutEventBus.$emit('update-payment-type', self.paymentType);
        },
      },
      '#paypal-button-container',
    );
    if (globals.showPaypal && !this.globals.getIsSubscription()) {
      this.cardRadioButton.push({
        label: this.i18n.paymentBilling.paypal,
        value: this.i18n.paymentBilling.paypal,
      });
    }
    this.arrangeHorizontal = !this.isMobile();
    this.isMobileFisrtCard = !this.isMobile();
    this.paymentType = this.cardRadioButton[0].value;
    this.$refs.cardRadioButton.setSelectedByValue(
      this.cardRadioButton[0].value,
    );
    this.showSavedData();
    if (this.globals.getIsLoggedIn()) {
      this.guest = false;
    } else {
      this.guest = true;
    }

    const eventMethod = window.addEventListener
      ? 'addEventListener'
      : 'attachEvent';
    const eventer = window[eventMethod];
    const messageEvent =
      eventMethod === 'attachEvent' ? 'onmessage' : 'message';
    eventer(messageEvent, (e) => {
      // data you receive from iframe or paypal window is stored inside e.data
      if (e.data) {
        if (
          e.data.paymentMethod === paymentTypes.paypal &&
          e.data.status !== paypalResponseStatus.error
        ) {
          this.isPaypalResponse = true;
          this.getPaypalStatus(e.data);
        } else if (e.data.paymentMethod === paymentTypes.creditCard) {
          this.$refs.paymentForm.iframeResponse(e.data, 'checkout');
        }
      }
    });
  },
  methods: {
    editPayment(event) {
      this.addedCard = {};
      checkoutEventBus.$emit('payment-method-edit');
      if (!this.guest) {
        this.getPaymentDetails();
      } else {
        this.addNewCard = true;
      }
      this.isEditable = true;
    },
    // set data for cardSelected Object
    setData(item) {
      this.cardSelected.label = item.label;
      this.cardSelected.cardNumber = item.cardNumber
        ? item.cardNumber.match(/\d+/)[0]
        : '';
      if (item.cardType.name) {
        this.cardSelected.cardType = item.cardType.name;
      } else {
        this.cardSelected.cardType = item.cardType;
      }
      this.cardSelected.paymentId = item.paymentId;
      this.cardSelected.address = item.address;
      this.cardSelected.accountHolderName = item.accountHolderName;
      this.userAddress = item.billingAddress;
      this.isButtonDisabled = false;
    },
    // show data it the cart call already has payment details
    showSavedData() {
      if (this.selectedCard && this.selectedCard.cardNumber) {
        this.dataLoaded = true;
        this.isEditable = false;
        this.addNewCard = false;
        this.isButtonDisabled = false;
        this.cardSelected.cardNumber = this.selectedCard.cardNumber
          ? this.selectedCard.cardNumber.match(/\d+/)[0]
          : '';
        this.cardSelected.cardType = this.selectedCard.cardType.name;
        this.cardSelected.label = `<${this.selectedCard.cardType.name}> <${
          this.selectedCard.cardNumber
        }> <${this.selectedCard.expiryMonth}/${
          this.selectedCard.expiryYear
        }> <${this.selectedCard.accountHolderName}>`;
        this.cardSelected.paymentId = this.selectedCard.id;
        this.cardSelected.billingAddress = this.selectedCard.billingAddress;
        this.cardSelected.address = this.setAddress(this.selectedCard);
        this.cardSelected.accountHolderName = this.selectedCard.accountHolderName;
        this.$refs.cardDropdown.setDropdownLabel(this.cardSelected.label);
        this.$refs.cardRadioButton.setSelectedByValue(
          this.cardRadioButton[0].value,
        );
        checkoutEventBus.$emit('payment-method-added');
      } else {
        this.getPaymentDetails();
      }
    },
    getPaymentId() {
      // const payId = '';
      this.paymentDetails.payments.map((item) => {
        if (item.paymentToken === this.addedCard.paymentToken) {
          this.addedCard.paymentId = item.id;
        }
      });
      // return payId;
    },
    setNewCard(cardDetails) {
      this.addedCard = {};
      this.addedCard = cardDetails;
      this.addedCard.billingAddress = this.addedCard.billingAddress;
      this.addedCard.address = this.setAddress(this.addedCard);
      this.cardSelected = this.addedCard;
      this.setData(this.addedCard);
      this.isEditable = false;
      this.addNewCard = false;
      if (this.$refs.addCardModal) {
        this.$refs.addCardModal.close();
      }
      this.cardSelected.paymentId = this.cardSelected.id;
      this.savePayment();
      // if (!this.guest) {
      //   this.getPaymentDetails();
      // } else {
      //   this.cardSelected.paymentId = this.cardSelected.id;
      //   this.savePayment();
      // }
    },
    // get all the cards saved by the user
    getPaymentDetails() {
      if (this.globals.getIsLoggedIn()) {
        // const requestConfig = {};
        // requestConfig.headers = this.globals.getHeaders();
        // requestConfig.url = this.globals.getRestUrl('paymentDetails', 'user');
        // requestConfig.method = 'get';
        // this.generateRequest(
        //   requestConfig,
        //   this.handleGetPaymentDetailsResponse,
        //   this.handleGetPaymentDetailsError,
        // );
        this.checkoutService.getPaymentDetails(
          {},
          this.handleGetPaymentDetailsResponse,
          this.handleGetPaymentDetailsError,
        );
        this.$refs.spinner.showSpinner();
      }
    },
    handleGetPaymentDetailsResponse(response) {
      this.$refs.spinner.hideSpinner();
      if (this.$refs.addCardModal) {
        this.$refs.addCardModal.close();
      }
      if (response && response.data) {
        this.paymentDetails = response.data;
        // if (
        //   (!this.addedCard.paymentId || this.paymentDetails.payments.length === 1) &&
        //   this.newCardInProcess
        // ) {
        //   this.getPaymentId();
        //   this.savePayment();
        //   this.newCardInProcess = false;
        // }
        // if (this.paymentDetails.payments.length === 0) {
        //   this.addNewCard = true;
        // } else if (!this.addedCard.paymentToken) {
        //   this.addNewCard = false;
        //   this.createSavedCardsArray();
        // }
        if (this.paymentDetails.payments.length === 0) {
          this.addNewCard = true;
        } else {
          this.addNewCard = false;
          this.createSavedCardsArray();
        }
      }
      this.dataLoaded = true;
    },
    handleGetPaymentDetailsError() {
      this.$refs.spinner.hideSpinner();
    },
    // create the array for the dropdown
    createSavedCardsArray() {
      this.savedCards = [];
      this.paymentDetails.payments.map((item) => {
        this.savedCards.push({
          label: `<${item.cardType.name}> <${item.cardNumber}> <${
            item.expiryMonth
          }/${item.expiryYear}> <${item.accountHolderName}>`,
          value: item.cardNumber,
          cardType: item.cardType.name,
          cardNumber: item.cardNumber,
          default: item.defaultPayment,
          paymentId: item.id,
          paymentToken: item.paymentToken,
          accountHolderName: item.accountHolderName,
          billingAddress: item.billingAddress,
          address: this.setAddress(item),
        });
        this.handleDefault();
      });
      for (let j = 0; j < this.savedCards.length; j += 1) {
        if (
          this.savedCards &&
          this.savedCards.length !== 0 &&
          this.savedCards[j] &&
          this.savedCards[j].default
        ) {
          const defaultPaymentCard = this.savedCards[j];
          this.savedCards.splice(
            this.savedCards.indexOf(defaultPaymentCard),
            1,
          );
          this.savedCards.unshift(defaultPaymentCard);
          break;
        }
      }
    },
    setAddress(item) {
      let address = '';
      if (item.billingAddress.line2) {
        address = `${item.billingAddress.line1}, ${item.billingAddress.line2} ${
          item.billingAddress.town
        }, ${item.billingAddress.postalCode}`;
      } else {
        address = `${item.billingAddress.line1} ${item.billingAddress.town}, ${
          item.billingAddress.postalCode
        }`;
      }
      this.userAddress = item.billingAddress;
      return address;
    },
    // handle the value selected in the dropdown by the user
    handleSelected(event) {
      this.setData(event);
    },
    // handle default card selected
    handleDefault() {
      if (this.savedCards.length === 1) {
        this.$refs.cardDropdown.setDropdownLabel(this.savedCards[0].label);
        this.setData(this.savedCards[0]);
      } else if (this.selectedCard && this.selectedCard.cardNumber) {
        const cardSelected = {};
        cardSelected.cardNumber = this.selectedCard.cardNumber
          ? this.selectedCard.cardNumber.match(/\d+/)[0]
          : '';
        cardSelected.cardType = this.selectedCard.cardType.name;
        cardSelected.label = `<${this.selectedCard.cardType.name}> <${
          this.selectedCard.cardNumber
        }> <${this.selectedCard.expiryMonth}/${
          this.selectedCard.expiryYear
        }> <${this.selectedCard.accountHolderName}>`;
        cardSelected.paymentId = this.selectedCard.id;
        cardSelected.billingAddress = this.selectedCard.billingAddress;
        cardSelected.address = this.setAddress(this.selectedCard);
        cardSelected.accountHolderName = this.selectedCard.accountHolderName;
        this.$refs.cardDropdown.setDropdownLabel(cardSelected.label);
        this.setData(cardSelected);
      } else {
        this.savedCards.map((item) => {
          if (item.default) {
            this.$refs.cardDropdown.setDropdownLabel(item.label);
            this.setData(item);
          }
        });
      }
    },
    // Save the card selected by the user
    savePayment() {
      // const requestConfig = {};
      const paymentId = this.cardSelected.paymentId
        ? this.cardSelected.paymentId
        : this.addedCard.paymentId;
      // requestConfig.headers = this.globals.getHeaders();
      // requestConfig.url = this.globals.getRestUrl('paymentDetails', 'cart');
      // requestConfig.method = 'put';
      // requestConfig.params = {
      //   paymentDetailsId: paymentId,
      // };
      // this.generateRequest(
      //   requestConfig,
      //   this.handleSavePaymentResponse,
      //   this.handleSavePaymentError,
      // );
      this.checkoutService.savePayment(
        {},
        this.handleSavePaymentResponse,
        this.handleSavePaymentError,
        paymentId,
      );
      this.$refs.spinner.showSpinner();
    },
    handleSavePaymentResponse() {
      this.isEditable = false;
      checkoutEventBus.$emit('payment-method-added');
      checkoutEventBus.$emit('update-cart');
      this.$refs.spinner.hideSpinner();
    },
    handleSavePaymentError() {
      this.isEditable = true;
      checkoutEventBus.$emit('payment-method-edit');
      this.$refs.spinner.hideSpinner();
    },
    addNewPayment(event) {
      // this.newCardInProcess = true;
      this.$refs.addCardModal.open(event);
    },
    dismissAddPaymentModal(errorMessage, isModal) {
      if (isModal) {
        /* Commenting as per ALM-28818
        this.$refs.addCardModal.close(); */
        this.showFlyout(
          'error',
          this.i18n.paymentBilling.creditCardAddressError,
          false,
        );
      } else {
        this.showFlyout(
          'error',
          this.i18n.paymentBilling.creditCardAddressError,
          false,
        );
      }
      this.$refs.spinner.hideSpinner();
    },
    paymentOption(type) {
      this.paymentType = type.value;
      checkoutEventBus.$emit('update-payment-type', this.paymentType);
    },

    savePaypalResposne(data) {
      const requestConfig = {};
      requestConfig.data = data;
      this.checkoutService.savePaypalResponse(
        requestConfig,
        this.handlePaypalPaymentResponse,
        this.handlePaypalPaymentError,
      );
      this.$refs.spinner.showSpinner();
    },

    handlePaypalPaymentResponse(response) {
      if (response.data) {
        this.getPaypalStatus({ status: paymentStatus.accept });
      }
      this.$refs.spinner.hideSpinner();
    },

    handlePaypalPaymentError(error) {
      this.$refs.spinner.hideSpinner();
    },

    paypalPayment(type) {
      this.checkoutService.paypalRequest(
        {},
        this.handlePaypalResponse,
        this.handlePaypalError,
        type,
      );
      /* Commented as we moved to paypal checkout.js implementation
      this.paypalNavigation = window.open(
        '/',
        'paypalWindow',
        'width=660,height=700,location=no,scrollbars=yes,resizable=yes,status=no,menubar=no',
      );
      this.paypalNavigation.document.write(
        `<img style="position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%);" src="${
          globals.assetsPath
        }images/spinner.gif" alt="Loading..." />`,
      );
      const timer = setInterval(() => {
        if (this.paypalNavigation && this.paypalNavigation.closed) {
          clearInterval(timer);
          if (!this.isPaypalResponse) {
            this.$refs.spinner.hideSpinner();
            this.$refs.cardRadioButton.setSelectedByValue(
              this.cardRadioButton[0].value,
            );
            this.paymentType = this.cardRadioButton[0].value;
            checkoutEventBus.$emit('update-payment-type', this.paymentType);
          }
        } else {
          this.paypalNavigation.postMessage('', '*');
        }
      }, 1000);
      this.$refs.spinner.showSpinner(); */
    },

    handlePaypalResponse(response) {
      if (response.data) {
        /* Commented as we moved to paypal checkout.js implementation
        this.paypalNavigation.location = response.data; */
        return response.data;
      }
    },

    handlePaypalError(error) {
      this.paypalNavigation.close();
      this.$refs.spinner.hideSpinner();
      this.showFlyout(
        flyoutStatus.error,
        `${this.i18n.paypal.paymentError}`,
        false,
      );
      return error;
    },

    getPaypalStatus(data) {
      this.$refs.spinner.hideSpinner();
      checkoutEventBus.$emit('paypal-status', data);
    },
    deselectDefualtPaymentSelection() {
      // Unselecting the radio button group on initiation of apple pay
      if (this.$refs.cardRadioButton) {
        this.$refs.cardRadioButton.resetRadioGroup();
      }
      // hiding add card form
      if (this.addNewCard) {
        this.addNewCard = false;
      }
    },
    setDefualtPaymentMethod() {
      // Selecting the radio button group on apple pay cancel
      this.paymentType = this.cardRadioButton[0].value;
      if (this.$refs.cardRadioButton) {
        this.$refs.cardRadioButton.setSelectedByValue(
          this.cardRadioButton[0].value,
        );
      }
      // show the add card form if no save cards
      if (this.paymentDetails.payments.length === 0) {
        this.addNewCard = true;
      } else {
        this.addNewCard = false;
      }
      // Displaying Place order button back if apple pay cancels
      checkoutEventBus.$emit('update-payment-type', this.paymentType);
    },
  },
};
