import Vue from 'vue';
import flyoutBannerSecondary from './flyout-banner-secondary.vue';

/* eslint-disable */
describe('flyoutBannerSecondary', () => {
  it('renders flyout message correctly', () => {
    const Constructor = Vue.extend(flyoutBannerSecondary);
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
