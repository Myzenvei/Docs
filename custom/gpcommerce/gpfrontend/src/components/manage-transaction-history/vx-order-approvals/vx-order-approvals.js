/**
 * Handles order approval list page functionality
 */
import mockOrderApproval from './mockOrderApproval';
import $ from 'jquery';
import dt from 'datatables.net';
import mobileMixin from '../../common/mixins/mobile-mixin';
import globals from '../../common/globals';
import vxDropdownPrimary from '../../common/vx-dropdown-primary/vx-dropdown-primary.vue';
import ManageTransactionService from '../../common/services/manage-transaction-service';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import vxRefineApprovalList from '../../manage-transaction-history/vx-refine-approval-list/vx-refine-approval-list.vue';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import dataTableUtilsMixin from '../../common/mixins/data-table-utils-mixin';

export default {
  name: 'vx-order-approvals',
  components: {
    vxDropdownPrimary,
    vxModal,
    vxRefineApprovalList,
    vxSpinner,
  },
  mixins: [mobileMixin, dataTableUtilsMixin],
  props: {
    /**
     * Labels, button and caption texts
     */
    i18n: {
      type: Object,
    },
  },
  data() {
    return {
      mockOrderApproval,
      globals,
      manageTransactionService: new ManageTransactionService(),
      orderApprovalData: [],
      dateRange: {
        label: this.i18n.date6MonthFilter,
        value: 6,
      },
      status: 'CREATED',
      isAdmin: false,
      totalOrders: '',
      sortOptions: [],
      selectedSort: '',
      tableTitles: [
        this.i18n.order,
        this.i18n.orderPlaced,
        this.i18n.status,
        this.i18n.orderPlacedBy,
        this.i18n.shippedTo,
        this.i18n.noOfProducts,
        this.i18n.total,
      ],
      currentYear: '',
      filterData: {
        data: [],
        selectedItem: [],
      },
      statusApplied: false,
    };
  },
  computed: {},
  mounted() {
    this.$refs.spinner.showSpinner();
    if (this.globals.isB2B()) {
      this.getOrderApprovalsB2B();
    }
    // To get current date and to push in transaction date dropdown
    const newDate = new Date();
    this.currentYear = newDate.getFullYear();
    this.enableDateSort($);
  },
  methods: {
    generateSortOptions() {
      if (this.globals.isB2C() || this.globals.isEmployeeStore()) {
        this.sortOptions = this.i18n.b2cSortOptions.options;
      } else if (this.globals.isB2B()) {
        this.sortOptions = this.i18n.b2bApprovalSortOptions.options;
      }
    },
    getOrderApprovalsB2B() {
      this.manageTransactionService.getOrderApprovalsB2B(
        {},
        this.handleOrderApprovalSuccess,
        this.handleOrderApprovalError,
        this.dateRange.value,
      );
    },
    handleOrderApprovalSuccess(response) {
      this.$refs.spinner.hideSpinner();
      const data = response.data;
      if (this.$refs.refineApprovalListModel) {
        this.$refs.refineApprovalListModel.close();
      }
      if (data && data.orders) {
        this.totalOrders = `(${data.pagination.totalResults})`;
        this.orderApprovalData = data.orders;
        if (this.orderApprovalData && this.orderApprovalData.length !== 0) {
          this.isAdmin = this.orderApprovalData[0].shippingAddress.isAdmin;
        }
        this.generateSortOptions();
        this.getDataTable();
        if (this.isMobile() && this.selectedSort) {
          this.triggerSort();
        }
      }
    },
    handleOrderApprovalError() {
      this.$refs.spinner.hideSpinner();
    },
    handleOrderStatusSuccess(response) {
      this.statusApplied = true;
      this.$refs.refineApprovalListModel.close();
      const data = response.data;
      if (data && data.orders) {
        this.totalOrders = `(${data.pagination.totalResults})`;
        this.orderApprovalData = data.orders;
        if (this.orderApprovalData && this.orderApprovalData.length !== 0) {
          this.isAdmin = this.orderApprovalData[0].shippingAddress.isAdmin;
        }
        this.generateSortOptions();
        this.getDataTable();
        if (this.isMobile() && this.selectedSort) {
          this.triggerSort();
        }
      }
    },
    handleOrderStatusError(error) {
      this.statusApplied = false;
      this.$refs.statusFilter.displayStatusError(error);
    },
    getDataTable() {
      const self = this;
      const respTable = $('#order-Approval-data-table').DataTable({
        searching: false, // hides search, default to true
        lengthChange: false, // hides the length message, default to true
        info: false, // hides the info
        recordsTotal: 0,
        destroy: true,
        language: {
          emptyTable: self.i18n.emptyTableMessage,
          paginate: {
            next: '&#62;',
            previous: '&#60;',
          },
        },
        order: [[1, 'asc']],
        columnDefs: this.generateColumnDefs(),

        data: this.orderApprovalData,
        columns: [
          {
            data: null,
            defaultContent: '',
            render(data, type, row, meta) {
              let orderNumberField = '';
              if (self.globals.isB2B()) {
                orderNumberField = `<a href="${globals.getB2BBaseURL()}${
                  globals.navigations.orderApprovalDetails
                }${data.code}">${data.code}</a>`;
              }
              return orderNumberField;
            },
          },
          {
            data: null,
            defaultContent: '',
            type: 'extract-date',
            render(data, type, row) {
              if (data.placed) {
                const modifiedDateValue = data.placed.split('-');
                const modifiedDate = modifiedDateValue[2].split('T');
                const dateValue = `${modifiedDateValue[1]}/${modifiedDate[0]}/${
                  modifiedDateValue[0]
                }`;
                return dateValue;
              }
            },
          },
          {
            data: null,
            defaultContent: '',
            render(data, type, row) {
              return data.statusDisplay;
            },
          },
          {
            data: null,
            defaultContent: '',
            render(data, type, row) {
              return self.getOrderByname(data.shippingAddress.addresses[0]);
            },
          },
          {
            data: null,
            defaultContent: '',
            render(data, type, row) {
              const addressList = data.shippingAddress.addresses.map(
                (address) => {
                  const addressFull = self.formAddressString(address);
                  return `<p>${addressFull}</p>`;
                },
              );
              return addressList.join('');
            },
          },
          {
            data: null,
            defaultContent: '',
            render(data, type, row) {
              return data.totalNumberOfProducts;
            },
          },
          {
            data: null,
            defaultContent: '',
            render(data, type, row) {
              return data.total.formattedValue;
            },
          },
        ],
      });
      if (this.globals.isB2C() && !this.isAdmin) {
        respTable.column(3).visible(false);
      }
      if (this.globals.isB2B()) {
        respTable.column(4).visible(false);
      }
    },
    /**
     * This function is used to define columns for data table
     */
    generateColumnDefs() {
      const columnConfigArray = [];
      this.tableTitles.map((val, index) => {
        const columnnConfig = {};
        columnnConfig.title = val;
        columnnConfig.targets = index;
        const columnWidthArray = [
          '15%',
          '15%',
          '15%',
          '25%',
          '15%',
          '20%',
          '10%',
        ];
        columnnConfig.width = columnWidthArray[index];
        if (val === 'Status') {
          columnnConfig.bSortable = false;
        }
        columnnConfig.createdCell = (
          cell,
          cellData,
          rowData,
          rowIndex,
          colIndex,
        ) => {
          cell.setAttribute('data-title', val);
        };
        columnConfigArray.push(columnnConfig);
      });
      return columnConfigArray;
    },
    getOrderByname(address) {
      return `${address.firstName} ${address.lastName}`;
    },
    formAddressString(address) {
      let userAddress = '';
      let companyName = '';
      const name = `${address.firstName} ${address.lastName}`;
      if (address && address.companyName) {
        companyName = address.companyName;
      }
      if (address) {
        userAddress = [
          name,
          companyName,
          address.line1,
          address.line2,
          address.town,
          address.region.isocodeShort,
          address.postalCode,
          address.country.isocode,
        ]
          .filter(Boolean)
          .join(', ');
      }
      return userAddress;
    },
    openFilterModel() {
      this.$refs.refineApprovalListModel.open();
    },
    populateDefaultDate() {
      if (!this.selectedSort) {
        this.$refs.sortDropdown.setDropdownLabel('Sort By');
      } else {
        this.$refs.sortDropdown.setDropdownValue(this.selectedSort);
      }
    },
    // On click of any button in filter modal it will come to this function
    filterApplicable(data) {
      this.filterData = data;
      this.dateRange = data.dateRange;
      if (this.filterData.action === 'apply') {
        if (this.filterData.data.length) {
          this.manageTransactionService.getOrderApprovalsStatus(
            {},
            this.handleOrderStatusSuccess,
            this.handleOrderStatusError,
            this.filterData.data,
          );
        } else {
          this.getOrderApprovalsB2B();
        }
      }
    },
    removeFilters(index, event) {
      this.filterData.data.splice(index, 1);
      this.filterData.selectedItem.splice(index, 1);
      if (this.filterData.data.length) {
        this.filterApplicable(this.filterData);
      } else {
        this.getOrderApprovalsB2B();
      }
    },
    storeSort(e) {
      this.selectedSort = e.value;
    },
    triggerSort() {
      const sortValue = this.selectedSort.split('-')[0];
      const sortOrder = this.selectedSort.split('-')[1];
      const respTable = $('#order-Approval-data-table').DataTable();
      respTable.order([[sortValue, sortOrder]]).draw();
    },
  },
};
