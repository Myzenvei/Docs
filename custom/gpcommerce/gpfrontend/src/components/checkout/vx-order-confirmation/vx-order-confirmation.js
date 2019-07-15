/* Handles the order confirmation page */
import vxOrderSummary from '../../manage-shopping-cart/vx-order-summary/vx-order-summary.vue';
import vxProductTile from '../../common/vx-product-tile/vx-product-tile.vue';
import globals from '../../common/globals';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import fecha from 'fecha';
import flyoutBannerMixin from '../../common/vx-flyout-banner/vx-flyout-banner-mixin';
import vxCancelOrder from '../../manage-transaction-history/vx-cancel-order/vx-cancel-order.vue';
import CjEventService from '../../../copper-crane/components/common/services/cjEvent-service';
import {
  ProductAvailability,
  brandNames,
  discount,
  order,
  paymentTypes,
} from '../../common/mixins/vx-enums';
import { globalEventBus } from '../../../modules/event-bus';
import CheckoutService from '../../common/services/checkout-service';
import vxExtole from '../../../copper-crane/components/common/vx-extole/vx-extole.vue';
import vxReorderProduct from '../../common/vx-reorder-product/vx-reorder-product.vue';

export default {
  name: 'vx-order-confirmation',
  components: {
    vxOrderSummary,
    vxProductTile,
    vxSpinner,
    vxModal,
    vxCancelOrder,
    vxExtole,
    vxReorderProduct,
  },
  mixins: [flyoutBannerMixin],
  props: {
    /**
     * Labels, button and caption texts
     */
    i18n: Object,
    contactNumber: String,
  },
  data() {
    return {
      country: 'United States',
      orderDetails: {},
      globals,
      order,
      orderCode: '',
      formattedEntries: {},
      isMultiple: false,
      isGiftable: false,
      isInstallable: false,
      dataLoaded: false,
      installableProducts: [],
      promotionData: {},
      leaseAgrementData: {},
      ProductAvailability,
      brandNames,
      showCancel: false,
      discount,
      checkoutService: new CheckoutService(),
      cjEventService: new CjEventService(),
      giveAwayCouponCode: '',
      giveAwayCouponName: '',
      showReferPopup: true,
      paymentTypes,
    };
  },
  computed: {},
  mounted() {
    this.getOrderCode();
    this.getOrderDetails();
    globalEventBus.$emit('announce', this.i18n.orderConfirmation.orderEmail);
    $('.vx-order-confirmation').popover({
      trigger: 'click',
      content: `<p>${
        this.i18n.orderConfirmation.subscribePopoverText
      }</p><a href=${globals.getUrlWithContextPath(globals.navigations.learnMore)}>${
        this.i18n.orderConfirmation.subscribeLearn
      }</a>`,
      html: true,
      placement: 'bottom',
      container: '.vx-order-confirmation',
      selector: '.popover-subscribe',
    });
  },
  methods: {
    openTooltip() {
      this.$refs.subscribeInfo[0].children[0].setAttribute(
        'title',
        this.i18n.orderConfirmation.subscribeIconTitle,
      );
    },
    printPage(event) {
      window.print();
    },
    getOrderCode() {
      const urlData = window.location.pathname.split('/');
      this.orderCode = urlData[urlData.length - 1];
    },
    formAddressString(address) {
      let userAddress = '';
      let companyName = '';
      const name = `${address.firstName} ${address.lastName}`;
      if (address && address.companyName) {
        companyName = address.companyName;
      }
      if (address) {
        userAddress =
          `${name},` +
          `${companyName},` +
          `${address.line1},` +
          `${address.line2},` +
          `${address.town},` +
          `${address.region.isocodeShort},` +
          `${address.postalCode},` +
          `${address.country.isocode}`;
      }
      return userAddress;
    },
    getOrderDetails() {
      this.checkoutService.getOrderDetails(
        {},
        this.handleGetOrderDetailsResponse,
        this.handleGetOrderDetailsError,
        this.orderCode,
      );
      this.$refs.spinner.showSpinner();
    },
    handleGetOrderDetailsResponse(response) {
      if (response && response.data) {
        this.orderDetails = response.data;
        if (this.orderDetails.appliedOrderPromotions) {
          this.orderDetails.appliedOrderPromotions.map((promotion) => {
            if (promotion.giveAwayCouponCodes.length) {
              promotion.giveAwayCouponCodes.map((coupon) => {
                this.giveAwayCouponCode = coupon.couponCode;
                this.giveAwayCouponName = coupon.name;
              });
            }
          });
        }
        this.showCancel = this.orderDetails.cancellable;
        this.orderDetails.finalAddress = this.formAddressString(
          this.orderDetails.deliveryAddress,
        );
        this.formattedEntries = this.arrayToObject(
          this.orderDetails.entries,
          'entryNumber',
        );
        this.shippingType(this.orderDetails.deliveryGroup);
        this.dataLoaded = true;
        this.promotionData = this.getProductPromotions();
        if (!this.orderDetails.cancellable) {
          this.showReferPopup = false;
        }
        if (this.globals.siteConfig.isReferAFriend && this.showReferPopup) {
          this.cjEventService.initiateCJEventOnOrderConfirmation(
            this.orderDetails,
          );
          this.$refs.vxExtoleRef.initiateExtoleOnOrderConfirmation(
            this.orderDetails,
          );
        }
        this.$refs.spinner.hideSpinner();
      }
    },
    handleGetOrderDetailsError() {
      this.$refs.spinner.hideSpinner();
    },
    // check if shipping is single or multiple
    shippingType(deliveryGroup) {
      if (deliveryGroup.length > 1) {
        this.isMultiple = true;
      } else {
        this.isMultiple = false;
      }
    },
    // check if we need to show Lease Option
    checkLeasable(array) {
      let isLeasable = false;
      array.map((group) => {
        group.value.splitEntries.map((item) => {
          if (this.formattedEntries[item.entryNumber].leasable) {
            isLeasable = true;
          }
        });
      });
      return isLeasable;
    },
    // check if the product is selected for gift
    checkGiftable(product) {
      let isGift = false;
      if (product.additionalAttributes) {
        if (
          _.find(product.additionalAttributes.entry, {
            key: 'giftOpted',
            value: 'true',
          })
        ) {
          isGift = true;
          this.isGiftable = isGift;
        }
      }
      return isGift;
    },
    // check if the product is selected for installation
    checkInstallable(product) {
      let installable = false;
      if (product.additionalAttributes) {
        if (
          _.find(product.additionalAttributes.entry, {
            key: 'installed',
            value: 'true',
          })
        ) {
          installable = true;
          this.installableProducts[product.entryNumber] = product.product.code;
          this.isInstallable = installable;
        }
      }
      return installable;
    },
    // get install date
    getInstallDate(date) {
      fecha.masks.myMask = 'MM/DD/YYYY';
      const dateObj = fecha.parse(date, 'dddd MMMM Do, YYYY');
      return fecha.format(dateObj, 'myMask');
    },
    // Check if single or multiple shipping is used
    getShippingMethod(data) {
      const method = data.split('-').join(' ');
      return method;
    },
    getDateByTimeZone(date) {
      if (date) {
        const modifiedTime = date.substring(0, 19);
        const utcDate = new Date(Date.parse(modifiedTime));
        const utcTime = utcDate.getTime();
        // If you want to display date according to current timezone uncomment below line
        // const currentDate = new Date();
        // const estOffset = currentDate.getTimezoneOffset() * 60000;
        // const timeZoneName = currentDate.toTimeString().substring(18);
        const estOffset = 300 * 60000;
        const currentTime = utcTime - estOffset;
        return fecha.format(new Date(currentTime), 'mediumDate');
      }
    },
    getProductPromotions() {
      const promotionProductData = {};
      for (
        let i = 0;
        i < this.orderDetails.appliedProductPromotions.length;
        i += 1
      ) {
        for (
          let j = 0;
          j <
          this.orderDetails.appliedProductPromotions[i].consumedEntries.length;
          j += 1
        ) {
          const orderEntryNumber = this.orderDetails.appliedProductPromotions[i]
            .consumedEntries[j].orderEntryNumber;
          const description = this.orderDetails.appliedProductPromotions[i]
            .description;
          if (!this.orderDetails.entries[orderEntryNumber].promotionsRevoked) {
            promotionProductData[orderEntryNumber] = description;
          }
        }
      }
      return promotionProductData;
    },
    arrayToObject(array, keyField) {
      const modifiedObj = array.reduce((obj, item) => {
        obj[item[keyField]] = item;
        return obj;
      }, {});
      return modifiedObj;
    },

    showLeaseAgreement(event) {
      this.checkoutService.showLeaseAgreement(
        {},
        this.handleLeaseAgreementResponse,
        this.handleLeaseAgreementError,
        this.orderDetails.code,
      );
      this.$refs.spinner.showSpinner();
    },

    handleLeaseAgreementResponse(response) {
      if (response && response.data) {
        this.leaseAgrementData = response.data;
        this.$refs.spinner.hideSpinner();
        this.$refs.viewTermsModal.open();
      }
    },

    handleLeaseAgreementError(error) {
      this.$refs.spinner.hideSpinner();
    },
    handleCancelOrder(event) {
      this.$refs.cancelOrder.open(event);
    },
    handleCancelOrderSuccess() {
      this.$refs.cancelOrder.close();
      this.showReferPopup = false;
      this.showFlyout(
        'success',
        this.i18n.orderConfirmation.cancelOrderSuccessMsg,
        false,
      );
      this.showCancel = false;
      this.getOrderDetails();
    },
    handleCancelOrderError() {
      this.$refs.cancelOrder.close();
      this.showFlyout(
        'error',
        this.i18n.orderConfirmation.cancelOrderErrorMsg,
        true,
      );
    },
    handleKeepOrder() {
      this.$refs.cancelOrder.close();
    },
  },
};
