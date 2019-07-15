<template>

<vx-quick-order :i18n="messages.quickOrder"></vx-quick-order>
</template>

<script>
import messages from '../locale/messages';
import vxQuickOrder from '../components/quick-order/vx-quick-order/vx-quick-order.vue';

export default {
  name: 'cart',
  components: {
    vxQuickOrder,
  },
  data() {
    return {
      messages: messages['en-US'],
      showMiniCart: false,
    };
  },
  methods: {
    openModal() {
      this.$refs.shareCartModal.open();
    },
    opensaveCartModal() {
      this.$refs.saveCartModal.open();
    },
    handleSuccess() {
      this.$refs.shareCartModal.close();
    },
    handleError() {
      this.$refs.shareCartModal.close();
    },
  },
};
</script>
