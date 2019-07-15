/**
 * Component to show the order-details
 * */
import _ from 'lodash';
import fecha from 'fecha';
// import orderDetails from './vx-order-details-mock';
import vxOrderSummary from '../../manage-shopping-cart/vx-order-summary/vx-order-summary.vue';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import vxProductTile from '../../common/vx-product-tile/vx-product-tile.vue';
import globals from '../../common/globals';
import vxCancelOrder from '../../manage-transaction-history/vx-cancel-order/vx-cancel-order.vue';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import flyoutBannerMixin from '../../common/vx-flyout-banner/vx-flyout-banner-mixin';
import priceFormatMixin from '../../common/mixins/price-format-mixin';
import ManageTransactionService from '../../common/services/manage-transaction-service';
import vxSaveCart from '../../manage-shopping-cart/vx-save-cart/vx-save-cart.vue';
import {
  ProductAvailability,
  countryList,
  order,
  paymentTypes,
} from '../../common/mixins/vx-enums';
import vxReviewLeaseAgreement from '../../checkout/vx-review-lease-agreement/vx-review-lease-agreement.vue';
import vxReorderProduct from '../../common/vx-reorder-product/vx-reorder-product.vue';

export default {
  name: 'vx-order-details',
  mixins: [flyoutBannerMixin, priceFormatMixin],
  components: {
    vxOrderSummary,
    vxProductTile,
    vxSpinner,
    vxCancelOrder,
    vxModal,
    vxSaveCart,
    vxReviewLeaseAgreement,
    vxReorderProduct,
  },
  props: {
    /**
     * Labels, button and caption texts
     */
    i18n: Object,
    contactNumber: String,
  },
  data() {
    return {
      // orderDetails,
      orderDetails: {},
      globals,
      formattedEntries: {},
      isMultiple: false,
      isGiftable: false,
      isInstallable: false,
      dataLoaded: false,
      istallableProducts: [],
      showCancelOrderButton: false,
      manageTransactionService: new ManageTransactionService(),
      orderCode: '',
      renderData: [],
      productList: [],
      isAdmin: false,
      installableProducts: [],
      leaseAgrementData: {},
      ProductAvailability,
      order,
      statusMapping: {},
      country: {},
      countryList: [],
      legalTermName: '',
      promotionData: {},
      giveAwayCouponCode: '',
      giveAwayCouponName: '',
      paymentTypes,
    };
  },
  computed: {},
  mounted() {
    this.getOrderCode();
    this.getAdminFlag();
    this.getOrderDetails();
    $('.vx-order-details').popover({
      trigger: 'click',
      content: `<p>${
        this.i18n.orderDetails.subscribePopoverText
      }</p><a href=${globals.getUrlWithContextPath(globals.navigations.learnMore)}>${
        this.i18n.orderDetails.subscribeLearn
      }</a>`,
      html: true,
      placement: 'bottom',
      container: '.vx-order-details',
      selector: '.popover-subscribe',
    });
  },
  methods: {
    openTooltip() {
      this.$refs.subscribeInfo[0].children[0].setAttribute(
        'title',
        this.i18n.orderDetails.subscribeIconTitle,
      );
    },
    /**
     * check if shipping is single or multiple
     * */
    getOrderDetails() {
      this.$refs.spinner.showSpinner();
      if (this.globals.isB2C()) {
        this.manageTransactionService.getOrderDetailsB2C({},
          this.handleOrderDetailsSuccess,
          this.handleOrderDetailsError,
          this.orderCode,
        );
      } else if (this.globals.isB2B() && !this.isAdmin) {
        this.manageTransactionService.getOrderDetailsB2BBuyer({},
          this.handleOrderDetailsSuccess,
          this.handleOrderDetailsError,
          this.orderCode,
        );
      } else if (this.globals.isB2B() && this.isAdmin) {
        this.manageTransactionService.getOrderDetailsB2BAdmin({},
          this.handleOrderDetailsSuccess,
          this.handleOrderDetailsError,
          this.orderCode,
        );
      }
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
    getOrderCode() {
      const urlData = window.location.pathname.split('/');
      this.orderCode = urlData[urlData.length - 1];
    },
    getAdminFlag() {
      if (window.location.search && window.location.search.substring(1)) {
        this.isAdmin = true;
      }
    },
    handleOrderDetailsSuccess(response) {
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
      this.formatDataForRender(this.orderDetails);
      this.getShippingType(this.orderDetails.consignments);
      this.showCancelOrderButton = this.orderDetails.cancellable;
      this.promotionData = this.getProductPromotions();
      this.formattedEntries = this.arrayToObject(
        this.orderDetails.entries,
        'entryNumber',
      );
      this.dataLoaded = true;
      this.$refs.spinner.hideSpinner();
    },
    handleOrderDetailsError() {
      this.$refs.spinner.hideSpinner();
    },
    formatDataForRender(data) {
      this.renderData = [];
      data.consignments.map((consignment, index) => {
        const finalObj = {};
        finalObj.shippingAddress = consignment.shippingAddress;
        this.getDeliveryMethod(finalObj);
        let productsTemp = [];
        const obj = {};
        finalObj.statusList = [];
        // const renderObj = {};
        consignment.entries.map((entry) => {
          const statObj = {
            consignmentEntryStatus: entry.consignmentEntryStatus,
            entries: [],
          };
          const statusIndex = _.findIndex(finalObj.statusList, {
            consignmentEntryStatus: entry.consignmentEntryStatus,
          });
          if (statusIndex === -1) {
            statObj.entries.push(entry);
            finalObj.statusList.push(statObj);
          } else {
            finalObj.statusList[statusIndex].entries.push(entry);
          }
        });
        // consignment.statusMapping = obj;
        finalObj.statusList.map((statusEntry) => {
          // renderObj.statusEntry = statusEntry;
          const renderObj = {};
          renderObj.unShippedProducts = [];
          renderObj.trackingList = [];
          statusEntry.entries.map((entry) => {
            if (!entry.trackingList) {
              renderObj.unShippedProducts.push(entry);
            } else {
              if (entry.unshippedQuantity > 0) {
                entry.orderEntry.quantity = entry.unshippedQuantity;
                renderObj.unShippedProducts.push(entry);
              }
              entry.trackingList.map((trackingInfo) => {
                const price =
                  trackingInfo.quantityShipped *
                  entry.orderEntry.basePrice.value;
                const tempObj = {
                  orderEntry: entry.orderEntry,
                  quantityShip: trackingInfo.quantityShipped,
                  price: this.getFormattedPrice(price),
                  consignmentEntryStatus: entry.consignmentEntryStatus,
                };
                productsTemp.push(tempObj);
                trackingInfo.products = productsTemp;
                productsTemp = [];
                const prodIndex = _.findIndex(renderObj.trackingList, {
                  trackingID: trackingInfo.trackingID,
                });
                if (prodIndex === -1) {
                  renderObj.trackingList.push(trackingInfo);
                } else {
                  renderObj.trackingList[prodIndex].products.push(tempObj);
                }
              });
            }
          });
          statusEntry.renderObj = renderObj;
        });
        // finalObj.prods = renderObj;
        this.renderData.push(finalObj);
      });
    },
    getShippingType(consignments) {
      if (consignments.length > 1) {
        this.isMultiple = true;
      } else {
        this.isMultiple = false;
      }
    },
    getDeliveryMethod(finalObj) {
      this.orderDetails.deliveryGroup.map((deliveryMode, index) => {
        if (deliveryMode.key === finalObj.shippingAddress.id) {
          finalObj.deliveryMode = deliveryMode;
        }
      });
      return finalObj;
    },
    /**
     * check if we need to show Lease Option
     * */
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
    showLeaseAgreement(event) {
      this.manageTransactionService.getLeaseAgreement({},
        this.handleLeaseAgreementResponse,
        this.handleLeaseAgreementError,
        this.orderCode,
      );
      this.$refs.spinner.showSpinner();
    },
    handleLeaseAgreementResponse(response) {
      if (response && response.data) {
        this.country.name = this.getCountryName(response.data.country);
        this.legalTermName = response.data.legalTermName;
        this.country.isocode = response.data.country;
        this.leaseAgrementData = response.data;
        this.$refs.spinner.hideSpinner();
        this.$refs.viewTermsModal.open();
      }
    },
    handleLeaseAgreementError(error) {
      this.$refs.spinner.hideSpinner();
    },
    /**
     * check if the product is selected for gift
     * */
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
    /**
     * check if the product is selected for installationcheck if the product is selected for gift
     * */
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
    getInstallDate(date) {
      fecha.masks.myMask = 'MM/DD/YYYY';
      const dateObj = fecha.parse(date, 'dddd MMMM Do, YYYY');
      return fecha.format(dateObj, 'myMask');
    },
    /**
     * Check if single or multiple shipping is used
     * */
    getShippingMethod(data) {
      const method = data.split('-').join(' ');
      return method;
    },
    handleCancelOrder(event) {
      this.$refs.cancelOrder.open(event);
    },
    handleCancelOrderSuccess() {
      this.$refs.cancelOrder.close();
      this.showFlyout(
        'success',
        this.i18n.orderDetails.cancelOrderSuccessMsg,
        false,
      );
      this.showCancelOrderButton = false;
      this.getOrderDetails();
    },
    handleCancelOrderError() {
      this.$refs.cancelOrder.close();
      this.showFlyout(
        'error',
        this.i18n.orderDetails.cancelOrderErrorMsg,
        true,
      );
    },
    handleKeepOrder() {
      this.$refs.cancelOrder.close();
    },
    getProductList() {
      let visibleProductEntries = [];
      visibleProductEntries = this.orderDetails.entries.filter(
        item => item.visible,
      );
      visibleProductEntries.map((item) => {
        this.productList.push({
          code: item.product.code,
          quantity: item.quantity,
        });
      });
    },
    getProductPromotions() {
      const promotionData = {};
      // const orderLevelPromotionData = {};
      const productLevelPromotionData = {};
      //  For product level promotions
      if (this.orderDetails && this.orderDetails.appliedProductPromotions) {
        for (
          let i = 0; i < this.orderDetails.appliedProductPromotions.length; i += 1
        ) {
          for (
            let j = 0; j <
            this.orderDetails.appliedProductPromotions[i].consumedEntries
            .length; j += 1
          ) {
            const orderEntryNumber = this.orderDetails.appliedProductPromotions[
              i
            ].consumedEntries[j].orderEntryNumber;
            const description = this.orderDetails.appliedProductPromotions[i]
              .description;
            if (
              !this.orderDetails.entries[orderEntryNumber].promotionsRevoked
            ) {
              productLevelPromotionData[orderEntryNumber] = description;
            }
          }
        }
        promotionData.productLevelPromotionData = productLevelPromotionData;
      }
      /* //  Order level promotions for now commenting this code as need clarification on displaying cart level promotions
      // if (this.orderDetails && this.orderDetails.appliedOrderPromotions) {
      //   for (let i = 0; i < this.orderDetails.appliedOrderPromotions.length; i += 1) {
      //     for (let j = 0; j < this.orderDetails.appliedOrderPromotions[i].appliedCouponCodes.length; j += 1) {
      //       const description = this.orderDetails.appliedOrderPromotions[i]
      //         .description;
      //       orderLevelPromotionData[j] = description;
      //     }
      //   }
      //   promotionData.orderLevelPromotionData = orderLevelPromotionData;
      } */
      return promotionData;
    },
    handleSaveList() {
      this.getProductList();
      this.$refs.saveListModal.open();
    },
    handleListSaved() {
      this.$refs.saveListModal.close();
      this.showFlyout('success', this.i18n.saveList.saveListResponse, true);
    },
    arrayToObject(array, keyField) {
      const modifiedObj = array.reduce((obj, item) => {
        obj[item[keyField]] = item;
        return obj;
      }, {});
      return modifiedObj;
    },
    getCountryName(isocode) {
      this.countryList = countryList.options;
      let countryName = '';
      for (let i = 0; i < this.countryList.length; i += 1) {
        if (
          isocode &&
          this.countryList.length !== 0 &&
          this.countryList[i] &&
          this.countryList[i].value &&
          isocode.toUpperCase() === this.countryList[i].value.toUpperCase()
        ) {
          countryName = this.countryList[i].label;
          break;
        }
      }
      return countryName;
    },
    redirectContactUs() {
      const orderConfirmationNumber = this.orderDetails.code;
      this.globals.navigateToUrlWithParams(
        'contactUs',
        orderConfirmationNumber,
        'orderConfirmationNumber',
      );
    },
  },
};
