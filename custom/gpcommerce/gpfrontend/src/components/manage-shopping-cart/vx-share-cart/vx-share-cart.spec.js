import Vue from 'vue';
import VxShareCart from './vx-share-cart.vue';

/* eslint-disable */
describe('VxShareCart', () => {
  const Constructor = Vue.extend(VxShareCart);

  it('renders i18n props correctly', () => {
    const comp = new Constructor({
      propsData: {
        i18n: {
          shareListButton: 'This is button text',
        },
      },
    }).$mount();
    expect(comp.$el.querySelector('.submit').textContent).to.equal(
      'This is button text',
    );
  });
});
