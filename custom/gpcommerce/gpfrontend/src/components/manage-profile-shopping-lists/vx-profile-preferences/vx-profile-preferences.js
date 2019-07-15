import vxProfileCard from '../../common/vx-profile-card/vx-profile-card.vue';
import vxTaxExemption from '../vx-tax-exemption/vx-tax-exemption.vue';
import vxCommunicationPreferences from '../vx-communication-preferences/vx-communication-preferences.vue';
import vxSocialDisconnect from '../vx-social-disconnect/vx-social-disconnect.vue';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import vxChangePassword from '../vx-change-password/vx-change-password.vue';
import vxEditPersonalDetails from '../vx-edit-personal-details/vx-edit-personal-details.vue';
import vxFacebookLogin from '../../registration-login/vx-facebook-login/vx-facebook-login.vue';
import vxGoogleLogin from '../../registration-login/vx-google-login/vx-google-login.vue';
import globals from '../../common/globals';
import flyoutBannerMixin from '../../common/vx-flyout-banner/vx-flyout-banner-mixin';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import {
  socialAccountType,
  socialConnect,
  UserRoles,
  logoutFlowCookie,
  googleErrorResponse,
  monthNames,
  dateSuffix,
  PreferNotToIdentify,
  singleDigitDate,
} from '../../common/mixins/vx-enums';
import ManageProfileShoppingListService from '../../common/services/manage-profile-shopping-lists-service';

