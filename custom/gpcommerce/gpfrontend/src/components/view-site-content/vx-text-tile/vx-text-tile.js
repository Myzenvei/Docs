import globals from '../../common/globals';
import {
  eventBus,
  globalEventBus,
} from '../../../modules/event-bus';
import mobileMixin from '../../common/mixins/mobile-mixin';

export default {
  name: 'vx-text-tile',
  components: {},
  mixins: [mobileMixin],
  props: {
    textTileData: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      globals,
      fontStyleHeader: {
        color: this.textTileData.headerColor,
      },
      fontStyleText: {
        color: this.textTileData.textColor,
      },
      fontStyleCta: {
        color: this.textTileData.ctaColor,
      },
      alignmentClass: {
        'align-left': this.textTileData.textAlignment === 'left',
        'align-center': this.textTileData.textAlignment === 'center',
      },
      backgroundImg: '',
    };
  },
  computed: {
    backgroundStyle() {
      return !this.backgroundImg
        ? {
          'background-color': this.textTileData.backgroundColor,
        }
        : {
          'background-image': `url(${this.backgroundImg})`,
        };
    },
    textWidth() {
      return this.textTileData.imageWidth ? {
        width: `${100 - parseInt(this.textTileData.imageWidth)}%`,
      } : {
        width: '',
      };
    },

    backgroundAlt() {
      return this.getResponsiveAlt(
        this.textTileData.backgroundImageAltTextD,
        this.textTileData.backgroundImageAltTextT,
        this.textTileData.backgroundImageAltTextM,
      );
    },
  },
  mounted() {
    this.backgroundImg = this.getResponsiveImage(
      this.textTileData.backgroundImageD,
      this.textTileData.backgroundImageT,
      this.textTileData.backgroundImageM,
    );
  },
  methods: {
    openVideo(src) {
      eventBus.$emit('open-player', src);
    },
    emitScrollSignal(link) {
      globalEventBus.$emit('scroll-to', link);
    },
  },
};
