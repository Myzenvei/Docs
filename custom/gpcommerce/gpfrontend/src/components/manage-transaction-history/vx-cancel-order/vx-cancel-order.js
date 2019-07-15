/**
 * Component to handle cancel order functionality
 * */
import ManageTransactionService from '../../common/services/manage-transaction-service';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import globals from '../../common/globals';

export default {
  name: 'vx-cancel-order',
  components: {
    vxSpinner,
  },
  props: {
    /**
     * Labels, button and caption texts
     */
    i18n: Object,
    /**
     * Order Id
     */
    orderId: String,
  },
  data() {
    return {
      globals,
      manageTransactionService: new ManageTransactionService(),
    };
  },
  computed: {},
  mounted() {},
  methods: {
    keepOrder(event) {
      this.$emit('keep-order');
    },
    cancelOrder() {
      const requestConfig = {};
      requestConfig.data = this.reqBody;
      this.manageTransactionService.cancelOrder(
        requestConfig,
        this.handleCancelOrderResponse,
        this.handleCancelOrderError,
        this.orderId,
      );
      this.$refs.spinner.showSpinner();
    },
    handleCancelOrderResponse(response) {
      this.$refs.spinner.hideSpinner();
      this.$emit('cancel-order-success');
    },
    handleCancelOrderError(error) {
      this.$refs.spinner.hideSpinner();
      this.$emit('cancel-order-error');
    },
  },
};
