import $ from 'jquery';
import dt from 'datatables.net';

import vxDropdownPrimary from '../../common/vx-dropdown-primary/vx-dropdown-primary.vue';
import mobileMixin from '../../common/mixins/mobile-mixin';
import vxCreateList from '../vx-create-list/vx-create-list.vue';
import vxDeleteList from '../vx-delete-list/vx-delete-list.vue';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import globals from '../../common/globals';
import flyoutBannerMixin from '../../common/vx-flyout-banner/vx-flyout-banner-mixin';
import { eventBus } from '../../../modules/event-bus';
import ManageProfileShoppingListService from '../../common/services/manage-profile-shopping-lists-service';
import { order } from '../../common/mixins/vx-enums';
import dataTableUtilsMixin from '../../common/mixins/data-table-utils-mixin';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';

export default {
  name: 'vx-shopping-lists',
  components: {
    vxDropdownPrimary,
    vxCreateList,
    vxDeleteList,
    vxModal,
    vxSpinner,
  },
  props: {
    /**
     * Labels, button and caption texts
     */
    i18n: {
      type: Object,
    },
    /**
     * Indicator for Favorites enabled
     */
    isFavorites: {
      type: Boolean,
    },
  },
  mixins: [mobileMixin, flyoutBannerMixin, dataTableUtilsMixin],
  data() {
    return {
      globals,
      selectedLists: [],
      selectedListNames: [],
      allListNames: [],
      listName: '',
      hideRemoveLists: false,
      enableRemoveLink: false,
      filterHidden: true,
      tableTitles: [
        this.i18n.shoppingLists.checkbox,
        this.i18n.shoppingLists.listName,
        this.i18n.shoppingLists.lastUpdated,
        this.i18n.shoppingLists.noOfProducts,
        this.i18n.shoppingLists.subtotal,
      ],
      tableOrders: [
        this.i18n.shoppingLists.asc,
        this.i18n.shoppingLists.des,
        this.i18n.shoppingLists.latest,
        this.i18n.shoppingLists.old,
        this.i18n.shoppingLists.high,
        this.i18n.shoppingLists.low,
        this.i18n.shoppingLists.highPrice,
        this.i18n.shoppingLists.lowPrice,
      ],
      MobileSortOptions: [],
      manageProfileShoppingListService: new ManageProfileShoppingListService(),
      noLists: false,
      order,
      dataLoaded: true,
      respTable: {},
    };
  },
  mounted() {
    this.createOptions();
    this.enableDateSort($);
    this.onInit();
  },
  updated() {},
  methods: {
    /**
     * This function handles the sorting of all shopping lists service
     */
    triggerSort(e) {
      const partialSortTitles = [];
      this.tableTitles.map((child) => {
        partialSortTitles.push(child.split(' ')[0]);
      });
      let selectedSort = e.label.split(' ')[0];
      selectedSort = selectedSort.replace(':', '');
      const sortValue = partialSortTitles.indexOf(selectedSort);
      this.respTable.order([[sortValue, e.value]]).draw();
      this.filterHidden = !this.filterHidden;
      this.$refs.refineListModal.close();
    },
    /**
     * This function is called when the create list link is clicked
     */
    handleCreateList() {
      this.$refs.createListModal.open();
    },
    /**
     * This function is called when the delete list link is clicked
     */
    handleDeleteList() {
      this.$refs.deleteListModal.open();
    },
    /**
     * This function handles the response of get all shopping lists service
     */
    handleShoppingListsResponse(response) {
      if (this.respTable.data) {
        this.respTable.destroy();
      }
      this.$refs.spinner.hideSpinner();
      this.dataLoaded = true;
      this.getDataTable(response);
      if (response.data.wishlists) {
        this.hideRemoveLists = true;
        this.allListNames = [];
        this.allListNames = response.data.wishlists.map((child) => {
          if (
            child &&
            child.wishlistEntries &&
            child.wishlistEntries.length === 0
          ) {
            this.noLists = true;
          }
          return child.name;
        });
      }
    },
    /**
     * This function is used to define columns for data table
     */
    generateColumnDefs() {
      const columnConfigArray = [];
      this.tableTitles.map((val, index) => {
        const columnnConfig = {};
        if (index === 0) {
          columnnConfig.title = `<label class='checkbox-container'>
          <span hidden>checkbox</span><input name='selectAll' type='checkbox' aria-label='Select all'>
          <span class='checkmark'></span></label>`;
          columnnConfig.width = '5%';
          columnnConfig.className = 'select-checkbox form-group Checkbox-align';
          columnnConfig.searchable = false;
          columnnConfig.orderable = false;
        } else {
          columnnConfig.title = val;
        }
        columnnConfig.targets = index;
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
    /**
     * This function handles the error of get all shopping lists service
     */
    handleShoppingListsError() {
      this.$refs.spinner.hideSpinner();
    },
    /**
     * This function is called to populate the data table
     */
    getDataTable(data) {
      const self = this;
      const rowSelected = [];
      this.respTable = $('#shopping-lists-data-table').DataTable({
        searching: false, // hides search, default to true
        lengthChange: false, // hides the length message, default to true
        info: false, // hides the info
        recordsTotal: 0,
        destroy: true,
        language: {
          emptyTable: 'No saved lists found',
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
        order: [],
        columnDefs: this.generateColumnDefs(),
        data: data.data.wishlists,
        drawCallback(settings) {
          $('.dataTables_paginate  a').attr('href', '#');
          if (settings.aoData.length === 0) {
            $('.dataTables_paginate').hide();
          }
        },
        columns: [
          {
            data: null,
            defaultContent: '',
            render(data, type, row, meta) {
              return `<label class='checkbox-container' >
              <span hidden>checkbox</span><input type='checkbox' class='check' value='${
  meta.row
}' aria-label='Select' >
              <span class='checkmark'></span></label>`;
            },
          },
          {
            data: null,
            defaultContent: '',
            render(data, type, row, meta) {
              return `<a href="${globals.getNavBaseUrl()}${
                globals.serviceUrls.listDetailView
              }/?listName=${data.wishlistUid}">${data.name}</a>`;
            },
          },
          {
            data: null,
            defaultContent: '',
            type: 'extract-date',
            render(data, type, row) {
              if (data.modifiedTime) {
                const modifiedDateValue = data.modifiedTime.split('-');
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
            defaultContent: '\xa0',
            render(data, type, row) {
              return data.noOfProducts ? checkQuantity(data.noOfProducts) : 0;
            },
          },
          {
            data: null,
            defaultContent: '\xa0',
            render(data, type, row) {
              if (data.subtotal) {
                const subtotal = data.subtotal;
                return `${subtotal}`;
              }
              const subtotal = '$00.00';
              return `${subtotal}`;

              return '\xa0';
            },
          },
        ],
      });
      if (this.globals.isGpXpress()) {
        this.respTable.column(4).visible(false);
      }
      if (!data.data.wishlists) {
        this.respTable.clear();
        this.respTable.draw();
        unbindClick();
      } else if (data.data.wishlists.length === 1) {
        unbindClick();
      } else {
        this.respTable.order([1, 'asc']).draw();
      }

      function checkQuantity(data) {
        if (data) {
          return data;
        }
      }

      function unbindClick() {
        $('th')
          .unbind('click')
          .on('focus', function() {
            $(this).css('outline', 'none');
          });
      }

      function updateDataTableSelectAllCtrl(table) {
        const $table = table.table().node();
        const $chkboxAll = $('tbody input.check', $table);
        const $chkboxChecked = $('tbody input.check:checked', $table);
        const chkboxSelectAll = $('thead input[name="selectAll"]', $table).get(
          0,
        );

        // If none of the checkboxes are checked
        if ($chkboxChecked.length === 0) {
          chkboxSelectAll.checked = false;
          if ('indeterminate' in chkboxSelectAll) {
            chkboxSelectAll.indeterminate = false;
            self.enableRemoveLink = false;
          }
          // If all of the checkboxes are checked
        } else if ($chkboxChecked.length === $chkboxAll.length) {
          chkboxSelectAll.checked = true;
          if ('indeterminate' in chkboxSelectAll) {
            chkboxSelectAll.indeterminate = false;
            self.enableRemoveLink = true;
          }
          // If some of the checkboxes are checked
        } else {
          chkboxSelectAll.checked = false;
          if ('indeterminate' in chkboxSelectAll) {
            chkboxSelectAll.indeterminate = true;
            self.enableRemoveLink = true;
          }
        }
      }

      this.respTable
        .columns()
        .header()
        .to$()
        .addClass('all');
      $('#shopping-lists-data-table tbody .select-checkbox').on(
        'click',
        'input.check',
        function(e) {
          const $row = $(this).closest('tr');
          const data = self.respTable.row($row).data();
          const rowId = data.name;
          const index = $.inArray(rowId, rowSelected);

          // If checkbox is checked and row ID is not in list of selected row IDs
          if (this.checked && index === -1) {
            rowSelected.push(rowId);
            this.listName = rowId;
            self.selectedListNames.push(this.listName);
            // this.selectedLists = rowSelected;
            self.selectedLists.push(data.wishlistUid);
            // Otherwise, if checkbox is not checked and row ID is in list of selected row IDs
          } else if (!this.checked && index !== -1) {
            rowSelected.splice(index, 1);
            self.selectedListNames.splice(index, 1);
            self.selectedLists.splice(index, 1);
          }

          if (this.checked) {
            $row.addClass('selected');
          } else {
            $row.removeClass('selected');
          }
          // Update state of "Select all" control
          updateDataTableSelectAllCtrl(self.respTable);
          // Prevent click event from propagating to parent
          e.stopPropagation();
        },
      );
      $('thead input[name="selectAll"]', self.respTable.table().container()).on(
        'click',
        function(e) {
          if (this.checked) {
            $(
              '#shopping-lists-data-table tbody input.check:not(:checked)',
            ).trigger('click');
          } else {
            $('#shopping-lists-data-table tbody input.check:checked').trigger(
              'click',
            );
          }
          // Prevent click event from propagating to parent
          e.stopPropagation();
        },
      );
      self.respTable.on('draw', () => {
        // Update state of "Select all" control
        if (typeof data.data.wishlists !== 'undefined') {
          updateDataTableSelectAllCtrl(self.respTable);
        }
      });
    },
    onInit() {
      this.$refs.spinner.showSpinner();
      const requestConfig = {};
      this.manageProfileShoppingListService.getShoppingLists(
        requestConfig,
        this.handleShoppingListsResponse,
        this.handleShoppingListsError,
      );
    },
    /**
     * This function handles the success emitted from the create list modal component
     */
    deleteModalSuccess(success) {
      this.selectedListNames = [];
      this.selectedLists = [];
      this.allListNames = [];
      this.dataLoaded = false;
      this.onInit();
      this.$refs.deleteListModal.close();
      this.showFlyout('success', success, true);
    },
    /**
     * This function handles the error emitted from the delete list modal component
     */
    deleteModalError(error) {
      this.onInit();
      this.$refs.deleteListModal.close();
      this.showFlyout('error', error, true);
    },
    /**
     * This function handles the success emitted from the create list modal component
     */
    createModalSuccess(success) {
      this.dataLoaded = false;
      this.onInit();
      this.$refs.createListModal.close();
      this.showFlyout('success', success, true);
    },
    showlistDetailPage() {
      eventBus.$emit('show-list', this.listName);
    },
    toggleFilter() {
      this.filterHidden = !this.filterHidden;
      this.$refs.refineListModal.open();
    },
    closeRefineListModal() {
      this.filterHidden = !this.filterHidden;
    },
    createOptions() {
      const self = this;
      this.tableOrders.map((val, index) => {
        const obj = {};
        obj.label = val;
        obj.index = index;
        if (
          val === self.i18n.shoppingLists.asc ||
          val === self.i18n.shoppingLists.old ||
          val === self.i18n.shoppingLists.low ||
          val === self.i18n.shoppingLists.lowPrice
        ) {
          obj.value = self.order.ascending;
        } else {
          obj.value = self.order.descending;
        }
        self.MobileSortOptions.push(obj);
      });
    },
    populateDefaultMessage() {
      this.$refs.sortDropdown.setDropdownLabel(this.i18n.shoppingLists.sortBy);
    },
  },
};
