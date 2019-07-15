import Vue from 'vue';
import VxEmptyCart from './vx-empty-cart.vue';

/* eslint-disable */
describe('VxEmptyCart', () => {
  const Constructor = Vue.extend(VxEmptyCart);

  it('renders i18n props correctly', () => {
    const comp = new Constructor({
      propsData: {
        i18n: {
          emptyShoppingCartHeading: 'This is some text',
        },
        isMiniCart: false,
      },
    }).$mount();
    expect(
      comp.$el.querySelector('.empty-shopping-cart-heading').textContent,
    ).to.equal('This is some text');
  });
});
