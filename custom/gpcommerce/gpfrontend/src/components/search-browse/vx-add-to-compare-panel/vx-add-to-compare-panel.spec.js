import Vue from 'vue';
import panel from './vx-add-to-compare-panel.vue';

describe('SearchBrowsePanelComponent', () => {
  it('renders products props properly', () => {
    const Constructor = Vue.extend(panel);
    const comp = new Constructor().$mount();
    comp.products = 'test';
    expect(comp.products).to.equal('test');
  })

  it('renders disableButtonFalse props properly', () => {
    const Constructor = Vue.extend(panel);
    const comp = new Constructor().$mount();
    comp.disableButtonFalse = 'test';
    expect(comp.disableButtonFalse).to.equal('test');
  })
})
