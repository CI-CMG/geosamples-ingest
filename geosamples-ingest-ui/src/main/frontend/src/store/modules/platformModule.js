import { apiService } from '@/api';

const defaultParams = {
  id: '',
  platform: '',
  masterId: '',
  icesCode: '',
};

export default {

  namespaced: true,

  state: {
    items: [],
    searching: false,

    page: 1,
    totalPages: 1,
    totalItems: 0,

    params: { ...defaultParams },

    sortBy: 'platform',
    sortDesc: false,

    platform: null,
    loading: false,
    saving: false,
    deleting: false,

  },

  mutations: {
    platformRequest(state) {
      state.loading = true;
    },
    platformSuccess(state, platform) {
      state.loading = false;
      state.platform = platform;
    },
    platformFailure(state) {
      state.loading = false;
    },

    platformSaveRequest(state) {
      state.saving = true;
    },
    platformSaveSuccess(state, platform) {
      state.saving = false;
      state.platform = platform;
    },
    platformSaveFailure(state) {
      state.saving = false;
    },

    platformDeleteRequest(state) {
      state.saving = true;
    },
    platformDeleteSuccess(state) {
      state.saving = false;
    },
    platformDeleteFailure(state) {
      state.saving = false;
    },

    firstPage(state) {
      state.page = 1;
      state.totalPages = 1;
      state.totalItems = 0;
    },
    setId(state, id) {
      state.params.id = id;
    },
    setPlatform(state, platform) {
      state.params.platform = platform;
    },
    setIcesCode(state, icesCode) {
      state.params.icesCode = icesCode;
    },
    setMasterId(state, masterId) {
      state.params.masterId = masterId;
    },
    setPage(state, page) {
      state.page = page;
    },
    setSortBy(state, sortBy) {
      state.sortBy = sortBy;
    },
    setSortDesc(state, sortDesc) {
      state.sortDesc = sortDesc;
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
    // eslint-disable-next-line no-unused-vars
    changePage({ dispatch, commit }, page) {
      commit('setPage', page);
      return dispatch('searchPage');
    },
    search({ commit, dispatch }) {
      commit('firstPage');
      return dispatch('searchPage');
    },
    reset({ commit, dispatch }) {
      commit('firstPage');
      commit('clearParams');
      return dispatch('searchPage');
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
      return apiService.get('/platform', {
        params:
          {
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
      commit('platformRequest');
      return apiService.get(`/platform/${id}`)
        .then(
          (response) => {
            commit('platformSuccess', response.data);
            return response.data;
          },
          (error) => {
            commit('platformFailure');
            throw error;
          },
        );
    },
    save({ commit }, { provider, id }) {
      commit('platformSaveRequest');
      const req = id ? () => apiService.put(`/platform/${id}`, provider) : () => apiService.post('/platform', provider);
      return req()
        .then(
          (response) => {
            commit('platformSaveSuccess', response.data);
            return response.data;
          },
          (error) => {
            commit('platformSaveFailure');
            const { response } = error;
            if (response) {
              const { data } = response;
              if (data) {
                const { formErrors } = data;
                if (formErrors) {
                  const paths = Object.keys(formErrors);
                  paths.forEach((path) => {
                    const message = formErrors[path].join(', ');
                    commit('platformForm/setTouched', { path, touched: false }, { root: true });
                    commit('platformForm/setError', { path, error: message }, { root: true });
                  });
                }
              }
            }
            throw error;
          },
        );
    },
    delete({ commit }, id) {
      commit('platformDeleteRequest');
      return apiService.delete(`/platform/${id}`)
        .then(
          (response) => {
            commit('platformDeleteSuccess', response.data);
            return response.data;
          },
          (error) => {
            commit('platformDeleteFailure');
            throw error;
          },
        );
    },
  },
};
