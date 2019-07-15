/**
 * Component to show order-history
 * */
import mockOrderHistory from './mockOrderHistory';
import $ from 'jquery';
import dt from 'datatables.net';
import mobileMixin from '../../common/mixins/mobile-mixin';
import globals from '../../common/globals';
import vxDropdownPrimary from '../../common/vx-dropdown-primary/vx-dropdown-primary.vue';
import ManageTransactionService from '../../common/services/manage-transaction-service';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import vxOrderHistoryFilter from '../../manage-transaction-history/vx-order-history-filter/vx-order-history-filter.vue';
import dataTableUtilsMixin from '../../common/mixins/data-table-utils-mixin';

export default {
  name: 'vx-order-history',
  components: {
    vxDropdownPrimary,
    vxModal,
    vxOrderHistoryFilter,
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
      mockOrderHistory,
      globals,
      manageTransactionService: new ManageTransactionService(),
      orderHistoryData: [],
      dateRange: 6,
      isAdmin: false,
      totalOrders: '',
      sortOptions: [],
      selectedSort: '',
      // Order History transaction date dropdown data
      orderHistoryDropdownData: {
        options: [
          {
            label: this.i18n.date30DaysFilter,
            value: 30,
          },
          {
            label: this.i18n.date6MonthFilter,
            value: 6,
          },
        ],
      },
      tableTitles: [
        this.i18n.orderNumber,
        this.i18n.orderPlaced,
        this.i18n.status,
        this.i18n.orderPlacedBy,
        this.i18n.shippedTo,
        this.i18n.productNumber,
        this.i18n.total,
      ],
      currentYear: '',
    };
  },
  computed: {},
  mounted() {
    this.$refs.spinner.showSpinner();
    if (!this.isMobile()) {
      this.$refs.orderHistoryData.setDropdownValue(this.dateRange);
    }
    if (this.globals.isB2B()) {
      this.getOrderHistoryB2B();
    } else {
      this.getOrderHistoryB2C();
    }
    // To get current date and to push in transaction date dropdown
    const newDate = new Date();
    this.currentYear = newDate.getFullYear();
    this.orderHistoryDropdownData.options.push({
      label: this.currentYear,
      value: parseInt(this.currentYear, 10),
    });
    this.orderHistoryTimeData(this.currentYear);
    if ($.fn.dataTableExt) {
      this.enableDateSort($);
    }
  },
  methods: {
    generateSortOptions() {
      if (this.globals.isB2C() || this.globals.isEmployeeStore()) {
        this.sortOptions = this.i18n.b2cSortOptions.options;
      } else if (this.globals.isB2B()) {
        this.sortOptions = this.isAdmin
          ? this.i18n.b2bAdminSortOptions.options
          : this.i18n.b2bBuyerSortOptions.options;
      }
    },
    getOrderHistoryB2C() {
      this.manageTransactionService.getOrderHistoryB2C(
        {},
        this.handleOrderHistorySuccess,
        this.handleOrderHistoryError,
        this.dateRange,
      );
    },
    getOrderHistoryB2B() {
      this.manageTransactionService.getOrderHistoryB2B(
        {},
        this.handleOrderHistorySuccess,
        this.handleOrderHistoryError,
        this.dateRange,
      );
    },
    handleOrderHistorySuccess(response) {
      this.$refs.spinner.hideSpinner();
      const data = response.data;
      if (data && data.orders) {
        this.totalOrders = `(${data.pagination.totalResults})`;
        this.orderHistoryData = data.orders;
        if (this.orderHistoryData.length) {
          this.orderHistoryData.map((order) => {
            if (order.shippingAddress.isAdmin) {
              this.isAdmin = order.shippingAddress.isAdmin;
            }
          });
        }
        this.generateSortOptions();
        this.getDataTable();
        if (this.isMobile() && this.selectedSort) {
          this.triggerSort();
        }
      }
    },
    handleOrderHistoryError() {
      this.$refs.spinner.hideSpinner();
    },
    getDataTable() {
      const self = this;
      const respTable = $('#order-History-data-table').DataTable({
        searching: false, // hides search, default to true
        lengthChange: false, // hides the length message, default to true
        info: false, // hides the info
        recordsTotal: 0,
        destroy: true,
        language: {
          emptyTable: self.i18n.emptyTableMessage,
          paginate: {
            next: '<span aria-hidden="true">&#62;</span>',
            previous: '<span aria-hidden="true">&#60;</span>',
          },
          aria: {
            paginate: {
              previous: 'Previous',
              next: 'Next',
            },
          },
        },
        order: [[1, 'dsc']],
        columnDefs: this.generateColumnDefs(),

        data: this.orderHistoryData,
        drawCallback() {
          $('.dataTables_paginate  a').attr('href', '#');
        },
        columns: [
          {
            data: null,
            defaultContent: '',
            render(data, type, row, meta) {
              let orderNumberField = '';
              if (data.sourceNetSuite) {
                orderNumberField = `<p>${data.code}</p>`;
              } else if (self.globals.isB2B() && !self.isAdmin) {
                orderNumberField = `<a href="${globals.getB2BBaseURL()}${
                  globals.navigations.orderDetails
                }${data.code}">${data.code}</a>`;
              } else if (self.globals.isB2B() && self.isAdmin) {
                orderNumberField = `<a href="${globals.getB2BBaseURL()}${
                  globals.navigations.orderDetails
                }${data.code}?admin=${self.isAdmin}">${data.code}</a>`;
              } else if (self.globals.isB2C()) {
                orderNumberField = `<a href="${globals.getB2CBaseURL()}${
                  globals.navigations.orderDetails
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
              return data.user.name;
            },
          },
          {
            data: null,
            defaultContent: '',
            render(data, type, row) {
              const addressList = data.shippingAddress.addresses.map(
                (address) => {
                  const addressFormatted = self.formAddressString(address);
                  const addressFull = `<p>${addressFormatted.name}</p><p>${
                    addressFormatted.companyName
                  }</p><p>${addressFormatted.line1}</p><p>${
                    addressFormatted.line2
                  }</p><p><span class="address-format">${
                    addressFormatted.town
                  }</span> <span class="address-format">${
                    addressFormatted.region
                  }</span> <span class="address-format">${
                    addressFormatted.postalCode
                  }</span></p><p>${addressFormatted.country}</p>`;
                  return addressFull;
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
      if (this.globals.isB2C() || !this.isAdmin) {
        respTable.column(3).visible(false);
      }
      if (this.globals.isB2B()) {
        respTable.column(4).visible(false);
      }

      if (
        this.orderHistoryData.length === 0 ||
        this.orderHistoryData.length === 1
      ) {
        unbindClick();
      } else {
        respTable.order([1, 'desc']).draw();
      }

      function unbindClick() {
        $('th')
          .unbind('click')
          .on('focus', function() {
            $(this).css('outline', 'none');
          });
      }
    },
    /**
     * This function is used to define columns for data table
     */
    generateColumnDefs() {
      const columnConfigArray = [];
      const self = this;
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
        columnnConfig.createdCell = (
          cell,
          cellData,
          rowData,
          rowIndex,
          colIndex,
        ) => {
          cell.setAttribute('data-title', val);
        };
        if (val === self.i18n.shippedTo) {
          columnnConfig.orderable = false;
        }
        columnConfigArray.push(columnnConfig);
      });
      return columnConfigArray;
    },
    formAddressString(address) {
      let userAddress = '';
      let companyName = '';
      let line2 = '';
      const name = `${address.firstName} ${address.lastName}`;
      if (address && address.companyName) {
        companyName = address.companyName;
      }
      if (address && address.line2) {
        line2 = address.line2;
      }
      if (address) {
        userAddress = {
          name,
          companyName,
          line1: address.line1,
          line2,
          town: address.town,
          region: address.region.isocodeShort,
          postalCode: address.postalCode,
          country: address.country.isocode,
        };
      }
      return userAddress;
    },
    dateFilterSelected(dateRange) {
      this.dateRange = dateRange.value;
      if (!this.isMobile()) {
        if (this.globals.isB2B()) {
          this.getOrderHistoryB2B();
        } else {
          this.getOrderHistoryB2C();
        }
      }
    },
    // Order history years data population
    orderHistoryTimeData(year) {
      let priorYear = parseInt(year, 10);
      const orderCreationYear = this.mockOrderHistory.orders[0].creationTime.split(
        '-',
      )[0];
      const totalPriorYears = year - parseInt(orderCreationYear, 10);
      for (let i = 0; i < totalPriorYears; i += 1) {
        priorYear -= 1;
        this.orderHistoryDropdownData.options.push({
          label: priorYear,
          value: priorYear,
        });
      }
    },
    openFilterModel(event) {
      this.$refs.sortFilterModel.open(event);
    },
    populateDefaultDate() {
      this.$refs.orderHistoryData.setDropdownValue(this.dateRange);
      if (!this.selectedSort) {
        this.$refs.sortDropdown.setDropdownLabel('Sort By');
      } else {
        this.$refs.sortDropdown.setDropdownValue(this.selectedSort);
      }
    },
    // On click of any button in filter modal it will come to this function
    filterApplicable(data) {
      if (data === 'clear') {
        this.dateRange = 6;
        this.selectedSort = '';
        this.$refs.orderHistoryData.setDropdownValue(this.dateRange);
        this.$refs.sortDropdown.setDropdownLabel(this.i18n.defaultSort);
      } else if (data === 'apply') {
        this.applyFilter();
      }
    },
    // To apply filter value.
    applyFilter() {
      if (this.globals.isB2B()) {
        this.getOrderHistoryB2B();
      } else {
        this.getOrderHistoryB2C();
      }
      this.$refs.sortFilterModel.close();
    },
    storeSort(e) {
      this.selectedSort = e.value;
    },
    triggerSort() {
      const sortValue = this.selectedSort.split('-')[0];
      const sortOrder = this.selectedSort.split('-')[1];
      const respTable = $('#order-History-data-table').DataTable();
      respTable.order([[sortValue, sortOrder]]).draw();
    },
  },
};
