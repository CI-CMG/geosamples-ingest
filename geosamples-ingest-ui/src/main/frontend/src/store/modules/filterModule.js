import { geometryFilterService } from '@/api';

export default {

  namespaced: true,

  state: {
    file: null,
    uploading: false,
    tilesGenerating: false,
    checker: null,
    visibleLayers: {
      'Territorial Seas': true,
      EEZ: true,
    },
    layerChooserCollapsed: true,
    areas: [],
    textSearch: '',
    excludeSearch: '',
    loadingAreas: false,
    areaPage: 1,
    areaTotalPages: 1,
    areaTotalItems: 0,
    change: [],
    savingChanges: false,
    tabIndex: 0,
  },

  mutations: {
    changeTab(state, tabIndex) {
      state.tabIndex = tabIndex;
    },
    refreshChange(state) {
      state.change = [...state.change];
    },
    addChange(state, changeObj) {
      if (!state.change.filter((obj) => obj.id === changeObj.id && obj.type === changeObj.type).length) {
        state.change = [...state.change, changeObj];
      }
    },
    removeChange(state, { id, type }) {
      let i = -1;
      let found = -1;
      for (i = 0; i < state.change.length; i += 1) {
        const obj = state.change[i];
        if (obj.id === id && obj.type === type) {
          found = i;
          break;
        }
      }
      if (found >= 0) {
        const next = [...state.change];
        next.splice(found, 1);
        state.change = next;
      }
    },
    clearChange(state) {
      state.change = [];
    },
    setTextSearch(state, textSearch) {
      state.textSearch = textSearch;
    },
    setExcludeSearch(state, excludeSearch) {
      state.excludeSearch = excludeSearch;
    },
    changeRequest(state) {
      state.savingChanges = true;
    },
    changeSuccess(state) {
      state.savingChanges = false;
      state.change = [];
    },
    changeFailure(state) {
      state.savingChanges = false;
    },
    areaRequest(state, { page }) {
      state.areaPage = page;
      state.areaTotalPages = 1;
      state.areaTotalItems = 0;
      state.loadingAreas = true;
      state.areas = [];
    },
    areaSuccess(state, {
      page, totalPages, items, totalItems,
    }) {
      state.areaPage = page;
      state.areaTotalPages = totalPages;
      state.areaTotalItems = totalItems;
      state.loadingAreas = false;
      state.areas = items;
    },
    areaFailure(state) {
      state.loadingAreas = false;
    },
    toggleLayerChooserCollapsed(state) {
      state.layerChooserCollapsed = !state.layerChooserCollapsed;
    },
    toggleLayer(state, name) {
      state.visibleLayers[name] = !state.visibleLayers[name];
    },
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
    changeAreas({ commit, state }) {
      if (state.change.length) {
        commit('changeRequest');
        const body = {
          changes: state.change.map(({ id, type, exclude }) => ({ id, type, exclude })),
        };
        return geometryFilterService.put('/geometry/exclude', body)
          .then(
            (response) => {
              commit('changeSuccess', response.data);
              setTimeout(() => { commit('refreshChange'); }, 2000);
              return response.data;
            },
            (error) => {
              commit('changeFailure');
              throw error;
            },
          );
      }
      return null;
    },
    searchAreas({ commit, state }, { page }) {
      commit('areaRequest', { page });
      const params = new URLSearchParams();
      params.append('page', page);
      const text = state.textSearch;
      const exclude = state.excludeSearch;
      if (text) {
        params.append('text', text);
      }
      if (exclude) {
        params.append('exclude', exclude);
      }
      return geometryFilterService.get('/geometry/area-search', { params })
        .then(
          (response) => {
            commit('areaSuccess', response.data);
            return response.data;
          },
          (error) => {
            commit('areaFailure');
            throw error;
          },
        );
    },

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
