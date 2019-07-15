import ManageShoppingCartService from '../../../components/common/services/manage-shopping-cart-service';
import globals from '../../../components/common/globals';
import vxSpinner from '../../../components/common/vx-spinner/vx-spinner.vue';
import vxModal from '../../../components/common/vx-modal/vx-modal.vue';
import flyoutBannerMixin from '../../../components/common/vx-flyout-banner/vx-flyout-banner-mixin';
import { eventBus, cartEventBus } from '../../../modules/event-bus';
import AnalyticsService from '../../../components/common/services/analytics-service';
// import navigateToCheckout from './vx-cart-checkout-mock-data';

export default {
  name: 'vx-sample-cart-checkout',
  mixins: [flyoutBannerMixin],
  components: {
    vxSpinner,
    vxModal,
  },
  props: {
    /**
     * text coming from property file
     */
    i18n: Object,
    orderSampleRedirectUrl: String,
  },
  data() {
    return {
      globals,
      // navigateToCheckout,
      clickCount: 0,
      userReview: false,
      productEntries: {},
      analyticsService: new AnalyticsService(),
    };
  },
  computed: {},
  mounted() {
    eventBus.$on('cart-details', (productEntries) => {
      this.productEntries = productEntries;
    });
    if (this.globals.userInfo.b2bUserReviewStatus === 'true') {
      this.userReview = true;
    } else {
      this.userReview = false;
    }
  },
  methods: {
    /**
     * This function handles the flow from cart to checkout
     */
    checkoutButtonClicked() {
      // sending the data to Google Analytics on Checkout
      if (typeof dataLayer !== 'undefined' && this.productEntries.product) {
        this.analyticsService.trackCheckout(this.productEntries);
      }
      const manageShoppingCartService = new ManageShoppingCartService();

      if (!this.globals.getIsLoggedIn()) {
        this.globals.setCookie('flow', 'checkout');
        this.globals.navigateToUrl('login');
      } else {
        this.$refs.spinner.showSpinner();
        manageShoppingCartService.validateGPXpressCart(
          {},
          this.navigateToGpxpressCheckoutResponse,
          this.navigateToGpxpressCheckoutError,
        );
      }
    },
    /**
     * This function handles the response of flow from cart to checkout
     */
    navigateToGpxpressCheckoutResponse(response) {
      this.$refs.spinner.hideSpinner();
      const data = response.data;
      if (data.success) {
        const newRedirectUrl = this.orderSampleRedirectUrl;
        // window.location.href = newRedirectUrl;
        // For Iframe uncomment below code
        window.parent.location.href = newRedirectUrl;
      }
    },
    /**
     * This function handles the error of flow from cart to checkout
     */
    navigateToGpxpressCheckoutError(error) {
      this.$refs.spinner.hideSpinner();
      if (
        error &&
        error.response &&
        error.response.data &&
        error.response.data.errors.length !== 0
      ) {
        this.showFlyout('error', error.response.data.errors[0].message, true);
      }
    },
  },
};
