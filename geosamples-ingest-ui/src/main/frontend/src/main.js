import './resourceBasePath';
import Vue from 'vue';
import router from '@/router/router';
import store from '@/store/store';
import '@/assets/css/main.scss';
import { start as startSessionMonster } from '@/sessionMonster';
import { BootstrapVue, IconsPlugin } from 'bootstrap-vue';
import VueVirtualScroller from 'vue-virtual-scroller';
import vSelect from 'vue-select';
import App from './App.vue';

Vue.config.productionTip = false;

startSessionMonster(router);

Vue.use(VueVirtualScroller);
Vue.use(BootstrapVue);
Vue.use(IconsPlugin);
Vue.component('v-select', vSelect);

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount('#app');
