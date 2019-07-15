import globals from '../../../components/common/globals';
export default {
  name: 'vx-minimized-header',
  components: {},
  props: {
    /**
     * Image Name
     * @type {String}
     */
    imgUrl: {
      type: String,
      default: ''
    },
    /**
     * Image Alt Text
     * @type {String}
     */
    imgAltText: {
      type: String,
      default: ''
    },
    i18n: {
      type: Object
    }
  },
  data() {
    return {
      globals,
    };
  },
  computed: {},
  mounted() {},
  methods: {

  },
};
