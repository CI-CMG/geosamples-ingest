import { apiService } from '@/api';

const defaultParams = {};

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

    sortBy: 'interval',
    sortDesc: false,

    item: null,
    loading: false,
    saving: false,
    deleting: false,
    loadingApproval: false,
    loadingSampleIntervals: false,

    options: {},
    loadingOptions: false,

    sampleIntervals: [],
    sampleIntervalPage: 1,
    sampleIntervalTotalPages: 1,
    sampleIntervalTotalItems: 0,
    sampleIntervalItemsPerPage: 20,
  },

  mutations: {
    setSampleIntervalPage(state, page) {
      state.sampleIntervalPage = page;
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

    loadOptionsRequest(state) {
      state.loadingOptions = true;
    },

    loadOptionsComplete(state) {
      state.loadingOptions = false;
    },

    updateOptions(state, options) {
      state.options = options;
    },

    loadSampleIntervalsRequest(state) {
      state.loadingSampleIntervals = true;
    },
    loadSampleIntervalsSuccess(state, {
      items, page, totalPages, totalItems,
    }) {
      state.loadingSampleIntervals = false;
      state.sampleIntervals = items;
      state.sampleIntervalPage = page;
      state.sampleIntervalTotalPages = totalPages;
      state.sampleIntervalTotalItems = totalItems;
    },
    loadSampleIntervalsFailure(state) {
      state.loadingSampleIntervals = false;
    },
    setPage(state, page) {
      if (page == null || page < 1) {
        state.page = 1;
      } else {
        state.page = page;
      }
    },
  },

  actions: {
    loadOptions({ commit }) {
      commit('loadOptionsRequest');
      commit('updateOptions', {});
      const nextOpts = {};

      Promise.all([
        loadAll('/lithology', ({ lithology, lithologyCode }) => ({ value: lithologyCode, text: lithology })).then(sortOptions).then((options) => { nextOpts.lithologyCode = options; return options; }),
        loadAll('/texture', ({ texture, textureCode }) => ({ value: textureCode, text: texture })).then(sortOptions).then((options) => { nextOpts.textureCode = options; return options; }),
        loadAll('/age', ({ age, ageCode }) => ({ value: ageCode, text: age })).then(sortOptions).then((options) => { nextOpts.ageCode = options; return options; }),
        loadAll('/rock-mineral', ({ rockMineral, rockMineralCode }) => ({ value: rockMineralCode, text: rockMineral })).then(sortOptions).then((options) => { nextOpts.rockMineralCode = options; return options; }),
        loadAll('/weathering', ({ weathering, weatheringCode }) => ({ value: weatheringCode, text: weathering })).then(sortOptions).then((options) => { nextOpts.weathMetaCode = options; return options; }),
        loadAll('/remark', ({ remark, remarkCode }) => ({ value: remarkCode, text: remark })).then(sortOptions).then((options) => { nextOpts.remarkCode = options; return options; }),
        loadAll('/munsell', ({ munsellCode }) => ({ value: munsellCode, text: munsellCode })).then(sortOptions).then((options) => { nextOpts.munsellCode = options; return options; }),
        loadAll('/device', ({ device, deviceCode }) => ({ value: deviceCode, text: device })).then(sortOptions).then((options) => { nextOpts.deviceCode = options; return options; }),
        loadAll('/provider/sample', ({ sample, cruise, imlgs }) => ({ value: imlgs, text: `${sample} (${cruise})` })).then(sortOptions).then((options) => { nextOpts.imlgs = options; return options; }),
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
      return apiService.get('/provider/interval', {
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
    searchByImlgs({ state, commit }, imlgs) {
      commit('loadSampleIntervalsRequest');
      return apiService.get(`/provider/interval?imlgs=${imlgs}&page=${state.sampleIntervalPage}&itemsPerPage=${state.sampleIntervalItemsPerPage}`).then(({ data }) => {
        commit('loadSampleIntervalsSuccess', data);
        return data;
      },
      (error) => {
        commit('loadSampleIntervalsFailure');
        throw error;
      });
    },
    load({ commit }, id) {
      commit('loadRequest');
      return apiService.get(`/provider/interval/${id}`)
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
      const req = id ? () => apiService.put(`/provider/interval/${id}`, provider) : () => apiService.post('/provider/interval', provider);
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
                    commit('providerIntervalForm/setTouched', { path, touched: false }, { root: true });
                    commit('providerIntervalForm/setError', { path, error: message }, { root: true });
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
      return apiService.delete(`/provider/interval/${id}`)
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
      return apiService.get(`/provider/interval/approval/${id}`)
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
