import Vue from 'vue';
import flyoutBanner from './flyout-banner.vue';

/* eslint-disable */
describe('flyoutBanner', () => {
  it('renders flyout message correctly', () => {
    const Constructor = Vue.extend(flyoutBanner);
    const comp = new Constructor({
      data: {
        flyoutObject: {
          message: 'This is some message',
        },
        visible: true,
      },
    }).$mount();
    Vue.nextTick(() => {
      expect(comp.$el.querySelectorAll('.flyout-container span')[1].textContent).to.equal(
        'This is some message',
      );
    });
  });
});
