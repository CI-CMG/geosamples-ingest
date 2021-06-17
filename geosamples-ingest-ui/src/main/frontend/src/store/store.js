import Vue from 'vue';
import Vuex from 'vuex';
import app from './modules/appModule';
import user from './modules/userModule';
import loginForm from './modules/loginFormModule';

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    app,
    user,
    loginForm,
  },
});
