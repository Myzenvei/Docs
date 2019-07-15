/**
 * This component handles the csv upload functionality
 */
import globals from '../../common/globals';
import vxFileUpload from '../../common/vx-file-upload/vx-file-upload.vue';
import { fileDownload } from '../../common/mixins/vx-enums';

export default {
  name: 'vx-csv-upload',
  components: {
    vxFileUpload,
  },
  props: {
    /**
     * Labels, button and caption texts
     */
    i18n: Object,
    /**
     * template error flag
     */
    templateError: {
      default: '',
      type: String,
    },
  },
  data() {
    return {
      globals,
      csv: '',
      downloadSampleURL: '',
      attached: false,
      fileType: '.csv',
      disableAttach: false,
    };
  },
  computed: {
    templateFormatError() {
      return this.templateError;
    },
  },
  mounted() {

  },
  methods: {
    fileDownload() {
      this.csv = this.i18n.csvUpload.sampleCsvdata;
      if (navigator.msSaveBlob) { // IE 10+
        var exportedFilename = fileDownload.csv;
        var blob = new Blob([this.csv], {
          type: 'text/csv;charset=utf-8;'
        });
        navigator.msSaveBlob(blob, exportedFilename);
      } else {

        this.downloadSampleURL = `${this.i18n.csvUpload.downloadHeaders},${encodeURIComponent(this.csv)}`;
        var link = document.createElement("a");
        link.download = fileDownload.csv;
        link.href = this.downloadSampleURL;
        document.body.appendChild(link);
        link.click();
        link.remove();
      }
    },
    fileUpload(fileList) {
      this.templateError = '';
      this.$emit('upload', fileList);
    },
    attachmentListUpdated(fileList) {
      if (fileList.length) {
        this.disableAttach = true;
      } else {
        this.disableAttach = false;
      }
    },
  },
};
