import Vue from 'vue';
import shareItem from './vx-share-item-modal.vue';

describe('Share Item Component', () => {
  it('renders i18n props correctly', () => {
    const Constructor = Vue.extend(shareItem);
    const comp = new Constructor({
      propsData: {
        i18n: {
          content: 'This is Modal Content'
        }
      },
    }).$mount();
    expect(comp.$el.querySelector('.share-item-modal-content').textContent).to.equal('This is Modal Content');
  })
})
