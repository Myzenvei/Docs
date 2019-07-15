import endpointUrls from './endpoint';
import prototypes from './prototypes';
import {
  imageFormat,
  brandNames,
  logoutFlowCookie,
  cookies,
} from '../common/mixins/vx-enums';
import ymOverrides from '../common/mixins/y-marketing-overrides';

/* eslint-disable */
const AUTH_KEY = 'authToken';
const LOGIN_KEY = 'loggedIn';
const ACCESS_KEY = 'access_token';

const GUID = 'guid';

// const BASIC_URL = 'https://localhost:9002';
// For production build uncomment the below line and comment the above
const BASIC_URL = window.location.origin;

const MOCK_BASE_URL = 'http://localhost:3000';

const BASE_URL = BASIC_URL + '/gpcommercewebservices/v2';

const B2B = 'B2B';
const B2C = 'B2C';

const NAV_BASE_B2C_URL = '';
const NAV_BASE_B2B_URL = '/gp';
const CURRENCY = 'USD';

const ANONYMOUS_UID_ROLE = 'anonymous'; //Role for anonymous user is anonymous
const CURRENT_UID_ROLE = 'current'; // Role for logged in user is current

const LOGGED_IN_USER_CARD_GUID = 'current'; //Guid for Current user is current

prototypes.init();

