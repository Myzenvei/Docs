import detectDeviceMixin from '../../common/mixins/detect-device-mixin';

export default {
  name: 'vx-add-custom-attribute',
  components: {},
  mixins: [detectDeviceMixin],
  props: {
    /**
   * Labels, button and caption texts
   */
    i18n: {
      type: Object,
    },
    attributeList: {
      type: Array,
      default: [],
    }
  },
  data() {
    return {
      attributeInfo: {
        attributeOne: "",
        attributeTwo: "",
        attributeThree: "",
      },
      showPlaceholder: true,
    }
  },
  computed: {
    isAccordionValuesChanged() {
      if (this.attributeInfo.attributeOne.length !== 0 || this.attributeInfo.attributeTwo.length !== 0 || this.attributeInfo.attributeThree.length !== 0)
        return this.attributeInfo.attributeOne !== this.attributeList[0].value || this.attributeInfo.attributeTwo !== this.attributeList[1].value || this.attributeInfo.attributeThree !== this.attributeList[2].value;
      return false;
    }
  },
  watch: {
  },
  mounted() {
    this.prePopulateAttributes();
  },
  methods: {
    prePopulateAttributes() {
      if (this.attributeList) {
        if (this.attributeList[0].value) {
          this.attributeInfo.attributeOne = this.attributeList[0].value;
        }
        if (this.attributeList[1].value) {
          this.attributeInfo.attributeTwo = this.attributeList[1].value;
        }
        if (this.attributeList[2].value) {
          this.attributeInfo.attributeThree = this.attributeList[2].value;
        }
      }
    },
    submitForm() {
      const self = this;
      self.$emit('onCustomAttributeInit', this.attributeInfo);
      this.isAccordionValuesChanged = false;
    },
  },
}
