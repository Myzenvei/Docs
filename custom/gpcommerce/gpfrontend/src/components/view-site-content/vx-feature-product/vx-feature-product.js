import {
  eventBus,
  globalEventBus,
} from '../../../modules/event-bus';
import videoPlayer from '../../common/vx-video-player/vx-video-player.vue';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import mobileMixin from '../../common/mixins/mobile-mixin';
import globals from '../../common/globals';
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
  },
  data() {
    return {
      globals,
      i18n: viewSiteContent.featureProduct,
      backgroundImg: '',
    };
  },
  computed: {
    backgroundStyle() {
      return !this.backgroundImg
        ? {
          'background-color': this.imgTitleData.backgroundColor,
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
    backgroundImage(tile, index) {
      const imgM = tile.imgSrcM ? tile.imgSrcM : tile.imgSrcD;
      return (this.isTablet() && !this.isMobile()) || !this.isTablet() ? {
        backgroundImage: `url(${tile.imgSrcD})`,
      } : {
        backgroundImage: `url(${imgM})`,
      };
    },
    emitScrollSignal(link) {
      globalEventBus.$emit('scroll-to', link);
    },
  },
};
