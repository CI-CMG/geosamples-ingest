import {
  defineState,
  initializeState,
  setValue,
  addToArray,
  deleteFromArray,
  getValue,
  isTouched,
  setTouched,
  isDirty,
  getError,
  setError,
  touchAll,
  clearAllErrors,
  stateToObject,
  reset,
  isFormDirty,
  isFormUntouchedErrors,
} from '@ci-cmg/form-vuex';

const model = {
  cruiseContains: ['string'],
  sampleContains: ['string'],
  facilityCode: ['string'],
  platformContains: ['string'],
  deviceCode: ['string'],
  date: ['string'],
  storageMethodCode: ['string'],
  piContains: ['string'],
  provinceCode: ['string'],
  igsn: ['string'],
  imlgs: ['string'],
  interval: ['int'],
  publish: 'string',
  lithCode: ['string'],
  textCode: ['string'],
  ageCode: ['string'],
  rockLithCode: ['string'],
  rockMinCode: ['string'],
  weathMetaCode: ['string'],
  remarkCode: ['string'],
  munsellCode: ['string'],
  approvalState: ['string'],
  area: 'string',
  swCoordinate: 'string',
  neCoordinate: 'string',
};

export default {
  namespaced: true,

  state: defineState(),

  getters: {
    getValue: (state) => (path) => getValue(state, path, model),
    isDirty: (state) => (path) => isDirty(state, path),
    isTouched: (state) => (path) => isTouched(state, path),
    getError: (state) => (path) => getError(state, path),
    formDirty: (state) => isFormDirty(state, model),
    object: (state) => stateToObject(state, model),
    formHasUntouchedErrors: (state) => isFormUntouchedErrors(state, model),
  },

  mutations: {
    initialize: (state, initial) => initializeState(state, model, initial),
    setValue: (state, { path, value }) => setValue(state, path, value),
    addToArray: (state, { path, value }) => addToArray(state, model, path, value),
    deleteFromArray: (state, path) => deleteFromArray(state, path),
    setTouched: (state, { path, touched }) => setTouched(state, path, touched),
    setError: (state, { path, error }) => setError(state, path, error),
    touchAll: (state, touched) => touchAll(state, touched),
    clearAllErrors: (state) => clearAllErrors(state),
    reset: (state) => reset(state, model),
  },

  actions: {
    submit({ commit, getters }) {
      const formJson = getters.object;
      commit('touchAll', false);
      commit('clearAllErrors');
      return formJson;
    },
    reset({ commit }) {
      commit('touchAll', false);
      commit('clearAllErrors');
      commit('reset');
    },
  },
};
