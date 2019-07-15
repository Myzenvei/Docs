import globals from '../../common/globals';
import {
  leftNavigation,
} from '../../common/mixins/vx-enums';

export default {
  name: 'vx-left-nav',
  components: {},
  mixins: [],
  props: {
    /**
     * @model details
     */
    leftNavData: {
      type: Array,
      required: true,
    },
    i18n: Object,
    showCompanySection: Boolean,
  },
  data() {
    return {
      navigationData: this.leftNavData,
      currentUrlPath: window.location.pathname.substring(window.location.pathname.indexOf('/', 1)),
      baseURL: globals.getNavBaseUrl(),
      userInfo: globals.userInfo.userRoles,
      leftNavigation,
      globals
    };
  },
  computed: {},
  mounted() {},
  methods: {},
};
