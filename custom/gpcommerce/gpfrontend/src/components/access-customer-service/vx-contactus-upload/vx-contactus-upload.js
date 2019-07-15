import vxFileUpload from '../../common/vx-file-upload/vx-file-upload.vue';


export default {
  name: 'vx-contactus-upload',
  components: {
    vxFileUpload,
  },
  props: {
    /**
     * Labels, button and caption texts
     */
    i18n: {
      type: Object,
      default: {},
    },
    /**
     * files attached
     */
    attachments: {
      type: Array,
      default: [],
    },
  },
  data() {
    return {
      isFileSizeExceeded: false,
      fileSizeLimit: 10,
      isFileNameDuplicate: false,
      filesAttached: [],
      fileSizeUnit: '',
      fileType: '.jpg, .jpeg, .gif, .png, .txt, .doc, .docx, .pdf, .ppt,.pptx, .xls, .xlsx, .csv',
      disableAttach: false,
      fileSize: '',
    };
  },

  mounted() {

  },
  methods: {
    /**
     * This function emits the file uploaded
     */
    fileUpload(fileList) {
      this.$emit('upload', fileList);
    },
    /**
     * This function calls functions to check if file name is duplicate, file size, and disables attachement of more than one file
     */
    attachmentListUpdated(fileList) {
      this.isFileSizeExceeded = this.validateFileSize(fileList, this.getFileSize(fileList));
      if (this.isFileNameDuplicate) {
        this.isFileNameDuplicate = false;
      }
      if (this.attachments.length && fileList.length) {
        this.validateFilename(this.attachments, fileList);
      }
      if (fileList.length) {
        this.disableAttach = true;
      } else {
        this.disableAttach = false;
      }
    },
    /**
     * This function checks file size is more than 10 MB
     */
    validateFileSize(fileList, fileSize) {
      if (Number(fileSize) >= Number(this.fileSizeLimit)) {
        return true;
      }
      return false;
    },
    /**
     * This function gets file size
     */
    getFileSize(fileList) {
      let sizeInMB = 0,
        totalSizeInBytes = 0,
        sizeInKB = 0;
      fileList.map(file => (totalSizeInBytes += file.size));
      sizeInKB = totalSizeInBytes / Math.pow(1024, 1);
      sizeInMB = totalSizeInBytes / Math.pow(1024, 2);
      if (sizeInMB < 1) {
        this.fileSizeUnit = this.i18n.fileSizeUnitKB;
        this.fileSize = sizeInKB.toFixed(2);
      } else {
        this.fileSizeUnit = this.i18n.fileSizeUnitMB;
        this.fileSize = sizeInMB.toFixed(2);
      }
      return sizeInMB.toFixed(0);
    },
    /**
     * This function checks is file name is duplicate
     */
    validateFilename(attachments, fileList) {
      attachments.map((item) => {
        if (item.name === fileList[0].name) {
          this.isFileNameDuplicate = true;
        }
      });
    },
  },
};
