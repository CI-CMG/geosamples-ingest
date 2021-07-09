import { apiService } from '@/api';

export default {

  namespaced: true,

  state: {
    file: null,
    uploading: false,
    errorData: {},
  },

  mutations: {
    setFile(state, file) {
      state.file = file;
    },
    setErrorData(state, errorData) {
      state.errorData = errorData;
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
    uploadFile({ commit, state, dispatch }, { router }) {
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
            dispatch('checkTilesGenerating');
            return response.data;
          },
          (error) => {
            const { response } = error;
            if (response) {
              const { data } = response;
              if (data) {
                const { formErrors } = data;
                commit('setErrorData', formErrors);
                // if (formErrors) {
                //   const paths = Object.keys(formErrors);
                //   paths.forEach((path) => {
                //     const message = formErrors[path].join(', ');
                //     commit('submissionForm/setTouched', { path, touched: false }, { root: true });
                //     commit('submissionForm/setError', { path, error: message }, { root: true });
                //   });
                // }
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
