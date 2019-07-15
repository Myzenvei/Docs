import Vue from 'vue';
import productDetailTab from './vx-product-details-tab.vue';

describe('Product Details Tab Component', () => {
  it('renders i18n props correctly', () => {
    const Constructor = Vue.extend(productDetailTab);
    const comp = new Constructor({
      propsData: {
        i18n: {
          topLeftHeading: 'This is Heading'
        }
      },
    }).$mount();
    expect(comp.$el.querySelector('.product-details-tab__topleftheading').textContent).to.equal('This is Heading');
  })
})
