import Vue from 'vue';

import './button-counter.less';

Vue.component('button-counter', {
  props: ['i18n'],
  data() {
    return {
      count: 0,
    };
  },
  template:
    '<div class="button-holder"><button v-on:click="count++">{{i18n.heading}} {{ count }} times.</button></div>',
});
