import backgroundImageMixin from '../../common/mixins/backgroundImage-mixin.js';
import mobileMixin from '../../common/mixins/mobile-mixin';
import globals from '../../common/globals';
import {
  globalEventBus,
} from '../../../modules/event-bus';

export default {
  name: 'vx-side-by-side',
  components: {},
  mixins: [backgroundImageMixin, mobileMixin],
  props: {
    sbsData: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      globals,
    };
  },
  computed: {
    backgroundAlt() {
      return this.getResponsiveAlt(this.sbsData.sbsGlobalBgImgAltTextD, this.sbsData.sbsGlobalBgImgAltTextT, this.sbsData.sbsGlobalBgImgAltTextM);
    },
  },
  mounted() {
    // this.backgroundImage();
  },
  methods: {
    backgroundImage(data, index) {
      // this.sbsData.sbsImage = this.imageTileData.imageSrcM ? this.imageTileData.imageSrcM : this.imageTileData.imageSrcD;
      return (this.isTablet() && !this.isMobile()) || !this.isTablet() ?
        {
          backgroundImage: `url(${data.sbsImage})`,
        } :
        {
          backgroundImage: `url(${data.sbsImage})`,
        };
    },
    getBackgroundAlt(data, index) {
      return data.sbsImageAltText;
    },
    emitScrollSignal(link) {
      globalEventBus.$emit('scroll-to', link);
    },
  },
};
