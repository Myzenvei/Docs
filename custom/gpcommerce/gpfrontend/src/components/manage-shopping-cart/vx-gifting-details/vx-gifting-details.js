import globals from '../../common/globals';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import ManageShoppingCartService from '../../common/services/manage-shopping-cart-service';


export default {
  name: 'vx-gifting-details',
  components: {
    vxSpinner,
  },
  props: {
    /**
     * data of gift product
     */
    giftableDetails: Object,
    /**
     * text coming from property file
     */
    i18n: Object,
  },
  data() {
    return {
      globals,
      giftDetails: Object,
      manageShoppingCartService: new ManageShoppingCartService(),
    };
  },
  computed: {},
  mounted() {
    this.callGiftDetails();
  },
  methods: {
    /**
     * This function gets gifting details
     */
    callGiftDetails() {
      this.manageShoppingCartService.giftDetails({}, this.handleGiftDetailsResponse, this.handleGiftDetailsError, this.giftableDetails.code);
      this.$refs.spinner.showSpinner();
    },
    /**
     * This function handles the response of gifting details call
     */
    handleGiftDetailsResponse(response) {
      this.giftDetails = response.data;
      this.$refs.spinner.hideSpinner();
    },
    /**
     * This function handles the error of gifting details call
     */
    handleGiftDetailsError() {
      this.$refs.spinner.hideSpinner();
    },
  },
};
