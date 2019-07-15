/* Extole Referal integration component */
import globals from '../globals';
import siteGlobals from '../../../../components/common/globals';

export default {
  name: 'vx-extole',
  components: {},
  props: {

  },
  data() {
    return {
      globals,
    };
  },
  created() {
    // Loading Extole Library Js
    // <script src="https://copperandcrane.extole.io/core.js" async></script>
    const extoleJS = document.createElement('script');
    extoleJS.setAttribute('src', this.globals.ExtoleJSURL);
    document.head.appendChild(extoleJS);
  },
  computed: {

  },
  mounted() {
    this.validateUserData();
  },
  methods: {
    /**
     * validating User data
     */
    validateUserData() {
      this.userData = {
        email: '',
        partnerUserId: '',
        firstName: '',
        lastName: '',
      };
      const userDetails = siteGlobals.userInfo;
      if (userDetails) {
        this.userData.email = userDetails.email || '';
        this.userData.partnerUserId = userDetails.customerId || '';
        this.userData.firstName = userDetails.firstName || '';
        this.userData.lastName = userDetails.lastName || '';
      }
    },
    /**
     * Adds Extole Script tag
     * @param {[type]} content content object to be sent
     */
    addExtoleScriptTag(content) {
      if (!content) {
        return;
      }
      const jsSnippet = document.createElement('script');
      jsSnippet.type = 'text/javascript';
      jsSnippet.text =
      `${'(function(c,b,f,k,a){c[b]=c[b]||{};for(c[b].q=c[b].q||[];a<k.length;)f(k[a++],c[b])})(window,"extole",' +
        'function (c,b){b[c]=b[c]||function (){b.q.push([c,arguments])}},["createZone"],0);' +
        'extole.createZone('}${JSON.stringify(content)});`;
      document.head.appendChild(jsSnippet);
    },
    /**
     * To initiate Extole integration on the footer
     */
    initiateExtoleOnHeader() {
      // Zone creation for follwoing tag
      // <span id='extole_zone_global_header'></span>
      const extoleData = {
        name: 'global_header',
        element_id: 'extole_zone_global_header',
        data: {
          email: this.userData.email,
          partner_user_id: this.userData.partnerUserId,
        },
      };
      this.addExtoleScriptTag(extoleData);
    },

    /**
     * To initiate Extole integration on the footer
     */
    initiateExtoleOnFooter() {
      // Zone creation for follwoing tag
      // <span id='extole_zone_global_footer'></span>
      const extoleData = {
        name: 'global_footer',
        element_id: 'extole_zone_global_footer',
        data: {
          email: this.userData.email,
          partner_user_id: this.userData.partnerUserId,
        },
      };
      this.addExtoleScriptTag(extoleData);
    },

    /**
     * To initiate Extole integration on the PDP page
     */
    initiateExtoleOnPDP() {
      // Zone creation for follwoing tag
      // <span id='extole_zone_product_page'></span>
      const extoleData = {
        name: 'product_page',
        element_id: 'extole_zone_product_page',
        data: {
          labels: 'drop-a-hint',
        },
      };
      this.addExtoleScriptTag(extoleData);
    },

    /**
     * To initiate Extole integration on the Order confirmation
     * Triggers 2 Scripts tags
     * @param  {Object} oderDetails Order Data
     */
    initiateExtoleOnOrderConfirmation(oderDetails) {
      const orderData = {};
      if (oderDetails) {
        orderData.orderId = oderDetails.code || '';
        orderData.cartValue = (oderDetails.totalPrice.value).toString() || '';
        orderData.couponCode = this.globals.getCouponCode(oderDetails) || '';
        // Zone creation for follwoing tag
        // <span id='extole_zone_confirmation'></span>
        const extoleDataConfirmation = {
          name: 'confirmation',
          element_id: 'extole_zone_confirmation',
          data: {
            email: this.userData.email,
            partner_user_id: this.userData.partnerUserId,
          },
        };
        const extoleDataConversion = {
          name: 'conversion',
          data: {
            email: this.userData.email,
            first_name: this.userData.firstName,
            last_name: this.userData.lastName,
            partner_user_id: this.userData.partnerUserId,
            coupon_code: orderData.couponCode,
            partner_conversion_id: orderData.orderId,
            cart_value: orderData.cartValue,
          },
        };

        this.addExtoleScriptTag(extoleDataConfirmation);
        this.addExtoleScriptTag(extoleDataConversion);
      }
    },
  },
};
