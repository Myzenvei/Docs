import {
  eventBus
} from '../../../modules/event-bus';
import cookiesMixin from '../../common/mixins/cookies-mixin';
import globals from '../../common/globals.js';
import FocusTrap from 'vue-focus-lock';

/**
 * Compare products panel
 */

export default {
  props: ['comparedProducts', 'disableButtonFalse', 'maxProductsCompare', 'i18n'],
  name: 'vx-add-to-compare-panel',
  mixins: [cookiesMixin],
  components: {
    FocusTrap,
  },
  data() {
    return {
      buttonDisabled: false,
      globals,
      hideComparePanel: false,
      products: '',
    };
  },
  methods: {
    // Navigate to product comparison page
    viewDetails() {
      const self = this;
      let compareProductsString = '';
      const prodArr = self.products.map(item => item.code);
      compareProductsString = self.generateProductsString(prodArr);
      this.globals.navigateToUrlWithParams(
        'compareProducts',
        compareProductsString,
        'productCodes',
      );
    },
    // function to remove products from compare panel
    removePanel(event) {
      this.removeCompareProductFromCookie('CompareCookie', event);
      this.products.splice(this.products.indexOf(event), 1);
      eventBus.$emit('productLength', this.products.length);
      eventBus.$emit('removeFromPanel', event.code);
      this.$emit('panelDelete', event.code);
    },
    // function to remove product from compare cookie
    removeCompareProductFromCookie(cookieName, cookieVal) {
      if (this.readCookie(cookieName)) {
        const cookieData = JSON.parse(this.readCookie(cookieName));
        cookieData.forEach((item, index) => {
          if (item.code === cookieVal.code) {
            cookieData.splice(index, 1);
          }
        });
        this.createCookie(cookieName, JSON.stringify(cookieData));
      }
    },
    // function to generate query string
    generateProductsString(prodArr) {
      return prodArr.join(':');
    },
    closePanel() {
      this.hideComparePanel = !this.hideComparePanel;
    },
  },
  created() {
    const self = this;
    document.body.addEventListener('keyup', (e) => {
      if (e.keyCode === 27) {
        self.closePanel();
      }
    });
  },
  mounted() {
    this.products = this.comparedProducts;
  },
  watch: {
    // watch to update data passed by Prop
    comparedProducts(data) {
      this.$nextTick(() => {
        this.products = data;
      });
    },
  },
};
