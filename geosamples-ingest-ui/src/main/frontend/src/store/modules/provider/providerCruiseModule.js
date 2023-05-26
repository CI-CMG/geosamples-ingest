import { apiService } from '@/api';

const defaultParams = {
  cruiseNameContains: '',
  cruiseNameEquals: '',
  year: '',
  publish: '',
  platformEquals: '',
  approvalState: '',
};

const loadAll = (endpoint, transform, result = [], page = 1) => apiService.get(endpoint, {
  params: { page },
}).then(
  (response) => {
    const { items, totalPages } = response.data;
    items.forEach((item) => result.push(transform(item)));
    if (page < totalPages) {
      return loadAll(endpoint, transform, result, page + 1);
    }
    return result;
  },
);

const sortOptions = (options) => {
  options.sort((a, b) => {
    const atxt = a.text.toLowerCase();
    const btxt = b.text.toLowerCase();
    if (atxt === btxt) {
      return 0;
    } if (atxt > btxt) {
      return 1;
    }
    return -1;
  });
  return options;
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

    sortBy: 'cruiseName',
    sortDesc: false,

    item: null,
    loading: false,
    saving: false,
    deleting: false,
    loadingApproval: false,
    options: {},
    loadingOptions: false,

  },

  mutations: {
    loadOptionsRequest(state) {
      state.loadingOptions = true;
    },

    loadOptionsComplete(state) {
      state.loadingOptions = false;
    },

    updateOptions(state, options) {
      state.options = options;
    },

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

    setCruiseNameContains(state, value) {
      state.params.cruiseNameContains = value;
    },
    setCruiseNameEquals(state, value) {
      state.params.cruiseNameEquals = value;
    },
    setYear(state, value) {
      state.params.year = value;
    },
    setPlatformEquals(state, value) {
      state.params.platformEquals = value;
    },
    setApprovalState(state, value) {
      state.params.approvalState = value;
    },
    setPublish(state, value) {
      state.params.publish = value;
    },

    setPage(state, page) {
      if (page == null || page < 1) {
        state.page = 1;
      } else {
        state.page = page;
      }
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
    loadApprovalRequest(state) {
      state.loadingApproval = true;
    },
    loadApprovalComplete(state) {
      state.loadingApproval = false;
    },
  },

  actions: {
    loadOptions({ commit }) {
      commit('loadOptionsRequest');
      commit('updateOptions', {});
      const nextOpts = {};

      Promise.all([
        loadAll('/provider/platform', ({ platform }) => ({ value: platform.toUpperCase(), text: platform })).then(sortOptions).then((options) => { nextOpts.platform = options; return options; }),
      ]).then(() => {
        commit('updateOptions', nextOpts);
        commit('loadOptionsComplete');
      });
    },

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
      return apiService.get('/provider/cruise', {
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
      return apiService.get(`/provider/cruise/${id}`)
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
      const req = id ? () => apiService.put(`/provider/cruise/${id}`, provider) : () => apiService.post('/provider/cruise', provider);
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
                    commit('providerCruiseForm/setTouched', { path, touched: false }, { root: true });
                    commit('providerCruiseForm/setError', { path, error: message }, { root: true });
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
      return apiService.delete(`/provider/cruise/${id}`)
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
    loadApproval({ commit }, id) {
      commit('loadApprovalRequest');
      return apiService.get(`/provider/cruise/approval/${id}`)
        .then(
          (response) => {
            commit('loadApprovalComplete');
            return response.data;
          },
          (error) => {
            commit('loadApprovalComplete');
            throw error;
          },
        );
    },
  },
};