const globals = {
  headers: {
    contentType: 'application/json',
    Authorization: '',
  },
  siteId: '',
  siteType: '',
  currentPage: '',
  locale: '',
  currency: 'USD',
  commonResourcePath: '',
  contextPath: '',
  assetsPath: '',
  csrfToken: '',
  uid: '', //uid is anonymous for loggedout user and emailid for loggedin
  googleMapsKey: 'AIzaSyABABgClws6FM5LEutXRCq10S59GyJQsgc', //:todo - we need to update the value once moved to dev
  googlePayScriptURL: 'https://pay.google.com/gp/p/js/pay.js',
  userInfo: {
    firstName: '',
    lastName: '',
    email: '',
    contactNumber: '',
    unit: '', // unit or Company
    userRoles: [],
    b2bUnitLevel: '',
    b2bUserReviewStatus: '',
    customerId: '',
  },
  hasGlobalAuth: false,
  globalAuthToken: '', // When the user is not logged in.
  loggedIn: false,
  isMock: false,
  isSubscription: false,

  assetsSubPath: 'static/assets/',
  // Navigations Urls
  navigations: {},
  // front end config object
  siteConfig: {},
  // External Navigation Urls
  externalNavigations: {
    kauthUrl:
      'https://auth.kochid.com/as/authorization.oauth2?client_id=gpkochauthlocal&redirect_uri=https://apparel-uk.local:9002/gpcommercestorefront/&response_type=code&scope=openid',
    qplesUrl:
      'https://s3.amazonaws.com/qples-gallery/WwBjoNdL1B_simple_gallery.html',
  },
  // Service urls
  serviceUrls: endpointUrls,
  // Third party application parameters
  thirdPartyApps: {},
  //Show or Hide Paypal button in payment page.
  showPaypal: false,
  //Set the paypal Environment - sandbox or production
  paypalEnv: 'sandbox',
  // Content schema tag values for logo
  logoContentSchema: {
    itemType: 'http://schema.org/Organization',
    itemProp: 'logo',
  },
  // Application auth parameters
  applicationAuthParams: {},
  // Flag for catefories banner
  isCategoryImageAvailable: false,
  // Data for catefories banner
  catergoryBannerData: {},
  // AddToAny external script file
  addToAny: 'https://static.addtoany.com/menu/page.js',
  // Get the Rest Url
  getRestUrl(endPoint, type) {
    let url = '';
    if (endPoint === 'auth') {
      url = BASIC_URL + this.serviceUrls[endPoint];
    } else if (type === 'user') {
      url =
        BASE_URL +
        '/' +
        this.siteId +
        '/users/' +
        this.getUserRole() +
        this.serviceUrls[endPoint];
    } 
    else if(type === 'cart'){
      url =
        BASE_URL +
        '/' +
        this.siteId +
        '/users/' +
        this.getUserRole() +
        '/carts/' +
        this.getCartGuid() +
        this.serviceUrls[endPoint];
    } else if (type === 'userid') {
      url = BASE_URL + '/' + this.siteId + '/users/';
    } else if (type === 'subscribe') {
      url =
        BASE_URL +
        '/' +
        this.siteId +
        '/users/' +
        this.getUserRole() +
        '/carts/' +
        this.getSubscribtionCartId() +
        this.serviceUrls[endPoint];
    } else {
      url = BASE_URL + '/' + this.siteId + this.serviceUrls[endPoint];
    }
    if (!this.isMock && this.isMock !== 'true') {
      return url;
    }
    return this.getMockURL(url);
  },
  /**
   * Get mock url for services
   */
  getMockURL(url) {
    return url.replace(BASE_URL, MOCK_BASE_URL);
  },
  getCreditCardiFrameURL: function() {
    return (
      this.getNavBaseUrl() +
      '/_ui/responsive/common/templates/credit-card-form.html'
    );
  },
  getB2BBaseURL: function() {
    return (
      this.getNavBaseUrl() +
      '/' +
      this.siteId +
      '/' +
      this.locale +
      '/' +
      this.currency
    );
  },
  getCreditCardiFrameURL: function() {
    return (
      this.getNavBaseUrl() +
      '/_ui/responsive/common/templates/credit-card-form.html'
    );
  },
  getB2BBaseURL: function() {
    return (
      this.getNavBaseUrl() +
      '/' +
      this.siteId +
      '/' +
      this.locale +
      '/' +
      this.currency
    );
  },
  getB2CBaseURL: function() {
    return this.getNavBaseUrl() + '/' + this.locale; //This is same for EMP and B2C
  },
  navigateToUrlWithParams(endPath, params, queryParameter) {
    var url = this.getNavBaseUrl();
    url += this.navigations[endPath];
    if (params) {
      if (queryParameter) {
        url += '?' + queryParameter + '=' + params + '&site=' + this.siteId;
      } else {
        url += '?q=' + params + '&site=' + this.siteId;
      }
    } else {
      url += '?site=' + this.siteId;
    }
    window.location = url;
  },
  // Navigate to the url
  navigateToUrl(url, external) {
    window.location = this.getNavigationUrl(url, external);
  },
  // Get the navigation Url
  getNavigationUrl(endPath, external) {
    if (external) {
      return this.externalNavigations[endPath];
    } else if (this.isB2B()) {
      return this.getB2BBaseURL() + this.navigations[endPath];
    }
    return this.getB2CBaseURL() + this.navigations[endPath];
  },
  getNavBaseUrl() {
    if (this.siteType === B2B) {
      return NAV_BASE_B2B_URL;
    }
    return NAV_BASE_B2C_URL;
  },
  //Get the url param
  getUrlParam: function(paramKey, url) {
    var str = window.location.search;
    var objURL = {};
    str.replace(new RegExp('([^?=&]+)(=([^&]*))?', 'g'), function(
      $0,
      $1,
      $2,
      $3,
    ) {
      objURL[$1] = $3;
    });

    return objURL[paramKey];

    const href = url || window.location.href;
    const reg = new RegExp(`[?&]${paramKey}=([^&#]*)`, 'i');
    const string = reg.exec(href);
    return string ? decodeURI(string[1]) : null;
  },

  /* Preparing the url by removing all the extra slashes prepended with context path */
  getUrlWithContextPath(url) {
    if (!url) {
      return;
    }
    //check for "JumpTo" urls. No need to append anything in case of jump to URL.
    if (url.indexOf('#') === 0) {
      return url;
    }

    let finalUrl = '';
    let conPath = this.contextPath;
    conPath = conPath.replace(/^\/+/g, '');
    url = url.replace(/^\/+/g, '');
    if (conPath) {
      finalUrl = `/${conPath}/${url}`;
    } else {
      finalUrl = `/${url}`;
    }
    return finalUrl;
  },
  //get isSubscription flag
  getIsSubscription: function() {
    if (!this.getSubscribtionCartId()) {
      this.isSubscription = false;
    } else {
      this.isSubscription = true;
    }
    return this.isSubscription;
  },
  //set isSubscription flag
  setIsSubscription: function(isSubscription) {
    this.isSubscription = isSubscription;
  },
  //get site Id
  getSiteId: function() {
    return this.siteId;
  },
  //set the siteId
  setSiteId: function(siteId) {
    this.siteId = siteId;
  },
  //set the site name
  getSiteName: function() {
    if (this.siteId === brandNames.gppro) {
      return 'GP Pro';
    } else if (this.siteId === brandNames.dixie) {
      return 'Dixie';
    } else if (this.siteId === brandNames.gpemployee) {
      return 'Employee Store';
    } else if (this.siteId === brandNames.b2cwhitelabel) {
      return 'B2C White Label';
    } else if (this.siteId === brandNames.b2bwhitelabel) {
      return 'B2B White Label';
    } else if (this.siteId === brandNames.gpxpress) {
      return 'GP Xpress';
    } else if (this.siteId === brandNames.copperandcrane) {
      return 'Copper And Crane';
    } else if (this.siteId === brandNames.vanityfair) {
      return 'Vanity Fair';
    } else if (this.siteId === brandNames.brawny) {
      return 'Brawny';
    } else if (this.siteId === brandNames.innovia) {
      return 'Innovia';
    } else if (this.siteId === brandNames.sparkle) {
      return 'Sparkle';
    }
  },
  // get site Type
  getSiteType() {
    return this.siteType;
  },
  // Get Application Params
  getApplicationParams() {
    const obj = {};
    obj.client_id = this.applicationAuthParams.client_id;
    obj.client_secret = this.applicationAuthParams.client_secret;
    obj.grant_type = this.applicationAuthParams.grant_type;
    return obj;
  },
  // Is B2B Customer
  isB2B() {
    return this.siteType === B2B;
  },
  // Is B2C Customer
  isB2C() {
    return this.siteType === B2C;
  },
  // Is employee Store
  isEmployeeStore() {
    return this.siteId === brandNames.gpemployee;
  },
  isDixie() {
    return this.getSiteId() === brandNames.dixie;
  },
  isMardigras() {
    return this.getSiteId() === brandNames.mardigras;
  },
  isGppro() {
    return this.getSiteId() === brandNames.gppro;
  },
  isVanityfair() {
    return this.getSiteId() === brandNames.vanityfair;
  },
  isB2BWhiteLabel() {
    return this.getSiteId() === brandNames.b2bwhitelabel;
  },
  isB2CWhiteLabel() {
    return this.getSiteId() === brandNames.b2cwhitelabel;
  },
  isGpXpress() {
    return this.getSiteId() === brandNames.gpxpress;
  },
  isCopperCrane() {
    return this.getSiteId() === brandNames.copperandcrane;
  },
  isBrawny() {
    return this.getSiteId() === brandNames.brawny;
  },
  isInnovia() {
    return this.getSiteId() === brandNames.innovia;
  },
  // Get Login Params
  setHeaders() {},
  getHeaders() {
    if (
      this.getCookie(ACCESS_KEY) &&
      (this.getIsLoggedIn() || this.uid === 'asm_anonymous')
    ) {
      this.headers.Authorization = 'Bearer' + ' ' + this.getCookie(ACCESS_KEY);
    } else {
      this.headers.Authorization = 'Bearer' + ' ' + this.getAuthToken();
    }
    if (this.isGpXpress()) {
      (this.headers.soldTo = this.getCookie(cookies.soldTo)),
        (this.headers.userId = this.getCookie(cookies.userId)
          ? this.getCookie(cookies.userId).toLowerCase()
          : this.getCookie(cookies.userId));
    }
    return this.headers;
  },
  // setIsLogin and getIsLogin
  setIsloggedIn(value) {
    this.loggedIn = value;
  },
  // Returns true if the hybris store front session is logged in
  getIsLoggedIn() {
    if (this.getUserRole() === CURRENT_UID_ROLE) {
      this.loggedIn = true;
    } else {
      this.loggedIn = false;
    }
    return this.loggedIn;
  },
  // SetAuth Token and GetAuth Token
  setAuthToken(value) {
    // this.setStorage(AUTH_KEY, value);
    //Setting the AUTH Toke in Variable instead of Storage
    this.globalAuthToken = value;
    this.hasGlobalAuth = true;
  },
  getAuthToken() {
    // Instead of getting the value from storage, getting from globalAuthToken variable
    // return this.getStorage(AUTH_KEY);
    return this.globalAuthToken;
  },
  deleteAuthToken() {
    this.deleteStorage(AUTH_KEY);
  },
  // Set the storage
  setStorage(key, value) {
    localStorage.setItem(key, value);
  },
  // get the storage
  getStorage(key) {
    return localStorage[key];
  },
  // delete from storage
  deleteStorage(key) {
    localStorage.removeItem(key);
  },
  logout(logoutFlow) {
    // A backend function call for logout should happen to clear the Jession id and login cookie
    //  this.deleteCookie(ACCESS_KEY);
    this.clearAllCookies();
    if (logoutFlow) {
      this.setCookie(logoutFlowCookie.logoutFlow, logoutFlow);
    }
    // this.deleteStorage(LOGIN_KEY);
    this.setIsloggedIn(false);
    this.navigateToUrl('logout');
  },
  // Set Cookies and Get Cookies
  setCookie: function(name, value, days) {
    if (days) {
      var date = new Date();
      date.setTime(date.getTime() + days * 24 * 60 * 60 * 1000);
      var expires = '; expires=' + date.toUTCString();
    } else var expires = '';
    document.cookie = name + '=' + value + expires + '; path=/';
  },
  getCookie: function(key) {
    var v = document.cookie.match('(^|;) ?' + key + '=([^;]*)(;|$)');
    return v ? v[2] : null;
  },
  deleteCookie: function(key) {
    this.setCookie(key, '', -1);
  },
  clearAllCookies() {
    document.cookie.split(';').forEach(function(c) {
      document.cookie = c
        .replace(/^ +/, '')
        .replace(/=.*/, '=;expires=' + new Date().toUTCString() + ';path=/');
    });
  },
  // Get User Type
  getUserRole: function() {
    if (this.uid === ANONYMOUS_UID_ROLE || this.uid==='asm_anonymous') {
      return ANONYMOUS_UID_ROLE;
    }
    return CURRENT_UID_ROLE;
  },
  getCartGuid: function() {
    if (this.getIsLoggedIn()) {
      return LOGGED_IN_USER_CARD_GUID;
    }
    return this.getCookie(GUID);
  },
  getSubscribtionCartId: function() {
    return this.getCookie(cookies.subscrCartId);
  },
  // Get Apple Pay Merchant Name
  getApplePayMerchantName() {
      return this.getStorage('applePayMerchantName') || 'Digital Roadmap';
  },
  getThumbnailImageUrl(imagesArr) {
    if (imagesArr.length) {
      return imagesArr.filter(image => image.format == imageFormat.PRODUCT)[0]
        .url;
    } else {
      return imagesArr.filter(
        image => image.format == this.assetsPath + 'images/no_image.svg',
      );
    }
  },
  // Takes errors and returns first error input element name
  getElementName(errors) {
    return Object.keys(errors.collect())[0];
  },
  /**
   * Focusing the first error field in the form.
   * @param {Element} parentElement
   * @param {String} fieldName
   */
  setFocusByName(parentElement, fieldName) {
    if (parentElement.querySelector('input[name=' + fieldName + ']')) {
      parentElement.querySelector('input[name=' + fieldName + ']').focus();
    } else if (
      parentElement.querySelector('section[aria-label=' + fieldName + ']')
    ) {
      parentElement
        .querySelector('section[aria-label=' + fieldName + '] button')
        .focus();
    }

    //document.getElementsByName(firstField)[0].focus();
  },
};

