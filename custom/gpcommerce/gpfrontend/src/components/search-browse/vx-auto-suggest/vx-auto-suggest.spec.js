import Vue from 'vue';
import autoSuggest from './vx-auto-suggest.vue';

describe('SearchBrowseAutoSuggestComponent', () => {
  it('renders productData props correctly', () => {
    const Constructor = Vue.extend(autoSuggest);
    const comp = new Constructor({
      propsData: {
        i18n: {
          heading: 'This is the Heading'
        }
      },
    }).$mount();
    expect(comp.$el.querySelectorAll('.view-all h5').textContent).to.equal('This is the Heading');
  })
})
