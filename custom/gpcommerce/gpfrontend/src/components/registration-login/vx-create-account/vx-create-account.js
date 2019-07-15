import { Validator } from 'vee-validate';
// import createAccount from './create-account-i18n';
import vxDropdownPrimary from '../../common/vx-dropdown-primary/vx-dropdown-primary.vue';
import globals from '../../common/globals';
import vxCompanyDetails from '../vx-company-details/vx-company-details.vue';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import loginMixin from '../../common/mixins/login-mixin';
import kauthMixin from '../../common/mixins/kauth-mixin';
import detectDeviceMixin from '../../common/mixins/detect-device-mixin';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import vxTermsCondition from '../../common/vx-terms-condition/vx-terms-condition.vue';
import RegistrationLoginService from '../../common/services/registration-login-service';
import { globalEventBus } from '../../../modules/event-bus';
import {
  monthLiterals,
  monthCollection1,
  monthCollection2,
  genderList,
} from '../../common/mixins/vx-enums';
import _ from 'lodash';

/**
 * Register component
 */

export default {
  name: 'vx-create-account',
  mixins: [loginMixin, kauthMixin, detectDeviceMixin],
  props: {
    actionUrl: String,
    i18n: Object,
    countryMarketingValues: Object,
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
      globals,
      registrationLoginService: new RegistrationLoginService(),
      kochTokens: {},
      // i18n: createAccount,
      createAccountServerError: '',
      marketingCheckbox: {},
      monthLiterals,
      date: [],
      dateLimit: 31,
      populateMonths: [],
      dateOfBirth: '',
      countryList: [
        {
          label: 'United States of America',
          value: 'US',
        },
        {
          label: 'Canada',
          value: 'CA',
        },
        {
          label: 'Other',
          value: 'other',
        },
      ],
      genderList,
      user: {
        country: {
          label: 'United States of America',
          value: 'US',
        },
        gender: {
          label: 'Select Gender',
          value: '',
        },
        month: {
          label: 'Month',
          value: '',
        },
        date: {
          label: 'Date',
          value: '',
        },
        firstName: '',
        lastName: '',
        uid: '',
        password: '',
        b2bMailerCheck: '',
        addToMarketComm: '',
      },
      showPassword: false,
      showDateError: false,
      showMonthError: false,
    };
  },
  components: {
    vxDropdownPrimary,
    vxCompanyDetails,
    vxTermsCondition,
    vxModal,
    vxSpinner,
  },
  methods: {
    openTermsModal(event) {
      this.$refs.termsConditionModal.open(event);
    },
    togglePassword() {
      this.showPassword = !this.showPassword;
      if (this.showPassword) {
        this.$refs.password.type = 'text';
      } else {
        this.$refs.password.type = 'password';
      }
    },
    submitForm(e) {
      const self = this;
      e.preventDefault();
      if (this.user.month.value === '' && this.user.date.value !== '') {
        this.showMonthError = true;
      } else if (this.user.month.value !== '' && this.user.date.value === '') {
        this.showDateError = true;
      } else if (
        (this.user.month.value === '' && this.user.date.value === '') ||
        (this.user.month.value !== '' && this.user.date.value !== '')
      ) {
        this.showMonthError = false;
        this.showDateError = false;
      }
      this.$validator.validateAll().then((result) => {
        if (result && !this.showMonthError && !this.showDateError) {
          this.setDob();
          if (self.globals.isB2C() || globals.isEmployeeStore()) {
            self.user.b2bMailerCheck = false;
          } else {
            self.user.addToMarketComm = false;
          }

          const requestConfig = {};
          requestConfig.data = self.createRequestbody();
          self.registrationLoginService.registerUser(
            requestConfig,
            this.handleRegisterResponse,
            this.handleRegisterError,
          );
          self.$refs.spinner.showSpinner();
        } else {
          this.globals.setFocusByName(
            this.$el,
            this.globals.getElementName(this.errors),
          );
        }
      });
    },
    createRequestbody() {
      let marketingCommunications = false;
      if (this.globals.isB2B()) {
        marketingCommunications = this.user.b2bMailerCheck;
      } else {
        marketingCommunications = this.user.addToMarketComm;
      }
      const reqbody = {
        firstName: this.user.firstName,
        lastName: this.user.lastName,
        password: this.user.password,
        country: this.user.country.value,
        gender: this.user.gender.value,
        dateOfBirth: this.dateOfBirth,
        uid: this.user.uid,
        addToMarketComm: marketingCommunications,
        titleCode: 'mr',
      };
      if (this.globals.isEmployeeStore()) {
        // reqbody.token = this.getStorage(this.kochKeys.KOCHCODE) || 'token';
        reqbody.kochAuthAccessToken = this.kochTokens.kochAuthAccessToken;
        reqbody.kochAuthIdToken = this.kochTokens.kochAuthIdToken;
        reqbody.kochAuthRefreshToken = this.kochTokens.kochAuthRefreshToken;
        reqbody.kochAuthTS = this.kochTokens.kochAuthTS;
        reqbody.kochEmailId = this.kochTokens.kochEmailId;
        reqbody.isSocialLogin = false;
        reqbody.loginType = 'gpemployee';
      }
      return reqbody;
    },
    handleRegisterResponse(data) {
      this.$refs.spinner.hideSpinner();
      this.showFlyoutNextPage('success', this.i18n.registerSuccessMsg, true);
      if (this.globals.isB2C()) {
        // If employee store, delete Koch Auth data
        if (this.globals.isEmployeeStore()) {
          this.deleteKauthData();
        }
        this.submitLogin();
      } else if (this.globals.isB2B()) {
        // If it is B2B then survey details call we will generate
        // if it is success company details modal will be opened
        if (this.globals.siteConfig.createAcountMoreDetailsEnabled) {
          this.$refs.companyDetailsModal.open();
        } else {
          this.submitLogin();
        }
      }
    },
    // On submit or skip or close  we will close the modal we will send
    // data of company details / survey
    // details to company details ajax request on its response we will call login
    // company details success response we will allow person to login.

    loginRequest() {
      this.$refs.companyDetailsModal.close();
      this.submitLogin();
    },
    handleRegisterError(error) {
      this.$refs.spinner.hideSpinner();
      if (error) {
        this.createAccountServerError = this.i18n[
          error.response.data.errors[0].message
        ];
        globalEventBus.$emit('announce', this.createAccountServerError);
      }
    },
    login() {
      this.globals.navigateToUrl('login');
    },
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
    handleChangeMonth(evt) {
      switch (evt.value) {
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
      this.user.month.value = evt.value;
      if (this.showMonthError) {
        this.showMonthError = false;
      }
      this.setDateDropDown();
    },
    handleChangeDate(evt) {
      let temp = {};
      const valueLiteral = 'value';
      switch (evt.value) {
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
      this.user.date.value = evt.value;
      this.populateMonths = temp;
    },
    filterMonths(collection, property, values) {
      return _.filter(collection, (item) => _.includes(values, item[property]));
    },
    setDob() {
      if (
        this.$refs.dateDropdown &&
        this.$refs.monthDropdown &&
        this.user.date.value !== '' &&
        this.user.month.value !== ''
      ) {
        this.dateOfBirth = `${this.user.date.value}-${this.user.month.value}`;
      }
    },
    handleLoginCallback() {
      this.showFlyoutNextPage('success', 'User logged in', true);
    },
    handleCountryForMarketing(country) {
      this.marketingCheckbox = this.countryMarketingValues[country];
    },
    handleKochTokenResponse(response) {
      this.kochTokens = response.data;
    },
    handleKochTokenError(error) {},
  },
  mounted() {
    globalEventBus.$emit('announce', 'Create Account Page');
    if (this.globals.isEmployeeStore()) {
      const kochCode = this.getStorage(this.kochKeys.KOCHCODE) || 'token';
      this.registrationLoginService.getKochAuthToken(
        {},
        this.handleKochTokenResponse,
        this.handleKochTokenError,
        kochCode,
      );
    }
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
          password: {
            required: this.i18n.passwordRequiredError,
            regex: this.i18n.passwordInvalidError,
            min: this.i18n.passwordMinError,
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
    this.populateMonths = this.monthLiterals.options;
    this.setDateDropDown();
    Validator.localize(veeCustomErrorMessage);
    this.handleCountryForMarketing(this.user.country.value.toLowerCase());
    this.$refs.countryDropdown.setDropdownLabel(this.user.country.label);
    if (this.$refs.genderDropdown) {
      this.$refs.genderDropdown.setDropdownLabel(this.user.gender.label);
    }
    if (this.$refs.monthDropdown) {
      this.$refs.monthDropdown.setDropdownLabel(this.user.month.label);
    }
    if (this.$refs.dateDropdown) {
      this.$refs.dateDropdown.setDropdownLabel(this.user.date.label);
    }
    if (this.globals.isB2B()) {
      this.user.b2bMailerCheck = this.marketingCheckbox.B2BMailerCheck;
    } else {
      this.user.addToMarketComm = this.marketingCheckbox.B2CAddToMarket;
    }
  },
  watch: {
    'user.country': function() {
      this.handleCountryForMarketing(this.user.country.value.toLowerCase());
      if (this.globals.isB2B()) {
        this.user.b2bMailerCheck = this.marketingCheckbox.B2BMailerCheck;
      } else {
        this.user.addToMarketComm = this.marketingCheckbox.B2CAddToMarket;
      }
    },
  },
};
