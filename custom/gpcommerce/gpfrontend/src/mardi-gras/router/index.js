import Vue from 'vue';
import Router from 'vue-router';

// Pages
import homePage from '../pages/home-page.vue';

// :todo
// import ourNapkinsPage from '@/pages/our-napkins-page';
// import findAndBuyPage from '@/pages/find-and-buy-page';
// import faqPage from '@/pages/faq-page';
// import teachersPage from '@/pages/teachers-page';
// import signupPage from '@/pages/sign-up-page';

Vue.use(Router);

export default new Router({
  mode: 'history',
  routes: [{
    path: '/',
    name: 'Home',
    component: homePage,
  }],
});
