/* Sections below the details section on details/view pages */

import globals from '../../common/globals';
import vxProfileCard from '../../common/vx-profile-card/vx-profile-card.vue';

export default {
  name: 'vx-info-section',
  components: {
    vxProfileCard,
  },
  props: {
    /**
     * Display info cards with active true only
     */
    displayActiveOnly: {
      type: Boolean,
      required: true,
    },
    /**
     * Condition to display or hide new button
     */
    newButton: {
      type: Boolean,
      required: true,
    },

    /**
     * Condition to display or hide existing button
     */
    existingButton: {
      type: Boolean,
      required: true,
    },

    /**
     * The class for the icon to be displayed in cards (optional)
     * Example `icon-trash`
     */
    iconClass: {
      type: String,
      required: false,
    },

    /**
     * Data for info section with default value
     */
    infoData: {
      true: Array,
      required: true,
      default: [
        {
          label: 'Unique ID',
          info: 'Description',
          link: 'Link',
          // status is optional
          status: false,
        },
      ],
    },

    /**
     * Labels and button texts
     */
    i18n: {
      type: Object,
      required: true,
    },

    /**
     * Checking whether to delete all items in the section.
     */
    deleteAll: {
      type: Boolean,
      required: true,
      default: true,
    },
  },
  data() {
    return {
      globals,
    };
  },
  computed: {
    childUnits() {
      // computing the number of child units in info section
      return this.displayInfoData.length;
    },
    displayInfoData() {
      if (this.displayActiveOnly) {
        return this.infoData.filter(child => child.status === true);
      }
      return this.infoData;
    },
  },
  mounted() {},
  methods: {
    /**
     * Gets called when Add New button is clicked
     *
     * @param {string} section Identifies the info section from where event is triggered
     */
    addNew(section) {
      /**
       * Add New Event.
       *
       * @event addNewTo
       * @type {string}
       */
      this.$emit('addNewTo', section);
    },

    /**
     * Gets called when Add Existing button is clicked
     *
     * @param {string} section Identifies the info section from where event is triggered
     */
    addExisting(section) {
      /**
       * Add Existing Event.
       *
       * @event addExistingTo
       * @type {string}
       */
      this.$emit('addExistingTo', section);
    },

    /**
     * Gets called when delete icon in cards is clicked
     *
     * @param {string} section Identifies the info section from where event is triggered
     * @param {string} item Identifies the child unit to be deleted from info section
     */
    handleDelete(section, itemLabel, item) {
      /**
       * Delete Event.
       *
       * @event delete
       * @type {Object}
       */
      this.$emit('delete', { delete: itemLabel, from: section, item });
    },
  },
};
