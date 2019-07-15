import globals from '../globals';
import CommonService from '../services/common-service';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';

export default {
  name: 'vx-delete-address',
  components: {
    vxSpinner,
  },
  props: ['address', 'i18n'],
  data() {
    return {
      globals,
      commonService: new CommonService(),
    };
  },
  computed: {},
  mounted() {},
  methods: {
    deleteAddress(event) {
      // Service call when delete is clicked
      this.commonService.deleteAddress({}, this.handleDeleteAddressResponse, this.handleDeleteAddressError, this.address.id);
      this.$refs.deleteSpinner.showSpinner();
    },
    handleDeleteAddressResponse(response) {
      this.$refs.deleteSpinner.hideSpinner();
      this.$emit('onAddressDeleted', response);
    },
    handleDeleteAddressError(error) {
      if (error) {
        this.$refs.spinner.hideSpinner();
      }
    },
  },
};
