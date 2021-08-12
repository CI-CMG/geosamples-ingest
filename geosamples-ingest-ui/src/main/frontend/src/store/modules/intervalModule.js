import { apiService } from '@/api';

export default {

  namespaced: true,

  state: {
    itemsPerPage: 200,

    items: [],
    searching: false,

    page: 1,
    totalPages: 1,
    totalItems: 0,

    cruise: '',
    facilityCode: '',
    platform: '',

    // TODO support imlgs
    sortBy: 'cruise',
    sortDesc: false,

    // item: null,
    // loading: false,
    // saving: false,
    // deleting: false,

    sampleItem: null,
    sampleLoading: false,
    sampleSaving: false,

    intervalItem: null,
    intervalLoading: false,
    intervalSaving: false,

  },

  mutations: {
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

    // saveRequest(state) {
    //   state.saving = true;
    // },
    // saveSuccess(state, item) {
    //   state.saving = false;
    //   state.item = item;
    // },
    // saveFailure(state) {
    //   state.saving = false;
    // },
    //
    // deleteRequest(state) {
    //   state.saving = true;
    // },
    // deleteSuccess(state) {
    //   state.saving = false;
    // },
    // deleteFailure(state) {
    //   state.saving = false;
    // },

    firstPage(state) {
      state.page = 1;
      state.totalPages = 1;
      state.totalItems = 0;
    },
    setCruise(state, cruise) {
      state.cruise = cruise == null ? '' : cruise;
    },
    setFacilityCode(state, facilityCode) {
      state.facilityCode = facilityCode == null ? '' : facilityCode;
    },
    setPlatform(state, platform) {
      if (platform == null) {
        state.platform = '';
      } else {
        state.platform = platform;
      }
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
    clearParams(state) {
      state.cruise = '';
      state.facilityCode = '';
      state.platform = '';
    },
    searchRequest(state) {
      state.searching = true;
    },
    searchSuccess(state, data) {
      state.searching = false;
      state.items = data.items.map((item) => ({ ...item, selected: false }));
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
      state.cruise = '';
      state.facilityCode = '';
      state.platform = '';
      state.items = [];
      state.searching = false;
      state.page = 1;
      state.totalPages = 1;
      state.totalItems = 0;
    },
  },

  actions: {
    // changePage({ dispatch, commit }, page) {
    //   commit('setPage', page);
    //   return dispatch('searchPage');
    // },
    // search({ commit, dispatch }) {
    //   commit('firstPage');
    //   return dispatch('searchPage');
    // },
    // reset({ commit, dispatch }) {
    //   commit('firstPage');
    //   commit('clearParams');
    //   return dispatch('searchPage');
    // },
    accept({ commit, state }) {
      commit('acceptRequest');
      const items = state.items.filter((i) => i.selected).map((i) => ({ imlgs: i.imlgs, interval: i.interval, publish: true }));
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
    searchPage({ commit, state }) {
      commit('searchRequest');
      const params = {
        page: state.page,
        order: `${state.sortBy}:${state.sortDesc ? 'desc' : 'asc'}`,
        itemsPerPage: state.itemsPerPage,
      };
      if (state.platform) {
        params.platform = state.platform;
      }
      if (state.facilityCode) {
        params.facilityCode = state.facilityCode;
      }
      if (state.cruise) {
        params.cruise = state.cruise;
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

    loadInterval({ commit }, { id, imlgs }) {
      commit('loadSampleRequest');
      return apiService.get(`/interval/${encodeURIComponent(imlgs)}/${encodeURIComponent(id)}`)
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
    saveInterval({ commit }, { provider, id, imlgs }) {
      commit('saveIntervalRequest');
      const req = id ? () => apiService.put(`/interval/${encodeURIComponent(imlgs)}/${encodeURIComponent(id)}`, provider) : () => apiService.post('/interval', provider);
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
    deleteInterval({ commit }, { id, imlgs }) {
      commit('deleteIntervalRequest');
      return apiService.delete(`/interval/${encodeURIComponent(imlgs)}/${encodeURIComponent(id)}`)
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
