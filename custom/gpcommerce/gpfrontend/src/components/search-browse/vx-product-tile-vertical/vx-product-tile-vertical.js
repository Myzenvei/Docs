import globals from '../../common/globals';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import vxSaveCart from '../../manage-shopping-cart/vx-save-cart/vx-save-cart.vue';
import vxNotifyMe from '../../search-browse/vx-notify-me/vx-notify-me.vue';
import flyoutBannerMixin from '../../common/vx-flyout-banner/vx-flyout-banner-mixin';
import {
  ProductAvailability,
  favorites,
  MaterialStatus,
} from '../../common/mixins/vx-enums';
import mobileMixin from '../../common/mixins/mobile-mixin';
import {
  eventBus,
  cartEventBus,
  globalEventBus,
} from '../../../modules/event-bus';
import cookiesMixin from '../../common/mixins/cookies-mixin';
import AnalyticsService from '../../common/services/analytics-service';
import YMarketingService from '../../common/services/yMarketing-service';
import SearchBrowseService from '../../common/services/search-browse-service';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import { stat } from 'fs';
import vxStarRating from '../../common/vx-star-rating/vx-star-rating.vue';
import vxStepperControl from '../../common/vx-stepper-control/vx-stepper-control.vue';
import vxProductIcons from '../../common/vx-product-icons/vx-product-icons.vue';
/**
 * Product tile component
 */

