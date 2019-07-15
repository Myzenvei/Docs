import Paginate from 'vuejs-paginate';
// import vxCancelSubscription from '../vx-cancel-subscription/vx-cancel-subscription.vue';
import vxEditSubscription from '../vx-edit-subscription/vx-edit-subscription.vue';
import vxCancelSubscription from '../vx-cancel-subscription/vx-cancel-subscription.vue';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import globals from '../../common/globals';
import ManageSubscriptionService from '../../common/services/manage-subscription-service';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import vxProductTile from '../../common/vx-product-tile/vx-product-tile.vue';
import {
  ProductAvailability,
  addToCartStatus,
  flyoutStatus,
  subscriptionAvailability,
} from '../../common/mixins/vx-enums';

export default {
  name: 'vx-subscriptions-landing',
  components: {
    vxEditSubscription,
    vxCancelSubscription,
    vxSpinner,
    vxModal,
    vxProductTile,
    'vx-paginate': Paginate,
  },
  props: {
    i18n: {
      type: Object,
    },
  },
  data() {
    return {
      ProductAvailability,
      flyoutStatus,
      addToCartStatus,
      subscriptionAvailability,
      manageSubscriptionService: new ManageSubscriptionService(),
      globals,
      subscriptionData: {},
      displayedSubscriptions: [],
      subscriptioncode: '',
      subscriptionEntryTemp: {},
      pagination: {
        next: '&#62;',
        previous: '&#60;',
        totalPages: '',
        pageRange: 10,
        currentPage: 1,
      },
    };
  },
  computed: {},
  created() { },
  mounted() {
    this.getSubscriptionDetails();
    $('.vx-subscriptions-landing').popover({
      container: '.vx-subscriptions-landing',
      selector: '[data-toggle="popover"]',
      placement: 'bottom',
      trigger: 'click',
      html: true,
    });
  },
  methods: {
    handleCancelSubscription(event, subscriptionEntry) {
      this.subscriptioncode = subscriptionEntry;
      this.$refs.cancelSubscriptionModal.open(event);
    },
    handleCancelSubscriptionSuccess() {
      this.getSubscriptionDetails();
      this.$refs.cancelSubscriptionModal.close();
    },
    handleCancelSubscriptionError() {
      this.$refs.cancelSubscriptionModal.close();
    },
    handleKeepSubscription() {
      this.$refs.cancelSubscriptionModal.close();
    },
    handleEditSubscription(event, data) {
      this.$refs.editSubscriptionModal.open(event);
      this.subscriptionEntryTemp = data;
    },
    /**
     * This function handles the success emitted from the edit subscription modal component
     */
    editSubscriptionSuccess() {},
    /**
     * This function handles the error emitted from the edit subscription modal component
     */
    editSubscriptionError() {},
    getSubscriptionDetails() {
      const requestConfig = {};
      this.manageSubscriptionService.getSubscriptionDetails(
        requestConfig,
        this.handleGetSubscriptionDetailsResponse,
        this.handleGetSubscriptionDetailsError,
      );
    },
    handleGetSubscriptionDetailsResponse(response) {
      if (response.data.carts.length) {
        this.subscriptionData = response.data;
        this.pagination.totalPages = Math.ceil(
          response.data.carts.length / this.pagination.pageRange,
        )
        this.displayedSubscriptions = this.subscriptionData.carts.slice(0, this.pagination.pageRange);
      }
      else {
        this.subscriptionData = {};
      }
    },
    handleGetSubscriptionDetailsError(error) { },
    updatedSubscriptionSuccess() {
      this.$refs.editSubscriptionModal.close();
      this.getSubscriptionDetails();
    },
    formatDate(activationDate) {
      const splitDate = activationDate.split(' ')[0].split('-');
      // Format mm/dd/yy
      return `${splitDate[1]}/${splitDate[2]}/${splitDate[0]}`;
    },
    openSubscriptionInfoTooltip() {
      this.$refs.subscribeInfoPopover[0].children[0].setAttribute(
        'title',
        this.i18n.subscriptionsLanding.subscribeInfoTitle,
      );
    },
    openSubscriptionAddressTooltip() {
      this.$refs.subscribeAddressPopover[0].children[0].setAttribute(
        'title',
        this.i18n.subscriptionsLanding.subcriptionIconInfo,
      );
    },
    getPopoverInfoContent() {
      return `<p>${this.i18n.subscriptionsLanding.subscribePopoverText}</p><a href=${globals.getUrlWithContextPath(globals.navigations.learnMore)}>${
        this.i18n.subscriptionsLanding.subscribeLearn
        }</a>`;
    },
    getPopoverAddressContent(subscriptionEntry) {
      return `
      <p class="popover-name">${subscriptionEntry.entries[0].splitEntries[0].deliveryAddress.firstName} ${subscriptionEntry.entries[0].splitEntries[0].deliveryAddress.lastName}</p>
      <p class="popover-address">${subscriptionEntry.entries[0].splitEntries[0].deliveryAddress.line1} ${subscriptionEntry.entries[0].splitEntries[0].deliveryAddress.line2},
       ${subscriptionEntry.entries[0].splitEntries[0].deliveryAddress.region.name} ${subscriptionEntry.entries[0].splitEntries[0].deliveryAddress.region.countryIso}, ${subscriptionEntry.entries[0].splitEntries[0].deliveryAddress.postalCode} </p>
      `
    },
    paginationCallback(pageNumber) {
      if (pageNumber !== this.pagination.currentPage) {
        this.displayedSubscriptions = this.subscriptionData.carts.slice(this.pagination.pageRange * (pageNumber - 1), this.pagination.pageRange * pageNumber);
        this.pagination.currentPage = pageNumber;
      }
    },
    handleSubscriptionStatus(subscriptionStatus) {
      if (subscriptionStatus === this.subscriptionAvailability.OUT_OF_STOCK) {
        return this.i18n.subscriptionsLanding.outOfStock
      } else
        if (subscriptionStatus === this.subscriptionAvailability.ACTIVE) {
          return this.i18n.subscriptionsLanding.active
        } else if (subscriptionStatus === this.subscriptionAvailability.PAYMENT_FAILURE) {
          return this.i18n.subscriptionsLanding.paymentFailure
        }
    },
  },
};
