import globals from '../../common/globals';
import ManageProfileShoppingListService from '../../common/services/manage-profile-shopping-lists-service';
import vxRadioButtonGroup from '../../common/vx-radio-button-group/vx-radio-button-group.vue';
import detectDeviceMixin from '../../common/mixins/detect-device-mixin';


export default {
  name: 'vx-image-download',
  components: {
    vxRadioButtonGroup,
  },
  mixins: [detectDeviceMixin],
  props: {
    i18n: {
      type: Object,
    },
  },
  data() {
    return {
      globals,
      manageProfileShoppingListService: new ManageProfileShoppingListService(),
      formatOptions: [{
          label: this.i18n.imageDownloadJpg,
          value: this.i18n.formatJPG,
        },
        {
          label: this.i18n.imageDownloadPng,
          value: this.i18n.formatPNG,
        },
        {
          label: this.i18n.imageDownloadGif,
          value: this.i18n.formatGIF,
        },
      ],
      sizeOptions: [{
          label: this.i18n.imageDownloadOriginalSize,
          value: this.i18n.sizeDefault,
        },
        {
          label: this.i18n.imageDownloadLarge,
          value: this.i18n.size1200,
        },
        {
          label: this.i18n.imageDownloadMedium,
          value: this.i18n.size515,
        },
        {
          label: this.i18n.imageDownloadSmall,
          value: this.i18n.size300,
        },
        {
          label: this.i18n.imageDownloadThumbnail,
          value: this.i18n.size96,
        },
      ],
      imageInfo: {
        format: this.i18n.formatJPG,
        size: this.i18n.sizeDefault,
        allImages: false,
      },
    };
  },
  computed: {},
  mounted() {
    this.$refs.format.setSelectedByValue(this.formatOptions[0].value);
    this.$refs.size.setSelectedByValue(this.sizeOptions[0].value);
  },
  methods: {
    downloadImagesZip() {
      this.$emit('onImageDownloadInit', this.imageInfo);
    },
    downloadAllImageChange(event) {
      this.imageInfo.allImages = event.target.checked;
    },
  },
};
