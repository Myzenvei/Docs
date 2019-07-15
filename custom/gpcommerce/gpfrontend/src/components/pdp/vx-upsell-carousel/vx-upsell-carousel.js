import vxSlider from '../vx-slider/vx-slider.vue';
import { eventBus } from '../../../modules/event-bus';
import globals from '../../common/globals';
import messages from '../../../locale/messages';
import PdpService from '../../common/services/pdp-service';

export default {
  name: 'vx-upsell-carousel',
  components: {
    vxSlider,
  },
  props: {
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
     * Copy text coming from properties files
     */
    i18n: {
      type: Object,
    },
  },
  data() {
    return {
      upsellProductIds: [],
      globals,
      messages: messages['en-US'],
      upsellProductData: [],
      pdpService: new PdpService(),
      upsellProducts: [],
      sliderSlides: {
        desktop: {
          slides: 4,
          spaceAllowed: 56,
        },
        tablet: {
          slides: 4,
          spaceAllowed: 16,
        },
        mobile: {
          slides: 2,
          spaceAllowed: 16,
        },
      },
    };
  },
  computed: {},
  mounted() {
    const self = this;
    eventBus.$on('upsellData', (data) => {
      self.upsellProductIds = data;
      self.onUpsellData();
    });
  },
  methods: {
    /**
     * This function is called on mounted to get Upsell Products Data
     */
    onUpsellData() {
      const self = this;
      self.generateUrlRequest(self.upsellProductIds);
    },
    /**
     * This function handles the response of upselling products service which populates the upsell Carousel
     */
    handleUpsellProductsResponse(response) {
      const self = this;
      if (response && response.data) {
        self.upsellProductData = response.data;
        self.upsellProducts = this.upsellProductData.references.map(item => item.target);
      }
    },
    /**
     * This function handles the error of upselling products service
     */
    handleUpsellProductsError(error) {},
    /**
     * This function generates compare result url
     */
    generateUrlRequest(productId) {
      const self = this;
      const requestConfig = {};
      requestConfig.params = {
        referenceType: 'UPSELLING'
      };
      self.pdpService.getProductsData(
        requestConfig,
        this.handleUpsellProductsResponse,
        this.handleUpsellProductsError,
        productId,
      );
    },
  },
};
