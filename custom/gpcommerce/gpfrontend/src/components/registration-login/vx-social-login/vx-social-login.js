// import social from './social-login-i18n';
// import './vx-social-login.less';
import vxFacebookLogin from '../vx-facebook-login/vx-facebook-login.vue';
import vxGoogleLogin from '../vx-google-login/vx-google-login.vue';
import globals from '../../common/globals';
import loginMixin from '../../common/mixins/login-mixin';
import flyoutBannerMixin from '../../common/vx-flyout-banner/vx-flyout-banner-mixin';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import RegistrationLoginService from '../../common/services/registration-login-service';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import vxUpdateEmail from '../vx-update-email/vx-update-email.vue';
import {noEmailErrorCode} from '../../common/mixins/vx-enums';

/**
 * Component for login through social accounts
 */

export default {
  name: 'vx-social-login',
  mixins: [loginMixin, flyoutBannerMixin, noEmailErrorCode],
  components: {
    vxFacebookLogin,
    vxGoogleLogin,
    vxSpinner,
    vxModal,
    vxUpdateEmail,
  },
  props: {
    register: Boolean,
    actionUrl: String,
    i18n: Object,
    googleAuthCode: String,
    facebookAccessToken: String,
  },
  data() {
    return {
      // i18n: social,
      globals,
      registrationLoginService: new RegistrationLoginService(),
      heading: '',
      isSocialLogin: false,
      loginType: '',
      appToken: '',
      serverError: '',
      isCheckout: false,
      updateEmailData: '',
      noEmailErrorCode,
    };
  },
  computed: {},
  mounted() {
    if (this.register) {
      this.heading = this.i18n.socialRegisterheading;
    } else {
      this.heading = this.i18n.socialLoginheading;
    }
    if (this.globals.getCookie('flow') === 'checkout') {
      this.isCheckout = true;
    }
  },
  methods: {
    // Method that handles click on google login button
    googleLogin() {
      this.$refs.googleLogin.googleLogin();
    },
    // Method that handles click on facebook login button
    facebookLogin() {
      this.$refs.facebookLogin.facebookLogin();
    },
    // Facebook response gets Accesstoken in data which we need to pass in request data to backend
    // On decription of accesstoken we will get user data.
    // It contains isSocialLogin flag that will be true
    // if user tries to login/register through facebook or google
    facebookResponse(data) {
      this.isSocialLogin = true;
      this.loginType = this.i18n.facebookLoginType;
      this.appToken = data.accessToken;
      const requestdata = this.createSocialRequestData(
        this.appToken,
        this.isSocialLogin,
        this.loginType,
      );
      if (this.register) {
        this.callSocialRegister(requestdata);
      } else {
        this.submitLogin();
      }
    },
    // Handling error method
    facebookErrorResponse(error) {
      this.showFlyout('error', error.message, true);
    },
    // Google response gets authcode in data which we need to pass in request
    // data along with isSocialLogin flag to backend On decription of authcode
    // we will get user data.
    googleResponse(data) {
      this.isSocialLogin = true;
      this.loginType = this.i18n.googleLoginType;
      this.appToken = data;
      const requestdata = this.createSocialRequestData(
        this.appToken,
        this.isSocialLogin,
        this.loginType,
      );
      if (this.register) {
        this.callSocialRegister(requestdata);
      } else {
        this.submitLogin();
      }
    },
    // Create Social Request data
    createSocialRequestData(token, isSocialLogin, loginType) {
      const requestdata = {
        isSocialLogin,
        token,
        loginType,
      };
      return requestdata;
    },
    // Call Social Register
    callSocialRegister(requestdata) {
      const requestConfig = {};
      requestConfig.data = requestdata;
      this.registrationLoginService.registerUser(
        requestConfig,
        this.handleRegisterResponse,
        this.handleRegisterError,
      );
      this.$refs.spinner.showSpinner();
    },
    // Google error response handling
    googleErrorResponse(error) {
      if (error !== 'popup_closed_by_user') {
        this.showFlyout('error', error, true);
      }
    },
    // Register ajax error handling
    handleRegisterError(error) {
      this.hideSpinner();
      if (error) {
        this.serverError = this.i18n[error.response.data.errors[0].message];
      }
    },
    // Register ajax success response handling
    handleRegisterResponse(response) {
      this.hideSpinner();
      if (response.data.errorCode === this.noEmailErrorCode.NO_EMAIL_ERROR_CODE) {
        this.$refs.updateEmailModal.open();
        this.updateEmailData = response.data;
      } else {
        const requestdata = this.createSocialRequestData(
          this.appToken,
          this.isSocialLogin,
          this.loginType,
        );
        this.showFlyoutNextPage('success', this.i18n.registerSuccessMsg, true);
        this.submitLogin();
      }
    },
    handleEmailUpdateInitiated() {
      this.$refs.spinner.showSpinner();
    },
    handleEmailupdated() {
      this.showFlyoutNextPage('success', this.i18n.registerSuccessMsg, true);
      this.submitLogin();
    },
    hideSpinner() {
      this.$refs.spinner.hideSpinner();
    },
  },
};
