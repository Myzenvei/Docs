import Vue from 'vue';
import facebookLogin from './facebook-login.vue';

/* eslint-disable */
describe('facebookLogin', () => {
  it('renders appId props correctly', () => {
    const Constructor = Vue.extend(facebookLogin);
    const comp = new Constructor().$mount();
    comp.appId = 'This is appId';
    expect(comp.appId).to.equal('This is appId');
  });
});
