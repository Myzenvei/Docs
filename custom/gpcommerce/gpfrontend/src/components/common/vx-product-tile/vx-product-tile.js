import globals from '../../common/globals';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import vxStepperControl from '../../common/vx-stepper-control/vx-stepper-control.vue';
import vxNotifyMe from '../../search-browse/vx-notify-me/vx-notify-me.vue';
import flyoutBannerMixin from '../../common/vx-flyout-banner/vx-flyout-banner-mixin';
import vxModal from '../../common/vx-modal/vx-modal.vue';

export default {
  name: 'vx-product-tile',
  mixins: [flyoutBannerMixin],
  components: {
    vxSpinner,
    vxStepperControl,
    vxNotifyMe,
    vxModal,
  },
  props: {
    verticle: {
      type: Boolean,
      default: false,
    },
    layout: {
      type: String,
    },
  },
  data() {
    return {
      globals,
      savedAmount: 0,
      pdpContextPath: '',
    };
  },
  computed: {},
  mounted() {},
  methods: {
    setQuantity(newQty) {
      this.product.quantity = newQty;
    },
    handleQuantity(quantity) {
      this.savedAmount = this.product.price.value * quantity;
      this.product.quantity = quantity;
      this.product.price.finalPrice = this.product.price.value * quantity;
      this.$emit('quantity-updated', this.product);
    },
    notifyMe() {
      this.$refs.notifyMeModal.open();
    },
    onNotifyMeError(error) {
      this.$refs.notifyMeModal.close();
      this.showFlyout('error', error, true);
    },
    onNotifyMeSuccess(success) {
      this.$refs.notifyMeModal.close();
      this.showFlyout('success', success, true);
    },
    deleteCartItem() {
      this.$emit('item-deleted', this.product);
    },
  },
};
