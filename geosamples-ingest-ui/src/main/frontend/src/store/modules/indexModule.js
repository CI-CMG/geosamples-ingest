import { indexService } from '@/api';

export default {

  namespaced: true,

  state: {
    index: [],
    loading: false,
  },

  mutations: {
    indexRequest(state) {
      state.loading = true;
      state.index = [];
    },
    indexSuccess(state, { items }) {
      state.loading = false;
      state.index = items;
    },
    indexFailure(state) {
      state.loading = false;
    },
  },

  actions: {
    loadIndex({ commit }) {
      commit('indexRequest');
      return indexService.get('/csb/index')
        .then(
          (response) => {
            commit('indexSuccess', response.data);
            return response.data;
          },
          (error) => {
            commit('indexFailure');
            throw error;
          },
        );
    },

  },
};
