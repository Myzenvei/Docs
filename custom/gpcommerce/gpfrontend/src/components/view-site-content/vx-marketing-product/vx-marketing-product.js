import globals from '../../common/globals';
import {
  globalEventBus,
} from '../../../modules/event-bus';

export default {
  name: 'vx-marketing-product',
  components: {},
  props: {
    tabBody: {
      type: Array,
      required: true,
    },
    tabBodyTitle: {
      type: String,
      required: true,
    },
    broucherLink: {
      type: String,
    },
    requestTrialLink: {
      type: String,
    },
    brochureText: {
      type: String,
    },
    requestTrialText: {
      type: String,
    },
    isBrochureLinkExternal: {
      type: Boolean,
    },
    isRequestTrialLinkExternal: {
      type: Boolean,
    },
  },
  data() {
    return {
      tabData: {},
      descriptiveData: {
        descriptiveSrc: '',
        descriptiveText: '',
        header: '',
      },
      tileCtaData: {
        ctaText: '',
        ctaLink: '',
        isExternalLink: '',
        jumpToHtml: false,
      },
      tileText: '',
      globals,
    };
  },
  computed: {},
  mounted() {
    this.tabData = this.tabBody;
    this.loadDescriptiveImage(this.tabBody[0]);
  },
  methods: {
    loadDescriptiveImage(prd) {
      if (prd.imgSrcD) {
        this.descriptiveData.descriptiveSrc = prd.imgSrcD || `${this.globals.assetsPath}images/no_image.svg`;
      } else {
        this.descriptiveData.descriptiveSrc = `${this.globals.assetsPath}images/no_image.svg`;
      }
      this.descriptiveData.descriptiveText = prd.imgSrcAltTextD;
      this.descriptiveData.header = prd.header;
      this.tileText = prd.tileText;
      this.tileCtaData.ctaText = prd.ctaText;
      this.tileCtaData.ctaLink = prd.ctaLink;
      this.tileCtaData.isExternalLink = prd.isExternalLink;
      this.tileCtaData.jumpToHtml = prd.jumpToHtml;
    },
    emitScrollSignal(link) {
      globalEventBus.$emit('scroll-to', link);
    },

  },
};
