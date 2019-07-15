import globals from '../../common/globals';
import mobileMixin from '../../common/mixins/mobile-mixin';

export default {
  name: 'vx-marketing-promo',
  components: {},
  mixins: [mobileMixin],
  props: {
    promotionData: {
      type: Object,
      required: true,
      default: {},
    },
  },
  data() {
    return {
      globals,
      backgroundImg: '',
      fontStyle: {
        color: this.promotionData.fontcolor,
      },
    };
  },
  computed: {
    backgroundStyle() {
      return !this.backgroundImg
        ? {
          'background-color': this.promotionData.backgroundColor,
        }
        : {
          'background-image': `url(${this.backgroundImg})`,
        };
    },

    backgroundAlt() {
      return this.getResponsiveAlt(
        this.promotionData.backgroundImageAltTextD,
        this.promotionData.backgroundImageAltTextT,
        this.promotionData.backgroundImageAltTextM,
      );
    },
  },
  mounted() {
    this.backgroundImg = this.getResponsiveImage(
      this.promotionData.backgroundImageD,
      this.promotionData.backgroundImageT,
      this.promotionData.backgroundImageM,
    );
  },
  methods: {},
};
