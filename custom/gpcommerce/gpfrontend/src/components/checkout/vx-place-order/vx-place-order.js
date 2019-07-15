import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import globals from '../../common/globals';
import { checkoutEventBus } from '../../../modules/event-bus';
import {
  paymentTypes,
  placeOrder,
  cookies,
} from '../../common/mixins/vx-enums';
import flyoutBannerMixin from '../../common/vx-flyout-banner/vx-flyout-banner-mixin';
import CheckoutService from '../../common/services/checkout-service';
import AnalyticsService from '../../common/services/analytics-service';

export default {
  name: 'vx-place-order',
  mixins: [flyoutBannerMixin],
  components: {
    vxSpinner,
  },
  props: {
    i18n: Object,
    orderDetails: Object,
  },
  data() {
    return {
      globals,
      paymentTypes,
      placeOrder,
      cookies,
      isButtonDisabled: true,
      isPayPal: false,
      analyticsService: new AnalyticsService(),
      orderButtonLabel: this.i18n.placeOrderLabel,
      isApplePay: false,
      isGooglePay: false,

    };
  },
  computed: {},
  mounted() {
    checkoutEventBus.$on('enable-place-order', () => {
      this.isButtonDisabled = false;
    });
    checkoutEventBus.$on('disable-place-order', () => {
      this.isButtonDisabled = true;
    });
    checkoutEventBus.$on('place-order', () => {
      this.placeOrderRequest();
    });
    checkoutEventBus.$on('update-payment-type', (paymentType) => {
      this.isPayPal = paymentType === paymentTypes.paypal;
      this.isApplePay = paymentType === paymentTypes.applePay;
      this.isGooglePay = paymentType === paymentTypes.googlePay;

    });
    if (this.globals.getIsSubscription()) {
      this.orderButtonLabel = this.i18n.subscribeOrderLabel;
    }
  },
  methods: {
    placeOrderRequest(event) {
      const checkoutService = new CheckoutService();
      checkoutService.placeOrder(
        {},
        this.handlePlaceOrderResponse,
        this.handlePlaceOrderError,
      );
      this.$refs.spinner.showSpinner();
    },
    // After successfull response we need to navigate user to order confirmation page.
    handlePlaceOrderResponse(response) {
      const queryParam = response.data.code;
      this.globals.deleteCookie(cookies.subscrCartId);
      // sending the data to Google Analytics on Order Confirmation
      if (typeof dataLayer !== 'undefined') {
        this.analyticsService.trackProductPurchased(response.data);
      }
      const url = `${this.globals.getNavigationUrl(
        'orderConfirmation',
      )}/${queryParam}`;
      window.location = url;
      this.$refs.spinner.hideSpinner();
    },
    handlePlaceOrderError(error) {
      this.$refs.spinner.hideSpinner();
      if (
        error.response.data.errors[0].code === this.i18n.lowQuantityErrorCode ||
        error.response.data.errors[0].code ===
        this.i18n.lowQuantityErrorCodeSubscription
      ) {
        if (!this.globals.getIsSubscription()) {
          this.globals.navigateToUrl(placeOrder.cart);
          this.showFlyoutNextPage(
            placeOrder.error,
            error.response.data.errors[0].message,
            true,
          );
        } else {
          const productUrl = this.orderDetails.entries[0].product.url;
          const fullProductUrl =
            this.globals.getNavigationUrl(placeOrder.empty) + productUrl;
          window.location = fullProductUrl;
          this.showFlyoutNextPage(
            placeOrder.error,
            error.response.data.errors[0].message,
            true,
          );
        }
      } else if (
        error.response.data.errors[0].code === this.i18n.paymentErrorCode
      ) {
        this.showFlyout(
          placeOrder.error,
          this.i18n.paymentErrorCodeMessage,
          false,
        );
      }
    },
    createRequestbody() { },
  },
};
