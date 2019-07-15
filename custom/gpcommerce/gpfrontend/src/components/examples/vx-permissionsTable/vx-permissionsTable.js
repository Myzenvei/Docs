import dt from 'datatables.net';
import vxDropdownPrimary from '../../common/vx-dropdown-primary/vx-dropdown-primary.vue';
import mobileMixin from '../../common/mixins/mobile-mixin';

export default {
  name: 'vx-permissions-table',
  components: {
    vxDropdownPrimary,
  },
  props: [],
  mixins: [mobileMixin],
  data() {
    return {
      tableTitles: [
        {
          title: 'ID',
        },
        {
          title: 'Type',
        },
        {
          title: 'Business Unit',
        },
        {
          title: 'Time Period',
        },
        {
          title: 'Amount',
        },
        {
          title: 'Status',
        },
      ],
      permissions: [
        {
          active: true,
          b2BPermissionTypeData: {
            code: 'B2BOrderThresholdPermission',
            name: 'Allowed Order Threshold (per order)',
          },
          code: 'Rustic 20K USD ORDER',
          currency: {
            active: false,
            isocode: 'USD',
            name: 'US Dollar',
            symbol: '$',
          },
          selected: false,
          unit: {
            name: 'Rustic Services',
            uid: 'Rustic Services',
            active: true,
            selectable: false,
            selected: false,
          },
          value: 20000,
        },
        {
          active: true,
          b2BPermissionTypeData: {
            code: 'B2BOrderThresholdPermission',
            name: 'Allowed Order Threshold (per order)',
          },
          code: 'Rustic 20K USD ORDER',
          currency: {
            active: false,
            isocode: 'USD',
            name: 'US Dollar',
            symbol: '$',
          },
          selected: false,
          unit: {
            name: 'Rustic Services',
            uid: 'Rustic Services',
            active: true,
            selectable: false,
            selected: false,
          },
          value: 20000,
        },
        {
          active: true,
          b2BPermissionTypeData: {
            code: 'B2BOrderThresholdPermission',
            name: 'Allowed Order Threshold (per order)',
          },
          code: 'Rustic 20K USD ORDER',
          currency: {
            active: false,
            isocode: 'USD',
            name: 'US Dollar',
            symbol: '$',
          },
          selected: false,
          unit: {
            name: 'Rustic Services',
            uid: 'Rustic Services',
            active: true,
            selectable: false,
            selected: false,
          },
          value: 20000,
        },
        {
          active: true,
          b2BPermissionTypeData: {
            code: 'B2BOrderThresholdTimespanPermission',
            name: 'Allowed Order Threshold (per timespan)',
          },
          code: 'Rustic 25K USD MONTH',
          currency: {
            active: false,
            isocode: 'USD',
            name: 'US Dollar',
            symbol: '$',
          },
          periodRange: 'MONTH',
          selected: false,
          timeSpan: 'MONTH',
          unit: {
            name: 'Rustic Services',
            uid: 'Rustic Services',
            active: true,
            selectable: false,
            selected: false,
          },
          value: 25000,
        },
        {
          active: true,
          b2BPermissionTypeData: {
            code: 'B2BOrderThresholdPermission',
            name: 'Allowed Order Threshold (per order)',
          },
          code: 'Rustic 20K USD ORDER',
          currency: {
            active: false,
            isocode: 'USD',
            name: 'US Dollar',
            symbol: '$',
          },
          selected: false,
          unit: {
            name: 'Rustic Services',
            uid: 'Rustic Services',
            active: true,
            selectable: false,
            selected: false,
          },
          value: 20000,
        },
        {
          active: true,
          b2BPermissionTypeData: {
            code: 'B2BOrderThresholdPermission',
            name: 'Allowed Order Threshold (per order)',
          },
          code: 'Rustic 20K USD ORDER',
          currency: {
            active: false,
            isocode: 'USD',
            name: 'US Dollar',
            symbol: '$',
          },
          selected: false,
          unit: {
            name: 'Rustic Services',
            uid: 'Rustic Services',
            active: true,
            selectable: false,
            selected: false,
          },
          value: 20000,
        },
        {
          active: true,
          b2BPermissionTypeData: {
            code: 'B2BOrderThresholdPermission',
            name: 'Allowed Order Threshold (per order)',
          },
          code: 'Rustic 20K USD ORDER',
          currency: {
            active: false,
            isocode: 'USD',
            name: 'US Dollar',
            symbol: '$',
          },
          selected: false,
          unit: {
            name: 'Rustic Services',
            uid: 'Rustic Services',
            active: true,
            selectable: false,
            selected: false,
          },
          value: 20000,
        },
        {
          active: true,
          b2BPermissionTypeData: {
            code: 'B2BOrderThresholdTimespanPermission',
            name: 'Allowed Order Threshold (per timespan)',
          },
          code: 'Rustic 25K USD MONTH',
          currency: {
            active: false,
            isocode: 'USD',
            name: 'US Dollar',
            symbol: '$',
          },
          periodRange: 'MONTH',
          selected: false,
          timeSpan: 'MONTH',
          unit: {
            name: 'Rustic Services',
            uid: 'Rustic Services',
            active: true,
            selectable: false,
            selected: false,
          },
          value: 25000,
        },
      ],
      sortOptions: [
        {
          label: 'ID',
          value: '0',
        },
        {
          label: 'Type',
          value: '1',
        },
        {
          label: 'Business Unit',
          value: '2',
        },
        {
          label: 'Time Period',
          value: '3',
        },
        {
          label: 'Amount',
          value: '4',
        },
        {
          label: 'Status',
          value: '5',
        },
      ],
    };
  },
  computed: {},
  mounted() {
    const respTable = $('#permissions-table').DataTable({
      searching: false, // hides search, default to true
      lengthChange: false, // hides the length message, default to true
      info: false, // hides the info
      language: {
        emptyTable: 'no data available',
      },
      order: [], // used to set the default order of the column
      columnDefs: [
        {
          targets: 0,
          title: this.tableTitles[0].title,
          createdCell: (cell, cellData, rowData, rowIndex, colIndex) => {
            cell.setAttribute('data-title', 'ID');
          },
        },
        {
          title: this.tableTitles[1].title,
          targets: 1,
          createdCell: (cell, cellData, rowData, rowIndex, colIndex) => {
            cell.setAttribute('data-title', 'Type');
          },
        },
        {
          title: this.tableTitles[2].title,
          targets: 2,
          createdCell: (cell, cellData, rowData, rowIndex, colIndex) => {
            cell.setAttribute('data-title', 'Business Unit');
          },
        },
        {
          title: this.tableTitles[3].title,
          targets: 3,
          createdCell: (cell, cellData, rowData, rowIndex, colIndex) => {
            cell.setAttribute('data-title', 'Time Period');
          },
        },
        {
          title: this.tableTitles[4].title,
          targets: 4,
          createdCell: (cell, cellData, rowData, rowIndex, colIndex) => {
            cell.setAttribute('data-title', 'Amount');
          },
        },
        {
          title: this.tableTitles[5].title,
          targets: 5,
          createdCell: (cell, cellData, rowData, rowIndex, colIndex) => {
            cell.setAttribute('data-title', 'Status');
          },
        },
      ],
      data: this.permissions,
      columns: [
        {
          data: null,
          render(data, type, row, meta) {
            return `<a href="javascript:void(0)">${data.code}</a>`;
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
          render(data, type, row) {
            return data.active;
          },
        },
      ],
    });
    respTable
      .columns()
      .header()
      .to$()
      .addClass('all');
    respTable.rows().every(function() {
      this.child($('<tr>' + '<td colSpan="6">.1</td>' + '</tr>')).show();
    });
  },
  methods: {
    triggerSort(e) {
      const sortValue = e.value;
      const respTable = $('#permissions-table').DataTable();
      respTable.order([[sortValue, 'dsc']]).draw();
    },
  },
};
