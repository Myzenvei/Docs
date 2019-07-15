export default {
  name: 'vx-remove-installation',
  components: {},
  props: {
    i18n: Object,
  },
  data() {
    return {};
  },
  computed: {},
  mounted() {},
  methods: {
    removeInstallation() {
      this.$emit('remove-installation-success');
    },
  },
};
