import vxCheckboxGroup from '../../common/vx-checkbox-group/vx-checkbox-group.vue';
import vxDropdownPrimary from '../../common/vx-dropdown-primary/vx-dropdown-primary.vue';
import mockOrderApproval from './mockOrderApproval';

export default {
  name: 'vx-refine-approval-list',
  components: {
    vxCheckboxGroup,
    vxDropdownPrimary,
  },
  props: {
    /**
     * Labels, button and caption texts
     */
    i18n: {
      type: Object,
      required: true,
    },

    /**
     * Checkbox option values array, to know that whether it is selected or not
     */
    filteredOptions: {
      type: Array,
      default: [],
    },
    /**
     * Selected options value getting from Approvals page.
     */
    selectedOptionsFromApprovals: {
      type: Array,
      required: true,
    },

    /**
     * Selected options items getting from Approvals page.
     */
    selectedItemsFromApprovals: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {
      selectedOptions: [],
      mockOrderApproval,
      dateRange: {
        label: 'Last 6 Months',
        value: 6,
      },
      // Order History transaction date dropdown data
      orderApprovalDropdownData: {
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
      currentYear: '',
      statusError: '',
      filterError: false,
      filterOptions: [],
      selectedObject: [],
    };
  },
  computed: {},
  mounted() {
    this.selectedOptions = this.selectedOptionsFromApprovals;
    this.selectedItem = this.selectedItemsFromApprovals;
    this.filterOptions = this.i18n.statusOptions.options.map((option) => {
      if (this.filteredOptions.includes(option.value)) {
        option.isChecked = true;
      } else {
        option.isChecked = false;
      }
      return option;
    });
    // To get current date and to push in transaction date dropdown
    const newDate = new Date();
    this.currentYear = newDate.getFullYear();
    this.orderApprovalDropdownData.options.push({
      label: this.currentYear,
      value: parseInt(this.currentYear, 10),
    });
    this.orderApprovalTimeData(this.currentYear);
  },
  methods: {
    filterAction(event) {
      if (event === 'clear') {
        this.$refs.statusCheckbox.resetAllCheckboxValues();
      } else {
        const filterData = {
          action: event,
          data: this.selectedOptions,
          selectedItem: this.selectedObject,
          dateRange: this.dateRange,
        };
        this.$emit('filterData', filterData);
      }
    },
    checkedOptions(data) {
      this.selectedOptions = data.selectedValue;
      this.selectedObject = data.selectedObject;
    },
    populateDefaultDate() {
      this.$refs.orderApprovalData.setDropdownValue(this.dateRange.value);
    },
    // Order history years data population
    orderApprovalTimeData(year) {
      let priorYear = parseInt(year, 10);
      const orderCreationYear = this.mockOrderApproval.orders[0].creationTime.split(
        '-',
      )[0];
      const totalPriorYears = year - parseInt(orderCreationYear, 10);
      for (let i = 0; i < totalPriorYears; i += 1) {
        priorYear -= 1;
        this.orderApprovalDropdownData.options.push({
          label: priorYear,
          value: priorYear,
        });
      }
    },
    dateFilterSelected(dateRange) {
      this.dateRange = dateRange;
    },
    displayStatusError(error) {
      this.filterError = true;
      this.statusError = error.response.data.errors[0].message;
    },
  },
};
