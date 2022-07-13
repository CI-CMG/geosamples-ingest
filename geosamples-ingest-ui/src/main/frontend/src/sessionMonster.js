// import base64url from 'base64url';
//
// const LOCAL_STORAGE_KEY = 'GEOSAMPLES_INGEST';

// const getStored = () => sessionStorage.getItem(LOCAL_STORAGE_KEY) || '';

// const decodeUser = (stored) => JSON.parse(base64url.decode(stored));

// let currentEncodedSession = '';

// const updateState = (nextEncodedSession, store) => {
//   if (currentEncodedSession !== nextEncodedSession) {
//     currentEncodedSession = nextEncodedSession;
//     if (currentEncodedSession) {
//       sessionStorage.setItem(LOCAL_STORAGE_KEY, currentEncodedSession);
//       const user = decodeUser(currentEncodedSession);
//       store.commit('user/updateUser', user);
//     } else {
//       sessionStorage.removeItem(LOCAL_STORAGE_KEY);
//       store.commit('user/clearUser');
//     }
//     return true;
//   }
//   return false;
// };

// eslint-disable-next-line no-unused-vars
export const setStorageFromAuthResponse = (user, store) => {
  // const encoded = base64url.encode(JSON.stringify(user));
  // updateState(encoded, store);
  // return user;
};

// eslint-disable-next-line no-unused-vars
export const start = (store) => {
  // const nextEncodedSession = getStored();
  // updateState(nextEncodedSession, store);
};

// eslint-disable-next-line no-unused-vars
export const logout = (router, store) => {
  // const stored = getStored();
  // if (stored) {
  //   updateState('', store);
  // }
  // router.push({ name: 'Login' });
};
