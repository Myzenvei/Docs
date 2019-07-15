/* Business unit details page */
import { Validator } from 'vee-validate';

import ManageB2bOrgService from '../../common/services/manage-b2b-org-service';
import mb2bInfoDataMixin from '../../common/mixins/mb2b-info-data-mixin';
import { UserRoles, UnitDetails, UserDetails } from '../../common/mixins/vx-enums';
import globals from '../../common/globals';
import errorCodes from '../../common/error-codes';

import vxDetailsSection from '../vx-details-section/vx-details-section.vue';
import vxInfoSection from '../vx-info-section/vx-info-section.vue';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import vxCompanyDetails from '../../registration-login/vx-company-details/vx-company-details.vue';
import vxDropdownPrimary from '../../common/vx-dropdown-primary/vx-dropdown-primary.vue';
import {
  userStates
} from '../../common/mixins/vx-enums';
import detectDeviceMixin from '../../common/mixins/detect-device-mixin';
import vxUserForm from '../vx-user-form/vx-user-form.vue';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';

export default {
  name: 'vx-business-unit-detail',
  components: {
    vxDetailsSection,
    vxInfoSection,
    vxModal,
    vxCompanyDetails,
    vxDropdownPrimary,
    vxUserForm,
    vxSpinner,
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
      userStates,
      businessUnitUsers: [],
      detailPageData: {
        unitDetailsData: {},
        detailsData: {
          displayData: {},
        },
        childGroupData: [],
        adminGroupData: [],
        buyerGroupData: [],
      },
      addedUserDetails: {
        firstName: '',
        lastName: '',
        uid: '',
        parentUnit: '',
        role: '',
        leaseSigner: true,
      },
      childUnitDetails: {
        companyName: '',
        parentUnit: {
          value: '',
          label: '',
        },
      },
      parentUnitsList: [],
      limitedParentUnitsList: [],
      isEmailExists: false,
      unitId: '',
      removedInfo: {},
      removedUser: {},
      removedUserData: [],
      existingAddedTo: '',
      addedExistingData: [],
      UserRoles,
      UnitDetails,
      UserDetails,
      manageB2bOrgService: new ManageB2bOrgService(),
      user: {
        country: {
          label: '',
          value: '',
        },
        firstName: '',
        lastName: '',
        uid: '',
      },
      emailExistsError: '',
      showInviteMsg: '',
      userDetailsUrl: '',
    };
  },
  computed: {
    admins() {
      return this.detailPageData.unitDetailsData.administrators
        .filter(child => child.roles[0] === UserRoles.ADMIN)
        .map(child => child.uid);
    },
    buyers() {
      return this.detailPageData.unitDetailsData.customers
        .filter(child => child.roles[0] === UserRoles.CUSTOMER)
        .map(child => child.uid);
    },
    hasParent() {
      return this.detailPageData.unitDetailsData.parent !== undefined;
    },
    unitName() {
      let name = '';
      if (
        this.detailPageData.unitDetailsData.addresses &&
        this.detailPageData.unitDetailsData.addresses.length
      ) {
        name = this.detailPageData.unitDetailsData.addresses[0].companyName;
      } else {
        name = this.detailPageData.unitDetailsData.name;
      }
      return name;
    },
    formattedAddress() {
      let address = '';
      if (this.detailPageData.unitDetailsData.addresses) {
        address = `${
          this.detailPageData.unitDetailsData.addresses[0].country &&
          this.detailPageData.unitDetailsData.addresses[0].country.isocode
            ? this.detailPageData.unitDetailsData.addresses[0].country.isocode
            : ''
        }<br>
      ${
  this.detailPageData.unitDetailsData.addresses[0].line1
    ? this.detailPageData.unitDetailsData.addresses[0].line1
    : ''
}<br>
      ${
  this.detailPageData.unitDetailsData.addresses[0].line2
    ? this.detailPageData.unitDetailsData.addresses[0].line2
    : ''
}<br>
      ${
  this.detailPageData.unitDetailsData.addresses[0].town
    ? this.detailPageData.unitDetailsData.addresses[0].town
    : ''
},
      ${
  this.detailPageData.unitDetailsData.addresses[0].region
    ? this.detailPageData.unitDetailsData.addresses[0].region.isocode
    : ''
} ${
  this.detailPageData.unitDetailsData.addresses[0].postalCode
    ? this.detailPageData.unitDetailsData.addresses[0].postalCode
    : ''
}`;
      }
      return address;
    },
    unitInfo() {
      if (this.detailPageData.unitDetailsData.addresses) {
        return this.detailPageData.unitDetailsData.addresses[0].phone;
      }
    },
    unitDetailI18n() {
      return {
        heading: this.i18n.detailsText,
        companyLabel: this.i18n.companyName,
        addressLabel: this.i18n.addressLabel,
        infoLabel: this.i18n.infoLabel,
        editText: this.i18n.editText,
        disableText: this.i18n.disableUnitText,
        enableText: this.i18n.enableUnitText,
      };
    },
    childInfoI18n() {
      return {
        groupLabel: this.i18n.childInfogroupLabel,
        addText: this.i18n.addText,
        newText: this.i18n.newText,
        existingText: this.i18n.existingText,
        disabledText: this.i18n.disabledText,
        iconDeleteTitle: this.i18n.iconDeleteTitle,
      };
    },
    adminInfoI18n() {
      return {
        groupLabel: this.i18n.adminInfogroupLabel,
        addText: this.i18n.addText,
        newText: this.i18n.newText,
        existingText: this.i18n.existingText,
        iconDeleteTitle: this.i18n.iconDeleteTitle,
      };
    },
    buyerInfoI18n() {
      return {
        groupLabel: this.i18n.buyerInfogroupLabel,
        addText: this.i18n.addText,
        newText: this.i18n.newText,
        existingText: this.i18n.existingText,
        iconDeleteTitle: this.i18n.iconDeleteTitle,
      };
    },
    addExistingHeading() {
      return this.existingAddedTo === this.adminInfoI18n.groupLabel
        ? `${this.i18n.addExistingheading} ${this.i18n.user}`
        : `${this.i18n.addExistingheading} ${this.i18n.user}`;
    },
  },
  created() {
    this.getUnitId();
    this.fetchBusinessUnitData();
    this.fetchBusinessUnitUsers();
    this.fetchUnitsDropdownList();
  },
  mounted() {
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
          parentUnit: {
            required: this.i18n.parentUnitDropdownError,
          },
          companyName: {
            required: this.i18n.businessUnitRequiredError,
          },
        },
      },
    };
    Validator.localize(veeCustomErrorMessage);

    this.userDetailsUrl = `${this.globals.getB2BBaseURL()}${this.globals.navigations.userLanding}?${
      this.UserDetails.USER_ID
    }=`;
  },
  methods: {
    /**
     * Getting unit ID from the url
     */
    getUnitId() {
      this.unitId = this.globals.getUrlParam(UnitDetails.UNIT_ID);
    },
    /**
     * Generating data for details section
     */
    generateDetailData() {
      const companyLabel = this.unitDetailI18n.companyLabel;
      const addressLabel = this.unitDetailI18n.addressLabel;
      const infoLabel = this.unitDetailI18n.infoLabel;

      Object.assign(this.detailPageData.detailsData, {
        id: this.detailPageData.unitDetailsData.uid,
        status: this.detailPageData.unitDetailsData.active,
        parent: this.detailPageData.unitDetailsData.parent,
        displayData: {
          [companyLabel]: this.unitName,
          [addressLabel]: this.formattedAddress,
          [infoLabel]: this.unitInfo,
        },
      });
    },

    /**
     * Fetching business unit data
     */
    fetchBusinessUnitData() {
      this.manageB2bOrgService.getBusinessUnitsDataService(
        {},
        this.handleBusinessUnitData,
        this.handleBusinessUnitDataError,
        this.unitId,
      );
    },

    /**
     * Handling business unit data
     * @param {object} unitData business unit data
     */
    handleBusinessUnitData(response) {
      const status = response.status;
      const data = response.data;
      if (status) {
        this.detailPageData.unitDetailsData = data;
        this.generateDetailData();
        if (this.detailPageData.unitDetailsData.children) {
          this.generateInfoData(
            'childGroupData',
            'children',
            true,
            globals.navigations.businessUnitsLanding,
            UnitDetails.UNIT_ID,
          );
        }
        if (this.detailPageData.unitDetailsData.administrators) {
          this.generateInfoData(
            'adminGroupData',
            'administrators',
            true,
            globals.navigations.userLanding,
            UserDetails.USER_ID,
          );
        }
        if (this.detailPageData.unitDetailsData.customers) {
          this.generateInfoData(
            'buyerGroupData',
            'customers',
            true,
            globals.navigations.userLanding,
            UserDetails.USER_ID,
          );
        }
      } else {
        this.handleErrorCallback(data);
      }
    },
    handleBusinessUnitDataError(error) {},

    fetchBusinessUnitUsers() {
      this.manageB2bOrgService.getB2bUsersService(
        {},
        this.handleBusinessUnitUsersData,
        this.handleBusinessUnitUsersDataError,
      );
    },

    handleBusinessUnitUsersData(response) {
      const status = response.status;
      const data = response.data;
      const self = this;
      if (status) {
        this.businessUnitUsers = data.users.filter(child => child.displayUid !== self.globals.uid);
      } else {
        this.handleErrorCallback(data);
      }
    },
    handleBusinessUnitUsersDataError(error) {},

    fetchUnitsDropdownList() {
      this.manageB2bOrgService.getBusinessUnits(
        {},
        this.handleUnitsDropdownResponse,
        this.handleUnitsDropdownError,
        '',
      );
      this.manageB2bOrgService.getBusinessUnits(
        {},
        this.handleLimitedUnitsDropdownResponse,
        this.handleLimitedUnitsDropdownError,
        '',
        true,
      );
    },

    handleUnitsDropdownResponse(response) {
      const status = response.status;
      const data = response.data;
      if (status) {
        this.parentUnitsList = data.units.map(child => ({
          label: child.name,
          value: child.id,
        }));
      }
    },
    handleUnitsDropdownError(error) {},
    handleLimitedUnitsDropdownResponse(response) {
      const status = response.status;
      const data = response.data;
      if (status) {
        this.limitedParentUnitsList = data.units.map(child => ({
          label: child.name,
          value: child.id,
        }));
      }
    },
    handleLimitedUnitsDropdownError(error) {},

    handleErrorCallback(errorData) {
    },

    handleUserDetailsResponse(response) {
      const status = response.status;
      const data = response.data;
      if (status) {
        this.user.uid = data.displayUid;
        this.user.firstName = data.firstName;
        this.user.lastName = data.lastName;
        this.user.country.label = data.defaultAddress ? data.defaultAddress.country.isocode : '';
        this.user.country.value = data.defaultAddress ? data.defaultAddress.country.isocode : '';
      }
    },
    handleUserDetailsError(error) {},

    handleEditClick(event) {
      if (this.hasParent) {
        this.childUnitDetails.companyName = this.detailPageData.unitDetailsData.name;
        this.$refs.editChildUnitModal.open(event);
      } else {
        const userId = `${globals.uid}|${globals.getSiteId()}`;
        this.$refs.editCompanyDetailsModal.open(event);
        this.manageB2bOrgService.getUsersDetails(
          {},
          this.handleUserDetailsResponse,
          this.handleUserDetailsError,
          `${encodeURIComponent(userId)}`,
        );
      }
    },

    parentUnitDropdownInit() {
      this.$refs.parentUnitDropdown.setDropdownValue(this.detailPageData.unitDetailsData.unit.uid);
      this.childUnitDetails.parentUnit.value = this.detailPageData.unitDetailsData.unit.uid;
    },

    sendEditRequest() {
      this.$validator.validateAll().then((result) => {
        if (result) {
          const childUnit = encodeURI(this.childUnitDetails.companyName);
          const parentUnit = encodeURI(this.childUnitDetails.parentUnit.value);
          if (this.hasParent) {
            if (childUnit && parentUnit) {
              this.manageB2bOrgService.createChildUnitService(
                {},
                this.handleEditResponse,
                this.handleEditError,
                childUnit,
                parentUnit,
                this.detailPageData.unitDetailsData.uid,
              );
            }
          } else {
            this.closeEditParentModal();
          }
        } else {
          this.globals.setFocusByName(this.$el, this.globals.getElementName(this.errors));
        }
      });
    },

    handleEditResponse(response) {
      const status = response.status;
      const data = response.data;
      if (status) {
        this.detailPageData.unitDetailsData.name = this.childUnitDetails.companyName;
        this.detailPageData.unitDetailsData.parent = this.childUnitDetails.parentUnit.label;
        this.generateDetailData();
        this.resetValues();
        this.$refs.editChildUnitModal.close();
      }
    },
    handleEditError(error) {},
    closeEditParentModal() {
      this.fetchBusinessUnitData();
      this.generateDetailData();
      this.$refs.editCompanyDetailsModal.close();
    },

    handleDisableClick(event) {
      this.$refs.disableUnitModal.open(event);
    },

    sendDisableRequest() {
      this.manageB2bOrgService.disableUnitsService(
        {},
        (response) => {
          const status = response.status;
          const data = response.data;
          if (status) {
            this.detailPageData.unitDetailsData.active = false;
            this.generateDetailData();
            this.$refs.disableUnitModal.close();
          }
        },
        this.handleDisableUnitsError,
        this.unitId,
        false,
      );
    },

    handleEnableClick() {
      this.$refs.enableUnitModal.open();
    },

    sendEnableRequest() {
      this.manageB2bOrgService.disableUnitsService(
        {},
        (response) => {
          const status = response.status;
          const data = response.data;
          if (status) {
            this.detailPageData.unitDetailsData.active = true;
            this.generateDetailData();
            this.$refs.enableUnitModal.close();
          }
        },
        this.handleDisableUnitsError,
        this.unitId,
        true,
      );
    },
    handleDisableUnitsError(error) {},
    handleRemoveClick(removedInfo) {
      this.$refs.removeModal.open();
      this.removedInfo = removedInfo;
    },

    generateRemoveUserData() {
      this.removedUserData = [];
      if (this.removedInfo.from === this.buyerInfoI18n.groupLabel) {
        this.removedUser = this.detailPageData.unitDetailsData.customers.filter(
          child => child.uid === this.removedInfo.item.uid,
        );
        this.$set(this.removedUserData, 0, this.removedUser[0]);
      } else if (this.removedInfo.from === this.adminInfoI18n.groupLabel) {
        this.removedUser = this.detailPageData.unitDetailsData.administrators.filter(
          child => child.uid === this.removedInfo.item.uid,
        );
        this.$set(this.removedUserData, 0, this.removedUser[0]);
      }
    },

    sendRemoveRequest() {
      this.generateRemoveUserData();
      const payload = {
        users: [...this.removedUserData],
      };
      const requestConfig = {};
      requestConfig.data = payload;
      this.manageB2bOrgService.removeExistingUserService(
        requestConfig,
        this.handleRemoveResponse,
        this.handleRemoveError,
        this.unitId,
      );
    },

    handleRemoveResponse(response) {
      const status = response.status;
      const data = response.data;
      if (status) {
        if (this.removedInfo.from === this.buyerInfoI18n.groupLabel) {
          const removedIndex = this.detailPageData.unitDetailsData.customers.indexOf(
            this.removedUser,
          );
          this.detailPageData.unitDetailsData.customers.splice(removedIndex, 1);
          this.generateInfoData(
            'buyerGroupData',
            'customers',
            true,
            globals.navigations.userLanding,
            UserDetails.USER_ID,
          );
        } else if (this.removedInfo.from === this.adminInfoI18n.groupLabel) {
          const removedIndex = this.detailPageData.unitDetailsData.administrators.indexOf(
            this.removedUser,
          );
          this.detailPageData.unitDetailsData.administrators.splice(removedIndex, 1);
          this.generateInfoData(
            'adminGroupData',
            'administrators',
            true,
            globals.navigations.userLanding,
            UserDetails.USER_ID,
          );
        }
        this.removedUserData = [];
        this.$refs.removeModal.close();
      }
    },
    handleRemoveError(error) {},

    handleAddExistingClick(section) {
      this.$refs.addExistingModal.open();
      this.existingAddedTo = section;
      if (this.existingAddedTo === this.buyerInfoI18n.groupLabel) {
        this.addedExistingData = [...this.buyers];
      } else if (this.existingAddedTo === this.adminInfoI18n.groupLabel) {
        this.addedExistingData = [...this.admins];
      }
    },

    generateAddExistingUserData() {
      if (this.existingAddedTo === this.buyerInfoI18n.groupLabel) {
        this.addedExistingData = this.addedExistingData.map(user => ({
          uid: user,
          roles: [this.UserRoles.CUSTOMER],
        }));
      } else if (this.existingAddedTo === this.adminInfoI18n.groupLabel) {
        this.addedExistingData = this.addedExistingData.map(user => ({
          uid: user,
          roles: [this.UserRoles.ADMIN],
        }));
      }
    },

    sendAddExistingRequest() {
      this.generateAddExistingUserData();
      const payload = {
        users: [...this.addedExistingData],
      };
      const requestConfig = {};
      requestConfig.data = payload;
      let roleLabel = '';
      if (this.existingAddedTo === this.buyerInfoI18n.groupLabel) {
        roleLabel = this.UserRoles.CUSTOMER;
      } else if (this.existingAddedTo === this.adminInfoI18n.groupLabel) {
        roleLabel = this.UserRoles.ADMIN;
      }
      this.manageB2bOrgService.addExistingUserService(
        requestConfig,
        this.handleAddExistingResponse,
        this.handleAddExistingError,
        this.unitId,
        roleLabel,
      );
    },

    handleAddExistingResponse(response) {
      const status = response.status;
      const data = response.data;
      if (status) {
        this.fetchBusinessUnitData();
        this.$refs.addExistingModal.close();
        this.resetAddedValues();
      }
    },
    handleAddExistingError(error) {},

    resetAddedValues() {
      this.addedExistingData = [];
    },

    handleAddNewUserClick(label) {
      this.isEmailExists = false;
      if (label === this.i18n.adminInfogroupLabel) {
        this.addedUserDetails.role = 'b2badmingroup';
      } else {
        this.addedUserDetails.role = 'b2bcustomergroup';
      }
      this.$refs.addUserModal.open(label);
    },

    addAdministrator() {
      this.fetchBusinessUnitData();
      this.fetchBusinessUnitUsers();
      this.resetUserValues();
      this.$refs.addUserModal.close();
    },

    handleAddChildUnit(event) {
      this.childUnitDetails.companyName = '';
      this.childUnitDetails.parentUnit = {};
      this.$refs.addChildUnitModal.open(event);
    },

    sendAddChildUnitRequest() {
      this.$validator.validateAll().then((result) => {
        if (result) {
          const childUnit = encodeURI(this.childUnitDetails.companyName);
          const parentUnit = encodeURI(this.childUnitDetails.parentUnit.value);
          if (childUnit && parentUnit) {
            this.manageB2bOrgService.createChildUnitService(
              {},
              this.handleAddChildUnitResponse,
              this.handleAddChildUnitError,
              childUnit,
              parentUnit,
            );
          }
        } else {
          this.globals.setFocusByName(this.$el, this.globals.getElementName(this.errors));
        }
      });
    },

    handleAddChildUnitResponse(response) {
      const status = response.status;
      const data = response.data;
      if (status) {
        this.fetchBusinessUnitData();
        this.manageB2bOrgService.getBusinessUnits(
          {},
          this.handleLimitedUnitsDropdownResponse,
          this.handleLimitedUnitsDropdownError,
          '',
          true,
        );
        this.generateInfoData(
          'childGroupData',
          'children',
          true,
          globals.navigations.businessUnitsLanding,
          UnitDetails.UNIT_ID,
        );
        this.resetValues();
        this.$refs.addChildUnitModal.close();
      }
    },
    handleAddChildUnitError(error) {},

    resetValues() {
      this.childUnitDetails.companyName = '';
      this.childUnitDetails.parentUnit.value = '';
    },

    resetUserValues() {
      this.addedUserDetails = {
        firstName: '',
        lastName: '',
        uid: '',
        parentUnit: '',
        role: '',
        leaseSigner: true,
      };
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
      this.$refs.addUserModal.close();
      this.generateDetailData();
    },
  },
};
