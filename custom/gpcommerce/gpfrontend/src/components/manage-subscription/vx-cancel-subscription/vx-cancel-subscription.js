import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import globals from '../../common/globals';
import ManageSubscriptionService from '../../common/services/manage-subscription-service';

export default {
  name: 'vx-cancel-subscription',
  components: {
    vxSpinner,
  },
  props: {
    /**
     * Labels, button and caption texts
     */
    i18n: {
      type: Object,
    },
    /**
     * SubscriptionID
     */
    subscriptionId: {
      type: String,
      default: '',
    },
  },
  data() {
    return {
      globals,
      manageSubscriptionService: new ManageSubscriptionService(),

    };
  },
  computed: {},
  mounted() {},
  methods: {
    keepSubscription(event) {
      this.$emit('keep-subscription');
    },
    cancelSubscription() {
      const requestObj = {
        code: this.subscriptionId.code,
      };
      const requestConfig = {};
      requestConfig.data = requestObj;
      this.$refs.spinner.showSpinner();
      this.manageSubscriptionService.cancelSubscription(
        requestConfig,
        this.handleCancelSubscriptionResponse,
        this.handleCancelSubscriptionError,
      );
    },
    handleCancelSubscriptionResponse(response) {
      this.$refs.spinner.hideSpinner();
      this.$emit('cancel-Subscription-success');
    },
    handleCancelSubscriptionError(error) {
      this.$refs.spinner.hideSpinner();
      this.$emit('cancel-Subscription-error');
    },
  },
};
