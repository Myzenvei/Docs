/**
 * Component for the share cart/share list functionality
 */
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import globals from '../../common/globals';
import ManageShoppingCartService from '../../common/services/manage-shopping-cart-service';
import detectDeviceMixin from '../../common/mixins/detect-device-mixin';
import mobileMixin from '../../common/mixins/mobile-mixin';

const defaultRecipients = [];
export default {
  name: 'vx-share-cart',
  components: {
    vxSpinner,
  },
  mixins: [detectDeviceMixin, mobileMixin],
  props: {
    /**
     * text coming from property file
     */
    i18n: Object,
    /**
     * current cart Id
     */
    cartId: String,
    /**
     * product list
     */
    wishlistEntries: [],
  },
  data() {
    return {
      globals,
      recipients: defaultRecipients,
      recipientValue: defaultRecipients.join(),
      senderNameValue: '',
      senderEmailValue: '',
      shareCartId: this.cartId,
      queryInput: [],
      manageShoppingCartService: new ManageShoppingCartService(),
    };
  },

  watch: {
    recipientValue(val) {
      this.recipients = val.split(',').map(value => value.trim());
    },
  },

  computed: {},
  mounted() {
    if (globals.getIsLoggedIn()) {
      this.senderNameValue = `${this.globals.userInfo.firstName} ${this.globals.userInfo.lastName}`;
      this.senderEmailValue = this.globals.isGpXpress() ? this.globals.userInfo.userEmail : this.globals.userInfo.email;
    }
    this.handleResize();
    window.addEventListener('resize', this.handleResize);
  },
  methods: {
    /**
     * This function calls the share cart/ share wishlist service
     */
    onSubmit() {
      const self = this;
      const queryInput = [];
      self.$validator.validateAll().then((result) => {
        if (result) {
          const requestConfig = {};
          if (this.wishlistEntries && this.wishlistEntries.length) {
            self.wishlistEntries.map((product) => {
              queryInput.push(self.filterList(product, 'code', 'quantity', 'cmirCode'));
            });
            requestConfig.data = self.createRequestbody(queryInput);
            self.manageShoppingCartService.shareWishlist(
              requestConfig,
              this.handleShareCartResponse,
              this.handleShareCartError,
            );
          } else {
            requestConfig.params = self.createRequestbody();
            self.manageShoppingCartService.shareCart(
              requestConfig,
              this.handleShareCartResponse,
              this.handleShareCartError,
            );
          }
          self.$refs.spinner.showSpinner();
        } else {
          this.globals.setFocusByName(this.$el, this.globals.getElementName(this.errors));
        }
      });
    },
    /**
     * This function creates the requestbody for share cart service
     * @param  {Array} queryInput wishlistEntries details
     */
    createRequestbody(queryInput) {
      const requestBody = {};
      if (queryInput && queryInput.length) {
        requestBody.emailIds = this.recipients.toString();
        requestBody.items = queryInput;
      } else {
        requestBody.recipientemails = this.recipients.toString();
        requestBody.senderEmail = this.senderEmailValue;
        requestBody.senderName = this.senderNameValue;
      }
      return requestBody;
    },
    /**
     * This function remaps object
     * @param  {Object} object Object from which the remapped object is to be created
     * @param  {String} code object key
     * @param  {String} qty object key
     * @param  {String} cmir object key
     */
    filterList(object, code, qty, cmir) {
      const remapedObject = {
        code: object[code],
        count: object[qty],
        isValidSKU: true,
        cmirCode: object[cmir],
      };
      return remapedObject;
    },
    /**
     * This function handles the response share cart call
     */
    handleShareCartResponse(response) {
      this.$emit('share-success');
      this.$refs.spinner.hideSpinner();
    },
    /**
     * This function handles the error share cart call
     */
    handleShareCartError(error) {
      this.$emit('share-error');
      this.$refs.spinner.hideSpinner();
    },
    /**
     * This function sets the card id
     * @param  {String} cartId cart id
     */
    setCartId(cartId) {
      this.shareCartId = cartId;
    },
    handleResize() {
      if (document.querySelector('.share-cart-body')) {
        if (this.isMobile()) {
          document.querySelector('.share-cart-body').style.height = `${window.innerHeight - 175}px`;
        } else {
          document.querySelector('.share-cart-body').style.height = '';
        }
      }
    },
    destroyed() {
      window.removeEventListener('resize', this.handleResize);
    },
  },
};
