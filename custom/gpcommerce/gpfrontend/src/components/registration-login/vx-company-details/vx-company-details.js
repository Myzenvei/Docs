import _ from 'lodash';
import { TheMask } from 'vue-the-mask';
import { Validator } from 'vee-validate';
import uniqBy from 'lodash/uniqBy';
// import './vx-company-details.less';
import vxDropdownPrimary from '../../common/vx-dropdown-primary/vx-dropdown-primary.vue';
import vxRadioButtonGroup from '../../common/vx-radio-button-group/vx-radio-button-group.vue';
import globals from '../../common/globals';
import flyoutBannerMixin from '../../common/vx-flyout-banner/vx-flyout-banner-mixin';
import companyDetailsMock from './vx-company-details-mock';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import ManageB2bOrgService from '../../common/services/manage-b2b-org-service';
import RegistrationLoginService from '../../common/services/registration-login-service';
import CommonService from '../../common/services/common-service';
import { surveyQuestionType } from '../../common/mixins/vx-enums';
import {
  countryList,
  otherCountry,
  defaultCountry
} from '../../common/mixins/vx-enums';
import vxAddressVerification from '../../common/vx-address-verification/vx-address-verification.vue';
import detectDeviceMixin from '../../common/mixins/detect-device-mixin';

/**
 * Component for company details
 */

