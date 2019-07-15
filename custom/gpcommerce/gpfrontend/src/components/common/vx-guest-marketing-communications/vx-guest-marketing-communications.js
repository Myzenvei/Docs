import {
  Validator
} from 'vee-validate';
import globals from '../globals';

export default {
  name: 'vx-guest-marketing-communications',
  components: {},
  props: {
    i18n: {
      type: Object,
      // required: true,
    },
  },
  data() {
    return {
      email: '',
      globals,
    };
  },
  computed: {

  },
  mounted() {
    const veeCustomErrorMessage = {
      en: {
        custom: {
          email: {
            required: this.i18n.emailRequiredError,
            email: this.i18n.emailInvalidError,
          },
        },
      },
    };
    Validator.localize(veeCustomErrorMessage);
  },
  methods: {
    sendRequest() {
      this.$validator.validateAll().then((result) => {
        if (result) {
          // call goes here
        } else {
          this.globals.setFocusByName(this.$el, this.globals.getElementName(this.errors));
        }
      });
    },
  },
};
