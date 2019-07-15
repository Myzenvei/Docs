import { Validator } from 'vee-validate';
import { TheMask } from 'vue-the-mask';
import vxConfirmPassword from '../vx-confirm-password/vx-confirm-password.vue';
import messages from '../../../locale/messages';
import globals from '../../common/globals';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import {
  businessUnitLevel,
  logoutFlowCookie,
  monthLiterals,
  monthCollection1,
  monthCollection2,
  defaultGender,
  defaultMonth,
  defaultDate,
  genderList,
} from '../../common/mixins/vx-enums';
import ManageProfileShoppingListService from '../../common/services/manage-profile-shopping-lists-service';
import { globalEventBus } from '../../../modules/event-bus';
import detectDeviceMixin from '../../common/mixins/detect-device-mixin';
import _ from 'lodash';
import vxDropdownPrimary from '../../common/vx-dropdown-primary/vx-dropdown-primary.vue';

export default {
  name: 'vx-edit-personal-details',
  components: {
    TheMask,
    vxConfirmPassword,
    vxModal,
    vxSpinner,
    vxDropdownPrimary,
  },
  mixins: [detectDeviceMixin],
  props: {
    /**
     * Details of the user
     */
    userDetails: Object,
    /**
     * All copy texts coming from properties files
     */
    i18n: Object,
    isGenderEnabled: {
      type: String,
      default: 'false',
    },
    isDobEnabled: {
      type: String,
      default: 'false',
    },
  },
  data() {
    return {
      manageProfileShoppingListService: new ManageProfileShoppingListService(),
      buttonActive: true,
      messages: messages['en-US'],
      createAccountServerError: '',
      masked: true,
      globals,
      monthLiterals,
      date: [],
      dateLimit: 31,
      populateMonths: [],
      defaultGender,
      defaultMonth,
      defaultDate,
      dateOfBirth: '',
      genderList,
      user: {
        firstName: '',
        lastName: '',
        loginId: '',
        password: '',
        cellPhone: '',
        userIdUpdateFlag: false,
        gender: '',
        month: '',
        date: '',
        monthName: '',
      },
      isB2bUnit: false,
      showPassword: false,
      userPassword: '',
      showEmailInfo: false,
      businessUnitLevel,
      isEmailDuplicate: false,
      showDateError: false,
      showMonthError: false,
    };
  },
  computed: {},
  mounted() {
    this.user = {
      ...this.userDetails,
    };
    if (this.user.date) {
      this.setChangeDate();
    } else {
      this.setDateDropDown();
    }
    if (this.user.month) {
      this.setChangeMonth();
    } else {
      this.populateMonths = this.monthLiterals.options;
    }
    this.checkAccessLevel();
    /**
     * Provide Custom error messages for Vee validate conditions to run validation on input fields
     */
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
            required: this.i18n.emailInvalidError,
            email: this.i18n.emailInvalidError,
          },
          phoneNumber: {
            min: this.i18n.phoneMinError,
          },
          createAccountMonth: {
            required: this.i18n.monthRequiredError,
          },
          createAccountDay: {
            required: this.i18n.DayRequiredError,
          },
        },
      },
    };
    Validator.localize(veeCustomErrorMessage);
    if (this.$refs.genderDropdown) {
      if (this.user.hasOwnProperty('gender')) {
        this.$refs.genderDropdown.setDropdownValue(this.user.gender);
      } else {
        this.$refs.genderDropdown.setDropdownLabel(this.defaultGender.label);
        this.user.gender = '';
      }
    }

    if (this.$refs.dayDropdown) {
      if (this.user.hasOwnProperty('date')) {
        this.$refs.dayDropdown.setDropdownLabel(this.user.date);
      } else {
        this.$refs.dayDropdown.setDropdownLabel(this.defaultDate.label);
        this.user.date = '';
      }
    }
    if (this.$refs.monthDropdown) {
      if (this.user.hasOwnProperty('month')) {
        this.$refs.monthDropdown.setDropdownLabel(this.user.monthName);
      } else {
        this.$refs.monthDropdown.setDropdownLabel(this.defaultMonth.label);
        this.user.month = '';
      }
    }
  },
  methods: {
    setDateDropDown() {
      this.date = [];
      for (let i = 1; i <= this.dateLimit; i++) {
        const tempObj = {};
        if (i < 10) {
          tempObj.label = `0${i}`;
          tempObj.value = `0${i}`;
        } else {
          tempObj.label = i.toString();
          tempObj.value = i.toString();
        }
        this.date.push(tempObj);
      }
    },
    setChangeMonth() {
      switch (this.user.month) {
        case '02':
          this.dateLimit = 29;
          break;
        case '04':
        case '06':
        case '09':
        case '11':
          this.dateLimit = 30;
          break;
        default:
          this.dateLimit = 31;
      }
      if (this.showMonthError) {
        this.showMonthError = false;
      }
      this.setDateDropDown();
    },
    handleChangeMonth(evt) {
      this.user.month = evt.value;
      this.setChangeMonth();
    },
    setChangeDate() {
      let temp = {};
      const valueLiteral = 'value';
      switch (this.user.date) {
        case '31':
          temp = this.filterMonths(
            this.monthLiterals.options,
            valueLiteral,
            monthCollection1,
          );
          break;
        case '30':
          temp = this.filterMonths(
            this.monthLiterals.options,
            valueLiteral,
            monthCollection2,
          );
          break;
        default:
          temp = this.monthLiterals.options;
          break;
      }
      if (this.showDateError) {
        this.showDateError = false;
      }
      this.populateMonths = temp;
    },
    handleChangeDate(evt) {
      this.user.date = evt.value;
      this.setChangeDate();
    },
    filterMonths(collection, property, values) {
      return _.filter(collection, item => _.includes(values, item[property]));
    },
    setDob() {
      if (
        this.$refs.dayDropdown &&
        this.$refs.monthDropdown &&
        this.user.date !== '' &&
        this.user.month !== ''
      ) {
        this.dateOfBirth = `${this.user.date}-${this.user.month}`;
      }
    },
    /**
     * Shows text if user tries to change email address, triggers when input is focused
     */
    displayEmailInfo() {
      this.createAccountServerError = '';
      this.showEmailInfo = true;
    },
    /**
     * Handles Saveprofile Service call error
     * @param {Object} response
     */
    saveProfileError(error) {
      this.$refs.personalDetailsSpinner.hideSpinner();
      if (error) {
      }
    },
    /**
     * Handles Saveprofile Service call response
     * @param {Object} response
     */
    saveProfileResponse(response) {
      this.$refs.personalDetailsSpinner.hideSpinner();
      globalEventBus.$emit('updated-name', response.data.firstName);
      this.$emit('updated-personal-details', this.i18n.successPersonalDetails);
    },
    /**
     * based on the parameter it decides whether we need to call confirm-password component, since password is required to change Email.
     * @param {Boolean} isEmailUpdated
     * Sends edited user details to service call to save the changes
     */
    saveProfileDetails(isEmailUpdated) {
      const { firstName, lastName, cellPhone } = this.user;
      const requestObj = {
        firstName,
        lastName,
        uid: this.user.uid,
        gender: this.user.gender,
        dateOfBirth: this.dateOfBirth,
        cellPhone,
        userIdUpdateFlag: isEmailUpdated,
      };
      const requestConfig = {};
      requestConfig.data = requestObj;
      if (isEmailUpdated) {
        this.$refs.confirmPasswordModal.open();
      } else {
        this.manageProfileShoppingListService.saveProfileInformation(
          requestConfig,
          this.saveProfileResponse,
          this.saveProfileError,
        );
        this.$refs.personalDetailsSpinner.showSpinner();
      }
    },
    /**
     * Calls confirm password modal incase email is updated
     */
    callConfirmPasswordModal() {
      if (this.user.month === '' && this.user.date !== '') {
        this.showMonthError = true;
      } else if (this.user.month !== '' && this.user.date === '') {
        this.showDateError = true;
      } else if (
        (this.user.month === '' && this.user.date === '') ||
        (this.user.month !== '' && this.user.date !== '')
      ) {
        this.showMonthError = false;
        this.showDateError = false;
      }
      this.$validator.validateAll().then((result) => {
        if (result && !this.showMonthError && !this.showDateError) {
          if (this.user.uid) {
            this.setDob();
            if (this.userDetails.uid !== this.user.uid) {
              this.saveProfileDetails(true);
            } else {
              this.saveProfileDetails(false);
            }
          }
        } else {
          this.globals.setFocusByName(
            this.$el,
            this.globals.getElementName(this.errors),
          );
        }
      });
    },
    /**
     * Password entered during confirm-password component
     * @param {String} password
     * Emits the trigger for flyout incase password is confirmed, and then logs out the user
     */
    updatedPassword(password) {
      this.$refs.confirmPasswordModal.close();
      this.$emit('updated-personal-details', this.i18n.successPersonalDetails);
      this.globals.logout(logoutFlowCookie.emailUpdate);
    },
    /**
     * Checks access level of user, only users with certain access levels can change email address
     */
    checkAccessLevel() {
      if (
        this.user.hasOwnProperty('unit') &&
        (this.user.unit.b2bUnitLevel === businessUnitLevel.L2 ||
          this.user.unit.b2bUnitLevel === businessUnitLevel.L3)
      ) {
        this.isB2bUnit = true;
      }
    },
    checkError(error) {
      if (
        error &&
        ((error.code === '109' &&
          error.message.toLowerCase().indexOf('uniqueattributesinterceptor') >=
            0 &&
          error.message.includes(this.user.uid)) ||
          (error.code === '100' && error.message.includes(this.user.uid)))
      ) {
        this.$refs.confirmPasswordModal.close();
        this.isEmailDuplicate = true;
      }
    },
    hideEmailErrors() {
      this.isEmailDuplicate = false;
    },
  },
};
