import globals from '../../../components/common/globals';
import vxSpinner from '../vx-spinner/vx-spinner.vue';
import flyoutBannerMixin from '../vx-flyout-banner/vx-flyout-banner-mixin';
import SearchBrowseService from '../services/search-browse-service';
import {
  flyoutStatus,
} from '../../common/mixins/vx-enums';
import {
  cartEventBus,
} from '../../../modules/event-bus';

export default {
  name: 'vx-reorder-product',
  components: {
    vxSpinner,
  },
  mixins: [flyoutBannerMixin],
  props: {
    i18n: {
      type: Object,
    },
    singleProductData: {
      type: Object,
    },
    multipleProductsData: {
      type: Object,
    },
  },
  data() {
    return {
      globals,
      flyoutStatus,
      searchBrowseService: new SearchBrowseService(),
      selectedBulkProducts: [],
    };
  },
  computed: {
    // eslint-disable-next-line object-shorthand
    pluralizeReorderButton: function() {
      return this.selectedBulkProducts.length > 1 ?
        this.i18n.reorderMultipleButtonText :
        this.i18n.reorderSingleButtonText;
    },
  },
  mounted() {
    if (this.multipleProductsData) {
      this.selectedBulkProducts = _.values(this.multipleProductsData);
    } else {
      this.selectedBulkProducts.push(this.singleProductData);
    }
  },
  methods: {
    handleAddToCart() {
      this.$refs.spinner.showSpinner();
      const bulkProducts = [];
      // eslint-disable-next-line array-callback-return
      this.selectedBulkProducts.map((item) => {
        bulkProducts.push({
          code: item.product.code,
          minOrderQuantity: item.quantity || item.product.minOrderQuantity || 1,
        });
      });
      const requestBody = {
        products: bulkProducts,
      };
      const requestConfig = {};
      requestConfig.data = requestBody;
      this.searchBrowseService.addBulkToCart(
        requestConfig,
        this.handleAddBulkToCartResponse,
        this.handleAddBulkToCartError,
      );
    },
    handleAddBulkToCartResponse(response) {
      this.$refs.spinner.hideSpinner();
      if (response.data) {
        cartEventBus.$emit('call-basic-cart');
        const flyoutSuccess = [];
        const flyoutErrors = [];
        const foundCodes = [];
        // eslint-disable-next-line array-callback-return
        response.data.map((product) => {
          if (foundCodes.indexOf(product.statusCode) === -1) { // statusCode not already in the foundCodes array
            foundCodes.push(product.statusCode);
            switch (product.statusCode) { // Add the code to the correct flyout list
              case this.i18n.maxPurchaseableQuantityErrorCode:
                flyoutErrors.push(this.i18n.addedMaximumAllowableItemsMessage);
                break;
              case this.i18n.lowStockErrorCode:
                flyoutErrors.push(this.i18n.addedMaximumInStockItemsMessage);
                break;
              case this.i18n.success:
                flyoutSuccess.push(this.i18n.addToCartSuccess);
                break;
              default:
                flyoutErrors.push(this.i18n.couldNotAddItemsFailure);
                break;
            }
          }
        });

        if (flyoutSuccess.length) {
          this.showFlyout(this.flyoutStatus.success, flyoutSuccess.join('<br />'), true, 10000);
        }
        if (flyoutErrors.length) {
          this.showFlyout(this.flyoutStatus.error, flyoutErrors.join('<br />'), true, 10000);
        }
      }
    },
    handleAddBulkToCartError() {
      this.$refs.spinner.hideSpinner();
      this.showFlyout('error', this.i18n.generalFailure, true);
    },
  },
};
