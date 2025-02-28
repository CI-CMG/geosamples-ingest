import {
  RAW_BASE_PATH,
  AUTH_SERVICE_URL,
  CLIENT_ID as CID,
} from './resourceBasePath';

const trimTrailingSlashes = (path) => (path.trim().replace(/\/+$/, ''));

export const BASE_PATH = trimTrailingSlashes(RAW_BASE_PATH);
export const AUTH_SERVICE_BASE_PATH = trimTrailingSlashes(AUTH_SERVICE_URL);
export const CLIENT_ID = CID;

export const BASE_API = '/api/v1';

export const resolveApiPath = (endpoint) => {
  const ep = endpoint ? trimTrailingSlashes(endpoint) : '';
  return `${BASE_PATH}${BASE_API}${ep}`;
};

export const resolveOauthPath = (endpoint) => {
  const ep = endpoint ? trimTrailingSlashes(endpoint) : '';
  return `${AUTH_SERVICE_BASE_PATH}${ep}`;
};
