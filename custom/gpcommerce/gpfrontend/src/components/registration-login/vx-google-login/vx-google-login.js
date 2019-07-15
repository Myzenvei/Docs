/* Google sign in authentication */

/* Lint pacification */
/* global gapi */
/* eslint no-unused-vars: ['error', {'args': 'none'}] */
/* eslint no-console: ['error', { allow: ['log'] }] */

import googleData from './vx-google-login-i18n';

/**
 * Component for login through google account
 */

export default {
  name: 'vx-google-login',
  components: {},
  props: {
    /**
     * google client id
     * @type {String}
     */
    googleClientId: {
      type: String,
    },
  },
  data() {
    return {
      i18n: googleData,
    };
  },
  computed: {},
  mounted() {},
  created() {
    (function() {
      // Google SDK Loading Parameters
      const googleScriptTag = document.createElement('script');
      googleScriptTag.type = 'text/javascript';
      googleScriptTag.async = true;
      googleScriptTag.src = 'https://apis.google.com/js/platform.js';
      const pos = document.getElementsByTagName('script')[0];
      pos.parentNode.insertBefore(googleScriptTag, pos);
    })();
  },
  methods: {

    /**
     * Google login button click method.
     * We will check login status if it is logged in we will directly login the user
     * else based on response we will prompt login modal or error messages
     */
    googleLogin() {
      // let googleAuth;
      const self = this;

      /* Loading google api authentication
       * @type {[type]}
       */
      gapi.load('auth2', () => {
        // Google Auth intialization
        gapi.auth2.init({
          client_id: self.googleClientId,
          fetch_basic_profile: true,
          scope: 'profile email',
          cookiepolicy: 'single_host_origin',
        }).then((googleAuth) => {
          // console.log("Google api loaded");

          /* Initiate signIn event
           * 'consent' will make sure to choose an account or enter new account
           * details while signing in
           */
          Promise.resolve(googleAuth.signIn({
            prompt: 'consent',
          })).then((user) => {
            // console.log(googleAuth.currentUser.get().getId());
            // console.log(googleAuth.currentUser.get().getBasicProfile());
          }).catch((error) => {
            if (error && error.error === 'popup_blocked_by_browser') {
            } else {
            }
          });

          // Granting offline access
          googleAuth.grantOfflineAccess()
            .then(self.signInCallback)
            .catch((error) => {
              self.$emit('error', error.error);
            });
        }, (e) => {
        });
      });
    },
    signInCallback(authResult) {
      let token = '';
      const self = this;
      window.gapi.auth2.getAuthInstance().isSignedIn.listen(function(signedIn) {
        token = gapi.auth2.getAuthInstance().currentUser.get().getAuthResponse().access_token;
          if (token) {
            self.$emit('response', token);
          }
        });
        window.gapi.auth2.getAuthInstance().currentUser.listen((user) => {
          token = gapi.auth2
            .getAuthInstance()
            .currentUser.get()
            .getAuthResponse().access_token;
          if (token) {
            self.$emit('response', token);
          }
        });
    },
  },

};
