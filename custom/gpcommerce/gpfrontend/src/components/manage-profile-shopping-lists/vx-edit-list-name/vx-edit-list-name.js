import globals from '../../common/globals';
import ManageProfileShoppingListService from '../../common/services/manage-profile-shopping-lists-service';

import {
  Validator
} from 'vee-validate';

export default {
  name: 'vx-edit-list-name',
  props: {
    /**
     * All copy texts coming from properties files
     */
    i18n: {
      type: Object,
    },
    /**
     * get list id
     */
    listName: {
      type: String,
    },
    /**
     * get list name
     */
    listId: {
      type: String,
    },
  },
  data() {
    return {
      globals,
      manageProfileShoppingListService: new ManageProfileShoppingListService(),
    };
  },
  computed: {

  },
  mounted() {
    /**
     * Provide Custom error messages for Vee validate conditions to run validation on input fields
     */
    const veeCustomErrorMessage = {
      en: {
        custom: {
          listName: {
            required: this.i18n.editListName.listNameRequiredError,
            max: this.i18n.editListName.listNameMaxError,
          }
        },
      },
    };
    Validator.localize(veeCustomErrorMessage);
  },
  methods: {
    /**
     * This function is called when edit button is clicked
     */
    editListName() {
      const self = this;
      self.$validator.validateAll().then((result) => {
        if (result) {
          const requestConfig = {};
          requestConfig.params = {
            listName: self.listId,
          };
          self.manageProfileShoppingListService.editListName(requestConfig, self.handleEditListNameResponse, self.handleEditListNameError, this.listName);
        } else {
          this.globals.setFocusByName(this.$el, this.globals.getElementName(this.errors));
        }
      });
    },
    /**
     * This function handles the error of edit list name service
     */
    handleEditListNameResponse(response) {
      if (response) {
        this.$emit('close-edit-success', this.i18n.editListName.editListSuccess);
      }
    },
    /**
     * This function handles the response of edit list name service
     */
    handleEditListNameError(error) {
      if (error) {
        this.$emit('close-edit-error', error.response.data.errors[0].message);
      }
    },

  },
};
