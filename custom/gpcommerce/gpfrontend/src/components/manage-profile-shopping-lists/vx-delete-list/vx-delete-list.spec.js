import Vue from 'vue';
import deleteList from './vx-delete-list.vue';

describe('Delete List Component', () => {
  it('renders i18n props correctly', () => {
    const Constructor = Vue.extend(deleteList);
    const comp = new Constructor({
      propsData: {
        i18n: {
          deleteList: {
            content: 'This is Modal Content',
          },
        },
      },
    }).$mount();
    Vue.nextTick(() => {
      expect(comp.$el.querySelector('p').textContent).to.equal('This is Modal Content');
    });
  });
});
