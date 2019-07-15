import debounce from 'lodash/debounce';
import vxShareCart from '../../../components/manage-shopping-cart/vx-share-cart/vx-share-cart.vue';
// import shoppingCartData from './vx-shopping-cart-mock-data';
import vxModal from '../../../components/common/vx-modal/vx-modal.vue';
import vxSaveCart from '../../../components/manage-shopping-cart/vx-save-cart/vx-save-cart.vue';
import globals from '../../../components/common/globals';
import vxSpinner from '../../../components/common/vx-spinner/vx-spinner.vue';
import flyoutBannerMixin from '../../../components/common/vx-flyout-banner/vx-flyout-banner-mixin';
import ProductMixin from '../../../components/common/mixins/product-mixin';
import mobileMixin from '../../../components/common/mixins/mobile-mixin';
import {
  cartEventBus,
  eventBus,
  globalEventBus,
} from '../../../modules/event-bus';
import ManageShoppingCartService from '../../../components/common/services/manage-shopping-cart-service';
import vxProductTile from '../../../components/common/vx-product-tile/vx-product-tile.vue';
import YMarketingService from '../../../components/common/services/yMarketing-service';
import AnalyticsService from '../../../components/common/services/analytics-service';
import vxStepperControl from '../../common/vx-stepper-control/vx-stepper-control.vue';
import {
  flyoutStatus,
} from '../../common/mixins/vx-enums';

export default {
  name: 'vx-sample-shopping-cart',
  mixins: [flyoutBannerMixin, ProductMixin, mobileMixin],
  components: {
    vxShareCart,
    vxModal,
    vxSaveCart,
    vxSpinner,
    vxProductTile,
    vxStepperControl,
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
      cartHasVisibleItems: true,
      manageShoppingCartService: new ManageShoppingCartService(),
      yMarketing: new YMarketingService(),
      analyticsService: new AnalyticsService(),
      flyoutStatus,
      giveAwayCouponDescription: '',
    };
  },
  computed: {},
  created() {
    const self = this;
    // listening to emits of delete and edit item from cart-product tile component
    cartEventBus.$on('cart-update', () => {
      self.callShoppingCart();
    });
    cartEventBus.$on('cart-update-error', () => {
      self.callShoppingCart('FULL');
    });
    self.handleQuantity = debounce((productData, itemNumber, itemQuantity) => {
      // sending the data to Google Analytics on quantity update
      if (typeof dataLayer !== 'undefined') {
        if (productData.quantity !== itemQuantity) {
          let analyticsObject = {
            code: productData.product.code,
            name: productData.product.name,
            quantity: itemQuantity,
          };
          this.analyticsService.trackRemoveFromCart(analyticsObject);
          analyticsObject = {
            code: productData.product.code,
            name: productData.product.name,
            quantity: itemQuantity,
          };
          this.analyticsService.trackAddToCart(analyticsObject);
        }
      }
      // sending data to yMarketing on quantity update
      const cartData = {
        cartCode: this.globals.getCartGuid(),
        productCode: productData.product.code,
        productName: productData.product.name,
        productPrice: productData.basePrice.value,
      };
      this.yMarketing.trackUpdateCart(
        productData.product.code,
        productData.quantity,
        itemQuantity,
        cartData,
      );
      if (itemQuantity === 0) {
        this.deleteCartItem(productData, itemNumber);
      } else {
        const requestConfig = {};
        requestConfig.data = {
          quantity: itemQuantity,
          installed: false,
        };
        this.manageShoppingCartService.editCartItem(
          requestConfig,
          this.handleEditCartResponse,
          this.handleEditCartError,
          itemNumber,
        );
        this.$refs.spinner.showSpinner();
      }
    }, self.delayInterval);
  },
  mounted() {
    this.callShoppingCart();
  },
  methods: {
    /**
     * This function deletes an item from cart
     */
    deleteCartItem(productData, itemNumber, event) {
      // sending the data to Google Analytics on delete icon click
      if (typeof dataLayer !== 'undefined') {
        const analyticsObject = {
          code: productData.product.code,
          name: productData.product.name,
          quantity: productData.quantity,
        };
        this.analyticsService.trackRemoveFromCart(analyticsObject);
      }
      this.manageShoppingCartService.deleteCartItem({},
        this.handleDeleteCartItemResponse,
        this.handleDeleteCartItemError,
        itemNumber,
      );
      this.$refs.spinner.showSpinner();
      // sending data to yMarketing on delete icon click
      const cartData = {
        cartCode: this.globals.getCartGuid(),
        productCode: productData.product.code,
        productName: productData.product.name,
        productPrice: productData.totalPrice.value,
      };
      this.yMarketing.trackRemoveFromCart(
        productData.product.code,
        productData.quantity,
        cartData,
      );
    },
    /**
     * This function handles the response of delete an item from cart
     */
    handleDeleteCartItemResponse(response) {
      cartEventBus.$emit('cart-update');
      globalEventBus.$emit('announce', 'product deleted');
      this.$refs.spinner.hideSpinner();
    },
    /**
     * This function handles the error of delete an item from cart
     */
    handleDeleteCartItemError(error) {
      // this.$emit('errorDelete'); can show flyout
      cartEventBus.$emit('cart-update-error');
      this.$refs.spinner.hideSpinner();
    },
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
      this.$refs.spinner.showSpinner();
    },
    /**
     * This function handles the response of cart details
     */
    handleShoppingCartResponse(response) {
      this.$refs.spinner.hideSpinner();
      if (response && response.data) {
        this.shoppingCartData = response.data;
        if (this.shoppingCartData.appliedOrderPromotions) {
          this.shoppingCartData.appliedOrderPromotions.map((promotion) => {
            if (promotion.giveAwayCouponCodes.length && promotion.description) {
              this.giveAwayCouponDescription = promotion.description;
            }
          });
        }
        eventBus.$emit('cart-details', this.shoppingCartData.entries);
        cartEventBus.$emit('total-items-updated', response.data.totalItems);
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
      this.$refs.spinner.hideSpinner();
      this.cartHasVisibleItems = false;
      this.$root.$emit('cartHasVisibleItems', this.cartHasVisibleItems);
    },
    /**
     * This function closes the save list modal
     */
    handleListSaved() {
      this.$refs.saveCartModal.close();
    },
    setColor(color) {
      return {
        'background-color': `${color}`,
      };
    },
    /**
     * This function edits an item from cart
     */
    handleQuantity() {
      // dummy function which will invoke inside created().
    },
    /**
     * This function shows error when user changes the quanitity of a product and it exceeds maxOrderQuantity
     */
    maxValueUpdated() {
      this.showFlyout(this.flyoutStatus.error, this.i18n.maxValueUpdatedStatus, true);
    },
    /**
     * This function returns the maxOrderQuantity for a product
     */
    maxOrderQuantity(maxValue) {
      return maxValue;
    },
    /**
     * This function shows error when user changes the quanitity of a product and it exceeds minOrderQuantity
     */
    minValueUpdated() {
      this.showFlyout(this.flyoutStatus.error, this.i18n.minValueUpdatedStatus, true);
    },
    /**
     * This function returns the minOrderQuantity for a product
     */
    minOrderQuantity(minValue) {
      return minValue;
    },
    /**
     * This function handles the error of editing an item from cart
     */
    handleEditCartError(error) {
      cartEventBus.$emit('cart-update-error');
      this.$refs.spinner.hideSpinner();
    },
    /**
     * This function handles the response of editing an item from cart
     */
    handleEditCartResponse(response) {
      cartEventBus.$emit('cart-update');
      this.$refs.spinner.hideSpinner();
    },
  },
};
