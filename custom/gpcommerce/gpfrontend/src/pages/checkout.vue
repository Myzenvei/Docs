<template>
<div class="row checkout-page">
<div class="col-xs-12 col-md-8 px-xs-0 pr-sm-5">
  <vx-checkout :i18n="messages.checkout"></vx-checkout>
</div>
<div class="col-xs-12 col-md-4 px-xs-0" style="margin-top: 71px">
  <vx-order-summary :i18n="messages.manageShoppingCart.orderSummary"></vx-order-summary>
</div>
<div class="col-xs-12 px-xs-0">
  <vx-guest-marketing-communications :i18n="messages.checkout.guestMarketCommunication"></vx-guest-marketing-communications>
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
import vxGuestMarketingCommunications from '../components/common/vx-guest-marketing-communications/vx-guest-marketing-communications.vue';

export default {
  name: 'checkout',
  components: {
    vxCheckout,
    vxOrderSummary,
    vxGuestMarketingCommunications,
  },
  data() {
    return {
      messages: messages['en-US'],
    };
  },
  methods: {},
};
</script>
