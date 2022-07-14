import { apiService } from '@/api';

export default {

  namespaced: true,

  state: {
    token: '',
    user: {},
    loading: false,
  },

  mutations: {
    updateUser(state, { user, token, force }) {
      if (force || user.userName !== state.user.userName) {
        state.user = user;
      }
      if (token !== state.token) {
        state.token = token;
      }
    },
    clearUser(state) {
      state.user = {};
      state.token = '';
    },
  },

  actions: {
    updateUser({ state, commit }, { user, token }) {
      const userUpdated = user.userName !== state.user.userName;
      commit('updateUser', { user, token });
      if (!userUpdated) {
        return Promise.resolve(state.user);
      }
      return apiService.get('/me')
        .then((response) => {
          commit('updateUser', { user: response.data, token, force: true });
          return state.user;
        });
    },

  },
};
