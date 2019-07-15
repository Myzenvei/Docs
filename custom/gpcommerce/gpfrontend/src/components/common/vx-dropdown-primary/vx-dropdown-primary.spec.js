import Vue from 'vue';
import vxDropdownPrimary from './dropdown-primary.vue';

/* eslint-disable */
describe('DropdownPrimary', () => {
  it('renders dropdownValues props correctly', () => {
    const Constructor = Vue.extend(DropdownPrimary);
    const comp = new Constructor({
      propsData: {
        dropdownValues: [
          { label: 'value1', value: 'value1' },
          { label: 'value2', value: 'value2' },
        ],
      },
    }).$mount();
    expect(
      comp.$el.querySelectorAll('.dropdown-menu li a')[0].textContent,
    ).to.equal('value1');
  });
});
