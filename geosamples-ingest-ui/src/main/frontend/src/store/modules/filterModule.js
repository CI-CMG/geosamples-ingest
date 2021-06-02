import { geometryFilterService } from '@/api';

export default {

  namespaced: true,

  state: {
    file: null,
    uploading: false,
    tilesGenerating: false,
    checker: null,
  },

  mutations: {
    uploadRequest(state) {
      state.uploading = true;
    },
    uploadSuccess(state) {
      state.uploading = false;
      state.file = null;
    },
    uploadFailure(state) {
      state.uploading = false;
    },
    setFile(state, file) {
      state.file = file;
    },
    setChecker(state, checker) {
      state.checker = checker;
    },
    setTilesGenerating(state, generating) {
      if (state.tilesGenerating !== generating) {
        state.tilesGenerating = generating;
      }
    },
  },

  actions: {
    startGenChecker({ commit, dispatch }) {
      commit('setChecker', setInterval(() => dispatch('checkTilesGenerating'), 5000));
    },
    stopGenChecker({ commit, state }) {
      if (state.checker) {
        clearInterval(state.checker);
        commit('setChecker', null);
      }
    },
    generateTiles({ dispatch }) {
      geometryFilterService.post('/geometry/mvtgen', {}).then(() => dispatch('checkTilesGenerating'));
    },
    checkTilesGenerating({ commit }) {
      geometryFilterService.get('/geometry/mvtgen')
        .then(
          (response) => {
            commit('setTilesGenerating', response.data.status === 'GENERATING');
          },
        );
    },
    uploadShapeFile({ commit, state, dispatch }) {
      commit('uploadRequest');

      const formData = new FormData();
      formData.append('file', state.file);

      const config = {
        headers: {
          'content-type': 'multipart/form-data',
        },
      };

      return geometryFilterService.post('/geometry/shapefile', formData, config)
        .then(
          (response) => {
            commit('uploadSuccess');
            dispatch('checkTilesGenerating');
            return response.data;
          },
          (error) => {
            commit('uploadFailure');
            throw error;
          },
        );
    },

  },
};
