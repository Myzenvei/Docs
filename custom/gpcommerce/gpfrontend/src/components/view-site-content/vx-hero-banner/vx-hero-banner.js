import {
  eventBus,
  globalEventBus,
} from '../../../modules/event-bus';
import videoPlayer from '../../common/vx-video-player/vx-video-player.vue';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import mobileMixin from '../../common/mixins/mobile-mixin';
import { backgroundColor } from '../../common/mixins/vx-enums';
import globals from '../../common/globals';
import viewSiteContent from '../../../locale/view-site-content-i18n';

export default {
  name: 'vx-hero-banner',
  components: {
    vxModal,
    videoPlayer,
  },
  mixins: [mobileMixin],
  props: {
    heroBannerData: {
      type: Object,
      required: true,
    },
    isCategoriesBanner: {
      type: Boolean,
      required: false,
      default: false,
    },
  },
  data() {
    return {
      backgroundColor,
      globals,
      i18n: viewSiteContent.heroBanner,
      backgroundImg: '',
    };
  },
  computed: {
    backgroundStyle() {
      return !this.heroBannerData.subHeadBgColor || this.heroBannerData.subHeadBgColor === ''
        ? {
          'background-color': this.backgroundColor.transparent,
        }
        : {
          'background-color': this.heroBannerData.subHeadBgColor,
        };
    },
    backgroundImage() {
      return { 'background-image': `url(${this.backgroundImg})` };
    },

    backgroundAlt() {
      return this.getResponsiveAlt(
        this.heroBannerData.backgroundImageAltTextD,
        this.heroBannerData.backgroundImageAltTextT,
        this.heroBannerData.backgroundImageAltTextM,
      );
    },
  },
  created() {},
  mounted() {
    this.backgroundImg = this.getResponsiveImage(
      this.heroBannerData.backgroundImageD,
      this.heroBannerData.backgroundImageT,
      this.heroBannerData.backgroundImageM,
    );
    if (this.isCategoriesBanner) {
      this.heroBannerData = this.globals.catergoryBannerData;
      this.backgroundImg = this.getResponsiveImage(this.heroBannerData.backgroundImageD);
    }
  },
  methods: {
    openModal() {
      const videoSRC = this.heroBannerData.videoSrc;
      eventBus.$emit('open-player', videoSRC);
    },
    emitScrollSignal(link) {
      globalEventBus.$emit('scroll-to', link);
    },
  },
};
