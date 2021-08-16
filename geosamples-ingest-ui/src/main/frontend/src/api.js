import axios from 'axios';
import {
  BASE_PATH, BASE_API,
} from '@/basePath';

const apiService = axios.create();

const initialize = () => {
  apiService.defaults.baseURL = `${BASE_PATH}${BASE_API}`;
};

initialize();

export {
  apiService,
};
