import Vue from 'vue';
import Router from 'vue-router';

// Pages
import HomePage from '@/pages/home-page';
import LoginPage from '@/pages/login-page';
import RegistrationPage from '@/pages/registration-page';
import StyleGuidePage from '@/pages/styleguide-page';
import BannerCompPage from '@/pages/bannercomp-page';
import CommonComponents from '@/pages/common-components-page';
import FindStorePage from '@/pages/find-store-page';
import SocialFeedPage from '@/pages/social-feed-page';
import pdpPage from '@/pages/pdp-page';
import ListDetailsPage from '@/pages/list-details-page';
import ViewSiteContentPage from '@/pages/view-site-content-page';
import AddressPage from '@/pages/address-page';

import cart from '@/pages/cart';
import CheckoutPage from '@/pages/checkout';
import b2bOrg from '@/pages/b2bOrg-page';
import Permission from '@/pages/permissions-page';
import QuickOrderPage from '@/pages/quick-order-page';
import leftNav from '@/pages/left-nav';
import paymentForm from '@/pages/payment-form';
import ProfilePage from '@/pages/profile-page';
import orderConfirmationPage from '@/pages/order-confirmation';
import nfrPage from '@/pages/nfr-page';
import orderDetailsPage from '@/pages/order-details';


Vue.use(Router);

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'Home',
      component: HomePage,
    },
    {
      path: '/login',
      name: 'LoginPage',
      component: LoginPage,
    },
    {
      path: '/registration',
      name: 'RegistrationPage',
      component: RegistrationPage,
    },
    {
      path: '/styleGuide',
      name: 'StyleGuidePage',
      component: StyleGuidePage,
    },
    // TODO REMOVE COMP FROM NAMES, IT SHOULD BE BANNERPAGE
    {
      path: '/bannerComp',
      name: 'BannerCompPage',
      component: BannerCompPage,
    },
    {
      path: '/commonComponents',
      name: 'CommonComponentsPage',
      component: CommonComponents,
    },
    {
      path: '/leftNav',
      name: 'leftNavPage',
      component: leftNav,
    },
    {
      path: '/pdp',
      name: 'pdpPage',
      component: pdpPage,
    },
    {
      path: '/cart',
      name: 'Cart',
      component: cart,
    },
    {
      path: '/findStore',
      name: 'FindStorePage',
      component: FindStorePage,
    },
    {
      path: '/socialfeed',
      name: 'SocialFeedPage',
      component: SocialFeedPage,
    },
    {
      path: '/viewSiteContent',
      name: 'ViewSiteContentPage',
      component: ViewSiteContentPage,
    },
    {
      path: '/listDetails',
      name: 'ListDetailsPage',
      component: ListDetailsPage,
    },
    {
      path: '/quickOrder',
      name: 'QuickOrderPage',
      component: QuickOrderPage,
    },
    {
      path: '/checkout',
      name: 'CheckoutPage',
      component: CheckoutPage,
    },
    {
      path: '/b2bOrg',
      name: 'b2bOrg',
      component: b2bOrg,
    },
    {
      path: '/permssions',
      name: 'PermissionsLandingPage',
      component: Permission,
    },
    {
      path: '/profile',
      name: 'Profile',
      component: ProfilePage,
    },
    {
      path: '/address',
      name: 'AddressPage',
      component: AddressPage,
    },
    {
      path: '/paymentform',
      name: 'paymentform',
      component: paymentForm,
    },
    {
      path: '/orderConfirmation',
      name: 'orderConfirmation',
      component: orderConfirmationPage,
    },
    {
      path: '/nfr',
      name: 'nfrPage',
      component: nfrPage,
    },
    {
      path: '/orderDetails',
      name: 'orderDetails',
      component: orderDetailsPage,
    },
  ],
});
