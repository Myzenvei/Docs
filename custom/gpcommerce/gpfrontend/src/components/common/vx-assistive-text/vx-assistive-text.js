import {
  globalEventBus
} from '../../../modules/event-bus';

export default {
  name: 'vx-assistive-text',
  components: {},
  props: {
    value: String,
    ariaLive: {
      type: String,
      default: "assertive",
      validator: value => {
        return ['assertive', 'polite', 'off'].indexOf(value) !== -1;
      }
    }
  },
  data() {
    return {
      textToRead: []
    }
  },
  methods: {
    say(text) {
      if (text) {
        this.textToRead = [];
        this.textToRead.push(text);
      }
    }
  },
  mounted() {
    globalEventBus.$on('announce', (value) => {
      this.say(value);
    });
    // this.say(this.value);
  },
  watch: {
    value(val) {
      this.say(val);
    }
  }
}
