export default {
  name: 'vx-checkbox-group',
  components: {},
  props: {
    /**
     * Check box values object array
     * @type {Array}
     */
    checkBoxValues: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {
      checkedBox: [],
      selectedItems: [],
    };
  },
  computed: {},
  mounted() {
    if (this.checkBoxValues.length) {
      this.createCheckboxValues();
    } else {
      const domLoaded = setInterval(() => {
        if (this.checkBoxValues.length) {
          clearInterval(domLoaded);
          this.createCheckboxValues();
        }
      }, 100);
    }
  },
  methods: {
    checkTicked(event, item) {
      if (event.target.checked) {
        this.checkedBox.push(event.target.value);
        this.selectedItems.push(item);
      } else {
        this.checkedBox.splice(this.checkedBox.indexOf(event.target.value), 1);
        this.selectedItems.splice(this.selectedItems.indexOf(item), 1);
      }
      this.$emit('checked-options', {
        selectedValue: this.checkedBox,
        selectedObject: this.selectedItems,
      });
    },
    resetAllCheckboxValues() {
      this.checkedBox = [];
      this.selectedItems = [];
      this.$emit('checked-options', {
        selectedValue: this.checkedBox,
        selectedObject: this.selectedItems,
      });
    },

    createCheckboxValues() {
      for (const entry of this.checkBoxValues) {
        if (entry.isChecked) {
          this.checkedBox.push(entry.value);
          this.selectedItems.push(entry);
        }
      }
    },
  },
};
