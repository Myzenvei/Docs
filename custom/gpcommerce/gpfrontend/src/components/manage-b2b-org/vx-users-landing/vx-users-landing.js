import $ from 'jquery';
import dt from 'datatables.net';
import { Validator } from 'vee-validate';
import globals from '../../common/globals';
import errorCodes from '../../common/error-codes';
import mobileMixin from '../../common/mixins/mobile-mixin';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import vxDropdownPrimary from '../../common/vx-dropdown-primary/vx-dropdown-primary.vue';
import ManageB2bOrgService from '../../common/services/manage-b2b-org-service';
import { userStates, UserRoles } from '../../common/mixins/vx-enums';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import detectDeviceMixin from '../../common/mixins/detect-device-mixin';
import vxUserForm from '../vx-user-form/vx-user-form.vue';
import dataTableUtilsMixin from '../../common/mixins/data-table-utils-mixin';

export default {
  name: 'vx-users-landing',
  components: {
    vxDropdownPrimary,
    vxModal,
    ManageB2bOrgService,
    vxSpinner,
    vxUserForm,
  },
  props: {
    i18n: {
      type: Object,
      required: true,
    },
  },
  mixins: [mobileMixin, detectDeviceMixin, dataTableUtilsMixin],
  data() {
    return {
      globals,
      errorCodes,
      UserRoles,
      userStates,
      manageB2bOrgService: '',
      tableTitles: [],
      sortOptions: this.i18n.mobileSortOptions,
      userDetails: {
        firstName: '',
        lastName: '',
        uid: '',
        businessUnit: '',
        role: 'b2bcustomergroup',
        leaseSigner: true,
      },
      users: {},
      businessUnitsList: [],
      isEmailExists: false,
      showInviteMsg: false,
      userDetailsUrl: '',
    };
  },
  computed: {},
  mounted() {
    this.$refs.spinner.showSpinner();
    this.manageB2bOrgService = new ManageB2bOrgService();
    this.manageB2bOrgService.getUsersData(
      {},
      this.handleUsersData,
      this.handleUsersError,
    );
    this.manageB2bOrgService.getBusinessUnits(
      {},
      this.handleBusinessUnitsData,
      this.handleBusinessUnitsError,
      '',
    );
    this.userDetailsUrl = `${this.globals.getB2BBaseURL()}${
      this.globals.navigations.userDetails
    }?uid=`;

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
          businessUnit: {
            required: this.i18n.parentUnitDropdownError,
          },
        },
      },
    };
    Validator.localize(veeCustomErrorMessage);
  },

  methods: {
    handleUsersData(response) {
      this.$refs.spinner.hideSpinner();
      const status = response.status;
      const data = response.data;
      if (status) {
        this.users = data.users;
        this.initializeTable(data, this.i18n);
      }
    },
    handleUsersError(error) {
      console.log(error);
      this.$refs.spinner.hideSpinner();
    },
    initializeTable(usersData, i18n) {
      this.tableTitles = this.i18n.tableHeaders.split(',').map(ele => ({
        title: ele.trim(),
      }));
      const self = this;
      if ($.fn.dataTable.isDataTable($(self.$refs.usersTable))) {
        $(self.$refs.usersTable)
          .DataTable()
          .clear()
          .rows.add(usersData.users)
          .draw();
        return;
      }
      $(this.$refs.usersTable).DataTable({
        searching: false,
        lengthChange: false,
        info: false,
        defaultContent: '',
        language: {
          emptyTable: this.i18n.noDataAvailable,
        },
        order: [],
        columnDefs: [
          {
            targets: 0,
            title: this.tableTitles[0].title,
            createdCell: (cell, cellData, rowData, rowIndex, colIndex) => {
              cell.setAttribute('data-title', 'First Name');
            },
          },
          {
            title: this.tableTitles[1].title,
            targets: 1,
            createdCell: (cell, cellData, rowData, rowIndex, colIndex) => {
              cell.setAttribute('data-title', 'Last Name');
            },
          },
          {
            title: this.tableTitles[2].title,
            targets: 2,
            createdCell: (cell, cellData, rowData, rowIndex, colIndex) => {
              cell.setAttribute('data-title', 'Role');
            },
          },
          {
            title: this.tableTitles[3].title,
            bSortable: false,
            targets: 3,
            createdCell: (cell, cellData, rowData, rowIndex, colIndex) => {
              cell.setAttribute('data-title', 'Parent Unit');
            },
          },
          {
            title: this.tableTitles[4].title,
            targets: 4,
            createdCell: (cell, cellData, rowData, rowIndex, colIndex) => {
              cell.setAttribute('data-title', 'Status');
            },
          },
        ],
        data: usersData.users,
        columns: [
          {
            data: null,
            render(data, type, row, meta) {
              let name = '';
              if (
                ((userStates.pending === data.userApprovalStatus) || (userStates.rejected === data.userApprovalStatus)) &&
                !data.edited
              ) {
                name = `${data.firstName}<div class="review">${
                  userStates.underReview
                }</div>`;
              } else {
                name = `<a href="${globals.getB2BBaseURL()}${
                  globals.navigations.userLanding
                }?uid=${encodeURI(data.uid)}">${data.firstName}</a>`;
                if (data.edited) {
                  name += `<div class="review">${userStates.underReview}</div>`;
                }
              }
              return name;
            },
          },
          {
            data: null,
            render(data, type, row, meta) {
              let name = '';
              if (
            	((userStates.pending === data.userApprovalStatus) || (userStates.rejected === data.userApprovalStatus)) &&
                !data.edited
              ) {
                name = `${data.lastName}`;
              } else {
                name = `<a href="${globals.getB2BBaseURL()}${
                  globals.navigations.userLanding
                }?uid=${encodeURI(data.uid)}">${data.lastName}</a>`;
              }
              return name;
            },
          },
          {
            data: null,
            render(data, type, row) {
              let role = '';
              if (data.roles[0] === i18n.b2badmingroup) {
                role = data.leaseSigner
                  ? i18n.adminWithLeaseSigner
                  : i18n.admin;
              } else if (data.roles[0] === i18n.b2bcustomergroup) {
                role = data.leaseSigner
                  ? i18n.buyerWithLeaseSigner
                  : i18n.buyer;
              }
              return role;
            },
          },
          {
            data: null,
            render(data, type, row) {
              let parentUnits = '';
              for (let i = 0; i < data.units.length; i++) {
                if (i > 0) {
                  parentUnits += '<div class="mt-xs-3"></div>';
                }
                if (((userStates.pending === data.userApprovalStatus) || (userStates.rejected === data.userApprovalStatus)) && !data.edited) {
                  parentUnits += data.units[i].name;
                } else {
                  parentUnits += `<a href="${globals.getB2BBaseURL()}${
                    globals.navigations.businessUnitsLanding
                  }?unitid=${data.units[i].id}">${data.units[i].name}</a>`;
                }
              }
              return parentUnits;
            },
          },
          {
            data: null,
            render(data, type, row) {
            	if (data.active && userStates.pending === data.userApprovalStatus) {
            		return self.i18n.pending;
            	} else if (data.active) {
            		return self.i18n.enabled;
            	} else {
            		return self.i18n.disabled;
            	}
            },
          },
        ],
      });
      if (this.isMobile()) {
        this.$refs.sortDropdown.setDropdownLabel(this.sortOptions[0].label);
        this.triggerSort(this.sortOptions[0], this.$refs.usersTable);
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
     * To open the Add User Modal.
     */
    openAddUserModal(event) {
      this.$refs.addUserModal.open(event);
    },

    resetValues() {
      this.userDetails = {
        ...this.userDetails,
        firstName: '',
        lastName: '',
        uid: '',
        businessUnit: '',
        role: 'b2bcustomergroup',
        leaseSigner: true,
      };
      this.isEmailExists = false;
    },

    showLoadingSpinner() {
      this.$refs.spinner.showSpinner();
    },
    hideLoadingSpinner() {
      this.$refs.spinner.hideSpinner();
    },
    closeUserModal() {
      this.showInviteMsg = true;
      this.$refs.addUserModal.close();
    },
    setUsersData(userDetails) {
      if (this.globals.userInfo.b2bUnitLevel === userStates.l1) {
        const siteId = `|${globals.getSiteId()}`;
        window.location.href = `${this.userDetailsUrl}${
          userDetails.email
        }${encodeURIComponent(siteId)}`;
      } else {
        this.manageB2bOrgService.getUsersData(
          {},
          this.handleUsersData,
          this.handleUsersError,
        );
        this.$refs.addUserModal.close();
        this.resetValues();
      }
    },
  },
};
