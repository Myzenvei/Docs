import Vue from 'vue';
import vxTextTile from './vx-text-tile.vue';

describe('ViewSiteContentVxTextTileComponent', () => {
  it('Props are passed correctly', () => {
    const Constructor = Vue.extend(vxTextTile);
    const textTile = new Constructor({
      propsData: {
        textTileData: {
          header: 'Testing header',
        },
      },
    }).$mount();
    expect(textTile.$el.querySelector('h3').textContent).to.equal('Testing header');
  });
});
