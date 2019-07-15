import Vue from 'vue';
import vxSlider from './vx-slider.vue';

describe('Slider Component', () => {
  it('renders title prop correctly', () => {
    const Constructor = Vue.extend(vxSlider);
    const comp = new Constructor({
      propsData: {
        title: 'This is the Title'
      },
    }).$mount();
    expect(comp.$el.querySelector('h3').textContent).to.equal('This is the Title');
  })
})
