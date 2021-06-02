import { providerService } from '@/api';

export default {

  namespaced: true,

  state: {
    providers: [],
    loading: false,
    fetchedProvider: true,
    provider: {
      website: '',
    },
  },

  getters: {
    fetchedProvider: (state) => state.fetchedProvider,
    website(state) {
      return state.provider.website;
    },
  },

  mutations: {
    providersRequest(state) {
      state.loading = true;
      state.providers = [];
    },
    providersSuccess(state, { items }) {
      state.loading = false;
      state.providers = items;
    },
    providersFailure(state) {
      state.loading = false;
    },

    website(state, website) {
      state.provider.website = website;
    },
    providerRequest(state) {
      state.fetchedProvider = false;
      state.provider = {};
    },
    providerSuccess(state, provider) {
      state.fetchedProvider = true;
      state.provider = provider;
    },
    providerFailure(state) {
      state.fetchedProvider = true;
      state.provider = {};
    },
    saveProviderRequest(state) {
      state.fetchedProvider = false;
    },
    saveProviderSuccess(state, provider) {
      state.fetchedProvider = true;
      state.provider = provider;
    },
    saveProviderFailure(state) {
      state.fetchedProvider = true;
    },
  },

  actions: {
    loadProviders({ commit }) {
      commit('providersRequest');
      return providerService.get('/csb/provider')
        .then(
          (response) => {
            commit('providersSuccess', response.data);
            return response.data;
          },
          (error) => {
            commit('providersFailure');
            throw error;
          },
        );
    },

    saveProvider({ commit }, { provider, id }) {
      commit('saveProviderRequest');
      const req = id ? providerService.put(`/csb/provider/${id}`, provider) : providerService.post('/csb/provider', provider);
      return req.then(
        (response) => {
          commit('saveProviderSuccess', response.data);
          return response.data;
        },
        (error) => {
          commit('saveProviderFailure');
          const { response } = error;
          if (response) {
            const { data } = response;
            if (data) {
              const { formErrors } = data;
              if (formErrors) {
                const paths = Object.keys(formErrors);
                paths.forEach((path) => {
                  const message = formErrors[path].join(', ');
                  commit('providerForm/setTouched', { path, touched: false }, { root: true });
                  commit('providerForm/setError', { path, error: message }, { root: true });
                });
              }
            }
          }
          throw error;
        },
      );
    },

    loadProvider({ commit }, id) {
      commit('providerRequest');
      return providerService.get(`/csb/provider/${id}`)
        .then(
          (response) => {
            commit('providerSuccess', response.data);
            return response.data;
          },
          (error) => {
            commit('providerFailure');
            throw error;
          },
        );
    },

  },
};
