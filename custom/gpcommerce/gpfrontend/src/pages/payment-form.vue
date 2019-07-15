<template class="container">
  <div class="main__inner-wrapper">
    <div class="container">
      <div class="row">
        <div class="col-sm-12">
          <vx-payment-methods :i18n="messages.manageProfileShoppingLists"></vx-payment-methods>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import globals from '../components/common/globals';
import vxPaymentMethods from '../components/common/vx-payment-methods/vx-payment-methods.vue';
import messages from '../locale/messages';

export default {
  name: 'PaymentForm',
  components: {
    'vx-payment-methods': vxPaymentMethods,
  },
  data() {
    return {
      msg: 'Payment Form and Methods',
      globals,
      messages: messages['en-US'],
      sectionTitle: {
        tag: 'h1',
        title: 'iFrame Payment Gateway',
      },
    };
  },
  mounted() {},
  methods: {
    onPaymentFormError(error) {
      this.$refs.paymentFormModal.close();
      this.showFlyout('error', error, true);
    },
    onPaymentFormSuccess(success) {
      this.$refs.paymentFormModal.close();
      this.showFlyout('success', success, true);
    },
  },
};
</script>
