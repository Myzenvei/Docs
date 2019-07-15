import globals from '../../common/globals';
import BundleService from '../../common/services/bundle-service';
import vxAccordion from '../../common/vx-accordion/vx-accordion.vue';
import vxSlider from '../vx-slider/vx-slider.vue';
import vxProductTileVertical from '../../search-browse/vx-product-tile-vertical/vx-product-tile-vertical.vue';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import { ProductAvailability } from '../../common/mixins/vx-enums';

import { cartEventBus } from '../../../modules/event-bus';

import buildBundleData from './vx-build-bundle-data';

export default {
  name: 'vx-build-bundle',
  components: {
    vxSlider,
    vxAccordion,
    vxProductTileVertical,
    vxSpinner,
  },
  props: {
    i18n: {
      type: Object,
      required: true,
    },
    isBazaarVoice: {
      type: Boolean,
      default: true,
    },
  },
  data() {
    return {
      globals,
      bundleService: new BundleService(),
      bundlesData: {},
      rootBundleId: '',
      rootBundleData: {},
      leafBundles: [],
      selectedProducts: [],
      preSelectedProducts: [],
      updateBundle: false,
      buildBundleData,
      cartBundleNumber: '',
      entryGroupMap: {},
      pdpProductCode: '',
    };
  },
  computed: {
    addToCartEnabled() {
      const bundlesMinConditionData = this.leafBundles.map(item => ({
        id: item.id,
        minCount: item.minItemsAllowed,
      }));
      const countData = bundlesMinConditionData.map(bundle =>
        ({
          currentCount: this.selectedProducts.filter(product => product.bundleId === bundle.id).length,
          minCount: bundle.minCount,
        }),
      );

      if (countData.some(bundle => bundle.minCount > 0)) {
        return countData.map(bundle => bundle.currentCount >= bundle.minCount).reduce(
          (finalValue = true, currentValue) => finalValue && currentValue);
      } return !!this.selectedProducts.length;
    },
    isBundleUpdated() {
      return !!(this.selectedProducts.filter(product => this.preSelectedProducts.every(
        item => item.id !== product.id,
      )).length || this.preSelectedProducts.filter(product => this.selectedProducts.every(
        item => item.id !== product.id,
      )).length);
    },
  },
  created() {
    this.getRootBundleId();
  },
  mounted() {
    this.getBundleTemplateData();
  },
  methods: {
    expandFirstLeafBundle(accordion) {
      if (accordion === 0 && !this.updateBundle) {
        this.$refs.leafBundle0[0].toggleAccordion();
      }
    },

    getRootBundleId() {
      this.rootBundleId = this.globals.getUrlParam('bundleId', window.location.href);
      this.updateBundle = this.globals.getUrlParam('update', window.location.href) === 'true';
      this.cartBundleNumber = this.globals.getUrlParam('bundleNo', window.location.href);
      this.pdpProductCode = decodeURIComponent(this.globals.getUrlParam('productCode', window.location.href) || '');
    },

    updateBundleWithSelectedProducts(data) {
      const groupMap = [];
      data.entryGroupMap.entry.forEach(entry => groupMap.push(entry));
      groupMap.forEach((group) => {
        this.entryGroupMap[group.key] = group.value;
      });
      const bundleEntries = data.bundleEntries;
      bundleEntries.map((product) => {
        this.selectedProducts.push({
          id: product.productCode,
          bundleId: product.bundleTemplateId,
          entryNumber: product.entryNumber,
        });
      });
      this.leafBundles.forEach((bundle, index) => {
        if (this.selectedProducts.some(product => product.bundleId === bundle.id)) {
          this.$refs[`leafBundle${index}`][0].toggleAccordion();
        }
      });
      this.selectedProducts.forEach(product => this.preSelectedProducts.push(product));
    },
    /**
     * set Bundle information
     */
    setRootBundleData() {
      this.rootBundleData = {
        id: this.bundlesData.id,
        name: this.bundlesData.name,
        description: this.bundlesData.description,
        image: this.bundlesData.bundleImage,
      };
    },

    /**
     * set leaf - component information
     */
    /* eslint no-param-reassign: ["error", { "props": false }] */
    setLeafBundlesData() {
      this.leafBundles = [...this.bundlesData.bundleTemplates];
      this.leafBundles.forEach((item, index) => {
        item.isExpanded = false;
      });
      if (this.pdpProductCode) {
        this.leafBundles.forEach((item) => {
          if (item.starter) {
            this.selectedProducts.push({
              id: this.pdpProductCode,
              bundleId: item.id,
              isStarterBundleProduct: true,
            });
          }
        });
      }
    },

    /**
     * Fetch Bundle infomration and display bundle information
     * @return {[type]} [description]
     */
    getBundleTemplateData() {
      this.$refs.spinner.showSpinner();
      this.bundleService.getBundlesData({}, (response) => {
        if (response.data) {
          this.$refs.spinner.hideSpinner();
          this.bundlesData = response.data;
          this.setRootBundleData();
          this.setLeafBundlesData();
          if (this.updateBundle) {
            this.getBundleCartDetail();
          }
        }
      }, () => {}, this.rootBundleId);
      // this.bundlesData = this.buildBundleData;
    },

    /**
    * Handles the product add/remove on click of checkbox
    */
    handleAddToBundle(isSelected, productId, bundleId, isStarterBundleProduct) {
      let productIndex;
      const isProductSelected = this.selectedProducts.filter((item, index) => {
        if (item.id === productId) {
          productIndex = index;
        }
        return item.id === productId;
      }).length;
      const product = {
        id: productId,
        bundleId,
        isStarterBundleProduct,
      };
      if (isSelected && !isProductSelected) {
        this.selectedProducts.push(product);
      } else this.selectedProducts.splice(productIndex, 1);
    },

    /**
    * Maintains the product checkbox state in the component
    */
    handleProductState(productId, productInventory, materialStatus, maxCount, bundleId) {
      const isProductSelected = this.selectedProducts.filter(item => item.id === productId).length;
      const addedBundleProducts = this.selectedProducts.filter(item => item.bundleId === bundleId);
      const isDiscontinued = materialStatus === ProductAvailability.OBSOLETE;
      const isComingSoon = materialStatus === ProductAvailability.COMING_SOON;
      const isOutOfStock = (productInventory.stockLevelStatus !== ProductAvailability.IN_STOCK ||
        (productInventory.stockLevelStatus.code !== ProductAvailability.IN_STOCK && productInventory.stockLevelStatus.code !== undefined &&
        productInventory.stockLevelStatus.code !== ProductAvailability.LOW_STOCK));
      const inventoryAvailable = productInventory.stockLevel > 0; // TODO check for condition
      if (addedBundleProducts.length < maxCount) {
        return isComingSoon || (isDiscontinued && !inventoryAvailable) || (isOutOfStock && !inventoryAvailable) || false;
      } return !isProductSelected;
    },

    /**
     * Updates product checkbox state in the product tile component
     */
    updateProductState(productId) {
      if (this.selectedProducts.length) {
        return this.selectedProducts.filter(item => item.id === productId).length;
      } return false;
    },

    /**
     * Add bundle to cart method
     */
    addBundleToCart() {
      this.$refs.spinner.showSpinner();
      const starterBundleProducts = [];
      this.selectedProducts.filter(product =>
        product.isStarterBundleProduct === true).map((item) => {
        starterBundleProducts.push({
          productCode: item.id,
          bundleTemplateId: item.bundleId,
        });
      });
      const starterBundleEntry = starterBundleProducts[0];
      const bundleProducts = [];
      this.selectedProducts.map((item) => {
        if (item.id !== starterBundleEntry.productCode) {
          bundleProducts.push({
            productCode: item.id,
            bundleTemplateId: item.bundleId,
            // TODO can add this upon confirmation, as this needs a little code change
            // minOrderQuantity: item.product.minOrderQuantity || 1,
            // quantity: 1,
          });
        }
      });
      const requestBody = {
        rootTemplateId: this.rootBundleData.id,
        starterBundleEntry,
        bundleEntries: bundleProducts,
      };
      const requestConfig = {};
      requestConfig.data = requestBody;
      this.bundleService.addBundleToCart(
        requestConfig,
        (response) => {
          this.$refs.spinner.hideSpinner();
          if (response.data) {
            cartEventBus.$emit('call-basic-cart');
            // TODO check for flyout scenarios similar to reorder
            this.globals.navigateToUrl('cart'); // TODO check if redirection to cart after add bundle
          }
        },
        () => {
          this.$refs.spinner.hideSpinner();
          // TODO check for flyout scenario
        },
      );
    },

    /**
     * Initiates the carousel when accordion is opened
     */
    showBundleProducts(index) {
      this.leafBundles[index].isExpanded = !this.leafBundles[index].isExpanded;
      this.$forceUpdate();
    },

    getBundleCartDetail() {
      this.$refs.spinner.showSpinner();
      this.bundleService.getBundleCartDetail({}, (response) => {
        if (response.data) {
          this.$refs.spinner.hideSpinner();
          const bundleCartData = response.data;
          this.updateBundleWithSelectedProducts(bundleCartData);
        }
      }, () => {}, this.rootBundleId, this.cartBundleNumber);
    },

    updateCartBundle() {
      this.$refs.spinner.showSpinner();
      const addedProducts = this.selectedProducts.filter(product => this.preSelectedProducts.every(
        item => item.id !== product.id,
      ));
      const deletedProducts = this.preSelectedProducts.filter(product => this.selectedProducts.every(
        item => item.id !== product.id,
      ));
      const updatedProducts = [];
      addedProducts.map((product) => {
        updatedProducts.push({
          productCode: product.id,
          bundleTemplateId: product.bundleId,
          entryGroupNumber: this.entryGroupMap[product.bundleId],
        });
      });
      deletedProducts.map((product) => {
        updatedProducts.push({
          productCode: product.id,
          bundleTemplateId: product.bundleId,
          delete: true,
          entryGroupNumber: this.entryGroupMap[product.bundleId],
          entryNumber: product.entryNumber,
        });
      });
      const requestBody = {
        rootTemplateId: this.rootBundleData.id,
        bundleNo: this.cartBundleNumber,
        bundleEntries: updatedProducts,
      };

      const requestConfig = {};
      requestConfig.data = requestBody;
      this.bundleService.updateBundleInCart(requestConfig, (response) => {
        this.$refs.spinner.hideSpinner();
        if (response.data) {
          cartEventBus.$emit('call-basic-cart');
          // TODO check for flyout scenarios similar to reorder
          this.globals.navigateToUrl('cart');
        }
      }, () => {});
    },
  },
};
