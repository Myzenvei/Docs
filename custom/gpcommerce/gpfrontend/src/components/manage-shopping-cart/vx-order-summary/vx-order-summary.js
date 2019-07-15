/* Handles order summary section on cart, checkout and orderConfrimation */
import vxPromotion from '../../manage-shopping-cart/vx-promotion/vx-promotion.vue';
// import orderSummaryData from './vx-order-summary-mock-data';
// import orderSummaryInitializedData from './vx-order-summary-initialized-data';
import globals from '../../common/globals';
import vxOrderSummaryDetails from '../../common/vx-order-summary-details/vx-order-summary-details.vue';
import {
  cartEventBus,
  checkoutEventBus
} from '../../../modules/event-bus';
import mobileMixin from '../../common/mixins/mobile-mixin';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import vxTermsCondition from '../../common/vx-terms-condition/vx-terms-condition.vue';
import {
  pageType
} from '../../common/mixins/vx-enums';

export default {
  name: 'vx-order-summary',
  components: {
    vxPromotion,
    vxOrderSummaryDetails,
    vxTermsCondition,
    vxModal,
  },
  props: {
    /**
     * text coming from property file
     */
    i18n: Object,
    /**
     * Order details from order confirmation
     */
    orderDetails: Object,
    /**
     * page identifier
     */
    page: String,
    /**
     * to check for giftable products
     */
    isGiftable: Boolean,
    /**
     * to check for installable products
     */
    isInstallable: Boolean,
    /**
     * to check if opted for single or multiple shipping
     */
    isShippingMultiple: Boolean,
    /**
     * stores contact number
     */
    contactNumber: String,
  },
  watch: {
    orderDetails(newVal, oldVal) { // watch it
      this.orderSummaryData = this.orderDetails;
      this.dataLoaded = true;
    },
  },
  mixins: [mobileMixin],
  data() {
    return {
      orderSummaryData: {},
      globals,
      orderSummaryClicked: false,
      dataLoaded: false,
      pageType,
      termsConditionText: this.i18n.termsConditionInfo,
    };
  },
  created() {},
  computed: {},
  mounted() {
    if (this.page === pageType.checkout) {
      this.orderSummaryClicked = !this.orderSummaryClicked;
    }
    this.$root.$on('orderSummaryScrollUp', () => {
      this.orderSummaryClicked = !this.orderSummaryClicked;
      this.adjustFooterView();
    });
    this.orderSummaryData = this.orderDetails;
    this.dataLoaded = true;
    if (this.page === pageType.cart && this.isTablet()) {
      this.adjustFooterView();
    }
    this.orderSummaryHeightAdjust();
    window.addEventListener('resize', this.orderSummaryHeightAdjust);
    if (this.globals.getIsSubscription()) {
      this.termsConditionText = this.i18n.subscribeTermsConditionInfo;
    }
  },
  methods: {
    /**
     * This function prevents the Footer links from hiding behind the Summary section
     */
    adjustFooterView() {
      setTimeout(function () {
        if (document.querySelector('.footer-section')) {
          document.querySelector('.footer-section').style.paddingBottom = document.querySelector('.order-summary-section').offsetHeight + 'px';
        } else if (document.querySelector('.vx-employee-footer')) {
          document.querySelector('.vx-employee-footer').style.paddingBottom = document.querySelector('.order-summary-section').offsetHeight + 'px';
        }
      }, 50);
    },
    /**
     * This function opens the terms and condition modal
     */
    openTermsModal(event) {
      if (document.querySelector('.checkout-order-summary-details')) {
        document.querySelector('.checkout-order-summary-details').style.zIndex = 9;
        document.querySelector('.order-summary').scrollIntoView(false);
      }
      this.$refs.termsConditionModal.open(event);
    },
    /**
     * This function closes the terms and condition modal
     */
    closetermsConditionModal() {
      this.$refs.termsConditionModal.close();
      if (document.querySelector('.checkout-order-summary-details')) {
        document.querySelector('.checkout-order-summary-details').style.zIndex = '';
      }
    },
    /**
     * This function maintains the height of order summary section
     */
    orderSummaryHeightAdjust() {
      if (this.page === pageType.cart) {
        if (this.isMobile() || this.isTablet()) {
          document.querySelector('.vx-order-summary').style.maxHeight = (window.innerHeight - 186) + 'px';
        } else {
          document.querySelector('.vx-order-summary').style.maxHeight = '';
        }
      }
    }
  },
  destroyed() {
    window.removeEventListener('resize', this.orderSummaryHeightAdjust);
  }
};
