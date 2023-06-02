import { apiService } from '@/api';

const defaultParams = {
  imlgs: '',
  cruise: '',
  sample: '',
  platform: '',
  deviceCode: '',
  igsn: '',
  approvalState: '',
  publish: '',
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

    sortBy: 'cruise',
    sortDesc: false,

    item: null,
    loading: false,
    saving: false,
    deleting: false,
    loadingApproval: false,
    loadingLegs: false,

    options: {},
    legOptions: [],
    loadingOptions: false,
    cruiseOptions: [],
    loadingCruises: false,
    loadingCruiseSamples: false,
    cruiseSamples: [],
    cruiseSamplesPage: 1,
    cruiseSamplesTotalPages: 1,
    cruiseSamplesTotalItems: 0,
    cruiseSamplesItemsPerPage: 20,

    platformOptions: [],
    loadingPlatformOptions: false,
  },

  mutations: {
    setCruiseSamplesPage(state, page) {
      state.cruiseSamplesPage = page;
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

    setApprovalState(state, approvalState) {
      state.params.approvalState = approvalState;
    },

    setPublish(state, publish) {
      state.params.publish = publish;
    },

    setImlgs(state, imlgs) {
      state.params.imlgs = imlgs;
    },

    setCruise(state, cruise) {
      state.params.cruise = cruise;
    },

    setSample(state, sample) {
      state.params.sample = sample;
    },

    setPlatform(state, platform) {
      state.params.platform = platform;
    },

    setDeviceCode(state, deviceCode) {
      state.params.deviceCode = deviceCode;
    },

    setIgsn(state, igsn) {
      state.params.igsn = igsn;
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

    loadLegOptionsRequest(state) {
      state.loadingLegs = true;
    },

    loadLegOptionsComplete(state) {
      state.loadingLegs = false;
    },

    updateLegOptions(state, legOptions) {
      state.legOptions = legOptions;
    },

    loadCruiseOptionsRequest(state) {
      state.loadingCruises = true;
    },

    loadCruiseOptionsSuccess(state, { items }) {
      state.cruiseOptions = items.map(({ cruiseName, year }) => ({ text: `${cruiseName} (${year})`, value: { cruiseName, year } }));
      state.loadingCruises = false;
    },

    loadCruiseOptionsFailure(state) {
      state.loadingCruises = false;
    },

    loadCruiseSamplesRequest(state) {
      state.loadingCruiseSamples = true;
    },
    loadCruiseSamplesSuccess(state, {
      items, page, totalItems, totalPages, itemsPerPage,
    }) {
      state.cruiseSamples = items;
      state.cruiseSamplesPage = page;
      state.cruiseSamplesTotalPages = totalPages;
      state.cruiseSamplesTotalItems = totalItems;
      state.cruiseSamplesItemsPerPage = itemsPerPage;

      state.loadingCruiseSamples = false;
    },
    loadCruiseSamplesFailure(state) {
      state.loadingCruiseSamples = false;
    },

    setPage(state, page) {
      if (page == null || page < 1) {
        state.page = 1;
      } else {
        state.page = page;
      }
    },

    loadPlatformOptionsRequest(state) {
      state.loadingPlatformOptions = true;
    },

    loadPlatformOptionsSuccess(state, { items }) {
      state.platformOptions = items.map((item) => ({ value: item.platform.toUpperCase(), text: item.platform }));
      state.loadingPlatformOptions = false;
    },

    loadPlatformOptionsFailure(state) {
      state.loadingPlatformOptions = false;
    },
  },

  actions: {
    searchByCruiseNameAndCruiseYear({ state, commit }, { cruiseName, cruiseYear }) {
      commit('loadCruiseSamplesRequest');
      return apiService.get(`provider/sample?cruise=${cruiseName}&cruiseYear=${cruiseYear}&page=${state.cruiseSamplesPage}&itemsPerPage=${state.cruiseSamplesItemsPerPage}`).then(({ data }) => {
        commit('loadCruiseSamplesSuccess', data);
        return data;
      },
      (error) => {
        commit('loadCruiseSamplesFailure');
        throw error;
      });
    },

    loadOptions({ commit }) {
      commit('loadOptionsRequest');
      commit('updateOptions', {});
      const nextOpts = {};

      Promise.all([
        loadAll('/device', ({ deviceCode, device }) => ({ value: deviceCode, text: device })).then(sortOptions).then((options) => { nextOpts.deviceCode = options; return options; }),
        loadAll('/storage-method', ({ storageMethodCode, storageMethod }) => ({ value: storageMethodCode, text: storageMethod })).then(sortOptions).then((options) => { nextOpts.storageMethCode = options; return options; }),
        loadAll('/province', ({ provinceCode, province }) => ({ value: provinceCode, text: province })).then(sortOptions).then((options) => { nextOpts.provinceCode = options; return options; }),
      ]).then(() => {
        commit('updateOptions', nextOpts);
        commit('loadOptionsComplete');
      });
    },

    searchPlatformsByName({ commit }, name) {
      commit('loadPlatformOptionsRequest');
      return apiService.get('/provider/platform', {
        params: {
          platform: name,
          page: 1,
          itemsPerPage: 10,
        },
      }).then(({ data }) => {
        commit('loadPlatformOptionsSuccess', data);
        return data;
      },
      (error) => {
        commit('loadPlatformOptionsFailure');
        throw error;
      });
    },

    loadLegOptions({ commit }, { cruiseName, year }) {
      commit('loadLegOptionsRequest');
      commit('updateLegOptions', []);
      return apiService.get(`/provider/cruise?cruiseNameEquals=${cruiseName}&year=${year}&page=1&itemsPerPage=1`).then((response) => {
        if (response.data.items.length !== 0) {
          commit('updateLegOptions', response.data.items[0].legs);
        }
        commit('loadLegOptionsComplete');
      });
    },

    loadCruiseOptions({ commit }, { platformName, cruiseName }) {
      commit('loadCruiseOptionsRequest');
      return apiService.get('/provider/cruise', {
        params: {
          platformEquals: platformName,
          cruiseNameContains: cruiseName,
          page: 1,
          itemsPerPage: 10,
        },
      }).then(({ data }) => {
        commit('loadCruiseOptionsSuccess', data);
        return data;
      },
      (error) => {
        commit('loadCruiseOptionsFailure');
        throw error;
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
      return apiService.get('/provider/sample', {
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
      return apiService.get(`/provider/sample/${id}`)
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
      const req = id ? () => apiService.put(`/provider/sample/${id}`, provider) : () => apiService.post('/provider/sample', provider);
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
                    commit('providerSampleForm/setTouched', { path, touched: false }, { root: true });
                    commit('providerSampleForm/setError', { path, error: message }, { root: true });
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
      return apiService.delete(`/provider/sample/${id}`)
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
      return apiService.get(`/provider/sample/approval/${id}`)
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
