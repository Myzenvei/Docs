export default {
  name: 'vx-add-product-to-list',
  components: {},
  props: {
    i18n: {
      type: Object,
    },
  },
  data() {
    return {
      productNames: '',
    };
  },
  computed: {},
  created() {
    window.addEventListener('click', () => {
      this.$emit('closeAddProductToList');
    });
  },
  mounted() {},
  methods: {
    handleAddProductsToList() {
      this.$emit('addProductsToList', this.productNames);
    },
    closeAddProductToList() {
      this.$emit('closeAddProductToList');
    },
  },
};
