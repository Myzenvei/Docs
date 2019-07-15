import Vue from 'vue';
import vxImageTile from './vx-image-tile.vue';

describe('ViewSiteContentVxImageTileComponent', () => {
  it('Props are passed correctly', () => {
    const Constructor = Vue.extend(vxImageTile);
    const imageTile = new Constructor({
      propsData: {
        imageTileData: {
          imageAltText: 'Alternate text for image',
        },
      },
    }).$mount();
    expect(imageTile.$el.querySelector('img').alt).to.equal('Alternate text for image');
  });
});
