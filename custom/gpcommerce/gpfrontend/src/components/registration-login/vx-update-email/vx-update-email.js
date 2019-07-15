import { Validator } from 'vee-validate';
import detectDeviceMixin from '../../common/mixins/detect-device-mixin';
import RegistrationLoginService from '../../common/services/registration-login-service';
import loginMixin from '../../common/mixins/login-mixin';
import globals from '../../common/globals';
import { duplicateMailErrorCode } from '../../common/mixins/vx-enums';

export default {
  name: 'vx-update-email',
  components: {},
  mixins: [detectDeviceMixin, loginMixin, duplicateMailErrorCode],
  props: {
    i18n: {
      type: Object,
    },
    updateEmailData: {
      type: Object,
    },
  },
  data() {
    return {
      isDuplicateMail: false,
      emailID: '',
      registrationLoginService: new RegistrationLoginService(),
      duplicateMailErrorCode,
    };
  },
  computed: {},
  mounted() {},
  methods: {
    updateEmailID() {
      this.$validator.validateAll().then((result) => {
        if (result) {
          const requestConfig = {};
          requestConfig.data = {
            fbUniqueId: this.updateEmailData.fbUniqueId,
            firstName: this.updateEmailData.firstName,
            lastName: this.updateEmailData.lastName,
            fbMobileAccount: true,
            uid: this.emailID,
          };
          this.$emit('email-update-initiate');
          this.registrationLoginService.registerUser(
            requestConfig,
            this.handleRegisterResponse,
            this.handleRegisterError,
          );
        }
      });
    },
    handleRegisterResponse(res) {
      this.$emit('email-updated');
    },
    handleRegisterError(err) {
      this.$emit('hide-spinner');
      if (
        err.response.data.errors[0].code ===
        this.duplicateMailErrorCode.DUP_EMAIL_ERROR_CODE
      ) {
        this.isDuplicateMail = true;
      }
    },
  },
};
