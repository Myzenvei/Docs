import vxModal from '../../common/vx-modal/vx-modal.vue';
import globals from '../../common/globals';
import mobileMixin from '../../common/mixins/mobile-mixin';
import viewSiteContent from '../../../locale/view-site-content-i18n';
import {
  globalEventBus,
  eventBus,
} from '../../../modules/event-bus';

export default {
  name: 'vx-product-solution',
  components: {
    vxModal,
  },
  mixins: [mobileMixin],
  props: {
    productSolutionData: {
      type: Object,
      required: true,
      default: {},
    },
  },
  data() {
    return {
      globals,
      i18n: viewSiteContent.productSolution,
      backgroundImg: '',
    };
  },
  computed: {
    backgroundStyle() {
      return !this.backgroundImg
        ? {
          'background-color': this.productSolutionData.backgroundColor
            ? this.productSolutionData.backgroundColor
            : '',
        }
        : {
          'background-image': `url(${this.backgroundImg})`,
        };
    },

    backgroundAlt() {
      return this.getResponsiveAlt(
        this.productSolutionData.backgroundImageAltTextD,
        this.productSolutionData.backgroundImageAltTextT,
        this.productSolutionData.backgroundImageAltTextM,
      );
    },

    imageTileWidth() {
      if (this.productSolutionData.noOfColumns) {
        return `width: ${100 / this.productSolutionData.noOfColumns}%`;
      }
      return 'width: 100%';
    },
  },
  mounted() {
    this.backgroundImg = this.getResponsiveImage(
      this.productSolutionData.backgroundImageD,
      this.productSolutionData.backgroundImageT,
      this.productSolutionData.backgroundImageM,
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
