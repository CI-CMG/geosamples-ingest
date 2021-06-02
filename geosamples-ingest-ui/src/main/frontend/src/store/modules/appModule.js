import genId from '@/components/idGenerator';

const createError = (msg) => ({
  msg,
  countDown: 20,
  id: genId(),
});

export default {

  namespaced: true,

  state: {
    errors: [],
  },

  getters: {
    errors(state) {
      return state.errors;
    },
    countDown(state) {
      return state.countDown;
    },
  },

  mutations: {
    addErrors(state, errors) {
      const newErrors = errors.map(createError);
      state.errors = [...state.errors, ...newErrors];
    },
    countDown(state, { countDown, id }) {
      let error;
      let i;
      for (i = 0; i < state.errors.length; i++) {
        const e = state.errors[i];
        if (e.id === id) {
          error = e;
          break;
        }
      }
      if (error) {
        error.countDown = countDown;
        if (!countDown) {
          state.errors.splice(i, 1);
          const newErrors = [...state.errors];
          state.errors = newErrors;
        }
      }
    },
  },

  actions: {
    addErrors({ commit }, errors) {
      commit('addErrors', errors);
    },
    countDownChanged({ commit }, { countDown, id }) {
      commit('countDown', { countDown, id });
    },
    dismissAlert({ dispatch }, id) {
      dispatch('countDownChanged', { countDown: 0, id });
    },
  },
};
