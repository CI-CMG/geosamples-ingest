import { apiService } from '@/api';

export default {

  namespaced: true,

  state: {
    platforms: [],
    searching: false,
    params: {},
    page: 1,
    totalPages: 1,
    totalItems: 0,
  },

  mutations: {
    firstPage(state) {
      state.page = 1;
      state.totalPages = 1;
      state.totalItems = 0;
    },
    setPage(state, page) {
      state.page = page;
    },
    setParams(state, params) {
      state.params = params;
    },
    searchRequest(state) {
      state.searching = true;
    },
    searchSuccess(state, data) {
      state.searching = false;
      state.platforms = data.items;
      state.page = data.page;
      state.totalPages = data.totalPages;
      state.totalItems = data.totalItems;
    },
    searchFailure(state) {
      state.searching = false;
    },
  },

  actions: {
    changePage({ dispatch, commit }, page) {
      commit('setPage', page);
      return dispatch('searchPage');
    },
    search({ commit, dispatch }) {
      commit('firstPage');
      return dispatch('searchPage');
    },
    searchPage({ commit, state }) {
      commit('searchRequest');
      return apiService.get('/platform', { params: { ...state.params, page: state.page } })
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

    // uploadShapeFile({ commit, state, dispatch }) {
    //   commit('uploadRequest');
    //
    //   const formData = new FormData();
    //   formData.append('file', state.file);
    //
    //   const config = {
    //     headers: {
    //       'content-type': 'multipart/form-data',
    //     },
    //   };
    //
    //   return geometryFilterService.post('/geometry/shapefile', formData, config)
    //   .then(
    //     (response) => {
    //       commit('uploadSuccess');
    //       dispatch('checkTilesGenerating');
    //       return response.data;
    //     },
    //     (error) => {
    //       commit('uploadFailure');
    //       throw error;
    //     },
    //   );
    // },

  },
};
