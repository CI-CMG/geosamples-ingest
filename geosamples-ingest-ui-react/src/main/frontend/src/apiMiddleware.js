import store from '@/store/store';
import { logout } from '@/sessionMonster';
import { apiService } from './api';

const onSuccess = (response) => response;
const onError = (error, router) => {
  if (error.response) {
    let msg = ['An Unknown Error Occurred'];
    if (error.response.data && error.response.data.flashErrors) {
      msg = error.response.data.flashErrors;
    }
    store.commit('app/addErrors', msg);
    if (error.response.status === 401 || error.response.status === 403) {
      logout(router, store);
    }
  }
  throw error;
};

const addToken = (config) => {
  const token = store.state.userAuth.token;
  if (token) {
    config.headers = { Authorization: `Bearer ${token}` };
  }
  return config;
};

const intercept = (service, router) => {
  service.interceptors.request.use(addToken);
  service.interceptors.response.use(onSuccess, (error) => onError(error, router));
};

// eslint-disable-next-line no-unused-vars
export const register = (router) => {
  intercept(apiService, router);
};
