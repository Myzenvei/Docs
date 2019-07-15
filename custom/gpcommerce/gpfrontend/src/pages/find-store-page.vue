<template>
<div class="find-store">
 <vx-find-distributor :i18n="messages.viewSiteContent" :timeout-val="15000"></vx-find-distributor>
</div>
</template>

<script>
import vxFindDistributor from '../components/view-site-content/find-distributor/vx-find-distributor/vx-find-distributor.vue';
import messages from '../locale/messages';

export default {
  name: 'FindStore',
  components: {
    vxFindDistributor,
  },
  data() {
    return {
      msg: 'Find a Store',
      messages: messages['en-US'],
      storesList: [],
      storeLocation: '',
      selectedStore: {},
    };
  },
};
</script>
