import globals from '../globals';
import CommonService from '../../common/services/common-service';

export default {
  name: 'vx-application-auth',
  props: [
    'siteId',
    'siteType',
    'currentPage',
    'locale',
    'commonResourcePath',
    'csrfToken',
    'contextPath',
    'googleMapsKey',
    'isMock',
    'isLocal',
    'navigations',
    'thirdPartyApps',
    'showPaypal',
    'paypalEnv',
    'applicationAuthParams',
    'siteConfig',
  ],
  data() {
    return {
      globals,
      params: {},
      commonService: new CommonService(),
    };
  },
  methods: {
    handleAuthResponse(response) {
      // Set Application AuthToken Cookie if cookie doesnot exist
      this.globals.setAuthToken(response.data.access_token);
      // this.globals.setIsloggedIn(false);
    },
    handleAuthError() {},
    loggedInHandle() {
      if (this.globals.getIsLoggedIn()) {
        const pathname = window.location.pathname;
        if (pathname.includes('login') || pathname.includes('register')) {
          this.globals.navigateToUrl('home');
        }
      }
    },
  },
  created() {
    // Set siteId and siteType
    this.globals.siteId = this.siteId;
    this.globals.siteType = this.siteType;
    this.globals.currentPage = this.currentPage;
    this.globals.locale = this.locale;
    this.globals.commonResourcePath = this.commonResourcePath;
    this.globals.contextPath = this.contextPath;
    this.globals.csrfToken = this.csrfToken;
    this.globals.assetsPath = this.isLocal
      ? this.globals.assetsSubPath
      : `${this.commonResourcePath}/dist/${this.globals.assetsSubPath}`;
    this.globals.googleMapsKey = this.googleMapsKey;
    this.globals.isMock = this.isMock;
    this.globals.navigations = this.navigations;
    this.globals.thirdPartyApps = this.thirdPartyApps;
    this.globals.showPaypal = this.showPaypal;
    this.globals.paypalEnv = this.paypalEnv;
    this.globals.applicationAuthParams = this.applicationAuthParams;
    this.globals.siteConfig = this.siteConfig;
    // Page logged in
    // this.loggedInHandle();    //This is not required now
    // call only if authtoken is not present
    if (!this.globals.getIsLoggedIn() && this.globals.uid!=='asm_anonymous') {
      const requestConfig = {};
      this.commonService.applicationAuthService(
        requestConfig,
        this.handleAuthResponse,
        this.handleAuthError,
      );
    }
    if(this.globals.uid==='asm_anonymous'){
      this.globals.hasGlobalAuth=true;
    }
  },
};
