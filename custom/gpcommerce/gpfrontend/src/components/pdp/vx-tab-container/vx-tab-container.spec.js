import Vue from 'vue';
import productTabContainer from './vx-tab-container.vue';

describe('Product Tab Container Component', () => {
  it('renders i18n props correctly', () => {
    const Constructor = Vue.extend(productTabContainer);
    const comp = new Constructor({
      propsData: {
        i18n: {
          tabDetails: 'This is Tab Details Label'
        }
      },
    }).$mount();
    expect(comp.$el.querySelector('.active').textContent).to.equal('This is Tab Details Label');
  })
})
