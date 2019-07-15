import _ from 'lodash';
import uniqBy from 'lodash/uniqBy';
import globals from '../../common/globals';
import vxShippingAddress from '../vx-shipping-address/vx-shipping-address.vue';
import vxShippingMethod from '../vx-shipping-method/vx-shipping-method.vue';
// import checkoutData from './vx-checkout-mock';
import vxGiftOptions from '../vx-gift-options/vx-gift-options.vue';
import vxPaymentBilling from '../vx-payment-billing/vx-payment-billing.vue';
import vxOverview from '../vx-overview/vx-overview.vue';
import vxScheduleInstallation from '../vx-schedule-installation/vx-schedule-installation.vue';
import vxLeaseAgreement from '../vx-lease-agreement/vx-lease-agreement.vue';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import {
  checkoutEventBus,
  eventBus
} from '../../../modules/event-bus';
import {
  cartTypes,
  paymentStatus,
  cookies,
  flyoutStatus,
} from '../../common/mixins/vx-enums';
import vxOrderSummary from '../../manage-shopping-cart/vx-order-summary/vx-order-summary.vue';
import vxPlaceOrder from '../vx-place-order/vx-place-order.vue';
import flyoutBannerMixin from '../../common/vx-flyout-banner/vx-flyout-banner-mixin';
import CheckOutService from '../../common/services/checkout-service';
import cookiesMixin from '../../common/mixins/cookies-mixin';

