import Vue from 'vue';
import productTile from './vx-product-tile.vue';

describe('SearchBrowseProductTileComponent', () => {
  it('renders product props properly', () => {
    const Constructor = Vue.extend(productTile);
    const comp = new Constructor().$mount();
    comp.uncheckProduct = 'This is Product name';
    expect(comp.uncheckProduct).to.equal('This is Product name');
  })

  it('renders uncheckProduct props properly', () => {
    const Constructor = Vue.extend(productTile);
    const comp = new Constructor().$mount();
    comp.uncheckProduct = 'This will uncheck Product';
    expect(comp.uncheckProduct).to.equal('This will uncheck Product');
  })

  it('renders showRemove props properly', () => {
    const Constructor = Vue.extend(productTile);
    const comp = new Constructor().$mount();
    comp.showRemove = 'test';
    expect(comp.showRemove).to.equal('test');
  })

  it('renders showCompare props properly', () => {
    const Constructor = Vue.extend(productTile);
    const comp = new Constructor().$mount();
    comp.showCompare = 'test';
    expect(comp.showCompare).to.equal('test');
  })
})
