import { apiService } from '@/api';

const defaultParams = {
  roleNameContains: '',
};

export default {
  namespaced: true,

  state: {
    items: [],
    searching: false,
    params: { ...defaultParams },

    page: 1,
    totalPages: 1,
    totalItems: 0,

    sortBy: 'roleName',
    sortDesc: false,

    item: null,
    loading: false,
    saving: false,
    deleting: false,
  },

  mutations: {
    loadRequest(state) {
      state.loading = true;
    },

    loadSuccess(state, item) {
      state.loading = false;
      state.item = item;
    },

    loadFailure(state) {
      state.loading = false;
    },

    saveRequest(state) {
      state.saving = true;
    },

    saveSuccess(state, item) {
      state.saving = false;
      state.item = item;
    },

    saveFailure(state) {
      state.saving = false;
    },

    deleteRequest(state) {
      state.saving = true;
    },

    deleteSuccess(state) {
      state.saving = false;
    },

    deleteFailure(state) {
      state.saving = false;
    },

    firstPage(state) {
      state.page = 1;
      state.totalPages = 1;
      state.totalItems = 0;
    },

    setRoleNameContains(state, value) {
      state.params.roleNameContains = value;
    },

    setPage(state, value) {
      state.page = value;
    },

    setSortBy(state, value) {
      state.sortBy = value;
    },

    setSortDesc(state, value) {
      state.sortDesc = value;
    },

    clearParams(state) {
      state.params = { ...defaultParams };
    },

    searchRequest(state) {
      state.searching = true;
    },

    searchSuccess(state, data) {
      state.searching = false;
      state.items = data.items;
      state.page = data.page;
      state.totalPages = data.totalPages;
      state.totalItems = data.totalItems;
    },

    searchFailure(state) {
      state.searching = false;
    },

    clearAll(state) {
      state.params = { ...defaultParams };
      state.items = [];
      state.searching = false;
      state.page = 1;
      state.totalPages = 1;
      state.totalItems = 0;
    },
  },

  actions: {
    changePage({ commit, dispatch }, page) {
      commit('setPage', page);
      dispatch('searchPage');
    },

    search({ commit, dispatch }) {
      commit('firstPage');
      dispatch('searchPage');
    },

    reset({ commit, dispatch }) {
      commit('clearAll');
      commit('clearParams');
      dispatch('searchPage');
    },

    searchPage({ commit, state }) {
      commit('searchRequest');
      const keys = Object.keys(state.params);
      const params = {};
      keys.forEach((key) => {
        const value = state.params[key];
        if (value) {
          params[key] = value;
        }
      });

      return apiService.get('/role', {
        params: {
          ...params,
          page: state.page,
          order: `${state.sortBy}:${state.sortDesc ? 'desc' : 'asc'}`,
        },
      }).then(
        (response) => {
          commit('searchSuccess', response.data);
          return response.data;
        },
        (error) => {
          commit('searchFailure');
          throw error;
        },
      );
    },

    load({ commit }, id) {
      commit('loadRequest');
      return apiService.get(`/role/${id}`).then(
        (response) => {
          commit('loadSuccess', response.data);
          return response.data;
        },
        (error) => {
          commit('loadFailure');
          throw error;
        },
      );
    },

    save({ commit }, { role, roleId }) {
      commit('saveRequest');
      const req = roleId ? () => apiService.put(`/role/${roleId}`, role) : () => apiService.post('/role', role);
      return req().then(
        (response) => {
          commit('saveSuccess', response.data);
          return response.data;
        },
        (error) => {
          commit('saveFailure');
          const { response } = error;
          if (response) {
            const { data } = response;
            if (data) {
              const { formErrors } = data;
              if (formErrors) {
                const paths = Object.keys(formErrors);
                paths.forEach((path) => {
                  const message = formErrors[path].join(', ');
                  commit('roleForm/setTouched', { path, touched: false }, { root: true });
                  commit('roleForm/setError', { path, error: message }, { root: true });
                });
              }
            }
          }
          throw error;
        },
      );
    },

    delete({ commit }, id) {
      commit('deleteRequest');
      return apiService.delete(`/role/${id}`).then(
        (response) => {
          commit('deleteSuccess');
          return response.data;
        },
        (error) => {
          commit('deleteFailure');
          throw error;
        },
      );
    },
  },
};
