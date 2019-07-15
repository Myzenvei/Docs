import Vue from 'vue';
import vxCartProductTile from './vx-cart-product-tile.vue';

/* eslint-disable */
describe('vxCartProductTile', () => {
  const Constructor = Vue.extend(vxCartProductTile);

  it('renders i18n props correctly', () => {
    const comp = new Constructor({
      propsData: {
        isMiniCart: false,
        i18n: {
          quantity: 'Quantity',
        },
      },
    }).$mount();
    Vue.nextTick(() => {
      expect(comp.$el.querySelector('.product-quantity').textContent).to.equal('Quantity');
    });
  });

  it('renders productData props correctly', () => {
    const comp = new Constructor({
      propsData: {
        isMiniCart: true,
        i18n: {
          quantity: 'Quantity',
        },
        productData: {
          totalPrice: {
            formattedValue: '£14.54',
          },
        },
      },
    }).$mount();
    Vue.nextTick(() => {
      expect(comp.$el.querySelectorAll('.product-total-price')[0].textContent).to.equal('£14.54');
    });
  });
});
