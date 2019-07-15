import $ from 'jquery';
import dt from 'datatables.net';
import {
  Validator
} from 'vee-validate';
import mobileMixin from '../../common/mixins/mobile-mixin';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import globals from '../../common/globals';
import vxDropdownPrimary from '../../common/vx-dropdown-primary/vx-dropdown-primary.vue';
import ManageB2bOrgService from '../../common/services/manage-b2b-org-service';
import detectDeviceMixin from '../../common/mixins/detect-device-mixin';
import dataTableUtilsMixin from '../../common/mixins/data-table-utils-mixin';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';

export default {
  name: 'vx-user-groups-landing',
  components: {
    vxDropdownPrimary,
    vxModal,
    ManageB2bOrgService,
    vxSpinner,
  },
  props: {
    i18n: {
      type: Object,
      required: true,
    },
  },
  mixins: [mobileMixin,detectDeviceMixin, dataTableUtilsMixin],
  data() {
    return {
      globals,
      manageB2bOrgService: '',
      tableTitles: [],
      sortOptions: this.i18n.mobileSortOptions,
      userGroupDetails: {
        id: '',
        userGroupName: '',
        businessUnit: {
          label: '',
          value: '',
        },
        status: '',
      },
      businessUnitsList: [],
      userGroups: {},
      userGroupDetailsUrl: '',
      isUserGroupExists: false,
      isUserGroupExistsError: '',
    };
  },
  computed: {},
  mounted() {
    this.$refs.spinner.showSpinner();
    this.manageB2bOrgService = new ManageB2bOrgService();
    this.userGroupDetailsUrl = `${this.globals.getB2BBaseURL()}${
      this.globals.navigations.userGroupLanding
    }?ugid=`;
    /**
     * Getting JSON for user groups data.
     */
    this.manageB2bOrgService.getUserGroups({}, this.handleUserGroupsData, this.handleUserGroupsError);
    this.manageB2bOrgService.getBusinessUnits({}, this.handleBusinessUnitsData, this.handleBusinessUnitsError, '');

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
            required: this.i18n.businessUnitError,
          },
        },
      },
    };
    Validator.localize(veeCustomErrorMessage);
  },

  methods: {
    handleUserGroupsData(response) {
      this.$refs.spinner.hideSpinner();
      const status = response.status;
      const data = response.data;
      if (status) {
        this.userGroups = data;
        this.initializeTable(data);
      }
    },
    handleUserGroupsError(error) {
      this.$refs.spinner.hideSpinner();
    },
    initializeTable(userGroups) {
      this.tableTitles = this.i18n.tableHeaders.split(',').map(ele => ({
        title: ele.trim(),
      }));
      const self = this;
      if ($.fn.dataTable.isDataTable($(this.$refs.userGroupsTable))) {
        $(this.$refs.userGroupsTable)
          .DataTable()
          .clear()
          .rows.add(userGroups.usergroups)
          .draw();
        return;
      }
      $(this.$refs.userGroupsTable).DataTable({
        searching: false,
        lengthChange: false,
        info: false,
        pageLength: 10,
        language: {
          emptyTable: this.i18n.noDataAvailable,
          paginate: {
            next: '&#62;', // or '→'
            previous: '&#60;', // or '←'
          },
        },
        order: [],
        columnDefs: [{
            targets: 0,
            title: this.tableTitles[0].title,
            createdCell: (cell, cellData, rowData, rowIndex, colIndex) => {
              cell.setAttribute('data-title', this.tableTitles[0].title);
            },
          },
          {
            title: this.tableTitles[1].title,
            targets: 1,
            createdCell: (cell, cellData, rowData, rowIndex, colIndex) => {
              cell.setAttribute('data-title', this.tableTitles[1].title);
            },
          },
          {
            title: this.tableTitles[2].title,
            targets: 2,
            createdCell: (cell, cellData, rowData, rowIndex, colIndex) => {
              cell.setAttribute('data-title', this.tableTitles[2].title);
            },
          },
          {
            title: this.tableTitles[3].title,
            targets: 3,
            createdCell: (cell, cellData, rowData, rowIndex, colIndex) => {
              cell.setAttribute('data-title', this.tableTitles[3].title);
            },
          },
        ],
        data: userGroups.usergroups,
        columns: [{
            data: null,
            render(data, type, row, meta) {
              return `<a href="${globals.getB2BBaseURL()}${
                globals.navigations.userGroupLanding
              }?ugid=${data.uid}">${data.uid}</a>`;
            },
          },
          {
            data: null,
            render(data, type, row) {
              return data.name;
            },
          },
          {
            data: null,
            render(data, type, row) {
              return data.unit.name;
            },
          },
          {
            data: null,
            render(data, type, row) {
              return data.members ? self.i18n.active : self.i18n.disabled;
            },
          },
        ],
      });
      if (this.isMobile()) {
        this.$refs.sortDropdown.setDropdownLabel(this.sortOptions[0].label);
        this.triggerSort(this.sortOptions[0], this.$refs.userGroupsTable);
      }
    },

    handleBusinessUnitsData(response) {
      const status = response.status;
      const data = response.data;
      if (status) {
        this.businessUnitsList = data.units.map(this.createBusinessUnitsList);
      }
    },
    handleBusinessUnitsError(error) {

    },
    createBusinessUnitsList(item) {
      return {
        label: item.name,
        value: item.id,
      };
    },

    handleAddUserGroup(response) {
      const status = response.status;
      if (status) {
        window.location.href = `${this.userGroupDetailsUrl}${this.userGroupDetails.id}`;
      }
    },
    handleAddUserGroupError(error) {

    },
    /**
     * To open the Create User Group Modal.
     */
    openAddUserGroupModal(event) {
      this.$refs.createUserGroupModal.open(event);
    },
    addUserGroup() {
      this.$validator.validateAll().then((result) => {
        if (result) {
          const payload = {
            unitUid: this.userGroupDetails.businessUnit.value,
            userGroupName: this.userGroupDetails.userGroupName,
            userGroupUid: this.userGroupDetails.id,
          };
          const requestConfig = {};
          requestConfig.data = payload;
          this.manageB2bOrgService.createUserGroup(
            requestConfig,
            this.handleAddUserGroup,
            this.handleUserGroupExists,
          );
        } else {
          this.globals.setFocusByName(this.$el, this.globals.getElementName(this.errors));
        }
      });
    },

    handleUserGroupExists(error) {
      const data = error.response.data;
      if (data.errors[0].message) {
        this.isUserGroupExists = true;
        this.isUserGroupExistsError = data.errors[0].message;
      }
    },

    resetValues() {
      this.userGroupDetails.businessUnit = {};
      this.userGroupDetails.userGroupName = '';
      this.userGroupDetails.id = '';
      this.isUserGroupExists = false;
    },
  },
};
