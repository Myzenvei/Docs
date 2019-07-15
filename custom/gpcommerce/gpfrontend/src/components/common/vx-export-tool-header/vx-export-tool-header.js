export default {
  name: 'vx-export-tool-header',
  components: {},
  props: {
    headerTitle: {
      type: String,
      required: true,
    },
  },
  data() {
    return {

    };
  },
  computed: {
    heading() {
      return this.headerTitle;
    },
  },
  mounted() {

  },
  methods: {

  },
};
