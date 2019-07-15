import {
  Validator
} from 'vee-validate';
import globals from '../../../components/common/globals';
import ViewSiteContentService from '../../../components/common/services/view-site-content-service';
import vxQplesSignUp from '../vx-qples-sign-up/vx-qples-sign-up.vue';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';

export default {
  name: 'vx-qples-form',
  components: {
    vxQplesSignUp,
    vxSpinner,
  },
  props: {
    i18n: {
      type: Object,
    },
  },
  data() {
    return {
      globals,
      emailID: '',
      vanityFairService: new ViewSiteContentService(),
      showSignUpForm: false,
      showCouponsIFrame: false,
      couponUrl: '',
      contactKey: '',
    };
  },
  computed: {},
  mounted() {
    const veeCustomErrorMessage = {
      en: {
        custom: {
          email: {
            required: this.i18n.emailRequiredError,
            email: this.i18n.emailFormatError,
          },
        },
      },
    };
    Validator.localize(veeCustomErrorMessage);
  },
  methods: {
    getCouponPermissions() {
      this.$validator.validateAll().then((result) => {
        if (result) {
          const requestConfig = {};
          requestConfig.data = {
            email: this.emailID,
          };
          this.$refs.spinner.showSpinner();
          this.vanityFairService.getCouponPermissions(
            requestConfig,
            this.handleCouponPermissionsResponse,
            this.handleCouponPermissionsError,
          );
        }
      });
    },

    handleCouponPermissionsResponse(response) {
      this.$refs.spinner.hideSpinner();
      const permisionsData = response.data;
      if (!permisionsData.permission) {
        this.showSignUpForm = true;
      } else {
        this.showSignUpForm = false;
        this.setContactKey(permisionsData);
      }
    },

    setContactKey(permisionsData) {
      this.showCouponsIFrame = true;
      this.contactKey = permisionsData.contactKey;
      this.couponUrl = `${this.globals.thirdPartyApps.qples.endPoint}?${
        this.contactKey
      }${this.i18n.qplesSuffix}`;
    },

    handleCouponPermissionsError(error) {
      this.$refs.spinner.hideSpinner();
    },
  },
};
