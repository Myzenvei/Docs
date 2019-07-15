import Vue from 'vue';
import vxAddInstallation from './vx-add-installation.vue';

/* eslint-disable */
describe('vxAddInstallation', () => {
  const Constructor = Vue.extend(vxAddInstallation);

  it('renders i18n props correctly', () => {
    const comp = new Constructor({
      propsData: {
        i18n: {
          addInstallationButton: 'This is some text',
        },
      },
    }).$mount();
    Vue.nextTick(() => {
      expect(comp.$el.querySelector('.add-to-cart').textContent).to.equal('This is some text');
    });
  });
});
