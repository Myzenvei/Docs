const ProductMixin = {
  data() {},
  mounted() {},
  methods: {
    displayAttributes(val) {
      if (!this.isTablet()) {
        if (val.length > 74) {
          return `${val.substr(0, 74)}<br>${this.displayAttributes(
            val.substr(74),
          )}`;
        }
        return val;
      }
      return val;
    },
  },
};

export default ProductMixin;
