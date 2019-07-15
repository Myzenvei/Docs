import ManageShoppingCartService from '../../common/services/manage-shopping-cart-service';
import globals from '../../common/globals';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import {
  ProductAvailability,
  cookies,
  flyoutStatus,
} from '../../common/mixins/vx-enums';
import flyoutBannerMixin from '../../common/vx-flyout-banner/vx-flyout-banner-mixin';
import { eventBus, cartEventBus } from '../../../modules/event-bus';
import AnalyticsService from '../../common/services/analytics-service';

export default {
  name: 'vx-cart-checkout',
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
  },
  data() {
    return {
      globals,
      clickCount: 0,
      ProductAvailability,
      cookies,
      leaseError: false,
      userReview: false,
      lowStockData: [],
      productEntries: {},
      analyticsService: new AnalyticsService(),
      lowStockStatus: '',
    };
  },
  computed: {},
  mounted() {
    this.globals.deleteCookie(cookies.subscrCartId);
    eventBus.$on('cart-details', (productEntries) => {
      this.productEntries = productEntries;
    });
    this.$root.$on('showLeaseError', (value) => {
      this.leaseError = value;
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
      if (typeof dataLayer !== 'undefined') {
        this.analyticsService.trackCheckout(this.productEntries);
      }
      const manageShoppingCartService = new ManageShoppingCartService();

      if (!this.globals.getIsLoggedIn()) {
        this.globals.setCookie('flow', 'checkout');
        this.globals.navigateToUrl('login');
      } else {
        this.$refs.spinner.showSpinner();
        manageShoppingCartService.validateCart(
          {},
          this.handleValidateCartResponse,
          this.handleValidateCartError,
        );
      }
    },
    /**
     * This function handles the response of flow from cart to checkout
     */
    handleValidateCartResponse(response) {
      const manageShoppingCartService = new ManageShoppingCartService();
      this.$refs.spinner.hideSpinner();
      const data = response.data;
      this.lowStockData = [];
      for (let i = 0; i < data.cartModificationDataList.length; i += 1) {
        if (
          data.cartModificationDataList[i].statusCode ===
          this.ProductAvailability.LOW_STOCK
        ) {
          this.lowStockData.push({
            entry: data.cartModificationDataList[i].entry,
            status: data.cartModificationDataList[i].statusMessage,
          });
        }
        if (
          data.cartModificationDataList[i].statusCode ===
          this.ProductAvailability.NO_STOCK
        ) {
          this.lowStockData.push({
            entry: data.cartModificationDataList[i].entry,
            status: data.cartModificationDataList[i].statusMessage,
            noStock: true,
          });
        }
      }
      const status = response.status;
      if (status && this.lowStockData.length === 0) {
        if (data) {
          manageShoppingCartService.validateIncompatibleProducts(
            {},
            this.validateIncompatibleProductsResponse,
            this.validateIncompatibleProductsError,
          );
        }
      } else if (this.lowStockData.length !== 0) {
        this.lowStockStatus = '';
        this.lowStockStatus = this.lowStockData[0].status;
        cartEventBus.$emit('cart-update');
      }
      this.$root.$on('cart-reloaded', (value) => {
        if (value && this.lowStockStatus) {
          this.showFlyout(flyoutStatus.error, this.lowStockStatus, true);
          this.lowStockStatus = '';
        }
      });
    },
    /**
     * This function handles the error of flow from cart to checkout
     */
    handleValidateCartError(error) {
      this.$refs.spinner.hideSpinner();
      if (
        error &&
        error.response &&
        error.response.data &&
        error.response.data.errors.length !== 0 &&
        error.response.data.errors[0] &&
        error.response.data.errors[0].code
      ) {
        if (
          error.response.data.errors[0].code ===
          this.i18n.maxPurchaseableQuantityErrorCode
        ) {
          this.showFlyout(
            flyoutStatus.error,
            this.i18n.maxPurchaseableQuantityErrorMessage,
            true,
          );
        } else if (
          error.response.data.errors[0].code === this.i18n.lowStockErrorCode
        ) {
          this.showFlyout(
            flyoutStatus.error,
            this.i18n.lowStockErrorMessage,
            true,
          );
        }
      }
    },
    validateIncompatibleProductsResponse(response) {
      if (!this.clickCount && response.data.incompatibleData.incompatible) {
        // Open the modal
        this.$refs.validateCheckoutModal.open();
        if (document.querySelector('.order-summary-section')) {
          document.querySelector('.order-summary-section').style.zIndex = 9;
          document
            .querySelector('.order-summary-section')
            .scrollIntoView(false);
        }
        this.clickCount++;
      } else {
        this.globals.navigateToUrl('checkout');
      }
    },
    validateIncompatibleProductsError() {},
    /**
     * This function handles the close of incompatibility warning modal
     */
    validateCheckoutModalClose() {
      if (document.querySelector('.order-summary-section')) {
        document.querySelector('.order-summary-section').style.zIndex = '';
      }
    },
  },
};
