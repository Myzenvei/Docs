import vxProductTileVertical from '../vx-product-tile-vertical/vx-product-tile-vertical.vue';
import vxAddToComparePanel from '../vx-add-to-compare-panel/vx-add-to-compare-panel.vue';
import {
  eventBus
} from '../../../modules/event-bus';
import cookiesMixin from '../../common/mixins/cookies-mixin';
import mobileMixin from '../../common/mixins/mobile-mixin';

/**
 * Component for displaying search result products
 */
export default {
  name: 'vx-list-page',
  data() {
    return {
      compareProduct: [],
      disableButtonFalse: false,
      totalCheckedCount: 0,
      uncheckProduct: false,
      receivedCount: 0,
      assetLevel: '',
      showCompareCheckbox: true,
      viewportCount: 4,
      viewportSize: 0,
    };
  },
  props: ['products', 'isFavorites', 'isBazaarVoice', 'isBulkEnabled', 'searchBrowseI18n', 'singleProductEnabled'],
  mixins: [cookiesMixin, mobileMixin],
  components: {
    vxProductTileVertical,
    'vx-panel-view': vxAddToComparePanel,
  },
  created() {
    this.viewportSize = window.innerWidth;
    this.getViewportSize(this.viewportSize);
  },
  watch: {
    products() {
      this.$nextTick(() => {
        eventBus.$emit('editedCompare', this.compareProduct);
      });
    },
  },
  methods: {
    getViewportSize(e) {
      if (e < 768) {
        this.viewportCount = 2;
      }
    },
    // function to add products to be compared
    addToCompare(event) {
      this.updateTotalCount();
      // check for first product for comparison
      if (this.totalCheckedCount === 1 && event.assetCode) {
        this.assetLevel = event.assetCode;
        this.checkboxClicked(event);
      }
      // check for comparison conditions if panel has one or more product
      if (
        this.totalCheckedCount > 1 &&
        this.totalCheckedCount <= this.viewportCount &&
        event.assetCode
      ) {
        if (this.assetLevel === event.assetCode) {
          this.checkboxClicked(event);
        } else {
          this.uncheckProduct = true;
        }
      }
      // commented code for future purpose
      // else if (this.receivedCount > 0) {
      //   this.uncheckProduct = true;
      // }
    },
    checkboxClicked(event) {
      this.uncheckProduct = false;
      this.updateCompareProductData();
      this.toggleCompareButton();
      eventBus.$emit('editedCompare', this.compareProduct);
    },
    // function to disable view details button if compared products is less than 2
    toggleCompareButton() {
      if (this.compareProduct.length < 2) {
        this.disableButtonFalse = true;
      } else {
        this.disableButtonFalse = false;
      }
    },
    // function to remove product from compare panel
    removeFromCompare(event) {
      this.updateCompareProductData();
      this.toggleCompareButton();
      this.updateTotalCount();
      eventBus.$emit('editedCompare', this.compareProduct);
    },
    // Function to update panel count on removal of product from campare panel
    updateTotalCountPanel() {
      this.totalCheckedCount = this.totalCheckedCount - 1;
      eventBus.$emit('editedCompare', this.compareProduct);
    },
    updateTotalCount() {
      const cookieData = this.getCompareCookieData();
      if (cookieData) {
        this.totalCheckedCount = cookieData.length;
      } else {
        this.totalCheckedCount = 0;
      }
    },
    updateCounter(count) {
      this.receivedCount = +count;
    },
    getCompareCookieData() {
      let cookieData = this.readCookie('CompareCookie');
      if (cookieData) {
        cookieData = JSON.parse(cookieData);
      } else {
        cookieData = null;
      }
      return cookieData;
    },
    updateCompareProductData() {
      const self = this;
      if (!this.isBulkEnabled) {
        const cookieData = this.getCompareCookieData();
        if (cookieData) {
          self.compareProduct = cookieData;
        } else {
          self.compareProduct = [];
        }
      }
    },
    setAssetCode() {
      const cookieData = this.getCompareCookieData();
      if (cookieData && cookieData.length !== 0) {
        this.assetLevel = cookieData[0].assetCode;
      } else {
        this.assetLevel = null;
      }
    },
  },
  mounted() {
    const self = this;
    self.updateCompareProductData();
    self.setAssetCode();
  },
  updated() {
    eventBus.$on('productLength', data => (this.compareProduct.length = data));
    this.toggleCompareButton();
  },
};
