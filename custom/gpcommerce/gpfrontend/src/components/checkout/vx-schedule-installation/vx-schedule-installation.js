import _ from 'lodash';
import {
  Validator
} from 'vee-validate';
import {
  TheMask
} from 'vue-the-mask';
import fecha from 'fecha';
import DatePicker from 'vue2-datepicker';
import globals from '../../common/globals';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import vxRadioButtonGroup from '../../common/vx-radio-button-group/vx-radio-button-group.vue';
import {
  checkoutEventBus,
} from '../../../modules/event-bus';
import detectDeviceMixin from '../../common/mixins/detect-device-mixin';
import CheckoutService from '../../common/services/checkout-service';

export default {
  name: 'vx-schedule-installation',
  mixins: [detectDeviceMixin],
  components: {
    vxSpinner,
    vxRadioButtonGroup,
    TheMask,
    DatePicker,
  },
  props: {
    i18n: {
      type: Object,
    },
    installableProducts: {
      type: Object,
    },
    sectionIndex: Number,
  },
  data() {
    return {
      globals,
      installBtnLabel: '',
      showInstallationSection: false,
      showScheduleButton: true,
      disableField: true,
      customerName: '',
      customerPhone: '',
      masked: true,
      veeCustomErrorMessage: {
        en: {
          custom: {
            customerName: {
              required: this.i18n.scheduleInstallation.customerNameRequiredError,
              alpha_spaces: this.i18n.scheduleInstallation.customerNameAlphaError,
              max: this.i18n.scheduleInstallation.customerNameMaxError,
            },
            phoneNumber: {
              required: this.i18n.scheduleInstallation.phoneRequiredError,
              min: this.i18n.scheduleInstallation.phoneMinError,
            },
          },
        },
      },
      scheduledDetailsSkelton: {
        address: '',
        extraInfo: '',
        item: '',
        leadWeek: '',
        name: '',
        phoneNo: '',
        preferredDate: '',
        preferredTime: '',
      },
      scheduledDetails: {},
      disabledDates: {
        to: '',
        from: '',
      },
      dateInput: '',
      extraInfo: '',
      selectedInstallDate: '',
      radioOptions: [{
          label: 'Morning',
          value: 'morning'
        },
        {
          label: 'Afternoon',
          value: 'afternoon'
        },
      ],
      selectedDate: '',
      isSaved: false,
      isEditable: true,
      isMultipleAddress: false,
      formattedAddress: {},
      scheduledTime: '',
      checkoutService: new CheckoutService(),
    };
  },
  created() {
    Validator.localize(this.veeCustomErrorMessage);
  },
  computed: {},
  mounted() {
    this.triggerGetInstallationDetails();
  },
  methods: {
    triggerGetInstallationDetails() {
      const requestConfig = {};
      this.checkoutService.getInstallationDetails(requestConfig, this.handleGetInstallationDetailsResponse, this.handleGetInstallationDetailsError);
      this.$refs.spinner.showSpinner();
    },
    editMethod(event) {
      this.isSaved = !this.isSaved;
      this.showScheduleButton = true;
      this.isEditable = !this.isEditable;
      checkoutEventBus.$emit('section-edit', 'scheduleinstallation');
    },
    toggleViewMode() {
      this.isSaved = true;
      this.isEditable = false;
      checkoutEventBus.$emit('section-complete', 'scheduleinstallation');
    },
    handleGetInstallationDetailsResponse(response) {
      this.$refs.spinner.hideSpinner();
      this.responseObject = response.data;
      this.scheduledDetails = _.assign(
        this.scheduledDetailsSkelton,
        this.responseObject,
      );
      this.bindSavedDetails(this.scheduledDetails);
      if (this.responseObject.preferredDate) {
        this.toggleViewMode();
      }
      this.installableDisplayAddress();
    },
    handleGetInstallationDetailsError() {
      this.handleGetInstallationDetailsResponse();
      this.$refs.spinner.hideSpinner();
    },
    triggerSetInstallationDetails() {
      const requestConfig = {};
      requestConfig.data = this.createRequestbody();
      this.checkoutService.setInstallationDetails(requestConfig, this.handleSetInstallationDetailsResponse, this.handleSetInstallationDetailsError);
      this.$refs.spinner.showSpinner();
    },
    handleSetInstallationDetailsResponse() {
      this.$refs.spinner.hideSpinner();
      this.showInstallationSection = false;
      this.scheduledTime = this.$refs.scheduleTime.getSelectedRadio();
      this.selectedInstallDate = fecha.format(new Date(this.selectedDate), 'dddd MMMM Do, YYYY');
      this.showScheduleButton = false;
      this.toggleViewMode();
    },
    handleSetInstallationDetailsError() {
      this.$refs.spinner.hideSpinner();
    },
    createRequestbody() {
      const reqbody = {
        preferredDate: fecha.format(new Date(this.selectedDate), 'dddd MMMM Do, YYYY'),
        preferredTime: this.$refs.scheduleTime.getSelectedRadio(),
        name: this.scheduledDetails.name,
        phoneNo: this.scheduledDetails.phoneNo,
        extraInfo: this.scheduledDetails.extraInfo,
      };

      return reqbody;
    },
    bindSavedDetails(scheduledDetails) {
      if (scheduledDetails.preferredDate) {
        this.selectedInstallDate = scheduledDetails.preferredDate;
        this.scheduledTime = scheduledDetails.preferredTime;
        this.installBtnLabel = this.i18n.scheduleInstallation.installBtn.toUpperCase();
        this.$refs.scheduleTime.setSelectedByValue(
          scheduledDetails.preferredTime.toLowerCase(),
        );
      } else {
        this.selectedInstallDate = this.i18n.scheduleInstallation.unscheduledLabel;
        this.installBtnLabel = this.i18n.scheduleInstallation.scheduleBtn.toUpperCase();
        this.$refs.scheduleTime.setSelectedByValue(this.radioOptions[0].value);
      }
      this.setCalendarParams(
        +this.scheduledDetails.leadWeek,
        this.scheduledDetails.preferredDate,
      );
    },
    showInstallSection() {
      if (!this.showInstallationSection) {
        this.installBtnLabel = this.i18n.scheduleInstallation.installBtn.toUpperCase();
        this.showInstallationSection = true;
      } else {
        this.$validator.validateAll().then((result) => {
          if (result) {
            this.triggerSetInstallationDetails();
          } else {
            this.globals.setFocusByName(this.$el, this.globals.getElementName(this.errors));
          }
        });
      }
    },
    setCalendarParams(leadWeeks, preferredDate) {
      const today = new Date();
      const startWeek = new Date(
        today.getFullYear(),
        today.getMonth(),
        today.getDate() + 7 * leadWeeks,
      );
      const endWeek = new Date(
        today.getFullYear(),
        today.getMonth(),
        today.getDate() + 7 * (leadWeeks + 2),
      );
      this.disabledDates = {
        to: startWeek,
      };
      this.selectedDate = preferredDate
        ? fecha.parse(preferredDate, 'dddd MMMM Do, YYYY')
        : startWeek;
    },
    installableDisplayAddress() {
      if (this.scheduledDetails.address.length === 1) {
        this.isMultipleAddress = false;
        let companyName = '';
        let line2 = '';
        if (this.scheduledDetails.address[0] && this.scheduledDetails.address[0].value && this.scheduledDetails.address[0].value.companyName) {
          companyName = this.scheduledDetails.address[0].value.companyName;
        }
        if (this.scheduledDetails.address[0] && this.scheduledDetails.address[0].value && this.scheduledDetails.address[0].value.line2) {
          line2 = this.scheduledDetails.address[0].value.line2;
        }
        this.formattedAddress = {
          firstname: this.scheduledDetails.address[0].value.firstName,
          lastname: this.scheduledDetails.address[0].value.lastName,
          companyName,
          line1: this.scheduledDetails.address[0].value.line1,
          line2,
          town: this.scheduledDetails.address[0].value.town,
          region: this.scheduledDetails.address[0].value.region,
          postalCode: this.scheduledDetails.address[0].value.postalCode,
          isPallet: this.scheduledDetails.address[0].value.palletShipment,
          country: this.scheduledDetails.address[0].value.country,
        };
      } else if (this.scheduledDetails.address.length >= 1) {
        this.isMultipleAddress = true;
      }
      return this.formattedAddress;
    },
  },
};
