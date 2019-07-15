import {
  Validator,
} from 'vee-validate';
import {
  TheMask,
} from 'vue-the-mask';
import vxDropdownPrimary from '../../common/vx-dropdown-primary/vx-dropdown-primary.vue';
import globals from '../../common/globals';
import AcsService from '../../common/services/acs-service';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import vxContactusUpload from '../vx-contactus-upload/vx-contactus-upload.vue';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import flyoutBannerMixin from '../../common/vx-flyout-banner/vx-flyout-banner-mixin';
import ManageProfileShoppingListService from '../../common/services/manage-profile-shopping-lists-service';
import detectDeviceMixin from '../../common/mixins/detect-device-mixin';
import CommonService from '../../common/services/common-service';
import mobileMixin from '../../common/mixins/mobile-mixin';
import {
  globalEventBus,
} from '../../../modules/event-bus';
import {
  flyoutStatus,
  booleanFlags,
  UserRoles,
} from '../../common/mixins/vx-enums';

export default {
  name: 'vx-service-ticket',
  components: {
    vxDropdownPrimary,
    vxModal,
    vxContactusUpload,
    TheMask,
    vxSpinner,
  },
  mixins: [flyoutBannerMixin, detectDeviceMixin, mobileMixin],
  props: {
    /**
     * Labels, button and caption texts
     */
    i18n: {
      type: Object,
      default: {},
    },
  },
  data() {
    return {
      form: {
        streetAddress: '',
        city: '',
        ticketComments: '',
        companyName: '',
        country: '',
        email: '',
        firstName: '',
        jobTitle: '',
        lastName: '',
        orderNumber: '',
        phone: '',
        postalCode: '',
        region: '',
        topicOfInquiry: {},
      },
      attachments: [],
      acsService: new AcsService(),
      veeCustomErrorMessage: {
        en: {
          custom: {
            topicOfInquiry: {
              required: this.i18n.formErrorMsg.topicOfInquiry.required,
            },
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
            city: {
              required: this.i18n.formErrorMsg.city.required,
            },
            state: {
              required: this.i18n.formErrorMsg.state.required,
            },
            zipcode: {
              required: this.i18n.formErrorMsg.zipcode.required,
              regex: this.i18n.formErrorMsg.zipcode.regex,
              numeric: this.i18n.formErrorMsg.zipcode.numeric,
              alpha_num: this.i18n.formErrorMsg.zipcode.alpha_num,
            },
            orderId: {
              regex: this.i18n.formErrorMsg.orderId.regex,
            },
            comments: {
              required: this.i18n.formErrorMsg.comments.required,
            },
            telephone: {
              min: this.i18n.formErrorMsg.phone.min,
            },
          },
        },
      },
      globals,
      masked: true,
      userDetails: {},
      manageProfileShoppingListService: new ManageProfileShoppingListService(),
      commonService: new CommonService(),
      regionList: [],
      topicOfInquiryList: [],
      topicOfInquiry: {},
    };
  },
  created() {
    Validator.localize(this.veeCustomErrorMessage);
  },
  mounted() {
    // call user details for logged in user to prepopulate form or set country to US if guest user
    if (this.globals.getIsLoggedIn()) {
      this.getUserDetails();
    } else {
      this.setDefaultCountry();
    }

    // call to get topic of inquiry data
    this.acsService.getTopicOfInquiry({}, this.handleTopicOfInquiryResponse, this.handleTopicOfInquiryError);

    // populate order id if navigation from order details page
    const orderId = this.globals.getUrlParam('orderConfirmationNumber');
    if (this.globals.getIsLoggedIn() && orderId) {
      this.form.orderNumber = orderId;
    }
  },
  computed: {
    /**
     * This function disables file attachment functionality when 10 files are uploaded
     */
    disableUpload() {
      if (this.attachments.length === 10) {
        return true;
      }
      return false;
    },
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
          if (this.userDetails.roles && this.userDetails.roles[0]) {
            this.form.jobTitle = this.findJobTitle(this.userDetails.roles[0]);
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
        this.i18n.countryList.map((item, index) => {
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
      regionData.map((item, index) => {
        this.$set(this.regionList, index, {
          label: item.name,
          value: item.isocodeShort,
        });
      });
    },
    /**
     * This function handles response of get inquiry data call and creates topic of inquiry dropdown values
     */
    handleTopicOfInquiryResponse(response) {
      if (response.data && response.data.topicOfInquiry && response.data.topicOfInquiry.entry) {
        response.data.topicOfInquiry.entry.map((item, index) => {
          this.$set(this.topicOfInquiryList, index, {
            label: item.value,
            value: item.key,
          });
        });
        this.$refs.topicDropdown.setDropDownItem(this.topicOfInquiryList[0]);
        this.topicOfInquiry = this.topicOfInquiryList[0];
      }
    },
    /**
     * This function handles error of get inquiry data call
     */
    handleTopicOfInquiryError() {},
    /**
     * This function submits the form only if there are no errors in the form
     */
    handleSubmit(e) {
      e.preventDefault();
      this.$validator.validateAll().then((result) => {
        if (result) {
          const requestConfig = {};
          this.form.topicOfInquiry.key = this.topicOfInquiry.value;
          this.form.topicOfInquiry.value = this.topicOfInquiry.label;
          requestConfig.data = this.form;
          this.acsService.createServiceTicket(requestConfig, this.handleSubmitResponse, this.handleSubmitError);
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
        if (this.attachments.length) {
          const formData = new FormData();
          this.attachments.map((item) => {
            formData.append('file', item);
          });
          const requestConfig = {};
          requestConfig.data = formData;
          this.acsService.submitAttachments(requestConfig, this.handleSubmitAttachmentsResponse, this.handleSubmitAttachmentsError, response.data.id);
          this.$refs.spinner.showSpinner();
        } else {
          this.showFlyout(flyoutStatus.success, this.i18n.submitSuccess, true);
          this.navigateToParent();
        }
      }
    },
    /**
     * This function handles the error of submit call
     */
    handleSubmitError(error) {
      this.showFlyout(flyoutStatus.error, this.i18n.submitFailure, true);
      globalEventBus.$emit('announce', this.i18n.submitFailure);
      this.$refs.spinner.hideSpinner();
    },
    /**
     * This function handles the response of attachment submit call
     */
    handleSubmitAttachmentsResponse(response) {
      this.$refs.spinner.hideSpinner();
      this.showFlyout(flyoutStatus.success, this.i18n.submitSuccess, true);
      this.navigateToParent();
    },
    /**
     * This function handles the error of attachment submit call
     */
    handleSubmitAttachmentsError(error) {
      this.showFlyout(flyoutStatus.error, this.i18n.submitFailure, true);
      globalEventBus.$emit('announce', this.i18n.submitFailure);
      this.$refs.spinner.hideSpinner();
    },
    /**
     * This function redirects to the page from where the user navigated to contact us page
     */
    navigateToParent() {
      const orderNumber = this.globals.getUrlParam('orderConfirmationNumber');
      if (this.globals.loggedIn && orderNumber) {
        const url = `${this.globals.getNavBaseUrl()}${this.globals.navigations.orderDetails}${orderNumber}`;
        window.location = url;
      } else if (this.globals.loggedIn) {
        this.globals.navigateToUrl('supportTickets');
      } else if (!this.globals.loggedIn) {
        this.globals.navigateToUrl('home');
      }
      globalEventBus.$emit('announce', this.i18n.submitSuccess);
    },
    /**
     * This function validates zip code field
     */
    zipCodeValidation() {
      if (this.form.country === 'US') {
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
    /**
     * This function open the attach files modal
     */
    attachFiles(event) {
      this.$refs.fileAttachModal.open(event);
    },
    /**
     * This function removes the file attached from the form
     */
    removeAttachment(key, event) {
      this.attachments.splice(key, 1);
    },
    /**
     * This function listens to the response of the success file attachment modal and pushes the file to form object
     */
    fileUploaded(fileList) {
      this.attachments.push(fileList[0]);
      this.$refs.fileAttachModal.close(event);
    },
    /**
     * This function trunactec file name to 17 characters for contact us file upload
     */
    getFileName(filename) {
      const name = filename.slice(0, filename.lastIndexOf('.'));
      if (name.length > 17) {
        const extension = filename.substr(filename.lastIndexOf('.'), filename.length);
        const truncatedName = name.slice(0, 17);
        const newFileName = truncatedName + extension;
        return newFileName;
      }
      return filename;
    },
    findJobTitle(role) {
      let userRole = '';
      if (role === UserRoles.ADMINISTRATORS) {
        userRole = this.i18n.userRoles.administrators;
      } else if (role === UserRoles.BUYERS) {
        userRole = this.i18n.userRoles.buyers;
      } else if (role === UserRoles.ADMIN) {
        userRole = this.i18n.userRoles.admin;
      } else if (role === UserRoles.CUSTOMER) {
        userRole = this.i18n.userRoles.customer;
      }
      return userRole;
    },
  },
};
