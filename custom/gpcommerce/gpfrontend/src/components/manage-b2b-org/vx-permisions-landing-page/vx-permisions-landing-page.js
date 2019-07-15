import $ from 'jquery';
import dt from 'datatables.net';
import vxDropdownPrimary from '../../common/vx-dropdown-primary/vx-dropdown-primary.vue';
import mobileMixin from '../../common/mixins/mobile-mixin';
import dataTableUtilsMixin from '../../common/mixins/data-table-utils-mixin';
import globals from '../../common/globals';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import vxPermissionsForm from '../vx-permissions-form/vx-permissions-form.vue';
import ManageB2bOrgService from '../../common/services/manage-b2b-org-service';

export default {
  name: 'vx-permisions-landing-page',
  components: {
    vxDropdownPrimary,
    vxSpinner,
    vxPermissionsForm,
    vxModal,
  },
  props: {
    i18n: {
      type: Object,
      required: true,
    },
  },
  mixins: [mobileMixin, dataTableUtilsMixin],
  data() {
    return {
      globals,
      tableHeadings: [],
      sortOptions: [],
      MobileSortOptions: this.i18n.mobileSortOptions,
    };
  },
  computed: {},
  mounted() {
    this.$refs.spinner.showSpinner();
    this.fetchPermissinons();
  },
  methods: {
    generateSortOptions(data) {
      data.map((val, ind) => {
        const sortObj = {};
        sortObj.label = val;
        sortObj.value = ind;
        this.sortOptions.push(sortObj);
      });
    },

    fetchPermissinons() {
      const manageB2bOrgService = new ManageB2bOrgService();
      manageB2bOrgService.getPermissions(
        {},
        this.getPermissionsResponse,
        this.getPermissionsResponseError,
      );
    },
    generateColumnDefs() {
      const columnConfigArray = [];
      const columnWidthArray = ['15%', '20%', '20%', '15%', '15%', '15%'];
      this.sortOptions.map((val, index) => {
        const columnnConfig = {};
        columnnConfig.targets = index;
        columnnConfig.width = columnWidthArray[index];
        columnnConfig.title = val.label;
        columnnConfig.createdCell = (
          cell,
          cellData,
          rowData,
          rowIndex,
          colIndex,
        ) => {
          cell.setAttribute('data-title', val.label);
        };
        if (index === 4) {
          columnnConfig.type = 'currency';
        }
        columnConfigArray.push(columnnConfig);
      });
      return columnConfigArray;
    },
    getPermissionsResponse(response) {
      this.$refs.spinner.hideSpinner();
      const status = response.status;
      const data = response.data;
      if (status) {
        this.initializePermissionsTable(data);
      } else {
        this.handleErrorCallback(data);
      }
    },
    getPermissionsResponseError(error) {},
    initializePermissionsTable(dataSet) {
      this.tableHeadings = this.i18n.permissionColumns.split(',');
      this.generateSortOptions(this.tableHeadings);
      if ($.fn.dataTable.isDataTable('#permissions-data-table')) {
        $('#permissions-data-table')
          .DataTable()
          .clear()
          .rows.add(dataSet.permissions)
          .draw();
        return;
      }
      this.sortCurrencyCol();
      const permissionsTable = $('#permissions-data-table').DataTable({
        data: dataSet.permissions,
        searching: false, // hides search, default to true
        lengthChange: false, // hides the length message, default to true
        info: false, // hides the info
        pageLength: 10,
        destroy: true,
        language: {
          emptyTable: `${this.i18n.noPermissions}`,
          paginate: {
            next: '&#62;', // or '→'
            previous: '&#60;', // or '←'
          },
        },
        order: [],
        columnDefs: this.generateColumnDefs(),
        columns: [
          {
            data: null,
            render(data, type, row, meta) {
              return `<a href="${globals.getB2BBaseURL()}${
                globals.navigations.permissionLanding
              }?pDetail=${encodeURI(data.code)}">${data.code}</a>`;
            },
          },
          {
            data: null,
            render(data, type, row) {
              return data.b2BPermissionTypeData.name;
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
              return data.timeSpan ? data.timeSpan : '';
            },
          },
          {
            data: null,
            render(data, type, row) {
              return `${data.value} ${data.currency.isocode}`;
            },
          },
          {
            data: null,
            render: (data, type, row) =>
              `${data.active ? this.i18n.enabled : this.i18n.disabled}`,
          },
        ],
      });
      permissionsTable.draw();
      if (this.isMobile()) {
        this.$refs.sortDropdown.setDropdownLabel(this.MobileSortOptions[0].label);
        this.triggerSort(this.MobileSortOptions[0], '#permissions-data-table');
      }
    },
    handleErrorCallback(errorData) {},
    openPermissionsModal(event) {
      this.$refs.permissionModal.open(event);
    },
    closePermissionsModal() {
      this.$refs.permissionModal.close();
    },
    showLoadingSpinner() {
      this.$refs.spinner.showSpinner();
    },
    hideLoadingSpinner() {
      this.$refs.spinner.hideSpinner();
    },
  },
};
