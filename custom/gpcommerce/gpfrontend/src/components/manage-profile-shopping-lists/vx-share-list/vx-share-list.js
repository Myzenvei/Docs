import globals from '../../common/globals';
import ManageProfileShoppingListService from '../../common/services/manage-profile-shopping-lists-service';
import {
  Validator,
} from 'vee-validate';
import detectDeviceMixin from '../../common/mixins/detect-device-mixin';

const defaultRecipients = [];
export default {
  name: 'vx-share-list',
  mixins: [detectDeviceMixin],
  props: {
    i18n: {
      type: Object,
    },
    wishlistId: {
      type: String,
    },
    listId: {
      type: String,
    },
  },
  data() {
    return {
      globals,
      manageProfileShoppingListService: new ManageProfileShoppingListService(),
      recipients: defaultRecipients,
      recipientValue: defaultRecipients.join(),
    };
  },
  watch: {
    recipientValue(val) {
      this.recipients = val.split(',').map(value => value.trim());
    },
  },
  computed: {

  },
  mounted() {
    const veeCustomErrorMessage = {
      en: {
        custom: {
          recipients: {
            required: this.i18n.shareList.shareListRequired,
            email: this.i18n.shareList.shareListError,
          },
        },
      },
    };
    Validator.localize(veeCustomErrorMessage);
  },
  methods: {
    onSubmit() {
      const self = this;
      self.$validator.validateAll().then((result) => {
        if (result) {
          const requestConfig = {};
          requestConfig.params = self.createRequestbody();
          self.manageProfileShoppingListService.shareList(requestConfig, self.handleShareListResponse, self.handleShareListError, this.wishlistId);
        } else {
          this.globals.setFocusByName(this.$el, this.globals.getElementName(this.errors));
        }
      });
    },
    createRequestbody() {
      const requestBody = {
        wishlistId: this.wishlistId,
        recipientemails: this.recipients.toString(),
      };
      return requestBody;
    },
    handleShareListResponse(response) {
      if (response) {
        this.$emit('share-list-success', this.i18n.listDetails.shareItemResponse);
      }
    },
    handleShareListError(error) {
      if (error) {
        this.$emit('share-list-error', error.response.data.errors[0].message);
      }
    },
    setCartId(cartId) {
      this.shareCartId = cartId;
    },
  },
};
