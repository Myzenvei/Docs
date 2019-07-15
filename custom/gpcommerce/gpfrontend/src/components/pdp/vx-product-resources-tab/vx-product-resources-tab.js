import globals from '../../common/globals';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import vxShareResource from '../vx-share-resource/vx-share-resource.vue';
import flyoutBannerMixin from '../../common/vx-flyout-banner/vx-flyout-banner-mixin';

export default {
  name: 'product-resources-tab',
  mixins: [flyoutBannerMixin],
  components: {
    vxModal,
    vxShareResource,
  },
  props: {
    /**
     * Resources data of the product
     */
    productResourcesTabData: {
      type: Object,
      default: {},
    },
    /**
     * Copy text coming from properties files
     */
    i18n: {
      type: Object,
    },
  },
  data() {
    return {
      globals,
      showProductInfoTab: true,
      showProductResTab: false,
      productInfoDocuments: false,
      productInfoVideos: false,
      productResourcesDocuments: false,
      productResourcesVideos: false,
      shareResource: {},
    };
  },
  computed: {},
  mounted() {
    if (
      this.productResourcesTabData.dataSheets &&
      this.productResourcesTabData.dataSheets.dataSheet.length
    ) {
      for (let i = 0; i < this.productResourcesTabData.dataSheets.dataSheet.length; i++) {
        if (
          this.productResourcesTabData.dataSheets.dataSheet[i].mimeType.includes('Video') ||
          this.productResourcesTabData.dataSheets.dataSheet[i].mimeType.includes('Audio')
        ) {
          this.productInfoVideos = true;
        } else {
          this.productInfoDocuments = true;
        }
      }
    }

    if (
      this.productResourcesTabData.productResourceVideos &&
      this.productResourcesTabData.productResourceVideos.video.length
    ) {
      for (let i = 0; i < this.productResourcesTabData.productResourceVideos.video.length; i++) {
        if (
          this.productResourcesTabData.productResourceVideos.video[i].mimeType.includes('Video') ||
          this.productResourcesTabData.productResourceVideos.video[i].mimeType.includes('Audio')
        ) {
          this.productResourcesVideos = true;
        } else {
          this.productResourcesDocuments = true;
        }
      }
    }
  },
  methods: {
    /**
     * This function toggles Tab View based on the type
     * @param  {String} type type of Tab
     */
    toggleTabViews(type) {
      if (type === 'productinfo') {
        this.showProductInfoTab = true;
        this.showProductResTab = false;
      } else {
        this.showProductInfoTab = false;
        this.showProductResTab = true;
      }
    },
    /**
     * This function is called on click of share resource button
     */
    handleShareResource(resource) {
      this.shareResource = resource;
      this.$refs.shareResourceModal.open();
    },
    /**
     * This function is called on success emit of share resource modal
     */
    onShareResourceSuccess() {
      this.shareResource = {};
      this.$refs.shareResourceModal.close();
      this.showFlyout('success', this.i18n.shareModal.shareResourceSuccessMsg, true);
    },
  },
};
