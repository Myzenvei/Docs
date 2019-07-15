/* Handles prices on order summary section on cart, checkout and orderConfrimation */
import mobileMixin from '../../common/mixins/mobile-mixin';
import globals from '../../common/globals';
import { checkoutEventBus } from '../../../modules/event-bus';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import { pageType } from '../../common/mixins/vx-enums';

export default {
  name: 'vx-order-summary-details',
  components: {
    vxModal,
  },
  props: {
    /**
     * Labels, button and caption texts
     */
    i18n: Object,
    /**
     * page identifier
     */
    page: String,
    /**
     * Order summary details
     */
    orderSummaryData: Object,
    /**
     * to check for giftable products
     */
    giftable: Boolean,
    /**
     * to check for installable products
     */
    installable: Boolean,
    isShippingMultiple: Boolean,
  },
  data() {
    return {
      globals,
      orderSummaryClicked: false,
      isGiftable: false,
      isInstallable: false,
      pageType,
    };
  },
  mixins: [mobileMixin],
  computed: {},
  mounted() {
    this.isGiftable = this.giftable;
    this.isInstallable = this.installable;
  },
  methods: {
    openOrderSummary(event) {
      this.orderSummaryClicked = !this.orderSummaryClicked;
      this.$root.$emit('orderSummaryScrollUp', this.orderSummaryClicked);
    },
    openInstallationModal() {
      this.$refs.installableMoreInfo.open();
    },
    getProgressbarStyle(orderSummaryData) {
      const freeShippingLimitBalance = parseInt(orderSummaryData.freeShippingLimit, 10) || 0;
      const subtotal = parseInt(orderSummaryData.subTotal.value, 10);
      const progressPercentage = (subtotal * 100) / (subtotal + freeShippingLimitBalance);
      const obj = {
        width: `${progressPercentage}%`,
      };
      return obj;
    },
  },
};
