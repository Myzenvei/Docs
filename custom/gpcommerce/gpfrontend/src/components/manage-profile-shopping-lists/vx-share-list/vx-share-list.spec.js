import Vue from 'vue';
import shareList from './vx-share-list.vue';

describe('Share List Component', () => {
  it('renders i18n props correctly', () => {
    const Constructor = Vue.extend(shareList);
    const comp = new Constructor({
      propsData: {
        i18n: {
          shareList: {
            shareListDescription: 'This is Share List Description',
          },
        },
      },
    }).$mount();
    Vue.nextTick(() => {
      expect(comp.$el.querySelector('p').textContent).to.equal('This is Share List Description');
    });
  });
});
