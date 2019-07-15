import globals from '../globals';
import kauthMixin from '../mixins/kauth-mixin';

const headerMixin = {
  mixins: [kauthMixin],
  data() {
    return {
      globals,
    };
  },
  methods: {
    createAccountButHandler() {
      if (this.globals.isEmployeeStore()) {
        this.setStorage(this.kochKeys.KOCHREDIRECT, 'register');
        this.globals.navigateToUrl('kauthUrl', true);
      } else if (this.globals.getIsSubscription()) {
        this.globals.setCookie('flow', 'checkout');
        this.globals.navigateToUrlWithParams('register');
      } else {
        this.globals.navigateToUrl('register');
      }
    },
    clearKauthData() {
      if (this.globals.getIsLoggedIn()) {
        this.deleteKauthData();
      }
    },
  },
};

export default headerMixin;
