export default {
  name: 'vx-accordion',
  components: {},
  props: {
    isSlotBased: {
      type: Boolean,
    },
    accordionData: {
      type: Object,
    },
    opened: {
      type: Boolean,
      default: false,
    },
    i18n: {
      type: Object,
    },
    preserveAccordionState: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      isOpened: this.opened,
      accordionIdentifier: Math.random().toString(36).replace(/[^a-z]+/g, '') //to generate the random string which serves as unique id
    };
  },
  computed: {

  },
  mounted() {
    this.$emit('accordionMounted', true);
  },
  methods: {
    toggleAccordion(event) {
      this.isOpened = !this.isOpened;
      this.$emit('accordionStatus', this.isOpened);
    },

  },
};
