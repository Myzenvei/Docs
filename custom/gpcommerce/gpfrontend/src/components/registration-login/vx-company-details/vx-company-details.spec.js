import Vue from 'vue';
import companyDetails from './company-details.vue';

/* eslint-disable */
describe('companyDetails', () => {
  it('renders userData props correctly', () => {
    const Constructor = Vue.extend(companyDetails);
    const comp = new Constructor({
      propsData: {
        // Props are passed in "propsData".
        userData: {
          country: {
            label: 'UNITED STATES',
            value: 'US',
          },
        },
      },
    }).$mount();
    expect(comp.userData.country.label).to.equal('UNITED STATES');
  });
});
