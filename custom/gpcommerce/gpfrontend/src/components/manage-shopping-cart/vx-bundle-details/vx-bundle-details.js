import vxCartProductTile from '../../manage-shopping-cart/vx-cart-product-tile/vx-cart-product-tile.vue';
import globals from '../../common/globals';
import BundleService from '../../common/services/bundle-service';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import { cartEventBus, globalEventBus } from '../../../modules/event-bus';

export default {
  name: 'vx-bundle-details',
  components: {
    vxCartProductTile,
    vxSpinner,
  },
  props: {
    bundleData: {
      type: Object,
      required: true,
    },
    installationPrice: {
      type: Object,
      required: true,
    },
    promotionData: {
      type: Object,
      required: true,
    },
    i18n: {
      type: Object,
      required: true,
    },
    customerLeaseable: {
      type: Boolean,
    },
  },
  data() {
    return {
      globals,
      bundleId: '',
      bundleNumber: '',
      bundleService: new BundleService(),
    };
  },
  created() {
    this.bundleId = this.bundleData.id;
    this.bundleNumber = this.bundleData.number;
  },
  computed: {
    updateBundleUrl() {
      return `${this.globals.getNavBaseUrl()}${this.globals.navigations.bundlePage}/?bundleId=${this.bundleId}&bundleNo=${this.bundleNumber}&update=true`;
    },
  },
  mounted() {

  },
  methods: {
    removeBundleFromCart() {
      this.$refs.spinner.showSpinner();
      this.bundleService.deleteBundle({}, (response) => {
        if (response.data) {
          cartEventBus.$emit('cart-update');
          globalEventBus.$emit('announce', 'product deleted');
          this.$refs.spinner.hideSpinner();
        }
      }, () => {
        this.$refs.spinner.hideSpinner();
      }, this.bundleNumber);
    },
    handleLeafBundleMaxCount(bundle) {
      return bundle.products.length === bundle.maxItemsAllowed;
    },
  },
};
