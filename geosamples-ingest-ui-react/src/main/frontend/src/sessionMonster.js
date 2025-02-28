import base64url from 'base64url';
import axios from 'axios';
import qs from 'qs';
import crypto from 'crypto-browserify';
import jwtDecode from 'jwt-decode';
import store from '@/store/store';
import {
  BASE_PATH,
  resolveOauthPath,
  CLIENT_ID,
} from './basePath';

const LOCAL_STORAGE_KEY = 'GEOSAMPLES_INGEST_OAUTH';
const pollInterval = 1000;
const loginTimeout = 120000;
const scope = 'openid profile email groups';

const getStored = () => localStorage.getItem(LOCAL_STORAGE_KEY) || '';

const decodeUser = (stored) => {
  const tokenData = JSON.parse(base64url.decode(stored));
  const { token, exp } = tokenData;
  const jwtPayload = jwtDecode(token);
  const { sub: userName, name: displayName } = jwtPayload;
  const authorities = [];
  return {
    exp,
    token,
    user: {
      userName,
      displayName,
      authorities,
    },
  };
};

let currentEncodedSession = '';
let expTimestamp = 0;

const updateState = (nextEncodedSession) => {
  if (currentEncodedSession !== nextEncodedSession) {
    currentEncodedSession = nextEncodedSession;
    if (currentEncodedSession) {
      localStorage.setItem(LOCAL_STORAGE_KEY, currentEncodedSession);
      const { user, exp, token } = decodeUser(currentEncodedSession);
      expTimestamp = exp;
      store.dispatch('userAuth/updateUser', { user, token })
        .catch((error) => {
          localStorage.removeItem(LOCAL_STORAGE_KEY);
          expTimestamp = 0;
          store.commit('userAuth/clearUser');
          throw error;
        });
    } else {
      localStorage.removeItem(LOCAL_STORAGE_KEY);
      expTimestamp = 0;
      store.commit('userAuth/clearUser');
    }
    return true;
  }
  return false;
};

const refreshAccessToken = (stored, router) => {
  const tokenData = JSON.parse(base64url.decode(stored));
  const { refreshToken } = tokenData;
  let promise;
  if (refreshToken) {
    promise = axios({
      method: 'post',
      url: resolveOauthPath('/oidc/token'),
      data: qs.stringify({
        client_id: CLIENT_ID,
        grant_type: 'refresh_token',
        refresh_token: refreshToken,
      }),
      headers: {
        'content-type': 'application/x-www-form-urlencoded',
      },
    }).then(
      ({ data }) => ({ refresh_token: refreshToken, ...data }),
      (error) => {
        let errorData;
        const { response } = error;
        if (response) {
          const { data } = response;
          if (data) {
            errorData = data;
          }
        }
        const message = errorData ? 'Access token expired, please log in again' : 'Authentication Failed';
        updateState('');
        store.commit('app/addErrors', [message]);
        router.push({ name: 'Home' });
      },
    );
  } else {
    updateState('');
    promise = Promise.reject(new Error('No refresh token'));
  }
  return promise;
};

export const setStorageFromOauthResponse = ({
  access_token: token, expires_in: ein, refresh_token: refreshToken, id_token: idToken,
}) => {
  const tokenData = {
    token,
    exp: (ein * 1000) + Date.now(),
    refreshToken,
    idToken,
  };
  const encoded = base64url.encode(JSON.stringify(tokenData));
  updateState(encoded);
  return tokenData;
};

const pollStorage = (router) => {
  const now = Date.now();
  const nextEncodedSession = getStored();
  const updated = updateState(nextEncodedSession);
  if (!updated && expTimestamp && expTimestamp - now <= 3 * pollInterval) {
    refreshAccessToken(nextEncodedSession, router).then(setStorageFromOauthResponse);
  }
};

export const start = (router) => {
  pollStorage(router);
  return setInterval(
    () => {
      pollStorage(router);
    },
    pollInterval,
  );
};

export const logout = (router) => {
  const stored = getStored();
  if (stored) {
    updateState('');
    const tokenData = JSON.parse(base64url.decode(stored));
    const { refreshToken } = tokenData;
    if (refreshToken) {
      axios({
        method: 'post',
        url: resolveOauthPath('/oidc/revoke'),
        auth: {
          username: CLIENT_ID,
          password: '',
        },
        data: qs.stringify({
          token_type_hint: 'refresh_token',
          token: refreshToken,
        }),
        headers: {
          'content-type': 'application/x-www-form-urlencoded',
        },
      });
    }
  }
  router.push({ name: 'View' });
};

const getCallbackUrl = () => {
  const url = window.location.href;
  const split = url.split('/');
  return `${split[0]}//${split[2]}${BASE_PATH}/view/callback`;
};

const getLoginUrl = () => resolveOauthPath('/oidc/authorize');

const redirectUri = getCallbackUrl();
const loginUrl = getLoginUrl();

const sha256 = (buffer) => crypto.createHash('sha256').update(buffer).digest();

export const redirectLogin = () => {
  const nonce = base64url.encode(crypto.randomBytes(32));
  const codeVerifier = base64url.encode(crypto.randomBytes(32));
  const state = {
    url: window.location.href,
    codeVerifier,
    expires: Date.now() + loginTimeout,
  };
  sessionStorage.setItem(nonce, base64url.encode(JSON.stringify(state)));
  const challenge = base64url.encode(sha256(codeVerifier));
  const params = {
    response_type: 'code',
    client_id: CLIENT_ID,
    scope,
    redirect_uri: redirectUri,
    code_challenge: challenge,
    code_challenge_method: 'S256',
    state: nonce,
  };
  window.location.replace(`${loginUrl}?${qs.stringify(params)}`);
};

export const exchangeCodeForToken = ({ code, state, router }) => {
  const nonce = state;
  if (!code || !nonce) {
    return Promise.reject(new Error('Invalid authentication request'));
  }
  const savedState = sessionStorage.getItem(nonce);
  if (!savedState) {
    const message = 'Invalid authentication request';
    store.commit('app/addErrors', [message]);
    return Promise.reject(new Error(message));
  }
  sessionStorage.removeItem(nonce);
  const { url, codeVerifier, expires } = JSON.parse(base64url.decode(savedState));
  if (Date.now() > expires) {
    const message = 'login time exceeded';
    store.commit('app/addErrors', [message]);
    return Promise.reject(message);
  }
  return axios({
    method: 'post',
    url: resolveOauthPath('/oidc/token'),
    data: qs.stringify({
      code,
      client_id: CLIENT_ID,
      grant_type: 'authorization_code',
      code_verifier: codeVerifier,
      redirect_uri: redirectUri,
      scope,
    }),
    headers: {
      'content-type': 'application/x-www-form-urlencoded',
    },
  }).then(
    ({ data }) => ({ url, ...data }),
    (error) => {
      let errorData;
      const { response } = error;
      if (response) {
        const { data } = response;
        if (data) {
          errorData = data;
        }
      }
      const message = errorData ? JSON.stringify(errorData)
        : 'Authentication Failed';
      store.commit('app/addErrors', [message]);
      router.push({ name: 'View' });
      throw error;
    },
  ).then((response) => {
    setStorageFromOauthResponse(response);
    window.location.replace(response.url);
  });
};
