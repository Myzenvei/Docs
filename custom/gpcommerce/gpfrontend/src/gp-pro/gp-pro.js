// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import 'promise-polyfill/src/polyfill';
import Vue from 'vue';
import VeeValidate from 'vee-validate';
import VueTheMask from 'vue-the-mask';
// import GPPro from './gp-pro';
import router from './../router';
import globals from './../components/common/globals';

import store from './../components/common/store/store';
import messages from './../locale/gp-pro/messages';
import registerLoginModule from './../modules/registration-login';
import commonModule from './../modules/common';
import searchBrowseModule from './../modules/search-browse';
import viewSiteContentModule from './../modules/view-site-content';
import accessCustomerServiceModule from './../modules/access-customer-service';
import nfrModule from './../modules/nfr';
import manageShoppingCartModule from './../modules/manage-shopping-cart';
import pdpModule from './../modules/pdp';
import manageProfileShoppingListsModule from './../modules/manage-profile-shopping-lists';
import checkoutModule from './../modules/checkout';
import manageB2bOrgModule from './../modules/manage-b2b-org';
import quickOrderModule from './../modules/quick-order';
import manageTransactionHistoryModule from './../modules/manage-transaction-history';
import manageSubscriptionModule from './../modules/manage-subscription';
import '../components/common/directives/directives';
import siteConfig from '../config/gppro-config';

import { globalEventBus } from '../modules/event-bus';
import headerHeightAdjustment from '../components/common/mixins/header-height-adjustment-mixin';

/* global $ */
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
new Vue({
  el: '#gp-pro',
  data: {
    messages: messages['en-US'],
    globals,
    cartExists: false,
    siteConfig,
  },
  mixins: [
    commonModule,
    viewSiteContentModule,
    accessCustomerServiceModule,
    nfrModule,
    searchBrowseModule,
    headerHeightAdjustment,
    registerLoginModule,
    manageShoppingCartModule,
    pdpModule,
    manageProfileShoppingListsModule,
    checkoutModule,
    manageB2bOrgModule,
    quickOrderModule,
    manageTransactionHistoryModule,
    manageSubscriptionModule,
  ],
  router,
  store,
  components: {},
  created() {
    window.cartExists = this.cartExists;
  },
  mounted() {
    globalEventBus.$on('scroll-to', (val) => {
      $('html, body').animate({
        scrollTop: $(val).offset().top - document.querySelector('.header-section').offsetHeight,
      }, 1000);
    });
  },
  methods: {},
  updated() {
    this.setInnerWrapperPadding();
  },
  // template: '<GPPro/>',
});
