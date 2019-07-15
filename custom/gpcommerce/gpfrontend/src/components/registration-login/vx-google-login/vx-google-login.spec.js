import Vue from 'vue';
import googleLogin from './google-login.vue';

/* eslint-disable */
describe('googleLogin', () => {
  it('renders googleClientId props correctly', () => {
    const Constructor = Vue.extend(googleLogin);
    const comp = new Constructor().$mount();
    comp.googleClientId = 'This is googleClientId';
    expect(comp.googleClientId).to.equal('This is googleClientId');
  });
});
