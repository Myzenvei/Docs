import Vue from 'vue';
import compareTable from './vx-compare-table.vue';

describe('SearchBrowseCompareTableComponent', () => {
  it('renders productData props correctly', () => {
    const Constructor = Vue.extend(compareTable);
    const comp = new Constructor({
      propsData: {
        productData: {
          specifications: [{
            categoryLabel: 'This is Category Label'
          }]
        }
      },
    }).$mount();
    expect(comp.$el.querySelectorAll('.category')[0].textContent).to.equal('This is Category Label');
  })
})
