import Vue from 'vue';
import listDetails from './vx-list-details.vue';

describe('List Details Component', () => {
  it('renders i18n props correctly', () => {
    const Constructor = Vue.extend(listDetails);
    const comp = new Constructor({
      propsData: {
        i18n: {
          listDetails: {
            addListToCart: 'This is Add List To cart label',
          },
        },
      },
    }).$mount();
    Vue.nextTick(() => {
      expect(comp.$el.querySelector('button').textContent).to.equal('This is Add List To cart label');
    });
  });
});