export default {
  name: 'vx-profile-preferences',
  mixins: [flyoutBannerMixin],
  components: {
    'vx-profile-card': vxProfileCard,
    'vx-tax-exemption': vxTaxExemption,
    'vx-communication-preferences': vxCommunicationPreferences,
    vxModal,
    vxSpinner,
    vxChangePassword,
    vxEditPersonalDetails,
    vxSocialDisconnect,
    vxFacebookLogin,
    vxGoogleLogin,
  },
  props: {
    i18n: {
      type: Object,
      required: true,
    },
    showTaxExemption: {
      type: Boolean,
      default: false,
    },
    isGenderEnabled: {
      type: Boolean,
      default: false,
    },
    isDobEnabled: {
      type: Boolean,
      default: false,
    },
    showSocialLogin: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      userDetails: {},
      globals,
      appToken: '',
      isSocialLogin: false,
      isGoogleLogin: false,
      isFacebookLogin: false,
      taxExemptionStatus: '',
      socialAccount: '',
      userRole: '',
      monthSuffix: '',
      dateSuffix,
      dateDisplay: '',
      genderDetails: '',
      PreferNotToIdentify,
      preferNotToIdentify: 'Prefer not to identify',
      manageProfileShoppingListService: new ManageProfileShoppingListService(),
    };
  },
  computed: {},
  mounted() {
    this.getUserDetails();
  },
  methods: {
    showSpinner() {
      this.$refs.profileSpinner.showSpinner();
    },
    hideSpinner() {
      this.$refs.profileSpinner.hideSpinner();
    },
    getUserDetails() {
      const requestConfig = {};
      this.manageProfileShoppingListService.getUserDetails(
        requestConfig,
        this.handleUserDetailsResponse,
        this.handleUserDetailsError,
      );
      this.$refs.profileSpinner.showSpinner();
    },
    getTaxExemptionStatus() {
      const requestConfig = {};
      this.manageProfileShoppingListService.getTaxExemptionStatus(
        requestConfig,
        this.handleGetTaxExemption,
        this.handleGetTaxExemptionError,
      );
      this.$refs.profileSpinner.showSpinner();
    },
    callTaxExemptionModal(event) {
      this.$refs.taxExemptionModal.open(event);
    },
    callCommunicationPreferencesModal(event) {
      this.$refs.communicationPreferencesModal.open(event);
    },
    googleLogin() {
      this.socialAccount = socialAccountType.GOOGLE;
      this.$refs.googleLogin.googleLogin();
    },
    // Method that handles click on facebook login button
    facebookLogin() {
      this.socialAccount = socialAccountType.FACEBOOK;
      this.$refs.facebookLogin.facebookLogin();
    },
    // Facebook response gets Accesstoken in data which we need to pass in request data to backend
    // On decription of accesstoken we will get user data.
    // It contains isSocialLogin flag that will be true
    // if user tries to login/register through facebook or google
    facebookResponse(data) {
      this.appToken = data.accessToken;
      this.connectToSocialAccount();
    },
    // Handling error method
    facebookErrorResponse(error) {
      this.showFlyout('error', error.message, true, 10000);
    },
    // Google response gets authcode in data which we need to pass in request
    // data along with isSocialLogin flag to backend On decription of authcode
    // we will get user data.
    googleResponse(data) {
      this.appToken = data;
      this.connectToSocialAccount();
    },
    googleErrorResponse(error) {
      if (error !== googleErrorResponse.popUpClosedError) {
        this.showFlyout('error', error, true, 10000);
      }
    },
    disconnectGoogleLogin(event) {
      this.socialAccount = socialAccountType.GOOGLE;
      this.$refs.socialAccDisconnectModal.open(event);
    },
    disconnectFacebookLogin(event) {
      this.socialAccount = socialAccountType.FACEBOOK;
      this.$refs.socialAccDisconnectModal.open(event);
    },
    connectToSocialAccount() {
      const requestObj = {
        uid: this.userDetails.uid,
        loginType: this.socialAccount.toLowerCase(),
        token: this.appToken,
        password: socialConnect.password,
      };
      const requestConfig = {};
      requestConfig.data = requestObj;
      this.manageProfileShoppingListService.connectSocialAccount(
        requestConfig,
        this.handleConnectSocialAccount,
        this.handleConnectSocialAccountError,
      );
      this.$refs.profileSpinner.showSpinner();
    },
    callUpdatePasswordModal(event) {
      this.$refs.updatePasswordModal.open(event);
    },
    callEditPersonalDetailsModal(event) {
      this.$refs.editPersonalDetailsModal.open(event);
    },
    handleUserDetailsResponse(response) {
      this.userDetails = response.data;
      if (this.userDetails.roles && this.userDetails.roles.length) {
        if (this.userDetails.roles[0] === UserRoles.ADMIN) {
          this.userRole = UserRoles.DISPLAY_ADMIN;
        } else {
          this.userRole = UserRoles.DISPLAY_BUYER;
        }
      }
      this.userDetails.uid = response.data.uid.split('|')[0];
      if (
        response.data.gender &&
        response.data.gender === this.PreferNotToIdentify.otherGender
      ) {
        this.genderDetails = this.preferNotToIdentify;
      } else {
        this.genderDetails = response.data.gender;
      }
      if (response.data.dateOfBirth) {
        this.userDetails.date = response.data.dateOfBirth.split('-')[0];
        if (singleDigitDate.indexOf(this.userDetails.date) > -1) {
          this.dateDisplay = this.userDetails.date.replace(/0/g, '');
        } else {
          this.dateDisplay = this.userDetails.date;
        }
        this.userDetails.month = response.data.dateOfBirth.split('-')[1];
        this.userDetails.monthName =
          monthNames[parseInt(response.data.dateOfBirth.split('-')[1]) - 1];
        switch (this.userDetails.date) {
          case '01':
          case '21':
          case '31':
            this.monthSuffix = this.dateSuffix.first;
            break;
          case '02':
          case '22':
            this.monthSuffix = this.dateSuffix.second;
            break;
          case '03':
          case '23':
            this.monthSuffix = this.dateSuffix.third;
            break;
          default:
            this.monthSuffix = this.dateSuffix.other;
        }
      }
      this.setLoginType();
      if (this.globals.isB2B()) {
        this.getTaxExemptionStatus();
      } else {
        this.$refs.profileSpinner.hideSpinner();
      }
    },
    handleUserDetailsError(error) {
      if (error) {
        this.$refs.profileSpinner.hideSpinner();
      }
    },
    handleGetTaxExemption(response) {
      this.$refs.profileSpinner.hideSpinner();
      if (response.data) {
        this.taxExemptionStatus = response.data.toLowerCase().replace('_', ' ');
      }
    },
    handleGetTaxExemptionError(error) {
      this.$refs.profileSpinner.hideSpinner();
      if (error) {}
    },
    handleConnectSocialAccount(response) {
      this.$refs.profileSpinner.hideSpinner();
      this.showFlyout('success', this.i18n.socialSuccesConnect, true, 10000);
      this.updateSocialLoginStatus();
      // Logout the user
      this.globals.logout(logoutFlowCookie.socialConnect);
    },
    handleConnectSocialAccountError(response) {
      this.$refs.profileSpinner.hideSpinner();
      if (response) {
        this.showFlyout('error', response.message, true, 10000);
      }
    },
    updatedTaxExemptionFailure(failure) {
      this.$refs.taxExemptionModal.close();
      this.showFlyout('error', failure, true, 10000);
    },
    updatedTaxExemptionSuccess(success) {
      this.hideSpinner();
      this.$refs.taxExemptionModal.close();
      this.getTaxExemptionStatus();
      this.showFlyout('success', success, true, 10000);
    },
    updatedPersonalDetailSuccess(success) {
      this.$refs.editPersonalDetailsModal.close();
      this.getUserDetails();
      this.showFlyout('success', success, true, 10000);
    },
    updatePasswordSuccess(success) {
      this.$refs.updatePasswordModal.close();
      this.showFlyout('success', success, true, 10000);
    },
    updatePreferencesSuccess(success) {
      this.$refs.communicationPreferencesModal.close();
      this.showFlyout('success', success, true, 10000);
      this.getUserDetails();
    },
    updateDisconnectSocialAccError(error) {
      this.$refs.socialAccDisconnectModal.close();
      this.showFlyout('error', error, true, 10000);
    },
    updateDisconnectSocialAccSuccess(success) {
      this.resetSocialAccountData();
      this.$refs.socialAccDisconnectModal.close();
      this.showFlyout('success', success, true, 10000);
      // Logout the user
      this.globals.logout(logoutFlowCookie.socialDisconnect);
    },
    resetSocialAccountData() {
      this.isSocialLogin = false;
      this.isGoogleLogin = false;
      this.isFacebookLogin = false;
    },
    updateSocialLoginStatus() {
      this.isSocialLogin = true;
      if (
        this.socialAccount.toLowerCase() ===
        socialAccountType.GOOGLE.toLowerCase()
      ) {
        this.isGoogleLogin = true;
        this.isFacebookLogin = false;
      } else {
        this.isGoogleLogin = false;
        this.isFacebookLogin = true;
      }
    },
    setLoginType() {
      const {
        loginType
      } = {
        ...this.userDetails,
      };
      if (
        loginType === socialAccountType.GOOGLE.toLowerCase() ||
        loginType === socialAccountType.FACEBOOK.toLowerCase()
      ) {
        this.socialAccount =
          loginType.charAt(0).toUpperCase() +
          loginType.substr(1, loginType.length);
        this.updateSocialLoginStatus();
      } else {
        this.isSocialLogin = false;
      }
    },
    updateLoginType() {
      this.socialAccount =
        this.socialAccount.charAt(0).toUpperCase() +
        this.socialAccount.substr(1, this.socialAccount.length);
    },
  },
};
