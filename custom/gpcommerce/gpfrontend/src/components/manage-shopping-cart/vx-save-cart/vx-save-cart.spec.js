import Vue from 'vue';
import VxSaveCart from './vx-save-cart.vue';

/* eslint-disable */
describe('VxSaveCart', () => {
  const Constructor = Vue.extend(VxSaveCart);

  it('renders i18n props correctly', () => {
    const comp = new Constructor({
      propsData: {
        i18n: {
          save: 'This is some text',
        },
      },
    }).$mount();
    expect(comp.$el.querySelector('.save-list').textContent).to.equal(
      'This is some text',
    );
  });
});
