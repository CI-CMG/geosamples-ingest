import { apiService } from '@/api';
import { BASE_PATH } from '@/basePath';
import { coordinates2WktPolygon } from '@/store/modules/shapeUtil';

const fields = [
  'cruiseContains',
  'sampleContains',
  'facilityCode',
  'platformContains',
  'deviceCode',
  'date',
  'storageMethodCode',
  'piContains',
  'provinceCode',
  'igsn',
  'imlgs',
  'interval',
  'publish',
  'lithCode',
  'textCode',
  'ageCode',
  'rockLithCode',
  'rockMinCode',
  'weathMetaCode',
  'remarkCode',
  'munsellCode',
  'swCoordinate',
  'neCoordinate',
  'approvalState',
];

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

const sortColumns = {
  publish: 'Publish',
  imlgs: 'IMLGS',
  igsn: 'Sample IGSN',
  intervalIgsn: 'Interval IGSN',
  platform: 'Platform',
  beginDate: 'Begin Date',
  facility: 'Facility',
  cruise: 'Cruise',
  sample: 'Sample',
  interval: 'Interval',
};

const parseSortParameters = (sort) => {
  let sorts = [];
  if (sort) {
    sorts = sort.split(',');
  }
  const all = Object.keys(sortColumns);
  const available = [];
  const selected = [];
  sorts.forEach((s) => {
    if (s) {
      const parts = s.split(':');
      const key = parts[0];
      const column = sortColumns[key];
      if (column) {
        selected.push({ key: parts[0], asc: parts[1] === 'asc', column });
        const index = all.indexOf(key);
        if (index > -1) {
          all.splice(index, 1);
        }
      }
    }
  });
  all.forEach((key) => {
    available.push({ key, column: sortColumns[key] });
  });
  return { available, selected };
};

const defaultSortParameters = 'facility:asc,cruise:asc,sample:asc,interval:asc';

