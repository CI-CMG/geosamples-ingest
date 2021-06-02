import { auditService } from '@/api';

export default {

  namespaced: true,

  state: {
    audit: [],
    loading: false,
  },

  mutations: {
    auditRequest(state) {
      state.loading = true;
      state.audit = [];
    },
    auditSuccess(state, { items }) {
      state.loading = false;
      state.audit = items;
    },
    auditFailure(state) {
      state.loading = false;
    },
  },

  actions: {
    loadAudit({ commit }) {
      commit('auditRequest');
      return auditService.get('/csb/audit')
        .then(
          (response) => {
            commit('auditSuccess', response.data);
            return response.data;
          },
          (error) => {
            commit('auditFailure');
            throw error;
          },
        );
    },

  },
};
