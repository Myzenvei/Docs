import flyoutBannerMixin from '../../common/vx-flyout-banner/vx-flyout-banner-mixin';

export default {
  name: 'vx-banner',
  components: {},
  props: {
    isSoldToMatch: {
      type: Boolean,
      default: false,
    },
    bannerType: {
      type: String,
      default: 'error',
    },
    i18n: {
      type: Object,
    },
  },
  mixins: [flyoutBannerMixin],
  data() {
    return {};
  },
  computed: {},
  mounted() {
    if (this.isSoldToMatch) {
      this.showFlyout(
        this.bannerType,
        `${this.i18n.soldToError.errorMsg}`,
        false,
      );
    }
  },
  methods: {},
};
