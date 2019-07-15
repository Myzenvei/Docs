import vxProductTileVertical from '../../search-browse/vx-product-tile-vertical/vx-product-tile-vertical.vue';
import globals from '../../common/globals';
import PdpService from '../../common/services/pdp-service';
import {
  referenceTypes,
  relatedCategories,
} from '../../common/mixins/vx-enums';

export default {
  name: 'vx-related-products-tab',
  components: {
    vxProductTileVertical,
  },
  props: {
    /**
     * parent product code
     */
    parentProduct: {
      type: String,
      default: '',
    },
    /**
     * Copy text coming from properties files
     */
    i18n: {
      type: Object,
    },
    /**
     * indicates whether the site is configured for favorites
     */
    isFavorites: {
      type: Boolean,
      default: false,
    },
    /**
     * indicates whether the site is configured for Bazaar Voice
     */
    isBazaarVoice: {
      type: String,
      default: '',
    },
    /**
     * indicates whether the related products are enabled for the product
     */
    isRelatedEnabled: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      globals,
      relatedProductsData: {},
      pdpService: new PdpService(),
      relatedProducts: [],
      referenceTypes,
      relatedCategories,
    };
  },
  computed: {},
  mounted() {
    this.onInitCheck();
  },
  updated() {},
  methods: {
    /**
     * This function is called on mounted which passes parent product code
     */
    onInitCheck() {
      const self = this;
      self.generateUrlRequest(self.parentProduct);
    },
    /**
     * This function handles the response of related products service
     */
    handleRelatedProductsResponse(response) {
      if (response && response.data) {
        this.relatedProductsData = response.data;
        if (!this.isRelatedEnabled) {
          this.relatedProducts = this.relatedProductsData.references.map(item => item.target);
        } else {
          this.relatedProductsData.map((subcategory) => {
            if (relatedCategories.hasOwnProperty(subcategory.referenceType)) {
              const relatedCategory = relatedCategories[subcategory.referenceType];
              subcategory.referenceType = relatedCategory;
            }
          });
        }
      }
    },
    /**
     * This function handles the error of related products service
     */
    handleRelatedProductsError(error) {},
    /**
     * This function function to generate compare result url
     */
    generateUrlRequest(productId) {
      const self = this;
      const requestConfig = {};
      if (!this.isRelatedEnabled) {
        if (this.globals.loggedIn) {
          const userId = this.globals.uid;
          requestConfig.params = {
            referenceType: this.referenceTypes.similar,
            userId,
          };
        } else {
          requestConfig.params = {
            referenceType: this.referenceTypes.similar,
          };
        }
        self.pdpService.getProductsData(
          requestConfig,
          this.handleRelatedProductsResponse,
          this.handleRelatedProductsError,
          productId,
        );
      } else {
        self.pdpService.getRelatedProductsData(
          requestConfig,
          this.handleRelatedProductsResponse,
          this.handleRelatedProductsError,
          productId,
        );
      }
    },
  },
};
