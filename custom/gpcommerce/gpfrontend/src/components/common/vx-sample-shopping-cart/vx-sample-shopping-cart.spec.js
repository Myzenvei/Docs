import Vue from 'vue';
import VxShoppingCart from './vx-shopping-cart.vue';

/* eslint-disable */
describe('VxShoppingCart', () => {
  const Constructor = Vue.extend(VxShoppingCart);

  it('renders i18n props correctly', () => {
    const comp = new Constructor({
      propsData: {
        i18n: {
          shoppingCartHeading: 'This is some text',
        },
      },
    }).$mount();
    expect(comp.$el.querySelector('.cart-heading').textContent).to.equal(
      'This is some text',
    );
  });
});
