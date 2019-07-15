import slider from '../vx-slider/vx-slider.vue';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import {
  eventBus,
  cartEventBus
} from '../../../modules/event-bus';
import globals from '../../common/globals';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import AnalyticsService from '../../common/services/analytics-service';
import PdpService from '../../common/services/pdp-service';
import mobileMixin from '../../common/mixins/mobile-mixin';

export default {
  name: 'vx-refills',
  mixins: [mobileMixin],
  components: {
    'vx-slider': slider,
    vxModal,
    vxSpinner,
  },
  props: {
    /**
     * Details of the product
     */
    products: {
      type: Object,
      default: {},
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
     * indicates whether sample cart is enabled for the site
     */
    enableSampleCart: {
      type: Boolean,
      default: false,
    },
    /**
     * Copy text coming from properties files for search browse components
     */
    searchBrowseI18n: {
      type: Object,
    },
  },
  data() {
    return {
      pdpProductsData: this.products,
      referenceTypes: {
        refills: 'RELATEDPRODUCTSMODAL',
      },
      refillProductIds: [],
      selectedRefillProductData: [],
      globals,
      refillProductsData: Object,
      quantity: [],
      code: [],
      analyticsService: new AnalyticsService(),
      pdpService: new PdpService(),
      refillProducts: [],
      sliderSlides: {
        desktop: {
          slides: 3,
          spaceAllowed: 16,
        },
        tablet: {
          slides: 3,
          spaceAllowed: 16,
        },
        mobile: {
          slides: 1,
          spaceAllowed: 0,
        },
      },
      disableCartButton: true,
    };
  },
  watch: {
    /**
     * Disable the add to cart/order sample button when no refill is selected
     */
    selectedRefillProductData() {
      if (this.selectedRefillProductData.length !== 0) {
        this.disableCartButton = false;
      } else {
        this.disableCartButton = true;
      }
    },
  },
  mounted() {
    this.onPdpData();
    eventBus.$on('selectCheckedElement', (data) => {
      this.selectedRefillProductData.push(data);
    });
    eventBus.$on('selectUnCheckedElement', (data) => {
      this.selectedRefillProductData.forEach((el, index) => {
        if (data.code === el.code) {
          this.selectedRefillProductData.splice(
            this.selectedRefillProductData.indexOf(index),
          );
        }
      });
    });
    this.handleResize();
    window.addEventListener('resize', this.handleResize);
  },
  methods: {
    /**
     * This function is called on mounted which gets Refills products data
     */
    onPdpData() {
      this.$refs.spinner.showSpinner();
      const self = this;
      const pdpData = self.pdpProductsData;
      let refillProductData = [];
      // check for product refills
      if (pdpData.productReferences) {
        refillProductData = pdpData.productReferences.filter(
          item => item.referenceType === self.referenceTypes.refills,
        );
        self.refillProductIds = refillProductData.map(item => item.target.code);
      }
      if (self.refillProductIds.length > 0) {
        self.generateUrlRequest(pdpData.code);
      }
    },
    /**
     * This function is called on click of Add To Cart Button which adds that product to cart
     */
    handleAddToCart() {
      this.code = this.refillProductIds.join(':');
      this.code = this.selectedRefillProductData.map(ele => ele.code).join(':');
      this.quantity = this.selectedRefillProductData
        .map(ele => ele.quantity)
        .join(':');
      const requestObjParams = {
        qty: this.quantity,
        code: this.code,
      };
      const requestConfig = {};
      requestConfig.params = requestObjParams;
      this.pdpService.addRefillsProducts(
        requestConfig,
        this.handleAddRefillsProductsResponse,
        this.handleAddRefillsProductsError,
      );
      // sending the data to Google Analytics on Add to Cart button
      if (typeof dataLayer !== 'undefined') {
        this.selectedRefillProductData.map((product) => {
          this.analyticsService.trackAddToCart(product);
        });
      }
    },
    /**
     * This function handles the response of add refill products service which emits call success
     */
    handleAddRefillsProductsResponse(response) {
      if (response) {
        this.$emit('refills-success', this.i18n.refillsResponse);
        cartEventBus.$emit('call-basic-cart');
      }
    },
    /**
     * This function handles the error of add refill products service which emits call error
     */
    handleAddRefillsProductsError(error) {
      if (error) {
        this.$emit('refills-error', error.response.data.errors[0].message);
      }
    },
    /**
     * This function handles the response of refill products service which populates response on Refills Modal
     */
    handleRefillsProductsResponse(response) {
      if (response && response.data) {
        this.refillProductsData = response.data;
        this.refillProducts = this.refillProductsData.references.map(
          item => item.target,
        );
        this.$refs.spinner.hideSpinner();
      }
    },
    /**
     * This function handles the error of refill products service which hides the spinner
     */
    handleRefillsProductsError(error) {
      if (error) {
        this.$refs.spinner.hideSpinner();
      }
    },
    /**
     * This function to generate compare result url
     */
    generateUrlRequest(productId) {
      const self = this;
      const requestConfig = {};
      requestConfig.params = {
        referenceType: this.referenceTypes.refills,
      };
      self.pdpService.getProductsData(
        requestConfig,
        this.handleRefillsProductsResponse,
        this.handleRefillsProductsError,
        productId,
      );
    },
    /**
     * This function is called on click of No Thanks Button
     */
    handleNoThanks() {
      this.$emit('refills-no-thanks');
    },
    handleResize() {
      if (document.querySelector('.refill-container')) {
        if (this.isMobile()) {
          document.querySelector('.refill-container').style.height = `${window.innerHeight - 200}px`;
        } else {
          document.querySelector('.refill-container').style.height = '';
        }
      }
    },
    destroyed() {
      window.removeEventListener('resize', this.handleResize);
    },
  },
};
