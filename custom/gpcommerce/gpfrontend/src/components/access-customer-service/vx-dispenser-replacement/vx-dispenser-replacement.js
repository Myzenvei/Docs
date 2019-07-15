import {
  Validator,
} from 'vee-validate';
import {
  TheMask,
} from 'vue-the-mask';
import vxDropdownPrimary from '../../common/vx-dropdown-primary/vx-dropdown-primary.vue';
import globals from '../../common/globals';
import AcsService from '../../common/services/acs-service';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import flyoutBannerMixin from '../../common/vx-flyout-banner/vx-flyout-banner-mixin';
import ManageProfileShoppingListService from '../../common/services/manage-profile-shopping-lists-service';
import {
  flyoutStatus,
  booleanFlags,
  UserRoles,
  country,
} from '../../common/mixins/vx-enums';
import detectDeviceMixin from '../../common/mixins/detect-device-mixin';
import CommonService from '../../common/services/common-service';
import mobileMixin from '../../common/mixins/mobile-mixin';

export default {
  name: 'vx-dispenser-replacement',
  components: {
    vxDropdownPrimary,
    TheMask,
    vxSpinner,
  },
  mixins: [flyoutBannerMixin, detectDeviceMixin, mobileMixin],
  props: {
    i18n: {
      type: Object,
    },
  },
  data() {
    return {
      form: {
        keyQuantities: 0,
        dispenserType: '',
        streetAddress: '',
        city: '',
        companyName: '',
        country: '',
        email: '',
        firstName: '',
        lastName: '',
        phone: '',
        postalCode: '',
        region: ''
      },
      acsService: new AcsService(),
      veeCustomErrorMessage: {
        en: {
          custom: {
            firstName: {
              required: this.i18n.formErrorMsg.firstName.required,
              regex: this.i18n.formErrorMsg.firstName.regex,
            },
            lastName: {
              required: this.i18n.formErrorMsg.lastName.required,
              regex: this.i18n.formErrorMsg.lastName.regex,
            },
            email: {
              required: this.i18n.formErrorMsg.email.required,
              email: this.i18n.formErrorMsg.email.email,
            },
            address: {
              required: this.i18n.formErrorMsg.address.required,
            },
            companyName: {
              required: this.i18n.formErrorMsg.company.required,
            },
            city: {
              required: this.i18n.formErrorMsg.city.required,
            },
            state: {
              required: this.i18n.formErrorMsg.state.required,
            },
            keyQuantities: {
              required: this.i18n.formErrorMsg.keyQuantities.required,
            },
            dispenserType: {
              required: this.i18n.formErrorMsg.dispenserType.required,
            },
            zipcode: {
              required: this.i18n.formErrorMsg.zipcode.required,
              regex: this.i18n.formErrorMsg.zipcode.regex,
              numeric: this.i18n.formErrorMsg.zipcode.numeric,
              alpha_num: this.i18n.formErrorMsg.zipcode.alpha_num,
            },
            telephone: {
              required: this.i18n.formErrorMsg.phone.required,
              min: this.i18n.formErrorMsg.phone.min,
            },
          },
        },
      },
      globals,
      masked: true,
      manageProfileShoppingListService: new ManageProfileShoppingListService(),
      commonService: new CommonService(),
      regionList: [],
      isLoggedIn: false,
      dispenserKeyQuantityList: [],
      dispenserTypeValuesList: [],
      dispenserTicketData: {},
    }
  },
  created() {
    Validator.localize(this.veeCustomErrorMessage);
  },
  computed: {

  },
  mounted() {
    // call user details for logged in user to prepopulate form or set country to US if guest user
    if (this.globals.getIsLoggedIn()) {
      this.isLoggedIn = true;
      this.getUserDetails();
    } else {
      this.setDefaultCountry();
    }
    this.acsService.getDispenserTicketParams({}, this.getDispenserTicketParamsResponse, this.getDispenserTicketParamsError);
  },
  methods: {
    /**
    * This function gets user details
    */
    getUserDetails() {
      const requestConfig = {};
      this.manageProfileShoppingListService.getUserDetails(
        requestConfig,
        this.handleUserDetailsResponse,
        this.handleUserDetailsError,
      );
      this.$refs.spinner.showSpinner();
    },
    /**
    * This function handles response of user details calls and sets default values in the form for logged in users
    */
    handleUserDetailsResponse(response) {
      if (response.data) {
        this.userDetails = response.data;
        if (this.userDetails.firstName) {
          this.form.firstName = this.userDetails.firstName;
        }
        if (this.userDetails.lastName) {
          this.form.lastName = this.userDetails.lastName;
        }
        if (this.userDetails.displayUid) {
          this.form.email = this.userDetails.displayUid;
        }
        if (this.userDetails.country) {
          this.setCountry(this.userDetails.country, 'userdetails');
        } else {
          this.setDefaultCountry();
        }
        if (this.userDetails.cellPhone) {
          this.form.phone = this.userDetails.cellPhone;
        }
        if (this.globals.isB2B()) {
          if (this.userDetails.unit && this.userDetails.unit.addresses && this.userDetails.unit.addresses[0].companyName) {
            this.form.companyName = this.userDetails.unit.addresses[0].companyName;
          }
        }
      }
      this.$refs.spinner.hideSpinner();
    },
    /**
     * This function handles error of user details calls
     */
    handleUserDetailsError(error) {
      this.$refs.spinner.hideSpinner();
    },
    /**
    * This function sets country value the user selects and calls the state function based on country selected
    */
    setCountry(data, type) {
      if (type === 'userdetails') {
        this.i18n.countryList.forEach((item, index) => {
          if (item.value === data) {
            this.$refs.countryDropdown.setDropDownItem(this.i18n.countryList[index]);
            this.form.country = this.i18n.countryList[index].value;
          }
        });
        if (this.form.country) {
          this.callRegionService(data);
        } else {
          this.setDefaultCountry();
        }
      } else {
        this.form.country = data.value;
        this.callRegionService(data.value);
      }
    },
    /**
    * This function sets default country to US
    */
    setDefaultCountry() {
      this.$refs.countryDropdown.setDropDownItem(this.i18n.countryList[0]);
      this.form.country = this.i18n.countryList[0].value;
      this.callRegionService(this.form.country);
    },
    /**
     * This function gets states/region data
     */
    callRegionService(isoCode) {
      this.$refs.regionDropdown.resetDropdown();
      this.commonService.getRegions({},
        this.handleGetRegionsResponse,
        this.handleGetRegionsError,
        isoCode,
        booleanFlags.isContactUs,
      );
      this.$refs.spinner.showSpinner();
    },
    /**
     * This function handles response of states/regions call
     */
    handleGetRegionsResponse(response) {
      if (response.data) {
        this.regionList = [];
        this.$refs.spinner.hideSpinner();
        this.createRegionDropdownData(response.data.regions);
      }
    },
    /**
     * This function handles response of states/regions call
     */
    handleGetRegionsError(error) {
      this.regionList = [];
      this.$refs.spinner.hideSpinner();
    },
    /**
     * This function creates state dropdown values
     */
    createRegionDropdownData(regionData) {
      regionData.forEach((item, index) => {
        this.$set(this.regionList, index, {
          label: item.name,
          value: item.isocodeShort,
        });
      });
    },
    /**
    * This function handles response of get dispenser ticket data call and creates key quantities and dispenser type dropdown values
    */
    getDispenserTicketParamsResponse(response) {
      if (response.data && response.data.dispenserKeyQuantity.length && response.data.dispenserTypeValues.length) {
        this.dispenserTicketData.heading = response.data.headerText;
        this.dispenserTicketData.description = response.data.description;
        this.dispenserTicketData.disclaimer = response.data.disclaimer;
        response.data.dispenserKeyQuantity.forEach((item, index) => {
          this.$set(this.dispenserKeyQuantityList, index, {
            label: item,
            value: item,
          });
        });
        response.data.dispenserTypeValues.forEach((item, index) => {
          this.$set(this.dispenserTypeValuesList, index, {
            label: item,
            value: item,
          });
        });
      }
    },
    /**
     * This function handles error of get dispenser ticket data call
     */
    getDispenserTicketParamsError() {

    },
    /**
     * This function submits the form only if there are no errors in the form
     */
    handleSubmit(e) {
      e.preventDefault();
      this.$validator.validateAll().then((result) => {
        if (result) {
          const requestConfig = {};
          requestConfig.data = this.form;
          this.acsService.submitDispenserTicket(requestConfig, this.handleSubmitResponse, this.handleSubmitError);
          this.$refs.spinner.showSpinner();
        } else {
          this.globals.setFocusByName(this.$el, this.globals.getElementName(this.errors));
        }
      });
    },
    /**
     * This function handles the response of submit call and submits the attachements if there are any
     */
    handleSubmitResponse(response) {
      if (response.data) {
        this.$refs.spinner.hideSpinner();
        this.globals.navigateToUrl('home');
        this.showFlyoutNextPage(flyoutStatus.success, this.i18n.submitSuccess, true);
      }
    },

    /**
     * This function handles the error of submit call
     */
    handleSubmitError(error) {
      this.$refs.spinner.hideSpinner();
      this.showFlyout(flyoutStatus.error, this.i18n.submitFailure, true);
    },
    /**
    * This function validates zip code field
    */
    zipCodeValidation() {
      if (this.form.country === country.options[0].label) {
        return {
          required: true,
          max: 20,
          numeric: true,
        };
      } else {
        return {
          required: true,
          max: 20,
          alpha_num: true,
        };
      }
    },

  }
}
