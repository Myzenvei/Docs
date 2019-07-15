import {
  Validator
} from 'vee-validate';
import globals from '../../../../components/common/globals';
import ViewSiteContentService from '../../../../components/common/services/view-site-content-service';
import mgSignUp from '../../mg-sign-up/mg-sign-up.vue';

export default {
  name: 'mg-hero-banner-v-1',
  components: {mgSignUp},
  props: {
    bannerData: {
      type: Object,
      default: () => ({
        headingText: 'Sign Up',
        subHeadingText: 'COULD WE BE ANY CLOSER? YES! OH YES.',
        informationText: '<b>now that we’re besties, we want to keep in touch all year round via email.</b> as a subscriber, you wll get all the beg.)',
        imgSrcD: './static/assets/images/mardi-gras/find_us.png'
      }),
    },
    bannerColors: {
      type: Object,
      default: () => ({
        bannerTitleColor: '#522773',
        bannerHeadingColor: '#96C93D',
        bannerInfoColor: '#522773',
      }),
    },
    isShowInput: {
      type: Boolean,
      default: false,
    },
    btnText: {
      type: String,
      default: 'Go Home',
    },
    i18n: {
      type: Object,
    },
    className: {
      type: String,
    },
    isHideAge: {
      type: Boolean,
      default: false,
    },
    pageUid: {
      type: String,
      default: '',
    },
  },
  data() {
    return {
      globals,
      isDownloadKit: false,
      bannerImg: this.bannerData.imgSrcD,
      emailID: '',
      margediGrasService: new ViewSiteContentService(),
      showSignUpForm: false,
      showCouponsIFrame: false,
    }
  },
  computed: {

  },
  mounted() {
    // this.background = this.bannerColors.background ? globals.assetsPath + this.bannerColors.background : '';
    this.isDownloadKit = this.bannerColors.isDownloadKit ? this.bannerColors.isDownloadKit : false;
  },
  methods: {
    getCouponPermissions() {
      const requestConfig = {};
      requestConfig.data = {
        email: this.emailID,
      };
      this.margediGrasService.getCouponPermissions(
        requestConfig,
        this.handleCouponPermissionsResponse,
        this.handleCouponPermissionsError,
      );
    },

    handleCouponPermissionsResponse(response) {
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
      this.couponUrl = `${this.globals.thirdPartyApps.qples.endPoint}?${this.contactKey}${this.i18n.qplesSuffix}`;
    },

    handleCouponPermissionsError(error) {}
  },
}
