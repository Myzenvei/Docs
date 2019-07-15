import Vue from 'vue';
import shoppingLists from './vx-shopping-lists.vue';

describe('Shopping Lists Component', () => {
  it('renders i18n props correctly', () => {
    const Constructor = Vue.extend(shoppingLists);
    const comp = new Constructor({
      propsData: {
        i18n: {
          shoppingLists: {
            createListLink: 'This is Create List link',
          },
        },
      },
    }).$mount();
    Vue.nextTick(() => {
      expect(comp.$el.querySelector('a').textContent).to.equal('This is Create List link');
    });
  });
});
