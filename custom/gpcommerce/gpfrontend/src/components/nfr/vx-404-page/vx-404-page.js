import globals from '../../common/globals';

export default {
  name: 'vx-404-page',
  components: {},
  props: {
    i18n: {
      type: Object,
    },
  },
  data() {
    return {
      globals,
    };
  },
  computed: {

  },
  mounted() {

  },
  methods: {
    goToHomePage() {
      if (globals.isGpXpress()) {
        globals.navigateToUrl('category');
      }
      else {
        globals.navigateToUrl('home');
      }
    }
  },
}
