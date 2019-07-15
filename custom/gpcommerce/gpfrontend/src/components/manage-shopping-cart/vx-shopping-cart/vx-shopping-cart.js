/* eslint-disable no-prototype-builtins */
import vxShareCart from '../vx-share-cart/vx-share-cart.vue';
import vxCartProductTile from '../vx-cart-product-tile/vx-cart-product-tile.vue';
// import shoppingCartData from './vx-shopping-cart-mock-data';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import vxSaveCart from '../vx-save-cart/vx-save-cart.vue';
import globals from '../../common/globals';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import flyoutBannerMixin from '../../common/vx-flyout-banner/vx-flyout-banner-mixin';
import {
  cartEventBus,
  eventBus,
  globalEventBus,
} from '../../../modules/event-bus';
import ManageShoppingCartService from '../../common/services/manage-shopping-cart-service';
import vxBundleDetails from '../vx-bundle-details/vx-bundle-details.vue';


export default {
  name: 'vx-shopping-cart',
  mixins: [flyoutBannerMixin],
  components: {
    vxShareCart,
    vxCartProductTile,
    vxModal,
    vxSaveCart,
    vxSpinner,
    vxBundleDetails,
  },
  props: {
    /**
     * text coming from property file
     */
    i18n: Object,
    /**
     * indicator for lease error
     */
    customerLeaseable: Boolean,
  },
  data() {
    return {
      shoppingCartData: {
        entries: [],
      },
      globals,
      promotionData: Object,
      cartHasVisibleItems: true,
      installationPrice: Object,
      spinnerComponent: null,
      manageShoppingCartService: new ManageShoppingCartService(),
      guestListName: '',
      giveAwayCouponDescription: '',
      individualProducts: [],
      bundles: [],
    };
  },
  created() {
    // listening to emits of delete and edit item from cart-product tile component
    cartEventBus.$on('cart-update', () => {
      this.callShoppingCart();
    });
    cartEventBus.$on('cart-update-error', () => {
      // this.callShoppingCart('FULL');
    });
  },
  computed: {},
  mounted() {
    this.spinnerComponent = this.$refs.spinner;
    this.callShoppingCart();
  },
  methods: {
    /**
     * This function handles the opening of share cart modal
     */
    openShareCartModal(event) {
      this.$refs.shareCartModal.open(event);
    },
    /**
     * This function handles the success of the share cart modal
     */
    handleShareCartSuccess() {
      this.$refs.shareCartModal.close();
    },
    /**
     * This function handles the error of the share cart modal
     */
    handleShareCartError() {
      // this.$refs.shareCartModal.close();
    },
    /**
     * This function handles the save cart functionality
     */
    handleSaveACart(event) {
      if (this.globals.loggedIn) {
        this.$refs.saveCartModal.open(event);
      } else {
        this.globals.setCookie('flow', 'cart');
        if (this.globals.siteConfig.isGuestList) {
          this.guestListName = this.i18n.guestList;
        } else {
          this.globals.navigateToUrl('login');
          globalEventBus.$emit(
            'announce',
            'For adding to list, you need to login',
          );
          setTimeout(() => {
            this.globals.navigateToUrl('login');
          }, 300);
        }
      }
    },
    /**
     * This function gets cart details
     */
    callShoppingCart() {
      const requestConfig = {};
      this.manageShoppingCartService.getFullCartService(requestConfig, this.handleShoppingCartResponse, this.handleShoppingCartError);
      this.spinnerComponent.showSpinner();
    },
    /**
     * This function handles the response of cart details
     */
    handleShoppingCartResponse(response) {
      this.spinnerComponent.hideSpinner();
      if (response && response.data) {
        this.shoppingCartData = response.data;
        this.individualProducts = this.shoppingCartData.entries.filter(product => !product.hasOwnProperty('rootBundleTemplate'));
        this.generateBundleData();
        if (this.shoppingCartData.appliedOrderPromotions.length) {
          this.shoppingCartData.appliedOrderPromotions.map((promotion) => {
            if (promotion.giveAwayCouponCodes.length && promotion.description) {
              this.giveAwayCouponDescription = promotion.description;
            }
          });
        } else {
          this.giveAwayCouponDescription = '';
        }
        eventBus.$emit('cart-details', this.shoppingCartData.entries);
        cartEventBus.$emit('total-items-updated', response.data.totalItems);
        if (this.globals.isB2B()) {
          if (this.globals.getIsLoggedIn()) {
            this.showLeaseError();
          }
          this.installationPrice = this.getInstallationPrice();
        }
        this.promotionData = this.getProductPromotions();
        if (this.shoppingCartData.entries.length !== 0) {
          this.cartHasVisibleItems = true;
        } else {
          this.cartHasVisibleItems = false;
        }
        this.$root.$emit('cartHasVisibleItems', this.cartHasVisibleItems);
        this.$root.$emit('full-cart-called', this.shoppingCartData);
        this.$root.$emit('cart-reloaded', true);
      }
    },
    /**
     * This function handles the error of cart details
     */
    handleShoppingCartError() {
      this.spinnerComponent.hideSpinner();
      this.cartHasVisibleItems = false;
      this.$root.$emit('cartHasVisibleItems', this.cartHasVisibleItems);
    },
    /**
     * This function shows lease error
     */
    showLeaseError() {
      for (let i = 0; i < this.shoppingCartData.entries.length; i += 1) {
        if (this.shoppingCartData.entries[i].leasable && !this.customerLeaseable) {
          this.$root.$emit('showLeaseError', true);
          this.showFlyout('error', this.i18n.leaseCartError, false);
          return false;
        } else {
          this.$root.$emit('showLeaseError', false);
          this.dismissFlyout();
        }
      }
    },
    /**
     * This function closes the save list modal
     */
    handleListSaved() {
      this.$refs.saveCartModal.close();
    },
    /**
     * This function gets product promotions details
     */
    getProductPromotions() {
      const promotionProductData = {};
      for (let i = 0; i < this.shoppingCartData.appliedProductPromotions.length; i += 1) {
        for (let j = 0; j < this.shoppingCartData.appliedProductPromotions[i].consumedEntries.length; j += 1) {
          const orderEntryNumber = this.shoppingCartData
            .appliedProductPromotions[i].consumedEntries[j].orderEntryNumber;
          const description = this.shoppingCartData.appliedProductPromotions[i]
            .description;
          if (!this.shoppingCartData.entries[orderEntryNumber].promotionsRevoked) {
            promotionProductData[orderEntryNumber] = description;
          }
        }
      }
      return promotionProductData;
    },
    /**
     * This function gets price of installation products
     */
    getInstallationPrice() {
      let invisibleEntries = [];
      const installationPrice = {};
      invisibleEntries = this.shoppingCartData.entries.filter(item => !!item.visible === false);
      for (let i = 0; i < invisibleEntries.length; i += 1) {
        const installableProduct = _.find(invisibleEntries[i].additionalAttributes.entry, {
          key: 'installedEntry',
        });
        if (installableProduct) {
          installationPrice[installableProduct.value] = invisibleEntries[i].totalPrice.formattedValue;
        }
      }
      return installationPrice;
    },

    generateBundleData() {
      this.bundles = [];
      const bundleProducts = this.shoppingCartData.entries.filter(product => product.hasOwnProperty('rootBundleTemplate'));
      const bundlesInCart = [];
      this.shoppingCartData.entries.forEach((product) => {
        if (product.hasOwnProperty('rootBundleTemplate') && (bundlesInCart.every(bundle =>
          (bundle.id !== product.rootBundleTemplate.id || bundle.number !== product.bundleNo)))) {
          bundlesInCart.push({
            id: product.rootBundleTemplate.id,
            number: product.bundleNo,
            name: product.rootBundleTemplate.name,
          });
        }
      });
      bundlesInCart.forEach((bundle, index) => {
        this.bundles.push({
          id: bundle.id,
          name: bundle.name,
          number: bundle.number,
          bundleTemplateList: [],
        });
        bundleProducts.forEach((product) => {
          if (product.rootBundleTemplate.id === bundle.id && product.bundleNo === bundle.number && !this.bundles[index].bundleTemplateList.some(
            leafBundle => leafBundle.id === product.component.id,
          )) {
            this.bundles[index].bundleTemplateList.push({
              id: product.component.id,
              name: product.component.name,
              maxItemsAllowed: product.component.maxItemsAllowed,
              minItemsAllowed: product.component.minItemsAllowed,
              products: [],
            });
          }
        });
      });
      bundlesInCart.forEach((bundle, index) => {
        bundleProducts.forEach((product) => {
          let leafIndex;
          this.bundles[index].bundleTemplateList.forEach((leaf, leafNo) => {
            if (leaf.id === product.component.id) {
              leafIndex = leafNo;
            }
          });
          if (product.rootBundleTemplate.id === bundle.id && product.bundleNo === bundle.number && this.bundles[index].bundleTemplateList[leafIndex].products.every(
            item => item.product.code !== product.product.code,
          )) {
            this.bundles[index].bundleTemplateList[leafIndex].products.push(product);
          }
        });
      });
      this.$forceUpdate();
    },
  },
};
