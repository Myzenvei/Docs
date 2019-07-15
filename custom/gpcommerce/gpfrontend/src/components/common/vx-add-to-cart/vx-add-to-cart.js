import globals from '../../../components/common/globals';
import vxSpinner from '../vx-spinner/vx-spinner.vue';
import flyoutBannerMixin from '../vx-flyout-banner/vx-flyout-banner-mixin';
import SearchBrowseService from '../services/search-browse-service';
import AnalyticsService from '../services/analytics-service';
import YMarketingService from '../services/yMarketing-service';

import {
  cartEventBus,
  eventBus
} from '../../../modules/event-bus';

export default {
  name: 'vx-add-to-cart',
  components: {
    vxSpinner
  },
  mixins: [flyoutBannerMixin],
  props: {
    i18n: {
      type: Object
    },
  },
  data() {
    return {
      globals,
      quantity: "1",
      searchBrowseService: new SearchBrowseService(),
      analyticsService: new AnalyticsService(),
      yMarketing: new YMarketingService(),
      productPrice: "",
      productCode: "",
      productName: "",
    };
  },
  computed: {},
  mounted() {
    eventBus.$on("ymktAddToCart", (product) => {
      console.log("eventBus" + product);
      if(product.code){
        this.productCode = product.code;
      }
      if(product.name){
        this.productName = product.name;
      }
      if(product.price){
        this.productPrice = product.price.match(/([0-9.,]+)/)[0];
      }
      this.addToCart();
    });
  },
  methods: {
    addToCart() {
      this.$refs.spinner.showSpinner();
      const requestObjParams = {
        quantity: this.quantity,
        product: {
          code: `${this.productCode}`,
        },
      };
      const requestConfig = {};
      requestConfig.data = requestObjParams;
      this.searchBrowseService.addToCart(
        requestConfig,
        this.handleAddProductResponse,
        this.handleAddProductErrorResponse,
      );
      if (typeof dataLayer !== 'undefined') {
        const analyticsObject = {
          code: this.productCode,
          name: this.productName,
          quantity: this.quantity, // the default quantity set to 1 as there is no stepper control to edit quantity
        };
        // sending the data to Google Analytics on Add to Cart button click
        this.analyticsService.trackAddToCart(analyticsObject);
      }
      // sending data to yMarketing on add to cart button click
      const cartData = {
        cartCode: this.globals.getCartGuid(),
        productCode: this.productCode,
        productName: this.productName,
        productPrice: this.productPrice,
      };
      this.yMarketing.trackAddToCart(this.productCode, '1', cartData);
    },
    handleAddProductResponse(response) {
      this.$refs.spinner.hideSpinner();
      if (response && response.data) {
        // if (response.data.entry.product.assetCode === 'refill') {
        //   this.$refs.refillsModal.open();
        // }
        cartEventBus.$emit('cart-update');
        cartEventBus.$emit('call-basic-cart');
      }
    },
    handleAddProductErrorResponse(error) {
      this.$refs.spinner.hideSpinner();
      if (
        error &&
        error.response &&
        error.response.data &&
        error.response.data.errors.length !== 0 &&
        error.response.data.errors[0] &&
        error.response.data.errors[0].code
      ) {
        if (error.response.data.errors[0].code === this.i18n.maxPurchaseableQuantityErrorCode) {
          this.showFlyout('error', this.i18n.maxPurchaseableQuantityErrorMessage, true);
        } else if (error.response.data.errors[0].code === this.i18n.lowStockErrorCode) {
          this.showFlyout('error', this.i18n.lowStockErrorMessage, true);
        }
      }
    }
  },
};
