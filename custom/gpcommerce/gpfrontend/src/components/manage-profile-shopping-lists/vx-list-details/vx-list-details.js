import VueBarcode from 'vue-barcode';
import globals from '../../common/globals';
import vxProductTile from '../../common/vx-product-tile/vx-product-tile.vue';
import vxCreateList from '../vx-create-list/vx-create-list.vue';
import vxDeleteList from '../vx-delete-list/vx-delete-list.vue';
import vxDownloadInfo from '../../common/vx-download-info/vx-download-info.vue';
import vxEditListName from '../vx-edit-list-name/vx-edit-list-name.vue';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import vxStepperControl from '../../common/vx-stepper-control/vx-stepper-control.vue';
import shareList from '../vx-share-list/vx-share-list.vue';
import downloadProductInfo from '../vx-download-product-info/vx-download-product-info.vue';
import flyoutBannerMixin from '../../common/vx-flyout-banner/vx-flyout-banner-mixin';
import flyoutBannerSecondaryMixin from '../../common/vx-flyout-banner-secondary/vx-flyout-banner-secondary-mixin';
import {
  ProductAvailability,
  addToCartStatus,
  flyoutStatus,
  generatePdf,
  cookies,
} from '../../common/mixins/vx-enums';
import AnalyticsService from '../../common/services/analytics-service';
import { cartEventBus } from '../../../modules/event-bus';
import YMarketingService from '../../common/services/yMarketing-service';
import saveCart from '../../manage-shopping-cart/vx-save-cart/vx-save-cart.vue';
import vxNotifyMe from '../../search-browse/vx-notify-me/vx-notify-me.vue';
import ManageProfileShoppingListService from '../../common/services/manage-profile-shopping-lists-service';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import vxAddProductToList from '../vx-add-product-to-list/vx-add-product-to-list.vue';
import vxPdfGenerator from '../../common/vx-pdf-generator/vx-pdf-generator.vue';
import vxAddCustomAttribute from '../vx-add-custom-attribute/vx-add-custom-attribute.vue';
import vxAccordion from '../../common/vx-accordion/vx-accordion.vue';
import cookiesMixin from '../../common/mixins/cookies-mixin';
import vxShareItem from '../../pdp/vx-share-item/vx-share-item.vue';
import vxProductIcons from '../../common/vx-product-icons/vx-product-icons.vue';
import ProductMixin from '../../common/mixins/product-mixin';
import mobileMixin from '../../common/mixins/mobile-mixin';

