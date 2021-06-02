import { RAW_BASE_PATH, AUTH_SERVICE_URL, CLIENT_ID as CID } from './resourceBasePath';

const trimTrailingSlashes = (path) => (path.trim().replace(/\/+$/, ''));

export const BASE_PATH = trimTrailingSlashes(RAW_BASE_PATH);
export const CLIENT_ID = CID;
export const AUTH_SERVICE_BASE_PATH = trimTrailingSlashes(AUTH_SERVICE_URL);

export const PROVIDER_SERVICE_BASE_API = '/provider-service/api/v1';
// export const FEATURE_SERVICE_BASE_OPENAPI = '/feature-service/openapi/v3/api-docs';
export const USER_SERVICE_BASE_API = '/user-service/api/v1';
export const INDEX_SERVICE_BASE_API = '/index-service/api/v1';
export const AUDIT_SERVICE_BASE_API = '/audit-service/api/v1';
export const GEOMETRY_FILTER_SERVICE_BASE_API = '/geometry-filter-service/api/v1';

export const resolveProviderApiPath = (endpoint) => {
  const ep = endpoint ? trimTrailingSlashes(endpoint) : '';
  return `${BASE_PATH}${PROVIDER_SERVICE_BASE_API}${ep}`;
};

// export const resolveFeatureOpenApiPath = () => `${BASE_PATH}${FEATURE_SERVICE_BASE_OPENAPI}`;

export const resolveUserApiPath = (endpoint) => {
  const ep = endpoint ? trimTrailingSlashes(endpoint) : '';
  return `${BASE_PATH}${USER_SERVICE_BASE_API}${ep}`;
};

export const resolveOauthPath = (endpoint) => {
  const ep = endpoint ? trimTrailingSlashes(endpoint) : '';
  return `${AUTH_SERVICE_BASE_PATH}${ep}`;
};

export const LOCAL_STORAGE_PREFIX = `${AUTH_SERVICE_BASE_PATH}::${CLIENT_ID}`;
