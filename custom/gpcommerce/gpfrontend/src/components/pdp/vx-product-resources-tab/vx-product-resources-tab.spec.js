import Vue from 'vue';
import productResourcesTab from './vx-product-resources-tab.vue';

describe('Product Resources Tab Component', () => {
  it('renders i18n props correctly', () => {
    const Constructor = Vue.extend(productResourcesTab);
    const comp = new Constructor({
      propsData: {
        i18n: {
          topLeftHeading: 'This is Heading'
        }
      },
    }).$mount();
    expect(comp.$el.querySelector('.product-resources-tab__topleftheading').textContent).to.equal('This is Heading');
  })
})
