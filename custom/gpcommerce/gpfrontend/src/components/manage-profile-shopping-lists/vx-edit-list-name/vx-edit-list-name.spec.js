import Vue from 'vue';
import editListName from './vx-edit-list-name.vue';

describe('Edit List Name Component', () => {
  it('renders i18n props correctly', () => {
    const Constructor = Vue.extend(editListName);
    const comp = new Constructor({
      propsData: {
        i18n: {
          editListName: {
            editListLabel: 'This is Edit List label',
          },
        },
      },
    }).$mount();
    Vue.nextTick(() => {
      expect(comp.$el.querySelector('p').textContent).to.equal('This is Edit List label');
    });
  });
});
