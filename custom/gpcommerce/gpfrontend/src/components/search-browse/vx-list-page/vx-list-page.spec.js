import Vue from 'vue';
import listPage from './vx-list-page.vue';

describe('SearchBrowseListPageComponent', () => {
  it('renders Product props correctly', () => {
    const Constructor = Vue.extend(listPage);
    const comp = new Constructor().$mount();
    comp.products = "test";
    expect(comp.products).to.equal('test');
  })
})