const displayYcontentWrapper = function() {
  // Loading all components with authorized content
  var documentElement = document.getElementsByClassName('yComponentWrapper');
  for (var i = 0; i < documentElement.length; i++) {
    documentElement[i].style.display = 'block';
  }
};
const jsLoad = function() {
  if (!$('#cboxPrevious').html()) {
    $('#cboxPrevious')
      .attr('aria-hidden', 'true')
      .html('&lt;');
  }
  if (!$('#cboxNext').html()) {
    $('#cboxNext')
      .attr('aria-hidden', 'true')
      .html('&gt;');
  }
  if (!$('#cboxSlideshow').html()) {
    $('#cboxSlideshow')
      .attr('aria-hidden', 'true')
      .html('slide');
  }

  // Recomenended js
  $.getScript(
    `${
      globals.contextPath
    }/_ui/addons/gpymktrecommendationaddon/shared/common/js/gpymktrecommendationaddon.js`,
    () => {},
  );
  // handling yform show for sites without cart
  // for sites with cart this is handled in brand specific js
  if (!this.cartExists && globals.thirdPartyApps.yforms.endPoint) {
    $.getScript(
      `${globals.commonResourcePath}${globals.thirdPartyApps.yforms.endPoint}`,
      () => {
        $('.vx-ymarket-form').show(); // y-form should be visible when cart exists
        ymOverrides.maskTelephone();
        ymOverrides.setHiddenFieldValues();
      },
    );
  }
};

