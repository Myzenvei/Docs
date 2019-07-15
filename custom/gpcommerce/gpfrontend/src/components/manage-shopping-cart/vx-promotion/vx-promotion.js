import uniq from 'lodash/uniq';
import includes from 'lodash/includes';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import globals from '../../common/globals';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import vxVoucherDetails from '../../manage-shopping-cart/vx-voucher-details/vx-voucher-details.vue';
import { cartEventBus } from '../../../modules/event-bus';
import { checkoutEventBus } from '../../../modules/event-bus';
import ManageShoppingCartService from '../../common/services/manage-shopping-cart-service';

export default {
  name: 'vx-promotion',
  components: {
    vxSpinner,
    vxModal,
    vxVoucherDetails,
  },
  props: {
    /**
     * text coming from property file
     */
    i18n: Object,
    /**
     * contains promotion data
     */
    promotionData: Array,
    voucherData: Array,
  },
  data() {
    return {
      globals,
      voucherCode: '',
      voucherObject: {},
      showPromoError: false,
      promoErrorMsg: '',
      manageShoppingCartService: new ManageShoppingCartService(),
      appliedCodes: [],
    };
  },
  computed: {},
  created() {
    this.appliedPromotions();
  },
  mounted() {},
  methods: {
    /**
     * This function handles apply voucher call
     */
    applyVoucher() {
      const requestConfig = {};
      requestConfig.params = {
        voucherId: this.voucherCode,
      };
      this.manageShoppingCartService.applyVoucher(
        requestConfig,
        this.handleVoucherApplyResponse,
        this.handleVoucherApplyError,
      );
      this.$refs.spinner.showSpinner();
    },
    /**
     * This function handles the repsonse of apply voucher call
     */
    handleVoucherApplyResponse() {
      const calculationType = 'cart';
      this.$refs.spinner.hideSpinner();
      this.showPromoError = false;
      this.voucherCode = '';
      cartEventBus.$emit('cart-update');
      checkoutEventBus.$emit('update-cart', calculationType);
    },
    /**
     * This function handles the error of apply voucher call
     */
    handleVoucherApplyError(error) {
      this.$refs.spinner.hideSpinner();
      if (error.response) {
        const errorCodeArray = error.response.data.errors;
        const errorCode = errorCodeArray[0].code;
        if (errorCode === '108') {
          this.promoErrorMsg = this.i18n[errorCode];
        } else {
          this.promoErrorMsg = errorCodeArray[0].message;
        }
        this.showPromoError = true;
      }
      cartEventBus.$emit('cart-update-error');
    },
    /**
     * This function handles delete voucher call
     * @param  {String} code voucher code
     */
    deleteVoucher(code) {
    	this.showPromoError = false;
    	this.manageShoppingCartService.deleteVoucher(
        {},
        this.handleVoucherDeleteResponse,
        this.handleVoucherDeleteError,
        code,
      );
      this.$refs.spinner.showSpinner();
    },
    /**
     * This function gets voucher details
     * @param  {Object} item voucher object
     */
    voucherDetails(item, event) {
      if (item && !item.description) {
        return;
      }
      this.voucherObject = item;
      if (document.querySelector('.order-summary-section')) {
        document.querySelector('.order-summary-section').style.zIndex = 9;
        document.querySelector('.order-summary-section').scrollIntoView(false);
      } else if (document.querySelector('.checkout-order-summary-details')) {
        document.querySelector(
          '.checkout-order-summary-details',
        ).style.zIndex = 9;
        document
          .querySelector('.checkout-order-summary-details')
          .scrollIntoView(false);
      }
      this.$refs.promotionDetailsModal.open(event);
    },
    /**
     * This function handles response of voucher details calls
     */
    handleVoucherDeleteResponse() {
      const calculationType = 'cart';
      this.$refs.spinner.hideSpinner();
      cartEventBus.$emit('cart-update');
      checkoutEventBus.$emit('update-cart', calculationType);
    },
    /**
     * This function handles error of voucher details calls
     */
    handleVoucherDeleteError() {
      this.$refs.spinner.hideSpinner();
      cartEventBus.$emit('cart-update-error');
    },
    /**
     * This function closes the voucher modal
     */
    closeVoucherModal() {
      this.$refs.promotionDetailsModal.close();
      if (document.querySelector('.order-summary-section')) {
        document.querySelector('.order-summary-section').style.zIndex = '';
      } else if (document.querySelector('.checkout-order-summary-details')) {
        document.querySelector('.checkout-order-summary-details').style.zIndex =
          '';
      }
    },
    /**
     * This function shows promotional value in order summary
     * @param  {String} promoText promo text
     */
    calculateVoucherAmount(promoText) {
      const data = promoText.split(' ');
      if (~promoText.indexOf('$') && ~promoText.indexOf('%')) {
        // Always choose % over $
        return data.filter((word) => {
          if (word.indexOf('%') !== -1) {
            return word;
          }
        })[0];
      }
      return data.filter((word) => {
        if (word.indexOf('$') !== -1 || word.indexOf('%') !== -1) {
          return word;
        }
      })[0];
    },
    appliedPromotions(coupon) {
      const completeCodes = [];
      for (const value in this.promotionData) {
        completeCodes.push(this.promotionData[value].appliedCouponCodes[0]);
      }
      const uniqCodes = uniq(completeCodes);
      return !includes(uniqCodes, coupon);
    },
  },
};
