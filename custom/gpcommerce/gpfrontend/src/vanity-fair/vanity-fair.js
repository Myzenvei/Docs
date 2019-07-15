// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import 'promise-polyfill/src/polyfill';
import Vue from 'vue';
import VeeValidate from 'vee-validate';
import VueTheMask from 'vue-the-mask';
import router from './../router';
import globals from './../components/common/globals';
import messages from './../locale/vanity-fair/messages';
import commonModule from './../modules/common';
import searchBrowseModule from './../modules/search-browse';
import viewSiteContentModule from './../modules/view-site-content';
import pdpModule from './../modules/pdp';
import accessCustomerServiceModule from './../modules/access-customer-service';
import manageProfileShoppingListsModule from './../modules/manage-profile-shopping-lists';
import nfrModule from './../modules/nfr';
import headerHeightAdjustment from '../components/common/mixins/header-height-adjustment-mixin';

import '../components/common/directives/directives';

import { eventBus } from '../modules/event-bus';

import siteConfig from '../config/vanity-fair-config';

const config = {
  delay: 100,
};

Vue.use(VeeValidate, config);
Vue.use(VueTheMask);

Vue.config.productionTip = false;
Vue.config.keyCodes = {
  // Enter or space Capturing
  'enter-space': [13, 32],
};

/* eslint-disable no-new */
const vm = new Vue({
  el: '#vanity-fair',
  data: {
    messages: messages['en-US'],
    globals,
    cartExists: false,
    siteConfig,
    screenWidth: screen.width,
    desktopWidthMin: 1200,
  },
  mixins: [
    commonModule,
    searchBrowseModule,
    viewSiteContentModule,
    pdpModule,
    accessCustomerServiceModule,
    manageProfileShoppingListsModule,
    nfrModule,
    headerHeightAdjustment,
  ],
  router,
  components: {},
  created() {
    window.cartExists = this.cartExists;
  },
  mounted() {
    // On document fully loaded changing inner wrapper padding based on footer height dynamically.
    this.$root.$on('footer-height', (value) => {
      if (value) {
        if (this.screenWidth >= this.desktopWidthMin) {
          if (document.querySelector('.main__inner-wrapper')) {
            document.getElementsByClassName(
              'main__inner-wrapper',
            )[0].style.paddingBottom = `${value}px`;
          }
        }
      }
    });
  },
  methods: {},
  updated() {
    this.setInnerWrapperPadding();
  },
  // template: '<GpEmployeeStore/>',
});

window.vm = vm;
