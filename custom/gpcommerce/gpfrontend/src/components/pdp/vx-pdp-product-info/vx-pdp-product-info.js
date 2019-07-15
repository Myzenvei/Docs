import _ from 'lodash';
import globals from '../../common/globals';
import vxDropdownPrimary from '../../common/vx-dropdown-primary/vx-dropdown-primary.vue';
import vxStepperControl from '../../common/vx-stepper-control/vx-stepper-control.vue';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import vxShareItem from '../vx-share-item/vx-share-item.vue';
import saveCart from '../../manage-shopping-cart/vx-save-cart/vx-save-cart.vue';
import vxNotifyMe from '../../search-browse/vx-notify-me/vx-notify-me.vue';
import vxRefills from '../vx-refills/vx-refills.vue';
import flyoutBannerMixin from '../../common/vx-flyout-banner/vx-flyout-banner-mixin';
import ProductMixin from '../../common/mixins/product-mixin';
import mobileMixin from '../../common/mixins/mobile-mixin';
import {
  eventBus,
  cartEventBus,
  globalEventBus,
} from '../../../modules/event-bus';
import {
  ProductAvailability,
  favorites,
  flyoutStatus,
  pdpVariantTypes,
  cookies,
  referenceTypes,
} from '../../common/mixins/vx-enums';
import AnalyticsService from '../../common/services/analytics-service';
import YMarketingService from '../../common/services/yMarketing-service';
import PdpService from '../../common/services/pdp-service';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import vxStarRating from '../../common/vx-star-rating/vx-star-rating.vue';
import vxPdfGenerator from '../../common/vx-pdf-generator/vx-pdf-generator.vue';
import vxDownloadInfo from '../../common/vx-download-info/vx-download-info.vue';
import vxExtole from '../../../copper-crane/components/common/vx-extole/vx-extole.vue';
import cookiesMixin from '../../common/mixins/cookies-mixin';

