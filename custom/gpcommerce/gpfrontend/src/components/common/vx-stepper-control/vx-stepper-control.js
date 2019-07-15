import _ from 'lodash';
import detectDeviceMixin from '../../common/mixins/detect-device-mixin';

export default {
  name: 'vx-stepper-control',
  props: {
    /**
     * initial value
     * @type {Number}
     */
    value: {
      type: Number,
      default: 1,
    },
    /**
     * Mininum value
     * @type {Number}
     */
    minValue: {
      type: Number,
      default: 1,
    },
    maxValue: {
      type: Number,
      default: 999,
    },
    stockLevel: {
      type: Number,
      default: 100,
    },
    isDisabled: {
      type: Boolean,
      default: false,
    },
  },
  mixins: [detectDeviceMixin],
  data() {
    return {
      counter: JSON.parse(this.value),
    };
  },
  watch: {
    counter: _.debounce(function (newVal, oldVal) {
      const regex = RegExp('[^0-9]', 'g');
      newVal = regex.test(newVal) ? undefined : parseInt(newVal);
      if (regex.test(oldVal)) {
        return;
      }
      if (this.stockLevel > 0) {
        if (this.minValue > this.stockLevel) {
          this.counter = parseInt(this.minValue);
          this.$emit('minValueUpdated', parseInt(this.minValue));
          this.$emit('disableAddToCart', parseInt(this.stockLevel));
        } else if (!regex.test(newVal) && newVal !== parseInt(oldVal) && newVal >= parseInt(this.minValue) && newVal <= parseInt(this.maxValue) && newVal <= parseInt(this.stockLevel)) {
          this.$emit('currentCount', newVal);
        } else if (!regex.test(newVal) && newVal !== oldVal && newVal >= parseInt(this.minValue) && newVal > parseInt(this.maxValue)) {
          if (parseInt(this.maxValue) > parseInt(this.stockLevel) && newVal > parseInt(this.stockLevel)) {
            this.counter = parseInt(this.stockLevel);
            this.$emit('stockLevelUpdated', parseInt(this.stockLevel));
          } else {
            this.counter = parseInt(this.maxValue);
            this.$emit('maxValueUpdated', parseInt(this.maxValue));
          }
        } else if (!regex.test(newVal) && newVal !== oldVal && newVal <= parseInt(this.maxValue) && newVal < parseInt(this.minValue) && newVal < parseInt(this.stockLevel) && parseInt(this.minValue) < parseInt(this.stockLevel)) {
          if (parseInt(this.minValue) > parseInt(this.stockLevel)) {
            this.counter = parseInt(this.stockLevel);
            this.$emit('stockLevelUpdated', parseInt(this.stockLevel));
          } else {
            this.counter = parseInt(this.minValue);
            this.$emit('minValueUpdated', parseInt(this.minValue));
          }
        } else if (!regex.test(newVal) && newVal !== oldVal && newVal >= parseInt(this.minValue) && newVal <= parseInt(this.maxValue) && newVal > parseInt(this.stockLevel)) {
          this.counter = parseInt(this.stockLevel);
          this.$emit('stockLevelUpdated', parseInt(this.stockLevel));
        } else {
          this.counter = parseInt(oldVal);
        }
      } else {
        if (!regex.test(newVal) && newVal !== parseInt(oldVal) && newVal >= parseInt(this.minValue) && newVal <= parseInt(this.maxValue)) {
          this.$emit('currentCount', newVal);
        } else if (!regex.test(newVal) && newVal !== oldVal && newVal >= parseInt(this.minValue) && newVal > parseInt(this.maxValue)) {
          this.counter = parseInt(this.maxValue);
          this.$emit('maxValueUpdated', parseInt(this.maxValue));
        } else if (!regex.test(newVal) && newVal !== oldVal && newVal <= parseInt(this.maxValue) && newVal < parseInt(this.minValue)) {
          this.counter = parseInt(this.minValue);
          this.$emit('minValueUpdated', parseInt(this.minValue));
        }
      }
    }, 1000),
    value(newVal) {
      this.counter = parseInt(newVal);
    },
  },
  methods: {
    /**
     * increment count handler
     */
    incrementCount() {
      this.counter = parseInt(this.counter) + 1;
    },
    /**
     * decrement count handler
     */
    decrementCount() {
      if (parseInt(this.counter) > 1) {
        this.counter = parseInt(this.counter) - 1;
      }
    },
  },
};
