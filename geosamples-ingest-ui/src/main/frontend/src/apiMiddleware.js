import store from '@/store/store';
import {
  userService, providerService, indexService, auditService, geometryFilterService,
} from './api';

const onSuccess = (response) => response;
const onError = (error) => {
  // if (error.response && (error.response.status === 401 || error.response.status === 403)) {
  //
  //   router.push({ name: 'Login' });
  // } else {
  let msg = ['An Unknown Error Occurred'];
  if (error.response && error.response.data && error.response.data.flashErrors) {
    msg = error.response.data.flashErrors;
  }
  store.commit('app/addErrors', msg);
  // }
  throw error;
};

const addToken = (config) => {
  const token = store.getters['user/token'];
  if (token) {
    config.headers = { Authorization: `Bearer ${token}` };
  }
  return config;
};

const intercept = (service) => {
  service.interceptors.request.use(addToken);
  service.interceptors.response.use(onSuccess, onError);
};

export const register = () => {
  intercept(userService);
  intercept(providerService);
  intercept(indexService);
  intercept(auditService);
  intercept(geometryFilterService);
};
