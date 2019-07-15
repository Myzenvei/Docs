import {
  Validator
} from 'vee-validate';
import flyoutBannerMixin from '../../common/vx-flyout-banner/vx-flyout-banner-mixin';
import loginMixin from '../../common/mixins/login-mixin';
import globals from '../../common/globals';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import ManageProfileShoppingListService from '../../common/services/manage-profile-shopping-lists-service';
import detectDeviceMixin from '../../common/mixins/detect-device-mixin';

export default {
  name: 'vx-social-disconnect',
  mixins: [flyoutBannerMixin, loginMixin, detectDeviceMixin],
  components: {
    vxSpinner,
  },
  props: {
    i18n: Object,
    socialAccount: String
  },
  data() {
    return {
      manageProfileShoppingListService: new ManageProfileShoppingListService(),
      showPassword: false,
      userEmailId: '',
      globals,
      password: '',
    };
  },
  computed: {

  },
  mounted() {
    const veeCustomErrorMessage = {
      en: {
        custom: {
          Password: {
            required: this.i18n.passwordRequiredError,
            regex: this.i18n.passwordInvalidError,
          },
          email: {
            required: this.i18n.emailRequiredError,
            email: this.i18n.emailInvalidError,
          }
        },
      },
    };
    Validator.localize(veeCustomErrorMessage);
  },
  methods: {
    togglePassword() {
      this.showPassword = !this.showPassword;
      if (this.showPassword) {
        this.$refs.password.type = 'text';
      } else {
        this.$refs.password.type = 'password';
      }
    },
    verifyPassword() {
      this.$validator.validateAll().then((result) => {
        if (result) {
          const requestObj = {
            uid: this.userEmailId,
            password: this.password,
            loginType: this.socialAccount.toLowerCase(),
          };
          const requestConfig = {};
          requestConfig.data = requestObj;
          this.manageProfileShoppingListService.disconnectSocialAccount(requestConfig, this.handleDisconnectSocialAccount, this.handleDisconnectSocialAccountError);
          this.$refs.verifyDisconnectSocialAccount.showSpinner();
        } else {
          this.globals.setFocusByName(this.$el, this.globals.getElementName(this.errors));
        }
      });
    },
    handleDisconnectSocialAccount(response) {
      this.$refs.verifyDisconnectSocialAccount.hideSpinner();
      this.$emit('disconnect-social-success', this.i18n.socialSuccesDisconnect);
    },
    handleDisconnectSocialAccountError(error) {
      this.$refs.verifyDisconnectSocialAccount.hideSpinner();
      if (error) {
        this.$emit('disconnect-social-error', this.i18n.socialFailureDisconnect);
      }
    },
    updatePasswordMismatch() {
      this.isPasswordMismatch = false;
    }
  }
}
