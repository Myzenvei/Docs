/**
 * A mixin to format price with double and commas
 * */
const priceFormatMixin = {
  methods: {
    getFormattedPrice(unformattedPrice) {
      const price = unformattedPrice.toFixed(2);
      return `$${price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')}`;
    },
  },
};
export default priceFormatMixin;
