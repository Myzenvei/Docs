/**
 * Component for suggested products
 */
import vxSlider from '../../pdp/vx-slider/vx-slider.vue';
import globals from '../../common/globals';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import {
  cartEventBus
} from '../../../modules/event-bus';
import ManageShoppingCartService from '../../common/services/manage-shopping-cart-service';

export default {
  name: 'vx-cart-suggestions',
  components: {
    vxSlider,
    vxSpinner,
  },
  props: {
    /**
     * Title for the carousel
     */
    title: {
      type: String,
      default: '',
    },
    /**
     * Identifier for Quick Order
     */
    uid: {
      type: String,
      default: '',
    },
    /**
     * Indicator for Favorites enabled
     */
    isFavorites: {
      type: Boolean,
    },
    isBazaarVoice: {
      type: String,
    },
    i18n: {
      type: Object,
      default: {},
    },
  },
  data() {
    return {
      globals,
      suggestionsData: [],
      productIds: {},
      spinnerComponent: null,
      manageShoppingCartService: new ManageShoppingCartService(),
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
  created() {

  },
  mounted() {
    this.spinnerComponent = this.$refs.spinner;
    this.suggestionsCall();
    cartEventBus.$on('cart-update', () => {
      this.suggestionsCall();
    });
    cartEventBus.$on('quick-order-load', (data) => {
      this.productIds = data;
      this.suggestionsCall();
    });
  },
  methods: {
    /**
     * This function gets suggestion details
     */
    suggestionsCall() {
      if (this.uid === 'gpQuickOrderSuggestions') {
        const requestConfig = {};
        requestConfig.data = this.productIds;
        this.manageShoppingCartService.quickOrderSuggestions(requestConfig, this.handleSuggetionsResponse, this.handleSuggestionsError);
      } else {
        this.manageShoppingCartService.getCartSuggestions({}, this.handleSuggetionsResponse, this.handleSuggestionsError);
      }
      this.spinnerComponent.showSpinner();
    },
    /**
     * This function handles the response of suggestion call
     */
    handleSuggetionsResponse(response) {
      this.spinnerComponent.hideSpinner();
      this.suggestionsData = response.data;
    },
    /**
     * This function handles the error of suggestion call
     */
    handleSuggestionsError(error) {
      this.spinnerComponent.hideSpinner();
    },
  },
};
