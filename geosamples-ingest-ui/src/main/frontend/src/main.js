import './resourceBasePath';
import Vue from 'vue';
import router from '@/router/router';
import store from '@/store/store';
import '@/assets/css/main.scss';
import { start as startSessionMonster } from '@/sessionMonster';
import { BootstrapVue, IconsPlugin } from 'bootstrap-vue';
import App from './App.vue';

Vue.config.productionTip = false;

startSessionMonster();

Vue.use(BootstrapVue);
Vue.use(IconsPlugin);

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount('#app');
