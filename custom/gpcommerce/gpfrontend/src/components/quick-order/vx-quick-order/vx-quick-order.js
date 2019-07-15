/**
 * Handles quick order functionality
 */
import _ from 'lodash';
import vxShareCart from '../../manage-shopping-cart/vx-share-cart/vx-share-cart.vue';
import vxSaveCart from '../../manage-shopping-cart/vx-save-cart/vx-save-cart.vue';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import globals from '../../common/globals';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import vxCsvUpload from '../vx-csv-upload/vx-csv-upload.vue';
import QuickOrderService from '../../common/services/quick-order-service';
import vxStepperControl from '../../common/vx-stepper-control/vx-stepper-control.vue';
import vxNotifyMe from '../../search-browse/vx-notify-me/vx-notify-me.vue';
import vxAccordion from '../../common/vx-accordion/vx-accordion.vue';
import flyoutBannerMixin from '../../common/vx-flyout-banner/vx-flyout-banner-mixin';
import vxProductTile from '../../common/vx-product-tile/vx-product-tile.vue';
import {
  ProductAvailability, discount
} from '../../common/mixins/vx-enums';
import {
  cartEventBus
} from '../../../modules/event-bus';
import AnalyticsService from '../../common/services/analytics-service';
import YMarketingService from '../../common/services/yMarketing-service';
import priceFormatMixin from '../../common/mixins/price-format-mixin';
import {
  globalEventBus
} from '../../../modules/event-bus';


