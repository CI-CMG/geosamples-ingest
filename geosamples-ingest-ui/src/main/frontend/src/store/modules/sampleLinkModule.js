import { apiService } from '@/api';

const defaultParams = {
  id: '',
  imlgs: '',
  dataLink: '',
  linkLevel: '',
  linkSource: '',
  linkType: '',
  publish: '',
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

    sortBy: 'imlgs',
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
    setId(state, id) {
      state.params.id = id;
    },
    setImlgs(state, imlgs) {
      state.params.imlgs = imlgs;
    },
    setPublish(state, value) {
      state.params.publish = value;
    },
    setDataLink(state, dataLink) {
      state.params.dataLink = dataLink;
    },
    setLinkLevel(state, linkLevel) {
      state.params.linkLevel = linkLevel;
    },
    setLinkSource(state, linkSource) {
      state.params.linkSource = linkSource;
    },
    setLinkType(state, linkType) {
      state.params.linkType = linkType;
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
      return apiService.get('/sample-link', {
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
      commit('loadRequest');
      return apiService.get(`/sample-link/${id}`)
        .then(
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
    save({ commit }, { provider, id }) {
      commit('saveRequest');
      const req = id ? () => apiService.put(`/sample-link/${id}`, provider) : () => apiService.post('/sample-link', provider);
      return req()
        .then(
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
                    commit('sampleLinkForm/setTouched', { path, touched: false }, { root: true });
                    commit('sampleLinkForm/setError', { path, error: message }, { root: true });
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
      return apiService.delete(`/sample-link/${id}`)
        .then(
          (response) => {
            commit('deleteSuccess', response.data);
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
