import { apiService } from '@/api';
import { encodeSlash } from '@/store/modules/urlUtil';

const defaultParams = {
  userNameContains: '',
  userNameEquals: '',
  displayNameContains: '',
  facilityCode: '',
};

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

export default {

  namespaced: true,

  state: {
    items: [],
    searching: false,

    page: 1,
    totalPages: 1,
    totalItems: 0,

    params: { ...defaultParams },

    sortBy: 'userName',
    sortDesc: false,

    item: null,
    loading: false,
    saving: false,
    deleting: false,

    options: {},

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

    saveRequest(state) {
      state.saving = true;
    },
    saveSuccess(state, item) {
      state.saving = false;
      state.item = item;
    },
    saveFailure(state) {
      state.saving = false;
    },

    deleteRequest(state) {
      state.saving = true;
    },
    deleteSuccess(state) {
      state.saving = false;
    },
    deleteFailure(state) {
      state.saving = false;
    },

    firstPage(state) {
      state.page = 1;
      state.totalPages = 1;
      state.totalItems = 0;
    },
    setUserNameContains(state, value) {
      state.params.userNameContains = value;
    },
    setUserNameEquals(state, value) {
      state.params.userNameEquals = value;
    },
    setDisplayNameContains(state, value) {
      state.params.displayNameContains = value;
    },
    setFacilityCode(state, value) {
      console.log('setFacilityCode', value);
      state.params.facilityCode = value;
    },
    setPage(state, page) {
      state.page = page;
    },
    setSortBy(state, sortBy) {
      state.sortBy = sortBy;
    },
    setSortDesc(state, sortDesc) {
      state.sortDesc = sortDesc;
    },
    clearParams(state) {
      state.params = { ...defaultParams };
    },
    searchRequest(state) {
      state.searching = true;
    },
    searchSuccess(state, data) {
      state.searching = false;
      state.items = data.items;
      state.page = data.page;
      state.totalPages = data.totalPages;
      state.totalItems = data.totalItems;
    },
    searchFailure(state) {
      state.searching = false;
    },
    clearAll(state) {
      state.params = { ...defaultParams };
      state.items = [];
      state.searching = false;
      state.page = 1;
      state.totalPages = 1;
      state.totalItems = 0;
    },
    updateOptions(state, options) {
      state.options = options;
    },
  },

  actions: {
    loadOptions({ commit }) {
      commit('updateOptions', {});
      const nextOpts = {};

      Promise.all([
        loadAll('/facility', ({ facility, facilityCode, id }) => ({ value: { id, facilityCode }, text: `${facilityCode} - ${facility}` })).then(sortOptions).then((options) => { nextOpts.facilityCode = options; return options; }),
      ]).then(() => {
        commit('updateOptions', nextOpts);
      });
    },

    loadSearchOptions({ commit }) {
      commit('updateOptions', {});
      const nextOpts = {};

      Promise.all([
        loadAll('/facility', ({ facility, facilityCode }) => ({ value: facilityCode, text: `${facilityCode} - ${facility}` })).then(sortOptions).then((options) => { nextOpts.facilityCode = options; return options; }),
      ]).then(() => {
        commit('updateOptions', nextOpts);
      });
    },

    // eslint-disable-next-line no-unused-vars
    changePage({ dispatch, commit }, page) {
      commit('setPage', page);
      return dispatch('searchPage');
    },
    search({ commit, dispatch }) {
      commit('firstPage');
      return dispatch('searchPage');
    },
    reset({ commit, dispatch }) {
      commit('firstPage');
      commit('clearParams');
      return dispatch('searchPage');
    },
    searchPage({ commit, state }) {
      commit('searchRequest');
      const keys = Object.keys(state.params);
      const params = {};
      keys.forEach((key) => {
        const value = state.params[key];
        if (value) {
          params[key] = value;
        }
      });
      return apiService.get('/user', {
        params:
          {
            ...params,
            page: state.page,
            order: `${state.sortBy}:${state.sortDesc ? 'desc' : 'asc'}`,
          },
      }).then(
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
    load({ commit }, userName) {
      commit('loadRequest');
      return apiService.get(`/user/${encodeURIComponent(encodeSlash(userName))}`)
        .then(
          (response) => {
            commit('loadSuccess', response.data);
            return response.data;
          },
          (error) => {
            commit('loadFailure');
            throw error;
          },
        );
    },
    save({ commit }, { user, userName }) {
      commit('saveRequest');
      const req = userName ? () => apiService.put(`/user/${encodeURIComponent(encodeSlash(userName))}`, user) : () => apiService.post('/user', user);
      return req()
        .then(
          (response) => {
            commit('saveSuccess', response.data);
            return response.data;
          },
          (error) => {
            commit('saveFailure');
            const { response } = error;
            if (response) {
              const { data } = response;
              if (data) {
                const { formErrors } = data;
                if (formErrors) {
                  const paths = Object.keys(formErrors);
                  paths.forEach((path) => {
                    const message = formErrors[path].join(', ');
                    commit('userForm/setTouched', { path, touched: false }, { root: true });
                    commit('userForm/setError', { path, error: message }, { root: true });
                  });
                }
              }
            }
            throw error;
          },
        );
    },
    delete({ commit }, userName) {
      commit('deleteRequest');
      return apiService.delete(`/user/${encodeURIComponent(encodeSlash(userName))}`)
        .then(
          (response) => {
            commit('deleteSuccess', response.data);
            return response.data;
          },
          (error) => {
            commit('deleteFailure');
            throw error;
          },
        );
    },
  },
};
