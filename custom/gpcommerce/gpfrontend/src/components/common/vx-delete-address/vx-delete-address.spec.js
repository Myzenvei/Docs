import Vue from 'vue';
import deleteAddress from './vx-delete-address.vue';

describe('Delete Address Component', () => {
  it('renders i18n props correctly', () => {
    const Constructor = Vue.extend(deleteAddress);
    const comp = new Constructor({
      propsData: {
        i18n: {
          content: 'This is Modal Content',
        },
      },
    }).$mount();
    Vue.nextTick(() => {
      expect(comp.$el.querySelector('p').textContent).to.equal('This is Modal Content');
    });
  });
});
