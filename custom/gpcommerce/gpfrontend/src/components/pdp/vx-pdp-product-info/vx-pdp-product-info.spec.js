import Vue from 'vue';
import pdpProductInfo from './vx-pdp-product-info.vue';

describe('PDP Product Info Component', () => {
  it('renders i18n props correctly', () => {
    const Constructor = Vue.extend(pdpProductInfo);
    const comp = new Constructor({
      propsData: {
        i18n: {
          quantity: 'This is Quantity Label'
        }
      },
    }).$mount();
    expect(comp.$el.querySelector('.qty-label').textContent).to.equal('This is Quantity Label');
  })
})
