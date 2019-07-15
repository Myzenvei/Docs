import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

const store = new Vuex.Store({
  // state of the components
  state: {},
  // All Filters Under getters
  getters: {},
  // Service calls under actions , also conditional actions and commits for mutation
  // To call actions use store.dispatch
  actions: {},
  // All state changes in mutations
  // To call actions use store.commit
  mutations: {},
  // Each modules have state, getters, actions, mutations
  modules: {},
});

export default store;
