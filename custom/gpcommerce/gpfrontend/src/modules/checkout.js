import vxGuestCheckout from '../components/checkout/vx-guest-checkout/vx-guest-checkout.vue';
import vxCheckout from '../components/checkout/vx-checkout/vx-checkout.vue';
import vxPlaceOrder from '../components/checkout/vx-place-order/vx-place-order.vue';
import vxOrderConfirmation from '../components/checkout/vx-order-confirmation/vx-order-confirmation.vue';
import vxGuestCreateAccount from '../components/checkout/vx-guest-create-account/vx-guest-create-account.vue';

const checkoutModule = {
  components: {
    vxGuestCheckout,
    vxCheckout,
    vxPlaceOrder,
    vxOrderConfirmation,
    vxGuestCreateAccount,
  },
};

export default checkoutModule;
