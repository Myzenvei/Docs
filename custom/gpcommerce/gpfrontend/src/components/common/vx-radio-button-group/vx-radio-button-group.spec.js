import Vue from 'vue';
import vxRadioButtonGroup from './vx-radio-button-group.vue';

/* eslint-disable */
describe('radioButtonGroup', () => {
  it('renders radiobuttonValues props correctly', () => {
    const Constructor = Vue.extend(vxRadioButtonGroup);
    const comp = new Constructor({
      propsData: {
        // Props are passed in "propsData".
        radiobuttonValues: [
          { label: 'radio1', value: 'radio1' },
          { label: 'radio2', value: 'radio2' },
        ],
      },
    }).$mount();
    expect(
      comp.$el.querySelectorAll('.form-check-label')[0].textContent,
    ).to.equal('radio1');
  });
});
