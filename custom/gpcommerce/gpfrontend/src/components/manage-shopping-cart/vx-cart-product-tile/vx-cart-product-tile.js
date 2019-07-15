import _ from 'lodash/core';
import debounce from 'lodash/debounce';
import vxStepperControl from '../../common/vx-stepper-control/vx-stepper-control.vue';
import globals from '../../common/globals';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import { cartEventBus, globalEventBus } from '../../../modules/event-bus';
import vxAddInstallation from '../../manage-shopping-cart/vx-add-installation/vx-add-installation.vue';
import vxRemoveInstallation from '../../manage-shopping-cart/vx-remove-installation/vx-remove-installation.vue';
import vxInstallationDetails from '../../manage-shopping-cart/vx-installation-details/vx-installation-details.vue';
import vxGiftingDetails from '../../manage-shopping-cart/vx-gifting-details/vx-gifting-details.vue';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import flyoutBannerMixin from '../../common/vx-flyout-banner/vx-flyout-banner-mixin';
import YMarketingService from '../../common/services/yMarketing-service';
import AnalyticsService from '../../common/services/analytics-service';
import ManageShoppingCartService from '../../common/services/manage-shopping-cart-service';
import vxProductIcons from '../../common/vx-product-icons/vx-product-icons.vue';
import ProductMixin from '../../common/mixins/product-mixin';
import mobileMixin from '../../common/mixins/mobile-mixin';
import {
  ProductAvailability,
  discount,
  flyoutStatus,
} from '../../common/mixins/vx-enums';

