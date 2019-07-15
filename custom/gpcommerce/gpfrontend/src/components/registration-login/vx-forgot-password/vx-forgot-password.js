import {
  Validator
} from 'vee-validate';
import globals from '../../common/globals';
import mobileMixin from '../../common/mixins/mobile-mixin';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import {
  globalEventBus,
} from '../../../modules/event-bus';
import RegistrationLoginService from '../../common/services/registration-login-service';
import detectDeviceMixin from '../../common/mixins/detect-device-mixin';


/**
 * Forgot password component to reset password
 */

export default {
  name: 'vx-forgot-password',
  mixins: [mobileMixin, detectDeviceMixin],
  components: {
    vxSpinner,
  },

  props: {
    isExpiry: Boolean,
    i18n: Object,
  },
  data() {
    return {
      registrationLoginService: new RegistrationLoginService(),
      resetButtonClicked: false,
      emailAddress: '',
      globals,
      forgotPasswordStatus: '',
      forgotPasswordLabel: '',
      passwordServerError: '',
      screenHeight: window.innerHeight,
    };
  },
  computed: {},
  mounted() {
    let veeCustomErrorMessage = {
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
    // Reset password function will be called on click of reset password button which validates
    // email feild and sends request data in forgot password ajax call

    resetPassword() {
      if (this.checkFieldValidation() && this.emailAddress !== '') {
        // request data is TBD
        const requestdata = {
          userId: this.emailAddress,
        };
        // $emit will emit/send requestData to parent component.
        const requestConfig = {};
        requestConfig.params = requestdata;
        this.registrationLoginService.forgotPassword(requestConfig, this.handleResponse, this.handleError);
        this.$refs.spinner.showSpinner();
      }
    },
    checkFieldValidation() {
      return this.$validator.validateAll().then((result) => {
        if (result) {
          return true;
        } else {
          this.globals.setFocusByName(this.$el, this.globals.getElementName(this.errors));
          return false;
        }
      });
    },
    // Handle the response along with success message
    handleResponse() {
      this.$refs.spinner.hideSpinner();
      this.resetButtonClicked = true;
      this.$emit('submit');
      this.forgotPasswordLabel = this.i18n.passwordResetPostLabel;
      this.forgotPasswordStatus = this.i18n.passwordResetPostDesc;
      globalEventBus.$emit('announce', this.forgotPasswordStatus);
    },
    // Handle the response along with error message
    handleError(error) {
      this.$refs.spinner.hideSpinner();
      if (error) {
        this.resetButtonClicked = false;
        this.passwordServerError = this.i18n[
          error.response.data.errors[0].message
        ];
        globalEventBus.$emit('announce', this.passwordServerError);
      }
    },
    emailFocusIn() {
      this.passwordServerError = '';
      var self = this;
      setTimeout(function(){
        if(self.isMobile() && window.innerHeight < self.screenHeight) {
          document.activeElement.scrollIntoView(true);
        }
      },200);
    },
  },
};
