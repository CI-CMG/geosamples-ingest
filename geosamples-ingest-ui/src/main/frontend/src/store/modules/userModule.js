import { userService } from '@/api';

export default {

  namespaced: true,

  state: {
    token: '',
    user: {},
  },

  getters: {
    user(state) {
      return state.user;
    },
    token(state) {
      return state.token;
    },
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
      return userService.get('/me')
        .then((response) => {
          commit('updateUser', { user: response.data, token, force: true });
          return state.user;
        });
    },

  },
};
