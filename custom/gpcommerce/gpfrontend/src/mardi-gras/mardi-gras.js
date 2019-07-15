// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import 'promise-polyfill/src/polyfill';
import Vue from 'vue';
import VeeValidate from 'vee-validate';
import VuePaginate from 'vue-paginate';
import router from './router';
import globals from '././../components/common/globals';
import messages from './locale/messages';
import commonModule from './modules/common';
import nfrModule from './../modules/nfr';

export const eventBus = new Vue();
export const cartEventBus = new Vue();
export const checkoutEventBus = new Vue();

Vue.use(VeeValidate);
Vue.use(VuePaginate);

Vue.config.productionTip = false;
Vue.config.keyCodes = {
  // Enter or space Capturing
  'enter-space': [13, 32],
};


/* eslint-disable no-new */
new Vue({
  el: '#mardi-gras',
  data: {
    messages: messages['en-US'],
    globals,
    navSocialLinks: {
      navLinks: [{
        linkText: 'Mardigras Social Icon',
        linkURL: '/',
        linkImg: 'localhost:9002/gpcommercestorefront/medias/facebook.png?context\u003dbWFzdGVyfGltYWdlc3w1Nzh8aW1hZ2UvcG5nfGltYWdlcy9oOGIvaDdjLzg3OTc5MjgyOTIzODIucG5nfDhmMDUxMzA2Y2M1YmQwZDdiZTMzY2FiOTM2NjNlZGY3Yzk4MmQxNGIyNDJlOTNlZDczMjExODRiZWZiMTEzNDg',
        isSocialLink: false,
        isSocialIcon: true,
      }, {
        linkText: 'Link',
        linkURL: '/',
        isSocialLink: true,
        isSocialIcon: false,
      }],
    },
    navData: {
      headerOptions: {
        includeMiniCart: true,
        includeSearchBox: true,
        brandLogo: 'images/logo.svg',
        isCheckout: false,
      },
      loginMenu: {
        title: 'Login Nav Node',
        menuOptions: [{
          option: 'Home',
          optionLink: '/',
          external: false,
        }, {
          option: 'Our Napkins',
          optionLink: '/ournapkins',
          external: false,
        }, {
          option: 'Find \u0026 Buy US',
          optionLink: '/findandbuy',
          external: false,
        }, {
          option: 'FAQ',
          optionLink: '/faqs',
          external: false,
        }, {
          option: 'Teachers',
          optionLink: '/',
          external: false,
        }],
      },
    },
    videoOptions: '?modestbranding=1&rel=0&controls=1&showinfo=1&html5=1&autoplay=1',
    footerData: {
      columns: [{
        links: [{
          linkText: 'GP Sustainability Site',
          linkTo: 'https://www.Mardigras.com/',
        }, {
          linkText: 'Terms of Use',
          linkTo: 'https://www.Mardigras.com/',
        }, {
          linkText: 'Privacy Notice',
          linkTo: 'https://www.Mardigras.com/',
        }, {
          linkText: 'California Privacy Rights',
          linkTo: 'https://www.Mardigras.com/',
        }, {
          linkText: 'GP Professional',
          linkTo: 'https://www.Mardigras.com/',
        }],
      }],
      footerLogo: '/gpcommercestorefront/medias/gpLogo.png?context\u003dbWFzdGVyfHJvb3R8MTI4MXxpbWFnZS9wbmd8aDg4L2g2OC84Nzk3OTE0NDMxNTE4LnBuZ3wwYWUwZWExZTUwYWE3MTUwMmNlMzU2NTRkNGI2MWUwNTBkMDExMTIzNzIzMzI4MjMxMDRlM2ExN2YxNWViODdj',
      footerLogoUrl: 'https://www.Mardigras.com/',
      footerLogoAltText: 'gpLogo.png',
    },
  },
  mixins: [
    commonModule,
    nfrModule,
  ],
  router,
  components: {},
  created() {
    window.cartExists = this.cartExists;
  },
  mounted() {},
  methods: {},
});
