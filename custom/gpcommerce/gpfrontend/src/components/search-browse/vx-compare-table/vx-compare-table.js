import { eventBus } from '../../../modules/event-bus';
import mobileMixin from '../../common/mixins/mobile-mixin';

/**
 * Component for displaying the data of products in tabular form on compare results page
 */
export default {
  name: 'vx-compare-table',
  components: {},
  props: ['productData'],
  mixins: [mobileMixin],
  data() {
    return {
      removeData: '',
    };
  },
  computed: {},
  methods: {
    // function to find index of removed data
    getProductTileIndex() {
      const self = this;
      let productIndex = -1;
      this.productData.products.some((el, i) => {
        if (el.code === self.removeData) {
          productIndex = i;
          return true;
        }
      });
      this.removeProductSpecification(productIndex);
    },
    // function to remove data of removed product from compare table
    removeProductSpecification(productIndex) {
      this.productData.products.splice(productIndex, 1);
      this.productData.specifications.map((specification) => {
        specification.subCategories.map((subCategory) => {
          subCategory.columnValues.splice(productIndex, 1);
        });
      });
    },
    specCols(columnValues) {
      if (this.isMobile()) {
        return columnValues.slice(0, 2);
      }
      return columnValues;
    },
  },
  mounted() {
    eventBus.$on('comparePageRemove', (data) => {
      this.removeData = data;
      this.getProductTileIndex();
    });
  },
};
