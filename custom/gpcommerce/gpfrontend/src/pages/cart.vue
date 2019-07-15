<template>
<div class="row cart-page" style="margin: 40px">
<div class="col-xs-12 col-md-8 ">
<vx-shopping-cart :i18n="messages.manageShoppingCart.shoppingCart"></vx-shopping-cart>
</div>
<div class="col-xs-12 col-md-4" style="margin-top: 71px">
  <vx-order-summary :i18n="messages.manageShoppingCart.orderSummary"></vx-order-summary>
</div>
<div class="col-xs-12">
  <vx-empty-cart :i18n="messages.manageShoppingCart.emptyShoppingCart"></vx-empty-cart>
</div>
</div>
</template>

<script>
import vxShoppingCart from '../components/manage-shopping-cart/vx-shopping-cart/vx-shopping-cart.vue';
import vxShareCart from '../components/manage-shopping-cart/vx-share-cart/vx-share-cart.vue';
import vxModal from '../components/common/vx-modal/vx-modal.vue';
import messages from '../locale/messages';
import vxMiniCart from '../components/manage-shopping-cart/vx-mini-cart/vx-mini-cart.vue';
import vxSaveCart from '../components/manage-shopping-cart/vx-save-cart/vx-save-cart.vue';
import vxOrderSummary from '../components/manage-shopping-cart/vx-order-summary/vx-order-summary.vue';
import vxEmptyCart from '../components/manage-shopping-cart/vx-empty-cart/vx-empty-cart.vue';

export default {
  name: 'cart',
  components: {
    vxShoppingCart,
    vxShareCart,
    vxModal,
    vxMiniCart,
    vxSaveCart,
    vxOrderSummary,
    vxEmptyCart,
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
