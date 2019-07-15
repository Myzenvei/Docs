import Vue from 'vue';
import vxMiniCart from './vx-mini-cart.vue';

/* eslint-disable */
describe('vxMiniCart', () => {
  const Constructor = Vue.extend(vxMiniCart);

  it('renders i18n props correctly', () => {
    const comp = new Constructor({
      propsData: {
        i18n: {
          subTotal: 'This is some text',
        },
      },
      data: {
        shoppingCartData: {
          totalItems: 5,
        },
      },
    }).$mount();
    Vue.nextTick(() => {
      expect(comp.$el.querySelector('.heading').textContent).to.equal('This is some text');
    });
  });
});
