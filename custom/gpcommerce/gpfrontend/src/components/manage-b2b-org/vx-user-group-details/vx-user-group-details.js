import { Validator } from 'vee-validate';
import vxDropdownPrimary from '../../common/vx-dropdown-primary/vx-dropdown-primary.vue';
import globals from '../../common/globals';
import ManageB2bOrgService from '../../common/services/manage-b2b-org-service';
import mb2bInfoDataMixin from '../../common/mixins/mb2b-info-data-mixin';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import vxDetailsSection from '../vx-details-section/vx-details-section.vue';
import vxInfoSection from '../vx-info-section/vx-info-section.vue';
import { UserDetails, userStates } from '../../common/mixins/vx-enums';
import detectDeviceMixin from '../../common/mixins/detect-device-mixin';

export default {
  name: 'vx-user-group-details',
  components: {
    vxDetailsSection,
    vxInfoSection,
    ManageB2bOrgService,
    vxModal,
    vxDropdownPrimary,
  },
  mixins: [mb2bInfoDataMixin, detectDeviceMixin],
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
      userStates,
      manageB2bOrgService: '',
      detailPageData: {
        unitDetailsData: {},
        detailsData: {
          displayData: {},
        },
        permissionsGroupData: [],
        membersGroupData: [],
      },
      unitId: '',
      userGroupId: '',
      deletePayload: {},
      existingAddedTo: '',
      existingUsers: [],
      existingPermissions: [],
      removedInfo: {},
      editUserGroupData: {
        id: '',
        userGroupName: '',
        businessUnit: {},
      },
      businessUnitsList: [],
      permissionDetailsUrl: '',
      userDetailsUrl: '',
      showDisablebutton: true,
      existingItems: [],
      isUserGroupExists: false,
      isUserGroupExistsError: '',
    };
  },
  computed: {},
  created() {},
  mounted() {
    this.manageB2bOrgService = new ManageB2bOrgService();
    this.userGroupId = this.globals.getUrlParam(this.UserDetails.USER_GROUP_ID);
    this.manageB2bOrgService.getBusinessUnits(
      {},
      this.handleBusinessUnitsData,
      this.handleBusinessUnitsError,
      '',
    );
    this.manageB2bOrgService.getUserGroupDetails(
      {},
      this.handleUserGroupsData,
      this.handleUserGroupsError,
      this.userGroupId,
    );
    this.permissionDetailsUrl = `${this.globals.getB2BBaseURL()}${
      this.globals.navigations.permissionLanding
    }?pdetail=`;
    this.userDetailsUrl = `${this.globals.getB2BBaseURL()}${
      this.globals.navigations.userLanding
    }?uid=`;

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
      const idLabel = this.i18n.id;
      const userGroupNameLabel = this.i18n.userGroupName;
      const businessUnitLabel = this.i18n.businessUnit;
      const statusLabel = this.i18n.status;
      this.showDisablebutton =
        this.detailPageData.unitDetailsData.members &&
        this.detailPageData.unitDetailsData.members.length;

      Object.assign(this.detailPageData.detailsData, {
        id: this.detailPageData.unitDetailsData.uid,
        status:
          this.detailPageData.unitDetailsData.members &&
          this.detailPageData.unitDetailsData.members.length,
        parent: this.detailPageData.unitDetailsData.parent,
        displayData: {
          [idLabel]: this.detailPageData.unitDetailsData.uid,
          [userGroupNameLabel]: this.detailPageData.unitDetailsData.name,
          [businessUnitLabel]: this.detailPageData.unitDetailsData.unit.name,
          [statusLabel]:
            this.detailPageData.unitDetailsData.members &&
            this.detailPageData.unitDetailsData.members.length
              ? this.i18n.statusActive
              : this.i18n.statusDisable,
        },
      });

      Object.assign(this.editUserGroupData, {
        id: this.detailPageData.unitDetailsData.uid,
        userGroupName: this.detailPageData.unitDetailsData.name,
        businessUnit: {
          label: this.detailPageData.unitDetailsData.unit.name,
          value: this.detailPageData.unitDetailsData.unit.uid,
        },
      });

      this.manageB2bOrgService.getB2bUsersService(
        {},
        this.handleB2bUsers,
        this.handleB2bUsersError,
        this.detailPageData.unitDetailsData.unit.uid,
      );
      const permissionRequestConfig = {};
      permissionRequestConfig.data = {
        b2BUnitList: [this.detailPageData.unitDetailsData.unit.uid],
      };
      this.manageB2bOrgService.getB2bPermissionsService(
        permissionRequestConfig,
        this.handleB2bPermissions,
        this.handleB2bPermissionsError,
      );
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
              child.currency.isocode
            }<br/>${child.unit.name}`,
            link: this.permissionDetailsUrl + child.code,
            status: true,
            uid: child.code,
          }),
        );
      } else {
        this.detailPageData.permissionsGroupData = [];
      }
    },

    generateMembersInfoData() {
      if (
        this.detailPageData.unitDetailsData.members &&
        this.detailPageData.unitDetailsData.members.length
      ) {
        this.detailPageData.membersGroupData = this.detailPageData.unitDetailsData.members.map(
          child => ({
            label: child.name,
            info: child.uid,
            link: this.userDetailsUrl + encodeURI(child.uid),
            status: true,
            uid: child.uid,
          }),
        );
      } else {
        this.detailPageData.membersGroupData = [];
      }
    },

    handleBusinessUnitsData(response) {
      const status = response.status;
      const data = response.data;
      if (status) {
        this.businessUnitsList = data.units.map(this.createBusinessUnitsList);
      }
    },
    handleBusinessUnitsError(error) {},
    createBusinessUnitsList(item) {
      return {
        label: item.name,
        value: item.id,
      };
    },

    /**
     * Handling userGroup data
     * @param {object} unitData userGroup data
     */
    handleUserGroupsData(response) {
      const data = response.data;
      const status = response.status;
      if (status) {
        this.detailPageData.unitDetailsData = data;
        this.generateDetailData();
        this.generatePermissionsInfoData();
        this.generateMembersInfoData();
      } else {
        this.handleErrorCallback(data);
      }
    },
    handleUserGroupsError(error) {},
    handleErrorCallback(errorData) {
    },

    handleB2bUsers(response) {
      const status = response.status;
      const data = response.data;
      if (status) {
        this.existingUsers = data.users.map(child => ({
          label: child.name,
          info: child.uid,
          email: child.email,
          link: encodeURI(child.uid),
          status: child.active,
          isPending: child.userApprovalStatus === userStates.pending,
        }));
      }
    },
    handleB2bUsersError(error) {},

    handleB2bPermissions(response) {
      const status = response.status;
      const data = response.data;
      if (status) {
        this.existingPermissions = data.permissions.map(child => ({
          label: child.b2BPermissionTypeData.code,
          info: `<p>${child.b2BPermissionTypeData.name}</p><p>${
            child.periodRange ? child.periodRange : ''
          }</p><p>${child.value}</p><p>${child.unit.name}</p>`,
          link: child.code,
          status: child.active,
        }));
      }
    },
    handleB2bPermissionsError(error) {},

    handleDisableClick() {
      this.$refs.disableUserGroupModal.open();
    },

    handleEnableClick() {
      this.$refs.enableUserGroupModal.open();
    },

    handleEditClick() {
      this.$refs.editUserGroupModal.open();
    },

    editUserGroup() {
      this.$validator.validateAll().then((result) => {
        if (result) {
          this.isUserGroupExists = false;
          const payload = {
            unitUid: this.editUserGroupData.businessUnit.value,
            userGroupName: this.editUserGroupData.userGroupName,
            userGroupUid: this.editUserGroupData.id,
          };
          const requestConfig = {};
          requestConfig.data = payload;
          this.manageB2bOrgService.createUserGroup(
            requestConfig,
            this.handleAddUserGroup,
            this.handleUserGroupExists,
            this.detailPageData.unitDetailsData.uid,
          );
        } else {
          this.globals.setFocusByName(this.$el, this.globals.getElementName(this.errors));
        }
      });
    },

    handleAddUserGroup(response) {
      const data = response.data;
      const status = response.status;
      if (status) {
        if (this.detailPageData.unitDetailsData.uid === this.editUserGroupData.id) {
          this.detailPageData.unitDetailsData.uid = this.editUserGroupData.id;
          this.detailPageData.unitDetailsData.name = this.editUserGroupData.userGroupName;
          this.detailPageData.unitDetailsData.unit.name = this.editUserGroupData.businessUnit.label;
          this.generateDetailData();
          this.$refs.editUserGroupModal.close();
        } else {
          window.location.search = `?ugid=${this.editUserGroupData.id}`;
        }
      }
    },

    handleUserGroupExists(error) {
      const data = error.response.data;
      if (data.errors[0].message) {
        this.isUserGroupExists = true;
        this.isUserGroupExistsError = data.errors[0].message;
      }
    },

    businessUnitDropdownInit() {
      const self = this;
      this.$refs.businessUnitDropdown.setDropdownLabel(
        self.detailPageData.unitDetailsData.unit.name,
      );
      this.editUserGroupData.businessUnit.value = this.detailPageData.unitDetailsData.unit.uid;
    },

    handleDeleteClick() {
      this.$refs.deleteUserGroupModal.open();
    },

    sendDeleteRequest() {
      this.$refs.deleteUserGroupModal.close();
      this.manageB2bOrgService.postDeleteItem(
        {},
        this.handleDeleteResponse,
        this.handleDeleteError,
        this.userGroupId,
      );
    },

    handleDeleteResponse(response) {
      const status = response.status;
      const data = response.data;
      if (status) {
        this.$refs.deleteUserGroupModal.close();
        window.location.href = `${globals.getB2BBaseURL()}${globals.navigations.userGroups}`;
      }
    },
    handleDeleteError(error) {},

    handleAddExistingClick(label) {
      this.existingAddedTo = label;
      if (
        label === this.i18n.userGroups.groupLabel &&
        this.detailPageData.unitDetailsData.members
      ) {
        this.existingItems = this.detailPageData.unitDetailsData.members.map(child => child.uid);
      } else if (this.detailPageData.unitDetailsData.permissions) {
        this.existingItems = this.detailPageData.unitDetailsData.permissions.map(
          child => child.code,
        );
      }
      this.$refs.addExistingModal.open();
    },

    sendAddExistingRequest() {
      const payload = {
        codes: this.existingItems,
      };
      const requestConfig = {};
      requestConfig.data = payload;
      this.manageB2bOrgService.postExistingItems(
        requestConfig,
        this.handlePostExisting,
        this.handlePostExistingError,
        this.userGroupId,
        this.existingAddedTo,
      );
    },

    handlePostExisting(response) {
      const status = response.status;
      const data = response.data;
      if (status) {
        if (this.existingAddedTo === this.i18n.permissions.groupLabel) {
          this.generatePermissionsInfoData(data);
        } else {
          this.generateMembersInfoData(data);
        }
        this.manageB2bOrgService.getUserGroupDetails(
          {},
          this.handleUserGroupsData,
          this.handleUserGroupsError,
          this.userGroupId,
        );
        this.$refs.addExistingModal.close();
      }
    },
    handlePostExistingError(error) {},

    handleRemoveClick(groupLabel) {
      this.removedInfo = groupLabel;
      this.deletePayload = {};
      this.$refs.removeModal.open();
      this.deletePayload.userGroupId = this.detailPageData.unitDetailsData.uid;
      this.deletePayload.id = groupLabel.item.uid;
    },

    sendRemoveRequest() {
      this.manageB2bOrgService.postDeletePermission(
        {},
        this.handleItemDelete,
        this.handleItemDeleteError,
        this.removedInfo,
        this.deletePayload,
      );
    },

    sendDisableRequest() {
      this.manageB2bOrgService.postDisableItem(
        {},
        this.handleDisableResponse,
        this.handleDisableError,
        this.detailPageData.detailsData.id,
      );
    },

    handleDisableResponse(response) {
      const status = response.status;
      const data = response.data;
      if (status) {
        this.$refs.disableUserGroupModal.close();
        this.showDisablebutton = false;
        this.detailPageData.membersGroupData = [];
        this.detailPageData.unitDetailsData.members = [];
        this.generateDetailData();
      }
    },
    handleDisableError(error) {},

    sendEnableRequest() {
      const payload = {
        userGroupId: this.detailPageData.detailsData.id,
      };
      const requestConfig = {};
      requestConfig.data = payload;
      this.manageB2bOrgService.enableUser(
        requestConfig,
        this.handleEnableResponse,
        this.handleEnableError,
      );
    },

    handleEnableResponse(response) {
      const status = response.status;
      const data = response.data;
      if (status) {
        this.$refs.enableUserGroupModal.close();
      }
    },
    handleEnableError(error) {},

    handleItemDelete(response) {
      const status = response.status;
      const data = response.data;
      if (status) {
        if (this.removedInfo.from === this.i18n.permissions.groupLabel) {
          this.detailPageData.permissionsGroupData = this.detailPageData.permissionsGroupData.filter(
            child => child.uid !== this.removedInfo.item.uid,
          );
          this.detailPageData.unitDetailsData.permissions = this.detailPageData.unitDetailsData.permissions.filter(
            child => (child.uid || child.code) !== this.removedInfo.item.uid,
          );
        } else {
          this.detailPageData.membersGroupData = this.detailPageData.membersGroupData.filter(
            child => child.uid !== this.removedInfo.item.uid,
          );
          this.detailPageData.unitDetailsData.members = this.detailPageData.unitDetailsData.members.filter(
            child => (child.uid || child.uid) !== this.removedInfo.item.uid,
          );
        }
        this.generateDetailData();
        this.$refs.removeModal.close();
      }
    },
    handleItemDeleteError(error) {},

    resetAddedValues() {
      this.existingItems = [];
      this.existingAddedTo = '';
    },

    resetEditValues() {
      this.generateDetailData();
    },
  },
};
