import Vue from 'vue';
import createList from './vx-create-list.vue';

describe('Create List Component', () => {
  it('renders i18n props correctly', () => {
    const Constructor = Vue.extend(createList);
    const comp = new Constructor({
      propsData: {
        i18n: {
          createList: {
            content: 'This is Create List label',
          },
        },
      },
    }).$mount();
    Vue.nextTick(() => {
      expect(comp.$el.querySelector('p').textContent).to.equal('This is Create List label');
    });
  });
});
