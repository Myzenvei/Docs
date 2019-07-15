import { Validator } from 'vee-validate';
import vxDropdownPrimary from '../../common/vx-dropdown-primary/vx-dropdown-primary.vue';
import globals from '../../common/globals';
import ManageB2bOrgService from '../../common/services/manage-b2b-org-service';
import detectDeviceMixin from '../../common/mixins/detect-device-mixin';
import mobileMixin from '../../common/mixins/mobile-mixin';
import { userStates } from '../../common/mixins/vx-enums';
import errorCodes from '../../common/error-codes';

export default {
  name: 'vx-user-form',
  mixins: [detectDeviceMixin, mobileMixin],
  components: {
    vxDropdownPrimary,
  },
  props: {
    isEdit: {
      type: Boolean,
    },
    formData: {
      type: Object,
      default: {},
    },
    i18n: {
      type: Object,
    },
    isEmailExists: {
      type: Boolean,
    },
    businessUnitsList: {
      type: Object,
    },
    helpMessage: {
      type: String,
    },
    userRole: {
      type: String,
    },
  },
  data() {
    return {
      globals,
      isErrorMsg: '',
      reqbody: {},
      userStates,
      userDetails: {
        firstName: '',
        lastName: '',
        businessUnit: '',
        uid: '',
        role: '',
        leaseSigner: true,
        email: '',
        oldEmail: '',
      },
      payload: {},
      errorCodes,
      isFormModified: true,
      existingUserData: {},
    };
  },
  created() {},
  computed: {},
  mounted() {
    this.manageB2bOrgService = new ManageB2bOrgService();
    const veeCustomErrorMessage = {
      en: {
        custom: {
          firstName: {
            required: this.i18n.firstNameRequiredError,
            regex: this.i18n.firstNameRegexError,
            max: this.i18n.firstNameMaxError,
          },
          lastName: {
            required: this.i18n.lastNameRequiredError,
            regex: this.i18n.lastNameRegexError,
            max: this.i18n.lastNameMaxError,
          },
          email: {
            required: this.i18n.emailRequiredError,
            email: this.i18n.emailInvalidError,
          },
          businessUnit: {
            required: this.i18n.parentUnitDropdownError,
          },
        },
      },
    };
    Validator.localize(veeCustomErrorMessage);
    if (this.isEdit) {
      this.userDetails = {
        ...this.userDetails,
        firstName: this.formData.firstName,
        lastName: this.formData.lastName,
        businessUnit: this.formData.parentUnit,
        uid: this.formData.uid,
        role: this.formData.role,
        leaseSigner: this.formData.leaseSigner,
        email: this.formData.email,
        oldEmail: this.formData.oldEmail,
      };
      this.$refs.businessUnitDropdown.setDropdownValue(
        this.formData.parentUnit.value,
      );
    }
    this.existingUserData = Object.assign({}, this.userDetails);
    if (!this.userDetails.role) {
      this.userDetails.role = this.userRole;
    }
  },
  methods: {
    parentUnitDropdownMounted() {
      this.$refs.businessUnitDropdown.setDropdownLabel(this.i18n.select);
    },
    sendInvite() {
      this.$emit('showSpinner');
      const userId = `${this.userDetails.email}|${globals.getSiteId()}`;
      this.manageB2bOrgService.sendUserInvite(
        {},
        this.handleUserInvite,
        this.handleUserInviteError,
        `${encodeURIComponent(userId)}`,
      );
    },
    handleUserInvite(response) {
      this.$emit('hideSpinner');
      const status = response.status;
      if (status) {
        this.showInviteMsg = true;
        this.isEmailExists = false;
        this.resetValues();
        this.$emit('closeUserModal');
      }
    },
    handleUserInviteError() {
      this.$emit('hideSpinner');
    },
    submitForm() {
      if (this.isEdit) {
        this.isFormModified = false;
        const userDataProps = Object.getOwnPropertyNames(this.userDetails);
        for (let i = 0; i < userDataProps.length; i++) {
          if (
            typeof this.existingUserData[userDataProps[i]] !== 'object' &&
            this.existingUserData[userDataProps[i]] !==
              this.userDetails[userDataProps[i]]
          ) {
            this.isFormModified = true;
            break;
          } else if (
            typeof this.existingUserData[userDataProps[i]] === 'object' &&
            userDataProps[i] === 'businessUnit' &&
            this.existingUserData[userDataProps[i]].value !==
              this.userDetails[userDataProps[i]].value
          ) {
            this.isFormModified = true;
            break;
          }
        }
        if (this.isFormModified) {
          this.helpMessage = '';
        } else {
          return;
        }
      }
      this.$validator.validateAll().then((result) => {
        if (result) {
          this.$emit('showSpinner');
          this.payload = {
            email: this.userDetails.email,
            newEmail: this.userDetails.email,
            uid: this.userDetails.email,
            firstName: this.userDetails.firstName,
            lastName: this.userDetails.lastName,
            unit: {
              uid: this.userDetails.businessUnit.value,
            },
            selectedRole: this.userDetails.role,
            leaseSigner: this.userDetails.leaseSigner,
          };
          if (this.isEdit) {
            this.payload = {
              ...this.payload,
              email: this.userDetails.oldEmail,
              uid: decodeURIComponent(this.userDetails.uid),
            };
          }
          const requestConfig = {};
          requestConfig.data = this.payload;
          this.manageB2bOrgService.createUser(
            requestConfig,
            this.handleAddUsers,
            this.handleUsersDataError,
          );
        } else {
          this.globals.setFocusByName(
            this.$el,
            this.globals.getElementName(this.errors),
          );
        }
      });
    },

    handleAddUsers(response) {
      this.$emit('hideSpinner');
      const status = response.status;
      const data = response.data;
      if (status) {
        this.$emit('setUsers', this.userDetails, data);
      }
    },

    handleUsersDataError(error) {
      this.$emit('hideSpinner');
      const data = error.response.data;
      if (data.errors[0].code === errorCodes.EMAIL_EXISTS) {
        this.isEmailExists = true;
        this.helpMessage = this.i18n.emailExists;
      }
    },
    resetValues() {
      this.userDetails = {
        ...this.userDetails,
        firstName: '',
        lastName: '',
        uid: '',
        businessUnit: '',
        role: this.userRole,
        leaseSigner: true,
      };
      this.isEmailExists = false;
    },
  },
};
