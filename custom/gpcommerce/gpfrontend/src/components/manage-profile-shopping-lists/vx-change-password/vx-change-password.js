import VeeValidate, {
  Validator
} from 'vee-validate';
import flyoutBannerMixin from '../../common/vx-flyout-banner/vx-flyout-banner-mixin';
import loginMixin from '../../common/mixins/login-mixin';
import globals from '../../common/globals';
import { brandNames } from '../../common/mixins/vx-enums';
import mobileMixin from '../../common/mixins/mobile-mixin';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import ManageProfileShoppingListService from '../../common/services/manage-profile-shopping-lists-service';
import detectDeviceMixin from '../../common/mixins/detect-device-mixin';
/* Change Password from Profile page */
export default {
  name: 'vx-change-password',
  mixins: [flyoutBannerMixin, loginMixin, mobileMixin, detectDeviceMixin],
  components: {
    vxSpinner,
  },
  props: {
    /**
    * All copy texts coming from properties files
    */
    i18n: Object,
  },
  data() {
    return {
      manageProfileShoppingListService: new ManageProfileShoppingListService(),
      currentPasswordUpdateText: '',
      newPasswordUpdateText: '',
      showOldPassword: false,
      showNewPassword: false,
      userEmailId: '',
      updatePasswordServerError: '',
      isPasswordMismatch: false,
      isNewPassword: false,
      globals,
      brandNames,
      screenHeight: window.innerHeight,
    };
  },
  computed: {},
  created() {
    // let self = this;
    // Validator.extend('currentpassword', {
    //   getMessage(field, params, data) {
    //     return "Current password is not valid!";
    //   },
    //   // Returns a boolean value
    //   validate(value) {
    //     //console.log(value);
    //     return self.validateCurrentPassword();
    //   }
    // });
  },
  mounted() {
    /**
     * Provide Custom error messages for Vee validate conditions to run validation on input fields
     */
    const veeCustomErrorMessage = {
      en: {
        custom: {
          oldPassword: {
            required: this.i18n.currentPasswordRequiredError,
            regex: this.i18n.currentPasswordInvalidError,
          },
          newPassword: {
            required: this.i18n.newPasswordRequiredError,
            regex: this.i18n.newPasswordInvalidError,
          },
        },
      },
    };
    Validator.localize(veeCustomErrorMessage);
    this.handleResize();
    window.addEventListener('resize', this.handleResize);
  },
  methods: {
    /**
     * Toggle between showing the password with masking and without masking
     * @param {String} passwordType 
     */
    togglePassword(passwordType) {
      if (passwordType === 'oldPassword') {
        this.showOldPassword = !this.showOldPassword;
      } else {
        this.showNewPassword = !this.showNewPassword;
      }

      if (this.$refs[passwordType].type === 'text') {
        this.$refs[passwordType].type = 'password';
      } else {
        this.$refs[passwordType].type = 'text';
      }
    },
    // handleError(error) {
    //   this.showFlyout('error', 'Error will come from service', true);
    //   this.$refs.updateProfileSpinner.hideSpinner();
    //   if (error) {
    //     this.updatePasswordServerError = this.i18n[
    //       error.response.data.errors[0].message
    //     ];
    //   }
    // },
    /**
     * Submit old and new passwords entered as parameters to service call 
     */
    submitPassword() {
      this.$validator.validateAll().then((result) => {
        if (result) {
          const requestObj = {
            "old": this.currentPasswordUpdateText,
            "new": this.newPasswordUpdateText
          };
          const requestConfig = {};
          requestConfig.params = requestObj;
          if (this.currentPasswordUpdateText === this.newPasswordUpdateText) {
            this.isNewPassword = true;
          } else {
            this.isNewPassword = false;
            this.manageProfileShoppingListService.updateProfilePassword(requestConfig, this.handleUpdateProfilePasswordResponse, this.handleUpdateProfilePasswordError);
            this.$refs.updateProfileSpinner.showSpinner();
          }
        } else {
          this.globals.setFocusByName(this.$el, this.globals.getElementName(this.errors));
        }
      });
    },
    /**
     * Update variable during input to remove input-error class from current password input field
     */
    updatePasswordMismatch() {
      this.isPasswordMismatch = false;
    },
    /**
     * Handle response of service
     * @param {Object} response 
     */
    handleUpdateProfilePasswordResponse(response) {
      this.isPasswordMismatch = false;
      this.$refs.updateProfileSpinner.hideSpinner();
      if (response.status === 202) {
        this.$emit('update-password-success', "Password updated successfully.");
      }
    },
    /**
     * Handle error of service
     * @param {Object} error 
     */
    handleUpdateProfilePasswordError(error) {
      this.isPasswordMismatch = true;
      if (error) {
        this.$refs.updateProfileSpinner.hideSpinner();
      }
    },

    /**
     * Force element focused into view during keyboard input in mobile devices, preventing it from getting overlapped
     */
    focusInButStateChange() {
      this.updatePasswordServerError = '';
      var self = this;
      setTimeout(function () {
        if (self.isMobile() && window.innerHeight < self.screenHeight) {
          document.activeElement.scrollIntoView(true);
        }
      }, 200);
    },
    /**
     * It is to handle the keyboard input display correctly in few mobile devices and prevent overlapping of submit button on input fields
     */
    handleResize() {
      if (document.querySelector('.vx-update-password')) {
        if (this.isMobile() && this.globals.siteId !== this.brandNames.gpemployee) {
          document.querySelector('.vx-update-password').style.height = (window.innerHeight - 159) + 'px';
        }
        else {
          document.querySelector('.vx-update-password').style.height = '';
        }
      }
    }
  },
  destroyed() {
    window.removeEventListener('resize', this.handleResize);
  },
};
