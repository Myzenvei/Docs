import vxProductTile from '../../common/vx-product-tile/vx-product-tile.vue';
import vxAccordion from '../../common/vx-accordion/vx-accordion.vue';
import vxCartProductTile from '../../manage-shopping-cart/vx-cart-product-tile/vx-cart-product-tile.vue';
import globals from '../../common/globals';
import {
  checkoutEventBus,
} from '../../../modules/event-bus';
import vxProductIcons from '../../common/vx-product-icons/vx-product-icons.vue';
import {
  ProductAvailability,
  discount
} from '../../common/mixins/vx-enums';

export default {
  name: 'vx-overview',
  components: {
    vxProductTile,
    vxAccordion,
    vxCartProductTile,
    vxProductIcons,
  },
  props: {
    visibleEntries: Array,
    i18n: Object,
    totalItems: Number,
    promotionData: Object,
    splitApplicable: Boolean,
    totalPrice: Object,
  },
  data() {
    return {
      globals,
      isMultiple: false,
      restrictedEntries: [],
      ProductAvailability,
      restrictionError: false,
      giftWrapDetails: [],
      discount,
    };
  },
  computed: {},
  mounted() {
    checkoutEventBus.$on('shipping-multiple', () => {
      this.isMultiple = true;
    });
    checkoutEventBus.$on('shipping-single', () => {
      this.isMultiple = false;
    });
    // Restriction error
    checkoutEventBus.$on('restriction-error', (restrictedEntries) => {
      this.restrictedEntries = restrictedEntries;
      if (this.restrictedItems.length === 0) {
        this.restrictionError = false;
      } else {
        this.restrictionError = true;
      }
    });
    $('.vx-overview').popover({
      trigger: 'focus',
      content: `<p>${this.i18n.subscribePopoverText}</p><a href=${globals.getUrlWithContextPath(globals.navigations.learnMore)}>${
        this.i18n.subscribeLearn
      }</a>`,
      html: true,
      placement: 'bottom',
      container: '.vx-overview',
      selector: '.popover-subscribe',
    });
  },
  methods: {
    openTooltip() {
      this.$refs.subscribeInfo[0].children[0].setAttribute(
        'title',
        this.i18n.subscribeIconTitle,
      );
    },
    getCartQuantity() {
      let quantity = 0;
      if (this.visibleEntries.length > 0) {
        this.visibleEntries.map((item) => {
          quantity += item.quantity;
        });
      }
      return quantity;
    },
    restrictedItems(productCode) {
      let isRestricted = false;
      for (let i = 0; i < this.restrictedEntries.length; i += 1) {
        if (this.restrictedEntries[i].productCode === productCode) {
          isRestricted = true;
          break;
        }
      }
      return isRestricted;
    },
    getAddress(data) {
      let address = '';
      if (data.line2) {
        address = `${data.line1}, ${data.line2}, ${data.town}, ${data.region.isocodeShort}, ${
          data.postalCode
        }`;
      } else {
        address = `${data.line1}, ${data.town}, ${data.region.isocodeShort}, ${data.postalCode}`;
      }
      return address;
    },
    getShippingMethod(data) {
      const method = data.split('-').join(' ');
      return method;
    },
  },
};