export default {
  data() {
    return {
      ProductAvailability,
      favorites,
      yMarketing: new YMarketingService(),
      disableCheckbox: false,
      removeCompare: '',
      isHidden: true,
      pdpContextPath: '',
      globals,
      isHalfFilled: false,
      ratingCal: '',
      isActive: '',
      isCheckboxDisabled: false,
      analyticsService: new AnalyticsService(),
      maxCompareCount: '',
      maxCompare: {
        mobile: 2,
        desktop: 4,
      },
      searchBrowseService: new SearchBrowseService(),
      characterLimit: 150,
      isTruncatedName: false,
      showRating: true,
      showReviews: true,
      guestListName: '',
      MaterialStatus,
      refillQuantity: 0,
      isProductSelected: this.bundleProductStatus,
      soldIndividually: true,
    };
  },
  props: [
    'product',
    'uncheckProduct',
    'showRemoveOption',
    'showCompareOption',
    'showSelectOption',
    'isFavoriteVisible',
    'showProductIndicators',
    'isItemIdHide',
    'isRefill',
    'isBazaarVoice',
    'isBulkEnabled',
    'i18n',
    'bundleProductStatus',
    'isBundlesCheckboxDisabled',
    'hideButtonBlock',
    'isBundlesTile',
  ],
  mixins: [cookiesMixin, flyoutBannerMixin, mobileMixin],
  name: 'vx-product-tile-vertical',
  components: {
    vxModal,
    vxSaveCart,
    vxNotifyMe,
    vxSpinner,
    vxStarRating,
    vxStepperControl,
    vxProductIcons,
  },
  watch: {
    refillQuantity() {
      this.$set(this.product, 'quantity', this.refillQuantity);
    },
  },
  created() {
    this.soldIndividually = this.product.soldIndividually;
  },
  methods: {
    handleFavorites() {
      if (this.globals.loggedIn) {
        this.$refs.spinner.showSpinner();
        this.searchBrowseService.getShoppingLists(
          {},
          this.handleGetWishlistResponse,
          this.handleGetWishlistError,
        );
      } else {
        this.navigateToLogin();
      }
    },
    /**
     * This function is to remove the item from Favorites List
     */
    deleteCartItem(wishlistUid) {
      const requestBody = {
        wishlistUid,
        product: {
          code: this.checkVariant(this.product),
        },
      };
      const requestConfig = {};
      requestConfig.data = requestBody;
      this.searchBrowseService.deleteCartItem(
        requestConfig,
        this.handleDeleteCartItemResponse,
        this.handleDeleteCartItemError,
      );
    },
    /**
     * This function handles the response of delete cart item service
     */
    handleDeleteCartItemResponse() {
      this.$refs.spinner.hideSpinner();
    },
    /**
     * This function handles the error of delete cart item service
     */
    handleDeleteCartItemError() {},
    // To get all saved wish lists of cart
    handleGetWishlistResponse(response) {
      if (response) {
        const item = response.data.wishlists.filter(
          list => list.name === this.favorites.favorites,
        );
        if (this.isActive) {
          const requestdata = {
            code: this.checkVariant(this.product),
            entryType: 'PRODUCT',
            wishlistUid: item[0].wishlistUid,
            quantity: '1',
          };
          const requestConfig = {};
          requestConfig.data = requestdata;
          this.searchBrowseService.saveAList(
            requestConfig,
            this.handleSaveCartResponse,
            this.handleSaveCartError,
          );
        } else {
          this.deleteCartItem(item[0].wishlistUid);
        }
      }
    },
    // Handle the error of get all wishlists service
    handleGetWishlistError(error) {
      if (error) {
        this.$refs.spinner.hideSpinner();
        this.showFlyout('error', error.data.description, true);
      }
    },
    // Handle the response of save a list service
    handleSaveCartResponse(response) {
      if (response) {
        this.$refs.spinner.hideSpinner();
        this.showFlyout('success', response.data.description, true);
      }
    },
    // Handle the error of save a list service
    handleSaveCartError(error) {
      if (error) {
        this.showFlyout('error', error.data.description, true);
      }
    },
    toggleFav() {
      this.isActive = !this.isActive;
      this.handleFavorites();
      if (!this.isActive) {
        globalEventBus.$emit('announce', 'removed product from favourites');
      }
    },
    checkBoxClicked(event) {
      const self = this;
      if (event && !event.currentTarget.disabled) {
        this.product.checked = !this.product.checked;
        // this.$emit('checkedBox', this.product.checked);
        const comparedProduct = {
          code: self.product.code,
          assetCode: self.product.assetCode,
          images: self.product.images,
          name: encodeURI(self.product.name),
        };
        if (this.product.checked) {
          // create cookie append
          this.updateCookie('CompareCookie', comparedProduct, 'checked');
          this.$nextTick(() => {
            // this.$emit('incrementCounter', '1');
            this.$emit('checkedElement', this.product);
          });
        } else {
          // edit cookie pop
          this.updateCookie('CompareCookie', comparedProduct, 'unchecked');
          this.$nextTick(() => {
            // this.$emit('decrementCounter', '-1');
            this.$emit('unCheckedElement', this.product);
          });
        }
      }
    },
    updateCookie(cookieName, cookieVal, status) {
      // let arr = [];
      const self = this;
      if (this.readCookie(cookieName)) {
        const cookieData = JSON.parse(this.readCookie(cookieName));
        const cookieProductCodes = cookieData.map(item => item.code);
        if (status === 'checked' && cookieData.length < self.maxCompareCount) {
          if (cookieProductCodes.indexOf(cookieVal.code) === -1) {
            cookieData.push(cookieVal);
          }
        } else {
          cookieData.forEach((item, index) => {
            if (item.code === cookieVal.code) {
              cookieData.splice(index, 1);
            }
          });
        }
        this.createCookie(cookieName, JSON.stringify(cookieData));
      } else {
        const arr = [];
        arr.push(cookieVal);
        this.createCookie(cookieName, JSON.stringify(arr));
      }
    },
    selectCheckBoxClicked(event) {
      if (this.isRefill) {
            this.$set(this.product, 'quantity', this.refillQuantity);
       }
      if (event) {
        if (this.isBulkEnabled) {
          this.product.isBulk = !this.product.isBulk;
        } else {
          this.product.checked = !this.product.checked;
        }
        if (this.product.checked || this.product.isBulk) {
          eventBus.$emit('selectCheckedElement', this.product);
        } else {
          eventBus.$emit('selectUnCheckedElement', this.product);
        }
      }
    },
    getQuantity(evt) {
      this.refillQuantity = evt;
    },
    removeComparePageProduct(event) {
      this.updateCookie('CompareCookie', this.product, 'unchecked');
      eventBus.$emit('comparePageRemove', this.product.code);
      this.isHidden = false;
    },
    addToCart() {
      this.$refs.spinner.showSpinner();
      let productQuantity = 1;
      if (this.product.minOrderQuantity) {
        productQuantity = this.product.minOrderQuantity;
      }
      const requestObjParams = {
        quantity: productQuantity,
        product: {
          code: `${this.product.code}`,
        },
      };
      const requestConfig = {};
      requestConfig.data = requestObjParams;
      this.searchBrowseService.addToCart(
        requestConfig,
        this.handleAddProductResponse,
        this.handleAddProductErrorResponse,
      );
      if (typeof dataLayer !== 'undefined') {
        const analyticsObject = {
          code: this.product.code,
          name: this.product.name,
          quantity: '1', // the default quantity set to 1 as there is no stepper control to edit quantity
        };
        // sending the data to Google Analytics on Add to Cart button click
        this.analyticsService.trackAddToCart(analyticsObject);
      }
      // sending data to yMarketing on add to cart button click
      const cartData = {
        cartCode: this.globals.getCartGuid(),
        productCode: this.product.code,
        productName: this.product.name,
        productPrice: this.product.price.value,
      };
      this.yMarketing.trackAddToCart(this.product.code, '1', cartData);
    },
    handleAddProductResponse(response) {
      this.$refs.spinner.hideSpinner();
      if (response && response.data) {
        if (response.data.entry.product.assetCode === 'refill') {
          this.$refs.refillsModal.open();
        }
        cartEventBus.$emit('cart-update');
        cartEventBus.$emit('call-basic-cart');
      }
    },
    handleAddProductErrorResponse(error) {
      this.$refs.spinner.hideSpinner();
      if (
        error &&
        error.response &&
        error.response.data &&
        error.response.data.errors.length !== 0 &&
        error.response.data.errors[0] &&
        error.response.data.errors[0].code
      ) {
        if (
          error.response.data.errors[0].code ===
          this.i18n.maxPurchaseableQuantityErrorCode
        ) {
          this.showFlyout(
            'error',
            `${this.i18n.maxPurchaseableQuantityUpdateMessage1} ${
              this.product.maxOrderableQuantity
            }, ${this.i18n.maxPurchaseableQuantityUpdateMessage2}`,
            true,
          );
        } else if (
          error.response.data.errors[0].code === this.i18n.lowStockErrorCode
        ) {
          this.showFlyout('error', this.i18n.lowStockErrorMessage, true);
        }
      }
    },
    calculateRating(rating) {
      this.ratingCal = Math.floor(rating);
      if (rating - this.ratingCal > 0) {
        this.isHalfFilled = true;
      }
    },
    handleSelectList(event) {
      if (this.globals.loggedIn) {
        if (this.$parent.$el.classList.contains('swiper-slide')) {
          document.querySelector('.vx-slider').classList.add('modal-opened');
        }
        this.$refs.selectListModal.open(event);
        this.guestListName = '';
      } else if (this.globals.siteConfig.isGuestList) {
        this.guestListName = this.i18n.guestList;
      } else {
        this.navigateToLogin();
      }
    },
    navigateToLogin() {
      const url = `${this.globals.getNavBaseUrl()}/login?site=${
        this.globals.siteId
      }`;
      window.location = url;
    },
    navigateToProduct(variantURL) {
      const url = this.pdpContextPath + variantURL;
      window.location = url;
    },
    handleNotifyMe(event) {
      if (this.$parent.$el.classList.contains('swiper-slide')) {
        document.querySelector('.vx-slider').classList.add('modal-opened');
      }
      this.$refs.notifyMeModal.open(event);
    },
    onSelectListSuccess() {
      if (this.$refs.selectListModal) {
        this.$refs.selectListModal.close();
      }
      if (this.$parent.$el.classList.contains('swiper-slide')) {
        document.querySelector('.vx-slider').classList.remove('modal-opened');
      }
      this.showFlyout('success', this.i18n.selectListResponse, true);
    },
    onNotifyMeError(error) {
      this.$refs.notifyMeModal.close();
      if (this.$parent.$el.classList.contains('swiper-slide')) {
        document.querySelector('.vx-slider').classList.remove('modal-opened');
      }
      this.showFlyout('error', error, true);
    },
    onNotifyMeSuccess(success) {
      this.$refs.notifyMeModal.close();
      if (this.$parent.$el.classList.contains('swiper-slide')) {
        document.querySelector('.vx-slider').classList.remove('modal-opened');
      }
      this.showFlyout('success', success, true);
    },
    modalClose() {
      if (this.$parent.$el.classList.contains('swiper-slide')) {
        document.querySelector('.vx-slider').classList.remove('modal-opened');
      }
    },
    formatProductName(productName, characterLimit) {
      if (productName) {
        const formattedName = productName.replace(/<[^>]*>/g, '');
        if (formattedName.length > characterLimit) {
          this.isTruncatedName = true;
          return `${formattedName.substr(0, characterLimit)}...`;
        }
        return formattedName;
      }
    },
    getProductName(productName) {
      const fullName = productName.replace(/<[^>]*>/g, '');
      return fullName;
    },
    isProductComparable(data) {
      const self = this;
      if (data.length) {
        // This check is for the removal of last product from compare panel making the length as 0
        if (data.length === self.maxCompareCount) {
          // Disable checkboxes if maximum threshold value reached for comparison
          if (self.product.checked) {
            // Do not disable if products are checked
            self.isCheckboxDisabled = false;
          } else {
            self.isCheckboxDisabled = true;
          }
        } else if (
          // Disable checkboxes if products does not belong to same asset code
          data[0].assetCode &&
          self.product.assetCode &&
          data[0].assetCode === self.product.assetCode
        ) {
          self.isCheckboxDisabled = false;
        } else {
          // Disable checkbox by default if it does not matches above conditions
          self.isCheckboxDisabled = true;
        }
      } else if (self.product.assetCode) {
        // On removal of last product, enable checkboxes of products with assetcodes
        self.isCheckboxDisabled = false;
      }
    },
    findAStore() {
      const findStorePage =
        this.globals.getNavBaseUrl() + this.globals.serviceUrls.locateStore;
      window.location.href = findStorePage;
    },
    checkVariant(product) {
      if (product.hasVariant) {
        for (let i = 0; i < product.variantOptions.length; i++) {
          if (product.variantOptions[i].url === product.url) {
            return product.variantOptions[i].code;
          }
        }
      } else {
        return product.code;
      }
    },
    getVariantsPrice(product) {
      if (
        product.priceRange &&
        product.priceRange.minPrice &&
        product.priceRange.maxPrice
      ) {
        if (
          product.priceRange.minPrice.formattedValue ===
          product.priceRange.maxPrice.formattedValue
        ) {
          return product.priceRange.minPrice.formattedValue;
        }
        return `${product.priceRange.minPrice.formattedValue} - ${
          product.priceRange.maxPrice.formattedValue
        }`;
      }
      return product.price.formattedValue;
    },
    handleProductSelection() {
      this.$emit('productSelected', this.isProductSelected);
    },
  },
  mounted() {
    const self = this;
    this.calculateRating(this.product.bvAverageRating);
    this.isActive = this.product.isFavorite;
    self.pdpContextPath = window.ACC.config.encodedContextPath;
    if (this.product.minOrderQuantity && ((this.product.materialStatus === this.ProductAvailability.OBSOLETE && this.product.stock.stockLevelStatus === this.ProductAvailability.IN_STOCK) || (this.product.materialStatus === this.MaterialStatus.ACTIVE_PRODUCT && this.product.stock.stockLevelStatus === this.ProductAvailability.IN_STOCK))) {
        this.refillQuantity = this.product.minOrderQuantity;
      } else if (!this.product.minOrderQuantity && ((this.product.materialStatus === this.ProductAvailability.OBSOLETE && this.product.stock.stockLevelStatus === this.ProductAvailability.IN_STOCK) || (this.product.materialStatus === this.MaterialStatus.ACTIVE_PRODUCT && this.product.stock.stockLevelStatus === this.ProductAvailability.IN_STOCK))) {
        this.refillQuantity = 1;
      } else {
        this.refillQuantity = 0;
    }
    // Initial check to disable/enable compare checkboxes if cookie is present
    if (self.showCompareOption) {
      // set max compare count as per viewport
      if (self.isMobile()) {
        self.maxCompareCount = self.maxCompare.mobile;
      } else {
        self.maxCompareCount = self.maxCompare.desktop;
      }
      // Disable checkboxes by default if product does not have asset code
      if (typeof self.product.assetCode === 'undefined') {
        self.isCheckboxDisabled = true;
        self.$set(self.product, 'checked', false);
      }
      // read cookie and enable/disable checkboxes accordingly
      let cookieData = self.readCookie('CompareCookie');
      if (cookieData !== null && cookieData !== undefined) {
        cookieData = JSON.parse(cookieData);
        this.isProductComparable(cookieData);
      }
    }
    // Event for product removed from panel.
    eventBus.$on('removeFromPanel', (data) => {
      if (self.product.code === data) {
        self.$nextTick(() => {
          self.$set(self.product, 'checked', false);
        });
      }
    });
  },
  updated() {
    const self = this;
    if (this.uncheckProduct) {
      self.$set(self.product, 'checked', false);
    }
    // Event fired on addition or deletion of any product for comparison
    eventBus.$on('editedCompare', (data) => {
      this.isProductComparable(data);
    });
  },
};
