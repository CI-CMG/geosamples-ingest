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
  const { username, password } = store.getters['user/user'];
  if (username) {
    config.headers = { Authorization: `Basic ${btoa(`${username}:${password}`)}` };
  }
  return config;
};

const intercept = (service, router) => {
  service.interceptors.request.use(addToken);
  service.interceptors.response.use(onSuccess, (error) => onError(error, router));
};

export const register = (router) => {
  intercept(apiService, router);
};
