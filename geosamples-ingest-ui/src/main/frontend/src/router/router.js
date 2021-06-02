import Vue from 'vue';
import VueRouter from 'vue-router';
import { register } from '@/apiMiddleware';
import View from '@/views/view/View.vue';
import Main from '@/views/view/main/Main.vue';
import Admin from '@/views/view/main/admin/Admin.vue';
import Provider from '@/views/view/main/admin/provider/Provider.vue';
import ProviderList from '@/views/view/main/admin/provider/list/ProviderList.vue';
import EditProvider from '@/views/view/main/admin/provider/edit/EditProvider.vue';
import Callback from '@/views/view/callback/Callback.vue';
import Loggedout from '@/views/view/loggedout/Loggedout.vue';
import Home from '@/views/view/main/home/Home.vue';
import User from '@/views/view/main/admin/user/User.vue';
import UserList from '@/views/view/main/admin/user/list/ListUser.vue';
import EditUser from '@/views/view/main/admin/user/edit/EditUser.vue';
import Index from '@/views/view/main/admin/index/Index.vue';
import ListIndex from '@/views/view/main/admin/index/list/ListIndex.vue';
import Audit from '@/views/view/main/admin/audit/Audit.vue';
import ListAudit from '@/views/view/main/admin/audit/list/ListAudit.vue';
import Filter from '@/views/view/main/admin/filter/Filter.vue';
import EditFilter from '@/views/view/main/admin/filter/edit/EditFilter.vue';
import { RAW_BASE_PATH } from '@/resourceBasePath';

Vue.use(VueRouter);

const routes = [
  {
    path: '/view',
    component: View,
    name: 'View',
    redirect: { name: 'Home' },
    children: [
      {
        path: 'callback',
        name: 'Callback',
        component: Callback,
      },
      {
        path: 'loggedout',
        name: 'Loggedout',
        component: Loggedout,
      },
      {
        path: 'main',
        name: 'Main',
        component: Main,
        redirect: { name: 'Home' },
        children: [
          {
            path: 'admin',
            name: 'Admin',
            component: Admin,
            redirect: { name: 'Provider' },
            children: [
              {
                path: 'user',
                component: User,
                name: 'User',
                redirect: { name: 'UserList' },
                children: [
                  {
                    path: 'list',
                    name: 'UserList',
                    component: UserList,
                  },
                  {
                    path: 'add',
                    name: 'AddUser',
                    component: EditUser,
                  },
                  {
                    path: 'edit/:id',
                    name: 'EditUser',
                    component: EditUser,
                    props: true,
                  },
                  {
                    path: '*',
                    redirect: { name: 'User' },
                  },
                ],
              },
              {
                path: 'provider',
                component: Provider,
                name: 'Provider',
                redirect: { name: 'ProviderList' },
                children: [
                  {
                    path: 'list',
                    name: 'ProviderList',
                    component: ProviderList,
                  },
                  {
                    path: 'add',
                    name: 'AddProvider',
                    component: EditProvider,
                  },
                  {
                    path: 'edit/:id',
                    name: 'EditProvider',
                    component: EditProvider,
                    props: true,
                  },
                  {
                    path: '*',
                    redirect: { name: 'Provider' },
                  },
                ],
              },
              {
                path: 'filter',
                component: Filter,
                name: 'Filter',
                redirect: { name: 'EditFilter' },
                children: [
                  {
                    path: 'edit',
                    name: 'EditFilter',
                    component: EditFilter,
                  },
                  {
                    path: '*',
                    redirect: { name: 'Filter' },
                  },
                ],
              },
              {
                path: 'index',
                component: Index,
                name: 'Index',
                redirect: { name: 'IndexList' },
                children: [
                  {
                    path: 'list',
                    name: 'IndexList',
                    component: ListIndex,
                  },
                  {
                    path: '*',
                    redirect: { name: 'Index' },
                  },
                ],
              },
              {
                path: 'audit',
                component: Audit,
                name: 'Audit',
                redirect: { name: 'AuditList' },
                children: [
                  {
                    path: 'list',
                    name: 'AuditList',
                    component: ListAudit,
                  },
                  {
                    path: '*',
                    redirect: { name: 'Audit' },
                  },
                ],
              },
              {
                path: '*',
                redirect: { name: 'Admin' },
              },
            ],
          },
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

register();

export default router;