export default {
  name: 'vx-list-details',
  mixins: [
    flyoutBannerMixin,
    flyoutBannerSecondaryMixin,
    cookiesMixin,
    ProductMixin,
    mobileMixin,
  ],
  components: {
    'vx-product-tile': vxProductTile,
    vxModal,
    vxStepperControl,
    vxCreateList,
    vxDeleteList,
    vxDownloadInfo,
    vxNotifyMe,
    'vx-edit-list-name': vxEditListName,
    'vx-share-list': shareList,
    'vx-save-cart': saveCart,
    'vx-download-product-info': downloadProductInfo,
    vxSpinner,
    vxAddProductToList,
    vxPdfGenerator,
    vxAddCustomAttribute,
    vxAccordion,
    barcode: VueBarcode,
    vxShareItem,
    vxProductIcons,
  },
  props: {
    /**
     * Labels, button and caption texts
     */
    i18n: {
      type: Object,
    },
    /**
     * check for is favorites
     */
    isFavorites: {
      type: Boolean,
    },
    /**
     * check for is precurated
     */
    isPrecurated: {
      type: Boolean,
    },
    isSharedList: {
      type: Boolean,
    },
    isSharedCart: {
      type: Boolean,
    },
    isSharable: {
      type: Boolean,
      default: false,
    },
    isExcelDownloadable: {
      type: Boolean,
      default: false,
    },
    isDownloadable: {
      type: Boolean,
      default: false,
    },
    colorCodes: {
      type: Object,
      default: {},
    },
    isFilterRequired: {
      type: Boolean,
      default: false,
    },
    isAddProduct: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      ProductAvailability,
      flyoutStatus,
      addToCartStatus,
      globals,
      productEntries: [],
      listName: '',
      listId: '',
      quantityUpdate: {},
      wishlistUid: '',
      subtotal: 0,
      updatedQuantity: 0,
      prodPrice: 0,
      noListMessage: '',
      noProducts: false,
      message: '',
      selectedLists: [],
      selectedListNames: [],
      analyticsService: new AnalyticsService(),
      yMarketing: new YMarketingService(),
      manageProfileShoppingListService: new ManageProfileShoppingListService(),
      notifyCode: '',
      searchQuery: '',
      currentProduct: {},
      canAddProductsToList: false,
      isAddProductsToList: true,
      customAttributes: [],
      attributeList: [],
      isAccordionOpen: false,
      pdfInfo: {},
      pdf: {},
      pdpListdata: [],
      loadPdfGenerator: false,
      fromPdfModal: false,
      featureProducts: [],
      activeContainerIndex: '',
      accordionData: {
        hideAccordion: this.i18n.listDetails.hideAttribute,
        viewAccordion: this.i18n.listDetails.viewAttribute,
      },
      generatePdf,
      cookies,
      categoryGroups: {},
      addToCartDisabled: false,
      certificationsName: '',
      hasSustainabilityPage: true,
      isShareList: false,
    };
  },
  computed: {
    filteredProductEntries() {
      const search = this.searchQuery.toLowerCase().trim();

      if (!search) return this.productEntries;

      return this.productEntries.filter(
        item => item.product.name.toLowerCase().indexOf(search) > -1,
      );
    },
  },
  created() {
    this.listName = this.globals.getUrlParam('listName');
    // isShareList param is present when user navigates to list page via share list pdf email
    this.isShareList = this.globals.getUrlParam('isShareList');
    this.selectedLists.push(this.listName);
    if (this.globals.getCookie(cookies.guestListUid) && !this.listName) {
      if (!this.globals.loggedIn && this.globals.isB2B()) {
        this.setListPageUrl();
      }
    }
  },
  mounted() {
    this.onInit();
  },
  methods: {
    /**
     * This function handles the response of add product to cart service
     */
    handleAddProductResponse(response) {
      if (response && response.data) {
        cartEventBus.$emit('call-basic-cart');
      }
      if (response.data.statusCode) {
        if (
          response.data.statusCode ===
          this.i18n.listDetails.maxPurchaseableQuantityErrorCode
        ) {
          if (
            response.data.statusCode ===
            this.i18n.listDetails.maxPurchaseableQuantityErrorCode
          ) {
            if (
              this.productEntry &&
              this.productEntry.product.maxOrderQuantity &&
              response.data.quantityAdded ===
                this.productEntry.product.maxOrderQuantity
            ) {
              this.showFlyout(
                'error',
                this.i18n.listDetails.maxPurchaseableQuantityErrorMessage,
                true,
              );
            } else if (
              this.productEntry &&
              this.productEntry.product.maxOrderQuantity &&
              response.data.quantityAdded <
                this.productEntry.product.maxOrderQuantity
            ) {
              this.showFlyout(
                'error',
                `${this.i18n.listDetails.maxPurchaseableQuantityUpdateMessage1 +
                  this.productEntries.filter(
                    obj => obj.product.code === this.currentProduct.productCode,
                  )[0].product.maxOrderQuantity}, ${
                  this.i18n.listDetails.maxPurchaseableQuantityUpdateMessage2
                }`,
                true,
              );
            }
          }
        } else if (
          response.data.statusCode === this.i18n.listDetails.lowStockErrorCode
        ) {
          this.showFlyout(
            'error',
            this.i18n.listDetails.lowStockErrorMessage,
            true,
          );
        }
      }
    },
    /**
     * This function handles the error of add product to cart service
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
        if (
          error.response.data.errors[0].code ===
          this.i18n.listDetails.maxPurchaseableQuantityErrorCode
        ) {
          this.showFlyout(
            'error',
            `${this.i18n.listDetails.maxPurchaseableQuantityUpdateMessage1} ${
              this.productEntries.filter(
                obj => obj.product.code === this.currentProduct.productCode,
              )[0].product.maxOrderQuantity
            }, ${this.i18n.listDetails.maxPurchaseableQuantityUpdateMessage2}`,
            true,
          );
        } else if (
          error.response.data.errors[0].code ===
          this.i18n.listDetails.lowStockErrorCode
        ) {
          this.showFlyout(
            'error',
            this.i18n.listDetails.lowStockErrorMessage,
            true,
          );
        }
      }
    },
    onInit(fromPdf) {
      this.$refs.spinner.showSpinner();
      if (this.isPrecurated) {
        const requestConfig = {};
        requestConfig.data = {
          listName: this.listName,
        };
        this.manageProfileShoppingListService.getPrecuratedListDetails(
          requestConfig,
          this.handleShoppingListResponse,
          this.handleShoppingListError,
          fromPdf,
        );
      } else if (this.isFavorites) {
        const requestConfig = {};
        if (this.isSharedList || this.isSharedCart) {
          requestConfig.data = {
            listName: this.listName,
          };
          this.manageProfileShoppingListService.getSharedListDetails(
            requestConfig,
            this.handleShoppingListResponse,
            this.handleShoppingListError,
            fromPdf,
          );
        } else {
          this.manageProfileShoppingListService.getFavoritesListDetails(
            requestConfig,
            this.handleShoppingListResponse,
            this.handleShoppingListError,
            fromPdf,
          );
        }
      } else if (this.isSharedList || this.isSharedCart) {
        const requestConfig = {};
        requestConfig.data = {
          listName: this.listName,
        };
        this.manageProfileShoppingListService.getSharedListDetails(
          requestConfig,
          this.handleShoppingListResponse,
          this.handleShoppingListError,
          fromPdf,
        );
      } else {
        const requestConfig = {};
        this.manageProfileShoppingListService.getListDetails(
          requestConfig,
          this.handleShoppingListResponse,
          this.handleShoppingListError,
          this.listName,
          fromPdf,
          this.isShareList,
        );
      }
    },
    /**
     * This function handles the response of add product to cart service
     */
    handleShoppingListResponse(response) {
      this.$refs.spinner.hideSpinner();
      if (
        !response.data.wishlistEntries ||
        response.data.wishlistEntries.length === 0
      ) {
        this.noListMessage = this.i18n.listDetails.noProductMessage;
        this.noProducts = true;
        this.productEntries = response.data.wishlistEntries;
      } else {
        this.productEntries = response.data.wishlistEntries;
      }
      this.categoryGroups = response.data.wishlistEntriesGroup;
      if (
        response.data.customAttr1 ||
        response.data.customAttr2 ||
        response.data.customAttr3
      ) {
        this.categoryGroups.map((category) => {
          category.value.wishlistEntryGroup.map((entry) => {
            entry.product.customAttr1Value = !entry.customAttr1Value ?
              '' :
              entry.customAttr1Value;
            entry.product.customAttr2Value = !entry.customAttr2Value ?
              '' :
              entry.customAttr2Value;
            entry.product.customAttr3Value = !entry.customAttr3Value ?
              '' :
              entry.customAttr3Value;
          });
        });
      }
      if (!this.fromPdfModal) {
        const certificationsList = [];
        this.productEntries.map((product) => {
          if (product.product && product.product.gpCertifications) {
            product.product.gpCertifications.map((certification) => {
              certificationsList.push(certification.id);
            });
          }
        });
        this.certificationsName = certificationsList.join();
      }
      this.handleCustomAttributesResponse(response);
      this.wishlistUid = response.data.wishlistUid;
      this.subtotal = response.data.subtotal;
      this.listId = response.data.name;
      const existingListName = this.selectedListNames.indexOf(this.listId);
      if (existingListName === -1) {
        this.$set(this.selectedListNames, 0, this.listId);
      }
      if (this.fromPdfModal) {
        this.pdpListdata = response.data;
        this.hasSustainabilityPage =
          response.data.soldToId !== generatePdf.soldTo555555;
        this.loadPdfGenerator = true;
        this.fromPdfModal = false;
        this.$nextTick(() => {
          this.identifyPdfFormat();
        });
      }
      // create guest wishlist cookie if guest user navigates to list page via share list pdf email
      if (this.isShareList && !this.globals.loggedIn) {
        if (
          this.globals.siteConfig.isShareWishlistEnabled &&
          !this.globals.getCookie(cookies.guestListUid)
        ) {
          this.createCookie(
            cookies.guestListUid,
            response.data.wishlistUid,
            3000,
          );
        }
      }
      // update url with wishlistUid once user navigates to list page via share list pdf email
      if (this.isShareList) {
        this.selectedLists = [];
        this.setListPageUrl();
      }
      this.isShareList = false;
    },
    /**
     * This function handles the error of add product to cart service
     */
    handleShoppingListError(error) {
      this.$refs.spinner.hideSpinner();
      if (!this.listName && error.response.data.errors[0].code === '109') {
        this.noProducts = true;
        this.noListMessage = this.i18n.listDetails.noProductMessage;
      }
    },
    /**
     * This function handles the data of custom attribute from response
     */
    handleCustomAttributesResponse(response) {
      this.customAttributes = [];
      this.attributeList = [];
      this.isAccordionOpen = false;
      const attributes = [
        {
          label: 'customAttr1',
          value: 'customAttr1Value',
        },
        {
          label: 'customAttr2',
          value: 'customAttr2Value',
        },
        {
          label: 'customAttr3',
          value: 'customAttr3Value',
        },
      ];
      attributes.map((attr) => {
        if (response.data[attr.label]) {
          this.attributeList.push({
            label: attr.value,
            value: response.data[attr.label],
          });
        } else {
          this.attributeList.push({
            label: attr.value,
            value: '',
          });
        }
      });
      this.customAttributes = this.attributeList.filter(
        attribute => attribute.value !== '',
      );
    },
    /**
     * This function is called when add list to cart button
     */
    addListToCart() {
      const self = this;
      const requestConfig = {};
      self.manageProfileShoppingListService.addListToCart(
        requestConfig,
        self.handleAddListResponse,
        self.handleAddistError,
        self.listName,
      );
    },
    /**
     * This function handles the response of add list to cart service
     */
    handleAddListResponse(response) {
      if (response && response.data) {
        cartEventBus.$emit('call-basic-cart');
        const nonPurchasable = [];
        response.data.map((product) => {
          if (
            product.statusCode === addToCartStatus.failure ||
            product.statusCode ===
              this.i18n.listDetails.maxPurchaseableQuantityErrorCode ||
            product.statusCode === this.i18n.listDetails.lowStockErrorCode
          ) {
            nonPurchasable.push(product.errorProductName);
          }
        });
        if (nonPurchasable.length > 0) {
          this.showFlyout(
            'error',
            this.i18n.listDetails.addListToCartError,
            false,
          );
        }
      }
    },
    /**
     * This function handles the error of add list to cart service
     */
    handleAddistError() {},
    handleSaveList() {
      if (this.globals.loggedIn) {
        this.$refs.selectListModal.open();
      } else {
        this.globals.navigateToUrl('login');
      }
    },
    savePrecuratedList() {
      const products = [];
      this.productEntries.map((product) => {
        products.push({
          entryType: 'PRODUCT',
          product: {
            code: product.product.code,
          },
          quantity: 1,
        });
      });
      cartEventBus.$emit('precurated-list-products', products);
    },
    onSelectListSuccess() {
      this.$refs.selectListModal.close();
      this.showFlyout(
        'success',
        this.i18n.listDetails.selectListResponse,
        true,
      );
    },
    /**
     * This function is called when add list to trash icon in the product tile
     */
    deleteCartItem(itemNumber) {
      this.$refs.spinner.showSpinner();
      const requestBody = {
        wishlistUid: this.wishlistUid,
        product: {
          code: itemNumber,
        },
      };
      const requestConfig = {};
      requestConfig.data = requestBody;
      this.manageProfileShoppingListService.deleteCartItem(
        requestConfig,
        this.handleDeleteCartItemResponse,
        this.handleDeleteCartItemError,
      );
    },
    /**
     * This function handles the response of delete cart item service
     */
    handleDeleteCartItemResponse() {
      this.onInit();
      this.$refs.spinner.hideSpinner();
    },
    /**
     * This function handles the error of delete cart item service
     */
    handleDeleteCartItemError() {
      this.$refs.spinner.hideSpinner();
    },
    /**
     * This function handles the success emitted from the delete list modal component
     */
    deleteModalSuccess(success) {
      this.onInit();
      this.$refs.deleteListModal.close();
      if (this.readCookie(cookies.guestListUid)) {
        this.eraseCookie(cookies.guestListUid);
      }
      this.showFlyout('success', success, true);
      const listViewPage =
        this.globals.getNavBaseUrl() + this.globals.serviceUrls.listLandingView;
      window.location.href = listViewPage;
    },
    /**
     * This function handles the error emitted from the delete list modal component
     */
    deleteModalError(error) {
      this.onInit();
      this.$refs.deleteListModal.close();
      this.showFlyout('error', error, true);
    },
    /**
     * This function handles the error emitted from the share list modal component
     */
    onShareListError(error) {
      this.$refs.shareItemModal.close();
      this.showFlyout('error', error, true);
    },
    /**
     * This function handles the sucess emitted from the share list modal component
     */
    onShareListSuccess(success) {
      this.$refs.shareItemModal.close();
      if (this.productEntries.length === 1) {
        this.showFlyout(flyoutStatus.success, success, true);
      } else if (this.productEntries.length > 1) {
        this.showFlyout(
          flyoutStatus.success,
          this.i18n.listDetails.shareMultipleItemResponse,
          true,
        );
      }
    },
    /**
     * This function handles the error emitted from the share list pdf modal component
     */
    onShareListPdfError(error) {
      this.$refs.shareListPdfModal.close();
      this.showFlyout('error', error, true);
    },
    /**
     * This function handles the success emitted from the share list modal pdf component
     */
    onShareListPdfSuccess(success) {
      this.$refs.shareListPdfModal.close();
      if (this.productEntries.length === 1) {
        this.showFlyout(flyoutStatus.success, success, true);
      } else if (this.productEntries.length > 1) {
        this.showFlyout(
          flyoutStatus.success,
          this.i18n.listDetails.shareMultipleItemResponse,
          true,
        );
      }
    },
    /**
     * This function handles the sucess emitted from the edit list name modal component
     */
    editListNameSuccess(success) {
      this.$refs.editListModal.close();
      this.$refs.spinner.showSpinner();
      this.showFlyout('success', success, true);
      this.onInit();
    },
    /**
     * This function handles the error emitted from the edit list name modal component
     */
    editListNameError(error) {
      this.$refs.editListModal.close();
      this.showFlyout('error', error, true);
    },
    /**
     * This function is called when the edit icon is clicked
     */
    handleEditList(event) {
      this.$refs.editListModal.open(event);
    },
    /**
     * This function is called when the share icon is clicked
     */
    handleShareList(event, type) {
      if (this.globals.siteConfig.isShareWishlistEnabled && type === 'list') {
        // share list as pdf
        this.$refs.shareListPdfModal.open(event);
      } else {
        // normal list share
        this.$refs.shareItemModal.open(event);
      }
    },
    /**
     * This function is called when the Download icon is clicked
     */
    handleDownloadProductInfo(event) {
      this.$refs.downloadProductExcelModal.open(event);
    },
    /**
     * This function is called when the delete icon is clicked
     */
    handleDeleteList(event) {
      this.$refs.deleteListModal.open(event);
    },
    /**
     * This function is called when the download icon is clicked
     */
    handleDownloadList(event) {
      // this.$refs.pdfModule.renderImage('img#itf1');
      this.$refs.downloadImagePdfModal.open(event);
    },
    /**
     * This function is called when stepper control is clicked
     */
    getQuantity(prodPrice, prodCode, updatedQuantity) {
      this.$refs.spinner.showSpinner();
      this.updatedQuantity = updatedQuantity;
      if (this.updatedQuantity === 0) {
        this.deleteCartItem(prodCode);
      } else {
        this.prodPrice = Number(prodPrice.replace(',', '').substr(1));
        this.quantityUpdate = {
          wishlistUid: this.wishlistUid,
          entryType: 'PRODUCT',
          quantity: updatedQuantity,
          product: {
            code: prodCode,
          },
        };
        const requestConfig = {};
        requestConfig.data = this.quantityUpdate;
        this.manageProfileShoppingListService.updateQuantity(
          requestConfig,
          this.handleUpdateQuantityResponse,
          this.handleUpdateQuantityError,
        );
      }
    },
    /**
     * This function handles the error of update quantity service
     */
    handleUpdateQuantityError() {
      this.$refs.spinner.hideSpinner();
    },
    /**
     * This function handles the response of update quantity service
     */
    handleUpdateQuantityResponse(response) {
      this.$refs.spinner.hideSpinner();
      if (response) {
        this.handleShoppingListResponse(response);
      }
    },
    // Open gifting details modal
    openGiftModal(message) {
      this.message = message;
      this.$refs.addGiftModal.open();
    },
    openInstallationDetailsModal(message) {
      this.message = message;
      this.$refs.installationDetailsModal.open();
    },
    addToCart(prodQuantity, prodCode, prodPrice) {
      this.currentProduct.productCode = prodCode;
      const requestObjParams = {
        quantity: prodQuantity,
        product: {
          code: prodCode,
        },
      };
      // sending the data to Google Analytics on Add to Cart button click
      if (typeof dataLayer !== 'undefined') {
        const analyticsObject = {
          code: prodCode,
          name: this.listId,
          quantity: prodQuantity,
        };
        this.analyticsService.trackAddToCart(analyticsObject);
      }
      const requestConfig = {};
      requestConfig.data = requestObjParams;
      this.manageProfileShoppingListService.addProductToCart(
        requestConfig,
        this.handleAddProductResponse,
        this.handleAddProductError,
      );
      // sending data to yMarketing on add to cart button click
      const cartData = {
        cartCode: this.globals.getCartGuid(),
        productCode: prodCode,
        productName: this.listId,
        productPrice: prodPrice,
      };
      this.yMarketing.trackAddToCart(prodCode, prodQuantity, cartData);
    },
    notifyMe(product) {
      this.notifyCode = product.code;
      this.$refs.notifyMeModal.open();
    },
    onNotifyMeError(error) {
      this.$refs.notifyMeModal.close();
      this.showFlyout('error', error, true);
    },
    onNotifyMeSuccess(success) {
      this.$refs.notifyMeModal.close();
      this.showFlyout('success', success, true);
    },
    maxValueUpdated() {
      this.showFlyout(
        'error',
        this.i18n.listDetails.maxValueUpdatedStatus,
        true,
      );
    },
    maxOrderQuantity(maxValue) {
      return maxValue;
    },
    minValueUpdated() {
      this.showFlyout(
        this.flyoutStatus.error,
        this.i18n.listDetails.minValueUpdatedStatus,
        true,
      );
    },
    minOrderQuantity(minValue) {
      return minValue;
    },
    setStockLevel(stockLevel) {
      return stockLevel;
    },
    stockLevelUpdated() {
      this.showFlyout(
        this.flyoutStatus.error,
        this.i18n.listDetails.stockLevelUpdatedStatus,
        true,
      );
    },
    accordionStatus(event, index) {
      this.isAccordionOpen = !this.isAccordionOpen;
      const accordion = document.querySelector(`.accordion-${index}`);
      if (this.isAccordionOpen) {
        accordion.style.display = 'block';
      } else {
        accordion.style.display = 'none';
      }
    },
    saveCustomAttributeValue(evt, product, attribute, index) {
      if (evt.relatedTarget) {
        const custAttribute = evt.target.value;
        this.activeContainerIndex = index;
        const requestConfig = {};
        const requestBody = {
          wishlistUid: this.wishlistUid,
          code: product.code,
          customAttr1Value: product.customAttr1Value,
          customAttr2Value: product.customAttr2Value,
          customAttr3Value: product.customAttr3Value,
        };
        requestBody[attribute.label] = custAttribute;
        requestConfig.data = requestBody;
        this.manageProfileShoppingListService.saveCustomAttributes(
          requestConfig,
          this.handleSaveCustomAttributeResponse,
          this.handleSaveCustomAttributeError,
        );
      }
    },
    handleSaveCustomAttributeResponse() {
      this.onInit();
    },
    handleSaveCustomAttributeError() {},
    handleAddCustomAttribute() {
      this.$refs.addCustomAttributeModal.open();
    },
    handleAddProductsToList(productsArray) {
      const productsString = productsArray.replace(/\s/g, '');
      const requestConfig = {};
      requestConfig.params = {
        wishlistid: this.wishlistUid,
        productcodes: productsString,
      };
      this.manageProfileShoppingListService.addProductToList(
        requestConfig,
        this.handleAddProductToListResponse,
        this.handleAddProductToListError,
        this.wishlistUid,
      );
    },
    handleAddProductToListResponse(response) {
      this.showFlyout(flyoutStatus.success, response.data.description, true);
      this.noProducts = false;
      if (response.data.errorMessage) {
        this.showFlyoutSecondary(
          flyoutStatus.error,
          response.data.errorMessage,
          true,
        );
      }
      this.onInit();
    },
    handleAddProductToListError() {
      this.showFlyout(
        flyoutStatus.error,
        this.i18n.listDetails.addProductToListError,
        true,
      );
    },
    onCustomAttributeInit(attributeInfo) {
      const requestConfig = {};
      requestConfig.data = {
        wishlistUid: this.wishlistUid,
        customAttr1: attributeInfo.attributeOne,
        customAttr2: attributeInfo.attributeTwo,
        customAttr3: attributeInfo.attributeThree,
      };
      this.manageProfileShoppingListService.addCustomAttributes(
        requestConfig,
        this.handleAddCustomAttributeSuccess,
        this.handleAddCustomAttributeError,
      );
    },
    handleAddCustomAttributeSuccess() {
      this.$refs.addCustomAttributeModal.close();
      this.onInit();
    },
    handleAddCustomAttributeError() {},
    onImageDownloadInit(imageInfo) {
      const requestConfig = {};
      requestConfig.params = {
        wishlistid: this.wishlistUid,
        imageformat: imageInfo.format,
        resolution: imageInfo.size,
        allimages: imageInfo.allImages,
      };
      this.manageProfileShoppingListService.getImagesInZipFormat(
        requestConfig,
        this.handleimageDownloadResponse,
        this.handleImageDownloadError,
        this.wishlistUid,
      );
    },
    handleimageDownloadResponse(response) {
      const filename = this.i18n.listDetails.defaultDownloadedFilename;
      const blob = new Blob([response.data], {
        type: 'application/zip',
      });
      const link = document.createElement('a');
      link.href = window.URL.createObjectURL(blob);
      if (window.navigator && window.navigator.msSaveOrOpenBlob) {
        window.navigator.msSaveOrOpenBlob(
          blob,
          this.i18n.listDetails.defaultDownloadedFilename,
        );
        this.$refs.downloadImagePdfModal.close();
      }
      link.download = filename;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      this.$refs.downloadImagePdfModal.close();
    },
    handleImageDownloadError() {
      this.showFlyout(
        flyoutStatus.error,
        this.i18n.listDetails.imageDownloadError,
        true,
      );
    },
    onPdfDownloadInit(pdfInfo) {
      this.pdfInfo = pdfInfo;
      this.fromPdfModal = true;
      this.featureProducts = pdfInfo.featureProducts;
      this.onInit(true);
    },
    identifyPdfFormat() {
      if (
        this.pdfInfo.featureCheckedItems === generatePdf.fullDetails &&
        this.featureProducts.length
      ) {
        this.createCustomStylePDF();
      } else {
        switch (this.pdfInfo.formatColumns) {
          case 'Display in one column':
            this.createOneColumnFormat();
            break;
          case 'Display in two columns':
            this.createTwoColumnFormat();
            break;
          case 'Display in three columns':
            this.createThreeColumnFormat();
            break;
          case 'Display as full detail':
            this.createPdpFormat();
            break;
          default:
            this.createOneColumnFormat();
        }
      }
    },
    createCustomStylePDF() {
      this.$refs.pdfModule.createCustomStylePDF(
        this.pdfInfo,
        this.featureProducts,
      );
      this.$refs.downloadImagePdfModal.close();
    },
    createOneColumnFormat() {
      this.$refs.pdfModule.createOneColumnFormat(this.pdfInfo);
      this.loadPdfGenerator = false;
      this.$refs.downloadImagePdfModal.close();
    },
    createTwoColumnFormat() {
      this.$refs.pdfModule.createTwoColumnFormat(this.pdfInfo);
      this.loadPdfGenerator = false;
      this.$refs.downloadImagePdfModal.close();
    },
    createThreeColumnFormat() {
      this.$refs.pdfModule.createThreeColumnFormat(this.pdfInfo);
      this.loadPdfGenerator = false;
      this.$refs.downloadImagePdfModal.close();
    },
    createPdpFormat() {
      this.$refs.pdfModule.createListPdfPDPFormat(this.pdfInfo);
      this.loadPdfGenerator = false;
      this.$refs.downloadImagePdfModal.close();
    },
    handleListPdpFormatError(error) {
      // this.$refs.spinner.hideSpinner();
    },
    /**
     * This function handles the response of update quantity service
     */
    handleListPdpFormatResponse(response) {
      // this.$refs.spinner.hideSpinner();
      if (response) {
        this.pdpListdata = response.data.products;
      }
    },
    limitCharacters(data) {
      let newData = data.length > 500 ? `${data.substring(0, 500)}...` : data;
      newData = this.replaceText(newData);
      return newData;
    },
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
    downloadExcel(excelDownloadOption) {
      const requestConfig = {};
      requestConfig.params = {
        listkey: excelDownloadOption,
      };
      this.manageProfileShoppingListService.getExcelFile(
        requestConfig,
        this.handleExcelDownloadResponse,
        this.handleExcelDownloadError,
        this.wishlistUid,
      );
    },
    handleExcelDownloadResponse(response) {
      const disposition = response.headers['content-disposition'];
      const reg = /filename[^;=\n]*=(UTF-8(['"]*))?(.*)/;
      const matches = reg.exec(disposition);
      const filename =
        matches != null && matches[3]
          ? matches[3]
          : this.i18n.listDetails.defaultExcelFilename;
      // const filename = 'AllProducts.zip';
      const blob = new Blob([response.data], {
        type: 'application/zip',
      });
      const link = document.createElement('a');
      link.href = window.URL.createObjectURL(blob);
      if (window.navigator && window.navigator.msSaveOrOpenBlob) {
        window.navigator.msSaveOrOpenBlob(
          blob,
          this.i18n.listDetails.defaultExcelFilename,
        );
        this.$refs.downloadProductExcelModal.close();
      }
      link.download = filename;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      this.$refs.downloadProductExcelModal.close();
    },
    handleExcelDownloadError() {
      console.log('There is an error');
    },
    handleRemoveCategoryClick(evt, categoryGroup) {
      const productArray = categoryGroup.value.wishlistEntryGroup;
      let productString = '';
      productArray.map((item) => {
        productString = `${productString + item.product.code},`;
      });
      const requestConfig = {};
      const requestBody = {
        wishlistUid: this.wishlistUid,
        entryType: 'PRODUCT',
        category: categoryGroup.key,
        product: {
          code: productString,
        },
      };
      requestConfig.data = requestBody;
      this.manageProfileShoppingListService.removeCategory(
        requestConfig,
        this.handleRemoveCategoryResponse,
        this.handleRemoveCategoryError,
      );
    },
    handleRemoveCategoryResponse() {
      this.onInit();
    },
    handleRemoveCategoryError() {},
    disableAddToCart() {
      this.addToCartDisabled = true;
    },
    setListPageUrl() {
      let wuid = '';
      if (!this.globals.loggedIn) {
        wuid = this.readCookie(cookies.guestListUid) ?
          this.readCookie(cookies.guestListUid) :
          '';
      } else {
        wuid = this.wishlistUid;
      }
      const url = `${this.globals.getNavigationUrl(
        'listDetails',
      )}/?listName=${wuid}`;
      history.replaceState({}, 'List Detail', url);
      this.listName = this.globals.getUrlParam('listName');
      this.selectedLists.push(this.listName);
    },
    closeAddProductPopover() {
      this.canAddProductsToList = false;
    },
  },
  updated() {
    if (
      document.body.scrollHeight > window.innerHeight &&
      document.getElementsByClassName('add-to-cart-container')[0] &&
      document.getElementsByClassName('footer-section')[0]
    ) {
      document.getElementsByClassName('footer-section')[0].style.paddingBottom =
        '70px';
    }
  },
};