export default {

  namespaced: true,

  state: {

    sortParameters: parseSortParameters(defaultSortParameters),

    options: {},

    loadingApproval: false,

    itemsPerPage: 200,

    items: [],
    searching: false,

    page: 1,
    totalPages: 1,
    totalItems: 0,

    searchParameters: null,

    // TODO support imlgs
    sortBy: 'cruise',
    sortDesc: false,

    sampleItem: null,
    sampleLoading: false,
    sampleSaving: false,

    intervalItem: null,
    intervalLoading: false,
    intervalSaving: false,

  },

  mutations: {
    updateOptions(state, options) {
      state.options = options;
    },
    updateSearchParameters(state, searchParameters) {
      const params = {};
      fields.forEach((f) => {
        const value = searchParameters[f];
        if (value != null) {
          if (f === 'publish') {
            if (value) {
              params.publish = value;
            }
          } else {
            params[f] = Array.isArray(value) ? value : [value];
          }
        }
      });
      state.searchParameters = params;
    },

    updateSortParameters(state, query) {
      const { sort } = query;
      state.sortParameters = parseSortParameters(sort);
    },

    loadSampleRequest(state) {
      state.sampleLoading = true;
    },
    loadSampleSuccess(state, item) {
      state.sampleLoading = false;
      state.sampleItem = item;
    },
    loadSampleFailure(state) {
      state.sampleLoading = false;
    },
    saveSampleRequest(state) {
      state.sampleSaving = true;
    },
    saveSampleSuccess(state, item) {
      state.sampleSaving = false;
      state.sampleItem = item;
    },
    saveSampleFailure(state) {
      state.sampleSaving = false;
    },

    deleteSampleRequest(state) {
      state.sampleSaving = true;
    },
    deleteSampleSuccess(state) {
      state.sampleSaving = false;
    },
    deleteSampleFailure(state) {
      state.sampleSaving = false;
    },

    loadIntervalRequest(state) {
      state.intervalLoading = true;
    },
    loadIntervalSuccess(state, item) {
      state.intervalLoading = false;
      state.intervalItem = item;
    },
    loadIntervalFailure(state) {
      state.intervalLoading = false;
    },
    saveIntervalRequest(state) {
      state.intervalSaving = true;
    },
    saveIntervalSuccess(state, item) {
      state.intervalSaving = false;
      state.intervalItem = item;
    },
    saveIntervalFailure(state) {
      state.intervalSaving = false;
    },

    deleteIntervalRequest(state) {
      state.intervalSaving = true;
    },
    deleteIntervalSuccess(state) {
      state.intervalSaving = false;
    },
    deleteIntervalFailure(state) {
      state.intervalSaving = false;
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

    firstPage(state) {
      state.page = 1;
      state.totalPages = 1;
      state.totalItems = 0;
    },

    setPage(state, page) {
      if (page == null || page < 1) {
        state.page = 1;
      } else {
        state.page = page;
      }
    },
    setSort(state, sort) {
      if (sort) {
        const parts = sort.split(':');
        state.sortBy = parts[0];
        state.sortDesc = parts[1] === 'desc';
      } else {
        state.sortBy = 'cruise';
        state.sortDesc = true;
      }
    },
    setSortBy(state, sortBy) {
      state.sortBy = sortBy;
    },
    setSortDesc(state, sortDesc) {
      state.sortDesc = sortDesc;
    },
    searchRequest(state) {
      state.searching = true;
    },
    searchSuccess(state, data) {
      state.searching = false;
      state.items = data.items.map(
        (item) => (
          {
            ...item,
            selected: false,
            lastUpdate: item.lastUpdate ? new Date(item.lastUpdate * 1000).toISOString().slice(0, 10).replace(/-/g, '') : null,
          }
        ),
      );
      state.page = data.page;
      state.totalPages = data.totalPages;
      state.totalItems = data.totalItems;
    },
    searchFailure(state) {
      state.searching = false;
    },
    acceptRequest(state) {
      state.searching = true;
    },
    acceptSuccess(state) {
      state.searching = false;
    },
    acceptFailure(state) {
      state.searching = false;
    },
    clearAll(state) {
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
    loadApproval({ commit }, id) {
      commit('loadApprovalRequest');
      return apiService.get(`/interval/approval/${id}`)
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

    saveApproval({ commit }, { id, approval }) {
      commit('loadApprovalRequest');
      return apiService.patch(`/interval/review/${id}`, approval)
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

    loadOptions({ commit }) {
      commit('updateOptions', {});
      const nextOpts = {};

      Promise.all([
        loadAll('/age', ({ age, ageCode }) => ({ value: ageCode, text: `${ageCode} - ${age}` })).then(sortOptions).then((options) => { nextOpts.ageCode = options; return options; }),
        loadAll('/device', ({ device, deviceCode }) => ({ value: deviceCode, text: `${deviceCode} - ${device}` })).then(sortOptions).then((options) => { nextOpts.deviceCode = options; return options; }),
        loadAll('/facility', ({ facility, facilityCode }) => ({ value: facilityCode, text: `${facilityCode} - ${facility}` })).then(sortOptions).then((options) => { nextOpts.facilityCode = options; return options; }),
        loadAll('/lithology', ({ lithology, lithologyCode }) => ({ value: lithologyCode, text: `${lithologyCode} - ${lithology}` })).then(sortOptions).then((options) => { nextOpts.lithCode = options; return options; }),
        loadAll('/munsell', ({ munsellCode }) => ({ value: munsellCode, text: munsellCode })).then(sortOptions).then((options) => { nextOpts.munsellCode = options; return options; }),
        loadAll('/province', ({ province, provinceCode }) => ({ value: provinceCode, text: `${provinceCode} - ${province}` })).then(sortOptions).then((options) => { nextOpts.provinceCode = options; return options; }),
        loadAll('/remark', ({ remark, remarkCode }) => ({ value: remarkCode, text: `${remarkCode} - ${remark}` })).then(sortOptions).then((options) => { nextOpts.remarkCode = options; return options; }),
        loadAll('/rock-lithology', ({ rockLithology, rockLithologyCode }) => ({ value: rockLithologyCode, text: `${rockLithologyCode} - ${rockLithology}` })).then(sortOptions).then((options) => { nextOpts.rockLithCode = options; return options; }),
        loadAll('/rock-mineral', ({ rockMineral, rockMineralCode }) => ({ value: rockMineralCode, text: `${rockMineralCode} - ${rockMineral}` })).then(sortOptions).then((options) => { nextOpts.rockMinCode = options; return options; }),
        loadAll('/storage-method', ({ storageMethod, storageMethodCode }) => ({ value: storageMethodCode, text: `${storageMethodCode} - ${storageMethod}` })).then(sortOptions).then((options) => { nextOpts.storageMethodCode = options; return options; }),
        loadAll('/texture', ({ texture, textureCode }) => ({ value: textureCode, text: `${textureCode} - ${texture}` })).then(sortOptions).then((options) => { nextOpts.textCode = options; return options; }),
        loadAll('/weathering', ({ weathering, weatheringCode }) => ({ value: weatheringCode, text: `${weatheringCode} - ${weathering}` })).then(sortOptions).then((options) => { nextOpts.weathMetaCode = options; return options; }),
      ]).then(() => {
        commit('updateOptions', nextOpts);
      });
    },
    accept({ commit, state }, { publish }) {
      commit('acceptRequest');
      const items = state.items.filter((i) => i.selected).map((i) => ({ imlgs: i.imlgs, interval: i.interval, publish }));
      const body = { items };
      return apiService.patch('/sample-interval', body)
        .then(
          (response) => {
            commit('acceptSuccess', response.data);
            return response.data;
          },
          (error) => {
            commit('acceptFailure');
            throw error;
          },
        );
    },
    delete({ commit, state }) {
      commit('acceptRequest');
      const del = state.items.filter((i) => i.selected).map((i) => ({ imlgs: i.imlgs, interval: i.interval }));
      const body = { delete: del };
      return apiService.patch('/sample-interval', body)
        .then(
          (response) => {
            commit('acceptSuccess', response.data);
            return response.data;
          },
          (error) => {
            commit('acceptFailure');
            throw error;
          },
        );
    },
    searchPage({ commit, state }) {
      commit('searchRequest');

      const params = new URLSearchParams();
      params.append('page', state.page);
      params.append('order', `${state.sortBy}:${state.sortDesc ? 'desc' : 'asc'}`);
      params.append('itemsPerPage', state.itemsPerPage);

      if (state.searchParameters) {
        if (state.searchParameters.publish != null) {
          params.append('publish', state.searchParameters.publish);
        }
        fields.forEach((f) => {
          if (f !== 'swCoordinate' && f !== 'neCoordinate') {
            const field = state.searchParameters[f];
            if (field) {
              if (Array.isArray(field)) {
                field.forEach((v) => {
                  // noinspection JSDeepBugsSwappedArgs
                  params.append(f, v);
                });
              } else {
                // noinspection JSDeepBugsSwappedArgs
                params.append(f, field);
              }
            }
          }
        });
        if (state.searchParameters.swCoordinate && state.searchParameters.neCoordinate) {
          params.append(
            'area',
            coordinates2WktPolygon(
              state.searchParameters.swCoordinate, state.searchParameters.neCoordinate,
            ),
          );
        }
      }

      return apiService.get('/sample-interval', { params })
        .then(
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

    downloadSamplesIntervals({ state }, token) {
      const orders = [];
      state.sortParameters.selected.forEach(({ key, asc }) => {
        orders.push(`${key}:${asc ? 'asc' : 'desc'}`);
      });

      const params = {
        page: state.page,
        itemsPerPage: state.itemsPerPage,
        order: orders,
        ...state.searchParameters,
      };

      let parameterString = [];
      // eslint-disable-next-line no-restricted-syntax
      for (const parameter of Object.keys(params)) {
        if (params[parameter] !== '' && params[parameter] !== undefined && params[parameter] !== null) {
          if (Array.isArray(params[parameter])) {
            params[parameter] = params[parameter].join();
          }
          parameterString.push(`${encodeURIComponent(parameter)}=${encodeURIComponent(params[parameter])}`);
        }
      }
      parameterString = parameterString.join('&');
      const link = document.createElement('a');
      link.href = `${BASE_PATH}/api/v1/sample-interval/export?access_token=${token}&${parameterString}`;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    },

    loadSample({ commit }, id) {
      commit('loadSampleRequest');
      return apiService.get(`/sample/${encodeURIComponent(id)}`)
        .then(
          (response) => {
            commit('loadSampleSuccess', response.data);
            return response.data;
          },
          (error) => {
            commit('loadSampleFailure');
            throw error;
          },
        );
    },
    saveSample({ commit }, { provider, id }) {
      commit('saveSampleRequest');
      const req = id ? () => apiService.put(`/sample/${encodeURIComponent(id)}`, provider) : () => apiService.post('/sample', provider);
      return req()
        .then(
          (response) => {
            commit('saveSampleSuccess', response.data);
            return response.data;
          },
          (error) => {
            commit('saveSampleFailure');
            const { response } = error;
            if (response) {
              const { data } = response;
              if (data) {
                const { formErrors } = data;
                if (formErrors) {
                  const paths = Object.keys(formErrors);
                  paths.forEach((path) => {
                    const message = formErrors[path].join(', ');
                    commit('sampleForm/setTouched', { path, touched: false }, { root: true });
                    commit('sampleForm/setError', { path, error: message }, { root: true });
                  });
                }
              }
            }
            throw error;
          },
        );
    },
    deleteSample({ commit }, id) {
      commit('deleteSampleRequest');
      return apiService.delete(`/sample/${encodeURIComponent(id)}`)
        .then(
          (response) => {
            commit('deleteSampleSuccess', response.data);
            return response.data;
          },
          (error) => {
            commit('deleteSampleFailure');
            throw error;
          },
        );
    },

    loadInterval({ commit }, { id }) {
      commit('loadSampleRequest');
      return apiService.get(`/interval/${encodeURIComponent(id)}`)
        .then(
          (response) => {
            commit('loadSampleSuccess', response.data);
            return response.data;
          },
          (error) => {
            commit('loadSampleFailure');
            throw error;
          },
        );
    },
    saveInterval({ commit }, { provider, id }) {
      commit('saveIntervalRequest');
      const req = id ? () => apiService.put(`/interval/${encodeURIComponent(id)}`, provider) : () => apiService.post('/interval', provider);
      return req()
        .then(
          (response) => {
            commit('saveIntervalSuccess', response.data);
            return response.data;
          },
          (error) => {
            commit('saveIntervalFailure');
            const { response } = error;
            if (response) {
              const { data } = response;
              if (data) {
                const { formErrors } = data;
                if (formErrors) {
                  const paths = Object.keys(formErrors);
                  paths.forEach((path) => {
                    const message = formErrors[path].join(', ');
                    commit('intervalForm/setTouched', { path, touched: false }, { root: true });
                    commit('intervalForm/setError', { path, error: message }, { root: true });
                  });
                }
              }
            }
            throw error;
          },
        );
    },
    deleteInterval({ commit }, { id }) {
      commit('deleteIntervalRequest');
      return apiService.delete(`/interval/${encodeURIComponent(id)}`)
        .then(
          (response) => {
            commit('deleteIntervalSuccess', response.data);
            return response.data;
          },
          (error) => {
            commit('deleteIntervalFailure');
            throw error;
          },
        );
    },

    // load({ commit }, id) {
    //   commit('loadRequest');
    //   return apiService.get(`/texture/${encodeURIComponent(id)}`)
    //   .then(
    //     (response) => {
    //       commit('loadSuccess', response.data);
    //       return response.data;
    //     },
    //     (error) => {
    //       commit('loadFailure');
    //       throw error;
    //     },
    //   );
    // },
    // save({ commit }, { provider, id }) {
    //   commit('saveRequest');
    //   const req = id ? () => apiService.put(`/texture/${encodeURIComponent(id)}`, provider) : () => apiService.post('/texture', provider);
    //   return req()
    //   .then(
    //     (response) => {
    //       commit('saveSuccess', response.data);
    //       return response.data;
    //     },
    //     (error) => {
    //       commit('saveFailure');
    //       const { response } = error;
    //       if (response) {
    //         const { data } = response;
    //         if (data) {
    //           const { formErrors } = data;
    //           if (formErrors) {
    //             const paths = Object.keys(formErrors);
    //             paths.forEach((path) => {
    //               const message = formErrors[path].join(', ');
    //               commit('textureForm/setTouched', { path, touched: false }, { root: true });
    //               commit('textureForm/setError', { path, error: message }, { root: true });
    //             });
    //           }
    //         }
    //       }
    //       throw error;
    //     },
    //   );
    // },
    // delete({ commit }, id) {
    //   commit('deleteRequest');
    //   return apiService.delete(`/texture/${encodeURIComponent(id)}`)
    //   .then(
    //     (response) => {
    //       commit('deleteSuccess', response.data);
    //       return response.data;
    //     },
    //     (error) => {
    //       commit('deleteFailure');
    //       throw error;
    //     },
    //   );
    // },
  },
};
