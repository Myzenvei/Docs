import {
  Validator
} from 'vee-validate';
import vxDropdownPrimary from '../../common/vx-dropdown-primary/vx-dropdown-primary.vue';
import globals from '../../common/globals';
import ManageB2bOrgService from '../../common/services/manage-b2b-org-service';
import detectDeviceMixin from '../../common/mixins/detect-device-mixin';
import mobileMixin from '../../common/mixins/mobile-mixin';

export default {
  name: 'vx-permissions-form',
  mixins: [detectDeviceMixin, mobileMixin],
  components: {
    vxDropdownPrimary,
  },
  props: {
    isEdit: {
      type: Boolean
    },
    formData: {
      type: Object,
      default: {}
    },
    i18n: {
      type: Object
    },
    timePeriodValues: {
      type: String,
      default: 'DAY,WEEK,MONTH,QUARTER,YEAR'
    }
  },
  data() {
    return {
      globals,
      isErrorMsg: '',
      timePeriodDropdown: [],
      businessUnitsDropDown: [],
      reqbody: {},
      permissionsModel: {
        type: '',
        permissionId: '',
        bussinessUnit: '',
        timePeriod: '',
        thresholdAmount: '',
        currency: 'USD'
      },
      permissionType: [],
      currencyList: [{
        label: 'USD',
        value: 'USD'
      }]

    }
  },
  created() {},
  computed: {

  },
  mounted() {
    this.getTimePeriodValues();
    this.fetchBusinessUnitValues();
    this.fetchPermissionTypes();
    const veeCustomErrorMessage = {
      en: {
        custom: {
          permissionTypeDD: {
            required: this.i18n.permissionTypeError,
          },
          permissionId: {
            required: this.i18n.permissionIdError,
          },
          timePeriod: {
            required: this.i18n.timePeriodError,
          },
          thresholdAmount: {
            required: this.i18n.thresholdAmountError,
          },
          currencyType: {
            required: this.i18n.currencyTypeError,
          },
          businessUnit: {
            required: this.i18n.businessUnitError
          }
        },
      },
    };
    Validator.localize(veeCustomErrorMessage);
    if (this.$refs.permissionTypeDropdown) {
      this.$refs.permissionTypeDropdown.setDropdownLabel(this.i18n.permissionTypeLabel);
    }
    this.$refs.businessUnitDropdown.setDropdownLabel(this.i18n.businessUnitLabel);
    if (this.isEdit) {
      this.$refs.businessUnitDropdown.setDropdownLabel(this.formData.businessUnitName);
    }
    if (this.permissionsModel.type.value) {
      this.$refs.timePeriodDropdown.setDropdownLabel(this.i18n.timePeriodDropdownLabel);
    }
    this.$refs.currencyDropdown.setDropdownLabel(this.i18n.defaultCurrency);
    if (this.isEdit) {
      this.$refs.currencyDropdown.setDropdownLabel(this.formData['currency']);
      this.$nextTick(() => {
        if (this.$refs.timePeriodDropdown)
          this.$refs.timePeriodDropdown.setDropdownLabel(this.formData.displayData['TIME PERIOD']);
      });
    }

    if (this.isEdit) {
      this.permissionsModel = { ...this.permissionsModel,
        type: this.formData.displayData['PERMISSION TYPE'],
        code: this.formData.timeSpanCode,
        permissionId: this.formData.displayData['PERMISSION ID'],
        bussinessUnit: this.formData.businessUnitID,
        timePeriod: this.formData.displayData['TIME PERIOD'],
        thresholdAmount: this.formData['estimatedAmount'],
        currency: this.formData['currency']
      }
    } else {
    }


  },
  methods: {
    getTimePeriodValues() {
      this.timePeriodValues.split(',').map((val, index) => {
        let obj = {};
        obj.label = val;
        obj.value = val;
        this.timePeriodDropdown.push(obj);
      })
    },
    fetchPermissionTypes() {
      this.permissionType = [{
        label: `${this.i18n.thresholdLabel}`,
        value: `${this.i18n.thresholdValue}`
      }, {
        label: `${this.i18n.timespanLabel}`,
        value: `${this.i18n.timeSpanPermissionValue}`
      }]
    },
    fetchBusinessUnitValues() {
      const mb2BService = new ManageB2bOrgService();
      this.$emit('showSpinner');
      mb2BService.getBusinessUnits({}, this.onBusinessUnitsResponse, this.onBusinessUnitsError, '');
    },
    onBusinessUnitsResponse(response) {
      const status = response.status;
      const data = response.data;
      if (status && data.units) {
        this.getBusinessUnitDDvalues(data.units);
        this.$emit('hideSpinner');
      }
    },
    onBusinessUnitsError(error) {

    },
    getBusinessUnitDDvalues(data) {
      data.map((val, index) => {
        let obj = {};
        obj.label = val.name;
        obj.value = val.id;
        this.businessUnitsDropDown.push(obj);
      })

    },
    submitForm() {
      this.$validator.validateAll().then((result) => {
        if (result) {
          this.$emit('showSpinner');
          this.updatePermissionsDetails();
        } else {
          this.globals.setFocusByName(this.$el, this.globals.getElementName(this.errors));
        }
      });
    },
    updatePermissionsDetails() {
      if (this.permissionsModel.type === 'B2BOrderThresholdTimespanPermission' || this.permissionsModel.type === `${this.i18n.timeSpanPermission}`) {
        this.reqBody = {
          ...this.reqBody,
          "active": true,
          "b2BPermissionTypeData": {
            "code": `${this.i18n.timeSpanPermissionValue}`
          },
          "code": this.permissionsModel.permissionId,
          "currency": {
            "isocode": this.permissionsModel.currency
          },
          "unit": {
            "uid": this.permissionsModel.bussinessUnit.value ? this.permissionsModel.bussinessUnit.value : this.permissionsModel.bussinessUnit
          },
          "value": this.permissionsModel.thresholdAmount,
          "periodRange": this.permissionsModel.timePeriod
        }
      } else {
        this.reqBody = {
          ...this.reqBody,
          "active": true,
          "b2BPermissionTypeData": {
            "code": `${this.i18n.thresholdValue}`
          },
          "code": this.permissionsModel.permissionId,
          "currency": {
            "isocode": this.permissionsModel.currency
          },
          "unit": {
            "uid": this.permissionsModel.bussinessUnit.value ? this.permissionsModel.bussinessUnit.value : this.permissionsModel.bussinessUnit
          },
          "value": this.permissionsModel.thresholdAmount
        }
      }
      if (this.isEdit) {
        this.reqbody = {
          ...this.reqBody,
          "code": this.permissionsModel.permissionId
        };
        this.reqBody.originalCode = this.formData.code;
      }
      const manageB2bOrgService = new ManageB2bOrgService();
      const requestConfig = {};
      requestConfig.data = this.reqBody;
      manageB2bOrgService.updatePermissions(requestConfig, this.updateResponse, this.handleError, this.isEdit);
    },
    updateResponse(response) {
      this.$emit('hideSpinner');
      const status = response.status;
      const data = response.data;
      if (status) {
        this.$emit('fetchPDetails');
        if (this.isEdit && (this.formData.code !== this.permissionsModel.permissionId)) {
          this.$emit('setPCode', this.permissionsModel.permissionId);
        } else {
          window.location = `${globals.getNavBaseUrl()}${globals.navigations.permissionLanding}?pDetail=${this.permissionsModel.permissionId}`;
        }
      } else {
        this.handleErrorCallback(data);
      }
    },
    handleError(error) {
      const data = error.response.data;
      this.isErrorMsg = data.errors[0].message ? data.errors[0].message : '';
      this.$emit('hideSpinner');
    },
    setTimeperiodLabel(data) {
      this.$set(this.permissionsModel, 'type', data.value);
      this.$nextTick(() => {
        if (this.$refs.timePeriodDropdown)
          this.$refs.timePeriodDropdown.setDropdownLabel(this.i18n.timePeriodDropdownLabel);
      });
    }
  }
}
