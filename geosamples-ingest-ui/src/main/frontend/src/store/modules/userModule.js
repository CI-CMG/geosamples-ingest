import { apiService } from '@/api';
import { setStorageFromAuthResponse, logout } from '@/sessionMonster';

export default {

  namespaced: true,

  state: {
    user: {},
    loading: false,
  },

  getters: {
    user(state) {
      return state.user;
    },
  },

  mutations: {
    updateUser(state, user) {
      state.user = user;
    },
    clearUser(state) {
      state.user = {};
    },
    loginRequest(state) {
      state.loading = true;
      state.user = {};
    },
    loginSuccess(state, user) {
      state.loading = false;
      state.user = user;
    },
    loginFailure(state) {
      state.loading = false;
    },
  },

  actions: {
    login({ commit }, {
      username, password, router, store,
    }) {
      commit('loginRequest');
      return apiService.get('/me', { headers: { Authorization: `Basic ${btoa(`${username}:${password}`)}` } })
        .then((response) => {
          const user = { ...response.data, username, password };
          commit('loginSuccess', user);
          setStorageFromAuthResponse(user, store);
          router.push({ name: 'Home' });
          return response.data;
        },
        (error) => {
          commit('loginFailure');
          const { response } = error;
          if (response) {
            const { data } = response;
            if (data) {
              const { formErrors } = data;
              if (formErrors) {
                const paths = Object.keys(formErrors);
                paths.forEach((path) => {
                  const message = formErrors[path].join(', ');
                  commit('loginForm/setTouched', { path, touched: false }, { root: true });
                  commit('loginForm/setError', { path, error: message }, { root: true });
                });
              }
            }
          }
          logout(router, store);
          throw error;
        });
    },
  },
};
