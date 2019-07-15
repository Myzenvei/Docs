import Vue from 'vue';
import vxStepperControl from './vx-stepper-control.vue';

/* eslint-disable */
describe('stepperControl', () => {
  it('renders value props correctly', () => {
    const Constructor = Vue.extend(vxStepperControl);
    const comp = new Constructor().$mount();
    comp.value = 12;
    expect(comp.value).to.equal(12);
  });
});
