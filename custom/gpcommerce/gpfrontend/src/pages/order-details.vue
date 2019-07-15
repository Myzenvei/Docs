<template>
<div class="row">
<div class="col-xs-12">
<vx-order-details :i18n="messages.manageTransactionHistory"></vx-order-details>
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
import vxOrderDetails from '../components/manage-transaction-history/vx-order-details/vx-order-details.vue';

export default {
  name: 'order-details',
  components: {
    vxOrderDetails,
  },
  data() {
    return {
      messages: messages['en-US'],
    };
  },
  methods: {},
};
</script>
