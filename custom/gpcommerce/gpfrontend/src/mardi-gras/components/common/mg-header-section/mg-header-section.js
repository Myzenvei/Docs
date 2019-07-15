import mobileMixin from '../../../../components/common/mixins/mobile-mixin';
import globals from '../../../../components/common/globals';

export default {
  name: 'mg-header-section',
  mixins: [mobileMixin],
  components: {},
  props: {
    navData: {
      type: Object,
      required: true,
    },
    navSocialLinks: {
      type: Object,
      required: true,
    },
    activeLink: {
      type: String,
    },
    i18n: {
      type: Object,
    },
    isHomePage: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      globals,
      isHamburgerOpen: false,
    };
  },
  computed: {},
  mounted() {},
  methods: {
    toggleMenu() {
      this.isHamburgerOpen = !this.isHamburgerOpen;
    },
  },
};
