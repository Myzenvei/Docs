export default {
  name: 'vx-ymarket-form',
  components: {},
  props: {
    i18n: {
      type: Object
    }
  },
  data() {
    return {

    }
  },
  created() {
    // SAP libraries
    const sapYmarketingJS = document.createElement('script');
    const sapYmarketingCSS = document.createElement('link');

    sapYmarketingJS.setAttribute('src', '../../../static/assets/lib/yForms/sapContentPage.js');
    sapYmarketingCSS.setAttribute('href', '../../../static/assets/lib/yForms/sapContentPage.css');
    sapYmarketingCSS.setAttribute('media', 'all');
    sapYmarketingCSS.setAttribute('rel', 'stylesheet');
    sapYmarketingCSS.setAttribute('type', 'text/css');

    // document.head.appendChild(sapYmarketingCSS);
    // document.body.appendChild(sapYmarketingJS);
  },

  computed: {

  },
  mounted() {

  },
  methods: {

  }
}