export default {
  name: 'vx-quick-order',
  components: {
    vxSpinner,
    vxShareCart,
    vxSaveCart,
    vxModal,
    vxCsvUpload,
    vxStepperControl,
    vxNotifyMe,
    vxAccordion,
    vxProductTile,
  },
  props: {
    /**
     * Labels, button and caption texts
     */
    i18n: Object,
  },
  mixins: [flyoutBannerMixin, priceFormatMixin],
  data() {
    return {
      stockStatusMessage: '',
      globals,
      productSkuMapping: [],
      searchResult: [],
      searchHistory: [],
      searchInput: '',
      invalidSku: [],
      validSku: '',
      isError: false,
      isDuplicateError: false,
      subTotal: 0,
      purchasable: [],
      nonPurchasable: [],
      quickOrderService: new QuickOrderService(),
      ProductAvailability,
      errorValues: [],
      notifyCode: '',
      fromquickOrder: true,
      analyticsService: new AnalyticsService(),
      yMarketing: new YMarketingService(),
      templateError: '',
      discount,
      currentDate: '',
    };
  },

  computed: {},
  mounted() {
    this.currentDate = this.getCurrentDate();
  },
  methods: {
    setQuantity(newQty) {
      this.product.quantity = newQty;
    },
    checkProductStatus(product) {
      let customisedStatus = '';
      const stock = product.stock;
      if (product.offlineDate < this.currentDate || product.materialStatus === this.ProductAvailability.OBSOLETE) {
        if (product.hasOwnProperty('replacementProductCode')) {
          customisedStatus =
            this.i18n.quickOrder.replacementProduct + product.replacementProductCode;
        } else {
          customisedStatus =
            this.i18n.quickOrder.discontinuedProduct;
        }
      } else if (product.materialStatus === this.ProductAvailability.COMING_SOON) {
        customisedStatus = this.i18n.quickOrder.comingSoonProduct;
      } else if (product.quantity > product.maxOrderQuantity) {
        customisedStatus = `${this.i18n.quickOrder.maxOrderQuantity + product.maxOrderQuantity}`;
      } else if (stock.stockLevelStatus === this.ProductAvailability.LOW_STOCK) {
        customisedStatus = `${this.i18n.quickOrder.onlyCopyText +
          stock.stockLevel +
          this.i18n.quickOrder.inStockText}`;
      } else if (
        stock.stockLevelStatus === this.ProductAvailability.OUT_OF_STOCK &&
        !stock.nextAvailableDate
      ) {
        customisedStatus = `${this.i18n.quickOrder.outOfStockMessage +
          this.i18n.quickOrder.nonBackOderable}`;
      } else if (
        stock.stockLevelStatus === this.ProductAvailability.OUT_OF_STOCK &&
        stock.nextAvailableDate
      ) {
        customisedStatus = `${this.i18n.quickOrder.outOfStockMessage}(${this.i18n.quickOrder
          .backOderabledate + stock.nextAvailableDate})`;
      }
      return customisedStatus;
    },
    findDuplicate(search) {
      const duplicateInput = [];
      let finalSearchInput = [];
      const filteredIds = {};
      search.map((input) => {
        this.searchHistory.filter((product) => {
          if (product.code === input || product.cmirCode === input) {
            duplicateInput.push(input);
          }
        });
      });
      if (!duplicateInput.length) {
        finalSearchInput = search;
      } else {
        duplicateInput.map((duplicateValue) => {
          search.splice(_.findIndex(search, input => input === duplicateValue), 1);
          finalSearchInput = search;
        });
      }
      filteredIds.duplicateInput = duplicateInput;
      filteredIds.finalSearchInput = finalSearchInput;
      return filteredIds;
    },
    resetError() {
      this.isError = false;
      this.isDuplicateError = false;
      this.errorValues = [];
    },
    searchProducts() {
      this.resetError();
      let finalSearchInput = [];
      let search = [];
      let filteredIds = {};
      this.searchInput = this.searchInput
        .split(',')
        .map(el => el.trim())
        .join(',');
      search = this.searchInput.split(',');
      if (this.searchHistory.length) {
        filteredIds = this.findDuplicate(search);
        finalSearchInput = filteredIds.finalSearchInput;
        finalSearchInput = finalSearchInput.join(',');
      } else {
        finalSearchInput = this.searchInput;
      }
      if (finalSearchInput.length) {
        this.generateProductMappingRequest(finalSearchInput);
        this.searchInput = '';
      }
      if (filteredIds.duplicateInput && filteredIds.duplicateInput.length) {
        this.isDuplicateError = true;
        this.generateError(filteredIds.duplicateInput);
      }
    },
    useReplacement(product) {
      this.searchInput = product.replacementProductCode;
      this.searchProducts();
    },
    getCurrentDate() {
      const currentDate = new Date();
      const formattedDate = `${currentDate.getDate()}/${currentDate.getMonth() +
        1}/${currentDate.getFullYear()}`;
      return formattedDate;
    },
    /**
     * function to filter products into purchasable and non-purchasable
     */
    filterProducts() {
      let purchasable = [];
      let nonPurchasable = [];
      let products = [];
      let suggestionInput = [];
      const queryInput = [];
      products = this.appendInfo(this.searchResult);
      nonPurchasable = products.filter((product) => {
        if ((product.offlineDate < this.currentDate || product.materialStatus === this.ProductAvailability.OBSOLETE) && product.stock.stockLevelStatus === this.ProductAvailability.OUT_OF_STOCK) {
              product.discontinued = true;
              product.stock.stockStatusMessage = this.checkProductStatus(product);
            return product;
        } else if (
          product.stock.stockLevelStatus === this.ProductAvailability.OUT_OF_STOCK
        ) {
          return product;
        } else if (product.materialStatus === this.ProductAvailability.COMING_SOON) {
          product.stock.stockStatusMessage = this.checkProductStatus(product);
          return product;
        } else if (product.quantity > product.maxOrderQuantity) {
          product.stock.stockStatusMessage = this.checkProductStatus(product);
          return product;
        }
      });
      purchasable = products.filter((product) => {
        if (
          (product.offlineDate < this.currentDate || product.materialStatus === this.ProductAvailability.OBSOLETE) &&
          product.stock.stockLevelStatus !== this.ProductAvailability.OUT_OF_STOCK
        ) {
          product.stock.stockStatusMessage = this.checkProductStatus(product);
          return product;
        } else if (
          product.stock.stockLevelStatus !== this.ProductAvailability.OUT_OF_STOCK &&
          !product.discontinued &&
          product.materialStatus !== this.ProductAvailability.COMING_SOON
        ) {
          return product;
        }
      });
      if (nonPurchasable.length) {
        this.nonPurchasable.unshift(...nonPurchasable);
      }
      if (purchasable.length) {
        this.purchasable.unshift(...purchasable);
        this.calculateTotal();
      }
      this.purchasable.map((product) => {
        queryInput.push(this.filterList(product, 'code'));
      });
      suggestionInput = {
        items: queryInput,
      };
      cartEventBus.$emit('quick-order-load', suggestionInput);
    },
    filterList(object, code) {
      const remapedObject = {
        code: object[code],
      };
      return remapedObject;
    },
    calculateTotal() {
      let total = 0;
      let quantity = 0;
      this.purchasable.map((product) => {
        total += product.price.finalPrice;
        quantity += product.quantity;
      });
      this.subTotal = total;
      this.totalQuantity = quantity;
    },
    // calculateDiscounts(product) {
    //   return (product.weblistPrice.value - product.price.value) * product.quantity;
    // },
    appendInfo(products) {
      const self = this;
      return products.map((product) => {
        product.stock.stockStatusMessage = this.checkProductStatus(product);
        product.cmirCode = self.productSkuMapping.items.filter(
          item => item.code === product.code,
        )[0].cmirCode;
        product.quantity = self.productSkuMapping.items.filter(
          item => item.code === product.code,
        )[0].count;
        product.price.finalPrice = product.quantity * product.price.value;
        // if (product.weblistPrice) {
        //   product.discount = this.calculateDiscounts(product);
        // }
        return product;
      });
    },
    quantityUpdated() {
      this.calculateTotal();
    },
    /**
     * function to handle csv file upload
     *
     * @param {Array} fileList List of files attached
     */
    fileUploaded(fileList) {
      const formData = new FormData();
      const self = this;
      this.resetError();
      this.$refs.spinner.showSpinner();
      formData.append('file', fileList[0]);
      const requestConfig = {};
      requestConfig.data = formData;
      self.quickOrderService.postProductMappings(
        requestConfig,
        this.handleProuctMappingResponse,
        this.handleProuctMappingError,
      );
    },
    fileAttached() {
      this.attached = true;
    },
    filterMapping() {
      let invalidValues = [];
      this.validSku = this.productSkuMapping.items.filter(prod => prod.isValidSKU === true);
      this.invalidSku = this.productSkuMapping.items.filter(prod => prod.isValidSKU === false);
      if (this.invalidSku.length) {
        invalidValues = this.invalidSku.map((item) => {
          if (!item.cmirCode) {
            return item.code;
          }
          return item.cmirCode;
        });
        this.generateError(invalidValues);
        this.isError = true;
      }
    },
    generateError(errorValues) {
      this.errorValues = [...this.errorValues, ...errorValues];
      if (this.errorValues) {
        this.searchInput = this.errorValues.join(',');
      }
    },
    removeDuplicatesFromCSV(data) {
      let filteredIds = {};
      const ids = data.map(item => item.code);
      filteredIds = this.findDuplicate(ids);
      if (filteredIds.duplicateInput && filteredIds.duplicateInput.length) {
        this.isDuplicateError = true;
        this.generateError(filteredIds.duplicateInput);
      }
      return filteredIds.finalSearchInput;
    },
    checkInputState() {
      this.isError = false;
      this.isDuplicateError = false;
    },
    openUploadFile() {
      this.$refs.uploadCsv.open();
    },
    openShareCartModal(event) {
      this.$refs.shareListModal.open(event);
    },
    handleShareCartSuccess() {
      this.$refs.shareListModal.close();
      this.showFlyout('success', this.i18n.shareList.shareListResponse, true);
    },
    handleShareCartError() {},
    handleSaveACart(event) {
      if (this.globals.loggedIn) {
        this.$refs.saveListModal.open(event);
      } else {
        this.globals.setCookie('flow', 'cart');
        this.globals.navigateToUrl('login');
      }
    },
    handleListSaved() {
      this.$refs.saveListModal.close();
      this.showFlyout('success', this.i18n.saveList.saveListResponse, true);
    },
    /**
     * function to generate query string for solar search request
     */
    generateSearchQuery(productSkus) {
      const inputArr = productSkus.map(el => el.trim());
      const initialText = this.i18n.quickOrder.solarParamPrefix;
      const productSkuList = initialText + inputArr.join(':code:');
      return productSkuList;
    },
    /**
     * function to generate request for solar search request
     */
    generateSearchUrlRequest(queryString) {
      this.quickOrderService.getProductInfoService(
        {},
        this.handleProuctInfoResponse,
        this.handleProuctInfoError,
        queryString,
      );
    },
    handleProuctInfoResponse(response) {
      const data = response.data;
      if (data && data.products) {
        this.searchResult = data.products;
        this.searchHistory.push(...data.products);
        this.filterProducts();
      }
    },
    handleProuctInfoError(error) {},

    /**
     * function to generate request for product and cmir mapping request
     */
    generateProductMappingRequest(searchInput) {
      this.$refs.spinner.showSpinner();
      this.quickOrderService.getProductMappingsService(
        {},
        this.handleProuctMappingResponse,
        this.handleProuctMappingError,
        searchInput,
      );
    },

    handleProuctMappingResponse(response) {
      this.$refs.spinner.hideSpinner();
      this.$refs.uploadCsv.close();
      if (response) {
        if (!response.data) {
          this.productSkuMapping = response;
        } else {
          this.productSkuMapping = response.data;
        }
        let productSkus = [];
        let queryString = '';
        this.filterMapping();
        if (!this.searchHistory.length) {
          productSkus = this.validSku.map(item => item.code);
        } else {
          productSkus = this.removeDuplicatesFromCSV(this.validSku);
        }
        if (productSkus.length) {
          queryString = this.generateSearchQuery(productSkus);
          this.generateSearchUrlRequest(queryString);
        }
      }
    },

    handleProuctMappingError(error) {
      this.templateError = '';
      this.$refs.spinner.hideSpinner();
      if (error.response) {
        const errorCodeArray = error.response.data.errors;
        const errorCode = errorCodeArray[0].code;
        if (errorCode === '109') {
          this.templateError = errorCodeArray[0].message;
        }
      }
    },
    addItemsToCart() {
      const cartItems = [];
      // const url = globals.getRestUrl('addMultipleProductsToCart', 'cart');
      // const headers = this.globals.getHeaders();
      this.purchasable.map((product) => {
        cartItems.push({
          code: product.code,
          count: product.quantity,
        });
        // sending the data to Google Analytics on Add to Cart button click
        if (typeof dataLayer !== 'undefined') {
          this.analyticsService.trackAddToCart(product);
        }
        // sending data to yMarketing on add to cart button click
        const cartData = {
          cartCode: this.globals.getCartGuid(),
          productCode: product.code,
          productName: product.name,
          productPrice: product.price.value,
        };
        this.yMarketing.trackAddToCart(product.code, product.quantity, cartData);
      });

      // const data = { items: cartItems };
      // this.quickOrderService.addItemsToCartService(url, headers, data, (isSuccess, data) => {
      //   if (isSuccess) {
      //     this.handleAddToCartResponse(data);
      //   } else {
      //     this.handleAddToCartError(data);
      //   }
      // });
      const requestConfig = {};
      requestConfig.data = {
        items: cartItems,
      };
      this.quickOrderService.addItemsToCart(
        requestConfig,
        this.handleAddToCartResponse,
        this.handleAddToCartError,
      );
      this.$refs.spinner.showSpinner();
    },
    handleAddToCartResponse(response) {
      if (response.data.quickOrderError.length > 0) {
        this.showFlyout('error', this.i18n.quickOrder.addListToCartError, false);
      } else {
        this.dismissFlyout();
      }
      cartEventBus.$emit('call-basic-cart');
      this.$refs.spinner.hideSpinner();
      this.purchasable.splice(0, this.purchasable.length);
      // empty the search history
      this.searchHistory.splice(0, this.searchHistory.length);
      // assigning the non-purchasable products - which were not added to cart back in search history
      this.searchHistory.push(...this.nonPurchasable);
    },
    handleAddToCartError() {
      this.$refs.spinner.hideSpinner();
    },
    clearAll(event) {
      this.purchasable.splice(0, this.purchasable.length);
      this.nonPurchasable.splice(0, this.nonPurchasable.length);
      this.subTotal = 0;
      this.invalidSku = [];
      this.validSku = [];
      this.totalQuantity = 0;
      this.searchHistory = [];
      this.errorValues = [];
    },
    deleteProduct(code) {
      let suggestionInput = [];
      const queryInput = [];
      this.purchasable.map((product) => {
        // sending the data to Google Analytics on delete icon click
        if (typeof dataLayer !== 'undefined') {
          this.analyticsService.trackRemoveFromCart(product);
        }
        // sending data to yMarketing on delete icon click
        // this.purchasable.map((product) => {
        const cartData = {
          cartCode: this.globals.getCartGuid(),
          productCode: product.code,
          productName: product.name,
          productPrice: product.price.value,
        };
        this.yMarketing.trackRemoveFromCart(product.code, product.quantity, cartData);
        globalEventBus.$emit('announce', `Product ${code} deleted`);
        // });
      });
      this.purchasable = this.purchasable.filter(item => item.code !== code);
      this.nonPurchasable = this.nonPurchasable.filter(item => item.code !== code);
      this.searchHistory = this.searchHistory.filter(item => item.code !== code);
      this.searchResult = this.searchResult.filter(item => item.code !== code);
      this.calculateTotal();
      this.purchasable.map((product) => {
        queryInput.push(this.filterList(product, 'code'));
      });
      suggestionInput = {
        items: queryInput,
      };
      cartEventBus.$emit('quick-order-load', suggestionInput);
    },
    handleQuantity(code, quantity) {
      if (quantity === 0) {
        this.deleteProduct(code);
      } else {
        this.purchasable.map((product) => {
          if (code === product.code) {
            // sending data to yMarketing on quantity update
            const cartData = {
              cartCode: this.globals.getCartGuid(),
              productCode: product.code,
              productName: product.name,
              productPrice: product.price.value,
            };
            this.yMarketing.trackUpdateCart(product.code, product.quantity, quantity, cartData);
            // sending the data to Google Analytics on quantity update
            // if (product.quantity !== quantity) {
            //   this.analyticsService.trackRemoveFromCart(product);
            //   this.analyticsService.trackAddToCart(product);
            // }
            product.quantity = quantity;
            product.price.finalPrice = product.price.value * quantity;
            // if (product.weblistPrice) {
            //   product.discount = this.calculateDiscounts(product);
            // }
          }
        });
        this.calculateTotal();
      }
    },
    notifyMe(product) {
      if (this.globals.loggedIn) {
        this.notifyCode = product.code;
        this.$refs.notifyMeModal.open();
      } else {
        this.globals.setCookie('flow', 'cart');
        this.globals.navigateToUrl('login');
      }
    },
    onNotifyMeError(error) {
      this.$refs.notifyMeModal.close();
      this.showFlyout('error', error, true);
    },
    onNotifyMeSuccess(success) {
      this.$refs.notifyMeModal.close();
      this.showFlyout('success', success, true);
    },
  },
};