export default {
  name: 'vx-cart-product-tile',
  mixins: [flyoutBannerMixin, ProductMixin, mobileMixin],
  components: {
    vxStepperControl,
    vxSpinner,
    vxAddInstallation,
    vxRemoveInstallation,
    vxInstallationDetails,
    vxModal,
    vxGiftingDetails,
    vxProductIcons,
  },
  props: {
    /**
     * data of individual product in the cart
     */
    productData: Object,
    /**
     * text coming from property file
     */
    i18n: Object,
    /**
     * indicactes if this module is to be used for mini cart
     */
    isMiniCart: Boolean,
    /**
     * data of promotion
     */
    promotionData: Object,
    /**
     * indicactes if there is Lease error
     */
    isB2bLease: Boolean,
    /**
     * data of Installation price
     */
    installationPrice: Object,
    isSampleCart: {
      type: Boolean,
      default: false,
    },
    isBundleProduct: {
      type: Boolean,
      default: false,
    },
    isProductRemovable: {
      type: Boolean,
      default: true,
    },
  },
  data() {
    return {
      globals,
      yMarketing: new YMarketingService(),
      analyticsService: new AnalyticsService(),
      giftable: {
        isGiftOpted: false,
        isGiftable: false,
        code: '',
      },
      installable: {
        isInstallable: false,
        isInstalled: false,
        code: '',
        parentProductEntry: this.productData.entryNumber,
        parentProductQuantity: this.productData.quantity,
        parentProductCode: this.productData.product.code,
        parentProductPrice: this.productData.totalPrice.value,
        parentProductName: this.productData.product.name,
      },
      isLeasable: false,
      manageShoppingCartService: new ManageShoppingCartService(),
      ProductAvailability,
      delayInterval: 300,
      discount,
      lowStockEntryData: [],
      flyoutStatus,
      productIconsData: [],
    };
  },
  computed: {
    /**
     * This function shows lease error
     */
    leaseableError() {
      let leaseError = false;
      if (this.globals.isB2B() && this.globals.getIsLoggedIn()) {
        if (this.productData.leasable && !this.isB2bLease) {
          leaseError = true;
        } else {
          leaseError = false;
        }
      }
      return leaseError;
    },
  },
  watch: {
    'productData.quantity': function(newVal, oldVal) {
      this.installable.parentProductQuantity = newVal;
    },
  },
  created() {
    const self = this;
    /**
     * This function handles on adding quantity manually, debounce will send request once user finished typing.
     */
    self.handleQuantity = debounce((itemNumber, itemQuantity) => {
      // sending the data to Google Analytics on quantity update
      if (typeof dataLayer !== 'undefined') {
        if (this.productData.quantity !== itemQuantity) {
          let analyticsObject = {
            code: this.productData.product.code,
            name: this.productData.product.name,
            quantity: itemQuantity,
          };
          this.analyticsService.trackRemoveFromCart(analyticsObject);
          analyticsObject = {
            code: this.productData.product.code,
            name: this.productData.product.name,
            quantity: itemQuantity,
          };
          this.analyticsService.trackAddToCart(analyticsObject);
        }
      }
      // sending data to yMarketing on quantity update
      const cartData = {
        cartCode: this.globals.getCartGuid(),
        productCode: this.productData.product.code,
        productName: this.productData.product.name,
        productPrice: this.productData.basePrice.value,
      };
      this.yMarketing.trackUpdateCart(
        this.productData.product.code,
        this.productData.quantity,
        itemQuantity,
        cartData,
      );
      if (itemQuantity === 0) {
        this.deleteCartItem(itemNumber);
      } else {
        const requestConfig = {};
        requestConfig.data = {
          quantity: itemQuantity,
          installed: this.installable.isInstalled,
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
    if (!this.isMiniCart) {
      this.checkAdditionalAttributes();
      // if (this.globals.isB2B()) {
      //   this.checkLeasable(this.productData);
      // }
    }
    this.productIconsData = this.productData.product.productIcons;
  },

  methods: {
    /**
     * This function deletes an item from cart
     */
    deleteCartItem(itemNumber, event) {
      // sending the data to Google Analytics on delete icon click
      if (typeof dataLayer !== 'undefined') {
        const analyticsObject = {
          code: this.productData.product.code,
          name: this.productData.product.name,
          quantity: this.productData.quantity,
        };
        this.analyticsService.trackRemoveFromCart(analyticsObject);
      }
      this.manageShoppingCartService.deleteCartItem(
        {},
        this.handleDeleteCartItemResponse,
        this.handleDeleteCartItemError,
        itemNumber,
      );
      this.$refs.spinner.showSpinner();
      // sending data to yMarketing on delete icon click
      const cartData = {
        cartCode: this.globals.getCartGuid(),
        productCode: this.productData.product.code,
        productName: this.productData.product.name,
        productPrice: this.productData.totalPrice.value,
      };
      this.yMarketing.trackRemoveFromCart(
        this.productData.product.code,
        this.productData.quantity,
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
     * This function edits an item from cart
     */
    handleQuantity() {
      // dummy function which will invoke inside created().
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
    /**
     * This function opens add installation modal
     */
    openAddInstallationModal(event) {
      this.$refs.addInstallationModal.open(event);
    },
    /**
     * This function handles the success of addition of installation from the modal
     */
    handleAddInstallatioSuccess() {
      this.$refs.addInstallationModal.close();
      this.installable.isInstalled = true;
      globalEventBus.$emit('announce', 'Installation Service Added');
    },
    /**
     * This function handles the error of addition of installation from the modal
     */
    handleAddInstallatioError() {},
    /**
     * This function opens installation details modal
     */
    openInstallationDetailsModal(event) {
      this.$refs.installationDetailsModal.open(event);
    },
    /**
     * This function opens remove installation modal
     */
    openRemoveInstallationModal() {
      if (this.globals.isB2BWhiteLabel()) {
        this.$refs.removeInstallationModal.open(event);
      } else {
        this.removeInstallation();
      }
    },
    /**
     * This function removes installation if added
     */
    removeInstallation() {
      const requestConfig = {};
      requestConfig.data = {
        quantity: this.productData.quantity,
        additionalAttributes: {
          entry: [
            {
              key: 'installed',
              value: 'false',
            },
          ],
        },
      };
      this.manageShoppingCartService.removeInstallation(
        requestConfig,
        this.handleRemoveInstallationResponse,
        this.handleRemoveInstallationError,
        this.productData.entryNumber,
      );
      this.$refs.spinner.showSpinner();
    },
    /**
     * This function handles the response of removal of installation if added
     */
    handleRemoveInstallationResponse() {
      cartEventBus.$emit('cart-update');
      this.$refs.removeInstallationModal.close();
      this.$refs.spinner.hideSpinner();
      this.installable.isInstalled = false;
    },
    /**
     * This function handles the error of removal of installation if added
     */
    handleRemoveInstallationError() {
      this.$refs.spinner.hideSpinner();
    },
    /**
     * This function opens gifting details modal
     */
    openGiftModal(event) {
      this.$refs.addGiftModal.open(event);
    },
    /**
     * This function handles addition and removal of gifting option
     */
    editGift() {
      const requestConfig = {};
      requestConfig.data = {
        quantity: this.productData.quantity,
        additionalAttributes: {
          entry: [
            {
              key: 'giftOpted',
              value: this.giftable.isGiftOpted.toString(),
            },
          ],
        },
      };
      this.manageShoppingCartService.editGift(
        requestConfig,
        this.handleEditGiftResponse,
        this.handleEditGiftError,
        this.productData.entryNumber,
      );
      this.$refs.spinner.showSpinner();
    },
    /**
     * This function handles the response of addition and removal of gifting option
     */
    handleEditGiftResponse() {
      cartEventBus.$emit('cart-update');
      this.$refs.spinner.hideSpinner();
    },
    /**
     * This function handles the error of addition and removal of gifting option
     */
    handleEditGiftError() {
      this.$refs.spinner.hideSpinner();
    },
    /**
     * This function checks installation and gifting options
     */
    checkAdditionalAttributes() {
      if (this.productData.additionalAttributes) {
        // check if the product is already opted for installation
        if (this.globals.isB2B()) {
          this.checkInstallation(this.productData.additionalAttributes);
        }
        if (this.globals.isB2C() || this.globals.isEmployeeStore()) {
          this.checkGiftable(this.productData.additionalAttributes);
        }
      }
    },
    /**
     * This function checks gifting options
     * @param  {Object} additionalAttributes contains data of the product
     */
    checkGiftable(additionalAttributes) {
      const entry = additionalAttributes.entry;
      // check if we need to show gifting property
      if (this.getKey(entry, 'giftableProduct')) {
        this.giftable.code = this.getKey(entry, 'giftableProduct').value;
        this.giftable.isGiftable = true;
        if (this.getKey(entry, 'giftOpted')) {
          this.giftable.isGiftOpted =
            JSON.parse(this.getKey(entry, 'giftOpted').value) || false;
        }
      }
    },
    /**
     * This function checks installational options
     * @param  {Object} additionalAttributes contains data of the product
     */
    checkInstallation(additionalAttributes) {
      const entry = additionalAttributes.entry;
      // check if we need to show the user installation property
      if (this.getKey(entry, 'installableProductCode')) {
        this.installable.code = this.getKey(
          entry,
          'installableProductCode',
        ).value;
        this.installable.isInstallable = true;
        if (this.getKey(entry, 'installed')) {
          this.installable.isInstalled = JSON.parse(
            this.getKey(entry, 'installed').value,
          );
        }
      }
    },
    // check if we need to show the user leasable property
    // checkLeasable(product) {
    //   if (product.leasable) {
    //     this.isLeasable = true;
    //   }
    // },
    /**
     * This function finds a prticular key from an array
     * @param  {Array} entry product entry
     * @param  {String} keyCode key to be searched in the array
     */
    getKey(entry, keyCode) {
      return _.find(entry, {
        key: keyCode,
      });
    },
    maxValueUpdated() {
      this.showFlyout(
        this.flyoutStatus.error,
        this.i18n.maxValueUpdatedStatus,
        true,
      );
    },
    maxOrderQuantity(maxValue, qty, isgiveAway) {
      if (isgiveAway) {
        return qty;
      }
      return maxValue;
    },
    minValueUpdated() {
      this.showFlyout(
        this.flyoutStatus.error,
        this.i18n.minValueUpdatedStatus,
        true,
      );
    },
    minOrderQuantity(minValue, qty, isgiveAway) {
      if (isgiveAway) {
        return qty;
      }
      return minValue;
    },
    setStockLevel(stockLevel) {
      return stockLevel;
    },
    stockLevelUpdated() {
      this.showFlyout(
        this.flyoutStatus.error,
        this.i18n.stockLevelUpdatedStatus,
        true,
      );
    },
  },
};
