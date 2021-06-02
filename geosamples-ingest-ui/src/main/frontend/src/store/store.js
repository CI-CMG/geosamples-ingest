import Vue from 'vue';
import Vuex from 'vuex';
import providerForm from '@/store/modules/providerFormModule';
import provider from './modules/providerModule';
import app from './modules/appModule';
import user from './modules/userModule';
import userAdmin from './modules/userAdminModule';
import userAdminForm from './modules/userAdminFormModule';
import index from './modules/indexModule';
import audit from './modules/auditModule';
import filter from './modules/filterModule';

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    app,
    provider,
    providerForm,
    user,
    userAdmin,
    userAdminForm,
    index,
    audit,
    filter,
  },
});
