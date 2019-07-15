import {
  Validator
} from 'vee-validate';
import flyoutBannerMixin from '../../common/vx-flyout-banner/vx-flyout-banner-mixin';
import loginMixin from '../../common/mixins/login-mixin';
import globals from '../../common/globals';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import ManageProfileShoppingListService from '../../common/services/manage-profile-shopping-lists-service';

export default {
  name: 'vx-confirm-password',
  mixins: [flyoutBannerMixin, loginMixin],
  components: {
    vxSpinner,
  },
  props: {
    /**
   * All copy texts coming from properties files
   */
    i18n: Object,
    /**
     * All the user details entered during editing profile 
     */
    userDetails: Object
  },
  data() {
    return {
      manageProfileShoppingListService: new ManageProfileShoppingListService(),
      currentPasswordUpdateText: '',
      newPasswordUpdateText: '',
      showPassword: false,
      userEmailId: '',
      updatePasswordServerError: '',
      globals,
      password: '',
      isPasswordMismatch: false
    }
  },
  computed: {

  },
  mounted() {
    /**
    * Provide Custom error messages for Vee validate conditions to run validation on input fields
    */
    const veeCustomErrorMessage = {
      en: {
        custom: {
          Password: {
            required: this.i18n.passwordRequiredError,
            regex: this.i18n.passwordRequiredError,
          }
        },
      },
    };
    Validator.localize(veeCustomErrorMessage);
  },
  methods: {
    /**
    * Toggle between showing the password with masking and without masking
    */
    togglePassword() {
      this.showPassword = !this.showPassword;
      if (this.showPassword) {
        this.$refs.password.type = 'text';
      } else {
        this.$refs.password.type = 'password';
      }
    },
    /**
    * Call service to send profile details that have been edited and verify if password entered is correct
    */
    verifyPassword() {
      const {
        firstName,
        lastName,
        uid,
        cellPhone,
        password,
        userIdUpdateFlag
      } = {
        ...this.userDetails,
        password: this.password,
        userIdUpdateFlag: true
      };
      const requestObj = {
        firstName,
        lastName,
        uid,
        cellPhone,
        password,
        userIdUpdateFlag
      }

      this.$validator.validateAll().then((result) => {
        if (result) {
          const requestConfig = {};
          requestConfig.data = requestObj;
          this.manageProfileShoppingListService.passwordVerification(requestConfig, this.handlePasswordVerification, this.handlePasswordVerificationError);
          this.$refs.verifyPasswordSpinner.showSpinner();
        } else {
          this.globals.setFocusByName(this.$el, this.globals.getElementName(this.errors));
        }
      });
    },
    /**
     * Handle response of service to check if password is valid
     */
    handlePasswordVerification(response) {
      this.isPasswordMismatch = false;
      this.$emit('verifiedPassword', this.password);
    },
    /**
     * Handle error of service incase password is not valid
     */
    handlePasswordVerificationError(error) {
      this.$refs.verifyPasswordSpinner.hideSpinner();
      if (error) {
        if (error.hasOwnProperty('response') && error.response.data.errors[0].message.toLowerCase().indexOf("passwordmismatchexception") >= 0) {
          this.isPasswordMismatch = true;
        } else {
          this.$emit('errorExists', error.response.data.errors[0]);
        }
      }
    },
    /**
     * Update variable during input to remove input-error class from input field 
     */
    updatePasswordMismatch() {
      this.isPasswordMismatch = false;
    }
  }
}
