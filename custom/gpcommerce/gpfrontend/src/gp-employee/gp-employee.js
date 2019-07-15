// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import 'promise-polyfill/src/polyfill';
import Vue from 'vue';
import VeeValidate from 'vee-validate';
import VueTheMask from 'vue-the-mask';
import GpEmployeeStore from './gp-employee';
import router from './../router';
import globals from './../components/common/globals';

import store from './../components/common/store/store';
import messages from './../locale/gp-employee/messages';

import registerLoginModule from './../modules/registration-login';
import commonModule from './modules/common';
import manageShoppingCartModule from './../modules/manage-shopping-cart';
import searchBrowseModule from './../modules/search-browse';
import viewSiteContentModule from './../modules/view-site-content';
import pdpModule from './../modules/pdp';
import accessCustomerServiceModule from './../modules/access-customer-service';
import manageProfileShoppingListsModule from './../modules/manage-profile-shopping-lists';
import checkoutModule from './../modules/checkout';
import manageB2bOrgModule from './../modules/manage-b2b-org';
import quickOrderModule from './../modules/quick-order';
import nfrModule from './../modules/nfr';
import manageTransactionHistoryModule from './../modules/manage-transaction-history';

import '../components/common/directives/directives';
import siteConfig from '../config/gpemployee-config';
import ymOverrides from '../components/common/mixins/y-marketing-overrides';
import headerHeightAdjustment from '../components/common/mixins/header-height-adjustment-mixin';

import {
  eventBus,
  cartEventBus,
  globalEventBus,
  checkoutEventBus
} from '../modules/event-bus';

const config = {
  delay: 100,
};

/* global $ */

Vue.use(VeeValidate, config);
Vue.use(VueTheMask);

Vue.config.productionTip = false;
Vue.config.keyCodes = {
  // Enter or space Capturing
  "enter-space": [13, 32],
};

/* eslint-disable no-new */
var vm = new Vue({
  el: '#gp-employee',
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
    manageB2bOrgModule,
    quickOrderModule,
    nfrModule,
    manageTransactionHistoryModule,
    headerHeightAdjustment,
  ],
  router,
  store,
  components: {},
  mounted() {
    /**
     * scrolls to the section on click of the product
     */
    globalEventBus.$on('scroll-to', (val) => {
      $('html, body').animate({
        scrollTop: $(val).offset().top - document.querySelector('.header-section').offsetHeight
      }, 1000);
    });
  },
  methods: {
    addProductToCart(product) {
      eventBus.$emit("ymktAddToCart", product);
    },
    addProductToFavorites(product){
      eventBus.$emit("ymktToggleFavorite", product);
    },
    notifyProduct(product){
      eventBus.$emit("ymktNotifyButton", product);
    },
    yFormRender() {
      $.getScript(
        `${globals.commonResourcePath}${globals.thirdPartyApps.yforms.endPoint}`,
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
  updated() {
    this.setInnerWrapperPadding();
  },
  // template: '<GpEmployeeStore/>',
});

window.vm = vm;
