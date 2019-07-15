<template>
  <div class="nfr-page">
    <h1>{{ msg }}</h1>

    <div class="container-fluid">
      <div class="row">
        <div class="col-xs-12">
          <vx-404-page :i18n="messages.nfr.i18n404"></vx-404-page>
        </div>
      </div>
      <br>
      <div class="row">
        <div class="col-xs-12">
          <vx-500-page :i18n="messages.nfr.i18n500"></vx-500-page>
        </div>
      </div>
      <br>
    </div>
  </div>
</template>
<script>
import vx404Page from '../components/nfr/vx-404-page/vx-404-page.vue';
import vx500Page from '../components/nfr/vx-500-page/vx-500-page.vue';
import messages from '../locale/messages';

export default {
  name: 'nfr-page',
  components: {
    vx404Page,
    vx500Page,
  },
  created() {

  },
  data() {
    return {
      msg: 'Nfr Page',
      messages: messages['en-US'],
    };
  },
};
</script>
