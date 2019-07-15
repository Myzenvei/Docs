import Vue from 'vue';
import vxModal from './modal.vue';

/* eslint-disable */
describe('modal', () => {
  it('renders heading props correctly', () => {
    const Constructor = Vue.extend(modal);
    const comp = new Constructor({
      propsData: {
        // Props are passed in "propsData".
        heading: 'This is heading',
      },
      data: {
        // data are passed in "data".
        showModal: true,
      },
    }).$mount();
    expect(comp.$el.querySelector('.heading-text').textContent).to.equal(
      'This is heading',
    );
  });
});
