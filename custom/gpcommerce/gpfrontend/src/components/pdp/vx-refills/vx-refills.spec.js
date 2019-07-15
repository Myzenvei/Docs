import Vue from 'vue';
import refillsModal from './vx-refills-modal.vue';

describe('Refills Modal Component', () => {
  it('renders i18n props correctly', () => {
    const Constructor = Vue.extend(refillsModal);
    const comp = new Constructor({
      propsData: {
        i18n: {
          noThanks: 'This is No Thanks Button'
        }
      },
    }).$mount();
    expect(comp.$el.querySelector('.btn-default').textContent).to.equal('This is No Thanks Button');
  })
})
