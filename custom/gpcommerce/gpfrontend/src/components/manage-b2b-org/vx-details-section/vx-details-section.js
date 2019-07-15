/* Top section on details/view pages */
import { userStates } from '../../common/mixins/vx-enums';

export default {
  name: 'vx-details-section',
  components: {},
  props: {
    /**
     * Condition to display or hide edit button
     */
    editButton: {
      type: Boolean,
      required: true,
    },

    /**
     * Condition to display or hide disable button
     */
    disableButton: {
      type: Boolean
    },

    /**
     * Condition to display or hide the hyperlink
     */
    linkButton: {
      type: Boolean
    },

    /**
     * Data for details section with default value
     */
    detailsData: {
      type: Object,
      required: true,
      default: {
        id: 'Unique ID',
        status: false,
        // displayData generated from parent component as per the use case
        displayData: {},
      },
    },

    /**
     * Labels and button texts
     */
    i18n: {
      type: Object,
      required: true,
    },
    i18nUserStatus: {
      type: Object,
      required: true,
    },
    currentPage: {
      type: String,
    },

    /**
     * Flag to check if the section is for unit details
     */
    isUnitDetails: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      statusText: userStates.status,
    };
  },
  computed: {},
  mounted() {},
  methods: {
    /**
     * Called when edit button is clicked
     */
    handleEdit() {
      /**
       * Edit event.
       *
       * @event edit
       * @type {string}
       */
      this.$emit('edit', this.detailsData.id);
    },

    /**
     * Called when disable button is clicked
     */
    handleState() {
      if (this.detailsData.status) {
        /**
         * Disable event.
         *
         * @event disable
         * @type {string}
         */
        this.$emit('disable', this.detailsData.id);
      } else {
        /**
         * Enable event.
         *
         * @event enable
         * @type {string}
         */
        this.$emit('enable', this.detailsData.id);
      }
    },

    /**
     * Called when hyperlink is clicked
     */
    handleLink() {
      /**
       * Hyperlink click event.
       *
       * @event link
       * @type {string}
       */
      this.$emit('link', this.detailsData.id);
    },
    showStatus() {
      if (this.detailsData.status && userStates.pending === this.detailsData.userApprovalStatus) {
        return this.i18nUserStatus.pending;
      } else if (this.detailsData.status) {
        return this.i18nUserStatus.enabled;
      } else {
        return this.i18nUserStatus.disabled;
      }
    }
  },
};
