import { eventBus } from '../../../modules/event-bus';
import mobileMixin from '../../common/mixins/mobile-mixin';
import viewSiteContent from '../../../locale/view-site-content-i18n';

export default {
  name: 'vx-image-tile',
  components: {},
  props: {
    imageTileData: {
      type: Object,
      required: true,
    },
  },
  mixins: [mobileMixin],
  data() {
    return {
      i18n: viewSiteContent.imageTile,
      backgroundImg: '',
    };
  },
  computed: {
    backgroundImage() {
      return { 'background-image': `url(${this.backgroundImg})` };
    },

    backgroundAlt() {
      return this.getResponsiveAlt(
        this.imageTileData.imageSrcAltTextD,
        this.imageTileData.imageSrcAltTextT,
        this.imageTileData.imageSrcAltTextM,
      );
    },
  },
  mounted() {
    this.backgroundImg = this.getResponsiveImage(
      this.imageTileData.imageSrcD,
      this.imageTileData.imageSrcT,
      this.imageTileData.imageSrcM,
    );
  },
  methods: {
    openVideo(src) {
      eventBus.$emit('open-player', src);
    },
  },
};
