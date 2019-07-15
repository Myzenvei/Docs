export default {
  name: 'radio-button-group',
  components: {},
  props: {
    /**
     * Radio button values object array
     * @type {Array}
     */
    radiobuttonValues: {
      type: Array,
    },
    /**
     * Name of the radio button group
     * @type {String}
     */
    name: {
      type: String,
    },
    value: {
      type: String,
    },
    arrangeHorizontal: Boolean,
  },
  data() {
    return {
      radioButtonchecked: '',
    };
  },
  computed: {},
  mounted() {
    this.$emit('radioButtonMounted', {});
    if (this.value) {
      this.setSelectedByValue(this.value);
    }
  },
  methods: {
    /**
     * To set radio button  value
     * @param {Object} optionSelected option selected
     */
    setSelectedByValue(optionSelected) {
      this.radioButtonchecked = optionSelected;
    },
    // To get  radio button   value
    getSelectedRadio() {
      return this.radioButtonchecked;
    },
    // Radio button on selected will emit selected option object that contains label and value
    radioButtonOptionChange(item) {
      if (this.radioButtonchecked !== item.value) {
        this.radioButtonchecked = item.value;
        this.$emit('selected-option', item);
        this.$emit('input', item.value);
      }
    },
    radioButtonPrePopulate(value) {
      this.radioButtonchecked = value;
    },
    // function to reset radio button group.
    resetRadioGroup() {
      this.radioButtonchecked = '';
    },
  },
};
