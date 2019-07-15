import vxProductDetailsTabSpecs from '../vx-product-details-tab-specs/vx-product-details-tab-specs.vue';
import globals from '../../common/globals';

export default {
  name: 'product-details-tab',
  components: {
    'vx-product-details-tab-specs': vxProductDetailsTabSpecs,
  },
  props: {
    /**
     * Details of the product
     */
    productDetailsTabData: {
      type: Object,
      required: true,
      default: {},
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
      globals,
    };
  },
  computed: {},
  mounted() {},
  methods: {},
};
