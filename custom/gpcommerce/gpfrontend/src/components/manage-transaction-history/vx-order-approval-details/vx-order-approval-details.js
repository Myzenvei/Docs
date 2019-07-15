/* Handles the order confirmation page */
import vxOrderSummary from '../../manage-shopping-cart/vx-order-summary/vx-order-summary.vue';
import vxProductTile from '../../common/vx-product-tile/vx-product-tile.vue';
import globals from '../../common/globals';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import fecha from 'fecha';
import ManageTransactionService from '../../common/services/manage-transaction-service';
import { discount } from '../../common/mixins/vx-enums';
import {
  checkoutEventBus
} from '../../../modules/event-bus';
import {
  approvalDefaultStatus,
  approvalDetailStatus,
  paymentTypes,
} from '../../common/mixins/vx-enums';

export default {
  name: 'vx-order-approval-details',
  components: {
    vxOrderSummary,
    vxProductTile,
    vxSpinner,
    vxModal,
  },
  props: {
    /**
     * Labels, button and caption texts
     */
    i18n: Object,
  },
  data() {
    return {
      country: 'United States',
      orderDetails: {},
      globals,
      orderCode: '',
      formattedEntries: {},
      isMultiple: false,
      isGiftable: false,
      isInstallable: false,
      dataLoaded: false,
      istallableProducts: [],
      promotionData: {},
      leaseAgrementData: {},
      manageTransactionService: new ManageTransactionService(),
      statusUpdated: true,
      approvalComments: '',
      approvalDefaultStatus,
      approvalDetailStatus,
      discount,
      paymentTypes,
    };
  },
  computed: {},
  mounted() {
    this.getOrderCode();
    this.getOrderDetails();
  },
  methods: {
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
      if (address && address.companyName) {
        companyName = address.companyName;
      }
      if (address) {
        userAddress =
          `${address.firstName} ` +
          `${address.lastName},` +
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
      this.manageTransactionService.getOrderDetailsB2BAdmin({},
        this.handleOrderDetailsSuccess,
        this.handleOrderDetailsError,
        this.orderCode,
      );
      this.$refs.spinner.showSpinner();
    },
    handleOrderDetailsSuccess(response) {
      if (response && response.data) {
        this.orderDetails = response.data;
        if (this.orderDetails.status === this.approvalDefaultStatus.pendingApproval) {
          this.statusUpdated = false;
        } else {
          this.statusUpdated = true;
        }
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
        }
      }
      this.isGiftable = isGift;
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
          this.istallableProducts[product.entryNumber] = product.product.code;
        }
      }
      this.isInstallable = installable;
      return installable;
    },
    // Check if single or multiple shipping is used
    getShippingMethod(data) {
      const method = data.split('-').join(' ');
      return method;
    },
    getDate(date) {
      const modidfiedDate = date.substring(0, 10);
      return fecha.format(new Date(modidfiedDate), 'mediumDate');
    },
    getProductPromotions() {
      const promotionProductData = {};
      for (
        let i = 0; i < this.orderDetails.appliedProductPromotions.length; i += 1
      ) {
        for (
          let j = 0; j <
          this.orderDetails.appliedProductPromotions[i].consumedEntries.length; j += 1
        ) {
          const orderEntryNumber = this.orderDetails.appliedProductPromotions[i]
            .consumedEntries[j].orderEntryNumber;
          const description = this.orderDetails.appliedProductPromotions[i]
            .description;
          promotionProductData[orderEntryNumber] = description;
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
      this.manageTransactionService.showLeaseAgreement({},
        this.handleLeaseAgreementResponse,
        this.handleLeaseAgreementError,
        this.orderDetails.code
      );
      this.$refs.spinner.showSpinner();
    },

    handleLeaseAgreementResponse(response) {
      if (response && response.data) {
        this.leaseAgrementData = response.data;
        this.$refs.spinner.hideSpinner();
        this.$refs.viewTermsModal.open(event);
      }
    },

    handleLeaseAgreementError(error) {
      this.$refs.spinner.hideSpinner();
    },
    onStatusUpdate(status) {
      this.manageTransactionService.orderStatusUpdated({},
        this.handleOrderStatusUpdateSuccess,
        this.handleOrderStatusUpdateError,
        this.orderCode,
        status,
        this.approvalComments,
      );
      this.$refs.spinner.showSpinner();
    },
    handleOrderStatusUpdateSuccess() {
      this.$refs.spinner.hideSpinner();
      this.getOrderDetails();
    },
    handleOrderStatusUpdateError() {
      this.$refs.spinner.hideSpinner();
    },
    // get install date
    getInstallDate(date) {
      fecha.masks.myMask = 'MM/DD/YYYY';
      const dateObj = fecha.parse(date, 'dddd MMMM Do, YYYY');
      return fecha.format(dateObj, 'myMask');
    },
  },
};
