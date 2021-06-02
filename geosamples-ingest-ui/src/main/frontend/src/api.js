import axios from 'axios';
import {
  BASE_PATH, USER_SERVICE_BASE_API, PROVIDER_SERVICE_BASE_API, INDEX_SERVICE_BASE_API, AUDIT_SERVICE_BASE_API, GEOMETRY_FILTER_SERVICE_BASE_API,
} from '@/basePath';

const defaultTimeout = 60000;

const userService = axios.create();
const providerService = axios.create();
const indexService = axios.create();
const auditService = axios.create();
const geometryFilterService = axios.create();

const initialize = () => {
  userService.defaults.baseURL = `${BASE_PATH}${USER_SERVICE_BASE_API}`;
  userService.defaults.timeout = defaultTimeout;

  providerService.defaults.baseURL = `${BASE_PATH}${PROVIDER_SERVICE_BASE_API}`;
  providerService.defaults.timeout = defaultTimeout;

  indexService.defaults.baseURL = `${BASE_PATH}${INDEX_SERVICE_BASE_API}`;
  indexService.defaults.timeout = defaultTimeout;

  auditService.defaults.baseURL = `${BASE_PATH}${AUDIT_SERVICE_BASE_API}`;
  auditService.defaults.timeout = defaultTimeout;

  geometryFilterService.defaults.baseURL = `${BASE_PATH}${GEOMETRY_FILTER_SERVICE_BASE_API}`;
  geometryFilterService.defaults.timeout = 600000;
};

initialize();

export {
  userService,
  providerService,
  indexService,
  auditService,
  geometryFilterService,
};
