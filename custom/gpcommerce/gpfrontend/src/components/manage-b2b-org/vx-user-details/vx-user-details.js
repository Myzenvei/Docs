import { Validator } from 'vee-validate';
import vxDropdownPrimary from '../../common/vx-dropdown-primary/vx-dropdown-primary.vue';
import globals from '../../common/globals';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import errorCodes from '../../common/error-codes';
import ManageB2bOrgService from '../../common/services/manage-b2b-org-service';
import mb2bInfoDataMixin from '../../common/mixins/mb2b-info-data-mixin';
import vxDetailsSection from '../vx-details-section/vx-details-section.vue';
import vxInfoSection from '../vx-info-section/vx-info-section.vue';
import {
  UserDetails,
  UserRoles
} from '../../common/mixins/vx-enums';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import detectDeviceMixin from '../../common/mixins/detect-device-mixin';
import mobileMixin from '../../common/mixins/mobile-mixin';
import vxUserForm from '../vx-user-form/vx-user-form.vue';

export default {
  name: 'vx-user-details',
  components: {
    vxDetailsSection,
    vxInfoSection,
    ManageB2bOrgService,
    vxModal,
    vxDropdownPrimary,
    vxSpinner,
    vxUserForm,
  },
  mixins: [mb2bInfoDataMixin, detectDeviceMixin, mobileMixin],
  props: {
    i18n: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      globals,
      UserDetails,
      UserRoles,
      errorCodes,
      showResetPasswordMsg: false,
      manageB2bOrgService: '',
      deleteAllPermissions: false,
      detailPageData: {
        unitDetailsData: {},
        detailsData: {
          displayData: {},
          status: '',
        },
        parentUnitGroupData: [],
        permissionsGroupData: [],
        userGroupData: [],
      },
      businessUnitsList: [],
      unitId: '',
      userId: '',
      editUserData: {
        firstName: '',
        lastName: '',
        uid: '',
        email: '',
        parentUnit: {
          label: '',
          value: '',
        },
        role: '',
        leaseSigner: '',
      },
      existingUserData: {},
      existingUserGroups: [],
      existingPermissions: [],
      existingParentUnits: [],
      removedInfo: {},
      deletePayload: {},
      existingAddedTo: '',
      existingItems: [],
      isEmailExists: false,
      permissionDetailsUrl: '',
      userGroupDetailsUrl: '',
      businessUnitDetailsUrl: '',
      parentUnitsError: false,
      showInviteMsg: false,
      helpMessage: '',
      isFormModified: true,
    };
  },
  computed: {},
  mounted() {
    this.manageB2bOrgService = new ManageB2bOrgService();
    this.userId = this.globals.getUrlParam(this.UserDetails.USER_ID);
    this.manageB2bOrgService.getBusinessUnits(
      {},
      this.handleBusinessUnitsData,
      this.handleBusinessUnitsError,
      '',
    );
    this.manageB2bOrgService.getUsersDetails(
      {},
      this.handleUserDetailsData,
      this.handleUserDetailsError,
      this.userId,
    );
    this.permissionDetailsUrl = `${this.globals.getB2BBaseURL()}${
      this.globals.navigations.permissionLanding
    }?pdetail=`;
    this.userGroupDetailsUrl = `${this.globals.getB2BBaseURL()}${
      this.globals.navigations.userGroupLanding
    }?ugid=`;
    this.businessUnitDetailsUrl = `${this.globals.getB2BBaseURL()}${
      this.globals.navigations.businessUnitsLanding
    }?unitid=`;

    const veeCustomErrorMessage = {
      en: {
        custom: {
          id: {
            required: this.i18n.idRequiredError,
          },
          userGroupName: {
            required: this.i18n.userGroupNameRequiredError,
          },
          businessUnit: {
            required: this.i18n.businessUnitDropdownError,
          },
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
        },
      },
    };
    Validator.localize(veeCustomErrorMessage);
  },
  methods: {
    /**
     * Generating data for details section
     */
    generateDetailData() {
      const userLabel = this.i18n.name;
      const emailLabel = this.i18n.email;
      const contactLabel = this.i18n.contact;
      const statusLabel = this.i18n.status;
      const roleLabel = this.i18n.role;
      let role = '';
      if (this.detailPageData.unitDetailsData.roles[0] === this.i18n.b2badmingroup) {
        role = this.detailPageData.unitDetailsData.leaseSigner
          ? this.i18n.adminWithLeaseSigner
          : this.i18n.admin;
      } else if (this.detailPageData.unitDetailsData.roles[0] === this.i18n.b2bcustomergroup) {
        role = this.detailPageData.unitDetailsData.leaseSigner
          ? this.i18n.buyerWithLeaseSigner
          : this.i18n.buyer;
      }

      Object.assign(this.detailPageData.detailsData, {
        id: this.detailPageData.unitDetailsData.uid,
        status: this.detailPageData.unitDetailsData.active,
        parent: this.detailPageData.unitDetailsData.parent,
        displayData: {
          [userLabel]: this.detailPageData.unitDetailsData.name,
          [emailLabel]: this.detailPageData.unitDetailsData.displayUid,
          [contactLabel]: this.detailPageData.unitDetailsData.cellPhone
            ? this.detailPageData.unitDetailsData.cellPhone
            : this.i18n.phoneMessage,
          [statusLabel]: this.detailPageData.unitDetailsData.active
            ? this.i18n.enabled
            : this.i18n.disabled,
          [roleLabel]: role,
        },
        userApprovalStatus: this.detailPageData.unitDetailsData.userApprovalStatus,
      });

      Object.assign(this.editUserData, {
        firstName: this.detailPageData.unitDetailsData.firstName,
        lastName: this.detailPageData.unitDetailsData.lastName,
        uid: this.detailPageData.unitDetailsData.uid,
        email: this.detailPageData.unitDetailsData.displayUid,
        oldEmail: this.detailPageData.unitDetailsData.displayUid,
        parentUnit: {
          label: this.detailPageData.unitDetailsData.unit.name,
          value: this.detailPageData.unitDetailsData.unit.uid,
        },
        role: this.detailPageData.unitDetailsData.roles[0],
        leaseSigner: this.detailPageData.unitDetailsData.leaseSigner,
        userApprovalStatus: this.detailPageData.unitDetailsData.userApprovalStatus,
      });

      this.existingUserData = Object.assign({}, this.editUserData);
    },

    generatePermissionsInfoData() {
      if (
        this.detailPageData.unitDetailsData.permissions &&
        this.detailPageData.unitDetailsData.permissions.length
      ) {
        this.detailPageData.permissionsGroupData = this.detailPageData.unitDetailsData.permissions.map(
          child => ({
            label: child.code,
            info: `${child.b2BPermissionTypeData.name}<br/>${child.value} ${
              child.currency.name
            }<br/>${child.unit.name}`,
            link: this.permissionDetailsUrl + child.code,
            status: true,
            uid: child.code,
          }),
        );
      }
    },

    generateUserGroupsInfoData() {
      if (
        this.detailPageData.unitDetailsData.permissionGroups &&
        this.detailPageData.unitDetailsData.permissionGroups.length
      ) {
        this.detailPageData.userGroupData = this.detailPageData.unitDetailsData.permissionGroups.map(
          child => ({
            label: child.name,
            info: `${child.uid}<br/>${child.unit.name}`,
            link: this.userGroupDetailsUrl + child.uid,
            status: true,
            uid: child.uid,
          }),
        );
      }
    },

    generateParentUnitsInfoData() {
      if (
        this.detailPageData.unitDetailsData.units &&
        this.detailPageData.unitDetailsData.units.length
      ) {
        this.detailPageData.parentUnitGroupData = this.detailPageData.unitDetailsData.units.map(
          child => ({
            label: child.id,
            info: child.name,
            link: this.businessUnitDetailsUrl + child.id,
            status: true,
            uid: child.id,
          }),
        );
        if (this.detailPageData.parentUnitGroupData.length > 1) {
          this.deleteAllPermissions = true;
        } else {
          this.deleteAllPermissions = false;
        }
      }
    },

    handleBusinessUnitsData(response) {
      const status = response.status;
      const data = response.data;
      if (status) {
        this.businessUnitsList = data.units.map(this.createBusinessUnitsList);
        this.existingParentUnits = data.units.map(child => ({
          id: child.id,
          name: child.name,
        }));
        const requestConfig = {};
        requestConfig.data = {
          b2BUnitList: this.businessUnitsList.map(child => child.value),
        };
        this.manageB2bOrgService.getB2bPermissionsService(
          requestConfig,
          this.handleB2bPermissions,
          this.handleB2bPermissionsError,
        );
        this.manageB2bOrgService.getB2bUserGroupsService(
          requestConfig,
          this.handleB2bUsers,
          this.handleB2bUsersError,
        );
      }
    },
    handleBusinessUnitsError(error) {},
    createBusinessUnitsList(item) {
      return {
        label: item.name,
        value: item.id,
      };
    },

    businessUnitDropdownInit() {
      const self = this;
      this.$refs.businessUnitDropdown.setDropdownLabel(
        self.detailPageData.unitDetailsData.unit.name,
      );
      this.editUserData.parentUnit.value = this.detailPageData.unitDetailsData.unit.uid;
    },

    /**
     * Handling users data
     * @param {object} unitData users data
     */
    handleUserDetailsData(response) {
      const status = response.status;
      const data = response.data;
      if (status) {
        this.detailPageData.unitDetailsData = data;
        this.generateDetailData();
        this.generateParentUnitsInfoData();
        this.generatePermissionsInfoData();
        this.generateUserGroupsInfoData();
      } else {
        this.handleErrorCallback(data);
      }
    },
    handleUserDetailsError(error) {},
    handleErrorCallback(errorData) {
    },

    handleB2bUsers(response) {
      const status = response.status;
      const data = response.data;
      if (status) {
        this.existingUserGroups = data.usergroups;
      }
    },
    handleB2bUsersError(error) {},
    handleB2bPermissions(response) {
      const status = response.status;
      const data = response.data;
      if (status) {
        this.existingPermissions = data.permissions;
      }
    },
    handleB2bPermissionsError(error) {},

    handleParentUnits(response) {
      const status = response.status;
      const data = response.data;
      if (status) {
        this.existingParentUnits = data.parentUnits;
      }
    },

    handleDisableClick() {
      this.$refs.disableUserModal.open();
    },

    sendDisableRequest() {
      this.$refs.spinner.showSpinner();
      this.manageB2bOrgService.postDisableUser(
        {},
        this.handleDisableResponse,
        this.handleDisableError,
        encodeURI(this.detailPageData.detailsData.id),
      );
    },

    handleDisableResponse(response) {
      this.$refs.spinner.hideSpinner();
      const status = response.status;
      const data = response.data;
      if (status) {
        this.detailPageData.detailsData.status = false;
        this.detailPageData.unitDetailsData.active = false;
        this.generateDetailData();
        this.$refs.disableUserModal.close();
      }
    },
    handleDisableError(error) {},

    handleEnableClick() {
      this.$refs.enableUserModal.open();
    },

    sendEnableRequest() {
      this.$refs.spinner.showSpinner();
      this.manageB2bOrgService.postenableUser(
        {},
        this.handleEnableResponse,
        this.handleEnableError,
        encodeURI(this.detailPageData.detailsData.id),
      );
    },

    handleEnableResponse(response) {
      this.$refs.spinner.hideSpinner();
      const status = response.status;
      const data = response.data;
      if (status) {
        this.detailPageData.detailsData.status = true;
        this.detailPageData.unitDetailsData.active = true;
        this.generateDetailData();
        this.$refs.enableUserModal.close();
      }
    },
    handleEnableError(error) {},

    handleEditClick() {
      this.$refs.editUserModal.open();
    },

    setUsersData(editUserData, responseData) {
      if (
        this.detailPageData.unitDetailsData.displayUid === editUserData.email &&
        !responseData.userIdUpdateFlag
      ) {
        this.detailPageData.unitDetailsData.displayUid = editUserData.email;
        this.detailPageData.unitDetailsData.name = `${editUserData.firstName} ${
                editUserData.lastName
              }`;
        this.detailPageData.unitDetailsData.firstName = editUserData.firstName;
        this.detailPageData.unitDetailsData.lastName = editUserData.lastName;
        this.detailPageData.unitDetailsData.roles[0] = editUserData.role;
        this.detailPageData.unitDetailsData.unit.name = editUserData.businessUnit.label;
        this.detailPageData.unitDetailsData.unit.uid = editUserData.businessUnit.value;
        this.detailPageData.unitDetailsData.leaseSigner = editUserData.leaseSigner;
        this.detailPageData.unitDetailsData.userApprovalStatus = editUserData.userApprovalStatus;
        this.generateDetailData();
        this.$refs.editUserModal.close();
      } else if (responseData.userIdUpdateFlag) {
        this.globals.logout();
      } else {
        const uid = `${editUserData.email && editUserData.oldEmail ? editUserData.oldEmail : editUserData.email}|${this.globals.getSiteId()}`;
        window.location.search = `?uid=${encodeURIComponent(uid)}`;
      }
    },
    handleResetPasswordClick() {
      this.$refs.spinner.showSpinner();
      this.manageB2bOrgService.resetPassword(
        {},
        this.handlePasswordReset,
        this.handlePasswordResetError,
        this.detailPageData.unitDetailsData.displayUid,
      );
    },

    handlePasswordReset(response) {
      this.$refs.spinner.hideSpinner();
      const status = response.status;
      const data = response.data;
      if (status) {
        this.showResetPasswordMsg = true;
      }
    },
    handlePasswordResetError(error) {},

    handleRemoveClick(groupLabel) {
      this.removedInfo = groupLabel;
      this.deletePayload = {
        units: [],
      };
      this.$refs.removeModal.open();
      this.deletePayload.units.push({
        id: groupLabel.item.uid,
        name: encodeURIComponent(this.detailPageData.unitDetailsData.uid),
      });
    },

    sendRemoveRequest() {
      this.$refs.spinner.showSpinner();
      let label = '';
      if (this.removedInfo.from === this.i18n.parentUnits.groupLabel) {
        label = 'parentUnit';
      } else if (this.removedInfo.from === this.i18n.permissions.groupLabel) {
        label = 'permission';
      } else {
        label = 'userGroup';
      }
      this.manageB2bOrgService.postDeleteInfoItem(
        {},
        this.handleUserDelete,
        this.handleUserDeleteError,
        label,
        this.deletePayload,
      );
    },

    handleUserDelete(response) {
      this.$refs.spinner.hideSpinner();
      const status = response.status;
      const data = response.data;
      if (status) {
        if (this.removedInfo.from === this.i18n.parentUnits.groupLabel) {
          this.detailPageData.parentUnitGroupData = this.detailPageData.parentUnitGroupData.filter(
            child => child.uid !== this.removedInfo.item.uid,
          );
          this.detailPageData.unitDetailsData.units = this.detailPageData.unitDetailsData.units.filter(
            child => child.id !== this.removedInfo.item.uid,
          );
          if (this.detailPageData.parentUnitGroupData.length < 2) {
            this.deleteAllPermissions = false;
          }
        } else if (this.removedInfo.from === this.i18n.permissions.groupLabel) {
          this.detailPageData.permissionsGroupData = this.detailPageData.permissionsGroupData.filter(
            child => child.uid !== this.removedInfo.item.uid,
          );
          this.detailPageData.unitDetailsData.permissions = this.detailPageData.unitDetailsData.permissions.filter(
            child => (child.code || child.uid) !== this.removedInfo.item.uid,
          );
        } else {
          this.detailPageData.userGroupData = this.detailPageData.userGroupData.filter(
            child => child.uid !== data.id,
          );
          this.detailPageData.unitDetailsData.permissionGroups = this.detailPageData.unitDetailsData.permissionGroups.filter(
            child => child.uid !== data.id,
          );
        }
        this.$refs.removeModal.close();
      }
    },
    handleUserDeleteError(error) {},

    handleAddExistingClick(label) {
      this.existingAddedTo = label;
      this.existingItems = [];
      if (
        label === this.i18n.userGroups.groupLabel &&
        this.detailPageData.unitDetailsData.permissionGroups
      ) {
        this.existingItems = this.detailPageData.unitDetailsData.permissionGroups.map(
          child => child.uid,
        );
      } else if (
        label === this.i18n.permissions.groupLabel &&
        this.detailPageData.unitDetailsData.permissions
      ) {
        this.existingItems = this.detailPageData.unitDetailsData.permissions.map(
          child => child.code || child.link,
        );
      } else if (this.detailPageData.unitDetailsData.units) {
        this.existingItems = this.detailPageData.unitDetailsData.units.map(child => ({
          id: child.id,
          name: child.name,
        }));
      }
      this.$refs.addExistingModal.open();
    },

    sendAddExistingRequest() {
      this.$refs.spinner.showSpinner();
      let payload = {};
      if (this.existingAddedTo === this.i18n.parentUnits.groupLabel) {
        payload = {
          units: this.existingItems,
        };
        if (this.existingItems.length) {
          this.parentUnitsError = false;
        } else {
          this.parentUnitsError = true;
          this.$refs.spinner.hideSpinner();
          return;
        }
      } else {
        payload = {
          codes: this.existingItems,
        };
      }
      const requestConfig = {};
      requestConfig.data = payload;
      this.manageB2bOrgService.postExistingItemsToUser(
        requestConfig,
        this.handlePostExisting,
        this.handlePostExistingError,
        this.userId,
        this.existingAddedTo,
      );
    },

    handlePostExisting(response) {
      this.$refs.spinner.hideSpinner();
      const status = response.status;
      const data = response.data;
      if (status) {
        let tempArr = [];
        const self = this;
        if (this.existingAddedTo === this.i18n.parentUnits.groupLabel) {
          this.detailPageData.parentUnitGroupData = [];
          this.detailPageData.unitDetailsData.units = [];
          this.existingItems.forEach((element) => {
            tempArr = self.existingParentUnits.filter(item => item.id === element.id);
            this.detailPageData.unitDetailsData.units = this.detailPageData.unitDetailsData.units.concat(
              tempArr,
            );
          });
          this.manageB2bOrgService.getUsersDetails(
            {},
            this.handleUserDetailsData,
            this.handleUserDetailsError,
            this.userId,
          );
        } else if (this.existingAddedTo === this.i18n.permissions.groupLabel) {
          this.detailPageData.permissionsGroupData = [];
          this.detailPageData.unitDetailsData.permissions = [];
          this.existingItems.forEach((element) => {
            tempArr = self.existingPermissions.filter(item => item.code === element);
            this.detailPageData.unitDetailsData.permissions = this.detailPageData.unitDetailsData.permissions.concat(
              tempArr,
            );
          });
          this.generatePermissionsInfoData();
        } else if (this.existingAddedTo === this.i18n.userGroups.groupLabel) {
          this.detailPageData.userGroupData = [];
          this.detailPageData.unitDetailsData.permissionGroups = [];
          this.existingItems.forEach((element) => {
            tempArr = self.existingUserGroups.filter(item => item.uid === element);
            this.detailPageData.unitDetailsData.permissionGroups = this.detailPageData.unitDetailsData.permissionGroups.concat(
              tempArr,
            );
          });
          this.generateUserGroupsInfoData();
        }
        this.$refs.addExistingModal.close();
      }
    },
    handlePostExistingError(error) {
      this.$refs.spinner.hideSpinner();
    },
    resetAddedValues() {
      this.existingItems = [];
      this.existingAddedTo = '';
      this.parentUnitsError = false;
    },

    showLoadingSpinner() {
      this.$refs.spinner.showSpinner();
    },
    hideLoadingSpinner() {
      this.$refs.spinner.hideSpinner();
    },
    closeUserModal() {
      this.showInviteMsg = true;
      this.isEmailExists = false;
      this.$refs.editUserModal.close();
      this.generateDetailData();
    },
  },
};
