export default {
  name: 'vx-empty-cart',
  components: {},
  props: {
    /**
     * text coming from property file
     */
    i18n: Object,
    /**
     * indicactes for mini cart
     */
    isMiniCart: Boolean,
    /**
     * indicactes to show the message for empty cart
     */
    msgVisible: {
      type: Boolean,
      default: false,
    }
  },
  data() {
    return {};
  },
  computed: {},
  mounted() {},
  methods: {},
};
