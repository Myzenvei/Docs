// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import 'promise-polyfill/src/polyfill';
import Vue from 'vue';
import VeeValidate from 'vee-validate';
import VueTheMask from 'vue-the-mask';
import globals from './../components/common/globals';

import messages from './../locale/brawny/messages';

import registerLoginModule from './../modules/registration-login';
import commonModule from './../modules/common';
import manageShoppingCartModule from './../modules/manage-shopping-cart';
import searchBrowseModule from './../modules/search-browse';
import viewSiteContentModule from './../modules/view-site-content';
import pdpModule from './../modules/pdp';
import accessCustomerServiceModule from './../modules/access-customer-service';
import manageProfileShoppingListsModule from './../modules/manage-profile-shopping-lists';
import checkoutModule from './../modules/checkout';

// This import is only for b2b hence commenting it as brawny is b2c website
// import manageB2bOrgModule from './../modules/manage-b2b-org';
import quickOrderModule from './../modules/quick-order';
import nfrModule from './../modules/nfr';
import manageTransactionHistoryModule from './../modules/manage-transaction-history';

import '../components/common/directives/directives';

import { eventBus } from '../modules/event-bus';
import siteConfig from '../config/brawny-config';
import ymOverrides from '../components/common/mixins/y-marketing-overrides';
import debounce from 'lodash/debounce';

const config = {
  delay: 100,
};

/* global $ */

Vue.use(VeeValidate, config);
Vue.use(VueTheMask);

Vue.config.productionTip = false;
Vue.config.keyCodes = {
  // Enter or space Capturing
  'enter-space': [13, 32],
};

const vm = new Vue({
  el: '#brawny',
  data: {
    messages: messages['en-US'],
    globals,
    siteConfig,
    cartExists: false,
  },
  mixins: [
    commonModule,
    registerLoginModule,
    manageShoppingCartModule,
    searchBrowseModule,
    viewSiteContentModule,
    pdpModule,
    accessCustomerServiceModule,
    manageProfileShoppingListsModule,
    checkoutModule,
    quickOrderModule,
    nfrModule,
    manageTransactionHistoryModule,
  ],
  components: {},
  mounted() {},
  methods: {
    addProductToCart(product) {
      eventBus.$emit('ymktAddToCart', product);
    },
    addProductToFavorites(product) {
      eventBus.$emit('ymktToggleFavorite', product);
    },
    notifyProduct(product) {
      eventBus.$emit('ymktNotifyButton', product);
    },
    yFormRender() {
      $.getScript(
        `${globals.commonResourcePath}${
          globals.thirdPartyApps.yforms.endPoint
        }`,
        () => {
          $('.vx-ymarket-form').show(); // y-form should be visible when cart exists
          ymOverrides.maskTelephone();
          ymOverrides.setHiddenFieldValues();
        },
      );
    },
  },
  watch: {
    cartExists(newVal) {
      window.cartExists = this.cartExists;
      if (newVal) {
        this.yFormRender();
      }
    },
  },
});

window.vm = vm;
