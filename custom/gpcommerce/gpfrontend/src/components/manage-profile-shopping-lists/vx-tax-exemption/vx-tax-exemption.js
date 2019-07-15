import vxFileUpload from '../../common/vx-file-upload/vx-file-upload.vue';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import globals from '../../common/globals';
import ManageProfileShoppingListService from '../../common/services/manage-profile-shopping-lists-service';


export default {
  name: 'vx-tax-exemption',
  components: {
    vxFileUpload,
    vxSpinner,
  },
  props: {
    i18n: Object,
  },
  data() {
    return {
      manageProfileShoppingListService: new ManageProfileShoppingListService(),
      csv: '',
      downloadSampleURL: '',
      attached: false,
      fileType: '',
      disableAttach: false,
      fileSize: 10,
      isFileSizeExceeded: false,
      globals,
    };
  },
  computed: {},
  mounted() {
    // this.csv = this.i18n.csvUpload.sampleCsvdata;
    // this.downloadSampleURL = this.i18n.csvUpload.downloadHeaders + encodeURI(this.csv);
  },
  methods: {
    fileUpload(fileList) {
      this.$emit('show-spinner');
      const formData = new FormData();
      fileList.map(file => formData.append('documents', file));
      const requestConfig = {};
      requestConfig.data = formData;
      this.manageProfileShoppingListService.updateTaxExemption(requestConfig, this.handleUpdateTaxExemption, this.handleUpdateTaxExemptionError);
    },
    handleUpdateTaxExemption(response) {
      this.$emit('update-tax-exemption-success', this.i18n.taxExemptionSuccess);
    },
    handleUpdateTaxExemptionError(error) {
      if (error) {
        this.$emit('update-tax-exemption-failure', this.i18n.taxExemptionFailure);
      }
    },
    attachmentListUpdated(fileList, currentFile) {
      this.isFileSizeExceeded = this.validateFileSize(fileList, this.getFileSize(currentFile));
    },
    validateFileSize(fileList, fileSize) {
      if (Number(fileSize) >= Number(this.fileSize)) {
        return true;
      }
      return false;
    },
    getFileSize(fileList) {
      let sizeInMB = 0,
        totalSizeInBytes = 0;
      fileList.map(file => (totalSizeInBytes += file.size));
      sizeInMB = totalSizeInBytes / Math.pow(1024, 2);
      return sizeInMB.toFixed(0);
    },
  },
};