export default {
  name: 'vx-checkout',
  mixins: [flyoutBannerMixin, cookiesMixin],
  components: {
    vxShippingAddress,
    vxShippingMethod,
    vxGiftOptions,
    vxPaymentBilling,
    vxOverview,
    vxScheduleInstallation,
    vxLeaseAgreement,
    vxSpinner,
    vxOrderSummary,
    vxPlaceOrder,
  },
  props: {
    i18n: Object,
    b2bUnitLevel: String,
    guestEmail: {
      type: String,
      default: '',
    },
    applePayMerchantName: String,
  },

  data() {
    return {
      globals,
      checkOutService: new CheckOutService(),
      visibleSections: [],
      cookies,
      sections: [{
          id: 'shippingAddress',
          heading: this.i18n.checkout.shippingAddressHeading,
          component: 'vx-shipping-address',
          visible: true,
          enabled: true,
        },
        {
          id: 'leaseAgreement',
          heading: this.i18n.checkout.leaseAgreementHeading,
          component: 'vx-lease-agreement',
          visible: false,
          enabled: false,
        },
        {
          id: 'scheduleinstallation',
          heading: this.i18n.checkout.scheduelInstalltionHeading,
          component: 'vx-schedule-installation',
          visible: false,
          enabled: false,
        },
        {
          id: 'shippingMethod',
          heading: this.i18n.checkout.shippingMethodHeading,
          component: 'vx-shipping-method',
          visible: true,
          enabled: false,
        },
        {
          id: 'giftOptions',
          heading: this.i18n.checkout.giftOptionsHeading,
          component: 'vx-gift-options',
          visible: false,
          enabled: false,
        },
        {
          id: 'paymentBilling',
          heading: this.i18n.checkout.PaymentHeading,
          component: 'vx-payment-billing',
          visible: true,
          enabled: false,
        },
        {
          id: 'overview',
          heading: this.i18n.checkout.overviewHeading,
          component: 'vx-overview',
          visible: true,
          enabled: true,
        },
      ],
      giftableProducts: [],
      leasableProducts: [],
      installableProducts: [],
      checkoutData: {},
      visibleCheckoutEntries: [],
      dataLoaded: false,
      isShippingMultiple: false,
      cartTypes,
      userLevel: false,
      promotionData: Object,
      isShippingAddressAdded: false,
      isShippingMethodAdded: false,
      isPaymentAdded: false,
      leaseCountry: [],
      isGiftable: false,
      isInstallable: false,
      splitApplicable: false,
      addressChanged: false,
      isPaymentButtonDisabled: false,
      shippingAddress: {},
    };
  },
  computed: {},
  mounted() {
    // check for subscription
    // first check from url
    if (this.globals.getIsSubscription()) {
      this.callSubscriptionCart();
    } else {
      this.callCart(cartTypes.FULL);
    }

    // full cart call to update the cart data if anything is added from above sections
    checkoutEventBus.$on('update-cart', (calculationType) => {
      if (this.globals.getIsSubscription()) {
        this.callSubscriptionCart(calculationType);
      } else {
        this.callCart(cartTypes.FULL);
      }
    });
    // handling next section when shipping method is saved
    checkoutEventBus.$on('section-complete', (sectioncompletedId) => {
      if (sectioncompletedId === 'shippingMethod' && !this.checkoutData.totalPrice.value) {
        let paymentBillingIndex = this.getSectionIndex('paymentBilling');
        this.setVisible(paymentBillingIndex, false);
      } else {
        this.enableNextVisibleSection(sectioncompletedId);
      }
    });
    // collapse all section when change button of one section is clicked
    checkoutEventBus.$on('section-edit', (sectioncompletedId) => {
      this.disableNextVisibleSections(sectioncompletedId);
    });
    checkoutEventBus.$on('shipping-multiple', () => {
      this.isShippingMultiple = true;
    });
    checkoutEventBus.$on('shipping-single', () => {
      this.isShippingMultiple = false;
    });
    checkoutEventBus.$on('enable-split', () => {
      this.splitApplicable = true;
    });
    checkoutEventBus.$on('disable-split', () => {
      this.splitApplicable = false;
    });
    if (this.globals.isB2B()) {
      this.checkB2bUserLevel();
    } else {
      this.userLevel = true;
    }

    checkoutEventBus.$on('shipping-address-edit', () => {
      this.isShippingAddressAdded = false;
      this.togglePlaceOrderButton();
    });
    checkoutEventBus.$on('shipping-address-added', () => {
      this.isShippingAddressAdded = true;
      this.togglePlaceOrderButton();
    });
    checkoutEventBus.$on('shipping-method-edit', () => {
      this.isShippingMethodAdded = false;
      this.togglePlaceOrderButton();
    });
    checkoutEventBus.$on('shipping-method-added', () => {
      this.isShippingMethodAdded = true;
      this.togglePlaceOrderButton();
      this.addressChanged = false;
    });
    checkoutEventBus.$on('payment-method-edit', () => {
      this.isPaymentAdded = false;
      this.togglePlaceOrderButton();
    });
    checkoutEventBus.$on('payment-method-added', () => {
      this.isPaymentAdded = true;
      this.togglePlaceOrderButton();
    });
    checkoutEventBus.$on('use-this-address-clicked', () => {
      this.addressChanged = true;
    });
    checkoutEventBus.$on('paypal-status', (data) => {
      if (data.status === paymentStatus.accept) {
        checkoutEventBus.$emit('place-order');
      } else {
        this.$refs.spinner.hideSpinner();
        /* Commented as this error message is not in scope for R2.
          this.showFlyout(
          paymentStatus.error,
          `${this.i18n.paypal.paymentError}`,
          false,
        ); */
      }
    });
    checkoutEventBus.$on('apple-pay-status', (data) => {
      if (data) {
        checkoutEventBus.$emit('place-order');
      } else {
        this.showFlyout(
          flyoutStatus.error,
          this.i18n.paymentBilling.applePayFailureError,
          true,
        );
      }
    });

    checkoutEventBus.$on('google-pay-status', (data) => {
      if (data) {
        checkoutEventBus.$emit('place-order');
      } else {
        this.showFlyout(flyoutStatus.error, this.i18n.paymentBilling.googlePayFailureError, false);
        checkoutEventBus.$emit('payment-method-default');
      }
    });

    checkoutEventBus.$on('google-pay-payment-sheet', (data) => {
      if (!this.checkoutData.isSubscription) {
        this.showFlyout(flyoutStatus.error, this.i18n.paymentBilling.googlePaymentSheetFailureError, false);
      }
    });

    // storing apple pay merchant name.
    if (this.applePayMerchantName) {
      this.globals.setStorage('applePayMerchantName', this.applePayMerchantName);
    }

    this.checkShippingRestriction();
  },
  methods: {
    setVisible(index, value) {
      this.sections[index].visible = value;
    },
    disableNextVisibleSections(currentSectionId) {
      const index = this.getVisibleSectionIndex(currentSectionId);
      for (let i = index + 1; i < this.visibleSections.length - 1; i++) {
        this.visibleSections[i].enabled = false;
      }
    },
    enableNextVisibleSection(sectioncompletedId) {
      const completedSectionIndex = this.getVisibleSectionIndex(
        sectioncompletedId,
      );
      this.visibleSections[completedSectionIndex + 1].enabled = true;
    },
    // Check if the product has gift/lease/install
    checkAdditionalAttributes(array) {
      this.visibleCheckoutEntries = array.filter(item => item.visible);
      if (this.visibleCheckoutEntries.length > 0) {
        if (this.globals.isB2B()) {
          // check if the product is leased
          this.checkLease(this.visibleCheckoutEntries);
        }
        const additionalAttributesArray = this.visibleCheckoutEntries.filter(
          item => item.additionalAttributes,
        );
        if (additionalAttributesArray.length > 0) {
          if (this.globals.isB2B()) {
            // check if the product is already opted for installation
            this.checkInstallation(additionalAttributesArray);
          }
          // check if the product is already opted for gifting
          if (
            this.globals.isB2C() &&
            this.globals.siteConfig.isGiftingEnabled
          ) {
            this.checkGiftable(additionalAttributesArray);
          }
        }
      }
    },
    // check if we need to show gifting Option
    checkGiftable(array) {
      this.giftableProducts = array.filter(item =>
        _.find(item.additionalAttributes.entry, {
          key: 'giftOpted',
          value: 'true',
        }),
      );
      if (this.giftableProducts.length > 0) {
        const giftableIndex = this.getSectionIndex('giftOptions');
        this.setVisible(giftableIndex, true);
        this.isGiftable = true;
      }
    },
    // check if we need to show Intsallation Option
    checkInstallation(array) {
      this.installableProduct = array.filter(item =>
        _.find(item.additionalAttributes.entry, {
          key: 'installed',
          value: 'true',
        }),
      );
      if (this.installableProduct.length > 0) {
        const installableIndex = this.getSectionIndex('scheduleinstallation');
        this.setVisible(installableIndex, true);
        this.isInstallable = true;
      }
    },
    // check if we need to show Lease Option
    checkLease(array) {
      this.leasableProducts = [];
      this.leasableProducts = array.filter(item => item.leasable);
      if (
        this.checkoutData.deliveryOrderGroups &&
        this.checkoutData.deliveryOrderGroups[0] &&
        this.checkoutData.deliveryOrderGroups[0].entries &&
        this.checkoutData.deliveryOrderGroups[0].entries[0] &&
        this.checkoutData.deliveryOrderGroups[0].entries[0].splitEntries &&
        this.checkoutData.deliveryOrderGroups[0].entries[0].splitEntries[0]
      ) {
        this.leaseCountry = [];
        const leaseCountries = this.checkoutData.deliveryOrderGroups[0].entries;
        this.leasableQtyItems = [];
        this.leaseCountry = leaseCountries.map((item) => {
          if (item.splitEntries.length > 0) {
            return item.splitEntries.map((entry) => {
              const leasableQtyItem = {};
              if (item.leasable) {
                leasableQtyItem[
                  entry.deliveryAddress.country.isocode
                ] = parseInt(entry.qty);
                this.leasableQtyItems.push(leasableQtyItem);
              }
              return entry.deliveryAddress.country;
            });
          }
        });
        const leaseFlatenCountry = _.flattenDeep(this.leaseCountry);
        this.leaseCountry = _.uniqBy(leaseFlatenCountry, 'isocode');
      }
      if (this.leasableProducts.length > 0) {
        const leaseableIndex = this.getSectionIndex('leaseAgreement');
        this.setVisible(leaseableIndex, true);
      }
    },

    // subscription cart call
    callSubscriptionCart(calculationType) {
      const subscriptionCartId = this.globals.getCookie(cookies.subscrCartId);
      this.checkOutService.getSubscriptionCart(
        {},
        this.handleCartResponse,
        this.handleCartError,
        subscriptionCartId,
        calculationType,
      );
      this.$refs.spinner.showSpinner();
    },

    // cart call and response
    callCart() {
      this.checkOutService.fullCart(
        {},
        this.handleCartResponse,
        this.handleCartError,
      );
      this.$refs.spinner.showSpinner();
    },
    handleCartResponse(response) {
      this.$refs.spinner.hideSpinner();
      if (response && response.data) {
        if (response.data.entries && this.checkoutData.entries && this.checkoutData.entries.length !== response.data.entries.length) {
          checkoutEventBus.$emit('cart-entries-count-updated');
        }
        this.checkoutData = response.data;
        if (this.checkoutData.entries[0].splitEntries[0] && this.checkoutData.entries[0].splitEntries[0].deliveryAddress) {
          this.shippingAddress = this.checkoutData.entries[0].splitEntries[0].deliveryAddress;
        }
        const paymentBillingIndex = this.getSectionIndex('paymentBilling');
        if (this.isPaymentAdded && !this.checkoutData.totalPrice.value) {
          this.setVisible(paymentBillingIndex, true);
          this.isPaymentButtonDisabled = true;
        } else
          if (!this.checkoutData.totalPrice.value) {
            this.setVisible(paymentBillingIndex, false);
            this.togglePlaceOrderButton();
          } else {
            this.setVisible(paymentBillingIndex, true);
            this.isPaymentButtonDisabled = false;
            if (this.isShippingMethodAdded) {
              this.visibleSections.push(this.sections[paymentBillingIndex]);
              const paymentVisibleIndex = _.findIndex(
                this.visibleSections,
                section => section.id == 'paymentBilling',
              );
              this.visibleSections[paymentVisibleIndex].enabled = true;
            }
            this.togglePlaceOrderButton();
          }
        this.checkAdditionalAttributes(this.checkoutData.entries);
        this.visibleSections = this.getVisibleSections(this.sections);
        this.promotionData = this.getProductPromotions();
        this.dataLoaded = true;
        if (this.isShippingAddressAdded) {
          checkoutEventBus.$emit('update-lease-products', this.checkoutData);
        }
      }
      const queryParam = window.location.search.substr(1).split('=');
      if (queryParam[0] === paymentStatus.paymentDecision) {
        if (queryParam[1] === paymentStatus.accept) {
          checkoutEventBus.$emit('place-order');
        } else {
          this.$refs.spinner.hideSpinner();
        }
      }
    },
    handleCartError() {
      this.$refs.spinner.hideSpinner();
    },
    getVisibleSections(array) {
      const visibleArray = array.filter(item => item.visible);
      return visibleArray;
    },
    getSectionIndex(sectionId) {
      return _.findIndex(this.sections, section => section.id == sectionId);
    },
    getVisibleSectionIndex(sectionId) {
      return _.findIndex(
        this.visibleSections,
        section => section.id == sectionId,
      );
    },
    // Check the user level in B2B
    checkB2bUserLevel() {
      if (this.b2bUnitLevel === 'L1' || this.globals.uid === 'anonymous') {
        this.userLevel = true;
      }
    },
    getProductPromotions() {
      const promotionProductData = {};
      for (
        let i = 0;
        i < this.checkoutData.appliedProductPromotions.length;
        i += 1
      ) {
        for (
          let j = 0;
          j <
          this.checkoutData.appliedProductPromotions[i].consumedEntries.length;
          j += 1
        ) {
          const orderEntryNumber = this.checkoutData.appliedProductPromotions[i]
            .consumedEntries[j].orderEntryNumber;
          try {
            const description = `${
              this.checkoutData.appliedProductPromotions[i].description
              }-${
              this.checkoutData.deliveryOrderGroups.entries[orderEntryNumber]
                .promotionsRevoked
              }`;
            if (
              !this.checkoutData.deliveryOrderGroups.entries[orderEntryNumber]
                .promotionsRevoked
            ) {
              promotionProductData[orderEntryNumber] = description;
            }
          } catch (error) {
            const description = this.checkoutData.appliedProductPromotions[i]
              .description;
            promotionProductData[orderEntryNumber] = description;
          }
        }
      }
      return promotionProductData;
    },
    togglePlaceOrderButton() {
      if (
        this.isShippingAddressAdded &&
        this.isShippingMethodAdded &&
        this.isPaymentAdded
      ) {
        checkoutEventBus.$emit('enable-place-order');
      } else if (
        this.isShippingAddressAdded &&
        this.isShippingMethodAdded &&
        !this.checkoutData.totalPrice.value
      ) {
        checkoutEventBus.$emit('enable-place-order');
      } else {
        checkoutEventBus.$emit('disable-place-order');
      }
    },
    checkShippingRestriction() {
      if (
        this.checkoutData.shippingRestrictions &&
        this.checkoutData.shippingRestrictions.length !== 0
      ) {
        this.showFlyout(
          'error',
          `${this.i18n.shippingAddress.flyoutRestrictionErrorPart1} ${
          this.checkoutData.shippingRestrictions[0].country.isocode
          }${this.i18n.shippingAddress.flyoutRestrictionErrorPart2}`,
          false,
        );
        checkoutEventBus.$emit('disable-place-order');
      } else {
        this.dismissFlyout();
        checkoutEventBus.$emit('enable-place-order');
      }
    },
  },
};
