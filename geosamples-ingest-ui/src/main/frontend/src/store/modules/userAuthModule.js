import { apiService } from '@/api';

export default {

  namespaced: true,

  state: {
    token: '',
    user: {},
    loading: false,
    lastUserToken: '',
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
    updateUserOnly(state, { user }) {
      if (user.userName === state.user.userName) {
        state.user = user;
      }
    },
    updateLastUserToken(state, { token }) {
      state.lastUserToken = token;
    },
    clearUser(state) {
      state.user = {};
      state.token = '';
      state.lastUserToken = '';
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
    generateToken({ state, commit }, { alias }) {
      commit('updateLastUserToken', { token: '' });
      return apiService.post('/user-token/generate', { alias })
        .then((response) => {
          commit('updateLastUserToken', { token: response.data.token });
          return response.data.token;
        })
        .then(() => apiService.get('/me'))
        .then((response) => {
          commit('updateUserOnly', { user: response.data });
          return state.lastUserToken;
        });
    },
    deleteToken({ commit }, { alias }) {
      commit('updateLastUserToken', { token: '' });
      return apiService.post('/user-token/delete', { alias })
        .then((response) => response.data.alias)
        .then(() => apiService.get('/me'))
        .then((response) => {
          commit('updateUserOnly', { user: response.data });
          return '';
        });
    },
  },
};
