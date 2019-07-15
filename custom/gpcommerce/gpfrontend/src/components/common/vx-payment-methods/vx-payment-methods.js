import vxAddpaymentForm from '../vx-add-payment-form/vx-add-payment-form.vue';
import vxProfileCard from '../vx-profile-card/vx-profile-card.vue';
import vxModal from '../vx-modal/vx-modal.vue';
import { eventBus } from '../../../modules/event-bus';
import CyberSourceIntegrationService from '../../common/services/cybersource-integration-service';
import flyoutBannerMixin from '../../common/vx-flyout-banner/vx-flyout-banner-mixin';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import globals from '../../common/globals';
import { paymentTypes } from '../../common/mixins/vx-enums';

export default {
  name: 'vx-payment-methods',
  components: {
    'vx-add-payment-form': vxAddpaymentForm,
    vxProfileCard,
    vxModal,
    vxSpinner,
  },
  mixins: [flyoutBannerMixin],
  props: ['i18n'],
  data() {
    return {
      paymentMethodsData: null,
      displayTopHeadings: true,
      reloadingOnSuccess: true,
      deletePaymentObj: {},
      preferenceCardClasses: 'mb-xs-3 px-xs-3 py-xs-3 pl-sm-4 pr-sm-3',
      populatePaymentFormData: false,
      paymentFormData: {},
      cardDetails: [],
      deletePaymentId: '',
      targetEvent: '',
      csService: new CyberSourceIntegrationService(),
      globals,
    };
  },
  computed: {},
  mounted() {
    const self = this;
    self.fetchSavedCards();
    eventBus.$on('paymentMethodsData', (data) => {
      self.paymentsData = data;
      self.onResponseData();
    });
    const eventMethod = window.addEventListener
      ? 'addEventListener'
      : 'attachEvent';
    const eventer = window[eventMethod];
    const messageEvent =
      eventMethod === 'attachEvent' ? 'onmessage' : 'message';
    eventer(messageEvent, (e) => {
      if (e.data) {
        if (e.data.paymentMethod === paymentTypes.creditCard) {
          this.$refs.paymentMethodsSpinner.showSpinner();
          this.$refs.paymentForm.iframeResponse(e.data);
        }
      }
    });
  },
  methods: {
    newCardAdded() {
      this.cardDetails = [];
      this.fetchSavedCards();
      this.$refs.paymentMethodsSpinner.hideSpinner();
      this.$refs.paymentFormModal.close(event);
    },
    cancelDisable() {
      this.$refs.paymentDeleteModal.close();
    },
    openAddPaymentForm(event) {
      this.populatePaymentFormData = false;
      this.paymentFormData = {};
      this.$refs.paymentFormModal.open(event);
    },
    fetchSavedCards() {
      this.csService.fetchSavedCardsService(
        {},
        this.handleAddressData,
        this.handleAddressDataError,
      );
    },
    handleAddressData(response) {
      const status = response.status;
      const data = response.data;
      const self = this;
      if (status) {
        if (data && data.payments) {
          self.cardDetails = [];
          const fetchedDetails = data.payments;
          for (let roll = 0; roll < fetchedDetails.length; roll++) {
            const tempObj = {};
            tempObj.cardHolderName = fetchedDetails[roll].accountHolderName;
            tempObj.cardType = fetchedDetails[roll].cardType.name;
            tempObj.cardNumber = fetchedDetails[roll].cardNumber;
            tempObj.cardExpiry = `${fetchedDetails[roll].expiryMonth}/${
              fetchedDetails[roll].expiryYear
            }`;
            tempObj.zipCode = fetchedDetails[roll].billingAddress
              ? fetchedDetails[roll].billingAddress.postalCode
              : '';
            tempObj.isDefaultPaymentOpt = fetchedDetails[roll].defaultPayment;
            tempObj.subscriptionId = fetchedDetails[roll].subscriptionId;
            tempObj.id = fetchedDetails[roll].id;
            self.cardDetails.push(tempObj);
          }
        } else {
          self.cardDetails = [];
        }
      }
    },
    handleAddressDataError(error) {},
    openEditPaymentModal(paymentId, event) {
      this.targetEvent = event;
      this.csService.fetchSavedCardDetails(
        {},
        this.handleSavedCardData,
        this.handleSavedCardDataError,
        paymentId,
      );
    },
    openDeletePaymentModal(paymentObj, event) {
      this.deletePaymentObj = paymentObj;
      this.deletePaymentId = paymentObj.id;
      this.$refs.paymentDeleteModal.open(event);
    },
    sendPaymentDeleteRequest() {
      this.$refs.paymentMethodsSpinner.showSpinner();
      this.csService.deleteSavedCardDetails(
        {},
        this.handleDeleteSavedCardResponse,
        this.handleDeleteSavedCardError,
        this.deletePaymentId,
      );
    },
    handleDeleteSavedCardResponse(response) {
      const status = response.status;
      const data = response.data;
      if (status) {
        this.showFlyout(
          'success',
          this.i18n.paymentMethods.deletePaymentSuccessMessage,
          true,
        );
        this.fetchSavedCards();
        this.$refs.paymentDeleteModal.close();
        this.$refs.paymentMethodsSpinner.hideSpinner();
      }
    },
    handleDeleteSavedCardError(error) {},
    handleSavedCardData(response) {
      const status = response.status;
      const data = response.data;
      if (status) {
        if (data) {
          this.populatePaymentFormData = true;
          this.paymentFormData = data;
          this.$refs.paymentFormModal.open(this.targetEvent);
        }
      }
    },
    handleSavedCardDataError(error) {},
    onResponseData() {
      // eventBus.$emit('paymentSuccess', self.cardDetails);
    },
    dismissAddPaymentModal(errorMessage) {
      /* Commenting as per ALM-28818
      this.$refs.paymentFormModal.close(); */
      this.showFlyout(
        'error',
        this.i18n.paymentMethods.creditCardAddressError,
        false,
      );
      this.$refs.paymentMethodsSpinner.hideSpinner();
    },
  },
};
