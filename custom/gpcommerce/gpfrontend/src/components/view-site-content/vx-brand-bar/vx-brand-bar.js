import globals from '../../common/globals';
import mobileMixin from '../../common/mixins/mobile-mixin';
import {
  globalEventBus,
} from '../../../modules/event-bus';

export default {
  name: 'vx-brand-bar',
  components: {},
  mixins: [mobileMixin],
  props: {
    brandBarData: {
      type: Object,
      required: true,
      default: {},
    },
  },
  data() {
    return {
      globals,
      backgroundImg: '',
      fontStyleHeader: {
        color: this.brandBarData.headerColor,
      },
      fontStyleSubHeader: {
        color: this.brandBarData.subHeaderColor,
      },
      fontStyleCta: {
        color: this.brandBarData.ctaColor,
      },
    };
  },
  computed: {
    backgroundStyle() {
      return !this.backgroundImg
        ? {
          'background-color': this.brandBarData.backgroundColor,
        }
        : {
          'background-image': `url(${this.backgroundImg})`,
        };
    },
    backgroundAlt() {
      return this.getResponsiveAlt(
        this.brandBarData.backgroundImageAltTextD,
        this.brandBarData.backgroundImageAltTextT,
        this.brandBarData.backgroundImageAltTextM,
      );
    },
  },
  mounted() {
    this.backgroundImg = this.getResponsiveImage(
      this.brandBarData.backgroundImageD,
      this.brandBarData.backgroundImageT,
      this.brandBarData.backgroundImageM,
    );
  },
  methods: {
    emitScrollSignal(link) {
      globalEventBus.$emit('scroll-to', link);
    },
  },
};
