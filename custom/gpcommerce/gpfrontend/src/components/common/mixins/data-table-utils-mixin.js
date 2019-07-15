import $ from 'jquery';
import dt from 'datatables.net';

/**
 * Various utility funcctions for DataTables.
 */
const dataTableUtilsMixin = {
  methods: {
    /**
     * Method to sort currency column in DataTables.
     */
    sortCurrencyCol() {
      $.extend($.fn.dataTableExt.oSort, {
        'currency-pre': function(a) {
          const diff = a === '-' ? 0 : a.replace(/[^\d\-.]/g, '');
          return parseFloat(diff);
        },
        'currency-asc': function(a, b) {
          return a - b;
        },
        'currency-desc': function(a, b) {
          return b - a;
        },
      });
    },

    /**
     * Method to sort Date column in DataTables.
     * @param {*} $
     */
    enableDateSort($) {
      $.extend($.fn.dataTableExt.oSort, {
        'extract-date-pre': function(value) {
          let date = value;
          date = date.split('/');
          return Date.parse(`${date[0]}/${date[1]}/${date[2]}`);
        },
        'extract-date-asc': function(a, b) {
          const greaterVal = a > b ? 1 : 0;
          return a < b ? -1 : greaterVal;
        },
        'extract-date-desc': function(a, b) {
          const greaterVal = a > b ? -1 : 0;
          return a < b ? 1 : greaterVal;
        },
      });
    },

    /**
     * Function to trigger sort in mobile devices.
     */
    triggerSort(e, tableId) {
      const sortValue = e.value.split('-');
      const respTable = $(tableId).DataTable();
      respTable.order([[sortValue[0], sortValue[1]]]).draw();
    },
  },
};

export default dataTableUtilsMixin;
