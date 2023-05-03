import { apiService } from '@/api';

export default {

  namespaced: true,

  state: {
    authorities: [],
    roles: [],
    loading: false,
  },

  mutations: {
    loadRequest(state) {
      state.loading = true;
    },
    loadAuthoritiesSuccess(state, authorities) {
      state.loading = false;
      state.authorities = authorities;
    },
    loadRolesSuccess(state, roles) {
      state.loading = false;
      state.roles = roles;
    },
    loadFailure(state) {
      state.loading = false;
    },
    clearAll(state) {
      state.authorities = [];
    },
  },

  actions: {
    loadAuthorities({ commit }) {
      commit('loadRequest');
      return apiService.get('/descriptor/authority')
        .then(
          (response) => {
            commit('loadAuthoritiesSuccess', response.data.items);
            return response.data.items;
          },
          (error) => {
            commit('loadFailure');
            throw error;
          },
        );
    },

    loadRoles({ commit }) {
      commit('loadRequest');
      return apiService.get('/descriptor/role')
        .then(
          (response) => {
            commit('loadRolesSuccess', response.data.items);
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
