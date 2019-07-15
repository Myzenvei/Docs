import vxMultipleShippingMethod from '../vx-multiple-shipping-method/vx-multiple-shipping-method.vue';
import vxSingleShippingMethod from '../vx-single-shipping-method/vx-single-shipping-method.vue';
import globals from '../../common/globals';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import {
  checkoutEventBus,
} from '../../../modules/event-bus';
import detectDeviceMixin from '../../common/mixins/detect-device-mixin';

export default {
  name: 'vx-shipping-method',
  mixins: [detectDeviceMixin],
  components: {
    vxMultipleShippingMethod,
    vxSingleShippingMethod,
    vxSpinner,
  },
  props: {
    i18n: Object,
    checkoutData: Object,
    isShippingMultiple: {
      default: false,
      type: Boolean,
    },
    sectionIndex: Number,
    splitApplicable: Boolean,
    addressChanged: Boolean,
  },
  data() {
    return {
      isEditable: true,
      globals,
    };
  },
  computed: {},
  created() {
    checkoutEventBus.$on('show-saved-mode', () => {
      this.isEditable = false;
      checkoutEventBus.$emit('section-complete', 'shippingMethod');
      checkoutEventBus.$emit('shipping-method-added');
    });
    checkoutEventBus.$on('show-edit-mode', () => {
      this.isEditable = true;
      checkoutEventBus.$emit('section-edit', 'shippingMethod');
      checkoutEventBus.$emit('shipping-method-edit');
    });
  },
  mounted() {},
  methods: {
    editMethod(event) {
      this.isEditable = true;
      checkoutEventBus.$emit('section-edit', 'shippingMethod');
      checkoutEventBus.$emit('shipping-method-edit');
    },
  },
};
