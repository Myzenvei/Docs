import Vue from 'vue';
import notifyMe from './vx-notify-me.vue';

describe('Notify Me Component', () => {
  it('renders i18n props correctly', () => {
    const Constructor = Vue.extend(notifyMe);
    const comp = new Constructor({
      propsData: {
        i18n: {
          emailAddress: 'This is Email Address Label'
        }
      },
    }).$mount();
    expect(comp.$el.querySelector('p').textContent).to.equal('This is Email Address Label');
  });
  it('renders title props correctly', () => {
    const Constructor = Vue.extend(notifyMe);
    const comp = new Constructor({
      propsData: {
        title: 'This is Title'
      },
    }).$mount();
    expect(comp.$el.querySelector('h4').textContent).to.equal('This is Title');
  })
})
