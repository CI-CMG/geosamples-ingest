import { userService } from '@/api';

export default {

  namespaced: true,

  state: {
    users: [],
    page: 1,
    totalPages: 1,
    totalItems: 0,
    itemsPerPage: 50,
    loading: false,
    // for userAdminForm
    fetchedUser: false,
    user: {},
    fetchedAuthorities: false,
    authorities: [],
    savingUser: false,
  },

  getters: {
    users(state) {
      return state.users;
    },
    page(state) {
      return state.page;
    },
    totalPages(state) {
      return state.totalPages;
    },
    totalItems(state) {
      return state.totalItems;
    },
    itemsPerPage(state) {
      return state.itemsPerPage;
    },
    loading(state) {
      return state.loading;
    },
    //
    fetchedUser: (state) => state.fetchedUser,
    user: (state) => state.user,
    //
    fetchedAuthorities: (state) => state.fetchedAuthorities,
    authorities: (state) => state.authorities,
  },

  mutations: {
    handleRequest(state) {
      state.loading = true;
    },
    handleSuccess(state, data) {
      const {
        items, page, totalPages, totalItems, itemsPerPage,
      } = data;
      state.loading = false;
      state.users = items;
      state.page = page;
      state.totalPages = totalPages;
      state.totalItems = totalItems;
      state.itemsPerPage = itemsPerPage;
    },
    handleFailure(state) {
      state.loading = false;
    },

    // working on user updates
    userRequest(state) {
      state.fetchedUser = false;
      state.user = {};
    },
    userSuccess(state, user) {
      state.fetchedUser = true;
      state.user = user;
    },
    userFailure(state) {
      state.fetchedUser = true;
      state.user = {};
    },
    // list of authorities
    authorityRequest(state) {
      state.fetchedAuthorities = false;
      state.authorities = [];
    },
    authoritySuccess(state, authorities) {
      state.fetchedAuthorities = true;
      state.authorities = authorities;
    },
    authorityFailure(state) {
      state.fetchedAuthorities = true;
      state.authorities = [];
    },

    saveUserRequest(state) {
      state.fetchedUser = false;
    },
    saveUserSuccess(state, user) {
      state.fetchedUser = true;
      state.user = user;
    },
    saveUserFailure(state) {
      state.fetchedUser = true;
    },
  },

  actions: {
    loadPage({ commit }, { page, itemsPerPage, order }) {
      commit('handleRequest');
      const params = new URLSearchParams();
      params.append('page', page);
      params.append('itemsPerPage', itemsPerPage);
      order.forEach((o) => params.append('order', o));
      return userService.get('/user', { params })
        .then(
          (response) => {
            commit('handleSuccess', response.data);
            return response.data;
          },
          (error) => {
            commit('handleFailure');
            throw error;
          },
        );
    },

    loadUser({ commit }, id) {
      commit('userRequest');
      return userService.get(`/user/${encodeURIComponent(id)}`)
        .then(
          (response) => {
            commit('userSuccess', response.data);
            return response.data;
          },
          (error) => {
            commit('userFailure');
            throw error;
          },
        );
    },

    saveUser({ commit }, { user, id }) {
      commit('saveUserRequest');
      const req = id ? userService.put(`/user/${encodeURIComponent(id)}`, user) : userService.post('/user', user);
      return req.then(
        (response) => {
          commit('saveUserSuccess', response.data);
          return response.data;
        },
        (error) => {
          commit('saveUserFailure');
          const { response } = error;
          if (response) {
            const { data } = response;
            if (data) {
              const { formErrors } = data;
              if (formErrors) {
                const paths = Object.keys(formErrors);
                paths.forEach((path) => {
                  const message = formErrors[path].join(', ');
                  commit('userAdminForm/setTouched', { path, touched: false }, { root: true });
                  commit('userAdminForm/setError', { path, error: message }, { root: true });
                });
              }
            }
          }
          throw error;
        },
      );
    },

    loadAuthorities({ commit }) {
      commit('authorityRequest');
      return userService.get('/authority')
        .then(
          (response) => {
            commit('authoritySuccess', response.data.authorities);
            return response.data.authorities;
          },
          (error) => {
            commit('authorityFailure');
            throw error;
          },
        );
    },

  },
};
