import globals from '../../common/globals';

export default {
  name: 'vx-curated-list',
  components: {},
  props: {
    curatedList: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      globals,
    };
  },
  computed: {},
  mounted() {},
  methods: {},
};
