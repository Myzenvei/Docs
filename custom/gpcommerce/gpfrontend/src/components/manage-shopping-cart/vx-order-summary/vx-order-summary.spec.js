import Vue from 'vue';
import VxOrderSummary from './vx-order-summary.vue';

/* eslint-disable */
describe('VxOrderSummary', () => {
  const Constructor = Vue.extend(VxOrderSummary);

  it('renders i18n props correctly', () => {
    const comp = new Constructor({
      propsData: {
        i18n: {
          orderSummaryHeading: 'This is some text',
        },
      },
    }).$mount();
    expect(
      comp.$el.querySelector('.order-summary-heading').textContent,
    ).to.equal('This is some text');
  });
});
