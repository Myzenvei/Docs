import Vue from 'vue';
import forgotPassword from './forgot-password.vue';

/* eslint-disable */
describe('forgotPassword', () => {
  const Constructor = Vue.extend(forgotPassword);

  it('renders i18n props correctly', () => {
    const comp = new Constructor({
      propsData: {
        // Props are passed in "propsData".
        i18n: {
          passwordHeading: 'This is some text',
        },
      },
    }).$mount();
    expect(comp.$el.querySelector('.email-heading').textContent).to.equal('This is some text');
  });
});
