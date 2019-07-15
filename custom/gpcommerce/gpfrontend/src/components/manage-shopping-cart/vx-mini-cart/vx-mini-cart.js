import vxCartProductTile from '../vx-cart-product-tile/vx-cart-product-tile.vue';
// import shoppingCartData from '../vx-shopping-cart/vx-shopping-cart-mock-data';
import globals from '../../common/globals';
import vxEmptyCart from '../vx-empty-cart/vx-empty-cart.vue';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import ManageShoppingCartService from '../../common/services/manage-shopping-cart-service';


export default {
  name: 'vx-mini-cart',
  components: {
    vxCartProductTile,
    vxEmptyCart,
    vxSpinner,
  },
  props: {
    /**
     * text coming from property file
     */
    i18n: Object,
    isSampleCart: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      shoppingCartData: {},
      globals,
      displayCount: 3,
      manageShoppingCartService: new ManageShoppingCartService(),
      dataLoaded: false,
    };
  },
  computed: {},
  mounted() {
    this.callMiniCart();
  },
  methods: {
    /**
     * This function gets mini cart details
     */
    callMiniCart() {
      this.dataLoaded = false;
      this.manageShoppingCartService.getDefaultCart({}, this.handleMiniCartResponse, this.handleMiniCartError);
      this.$refs.spinner.showSpinner();
    },
    /**
     * This function handles the response of mini cart details call
     */
    handleMiniCartResponse(response) {
      if (this.$refs.spinner) {
        this.$refs.spinner.hideSpinner();
      }
      if (response && response.data) {
        this.shoppingCartData = response.data;
        this.dataLoaded = true;
      }
    },
    /**
     * This function handles the error of mini cart details call
     */
    handleMiniCartError(error) {
      if (this.$refs.spinner) {
        this.$refs.spinner.hideSpinner();
        this.dataLoaded = true;
      }
    },
    /**
     * This function shows last n elements from mini cart
     * @param  {Array} array products from mini cart response
     */
    lastNelements(array) {
      const filteredArray = array.filter(item => item.visible);
      return filteredArray.slice(Math.max(filteredArray.length - this.displayCount, 0));
    },
  },
};
