import globals from '../../common/globals';
import ManageProfileShoppingListService from '../../common/services/manage-profile-shopping-lists-service';
import {
  Validator,
} from 'vee-validate';

export default {
  name: 'vx-create-list',
  components: {},
  props: {
    /**
     * All copy texts coming from properties files
     */
    i18n: {
      type: Object,
    },
    /**
     * all the lists in the list landing table
     */
    allListNames: {
      type: Array,
    },
  },
  data() {
    return {
      globals,
      listName: '',
      inlineError: false,
      existingList: false,
      emptyListError: false,
      maxNameLength: 50,
      manageProfileShoppingListService: new ManageProfileShoppingListService(),
    };
  },
  computed: {},
  watch: {
    /**
     * Error thrown when trying to create list with same name as existing list is removed during any keypress in input field
     */
    listName() {
      if (this.inlineError) {
        this.inlineError = false;
      }
    },
  },
  mounted() {
    /**
     * Provides Custom error messages for Vee validate conditions to run validation on input fields
     */
    const veeCustomErrorMessage = {
      en: {
        custom: {
          listName: {
            required: this.i18n.createList.emptyListError,
            max: this.i18n.createList.maxListNameError,
          },
        },
      },
    };
    Validator.localize(veeCustomErrorMessage);
  },
  methods: {
    /**
     * This function is called when create button is clicked
     */
    createAList() {
      this.$validator.validateAll().then((result) => {
        if (result) {
          const listNames = this.allListNames.map(x =>
            x.toLowerCase());
          this.existingList = listNames.indexOf(this.listName.toLowerCase());
          if (this.existingList === -1) {
            if (this.listName === '') {
              this.emptyListError = true;
            } else {
              const requestdata = {
                name: this.listName.trim(),
              };
              const requestConfig = {};
              requestConfig.data = requestdata;
              this.manageProfileShoppingListService.createList(requestConfig, this.handleCreateListResponse, this.handleCreateListError);
            }
          } else if (this.listName !== '' && this.existingList > -1) {
            this.inlineError = true;
          }
        } else {
          this.globals.setFocusByName(this.$el, this.globals.getElementName(this.errors));
        }
      });
    },
    /**
     * This function handles the response of the create list service
     */
    handleCreateListResponse(response) {
      if (response && response.data) {
        this.$emit('close-create-success', this.i18n.createList.createListSuccess);
      }
    },
    /**
     * This function handles the error of the create list service
     */
    handleCreateListError(error) {
      if (error) {
        this.inlineError = true;
      }
    },
  },
};
