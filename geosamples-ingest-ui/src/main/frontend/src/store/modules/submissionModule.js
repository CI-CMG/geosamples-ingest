import { apiService } from '@/api';

export default {

  namespaced: true,

  state: {
    file: null,
    uploading: false,
    errorHeaders: [],
    dataRows: [],
  },

  mutations: {
    setFile(state, file) {
      state.file = file;
    },
    setErrorHeaders(state, errorData) {
      const errors = Object.keys(errorData);
      for (let i = 0; i < errors.length; i++) {
        let error = errors[i].split('.');
        if (error.length === 2) {
          error = error[1].trim();
          if (!state.errorHeaders.includes(error)) {
            state.errorHeaders.push(error);
          }
        }
      }
    },
    clearErrorHeaders(state) {
      state.errorHeaders = [];
    },
    setDataRows(state, dataRows) {
      state.dataRows = dataRows;
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
  },

  actions: {
    uploadFile({ commit, state }, { router }) {
      commit('uploadRequest');

      const formData = new FormData();
      formData.append('file', state.file);

      const config = {
        headers: {
          'content-type': 'multipart/form-data',
        },
      };

      return apiService.post('/curator-data/upload', formData, config)
        .then(
          (response) => {
            commit('uploadSuccess');
            const data = response.data;
            const { facilityCodes } = data;
            if (facilityCodes.length > 0) {
              // TODO support more specific query
              router.push({ name: 'IntervalList', query: { facilityCode: facilityCodes[0] } });
            }
            return response.data;
          },
          (error) => {
            const { response } = error;
            if (response) {
              const { data } = response;
              if (data) {
                const { formErrors, additionalData } = data;
                commit('setErrorHeaders', formErrors);
                if (additionalData && additionalData.rows) {
                  commit('submissionForm/initialize', additionalData, { root: true });
                } else {
                  commit('submissionForm/initialize', null, { root: true });
                }
                if (formErrors) {
                  const paths = Object.keys(formErrors);
                  paths.forEach((path) => {
                    const message = formErrors[path].join(', ');
                    commit('submissionForm/setTouched', { path, touched: false }, { root: true });
                    commit('submissionForm/setError', { path, error: message }, { root: true });
                  });
                }
              }
            }
            commit('uploadFailure');
            router.push({ name: 'ErrorSubmission' });
            throw error;
          },
        );
    },

  },
};
