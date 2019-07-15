
<template>
<div class="juicer-page">
  <h1>{{ msg }}</h1>
  <vx-juicer-feed> </vx-juicer-feed>
</div>
</template>

<script>
import JuicerFeed from '../components/common/vx-juicer-feed/vx-juicer-feed.vue'; // temparory

export default {
  name: 'SocialPage',
  components: {
    'vx-juicer-feed': JuicerFeed,
  },
  data() {
    return {
      msg: 'Jucier Feed',
    };
  },
};

</script>
