import globals from '../../common/globals';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import ManageShoppingCartService from '../../common/services/manage-shopping-cart-service';

export default {
  name: 'vx-installation-details',
  components: {
    vxSpinner,
  },
  props: {
    /**
     * data of installation product
     */
    installDetails: Object,
  },
  data() {
    return {
      globals,
      installationDetails: Object,
      manageShoppingCartService: new ManageShoppingCartService(),
    };
  },
  computed: {},
  mounted() {
    this.callInstallDetails();
  },
  methods: {
    /**
     * This function gets installation details
     */
    callInstallDetails() {
      this.manageShoppingCartService.installationDetails({}, this.handleInstallationDetailsResponse, this.handleInstallationDetailsError, this.installDetails.code);
      this.$refs.spinner.showSpinner();
    },
    /**
     * This function handles the response of installation details call
     */
    handleInstallationDetailsResponse(response) {
      this.installationDetails = response.data;
      this.$refs.spinner.hideSpinner();
    },
    /**
     * This function handles the error of installation details call
     */
    handleInstallationDetailsError() {
      this.$refs.spinner.hideSpinner();
    },
  },
};
