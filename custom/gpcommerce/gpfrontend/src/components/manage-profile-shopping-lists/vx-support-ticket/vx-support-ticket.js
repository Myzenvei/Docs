import $ from 'jquery';
import dt from 'datatables.net';
import globals from '../../common/globals';
import liveChatMixin from '../../common/mixins/live-chat-mixin';
import ManageProfileShoppingListService from '../../common/services/manage-profile-shopping-lists-service';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import {
  supportTicketOrder,
  order,
} from '../../common/mixins/vx-enums';
import vxDropdownPrimary from '../../common/vx-dropdown-primary/vx-dropdown-primary.vue';
import mobileMixin from '../../common/mixins/mobile-mixin';

export default {
  name: 'vx-support-ticket',
  mixins: [liveChatMixin, mobileMixin],
  components: {
    vxSpinner,
    vxDropdownPrimary,
  },
  props: {
    i18n: {
      type: Object,
    },
  },
  data() {
    return {
      globals,
      supportTicketOrder,
      liveAgentConfig: globals.thirdPartyApps.liveAgent,
      tableTitles: [
        this.i18n.tickets,
        this.i18n.topicOfInquiry,
        this.i18n.createdBy,
        this.i18n.dateOpened,
        this.i18n.status,
        this.i18n.serviceAgent,
      ],
      supportTicketTable: {},
      displayedResultsLength: 10,
      tableData: [],
      showButtons: false,
      manageProfileShoppingListService: new ManageProfileShoppingListService(),
      sortedData: [],
      nextSortingOrder: 'DESC',
      mobileSortOptions: [],
      tableOrders: [
        this.i18n.ticketNumberAsc,
        this.i18n.ticketNumberDsc,
        this.i18n.topicOfInquiryAsc,
        this.i18n.topicOfInquiryDsc,
        this.i18n.createdByAsc,
        this.i18n.createdByDsc,
        this.i18n.dateOpenedLatest,
        this.i18n.dateOpenedOldest,
        this.i18n.statusAsc,
        this.i18n.statusDsc,
      ],
      order,
    };
  },
  computed: {},
  created() {},
  mounted() {
    this.initSupportTicketData();
    this.createOptions();
  },
  methods: {
    generateColumnDefs() {
      const columnConfigArray = [];
      this.tableTitles.map((val, index) => {
        const columnnConfig = {};
        const columnWidthArray = ['10%', '20%', '20%', '20%', '10%', '20%'];
        columnnConfig.width = columnWidthArray[index];
        columnnConfig.targets = index;
        columnnConfig.title = val;
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
    createAccordionInTable(data) {
      // `d` is the original data object for the row
      return `<div class="accordion-container">
      <div class="order">
      <div class="heading order-container">Order #</div>
      <div class="heading-value">${data.orderNumber || ''}</div>
      </div>
      <div class="comments">
      <div class="heading">Comments</div>
      <div class="heading-value">${data.ticketComments || ''}</div>
      </div>
      <div class="resolution">
      <div class="heading"> Resolution Summary</div>
      <div class="heading-value">${data.sfdcResolutionSummary || ''}</div>
      </div>
      </div>`;
    },
    createTable(tableData) {
      const self = this;
      this.supportTicketTable = $(this.$refs.supportTicketTable).DataTable({
        searching: false,
        ordering: true,
        lengthChange: false,
        info: false,
        language: {
          emptyTable: this.i18n.noDataAvailable,
          paginate: {
            next: '&#62;',
            previous: '&#60;',
          },
        },
        order: [
          [3, 'desc']
        ],
        paging: true,
        pageLength: this.displayedResultsLength,
        columnDefs: this.generateColumnDefs(),
        data: tableData,
        columns: [{
            data: null,
            render: function (data, type, row) {
              const colData = data.sfdcCaseNumber || '';
              return `<span class="colData">${colData}</span>`
            }
          },
          {
            data: null,
            render: function (data, type, row) {
              const colData = data.topicOfInquiry.value || '';
              return `<span class="colData">${colData}</span>`
            }
          },
          {
            data: null,
            render: function (data, type, row) {
              const colData = `${data.firstName} ${data.lastName}` || '';
              return `<span class="colData">${colData}</span>`
            }
          },
          {
            data: null,
            render: function (data, type, row) {
              if (data.creationDate) {
                const modifiedDateValue = data.creationDate.split('-');
                const modifiedDate = modifiedDateValue[2].split('T');
                const dateValue = `${modifiedDateValue[1]}/${modifiedDate[0]}/${
                  modifiedDateValue[0]
                  }`;
                const colData = dateValue || '';
                return `<span class="colData">${colData}</span>`
              }
            }
          },
          {
            data: null,
            render: function (data, type, row) {
              const colData = data.sfdcStatus || '';
              return `<span class="colData">${colData}</span>`
            }
          },
          {
            data: null,
            render: function (data, type, row) {
              const colData = data.sfdcServiceAgent || '';
              return `<span class="colData">${colData}</span>`
            }
          },
          {
            className: 'details-control',
            orderable: false,
            data: null,
            render(data, type, row) {
              return `<span class="icon-chevron-down accordion-click"></span>`;
            },
          },
        ],
      });
      $('#support-ticket-data-table')
        .DataTable()
        .row(0)
        .every(function () {
          const firstRow = this.row();
          firstRow.child(self.createAccordionInTable(firstRow.data())).show();
          var tr = firstRow.nodes()[0];
          var span = $(tr.lastChild.children[0]);
          var accordion = $(tr.nextSibling.children[0]);
          accordion.addClass('open-accordion');
          $(tr).addClass('shown');
          span.addClass('icon-chevron-up');
          span.removeClass('icon-chevron-down');
        });
      $('#support-ticket-data-table th').on('click', function () {
        $('.shown').each(function () {
          const accordion = $(this);
          const accordionRow = self.supportTicketTable.row(accordion);
          if (accordionRow.child.isShown()) {
            accordionRow.child.hide();
          }
        });
        let trone = $('tr.odd:first');
        const firstRow = self.supportTicketTable.row(trone);
        firstRow.child(self.createAccordionInTable(firstRow.data())).show();
        var tr = firstRow.nodes()[0];
        var span = $(tr.lastChild.children[0]);
        var accordion = $(tr.nextSibling.children[0]);
        accordion.addClass('open-accordion');
        $(tr).addClass('shown');
        span.addClass('icon-chevron-up');
        span.removeClass('icon-chevron-down');
      });
      $('#support-ticket-data-table tbody').on(
        'click',
        'td.details-control',
        function () {
          const tr = $(this).closest('tr');
          const row = self.supportTicketTable.row(tr);
          const span = $(this.children[0]);
          if (row.child.isShown()) {
            // This row is already open - close it
            row.child.hide();
            tr.removeClass('shown');
            span.removeClass('icon-chevron-up');
            span.addClass('icon-chevron-down');
          } else {
            // Open this row
            $('.shown').each(function () {
              const accordion = $(this);
              const accordionSpan = $(this.lastChild.children[0]);
              accordion.removeClass('shown');
              accordionSpan.removeClass('icon-chevron-up');
              accordionSpan.addClass('icon-chevron-down');
              const accordionRow = self.supportTicketTable.row(accordion);
              if (accordionRow.child.isShown()) {
                accordionRow.child.hide();
              }
            });
            row.child(self.createAccordionInTable(row.data())).show();
            const accordion = $(tr[0].nextSibling.children[0]);
            accordion.addClass('open-accordion');
            tr.addClass('shown');
            span.addClass('icon-chevron-up');
            span.removeClass('icon-chevron-down');
          }
        },
      );
      $('th:first').off().on('click', function () {
        const col = this.cellIndex;
        let sortOrder = '';
        if (col === 0) {
          while (this.classList.length) {
            this.classList.remove(this.classList.item(0));
          }
          if (self.nextSortingOrder === self.supportTicketOrder.DESC) {
            this.classList.add(self.supportTicketOrder.ascClass);
          } else if (self.nextSortingOrder === self.supportTicketOrder.ASC) {
            this.classList.add(self.supportTicketOrder.descClass);
          }
          self.$refs.spinner.showSpinner();
          if (this.className === self.supportTicketOrder.ascClass) {
            self.nextSortingOrder = self.supportTicketOrder.ASC;
            sortOrder = self.supportTicketOrder.ASC;
          } else if (this.className === self.supportTicketOrder.descClass) {
            self.nextSortingOrder = self.supportTicketOrder.DESC;
            sortOrder = self.supportTicketOrder.DESC;
          }
          self.getSupportTicketSortedData(sortOrder);
        }
      });
    },
    handleCreateTicket() {
      this.globals.navigateToUrl('contactUs');
    },
    initSupportTicketData() {
      const requestConfig = {};
      if (globals.isB2C) {
        this.manageProfileShoppingListService.getSupportTicketsB2C(
          requestConfig,
          this.handleSupportTicketsResponse,
          this.handleSupportTicketsError,
        );
      } else if (globals.isB2B) {
        this.manageProfileShoppingListService.getSupportTicketsB2B(
          requestConfig,
          this.handleSupportTicketsResponse,
          this.handleSupportTicketsError,
        );
      }
      this.$refs.spinner.showSpinner();
    },
    handleSupportTicketsResponse(response) {
      this.$refs.spinner.hideSpinner();
      this.tableData = response.data.tickets;
      this.createTable(this.tableData);
    },
    handleSupportTicketsError(error) {
      this.$refs.spinner.hideSpinner();
    },
    getSupportTicketSortedData(order) {
      const requestConfig = {};
      requestConfig.params = {
        sortType: order,
      };
      this.manageProfileShoppingListService.sortSupportTicketData(
        requestConfig,
        this.handleSortSupportTicketDataResponse,
        this.handleSortSupportTicketDataError,
      );
    },
    handleSortSupportTicketDataResponse(response) {
      const self = this;
      const data = response.data.tickets;
      const datatable = $('#support-ticket-data-table').DataTable();
      datatable.destroy();
      this.createTable(data);
      var colHeading = $('th')[0];
      var th = $('th');
      th.each(function () {
        let currentTh = this;
        while (currentTh.classList.length) {
          currentTh.classList.remove(currentTh.classList.item(0));
        }
        currentTh.classList.add(self.supportTicketOrder.sortingClass);
      });
      if (this.nextSortingOrder === this.supportTicketOrder.ASC) {
        while (colHeading.classList.length) {
          colHeading.classList.remove(colHeading.classList.item(0));
        }
        colHeading.classList.add(this.supportTicketOrder.ascClass);
        colHeading.attributes['aria-label'].value = this.i18n.ariaLabelDescending;
        $(colHeading).attr('aria-sort', this.i18n.ariaSortAscending);
      } else if (this.nextSortingOrder === this.supportTicketOrder.DESC) {
        while (colHeading.classList.length) {
          colHeading.classList.remove(colHeading.classList.item(0));
        }
        colHeading.classList.add(this.supportTicketOrder.descClass);
        colHeading.attributes['aria-label'].value = this.i18n.ariaLabelAscending;
        $(colHeading).attr('aria-sort', this.i18n.ariaSortDescending);
      }
      this.$refs.spinner.hideSpinner();
    },
    handleSortSupportTicketDataError(error) {},
    createOptions() {
      const self = this;
      this.tableOrders.map((val, index) => {
        const obj = {};
        obj.label = val;
        obj.index = index;
        if (
          val === self.i18n.ticketNumberAsc ||
          val === self.i18n.topicOfInquiryAsc ||
          val === self.i18n.createdByAsc ||
          val === self.i18n.dateOpenedOldest ||
          val === self.i18n.statusAsc
        ) {
          obj.value = self.order.ascending;
        } else {
          obj.value = self.order.descending;
        }
        self.mobileSortOptions.push(obj);
      });
    },
    triggerSort(e) {
      const partialSortTitles = [];
      this.tableTitles.map((child) => {
        partialSortTitles.push(child.split(' ')[0]);
      });
      const selectedSort = e.label.split(' ')[0];
      if (selectedSort === this.supportTicketOrder.ticket) {
        const self = this;
        const th = $('th:first')[0];
        const sortOrder = e.value.toUpperCase();
        while (th.classList.length) {
          th.classList.remove(th.classList.item(0));
        }
        if (self.nextSortingOrder === self.supportTicketOrder.DESC) {
          th.classList.add(self.supportTicketOrder.ascClass);
        } else if (self.nextSortingOrder === self.supportTicketOrder.ASC) {
          th.classList.add(self.supportTicketOrder.descClass);
        }
        self.$refs.spinner.showSpinner();
        if (th.className === self.supportTicketOrder.ascClass) {
          self.nextSortingOrder = self.supportTicketOrder.ASC;
        } else if (th.className === self.supportTicketOrder.descClass) {
          self.nextSortingOrder = self.supportTicketOrder.DESC;
        }
        self.getSupportTicketSortedData(sortOrder);
      } else {
        const sortValue = partialSortTitles.indexOf(selectedSort);
        this.supportTicketTable.order([
          [sortValue, e.value]
        ]).draw();
      }
    },
    populateDefaultMessage() {
      this.$refs.sortDropdown.setDropdownLabel(this.i18n.sortBy);
    },
  },
};
