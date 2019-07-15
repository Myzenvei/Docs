// import updatePasswordCopy from './update-password-i18n';
import { Validator } from 'vee-validate';
import globals from '../../common/globals';
import flyoutBannerMixin from '../../common/vx-flyout-banner/vx-flyout-banner-mixin';
import loginMixin from '../../common/mixins/login-mixin';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import RegistrationLoginService from '../../common/services/registration-login-service';
import {
  globalEventBus,
} from '../../../modules/event-bus';
import detectDeviceMixin from '../../common/mixins/detect-device-mixin';

/**
 * Component for updating password
 */

export default {
  name: 'vx-update-password',
  mixins: [flyoutBannerMixin, loginMixin, detectDeviceMixin],
  components: {
    vxSpinner,
  },
  props: {
    heading: String,
    actionUrl: String,
    i18n: Object,
  },
  data() {
    return {
      globals,
      registrationLoginService: new RegistrationLoginService(),
      // i18n: updatePasswordCopy,
      passwordUpdateText: '',
      showPassword: false,
      isNewPassword: false,
      userEmailId: '',
      updatePasswordServerError: '',
    };
  },
  computed: {
    // If heading attribute is passed through props and contains any data
    // then headingData function returns props data else it returns default data from i18n

    headingData() {
      return this.heading || this.i18n.updatePasswordTitle;
    },
  },
  mounted() {
    const veeCustomErrorMessage = {
      en: {
        custom: {
          Password: {
            required: this.i18n.passwordRequiredError,
            regex: this.i18n.passwordInvalidError,
          }
        },
      },
    };
    Validator.localize(veeCustomErrorMessage);
  },
  methods: {
    // New password function will be called on click of reset password  which validates
    // password feild and sends request data in update password ajax call
    newPassword() {
      if (this.checkFieldValidation() && this.passwordUpdateText !== '') {
        const token = this.globals.getUrlParam('token');
        const requestdata = {
          password: this.passwordUpdateText,
          token,
        };
        // $emit will emit/send requestData to parent component.
        const requestConfig = {};
        requestConfig.params = requestdata;
        if (this.passwordUpdateText) {
          this.isNewPassword = true;
        } else {
          this.isNewPassword = false;
          this.registrationLoginService.updatePassword(requestConfig, this.handleResponse, this.handleError);
          this.$refs.spinner.showSpinner();
        }
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
    // Show hide password
    togglePassword() {
      this.showPassword = !this.showPassword;
      if (this.showPassword) {
        this.$refs.password.type = 'text';
      } else {
        this.$refs.password.type = 'password';
      }
    },

    // Update password response handling
    handleResponse(response) {
      this.userEmailId = response.data.uid;
      this.showFlyoutNextPage('success', this.i18n.passwordSuccessMsg, true);
      this.submitLogin();
      // More proper is to handle this via emit signal
      this.$emit('submit');
      this.$refs.spinner.hideSpinner();
    },
    // Update password error handling
    handleError(error) {
      this.showFlyout('error', 'Error will come from service', true);
      this.$refs.spinner.hideSpinner();
      if (error) {
        this.updatePasswordServerError = this.i18n[
          error.response.data.errors[0].message
        ];
        globalEventBus.$emit('announce', this.updatePasswordServerError);
      }
    },
  },
};
