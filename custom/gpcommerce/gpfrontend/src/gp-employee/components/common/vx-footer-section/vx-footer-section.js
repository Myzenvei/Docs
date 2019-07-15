import globals from '../../../../components/common/globals';

export default {
  name: 'vx-footer-section',
  components: {},
  props: {
    footerData: {
      type: Object,
      required: true,
    },
    copyrightText: {
      type: String,
    },
    isCheckoutFooter: {
      type: Boolean,
      required: true,
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

  },
};
