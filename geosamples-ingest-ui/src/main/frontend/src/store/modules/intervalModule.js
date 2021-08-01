import { apiService } from '@/api';

export default {

  namespaced: true,

  state: {
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
    searchPage({ commit, state }) {
      commit('searchRequest');
      const params = {
        page: state.page,
        order: `${state.sortBy}:${state.sortDesc ? 'desc' : 'asc'}`,
        itemsPerPage: 1000,
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
