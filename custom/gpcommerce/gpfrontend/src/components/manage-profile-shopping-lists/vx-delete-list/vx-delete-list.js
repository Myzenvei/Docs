import globals from '../../common/globals';
import ManageProfileShoppingListService from '../../common/services/manage-profile-shopping-lists-service';

export default {
  name: 'vx-delete-list',
  components: {},
  props: {
    /**
     * All copy texts coming from properties files
     */
    i18n: {
      type: Object,
    },
    /**
     * array of selected lists
     */
    selectedLists: {
      type: Array,
    },
    /**
     * array of selected lists
     */
    selectedListsName: {
      type: Array,
    },
  },
  data() {
    return {
      globals,
      manageProfileShoppingListService: new ManageProfileShoppingListService(),
      selectedListNames: [],
      multipleLists: false,
    };
  },
  computed: {},
  mounted() {
    if (this.selectedListsName.length > 1) {
      this.multipleLists = true;
    }
  },
  methods: {
    /**
     * This function is called when delete button is clicked
     */
    deleteList() {
      const requestConfig = {};
      requestConfig.data = this.selectedLists;
      this.manageProfileShoppingListService.deleteList(requestConfig, this.handleDeleteListResponse, this.handleDeleteListError);
    },
    /**
     * This function handles the response of the delete list service
     */
    handleDeleteListResponse(response) {
      if (response) {
        this.$emit('close-delete-success', this.i18n.deleteList.deleteListSuccess);
      }
    },
    /**
     * This function handles the error of the delete list service
     */
    handleDeleteListError(error) {
      if (error) {
        this.$emit('close-delete-error', error.response.data.errors[0].message);
      }
    },
  },
};
