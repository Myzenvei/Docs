import Vue from 'vue';
import VxPromotion from './vx-promotion.vue';

/* eslint-disable */
describe('VxPromotion', () => {
  const Constructor = Vue.extend(VxPromotion);

  it('renders promotionData array props correctly', () => {
    const comp = new Constructor({
      propsData: {
        i18n: {
          applyProduct: 'This is some text',
        },
        promotionData: [
          { voucherCode: 'this is some code' },
          { voucherCode: 'this is some other code' },
        ],
      },
    }).$mount();
    expect(
      comp.$el.querySelectorAll('.voucher-coupon')[0].textContent,
    ).to.equal('this is some code');
  });

  it('renders i18n props correctly', () => {
    const comp = new Constructor({
      propsData: {
        i18n: {
          applyProduct: 'This is some text',
        },
      },
    }).$mount();
    expect(comp.$el.querySelector('.btn-tertiary').textContent).to.equal(
      'This is some text',
    );
  });
});

