/* Handles file upload for all extensions */

import globals from '../globals';

export default {
  name: 'vx-file-upload',
  components: {},
  props: {
    /**
     * Labels, button and caption texts
     */
    i18n: Object,
    /**
     * file type
     */
    fileType: String,
    /**
     * Disable attach feature
     */
    disableAttach: Boolean,
    /**
     * Error flag
     */
    isError: {
      default: false,
      type: Boolean,
    },
    /**
     * Multiple File Upload flag
     */
    multipleFileUpload: {
      default: false,
      type: Boolean,
    },
    /**
     * template error flag
     */
    // commenting as this is part of quick order
    // templateError: {
    //   default: false,
    //   type: String,
    // },
    /**
     * flag for duplicate file anme
     */
    duplicateNameError: {
      default: false,
      type: Boolean,
    },
    /**
     * File size unit
     */
    fileSizeUnit: {
      default: '',
      type: String,
      required: false,
    },
    /**
     * falg for contact us form
     */
    isContactUs: {
      default: false,
      type: Boolean,
    },
    fileSize: {
      default: '',
      type: String,
      required: false,
    },
  },
  watch: {
    // commenting as this is part of quick order
    // templateError(newVal, oldVal) {
    //   // watch it
    //   this.errorMsg = this.templateError;
    //   this.isError = true;
    // },
    isError(newVal, oldVal) {
      if (this.isError) {
        this.errorMsg = this.i18n.attachmentSizeLimitError;
      }
    },
  },
  data() {
    return {
      globals,
      files: [],
      typeError: false,
      errorMsg: '',
      currentFile: [],
    };
  },
  mounted() {},
  methods: {
    /**
     * Gets called when file is attached
     *
     * @param {Array} fileList List of files attached
     */
    handleFileUpload(fileList) {
      this.currentFile = [];
      const fileExtension = fileList[0].name.substring(fileList[0].name.lastIndexOf('.') + 1);
      if (!this.fileType) {
        this.typeError = false;
        this.files.push(...fileList);
        this.currentFile.push(...fileList);
        /**
         * Delete attachment Event.
         *
         * @event attachments-updated
         * @type {Array}
         */
        this.$emit('attachments-updated', this.files, this.currentFile);
      } else if (this.fileType.indexOf(fileExtension.toLowerCase()) > 0) {
        this.typeError = false;
        this.files.push(...fileList);
        /**
         * Delete attachment Event.
         *
         * @event attachments-updated
         * @type {Array}
         */
        this.$emit('attachments-updated', this.files, this.currentFile);
      } else {
        this.errorMsg = this.i18n.attachmentTypeError;
        this.typeError = true;
      }
    },
    /**
     * Gets called when file is uploaded
     */
    uploadFile(event) {
      /**
       * File Upload Event.
       *
       * @event file-upload
       * @type {Array}
       */
      this.$emit('file-upload', this.files);
    },
    /**
     * Gets called when file is deleted
     *
     * @param {Number} index index of the deleted file
     */
    deleteAttachment(index, event) {
      this.files.splice(index, 1);
      this.errorMsg = '';
      this.isError = false;
      /**
       * Delete attachment Event.
       *
       * @event attachments-updated
       * @type {Array}
       */
      this.$emit('attachments-updated', this.files);
    },
    /**
     * This function trunactec file name to 17 characters for contact us file upload
     */
    getFileName(filename) {
      const name = filename.slice(0, filename.lastIndexOf('.'));
      if (this.isContactUs && name.length > 17) {
        const extension = filename.substr(filename.lastIndexOf('.'), filename.length);
        const truncatedName = name.slice(0, 17);
        const newFileName = truncatedName + extension;
        return newFileName;
      }
      return filename;
    },
  },
};
