// import login from './login-i18n';
import {
  Validator
} from 'vee-validate';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import vxForgotPassword from '../vx-forgot-password/vx-forgot-password.vue';
import globals from '../../common/globals';
import loginMixin from '../../common/mixins/login-mixin';
import flyoutBannerMixin from '../../common/vx-flyout-banner/vx-flyout-banner-mixin';
import headerMixin from '../../common/mixins/header-mixin';
import detectDeviceMixin from '../../common/mixins/detect-device-mixin';
import {
  globalEventBus,
} from '../../../modules/event-bus';
import cookiesMixin from '../../common/mixins/cookies-mixin';

/**
 * Login component
 */

export default {
  name: 'vx-login',
  mixins: [loginMixin, flyoutBannerMixin, headerMixin, detectDeviceMixin, cookiesMixin],
  props: {
    actionUrl: String,
    i18n: Object,
  },
  components: {
    vxModal,
    vxForgotPassword,
  },
  data() {
    return {
      user: {
        email: '',
        password: ''
      },
      // i18n: login,
      globals,
      showModal: true,
      disableField: false,
      showPassword: false,
      serverError: '',
      token: '',
      isCheckout: false,
    };
  },
  methods: {
    submitClicked(e) {
      const self = this;
      e.preventDefault();
      this.$validator.validateAll().then((result) => {
        if (result) {
          if (self.readCookie('guid_user') === 'current') {
            self.showFlyout('error', this.i18n.activeLoginSession, true);
          } else {
            self.submitLogin();
          }
        } else {
          self.globals.setFocusByName(self.$el, self.globals.getElementName(self.errors));
        }
      });
    },
    togglePassword() {
      this.showPassword = !this.showPassword;
      if (this.showPassword) {
        this.$refs.password.type = 'text';
      } else {
        this.$refs.password.type = 'password';
      }
    },
    handleForgotPassword(callingParent) {
      this.$refs.forgotPasswordModal.open(callingParent);
    },
    createAccount() {
      this.createAccountButHandler();
    },
    handleErrorCallback(errorData) {
      if (errorData.response.data.error_description === 'Reset credentials') {
        this.disableField = true;
        this.showFlyout('error', this.i18n.invalidAttemptsError, true);
      } else {
        this.serverError = errorData.response.data.error_description;
      }
    },
    hideServerError() {
      this.serverError = '';
    },
  },
  mounted() {
    this.handleUserUpdateSuccesMsg();
    globalEventBus.$emit('announce', 'Login Page');
    this.handleLoginErrorCode();
    this.setKochToken();
    let veeCustomErrorMessage = {
      en: {
        custom: {
          j_username: {
            required: this.i18n.emailRequiredError,
            email: this.i18n.emailInvalidError,
          },
          j_password: {
            required: this.i18n.passwordRequiredError,
            regex: this.i18n.passwordInvalidError,
          }
        }
      },
    };
    Validator.localize(veeCustomErrorMessage);
    if (this.globals.getCookie('flow') === 'checkout' || this.globals.getIsSubscription()) {
      this.isCheckout = true;
    }
  },
};
