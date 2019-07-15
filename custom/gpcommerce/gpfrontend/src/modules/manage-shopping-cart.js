import vxShoppingCart from '../components/manage-shopping-cart/vx-shopping-cart/vx-shopping-cart.vue';
import vxMiniCart from '../components/manage-shopping-cart/vx-mini-cart/vx-mini-cart.vue';
import vxOrderSummary from '../components/manage-shopping-cart/vx-order-summary/vx-order-summary.vue';
import vxCartGuid from '../components/manage-shopping-cart/vx-cart-guid/vx-cart-guid.vue';
import vxCartSuggestions from '../components/manage-shopping-cart/vx-cart-suggestions/vx-cart-suggestions.vue';
import vxCartCheckout from '../components/manage-shopping-cart/vx-cart-checkout/vx-cart-checkout.vue';

const manageShoppingCartModule = {
  data: {
    cartExists: false,
    orderSummaryClicked: false,
    cartHasVisibleItems: true,
    leaseError: false,
    cartData: {},
    cartDataLoaded: false,
  },
  components: {
    vxShoppingCart,
    vxMiniCart,
    vxOrderSummary,
    vxCartGuid,
    vxCartSuggestions,
    vxCartCheckout,
  },
  mounted() {
    this.$root.$on('orderSummaryScrollUp', () => {
      this.orderSummaryClicked = !this.orderSummaryClicked;
    });
    this.$root.$on('cartHasVisibleItems', (value) => {
      this.cartHasVisibleItems = value;
    });
    this.$root.$on('showLeaseError', (value) => {
      this.leaseError = value;
    });
    this.$root.$on('full-cart-called', (value) => {
      this.cartData = value;
      this.cartDataLoaded = true;
    });
  },
  methods: {},
};

export default manageShoppingCartModule;
