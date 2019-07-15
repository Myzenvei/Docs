import $ from 'jquery';
import dt from 'datatables.net';
import {
  Validator
} from 'vee-validate';
import globals from '../../common/globals';
import mobileMixin from '../../common/mixins/mobile-mixin';
import detectDeviceMixin from '../../common/mixins/detect-device-mixin';
import ManageB2bOrgService from '../../common/services/manage-b2b-org-service';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import vxDropdownPrimary from '../../common/vx-dropdown-primary/vx-dropdown-primary.vue';
import {
  UnitDetails
} from '../../common/mixins/vx-enums';

export default {
  name: 'vx-business-unit-landing',
  components: {
    vxModal,
    vxDropdownPrimary,
  },
  mixins: [mobileMixin, detectDeviceMixin],
  props: {
    i18n: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      globals,
      landingPageData: {},
      tableTitles: [this.i18n.unitName, this.i18n.adminsText, this.i18n.buyersText],
      businessUnitDetails: {
        companyName: '',
        parentUnit: '',
      },
      parentUnitsList: [],
      manageB2bOrgService: new ManageB2bOrgService(),
      displayedResultsLength: 10,
      childUnitsTable: {},
      UnitDetails,
    };
  },
  computed: {
    dataAvailable() {
      return this.landingPageData && Object.keys(this.landingPageData).length !== 0;
    },
    parentUnitDetails() {
      let phone = '';
      if (this.landingPageData.addresses && this.landingPageData.addresses[0].phone) {
        phone = this.landingPageData.addresses[0].phone;
      }
      return `${this.landingPageData.name}<br>
      ${this.landingPageData.role ? this.landingPageData.role : ''}<br>
      ${phone}`;
    },
    parentUnitAddress() {
      let address = '';
      if (this.landingPageData.addresses) {
        address = `${
          this.landingPageData.addresses[0].country &&
            this.landingPageData.addresses[0].country.isocode
            ? this.landingPageData.addresses[0].country.isocode
            : ''
          }<br>
      ${this.landingPageData.addresses[0].line1 ? this.landingPageData.addresses[0].line1 : ''}<br>
      ${this.landingPageData.addresses[0].line2 ? this.landingPageData.addresses[0].line2 : ''}<br>
      ${this.landingPageData.addresses[0].town ? this.landingPageData.addresses[0].town : ''},
      ${
          this.landingPageData.addresses[0].region && this.landingPageData.addresses[0].region.isocode
            ? this.landingPageData.addresses[0].region.isocode
            : ''
          }
      ${
          this.landingPageData.addresses[0].postalCode
            ? this.landingPageData.addresses[0].postalCode
            : ''
          }`;
      }
      return address;
    },
  },
  created() {
    this.fetchLandingPageData();
    this.fetchUnitsDropdownList();
  },
  mounted() {
    const veeCustomErrorMessage = {
      en: {
        custom: {
          firstName: {
            required: this.i18n.firstNameRequiredError,
          },
          lastName: {
            required: this.i18n.lastNameRequiredError,
          },
          email: {
            required: this.i18n.emailRequiredError,
            email: this.i18n.emailInvalidError,
          },
          parentUnit: {
            required: this.i18n.parentUnitRequiredError,
          },
          companyName: {
            required: this.i18n.unitNameRequiredError,
            max: this.i18n.unitNameMaxError,
          },
        },
      },
    };
    Validator.localize(veeCustomErrorMessage);
  },
  methods: {
    fetchLandingPageData() {
      this.manageB2bOrgService.getBusinessLandingDataService({}, this.handleLandingPageData, this.handleLandingPageError);
    },
    handleLandingPageData(response) {
      const data = response.data;
      const status = response.status;
      if (status) {
        this.landingPageData = data;
      } else {
        this.handleErrorCallback();
      }
      if (this.childUnitsTable.data) {
        this.childUnitsTable.destroy();
      }
      this.createTable();
    },
    handleLandingPageError(error) {

    },
    handleErrorCallback() {
      console.log('Error');
    },
    fetchUnitsDropdownList() {
      this.manageB2bOrgService.getBusinessUnits({}, this.handleUnitsDropdownResponse, this.handleUnitsDropdownError, '', true);
    },
    handleUnitsDropdownResponse(response) {
      const data = response.data;
      const status = response.status;
      if (status) {
        this.parentUnitsList = data.units.map(child => ({
          label: child.name,
          value: child.id,
        }));
      }
    },
    handleUnitsDropdownError(error) {

    },
    generateColumnDefs() {
      const columnConfigArray = [];
      this.tableTitles.map((val, index) => {
        const columnnConfig = {};
        if (index === 0) {
          columnnConfig.width = '60%';
        } else {
          columnnConfig.width = '20%';
        }
        columnnConfig.targets = index;
        columnnConfig.title = val;
        columnnConfig.createdCell = (cell, cellData, rowData, rowIndex, colIndex) => {
          cell.setAttribute('data-title', val);
        };
        columnConfigArray.push(columnnConfig);
      });
      return columnConfigArray;
    },
    createTable() {
      const self = this;
      this.childUnitsTable = $(this.$refs.childUnitsTable).DataTable({
        searching: false,
        ordering: false,
        lengthChange: false,
        info: false,
        language: {
          emptyTable: this.i18n.noDataAvailable,
          paginate: {
            next: '&#62;',
            previous: '&#60;',
          },
        },
        order: [],
        paging: true,
        pageLength: this.displayedResultsLength,
        columnDefs: this.generateColumnDefs(),
        data: this.landingPageData.children,
        columns: [{
          data: null,
          defaultContent: '',
          render(data, type, row, meta) {
            return `<a
              href="${globals.getNavBaseUrl()}${self.globals.navigations.businessUnitsLanding}/?${
              self.UnitDetails.UNIT_ID
              }=${encodeURIComponent(data.id)}">
              ${data.name}
              </a>`;
          },
        },
        {
          data: null,
          defaultContent: '',
          render(data, type, row) {
            return data.noOfAdministrators;
          },
        },
        {
          data: null,
          defaultContent: '',
          render(data, type, row) {
            return data.noOfCustomers;
          },
        },
        ],
      });
      this.childUnitsTable.rows().every(function () {
        const data = this.data().children;
        let childRowGroup = '';
        for (let i = 0; i < data.length; i++) {
          const childRow = `<tr class="nested-row"><td class="pb-sm-3 pt-sm-0 pb-xs-3 pt-xs-0"><a
          href="${globals.getNavBaseUrl()}${self.globals.navigations.businessUnitsLanding}/?${
            self.UnitDetails.UNIT_ID
            }=${encodeURIComponent(data[i].id)}">
          <ul class="mb-xs-0"><li>
          ${
            data[i].name
            }</li></ul></a><td class="pb-sm-3 pt-sm-0 pb-xs-3 pt-xs-0" data-title="Admins">
          ${
            data[i].noOfAdministrators ? data[i].noOfAdministrators : ''
            }</td><td class="pb-sm-3 pt-sm-0 pb-xs-3 pt-xs-0" data-title="Buyers">
          ${data[i].noOfCustomers ? data[i].noOfCustomers : ''}</td></tr>`;
          childRowGroup = childRowGroup.concat(childRow);
        }
        this.child($(childRowGroup)).show();
      });
    },
    handleAddBusinessUnit(event) {
      this.$refs.addBusinessUnitModal.open(event);
    },
    sendAddBusinessUnitRequest() {
      this.$validator.validateAll().then((result) => {
        if (result) {
          const childUnit = encodeURI(this.businessUnitDetails.companyName);
          const parentUnit = encodeURI(this.businessUnitDetails.parentUnit.value);
          if (childUnit && parentUnit) {
            this.manageB2bOrgService.createChildUnitService({},
              this.handleAddBusinessUnitResponse, this.handleAddBusinessUnitResponse, childUnit,
              parentUnit);
          }
        } else {
          this.globals.setFocusByName(this.$el, this.globals.getElementName(this.errors));
        }
      });
    },
    handleAddBusinessUnitResponse(response) {
      this.resetValues();
      this.fetchLandingPageData();
      this.$refs.addBusinessUnitModal.close();
      this.manageB2bOrgService.getBusinessUnits({}, this.handleUnitsDropdownResponse, this.handleUnitsDropdownError, '', true);
    },
    handleAddBusinessUnitError(error) {

    },
    resetValues() {
      this.businessUnitDetails.companyName = '';
      this.businessUnitDetails.parentUnit = {};
    },
  },
};
