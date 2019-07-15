import Vue from 'vue';
import updatePassword from './update-password.vue';

/* eslint-disable */
describe('updatePassword', () => {
  it('renders heading coming through i18n correctly', () => {
    const Constructor = Vue.extend(updatePassword);
    const comp = new Constructor({
      propsData: {
        // Props are passed in "propsData".
        i18n: {
          updatePasswordTitle: 'This is props heading',
        },
      },
    }).$mount();
    expect(comp.$el.querySelector('.update-password-title').textContent).to.equal(
      'This is props heading',
    );
  });

  it('renders heading props correctly', () => {
    const Constructor = Vue.extend(updatePassword);
    const comp = new Constructor({
      propsData: {
        // Props are passed in "propsData".
        heading: 'This is heading',
      },
    }).$mount();
    Vue.nextTick(() => {
      expect(comp.$el.querySelector('.update-password-title').textContent).to.equal(
        'This is heading',
      );
    });
  });
});
