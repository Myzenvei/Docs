import CommonService from '../../common/services/common-service';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import globals from '../../common/globals';

export default {
  name: 'vx-terms-condition',
  components: {
    vxSpinner,
  },
  props: [],
  data() {
    return {
      commonService: new CommonService(),
      termsConditionData: '',
      globals,
    };
  },
  computed: {

  },
  mounted() {
    this.$refs.spinner.showSpinner();
    this.commonService.getTermsAndCondition({}, this.handleTermsConditionResponse, this.handleTermsConditionError);
  },
  methods: {
    handleTermsConditionResponse(response) {
      this.$refs.spinner.hideSpinner();
      const status = response.status;
      const data = response.data;
      if (status) {
        this.termsConditionData = data;
      }
    },
    handleTermsConditionError(error) {
      this.$refs.spinner.hideSpinner();
    }
  },
};
