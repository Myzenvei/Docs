import globals from '../globals';
import flyoutBannerMixin from '../../common/vx-flyout-banner/vx-flyout-banner-mixin';
import cookiesMixin from '../../common/mixins/cookies-mixin';
import kauthMixin from './kauth-mixin';
import RegistrationLoginService from '../../common/services/registration-login-service';
import {
  globalEventBus,
} from '../../../modules/event-bus';
import { logoutFlowCookie, flyoutStatus } from './vx-enums';


const loginMixin = {
  mixins: [flyoutBannerMixin, kauthMixin, cookiesMixin],
  data() {
    return {
      globals,
      kochTokens: {},
      registrationLoginService: new RegistrationLoginService(),
    };
  },
  methods: {
    handleUserUpdateSuccesMsg() {
      if (this.readCookie(logoutFlowCookie.logoutFlow) === logoutFlowCookie.emailUpdate) {
        this.showFlyout(flyoutStatus.success, this.i18n.userUpdateSuccess, true);
      }
      if (this.readCookie(logoutFlowCookie.logoutFlow)) {
        this.eraseCookie(logoutFlowCookie.logoutFlow);
      }
    },
    submitLogin() {
      const self = this;
      setTimeout(() => {
        self.$refs.loginForm.submit();
      }, 1000);
    },
    handleLoginErrorCode() {
      if (this.globals.getUrlParam('error') === 'true') {
        const errorCode = this.globals.getUrlParam('errorCode');
        this.serverError = this.i18n[errorCode];
        if (errorCode === '199') {
          this.setStorage(this.kochKeys.KOCHCODE, 'token');
          globalEventBus.$emit('announce', this.serverError);
        }
        if ((errorCode === '190' || errorCode === '193') && (this.globals.isB2B() || this.globals.isB2C())) {
          this.serverError = this.i18n.b2berror190;
          globalEventBus.$emit('announce', this.serverError);
        }
        if (errorCode === '191') {
          this.disableField = true;
        }
        if (errorCode === '194') {
          this.setStorage(this.kochKeys.KOCHREDIRECT, 'login');
          this.globals.navigateToUrl('kauthUrl', true);
        }
      }
    },
    setKochToken() {
      if (this.globals.isEmployeeStore()) {
        this.token = this.getStorage(this.kochKeys.KOCHCODE) || 'token';
        if (this.token !== 'token') {
          this.registrationLoginService.getKochAuthToken({}, this.handleKochTokenResponse, this.handleKochTokenError, this.token);
        }
      }
    },
    handleKochTokenResponse(response) {
      this.kochTokens = response.data;
    },
    handleKochTokenError() {}, // error param is availabel in error scenario
  },
};

export default loginMixin;
