<template>
<div class="list-details-page">
    <div class="container-fluid">
        <div class="row">
            <div class="col-xs-12">
                <vx-list-details></vx-list-details>
            </div>
        </div>
    </div>
</div>
</template>
<script>
import vxListDetails from '../components/manage-profile-shopping-lists/vx-list-details/vx-list-details.vue';
import messages from '../locale/messages';

export default {
  name: 'list-details-page',
  components: {
    vxListDetails,
  },
  created() {},
  data() {
    return {
      msg: 'List Details Page',
      messages: messages['en-US'],
    };
  },
};
</script>
