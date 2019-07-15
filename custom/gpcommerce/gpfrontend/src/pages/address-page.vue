<template>
<div class="pdp-page">
  <div class="container-fluid">
    <div class="row">
      <div class="col-xs-12">
        <vx-my-account-address :i18n="messages.manageProfileShoppingLists"></vx-my-account-address>
      </div>
    </div>
  </div>
</div>
</template>
<script>
import messages from '../locale/messages';
import vxMyAccountAddress from '../components/manage-profile-shopping-lists/vx-my-account-address/vx-my-account-address.vue';

export default {
  name: 'address-page',
  components: {
    vxMyAccountAddress,
  },
  data() {
    return {
      messages: messages['en-US'],
    };
  },
};
</script>
