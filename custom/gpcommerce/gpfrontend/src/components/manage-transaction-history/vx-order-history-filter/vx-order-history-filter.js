/**
 * Component to handle filter in mobile view
 * */
export default {
  name: 'vx-order-history-filter',
  components: {},
  props: {
    /**
     * Labels, button and caption texts
     */
    i18n: {
      type: Object,
    },
  },
  data() {
    return {};
  },
  computed: {},
  mounted() {},
  methods: {
    filterAction(filterData) {
      this.$emit('filterData', filterData);
    },
  },
};
