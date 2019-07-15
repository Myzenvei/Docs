import globals from '../../common/globals';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import CheckoutService from '../../common/services/checkout-service';

import {
  Validator,
} from 'vee-validate';

export default {
  name: 'vx-guest-checkout',
  components: {
    vxSpinner,
  },
  props: {
    i18n: Object,
    checkoutEnabled: Boolean,
  },
  data() {
    return {
      checkoutService: new CheckoutService(),
      globals,
      guestEmail: '',
    };
  },
  computed: {

  },
  mounted() {
    const veeCustomErrorMessage = {
      en: {
        custom: {
          email: {
            required: this.i18n.emailRequiredError,
            email: this.i18n.emailInvalidError,
          },
        },
      },
    };
    Validator.localize(veeCustomErrorMessage);
  },
  methods: {
    handleGuestCheckoutSubmit(event) {
      event.preventDefault();
      if (this.guestEmail) {
        if (this.checkoutEnabled) {
          this.$refs.guestForm.submit();
          this.checkoutService.getGuestCheckout({}, this.handleGuestCheckoutResponse, this.handleGuestCheckoutError, this.guestEmail);
          this.$refs.spinner.showSpinner();
        }
      } else {
        this.globals.setFocusByName(this.$el, this.globals.getElementName(this.errors));
      }
    },
    handleGuestCheckoutResponse() {
      this.$refs.spinner.hideSpinner();
      this.globals.navigateToUrl('checkout');
    },
    handleGuestCheckoutError(error) {
      console.log(error);
      this.$refs.spinner.hideSpinner();
    },
    hideServerError() {

    },
  },
};
