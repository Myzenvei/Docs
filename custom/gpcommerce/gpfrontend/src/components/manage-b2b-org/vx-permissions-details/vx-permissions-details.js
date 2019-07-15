import vxDetailsSection from '../vx-details-section/vx-details-section.vue';
import globals from '../../common/globals';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import ManageB2bOrgService from '../../common/services/manage-b2b-org-service';
import vxPermissionsForm from '../vx-permissions-form/vx-permissions-form.vue';
import vxDropdownPrimary from '../../common/vx-dropdown-primary/vx-dropdown-primary.vue';
import { userStates } from '../../common/mixins/vx-enums';

export default {
  name: 'vx-permissions-details',
  components: {
    vxDetailsSection,
    vxSpinner,
    vxPermissionsForm,
    vxModal,
    vxDropdownPrimary,
  },
  props: {
    i18n: {
      type: Object,
      required: true,
    },
    i18nUserStatus: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      globals,
      permissionCode: '',
      detailPageData: {
        unitDetailsData: {},
        detailsData: {
          displayData: {},
        }
      },
      isMounted: false,
      statusText: userStates.status,
    }
  },
  created() {

  },
  computed: {

  },
  mounted() {
    this.permissionCode = decodeURI(this.getQueryString('pDetail'));
    this.fetchPermissionDetails();
    this.isMounted = true;
  },
  methods: {

    /**
     * Get the value of a querystring
     * @param  {String} field The field to get the value of
     * @param  {String} url   The URL to get the value from (optional)
     * @return {String}       The field value
     */
    getQueryString(field, url) {
      var href = url ? url : window.location.href;
      var reg = new RegExp('[?&]' + field + '=([^&#]*)', 'i');
      var string = reg.exec(href);
      return string ? string[1] : null;
    },

    updatePermissionCode(pCode) {
      this.permissionCode = pCode;
      window.location.search = `?pDetail=${encodeURI(this.permissionCode)}`;
    },

    /**
     * Fetching business unit data
     */
    fetchPermissionDetails() {
      const manageB2bOrgService = new ManageB2bOrgService();
      manageB2bOrgService.permissionDetailsDataService({}, this.handleBusinessUnitData, this.handleBusinessUnitError, this.permissionCode);
    },
    /**
     * Handling business unit data
     * @param {object} unitData business unit data
     */
    handleBusinessUnitData(response) {
      const status = response.status;
      const data = response.data;
      if (status) {
        this.hideLoadingSpinner();
        this.detailPageData.unitDetailsData = data;
        this.generateDetailData();
      } else {
        this.hideLoadingSpinner();
        this.handleErrorCallback(data);
      }
    },
    handleBusinessUnitError(error) {

    },

    /**
     * Generating data for details section
     */
    generateDetailData() {
      const typeLabel = this.i18n.typeLabel;
      const permissionIdLabel = this.i18n.permissionIdLabel;
      const businessUnitLabel = this.i18n.businessUnitLabel;
      const estimatedAmountLabel = this.i18n.estimatedAmountLabel;
      const timePeriodLabel = this.i18n.timePeriodLabel;
      const statusLabel = this.i18n.statusLabel;

      Object.assign(this.detailPageData.detailsData, {
        code: this.detailPageData.unitDetailsData.code,
        status: this.detailPageData.unitDetailsData.active,
        estimatedAmount: this.detailPageData.unitDetailsData.value,
        currency: this.detailPageData.unitDetailsData.currency.isocode,
        timeSpanCode: this.detailPageData.unitDetailsData.b2BPermissionTypeData.code,
        businessUnitID: this.detailPageData.unitDetailsData.unit.uid,
        businessUnitName: this.detailPageData.unitDetailsData.unit.name,
        displayData: this.detailPageData.unitDetailsData.timeSpan ? {
          [typeLabel]: this.detailPageData.unitDetailsData.b2BPermissionTypeData.name,
          [permissionIdLabel]: this.detailPageData.unitDetailsData.code,
          [businessUnitLabel]: this.detailPageData.unitDetailsData.unit.name,
          [estimatedAmountLabel]: `${this.detailPageData.unitDetailsData.value}&nbsp;${this.detailPageData.unitDetailsData.currency.isocode}`,
          [timePeriodLabel]: this.detailPageData.unitDetailsData.timeSpan ? this.detailPageData.unitDetailsData.timeSpan : '',
          [statusLabel]: this.detailPageData.unitDetailsData.active ? this.i18n.enabled : this.i18n.disabled
        } : {
          [typeLabel]: this.detailPageData.unitDetailsData.b2BPermissionTypeData.name,
          [permissionIdLabel]: this.detailPageData.unitDetailsData.code,
          [businessUnitLabel]: this.detailPageData.unitDetailsData.unit.name,
          [estimatedAmountLabel]: `${this.detailPageData.unitDetailsData.value}&nbsp;${this.detailPageData.unitDetailsData.currency.isocode}`,
          [statusLabel]: this.detailPageData.unitDetailsData.active ? this.i18n.enabled : this.i18n.disabled
        },
      });
    },
    disablePermission(id) {
      this.showLoadingSpinner();
      const manageB2bOrgService = new ManageB2bOrgService();
      manageB2bOrgService.disablePermissionService({}, this.handleDisablePermission, this.handleDisablePermissionError, this.permissionCode);
    },
    enablePermissions(id) {
      this.showLoadingSpinner();
      const manageB2bOrgService = new ManageB2bOrgService();
      manageB2bOrgService.enablePermissions({}, this.handleEnablePermission, this.handleEnablePermissionError, this.permissionCode);
    },

    editPermissions(event) {
      this.$refs.editPermissionModal.open(event);
    },
    hidePermissionsForm() {
      this.$refs.editPermissionModal.close();
    },
    handleDisablePermission(response) {
      const status = response.status;
      const data = response.data;
      if (status) {
        this.$refs.disableUnitModal.close();
        this.fetchPermissionDetails();
        this.hideLoadingSpinner();
      } else {
        this.hideLoadingSpinner();
        this.handleErrorCallback(data);
      }
    },
    handleDisablePermissionError(error) {

    },

    handleEnablePermission(response) {
      const status = response.status;
      const data = response.data;
      if (status) {
        this.fetchPermissionDetails();
        this.hideLoadingSpinner();
      } else {
        this.hideLoadingSpinner();
        this.handleErrorCallback(data);
      }
    },
    handleEnablePermissionError(error) {

    },
    handleErrorCallback(errorData) {
    },
    openDisableModal(event) {
      this.$refs.disableUnitModal.open(event);
    },
    sendDisableRequest(permissionID) {
      this.disablePermission(permissionID);
    },
    submitForm() {
      this.showLoadingSpinner();
    },
    showLoadingSpinner() {
      this.$refs.spinner.showSpinner();
    },
    hideLoadingSpinner() {
      this.$refs.spinner.hideSpinner();
    },
    showStatus() {
      if (userStates.pending === this.detailsData.userApprovalStatus) {
        return this.i18nUserStatus.pending;
      } else if (this.detailsData.active) {
        return this.i18nUserStatus.enabled;
      } else {
        return this.i18nUserStatus.disabled;
      }
    }

  }
}
