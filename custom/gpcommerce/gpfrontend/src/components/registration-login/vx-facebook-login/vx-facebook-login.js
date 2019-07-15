import facebookData from './vx-facebook-login-i18n';

/**
 * Component for login through facebook account
 */

export default {
  name: 'vx-facebook-login',
  props: ['appId'],
  data() {
    return {
      i18n: facebookData,
      isLogin: false,
    };
  },
  computed: {},
  mounted() {},
  /* eslint-disable */
  created() {
    // FB SDK Loading Parameters
    const self = this.appId;
    window.fbAsyncInit = function() {
      FB.init({
        appId: self,
        autoLogAppEvents: true,
        status: true,
        cookie: true,
        xfbml: true,
        oauth: true,
        version: 'v3.0',
      });
      FB.AppEvents.logPageView();
    };
    (function(d, s, id) {
      var js,
        fjs = d.getElementsByTagName(s)[0];
      if (d.getElementById(id)) {
        return;
      }
      js = d.createElement(s);
      js.id = id;
      (js.src = 'https://connect.facebook.net/en_US/sdk.js'),
        fjs.parentNode.insertBefore(js, fjs);
    })(document, 'script', 'facebook-jssdk');
  },

  methods: {
    // Facebook login button click method.
    // We will check login status if it is logged in we will directly login the user
    // Else based on response we will prompt login modal or error messages
    facebookLogin() {
      this.isLogin = true;
      const self = this;
      FB.getLoginStatus(function(response) {
        self.statusChangeCallback(response);
      });
    },
    // Response method from facebook
    statusChangeCallback(response) {
      if (response.status === 'connected') {
        this.$emit('response', response.authResponse);
      } else if (response.status === 'not_authorized') {
        FB.login(this.statusChangeCallback, {
          scope: 'email',
          return_scopes: true,
        });
      } else if (response.status === 'unknown' && this.isLogin) {
        FB.login(this.statusChangeCallback, {
          scope: 'email',
          return_scopes: true,
        });
        this.isLogin = false;
      } else {
        // Login method to show facebook login prompt to enter credentials
      }
    },
  },
  /* eslint-enable */
};
