import { apiService } from '@/api';

export default {

  namespaced: true,

  state: {
    authorities: [],
    loading: false,
  },

  mutations: {
    loadRequest(state) {
      state.loading = true;
    },
    loadSuccess(state, authorities) {
      state.loading = false;
      state.authorities = authorities;
    },
    loadFailure(state) {
      state.loading = false;
    },
    clearAll(state) {
      state.authorities = [];
    },
  },

  actions: {
    load({ commit }) {
      commit('loadRequest');
      return apiService.get('/descriptor/authority')
        .then(
          (response) => {
            commit('loadSuccess', response.data.items);
            return response.data.items;
          },
          (error) => {
            commit('loadFailure');
            throw error;
          },
        );
    },
  },
};
