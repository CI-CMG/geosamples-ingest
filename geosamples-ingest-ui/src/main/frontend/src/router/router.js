import Vue from 'vue';
import VueRouter from 'vue-router';
import { register } from '@/apiMiddleware';
import View from '@/views/view/View.vue';
import Main from '@/views/view/main/Main.vue';
import Home from '@/views/view/main/home/Home.vue';
import Platform from '@/views/view/main/platform/Platform.vue';
import PlatformList from '@/views/view/main/platform/list/PlatformList.vue';
import PlatformEdit from '@/views/view/main/platform/edit/PlatformEdit.vue';
import Login from '@/views/view/login/Login.vue';
import { RAW_BASE_PATH } from '@/resourceBasePath';
import store from '@/store/store';

Vue.use(VueRouter);

const routes = [
  {
    path: '/view',
    component: View,
    name: 'View',
    redirect: { name: 'Home' },
    children: [
      {
        path: 'login',
        name: 'Login',
        component: Login,
      },
      {
        path: 'main',
        name: 'Main',
        component: Main,
        redirect: { name: 'Home' },
        children: [
          {
            path: 'platform',
            component: Platform,
            name: 'Platform',
            redirect: { name: 'PlatformList' },
            children: [
              {
                path: 'list',
                name: 'PlatformList',
                component: PlatformList,
              },
              {
                path: 'add',
                name: 'PlatformAdd',
                component: PlatformEdit,
              },
              {
                path: 'edit/:id',
                name: 'PlatformEdit',
                component: PlatformEdit,
                props: true,
              },
              {
                path: '*',
                redirect: { name: 'Platform' },
              },
            ],
          },
          // {
          //   path: 'admin',
          //   name: 'Admin',
          //   component: Admin,
          //   redirect: { name: 'Provider' },
          //   children: [
          //     {
          //       path: 'user',
          //       component: User,
          //       name: 'User',
          //       redirect: { name: 'UserList' },
          //       children: [
          //         {
          //           path: 'list',
          //           name: 'UserList',
          //           component: UserList,
          //         },
          //         {
          //           path: 'add',
          //           name: 'AddUser',
          //           component: EditUser,
          //         },
          //         {
          //           path: 'edit/:id',
          //           name: 'EditUser',
          //           component: EditUser,
          //           props: true,
          //         },
          //         {
          //           path: '*',
          //           redirect: { name: 'User' },
          //         },
          //       ],
          //     },
          //     {
          //       path: 'provider',
          //       component: Provider,
          //       name: 'Provider',
          //       redirect: { name: 'ProviderList' },
          //       children: [
          //         {
          //           path: 'list',
          //           name: 'ProviderList',
          //           component: ProviderList,
          //         },
          //         {
          //           path: 'add',
          //           name: 'AddProvider',
          //           component: EditProvider,
          //         },
          //         {
          //           path: 'edit/:id',
          //           name: 'EditProvider',
          //           component: EditProvider,
          //           props: true,
          //         },
          //         {
          //           path: '*',
          //           redirect: { name: 'Provider' },
          //         },
          //       ],
          //     },
          //     {
          //       path: 'filter',
          //       component: Filter,
          //       name: 'Filter',
          //       redirect: { name: 'EditFilter' },
          //       children: [
          //         {
          //           path: 'edit',
          //           name: 'EditFilter',
          //           component: EditFilter,
          //         },
          //         {
          //           path: '*',
          //           redirect: { name: 'Filter' },
          //         },
          //       ],
          //     },
          //     {
          //       path: 'audit',
          //       component: Audit,
          //       name: 'Audit',
          //       redirect: { name: 'AuditList' },
          //       children: [
          //         {
          //           path: 'list',
          //           name: 'AuditList',
          //           component: ListAudit,
          //         },
          //         {
          //           path: '*',
          //           redirect: { name: 'Audit' },
          //         },
          //       ],
          //     },
          //     {
          //       path: '*',
          //       redirect: { name: 'Admin' },
          //     },
          //   ],
          // },
          {
            path: 'home',
            name: 'Home',
            component: Home,
          },
          {
            path: '*',
            redirect: { name: 'Main' },
          },
        ],
      },
      {
        path: '*',
        redirect: { name: 'View' },
      },
    ],
  },
  {
    path: '*',
    redirect: { name: 'View' },
  },
];

const router = new VueRouter({
  mode: 'history',
  base: RAW_BASE_PATH,
  routes,
  linkActiveClass: 'active',
});

router.beforeEach((to, from, next) => {
  const loggedIn = !!store.state.user.user.username;
  if (to.name === 'Login') {
    next(!loggedIn);
  } else if (loggedIn) {
    next();
  } else {
    next({ name: 'Login' });
  }
});

register(router);

export default router;