export default {
  name: 'vx-company-details',
  mixins: [flyoutBannerMixin, detectDeviceMixin],
  components: {
    TheMask,
    vxDropdownPrimary,
    vxRadioButtonGroup,
    vxSpinner,
    vxModal,
    vxAddressVerification,
  },
  props: {
    userData: Object,
    i18n: Object,
    isEditable: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      globals,
      defaultCountry,
      companyDetailsText: companyDetailsMock,
      masked: true,
      // Initialization for company detail model feilds
      companyDetails: {
        roleDropdownOption: {
          label: '',
          value: '',
        },
        regionsData: [],
        countryData: [],
        countryList,
        country: {
          label: 'Country',
          options: [],
        },
        region: {
          label: 'State',
          options: [],
        },
        companyDetailsDecription: '',
        CompanyPhnNo: '',
        companyName: '',
        Address1: '',
        Address2: '',
        Zipcode: '',
        City: '',
        selectedSurveyOption: {
          label: '',
          value: '',
        },
        surveyemployeeDropdown: {
          label: '',
          value: '',
        },
        stateDropdownOption: {
          label: '',
          value: '',
        },
        countryDropdownOption: {
          label: '',
          value: '',
        },
      },
      surveyCode: '',
      selectedSurveySet: [],
      surveyDetails: [],
      manageB2bOrgService: new ManageB2bOrgService(),
      registrationLoginService: new RegistrationLoginService(),
      commonService: new CommonService(),
      surveyEdit: false,
      stateErrorMessage: false,
      unverifiedAddress: {},
      selectedAddress: {},
      companyNameErrorMessage: false,
      textField: {},
      surveyQuestions: [],
      surveyQuestionType,
      onFirstLoad: false,
    };
  },
  computed: {},
  created() {
    if (this.isEditable) {
      this.fetchCompanyDetailsData();
    }
  },
  mounted() {
    // const self = this;
    this.registrationLoginService.getSurveyDetails({},
      this.handlesurveyDetailsResponse,
      this.handlesurveyDetailsError,
    );
    if (this.isEditable) {
      this.fetchSurveyData();
    } else {
      this.callCountryService();
    }
    const veeCustomErrorMessage = {
      en: {
        custom: {
          companyName: {
            required: this.i18n.companyNameError,
            max: this.i18n.companyNameMaxError,
          },
          addressLine1: {
            required: this.i18n.addressLine1Error,
          },
          city: {
            required: this.i18n.cityError,
          },
          state: {
            required: this.i18n.stateError,
          },
          zipcode: {
            required: this.i18n.zipCodeError,
            regex: this.i18n.zipCodeFormatError,
          },
          companyPhnNo: {
            required: this.i18n.companyPhnNoError,
            min: this.i18n.phoneMinError,
          },
        },
      },
    };
    Validator.localize(veeCustomErrorMessage);
  },
  methods: {
    // Survey details response handling
    handlesurveyDetailsResponse(response) {
      this.$refs.spinner.hideSpinner();
      if (response.data) {
        this.surveyDetails = response.data.questionSelected;
        this.surveyQuestions.push(this.surveyDetails[0]);
        // add value attribute to options object
        if(this.surveyDetails[0].options) {
          let firstQuestionOptions = [];
          firstQuestionOptions = this.renameArrayObjectKeys(
            this.surveyDetails[0].options,
            firstQuestionOptions);
          this.surveyQuestions[0].options = firstQuestionOptions;
        }
        this.surveyCode = response.data.surveyCode;
      }
    },
    // Address Verification call
    callAddressVerification() {
      this.$validator.validateAll().then((result) => {
        if (result) {
          this.unverifiedAddress = {
            titleCode: 'mr',
            firstName: this.userData.firstName,
            line1: this.companyDetails.Address1,
            lastName: this.userData.lastName,
            phone: this.companyDetails.CompanyPhnNo,
            companyName: this.companyDetails.companyName,
            postalCode: this.companyDetails.Zipcode,
            town: this.companyDetails.City,
            country: {
              isocode: this.userData.country.value,
              name: this.userData.country.label,
            },
            region: {
              countryIso: this.userData.country.value,
              isocode: `${this.userData.country.value}-${
                this.companyDetails.stateDropdownOption.value
                }`,
              isocodeShort: this.companyDetails.stateDropdownOption.value,
              name: this.companyDetails.stateDropdownOption.label,
            },
          };
          if (this.companyDetails.Address2) {
            this.unverifiedAddress.line2 = this.companyDetails.Address2;
          }
          this.$refs.addressVerificationModal.open();
        } else {
          this.globals.setFocusByName(this.$el, this.globals.getElementName(this.errors));
        }
      });
    },
    // Company details request and response
    submitCompanyDetails() {
      let data = this.getFormData();
      if (!data) {
        data = '';
      }
      data.questionSelected = data.questionSelected.filter(function (el) {
        return el != null;
      });
      const requestConfig = {};
      requestConfig.data = data;
      this.registrationLoginService.postCompanyDetails(
        requestConfig,
        this.handlecompanyDetailsResponse,
        this.handlecompanyDetailsError,
      );
      this.$refs.spinner.showSpinner();
    },
    handlecompanyDetailsResponse(response) {
      this.$refs.spinner.hideSpinner();
      if (response) {
        this.$emit('companyDetailsubmitted');
      }
    },
    handlecompanyDetailsError(error) {
      this.$refs.spinner.hideSpinner();
      const data = error.response.data;
      const status = error.response.status;
      this.showFlyout('error', status.message);
    },
    // skip company details function
    skipCompanyDetails() {
      const requestConfig = {};
      requestConfig.data = '';
      this.registrationLoginService.postCompanyDetails(
        requestConfig,
        this.handlecompanyDetailsResponse,
        this.handlecompanyDetailsError,
      );
      this.$refs.spinner.showSpinner();
      this.$emit('skip');
    },
    // Request formation
    getFormData() {
      // TODO: Uncomment after address verification
      const formData = {
        surveyCode: this.surveyCode,
        userId: this.userData.uid,
        role: this.companyDetails.roleDropdownOption.value,
        questionSelected: this.selectedSurveySet,
        address: {
          country: this.selectedAddress.value.country,
          firstName: this.selectedAddress.value.firstName,
          line1: this.selectedAddress.value.line1,
          line2: this.selectedAddress.value.line2,
          postalCode: this.selectedAddress.value.postalCode,
          town: this.selectedAddress.value.town,
          region: this.selectedAddress.value.region,
          lastName: this.selectedAddress.value.lastName,
          phone: this.selectedAddress.value.phone,
          companyName: this.selectedAddress.value.companyName,
        },
        surveyEdit: this.surveyEdit,
      };
      return formData;
    },
    setFormData() {},
    // Rename object key
    renameObjectKeys(object, label, value) {
      const remapedObject = {
        label: object[label],
        value: object[value],
      };
      return _.assign({}, remapedObject, object);
    },
    // Rename object key in array and return that
    renameArrayObjectKeys(inputArray, outputArray) {
      if (inputArray) {
        for (let i = 0; i < inputArray.length; i++) {
          outputArray.push(this.renameObjectKeys(inputArray[i], 'label', 'code'));
        }
        return outputArray;
      }
    },
    handlesurveyDetailsError() {
      this.$refs.spinner.hideSpinner();
    },
    fetchCompanyDetailsData() {
      const userId = `${globals.uid}|${globals.getSiteId()}`;
      this.manageB2bOrgService.getUsersDetails({},
        this.handleCompanyDetailsData,
        this.handlecompanyDetailsError,
        `${encodeURIComponent(userId)}`,
      );
    },
    handleCompanyDetailsData(response) {
      const status = response.status;
      const data = response.data;
      if (status && data.unit) {
        this.surveyEdit = true;
        this.companyDetails.companyName = data.unit.name;
        if (data.unit.role) {
          this.$refs.roleDropdown.setDropdownLabel(data.unit.role);
          this.companyDetails.roleDropdownOption.value = data.unit.role;
        }
        if (data.unit.addresses && data.unit.addresses.length) {
          this.companyDetails.Address1 = data.unit.addresses[0].line1;
          this.companyDetails.Address2 = data.unit.addresses[0].line2;
          this.companyDetails.City = data.unit.addresses[0].town;
          this.companyDetails.Zipcode = data.unit.addresses[0].postalCode;
          this.companyDetails.CompanyPhnNo = data.unit.addresses[0].phone;
          if (data.unit.addresses[0].country) {
            this.$refs.countryDropdown.setDropdownLabel(data.unit.addresses[0].country.name);
            this.companyDetails.countryDropdownOption.label = data.unit.addresses[0].country.name;
            this.companyDetails.countryDropdownOption.value = data.unit.addresses[0].country.isocode;
            this.callRegionService(this.companyDetails.countryDropdownOption.value);
          }
          if (data.unit.addresses[0].region) {
            this.$refs.stateDropdown.setDropdownLabel(data.unit.addresses[0].region.name);
            this.companyDetails.stateDropdownOption.label = data.unit.addresses[0].region.name;
            this.companyDetails.stateDropdownOption.value =
              data.unit.addresses[0].region.isocodeShort;
          }
        }
      }
      this.callCountryService();
      this.onFirstLoad = true;
    },
    fetchSurveyData() {
      const payload = {
        userId: globals.uid,
      };
      const requestConfig = {};
      requestConfig.data = payload;
      this.manageB2bOrgService.getCompanySurveyData(
        requestConfig,
        this.handleSurveyData,
        this.handleSurveyError,
      );
    },
    handleSurveyData(response) {
      const data = response.data;
      data.questionSelected.sort(function(a, b){
        if (a.code.toLowerCase() < b.code.toLowerCase())
          return -1;
        else if (a.code.toLowerCase() > b.code.toLowerCase())
          return 1;
      });
      data.questionSelected.map((selectedAns, index) => {
        this.surveyDetails.map((child) => {
          if (selectedAns.code === child.code) {
            if (child.questionType === surveyQuestionType.radio) {
              this.$nextTick(function () {
                this.$refs.surveyRadioQuestion[0].radioButtonPrePopulate(selectedAns.options[0].code);
              });
              this.onSelectedBusinessGroup(selectedAns.options[0], child, index);
            }
            else if (child.questionType === surveyQuestionType.select) {
              this.$refs.surveySelectQuestion[0].setDropdownLabel(selectedAns.options[0].label);
              this.onSelectedBusinessGroup(selectedAns.options[0], child, index);
            }
            else if (child.questionType === surveyQuestionType.textField) {
              this.onSelectedBusinessGroup(selectedAns.options[0], child, index);
            }
          }
        });
      });
    },
    handleSurveyError(error) {},
    callRegionService(isoCode) {
      if (!this.onFirstLoad) {
        this.$refs.stateDropdown.resetDropdown();
      }
      this.registrationLoginService.getRegions({},
        this.handleGetRegionsResponse,
        this.handleGetRegionsError,
        isoCode,
      );
      this.$refs.spinner.showSpinner();
    },
    handleGetRegionsResponse(response) {
      this.$refs.spinner.hideSpinner();
      this.companyDetails.regionsData = response.data.regions;
      this.$set(this.companyDetails.region.options, []);
      for (let i = 0; i < this.companyDetails.regionsData.length; i += 1) {
        this.$set(this.companyDetails.region.options, i, {
          label: this.companyDetails.regionsData[i].name,
          value: this.companyDetails.regionsData[i].isocodeShort,
          countryIso: this.companyDetails.regionsData[i].countryIso,
          isocode: this.companyDetails.regionsData[i].isocode,
        });
      }
    },
    handleGetRegionsError(error) {
      this.$refs.spinner.hideSpinner();
    },
    callCountryService() {
      this.commonService.getCountries({}, this.handleGetCountriesResponse, this.handleGetCountriesError);
      this.$refs.spinner.showSpinner();
    },
    handleGetCountriesResponse(response) {
      this.$refs.spinner.hideSpinner();
      if (response && response.data) {
        this.countryData = response.data.countries;
        this.$refs.countryDropdown.resetDropdown();
        this.companyDetails.country.options = [];
        for (let i = 0; i < this.countryData.length; i += 1) {
          this.$set(this.companyDetails.country.options, i, {
            label: this.countryData[i].name,
            value: this.countryData[i].isocode,
          });
        }
        this.$refs.countryDropdown.selectedItem = {
          label: this.companyDetails.country.options[1].label,
          value: this.companyDetails.country.options[1].value,
        };
        if (this.companyDetails.countryDropdownOption.label === otherCountry.label) {
          this.$refs.countryDropdown.setDropdownLabel(defaultCountry.label);
          let defaultCountrySelected = this.companyDetails.country.options.filter(country => {
            return country.value == defaultCountry.value;
          })
          this.setDropdownOption(defaultCountrySelected[0]);
        } else if (this.isEditable) {
          this.$refs.countryDropdown.setDropdownLabel(this.companyDetails.countryDropdownOption.label);
          this.setDropdownOption(this.companyDetails.countryDropdownOption);
        } else {
          this.$refs.countryDropdown.setDropdownLabel(this.userData.country.label);
          this.setDropdownOption(this.userData.country);
        }
      }
    },
    handleGetCountriesError(error) {
      this.$refs.spinner.hideSpinner();
    },
    setDropdownOption(evt) {
      this.userData.country = evt;
      this.callRegionService(evt.value);
      this.onFirstLoad = false;
    },
    handleSelectedAddressResponse(data) {
      // TODO: Uncomment below line after address verification
      this.$refs.addressVerificationModal.close();
      this.selectedAddress = data;
      this.submitCompanyDetails();
    },
    handleSelectedAddressError() {},

    // Survey Questions Populating based on 1st question option type
    onSelectedBusinessGroup(selectedAnswer, item, index) {
      //set text field value
      if(item.questionType === surveyQuestionType.textField) {
        this.textField[item.code] = selectedAnswer.target ? selectedAnswer.target.value :selectedAnswer.code;
      }
      // Store current question's answer
      let selectedSurveySetObject = {
        code: item.code,
        options: [{
          code: selectedAnswer.code || (selectedAnswer.target ? selectedAnswer.target.value:''),
        },]
      };
      this.selectedSurveySet[index] = selectedSurveySetObject;

      // add value attribute to options object for radio buttons to work
      let nextQuestionOptions = [];
      if(this.surveyDetails[index+1] && this.surveyDetails[index+1].options) {
        nextQuestionOptions = this.renameArrayObjectKeys(
          this.surveyDetails[index+1].options,
          nextQuestionOptions);
      }

      //check for next question and show/remove on page
      if(selectedAnswer.reDirectQuestion) {
        this.surveyQuestions.splice(index+1);
        this.surveyQuestions.push(this.surveyDetails[index+1]);
        this.surveyQuestions[index+1].options = nextQuestionOptions;
        if(this.surveyQuestions[index+1].questionType === surveyQuestionType.textField) {
          this.textField[this.surveyQuestions[index+1].code] = '';
          for(var i =index+2;i<this.surveyDetails.length;i++) {
            this.surveyQuestions.push(this.surveyDetails[i]);
            this.textField[this.surveyQuestions[i].code] = '';
          }
        }
      }
      else {
        if(item.questionType != surveyQuestionType.textField) {
          this.surveyQuestions.splice(index+1);
          this.selectedSurveySet.splice(index+1);
        }
      }
    },
  },
};
