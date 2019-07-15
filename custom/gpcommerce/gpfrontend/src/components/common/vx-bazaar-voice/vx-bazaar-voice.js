export default {
  name: 'vx-bazaar-voice',
  components: {},
  props: {
    bvClient: {
      type: String,
      required: true,
    },
    gpBvId: {
      type: String,
      required: true,
    },
    bvEnv: {
      type: String,
      required: true,
    },
    bvLocale: {
      type: String,
      required: true,
    },
    productId: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      bvresult: {},
    };
  },
  computed: {},
  mounted() {
    const self = this;
    // bazaar voice inbuilt script integrated.
    (function() {
      const bvapiUrl = `https://display.ugc.bazaarvoice.com/bvstaging/static/${
        self.bvClient
      }/${self.gpBvId}/${self.bvLocale}/bvapi.js`;

      function getScript(url, callback) {
        const head =
          document.getElementsByTagName('head')[0] || document.documentElement;
        const script = document.createElement('script');
        script.src = url;
        script.type = 'text/javascript';
        script.charset = 'utf-8';
        script.setAttribute('async', 'async');
        script.onload = script.onreadystatechange = function() {
          if (
            !this.readyState ||
            this.readyState === 'loaded' ||
            this.readyState === 'complete'
          ) {
            script.onload = script.onreadystatechange = null;
            callback();
          }
        };
        head.insertBefore(script, head.firstChild);
      }

      // work around Firefox 3.0, 3.5 lack of document.readyState
      // property.
      // Note: Because of this workaround, the <script> fragment must
      // be included within the <head> or <div> element so that it
      // executes before the window load event is fired.

      let docReady = false;
      const onDocReady = function() {
        docReady = true;
      };
      if (document.readyState === undefined && document.addEventListener) {
        document.addEventListener('DOMContentLoaded', onDocReady, false);
        window.addEventListener('load', onDocReady, false);
      }

      window.loadBazaarvoiceApi = function(callback) {
        if (window.$BV) {
          callback();
        } else {
          getScript(bvapiUrl, () => {
            if (docReady) {
              $BV.docReady();
            }
            setTimeout(() => {
              callback();
            }, 3000);
          });
        }
      };
    })();
    loadBazaarvoiceApi(() => {
      let productID;
      if (self.productId.includes('/')) {
        productID = self.productId.replace('/', '_');
      } else {
        productID = self.productId;
      }
      $BV.configure('global', {
        productId: productID,
      });
      $BV.ui('rr', 'inline_ratings', {
        productIds: {
          productID: {
            url: 'http://path_to_productX',
          },
        },
        containerPrefix: 'BVRRInlineRating',
      });

      $BV.ui('rr', 'show_reviews', {
        doShowContent() {
          // If the container is hidden (such as behind a tab), put code here to make it visible
          // (open the tab).
        },
      });
      $BV.ui('qa', 'show_questions', {
        doShowContent() {
          // If the container is hidden (such as behind a tab), put code here to make it visible
          // (open the tab).
        },
      });
    });
  },
  created() {},
  methods: {},
};
