<template>
<div class="row">

<div class="col-xs-12">
<vx-order-confirmation :i18n="messages.checkout"></vx-order-confirmation>
</div>
<div class="col-md-8 col-sm-12 col-xs-12 pr-mb-5 pr-xs-1">
  <vx-guest-create-account></vx-guest-create-account>
</div>
</div>
</template>

<style scoped>
.checkout-page {
  margin: 40px;
}
@media only screen and (max-width: 767px) {
  .checkout-page {
    margin: 16px;
  }
}
</style>

<script>
import messages from '../locale/messages';
import vxCheckout from '../components/checkout/vx-checkout/vx-checkout.vue';
import vxOrderSummary from '../components/manage-shopping-cart/vx-order-summary/vx-order-summary.vue';
import vxOrderConfirmation from '../components/checkout/vx-order-confirmation/vx-order-confirmation.vue';
import vxGuestCreateAccount from '../components/checkout/vx-guest-create-account/vx-guest-create-account.vue';

export default {
  name: 'checkout',
  components: {
    vxCheckout,
    vxOrderSummary,
    vxOrderConfirmation,
    vxGuestCreateAccount,
  },
  data() {
    return {
      messages: messages['en-US'],
    };
  },
  methods: {},
};
</script>
