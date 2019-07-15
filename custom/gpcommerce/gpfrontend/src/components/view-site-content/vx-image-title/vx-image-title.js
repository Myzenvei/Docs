import globals from '../../common/globals';
import { eventBus, globalEventBus } from '../../../modules/event-bus';
import videoPlayer from '../../common/vx-video-player/vx-video-player.vue';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import mobileMixin from '../../common/mixins/mobile-mixin';
import viewSiteContent from '../../../locale/view-site-content-i18n';

export default {
  name: 'vx-image-title',
  components: {
    vxModal,
    videoPlayer,
  },
  mixins: [mobileMixin],
  props: {
    imgTitleData: {
      type: Object,
      required: true,
      default: {},
    },
    jumpToHtml: {
      type: Boolean,
      default: true,
    },
  },
  data() {
    return {
      globals,
      i18n: viewSiteContent.imageTitle,
      backgroundImg: '',
    };
  },
  computed: {
    backgroundStyle() {
      return !this.backgroundImg
        ? {
          'background-color': this.imgTitleData.backgroundColor
            ? this.imgTitleData.backgroundColor
            : '',
        }
        : {
          'background-image': `url(${this.backgroundImg})`,
        };
    },

    imageTileWidth() {
      if (this.imgTitleData.noOfColumns) {
        return `width: ${100 / this.imgTitleData.noOfColumns}%`;
      }
      return 'width: 100%';
    },

    backgroundAlt() {
      return this.getResponsiveAlt(
        this.imgTitleData.backgroundImageAltTextD,
        this.imgTitleData.backgroundImageAltTextT,
        this.imgTitleData.backgroundImageAltTextM,
      );
    },
  },
  mounted() {
    this.backgroundImg = this.getResponsiveImage(
      this.imgTitleData.backgroundImageD,
      this.imgTitleData.backgroundImageT,
      this.imgTitleData.backgroundImageM,
    );
  },
  methods: {
    openModal(videoSRC) {
      eventBus.$emit('open-player', videoSRC);
    },
    emitScrollSignal(link) {
      globalEventBus.$emit('scroll-to', link);
    },
  },
};
