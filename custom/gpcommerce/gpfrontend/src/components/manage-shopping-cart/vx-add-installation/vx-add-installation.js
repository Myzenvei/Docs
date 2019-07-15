import globals from '../../common/globals';
import {
  cartEventBus
} from '../../../modules/event-bus';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import AnalyticsService from '../../common/services/analytics-service';
import YMarketingService from '../../common/services/yMarketing-service';
import ManageShoppingCartService from '../../common/services/manage-shopping-cart-service';


export default {
  name: 'vx-add-installation',
  components: {
    vxSpinner,
  },
  props: {
    /**
     * Details of the installation product and installable product
     **/
    installDetails: Object,
    /**
     * Labels, button and caption texts
     */
    i18n: Object,
  },
  data() {
    return {
      globals,
      installationDetails: Object,
      analyticsService: new AnalyticsService(),
      yMarketing: new YMarketingService(),
      manageShoppingCartService: new ManageShoppingCartService(),
    };
  },
  computed: {},
  mounted() {
    this.callInstallDetails();
  },
  methods: {
    /**
     * This function adds the installation product to cart
     */
    addToCart() {
      const requestConfig = {};
      requestConfig.data = {
        quantity: this.installDetails.parentProductQuantity,
        product: {
          code: this.installDetails.code,
        },
        additionalAttributes: {
          entry: [{
            key: 'installedEntry',
            value: this.installDetails.parentProductEntry,
          }],
        },
      };
      this.manageShoppingCartService.addInstallation(requestConfig, this.handleAddInstallationResponse, this.handleAddInstallationError);
      this.$refs.spinner.showSpinner();
      // sending the data to Google Analytics on Add to Cart Button click
      if (typeof dataLayer !== 'undefined') {
        const analyticsObject = {
          code: this.installDetails.parentProductCode,
          name: this.installDetails.parentProductName,
          quantity: this.installDetails.parentProductQuantity,
        };
        this.analyticsService.trackAddToCart(analyticsObject);
      }
      // sending data to yMarketing on add to cart button click
      const cartData = {
        cartCode: this.globals.getCartGuid(),
        productCode: this.installDetails.parentProductCode,
        productName: this.installDetails.parentProductName,
        productPrice: this.installDetails.parentProductPrice,
      };
      this.yMarketing.trackAddToCart(this.installDetails.parentProductCode, this.installDetails.parentProductQuantity, cartData);
    },
    /**
     * This function handles the response of adding the installation product to cart
     */
    handleAddInstallationResponse(response) {
      this.$refs.spinner.hideSpinner();
      this.callInstallUpdate();
    },
    /**
     * This function handles the error of adding the installation product to cart
     */
    handleAddInstallationError(error) {
      this.$refs.spinner.hideSpinner();
    },
    /**
     * This function gets the details for the add installation modal
     */
    callInstallDetails() {
      const requestConfig = {};
      this.manageShoppingCartService.installationDetails(requestConfig, this.handleInstallationDetailsResponse, this.handleInstallationDetailsError, this.installDetails.code);
      this.$refs.spinner.showSpinner();
    },
    /**
     * This function handles the response for the call to get details for the add installation modal
     */
    handleInstallationDetailsResponse(response) {
      this.installationDetails = response.data;
      this.$refs.spinner.hideSpinner();
    },
    /**
     * This function handles the error for the call to get details for the add installation modal
     */
    handleInstallationDetailsError() {
      this.$refs.spinner.hideSpinner();
    },
    /**
     * This function informs that installation has been selected for the installable product
     */
    callInstallUpdate() {
      const requestConfig = {};
      requestConfig.data = {
        quantity: this.installDetails.parentProductQuantity,
        additionalAttributes: {
          entry: [{
            key: 'installed',
            value: 'true',
          }],
        },
      };
      this.manageShoppingCartService.updateInstallation(requestConfig, this.handleUpdateInstallationResponse, this.handleUpdateInstallationError, this.installDetails.parentProductEntry);
      this.$refs.spinner.showSpinner();
    },
    /**
     * This function handles the response of the call that informs installation has been selected for the installable product
     */
    handleUpdateInstallationResponse(response) {
      cartEventBus.$emit('cart-update');
      this.$refs.spinner.hideSpinner();
      this.$emit('add-installation-success');
    },
    /**
     * This function handles the error of the call that informs installation has been selected for the installable product
     */
    handleUpdateInstallationError(error) {
      this.$refs.spinner.hideSpinner();
      this.$emit('add-installation-error');
    },
  },
};
