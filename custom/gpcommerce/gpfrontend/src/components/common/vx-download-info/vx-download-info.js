import vxPdfDownload from '../../common/vx-pdf-download/vx-pdf-download.vue';
import vxImageDownload from '../../common/vx-image-download/vx-image-download.vue';

export default {
  name: 'vx-download-info',
  components: {
    vxPdfDownload,
    vxImageDownload,
  },
  props: {
    i18n: Object,
    /**
     * Indicates whether it is PDP page
     */
    isPdp: {
      type: Boolean,
      default: false,
    },
    colorCodes: {
      type: Object,
      default: {},
    },
    productEntries: {
      type: Object,
      default: {},
    },
    isImageDownloadEnabled: {
      type: Boolean,
      default: false,
    },
    isPdfDownloadEnabled: {
      type: Boolean,
      default: false,
    },
    certificationsName: {
      type: String,
    }
  },
  data() {
    return {

    };
  },
  mounted() {

  },
  methods: {
    onImageDownloadInit(imageInfo) {
      this.$emit('onImageDownloadInit', imageInfo);
    },
    onPdfDownloadInit(pdfInfo) {
      this.$emit('onPdfDownloadInit', pdfInfo);
    }
  }
}
