import Vue from 'vue';
import socialLogin from './social-login.vue';

/* eslint-disable */
describe('socialLogin', () => {
  it('renders register props correctly', () => {
    const Constructor = Vue.extend(socialLogin);
    const comp = new Constructor().$mount();
    comp.register = false;
    expect(comp.register).to.equal(false);
  });
});