displayYcontentWrapper();
setTimeout(jsLoad, 2000);

let intervalCount = 0;
let intervalData = '';
const intervalLimit = 10;
const storageLimit = 5;

window.onbeforeunload = event => {
  const pathName = location.pathname;
  const scrollPosition = window.pageYOffset;
  if (scrollPosition) {
    setLocalStorage(pathName, scrollPosition);
  }
};

window.onload = event => {
  if (event.currentTarget.performance.navigation.type === 2) {
    const pathName = location.pathname;
    if (
      !intervalData && localStorage.getItem('scrollRestoration') &&
      JSON.parse(localStorage.getItem('scrollRestoration'))[pathName]
    ) {
      intervalData = window.setInterval(backButton, 1000);
    }
  }
};

const backButton = function() {
  console.log('Back Button Check Running');
  const pathName = location.pathname;
  const scrollToData = JSON.parse(localStorage.getItem('scrollRestoration'))[
    pathName
  ];
  if (
    document.getElementsByClassName('main__inner-wrapper')[0] &&
    document.getElementsByClassName('main__inner-wrapper')[0].childNodes
      .length &&
    document.body.scrollHeight > window.innerHeight
  ) {
    clearInterval(intervalData);
    window.scrollTo(0, scrollToData);
  }
  intervalCount >= intervalLimit
    ? clearInterval(intervalData)
    : intervalCount++;
};

const setLocalStorage = function(pathName, scrollPosition) {
  const scrollRestoration = localStorage.getItem('scrollRestoration');
  let scrollDataLength = 0;
  let scrollData = JSON.parse(scrollRestoration);
  if (scrollRestoration) {
    scrollDataLength = Object.keys(scrollData).length;
  }
  if (scrollData && scrollData[pathName]) {
    scrollData[pathName] = scrollPosition;
  } else {
    if (scrollData && scrollDataLength >= storageLimit) {
      delete scrollData[Object.keys(scrollData)[0]];
    }
    scrollData = {
      ...scrollData,
      [pathName]: scrollPosition,
    };
  }
  localStorage.setItem('scrollRestoration', JSON.stringify(scrollData));
};
export default globals;
