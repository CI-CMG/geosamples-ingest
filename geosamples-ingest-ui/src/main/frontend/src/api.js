import axios from 'axios';
import {
  BASE_PATH, BASE_API,
} from '@/basePath';

const defaultTimeout = 60000;

const apiService = axios.create();

const initialize = () => {
  apiService.defaults.baseURL = `${BASE_PATH}${BASE_API}`;
  apiService.defaults.timeout = defaultTimeout;
};

initialize();

export {
  apiService,
};
