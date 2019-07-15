/* Handles leasable section on order details and order confirmation pages */
import ManageTransactionService from '../services/manage-transaction-service';
import vxSpinner from '../vx-spinner/vx-spinner.vue';
import { order } from '../mixins/vx-enums';
import globals from '../globals';

export default {
  name: 'vx-review-lease',
  components: {
    vxSpinner,
  },
  props: { },
  data() {
    return {
      manageTransactionService: new ManageTransactionService(),
      leaseAgrementData: {},
      order,
      globals,
    };
  },
  computed: {},
  mounted() {
    this.orderCode = this.globals.getUrlParam(this.order.orderId);
    this.$nextTick(() => {
      this.manageTransactionService.getLeaseAgreement({},
        this.handleLeaseAgreementResponse,
        this.handleLeaseAgreementError,
        this.orderCode,
      );
    });
    this.$refs.spinner.showSpinner();
  },
  methods: {
    handleLeaseAgreementResponse(response) {
      this.$refs.spinner.hideSpinner();
      if (response && response.data) {
        this.leaseAgrementData = response.data;
      }
    },
    handleLeaseAgreementError(error) {
      this.$refs.spinner.hideSpinner();
    },
  },
};