export default {
  name: 'vx-pdp-product-info',
  mixins: [flyoutBannerMixin, ProductMixin, mobileMixin,cookiesMixin],
  components: {
    vxDropdownPrimary,
    vxStepperControl,
    vxModal,
    vxShareItem,
    vxNotifyMe,
    vxRefills,
    'vx-save-cart': saveCart,
    vxSpinner,
    vxStarRating,
    vxPdfGenerator,
    vxDownloadInfo,
    vxExtole,
  },
  props: {
    /**
     * Details of the product
     */
    pdpProductInfoData: {
      type: Object,
      default: {},
    },
    /**
     * Copy text coming from properties files
     */
    i18n: {
      type: Object,
    },
    /**
     * indicates whether the site is configured for favorites
     */
    isFavorites: {
      type: Boolean,
      default: false,
    },
    /**
     * Id of the product
     */
    productId: {
      type: String,
      default: '',
    },
    /**
     * indicates whether social share is enabled
     */
    isSocialShare: {
      type: Boolean,
      default: false,
    },
    /**
     * indicates whether sample cart is enabled
     */
    isSampleCart: {
      type: Boolean,
      default: false,
    },
    /**
     * indicates whether the site is configured for Bazaar Voice
     */
    isBazaarVoice: {
      type: String,
      default: '',
    },
    /**
     * indicates whether the site is configured for pdf download
     */
    showDownload: {
      type: String,
      default: '',
    },
    /**
     * colorcodes for pdf download
     */
    colorCodes: {
      type: Object,
      default: {},
    },
    /**
     * Copy text coming from properties files for search browse components
     */
    searchBrowseI18n: {
      type: Object,
    },
    /**
     * indicates whether to show custom tabs
     */
    showCustomTabs: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      ProductAvailability,
      favorites,
      flyoutStatus,
      pdpProductInfo: {
        name: '',
        stock: '',
        price: '',
        baseOptions: '',
        featureList: '',
      },
      quantityValue: 1,
      size: '',
      counts: '',
      variantColor: '#000000',
      globals,
      sizeList: [],
      selectedVariant: null,
      countsList: [],
      scentList: [],
      bvInlineRating: 'BVRRInlineRating-',
      sizeOptionsObj: {},
      styleOptionsObj: {},
      sizeOptionsArr: [],
      styleOptionsArr: [],
      selectedPrimaryVariant: null,
      selectedPrimaryVariantIndex: null,
      selectedSecondaryVariantIndex: null,
      sizeArr: [],
      countArr: [],
      scentArr: [],
      colorArr: [],
      prodUrl: '',
      prodId: '',
      prodName: '',
      prodPrice: 0,
      refillProductsData: Object,
      styleArr: [],
      isActive: '',
      isFavoriteFilled: false,
      analyticsService: new AnalyticsService(),
      yMarketing: new YMarketingService(),
      pdpService: new PdpService(),
      isSafari: /^((?!chrome|android).)*safari/i.test(navigator.userAgent),
      isDownload: false,
      isOpen: false,
      showRating: true,
      showReviews: true,
      imgBlob: '',
      guestListName: '',
      loadPdfGenerator: false,
      sizeEnabled: false,
      scentEnabled: false,
      countEnabled: false,
      addToCartDisabled: false,
      firstVariantEnabled: false,
      secondVariantEnabled: false,
      pdpVariantTypes,
      styleVariantName: '',
      subscriptionChecked: [],
      subscriptionCheckedItems: '',
      buttonText: this.i18n.addToCart,
      subscriptionFrequency: [],
      userChosenFrequency: {},
      referenceTypes,
      soldIndividually: true,
    };
  },
  computed: {},
  mounted() {
    this.onInit();
    this.getSubscriptionFrequency();
    if (this.globals.siteConfig.isReferAFriend) {
      this.$refs.vxExtoleRef.initiateExtoleOnPDP();
    }
  },
  updated() {
    document.addEventListener('click', this.onClick);
    if (
      document.body.scrollHeight > window.innerHeight &&
      document.getElementsByClassName('footer-section')[0] &&
      document.getElementsByClassName('cart-sticky-bottom')[0]
    ) {
      document.getElementsByClassName('footer-section')[0].style.paddingBottom =
        '70px';
    }
  },
  methods: {
    getSubscriptionFrequency() {
      this.$refs.spinner.showSpinner();
      const requestConfig = {};
      requestConfig.params = {
        productCode: this.productId,
      };
      this.pdpService.getSubscriptionFrequency(
        requestConfig,
        this.handleSubscriptionFrequencyResponse,
        this.handleSubscriptionFrequencyError,
      );
    },
    handleSubscriptionFrequencyResponse(response) {
      this.$refs.spinner.hideSpinner();
      const frequencyList = response.data.subscriptionFrequencyList;
      frequencyList.map((frequency) => {
        this.subscriptionFrequency.push({
          label: frequency.frequency.entry[0].key,
          value: frequency.frequency.entry[0].value,
        });
      });
      if(this.$refs.subscriptionFrequencyDropdown){
        this.handleSubscriptionFrequencyDefaultValue();
      }
    },
    handleSubscriptionFrequencyError(error) {
      this.$refs.spinner.hideSpinner();
    },

    radioButtonOptionFeature(item) {
      this.subscriptionCheckedItems = item.value;
      if (item.index === 2) {
        eventBus.$emit('subscriptionChecked', true);
        this.buttonText = this.i18n.subscribe;
      } else {
        this.buttonText = this.i18n.addToCart;
      }
    },
    toggleFav() {
      this.isActive = !this.isActive;
      this.handleFavorites();
      if (!this.isActive) {
        globalEventBus.$emit('announce', 'removed product from favourites');
      }
    },
    /**
     * This function is called on mounted which gets the product data
     */
    onInit() {
      const self = this;
      const requestConfig = {};
      requestConfig.data = {
        listName: this.listName,
      };
      requestConfig.data = {
        listName: this.listName,
      };
      if (this.globals.loggedIn) {
        const userId = this.globals.uid;
        self.pdpService.getProductData({},
          this.handleSuccessResponse,
          this.handleErrorResponse,
          this.productId,
          userId,
        );
      } else {
        self.pdpService.getProductData({},
          this.handleSuccessResponse,
          this.handleErrorResponse,
          this.productId,
        );
      }
    },
    /**
     * This function gives the quantity of a product
     * @param  {Number} quantity quantity value
     */
    getQuantity(quantity) {
      this.quantityValue = quantity;
    },
    /**
     * This function updates the color of the variant product
     * @param  {Number} color color value
     */
    updateColor(variant) {
      this.sizeOptionsArr.map((option) => {
        if (variant.code === option.code) {
          option.isSelected = true;
        }
      });
    },
    /**
     * This function updates the size of a product
     * @param  {Number} size size value
     */
    updateSize(size) {
      this.size = size;
      this.openSelectedVariant(size.url);
    },
    /**
     * This function updates the count of a product
     * @param  {Number} count count value
     */
    updateCounts(count) {
      this.counts = count;
      this.openSelectedVariant(count.url);
    },
    /**
     * This function updates the scent of a product
     * @param  {Number} scent scent value
     */
    updateScent(scent) {
      this.counts = scent;
      this.openSelectedVariant(scent.url);
    },
    /**
     * This function is called on click of Share Icon which opens the Share Item Modal for B2B and Socila Share for B2C
     */
    handleShareItem(event) {
      if (this.globals.isB2B()) {
        this.$refs.shareItemModal.open(event);
      } else {
        eventBus.$emit('show-social-share', true);
      }
    },
    /**
     * This function is called on click of Heart Icon
     */
    handleFavorites() {
      if (this.globals.loggedIn) {
        this.pdpService.getShoppingLists(
          {},
          this.handleGetWishlistResponse,
          this.handleGetWishlistError,
        );
      } else {
        this.navigateToLogin();
      }
    },
    /**
     * This function is to remove the item from Favorites List which adds the product to favorites list
     */
    deleteCartItem(wishlistUid) {
      const requestBody = {
        wishlistUid,
        product: {
          code: this.prodId,
        },
      };
      const requestConfig = {};
      requestConfig.data = requestBody;
      this.pdpService.deleteCartItem(
        requestConfig,
        this.handleDeleteCartItemResponse,
        this.handleDeleteCartItemError,
      );
    },
    /**
     * This function handles the response of delete cart item service
     */
    handleDeleteCartItemResponse() {},
    /**
     * This function handles the error of delete cart item service
     */
    handleDeleteCartItemError() {},
    /**
     * This function handles the response of get all wishlists service which is used to populate wishlist names on add to cart modal
     */
    handleGetWishlistResponse(response) {
      if (response) {
        const item = response.data.wishlists.filter(
          list => list.name === this.favorites.favorites,
        );
        if (this.isActive) {
          const requestdata = {
            code: this.prodId,
            entryType: 'PRODUCT',
            wishlistUid: item[0].wishlistUid,
            quantity: this.quantityValue,
          };
          const requestConfig = {};
          requestConfig.data = requestdata;
          this.pdpService.saveAList(
            requestConfig,
            this.handleSaveCartResponse,
            this.handleSaveCartError,
          );
        } else {
          this.deleteCartItem(item[0].wishlistUid);
        }
      }
    },
    /**
     * This function handles the error of get all wishlists service by showing the flyout banner with error message
     */
    handleGetWishlistError(error) {
      if (error) {
        this.showFlyout(this.flyoutStatus.error, error.data.description, true);
      }
    },
    /**
     * This function handles the response of save a list service by saving that product to list
     */
    handleSaveCartResponse(response) {
      if (response) {
        this.showFlyout(
          this.flyoutStatus.success,
          response.data.description,
          true,
        );
      }
    },
    /**
     * This function handles the error of save a list service by showing the flyout banner with error message
     */
    handleSaveCartError(error) {
      if (error) {
        this.showFlyout(this.flyoutStatus.error, error.data.description, true);
      }
    },
    /**
     * This function is called on click of Select a List Icon which opens the Select a List modal
     */
    handleSelectList(event) {
      if (this.globals.loggedIn) {
        this.$refs.selectListModal.open(event);
        this.guestListName = '';
      } else if (this.globals.siteConfig.isGuestList) {
        this.guestListName = this.i18n.guestList;
      } else {
        this.navigateToLogin();
        globalEventBus.$emit(
          'announce',
          'For adding to list, you need to login',
        );
        setTimeout(() => {
          this.navigateToLogin();
        }, 300);
      }
    },
    /**
     * This function navigates the user to the login page
     */
    navigateToLogin() {
      const url = `${this.globals.getNavBaseUrl()}/login?site=${
        this.globals.siteId
        }`;
      window.location = url;
    },
    /**
     * This function is called on click of Notify Me Button which opens the Notify Me modal
     */
    handleNotifyMe() {
      this.$refs.notifyMeModal.open();
    },
    handleButtonTextClick() {
      if (this.buttonText === this.i18n.addToCart) {
        this.addToCart();
      } else if (this.buttonText === this.i18n.subscribe) {
        this.subscribeToProduct();
      }
    },
    addToCart() {
      this.$refs.spinner.showSpinner();
      const requestObjParams = {
        quantity: this.quantityValue,
        product: {
          code: `${this.productId}`,
        },
      };
      const requestConfig = {};
      requestConfig.data = requestObjParams;
      this.pdpService.addProductToCart(
        requestConfig,
        this.handleAddProductResponse,
        this.handleAddProductError,
      );
      // sending the data to Google Analytics on Add to Cart Button click
      if (typeof dataLayer !== 'undefined') {
        const analyticsObject = {
          code: this.productId,
          name: this.prodName,
          quantity: this.quantityValue,
        };
        this.analyticsService.trackAddToCart(analyticsObject);
      }
      // sending data to yMarketing on add to cart button click
      const cartData = {
        cartCode: this.globals.getCartGuid(),
        productCode: this.productId,
        productName: this.prodName,
        productPrice: this.prodPrice,
      };
      this.yMarketing.trackAddToCart(
        this.productId,
        this.quantityValue,
        cartData,
      );
    },
    subscribeToProduct() {
      this.$refs.spinner.showSpinner();
      const requestObj = {
        quantity: this.quantityValue,
        product: {
          code: `${this.productId}`,
        },
        subscriptionFrequency: {
          entry: [{
            key: this.userChosenFrequency.label,
            value: this.userChosenFrequency.value,
          }]
        },
      };
      const requestConfig = {};
      requestConfig.data = requestObj;
      this.pdpService.subscribeProductToCart(
        requestConfig,
        this.handleSubscribeProductResponse,
        this.handleSubscribeProductError,
      );
    },
    handleSubscribeProductResponse(response) {
      const subscriptionCartId = this.globals.getCookie(cookies.subscrCartId);
      if (response && response.data) {
        this.pdpService.getSubscriptionCart(
          {},
          this.handleCartResponse,
          this.handleCartError, subscriptionCartId,
        );
        this.$refs.spinner.hideSpinner();
        if (
          response.data.statusCode ===
          this.i18n.maxPurchaseableQuantityErrorCode
        ) {
          if (
            this.pdpProductInfo &&
            this.pdpProductInfo.maxOrderQuantity &&
            response.data.quantityAdded === this.pdpProductInfo.maxOrderQuantity
          ) {
            this.showFlyout(
              this.flyoutStatus.error,
              this.i18n.maxPurchaseableQuantityErrorMessage,
              true,
            );
          } else if (
            this.pdpProductInfo &&
            this.pdpProductInfo.maxOrderQuantity &&
            response.data.quantityAdded < this.pdpProductInfo.maxOrderQuantity
          ) {
            if (!this.isSampleCart) {
              this.showFlyout(
                this.flyoutStatus.error,
                `${this.i18n.maxPurchaseableQuantityUpdateMessage1} ${
                this.pdpProductInfo.maxOrderQuantity
                }, ${this.i18n.maxPurchaseableQuantityUpdateMessage2}`,
                true,
              );
            } else if (this.isSampleCart) {
              this.showFlyout(
                this.flyoutStatus.error,
                this.i18n.maxPurchaseableQuantityErrorMessage,
                true,
              );
            }
          }
        } else if (response.data.statusCode === this.i18n.lowStockErrorCode) {
          this.showFlyout(
            this.flyoutStatus.error,
            this.i18n.lowStockErrorMessage,
            true,
          );
        } else if (this.pdpProductInfo.minOrderQuantity) {
          if (this.pdpProductInfo.stock.stockLevel < this.pdpProductInfo.minOrderQuantity) {
            this.addToCartDisabled = true;
          }
          this.quantityValue = this.pdpProductInfo.minOrderQuantity;
        } else {
          this.quantityValue = 1;
        }
        if (this.globals.getIsLoggedIn()) {
          this.globals.navigateToUrlWithParams('checkout');
        } else {
          if(this.globals.getCookie('flow') === 'checkout') {
            this.eraseCookie('flow');
          }
          this.globals.navigateToUrlWithParams('login');
        }
      }
    },
    handleSubscribeProductError(error) {
      if (error) {
        this.$refs.spinner.hideSpinner();
      };
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
          if (this.isSampleCart) {
            this.showFlyout(
              this.flyoutStatus.error,
              this.i18n.maxPurchaseableQuantityErrorMessage,
              true,
            );
          } else if (!this.isSampleCart) {
            this.showFlyout(
              this.flyoutStatus.error,
              `${this.i18n.maxPurchaseableQuantityUpdateMessage1} ${
              this.pdpProductInfo.maxOrderQuantity
              }, ${this.i18n.maxPurchaseableQuantityUpdateMessage2}`,
              true,
            );
          }
        } else if (
          error.response.data.errors[0].code === this.i18n.lowStockErrorCode
        ) {
          this.showFlyout(
            this.flyoutStatus.error,
            this.i18n.lowStockErrorMessage,
            true,
          );
        }
      }
    },
    handleCartResponse(response) {
    },
    handleCartError() {
    },
    findAStore() {
      const findStorePage =
        this.globals.getNavBaseUrl() + this.globals.serviceUrls.locateStore;
      window.location.href = findStorePage;
    },
    /**
     * This function handles the response of get PDP data service which populates the PDP with the response
     */
    handleSuccessResponse(response) {
      // sending the data to Google Analytics on PDP page load
      if (typeof dataLayer !== 'undefined') {
        this.analyticsService.trackImpressions(response.data);
        this.analyticsService.trackDetailImpressions(response.data);
      }
      const statusObj = _.pickBy(response.data, _.isBoolean);
      const imagesObj = _.pick(response.data, ['images', 'name']);
      const productIconsData = _.pick(response.data, ['productIcons']);
      let promoObj = null;
      let materialStatusObj = null;
      let thumbnailObj = {
        ...statusObj,
        ...imagesObj,
        ...productIconsData,
      };
      if (response.data.hasOwnProperty('promoText')) {
        promoObj = _.pick(response.data, 'promoText');
        thumbnailObj = {
          ...thumbnailObj,
          ...promoObj,
        };
      }
      if (response.data.hasOwnProperty('materialStatus')) {
        materialStatusObj = _.pick(response.data, 'materialStatus');
        thumbnailObj = {
          ...thumbnailObj,
          ...materialStatusObj,
        };
      }
      this.pdpProductInfo = response.data;
      this.soldIndividually = this.pdpProductInfo.soldIndividually;
      if (
        this.pdpProductInfo.isSubscribable &&
        this.pdpProductInfo.subscriptionPrice &&
        this.pdpProductInfo.subsPercentageDiscount &&
        !this.pdpProductInfo.stock.hasOwnProperty('nextAvailableDate') &&
        this.pdpProductInfo.materialStatus !== this.ProductAvailability.OBSOLETE &&
        this.pdpProductInfo.stock.stockLevelStatus !== this.ProductAvailability.OUT_OF_STOCK
      ) {
        this.buttonText = this.i18n.subscribe;
        this.subscriptionChecked = [
          {
            label: `${this.i18n.oneTimeDelivery}${
              this.pdpProductInfo.price.formattedValue
              }`,
            value: `${this.i18n.oneTimeDelivery}${
              this.pdpProductInfo.price.formattedValue
              }`,
            index: 1,
          },
          {
            label: `${this.i18n.save} ${
              this.pdpProductInfo.subsPercentageDiscount
              }${this.i18n.whenYouSubscribe}${
              this.pdpProductInfo.subscriptionPrice.formattedValue
              }`,
            value: `${this.i18n.save} ${
              this.pdpProductInfo.subsPercentageDiscount
              }${this.i18n.whenYouSubscribe}${
              this.pdpProductInfo.subscriptionPrice.formattedValue
              }`,
            index: 2,
          },
        ];
        this.subscriptionCheckedItems = `${this.i18n.save} ${
          this.pdpProductInfo.subsPercentageDiscount
          }${this.i18n.whenYouSubscribe}${
          this.pdpProductInfo.subscriptionPrice.formattedValue
          }`;
      }
      const certificationsList = [];
      if (this.pdpProductInfo.gpCertifications) {
        this.pdpProductInfo.gpCertifications.map((certification) => {
          certificationsList.push(certification.id);
        });
      }
      this.certificationsName = certificationsList.join();
      this.loadPdfGenerator = true;
      this.bvInlineRating += response.data.code;

      // set quantity to minimum order quantity
      if (this.pdpProductInfo.minOrderQuantity) {
        if (
          this.pdpProductInfo.stock.stockLevel <
          this.pdpProductInfo.minOrderQuantity
        ) {
          this.addToCartDisabled = true;
        }
        this.quantityValue = this.pdpProductInfo.minOrderQuantity;
      }

      // set the props for Share Item Modal
      this.prodUrl = response.data.url;
      this.prodId = response.data.code;
      this.prodName = response.data.name;
      this.isFavoriteFilled = response.data.isFavorite;

      if (
        !this.globals.isGpXpress() &&
        response.data.price &&
        response.data.price.value
      ) {
        this.prodPrice = response.data.price.value;
      }
      // send response to Refills Modal
      this.refillProductsData = response.data;
      response.data.showCustomTabs = this.showCustomTabs;
      // Send data to Tab Component
      eventBus.$emit('pdpProductsData', response.data);
      // Send data to Thumbnail Viewer
      eventBus.$emit('totalCarouselData', thumbnailObj);
      // Update Size Dropdown
      if (response.data.baseOptions.length) {
        this.setFacetData(response.data.baseOptions);
        if (this.sizeOptionsArr.length) {
          if (this.sizeArr.length) {
            this.formatDropdownData('sizeList', this.sizeArr, 'value', 'value');
          }
          if (this.scentArr.length && !this.sizeArr.length) {
            this.formatDropdownData(
              'scentList',
              this.scentArr,
              'value',
              'value',
            );
          }
          if (
            this.countArr.length &&
            !this.scentArr.length &&
            !this.sizeArr.length
          ) {
            this.formatDropdownData(
              'countsList',
              this.countArr,
              'value',
              'value',
            );
          }
        }
      }
      this.isActive = this.pdpProductInfo.isFavorite;
      if (response.data.disableSubscribeButton) {
    	  this.addToCartDisabled = true;
      }
    },
    /**
     * This function opens the selected variant of a product
     * @param  {String} url url of the variant of the product
     */
    openSelectedVariant(variantUrl) {
      if (variantUrl) {
        const colorUrl = variantUrl.split('/p/');
        const url = `${this.globals.getNavigationUrl('empty') +
          colorUrl[0]}/p/${colorUrl[1]}`;
        window.location = url;
      }
    },
    /**
     * This function handles the error of get PDP data service
     */
    handleErrorResponse() {},
    /**
     * This function handles the response of Add To Cart service which adds that product to cart
     */
    handleAddProductResponse(response) {
      if (response && response.data) {
        this.$refs.spinner.hideSpinner();
        cartEventBus.$emit('call-basic-cart');
        if (
          response.data.statusCode ===
          this.i18n.maxPurchaseableQuantityErrorCode
        ) {
          if (
            this.pdpProductInfo &&
            this.pdpProductInfo.maxOrderQuantity &&
            response.data.quantityAdded === this.pdpProductInfo.maxOrderQuantity
          ) {
            this.showFlyout(
              this.flyoutStatus.error,
              this.i18n.maxPurchaseableQuantityErrorMessage,
              true,
            );
          } else if (
            this.pdpProductInfo &&
            this.pdpProductInfo.maxOrderQuantity &&
            response.data.quantityAdded < this.pdpProductInfo.maxOrderQuantity
          ) {
            if (!this.isSampleCart) {
              this.showFlyout(
                this.flyoutStatus.error,
                `${this.i18n.maxPurchaseableQuantityUpdateMessage1} ${
                  this.pdpProductInfo.maxOrderQuantity
                }, ${this.i18n.maxPurchaseableQuantityUpdateMessage2}`,
                true,
              );
            } else if (this.isSampleCart) {
              this.showFlyout(
                this.flyoutStatus.error,
                this.i18n.maxPurchaseableQuantityErrorMessage,
                true,
              );
            }
          }
        } else if (response.data.statusCode === this.i18n.lowStockErrorCode) {
          this.showFlyout(
            this.flyoutStatus.error,
            this.i18n.lowStockErrorMessage,
            true,
          );
        } else if (this.pdpProductInfo.minOrderQuantity) {
          if (
            this.pdpProductInfo.stock.stockLevel <
            this.pdpProductInfo.minOrderQuantity
          ) {
            this.addToCartDisabled = true;
          }
          this.quantityValue = this.pdpProductInfo.minOrderQuantity;
        } else {
          this.quantityValue = 1;
        }
        if (this.pdpProductInfo.productReferences) {
          let refillProductData = [];
          refillProductData = this.pdpProductInfo.productReferences.filter(
            item => item.referenceType === this.referenceTypes.refills,
          );
          if (refillProductData.length) {
            this.$refs.refillsModal.open();
          }
        }
      }
    },
    /**
     * This function handles the error of Add To Cart service
     */
    handleAddProductError(error) {
      if (
        error &&
        error.response &&
        error.response.data &&
        error.response.data.errors.length !== 0 &&
        error.response.data.errors[0] &&
        error.response.data.errors[0].code
      ) {
        this.$refs.spinner.hideSpinner();
        if (
          error.response.data.errors[0].code ===
          this.i18n.maxPurchaseableQuantityErrorCode
        ) {
          if (this.isSampleCart) {
            this.showFlyout(
              this.flyoutStatus.error,
              this.i18n.maxPurchaseableQuantityErrorMessage,
              true,
            );
          } else if (!this.isSampleCart) {
            this.showFlyout(
              this.flyoutStatus.error,
              `${this.i18n.maxPurchaseableQuantityUpdateMessage1} ${
                this.pdpProductInfo.maxOrderQuantity
              }, ${this.i18n.maxPurchaseableQuantityUpdateMessage2}`,
              true,
            );
          }
        } else if (
          error.response.data.errors[0].code === this.i18n.lowStockErrorCode
        ) {
          this.showFlyout(
            this.flyoutStatus.error,
            this.i18n.lowStockErrorMessage,
            true,
          );
        }
      }
    },
    /**
     * This function sets the selected facet of a product
     * @param  {Array} arr data of the facet of a product
     */
    setFacetData(arr) {
      const self = this;
      self.sizeOptionsArr = arr[0].options;
      const variantArr = _.map(arr[0].options, 'variantOptionQualifiers');
      const flatArr = _.flatten(variantArr);
      self.sizeArr = _.filter(flatArr, {
        name: self.pdpVariantTypes.size,
      });
      self.scentArr = _.filter(flatArr, {
        name: self.pdpVariantTypes.scent,
      });
      self.countArr = _.filter(flatArr, {
        name: self.pdpVariantTypes.count,
      });
      self.colorArr = _.filter(flatArr, {
        name: self.pdpVariantTypes.color,
      });
      if (self.colorArr.length) {
        const colorArr = [];
        arr[0].options.map((item) => {
          if (item.variantOptionQualifiers) {
            item.variantOptionQualifiers.map((option) => {
              if (
                option.name === self.pdpVariantTypes.color &&
                colorArr.indexOf(option.hexCode) < 0
              ) {
                colorArr.push(option.hexCode);
                return self.styleOptionsArr.push(item);
              }
            });
          }
        });
      }
      if (self.sizeArr.length) {
        self.sizeEnabled = true;
        const primaryVariant = self.firstVariant(
          self.pdpVariantTypes.size,
          arr,
        );
        if (self.scentArr.length) {
          self.scentEnabled = true;
          self.secondVariantEnabled = true;
          self.secondVariant(self.pdpVariantTypes.scent, primaryVariant);
        } else if (self.countArr.length && !self.scentArr.length) {
          self.countEnabled = true;
          self.secondVariantEnabled = true;
          self.secondVariant(self.pdpVariantTypes.count, primaryVariant);
        } else {
          self.firstVariantEnabled = true;
        }
      } else if (self.scentArr.length && !self.sizeArr.length) {
        const primaryVariant = self.firstVariant(
          self.pdpVariantTypes.scent,
          arr,
        );
        self.scentEnabled = true;
        if (self.countArr.length) {
          self.countEnabled = true;
          self.secondVariantEnabled = true;
          self.secondVariant(self.pdpVariantTypes.count, primaryVariant);
        } else {
          self.firstVariantEnabled = true;
        }
      } else if (
        self.countArr.length &&
        !self.scentArr.length &&
        !self.sizeArr.length
      ) {
        self.countEnabled = true;
        self.firstVariantEnabled = true;
        self.firstVariant(self.pdpVariantTypes.count, arr);
      } else {
        self.firstVariant(self.pdpVariantTypes.color, arr);
      }
    },
    /**
     * This function sets the first variant of a product
     * @param  {String} variant type of the first variant of a product
     * @param  {Array} arr data of the first variant of a product
     */
    firstVariant(variant, arr) {
      const self = this;
      const codes = [];
      let sizeArr = [];
      let countArr = [];
      let scentArr = [];
      if (arr.length) {
        const code = arr[0].selected.code;
        arr[0].options.map((item) => {
          codes.push(item.code);
          if (code === item.code) {
            self.selectedPrimaryVariant = item;
            sizeArr = _.filter(item.variantOptionQualifiers, {
              name: self.pdpVariantTypes.size,
            });
            countArr = _.filter(item.variantOptionQualifiers, {
              name: self.pdpVariantTypes.count,
            });
            scentArr = _.filter(item.variantOptionQualifiers, {
              name: self.pdpVariantTypes.scent,
            });
          }
        });
      }
      this.$nextTick(() => {
        if (self.pdpProductInfo && self.$refs.sizeDropdown) {
          self.$refs.sizeDropdown.setDropdownLabel(sizeArr[0].value);
        }
      });
      this.$nextTick(() => {
        if (self.pdpProductInfo && self.$refs.countsDropdown) {
          self.$refs.countsDropdown.setDropdownLabel(countArr[0].value);
        }
      });
      this.$nextTick(() => {
        if (self.pdpProductInfo && self.$refs.scentDropdown) {
          self.$refs.scentDropdown.setDropdownLabel(scentArr[0].value);
        }
      });
      this.updateColor(this.selectedPrimaryVariant);
      return this.selectedPrimaryVariant;
    },
    /**
     * This function sets the second variant of a product
     * @param  {String} variant type of the second variant of a product
     * @param  {Array} arr data of the second variant of a product
     */
    secondVariant(variant, data) {
      const self = this;
      if (variant && data) {
        const secondVariant = _.filter(data.variantOptionQualifiers, {
          name: variant,
        });
        if (variant === self.pdpVariantTypes.count) {
          this.formatDropdownData(
            'countsList',
            secondVariant,
            'value',
            'value',
          );
          this.$nextTick(() => {
            if (self.pdpProductInfo && self.$refs.countsDropdown) {
              self.$refs.countsDropdown.setDropdownLabel(
                this.countsList[0].value,
              );
            }
          });
        }
        if (variant === self.pdpVariantTypes.scent) {
          this.formatDropdownData('scentList', secondVariant, 'value', 'value');
          if (self.pdpProductInfo && self.$refs.scentDropdown) {
            self.$refs.scentDropdown.setDropdownLabel(this.scentList[0].value);
          }
        }
      }
    },
    /**
     * This function sets the dropdown on PDP
     * @param  {String} dropdown variants of the product
     * @param  {Function} data data of the product
     * @param  {String} label label value
     * @param  {String} value price value
     */
    formatDropdownData(dropdown, data, label, value) {
      if (data.length) {
        let sequence = [];
        data.map((item) => {
          sequence.push(parseInt(item.sequence));
        });
        sequence = sequence.sort((a, b) => a - b);
        const sortedData = [];
        sequence.map((seq) => {
          data.map((item) => {
            if (seq == item.sequence) {
              sortedData.push(item);
            }
          });
        });
        sortedData.map((item) => {
          this.sizeOptionsArr.map((option) => {
            option.variantOptionQualifiers.map((qualifier) => {
              if (item.value === qualifier.value) {
                this[dropdown].push({
                  label: item[label],
                  value: item[value],
                  url: option.url,
                  stock: option.stock.stockLevelStatus === this.ProductAvailability.OUT_OF_STOCK && !this.globals.isVanityfair() ?
                    this.i18n.outOfStock : '',
                });
              }
            });
          });
        });
        this[dropdown] = _.uniqBy(this[dropdown], 'value');
      }
    },
    /**
     * This function is called on error emit of Refills component which closes the Refills Modal and shows a flyout banner with success message
     */
    onRefillsModalError(error) {
      this.$refs.refillsModal.close();
      this.showFlyout(this.flyoutStatus.error, error, true);
    },
    /**
     * This function is called on success emit of Refills component which closes the Refills Modal and shows a flyout banner with success message
     */
    onRefillsModalSuccess(success) {
      this.$refs.refillsModal.close();
      this.showFlyout(this.flyoutStatus.success, success, true);
    },
    /**
     * This function is called on click of No Thanks Button which closes the Refills Modal
     */
    onRefillsNoThanks() {
      this.$refs.refillsModal.close();
    },
    /**
     * This function is called on success emit of Select a List component which closes the Select a List Modal and shows a flyout banner with success message
     */
    onSelectListSuccess() {
      this.$refs.selectListModal.close();
      this.showFlyout(
        this.flyoutStatus.success,
        this.i18n.selectListResponse,
        true,
      );
    },
    /**
     * This function is called on error emit of Share List component which closes the Select a List Modal and shows a flyout banner with error message
     */
    onShareItemError(error) {
      this.$refs.shareItemModal.close();
      this.showFlyout(this.flyoutStatus.error, error, true);
    },
    /**
     * This function is called on success emit of Share List component which closes the Share List Modal and shows a flyout banner with success message
     */
    onShareItemSuccess(success) {
      this.$refs.shareItemModal.close();
      this.showFlyout(this.flyoutStatus.success, success, true);
    },
    /**
     * This function is called on error emit of Notify Me component which closes the Notify Me Modal and shows a flyout banner with error message
     */
    onNotifyMeError(error) {
      this.$refs.notifyMeModal.close();
      this.showFlyout(this.flyoutStatus.error, error, true);
    },
    /**
     * This function is called on success emit of Notify Me component which closes the Notify Me Modal and shows a flyout banner with success message
     */
    onNotifyMeSuccess(success) {
      this.$refs.notifyMeModal.close();
      this.showFlyout(this.flyoutStatus.success, success, true);
    },
    /**
     * This function is to hide social share icons
     */
    onClick(event) {
      if (event.target !== this.$refs.shareIcon) {
        eventBus.$emit('hide-social-share', true);
      }
    },
    /**
     * This function is called on success emit of updated max value
     */
    maxValueUpdated() {
      this.showFlyout(
        this.flyoutStatus.error,
        this.i18n.maxValueUpdatedStatus,
        true,
      );
    },
    /**
     * This function is called to set max value
     */
    maxOrderQuantity() {
      let maxValue = 0;
      if (this.pdpProductInfo && this.pdpProductInfo.maxOrderQuantity) {
        maxValue = this.pdpProductInfo.maxOrderQuantity;
      } else {
        maxValue = undefined;
      }
      return maxValue;
    },
    /**
     * This function is called on success emit of updated min value
     */
    minValueUpdated() {
      this.showFlyout(
        this.flyoutStatus.error,
        this.i18n.minValueUpdatedStatus,
        true,
      );
    },
    /**
     * This function is called to set min value
     */
    minOrderQuantity() {
      let minValue = 0;
      if (this.pdpProductInfo && this.pdpProductInfo.minOrderQuantity) {
        minValue = this.pdpProductInfo.minOrderQuantity;
      }
      return minValue;
    },
    /**
     * This function is called to set stock value
     */
    setStockLevel() {
      let stockLevel = 0;
      if (
        this.pdpProductInfo &&
        this.pdpProductInfo.stock &&
        this.pdpProductInfo.stock.stockLevel
      ) {
        stockLevel = this.pdpProductInfo.stock.stockLevel;
      }
      return stockLevel;
    },
    /**
     * This function is called on success emit of updated stock value
     */
    stockLevelUpdated() {
      this.showFlyout(
        this.flyoutStatus.error,
        this.i18n.stockLevelUpdatedStatus,
        true,
      );
    },
    /**
     * This function creates pdf
     */
    createPDF(pdfInfo) {
      this.$refs.pdfModule.createPdfPDPFormat(pdfInfo);
    },
    /**
     * This function limits the characters in the data
     */
    limitCharacters(data) {
      let newData = data.length > 500 ? `${data.substring(0, 500)}...` : data;
      newData = this.replaceText(newData);
      return newData;
    },
    /**
     * This function replaces text
     */
    replaceText(text) {
      const mapObj = {
        '™': '(TM)',
        '’': "'",
        '–': '-',
        '<br>': '',
        '”': '"',
      };
      const newText = text.replace(
        /™|’|–|<br>|”/gi,
        matched => mapObj[matched],
      );
      return newText;
    },
    /**
     * This function is called when the download icon is clicked
     */
    handleDownloadList(event) {
      this.$refs.pdfModule.renderImage({
        id: 'img#itf1',
      });
      this.$refs.downloadInfoModal.open(event);
    },
    /**
     * This function is called to get image data
     */
    onImageDownloadInit(imageInfo) {
      const requestConfig = {};
      requestConfig.params = {
        productCode: this.prodId,
        imageformat: imageInfo.format,
        resolution: imageInfo.size,
        allimages: imageInfo.allImages,
      };
      this.pdpService.getImagesInZipFormat(
        requestConfig,
        this.handleimageDownloadResponse,
        this.handleImageDownloadError,
        this.prodId,
      );
    },
    /**
     * This function handles the response of image download get call
     */
    handleimageDownloadResponse(response) {
      const blob = new Blob([response.data], {
        type: 'application/zip',
      });
      const link = document.createElement('a');
      link.href = window.URL.createObjectURL(blob);
      if (window.navigator && window.navigator.msSaveOrOpenBlob) {
        window.navigator.msSaveOrOpenBlob(
          blob,
          this.i18n.defaultDownloadedFilename,
        );
        this.$refs.downloadInfoModal.close();
      }
      link.download = this.i18n.defaultDownloadedFilename;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      this.$refs.downloadInfoModal.close();
    },
    /**
     * This function handles the error of image download get call
     */
    handleImageDownloadError(error) {
      if (error) {
        this.$refs.downloadInfoModal.close();
        this.showFlyout('error', this.i18n.imageDownloadError, true);
      }
    },
    /**
     * This function disables the add to cart button
     */
    disableAddToCart() {
      this.addToCartDisabled = true;
    },
    /**
     * This function gets the variant of a product
     * @param  {String} val value of the variant of a product
     * @param  {Boolean} isSelected value of the variant of a product
     */
    getVariantName(val, isSelected) {
      if (isSelected) {
        this.styleVariantName = val;
      }
      return val;
    },
    handleSubscriptionFrequencyDefaultValue() {
      if (this.subscriptionFrequency[0].value) {
        this.userChosenFrequency.value = this.subscriptionFrequency[0].value;
        this.userChosenFrequency.label = this.subscriptionFrequency[0].label;
        this.$refs.subscriptionFrequencyDropdown.setDropdownValue(this.subscriptionFrequency[0].value);
      }
    },
  },
};
