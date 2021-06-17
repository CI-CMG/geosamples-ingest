import { RAW_BASE_PATH } from './resourceBasePath';

const trimTrailingSlashes = (path) => (path.trim().replace(/\/+$/, ''));

export const BASE_PATH = trimTrailingSlashes(RAW_BASE_PATH);

export const BASE_API = '/api/v1';

export const resolveApiPath = (endpoint) => {
  const ep = endpoint ? trimTrailingSlashes(endpoint) : '';
  return `${BASE_PATH}${BASE_API}${ep}`;
};
