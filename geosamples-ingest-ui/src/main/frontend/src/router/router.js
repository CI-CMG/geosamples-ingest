import Vue from 'vue';
import VueRouter from 'vue-router';
import { register } from '@/apiMiddleware';
import View from '@/views/view/View.vue';
import Main from '@/views/view/main/Main.vue';
import Home from '@/views/view/main/home/Home.vue';
import Platform from '@/views/view/main/platform/Platform.vue';
import PlatformList from '@/views/view/main/platform/list/PlatformList.vue';
import PlatformEdit from '@/views/view/main/platform/edit/PlatformEdit.vue';
import Age from '@/views/view/main/age/Age.vue';
import AgeList from '@/views/view/main/age/list/AgeList.vue';
import AgeEdit from '@/views/view/main/age/edit/AgeEdit.vue';
import Device from '@/views/view/main/device/Device.vue';
import DeviceList from '@/views/view/main/device/list/DeviceList.vue';
import DeviceEdit from '@/views/view/main/device/edit/DeviceEdit.vue';
import Facility from '@/views/view/main/facility/Facility.vue';
import FacilityList from '@/views/view/main/facility/list/FacilityList.vue';
import FacilityEdit from '@/views/view/main/facility/edit/FacilityEdit.vue';
import Lithology from '@/views/view/main/lithology/Lithology.vue';
import LithologyList from '@/views/view/main/lithology/list/LithologyList.vue';
import LithologyEdit from '@/views/view/main/lithology/edit/LithologyEdit.vue';
import Province from '@/views/view/main/province/Province.vue';
import ProvinceList from '@/views/view/main/province/list/ProvinceList.vue';
import ProvinceEdit from '@/views/view/main/province/edit/ProvinceEdit.vue';
import Remark from '@/views/view/main/remark/Remark.vue';
import RemarkList from '@/views/view/main/remark/list/RemarkList.vue';
import RemarkEdit from '@/views/view/main/remark/edit/RemarkEdit.vue';
import RockLithology from '@/views/view/main/rock-lithology/RockLithology.vue';
import RockLithologyList from '@/views/view/main/rock-lithology/list/RockLithologyList.vue';
import RockLithologyEdit from '@/views/view/main/rock-lithology/edit/RockLithologyEdit.vue';
import RockMineral from '@/views/view/main/rock-mineral/RockMineral.vue';
import RockMineralList from '@/views/view/main/rock-mineral/list/RockMineralList.vue';
import RockMineralEdit from '@/views/view/main/rock-mineral/edit/RockMineralEdit.vue';
import StorageMethod from '@/views/view/main/storage-method/StorageMethod.vue';
import StorageMethodList from '@/views/view/main/storage-method/list/StorageMethodList.vue';
import StorageMethodEdit from '@/views/view/main/storage-method/edit/StorageMethodEdit.vue';
import Texture from '@/views/view/main/texture/Texture.vue';
import TextureList from '@/views/view/main/texture/list/TextureList.vue';
import TextureEdit from '@/views/view/main/texture/edit/TextureEdit.vue';
import Weathering from '@/views/view/main/weathering/Weathering.vue';
import WeatheringList from '@/views/view/main/weathering/list/WeatheringList.vue';
import WeatheringEdit from '@/views/view/main/weathering/edit/WeatheringEdit.vue';
import Munsell from '@/views/view/main/munsell/Munsell.vue';
import MunsellList from '@/views/view/main/munsell/list/MunsellList.vue';
import MunsellEdit from '@/views/view/main/munsell/edit/MunsellEdit.vue';
import Submit from '@/views/view/main/submit/Submit.vue';
import NewSubmission from '@/views/view/main/submit/new/NewSubmission.vue';
import ErrorSubmission from '@/views/view/main/submit/error/ErrorSubmission.vue';
import Login from '@/views/view/login/Login.vue';
import TempSample from '@/views/view/main/temp-sample/TempSample.vue';
import TempSampleList from '@/views/view/main/temp-sample/list/TempSampleList.vue';
import Interval from '@/views/view/main/interval/Interval.vue';
import IntervalList from '@/views/view/main/interval/list/IntervalList.vue';
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
            path: 'submit',
            component: Submit,
            name: 'Submit',
            redirect: { name: 'NewSubmission' },
            children: [
              {
                path: 'new',
                name: 'NewSubmission',
                component: NewSubmission,
              },
              {
                path: 'error',
                name: 'ErrorSubmission',
                component: ErrorSubmission,
              },
              {
                path: '*',
                redirect: { name: 'Submit' },
              },
            ],
          },
          {
            path: 'temp-sample',
            component: TempSample,
            name: 'TempSample',
            redirect: { name: 'TempSampleList' },
            children: [
              {
                path: 'list',
                name: 'TempSampleList',
                component: TempSampleList,
              },
              {
                path: '*',
                redirect: { name: 'TempSample' },
              },
            ],
          },
          {
            path: 'interval',
            component: Interval,
            name: 'Interval',
            redirect: { name: 'IntervalList' },
            children: [
              {
                path: 'list',
                name: 'IntervalList',
                component: IntervalList,
              },
              {
                path: '*',
                redirect: { name: 'Interval' },
              },
            ],
          },
          {
            path: 'age',
            component: Age,
            name: 'Age',
            redirect: { name: 'AgeList' },
            children: [
              {
                path: 'list',
                name: 'AgeList',
                component: AgeList,
              },
              {
                path: 'add',
                name: 'AgeAdd',
                component: AgeEdit,
              },
              {
                path: 'edit/:id',
                name: 'AgeEdit',
                component: AgeEdit,
                props: true,
              },
              {
                path: '*',
                redirect: { name: 'Age' },
              },
            ],
          },
          {
            path: 'device',
            component: Device,
            name: 'Device',
            redirect: { name: 'DeviceList' },
            children: [
              {
                path: 'list',
                name: 'DeviceList',
                component: DeviceList,
              },
              {
                path: 'add',
                name: 'DeviceAdd',
                component: DeviceEdit,
              },
              {
                path: 'edit/:id',
                name: 'DeviceEdit',
                component: DeviceEdit,
                props: true,
              },
              {
                path: '*',
                redirect: { name: 'Device' },
              },
            ],
          },
          {
            path: 'facility',
            component: Facility,
            name: 'Facility',
            redirect: { name: 'FacilityList' },
            children: [
              {
                path: 'list',
                name: 'FacilityList',
                component: FacilityList,
              },
              {
                path: 'add',
                name: 'FacilityAdd',
                component: FacilityEdit,
              },
              {
                path: 'edit/:id',
                name: 'FacilityEdit',
                component: FacilityEdit,
                props: true,
              },
              {
                path: '*',
                redirect: { name: 'Facility' },
              },
            ],
          },
          {
            path: 'lithology',
            component: Lithology,
            name: 'Lithology',
            redirect: { name: 'LithologyList' },
            children: [
              {
                path: 'list',
                name: 'LithologyList',
                component: LithologyList,
              },
              {
                path: 'add',
                name: 'LithologyAdd',
                component: LithologyEdit,
              },
              {
                path: 'edit/:id',
                name: 'LithologyEdit',
                component: LithologyEdit,
                props: true,
              },
              {
                path: '*',
                redirect: { name: 'Lithology' },
              },
            ],
          },
          {
            path: 'munsell',
            component: Munsell,
            name: 'Munsell',
            redirect: { name: 'MunsellList' },
            children: [
              {
                path: 'list',
                name: 'MunsellList',
                component: MunsellList,
              },
              {
                path: 'add',
                name: 'MunsellAdd',
                component: MunsellEdit,
              },
              {
                path: 'edit/:id',
                name: 'MunsellEdit',
                component: MunsellEdit,
                props: true,
              },
              {
                path: '*',
                redirect: { name: 'Munsell' },
              },
            ],
          },
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
          {
            path: 'province',
            component: Province,
            name: 'Province',
            redirect: { name: 'ProvinceList' },
            children: [
              {
                path: 'list',
                name: 'ProvinceList',
                component: ProvinceList,
              },
              {
                path: 'add',
                name: 'ProvinceAdd',
                component: ProvinceEdit,
              },
              {
                path: 'edit/:id',
                name: 'ProvinceEdit',
                component: ProvinceEdit,
                props: true,
              },
              {
                path: '*',
                redirect: { name: 'Province' },
              },
            ],
          },
          {
            path: 'remark',
            component: Remark,
            name: 'Remark',
            redirect: { name: 'RemarkList' },
            children: [
              {
                path: 'list',
                name: 'RemarkList',
                component: RemarkList,
              },
              {
                path: 'add',
                name: 'RemarkAdd',
                component: RemarkEdit,
              },
              {
                path: 'edit/:id',
                name: 'RemarkEdit',
                component: RemarkEdit,
                props: true,
              },
              {
                path: '*',
                redirect: { name: 'Remark' },
              },
            ],
          },
          {
            path: 'rock-lithology',
            component: RockLithology,
            name: 'RockLithology',
            redirect: { name: 'RockLithologyList' },
            children: [
              {
                path: 'list',
                name: 'RockLithologyList',
                component: RockLithologyList,
              },
              {
                path: 'add',
                name: 'RockLithologyAdd',
                component: RockLithologyEdit,
              },
              {
                path: 'edit/:id',
                name: 'RockLithologyEdit',
                component: RockLithologyEdit,
                props: true,
              },
              {
                path: '*',
                redirect: { name: 'RockLithology' },
              },
            ],
          },
          {
            path: 'rock-mineral',
            component: RockMineral,
            name: 'RockMineral',
            redirect: { name: 'RockMineralList' },
            children: [
              {
                path: 'list',
                name: 'RockMineralList',
                component: RockMineralList,
              },
              {
                path: 'add',
                name: 'RockMineralAdd',
                component: RockMineralEdit,
              },
              {
                path: 'edit/:id',
                name: 'RockMineralEdit',
                component: RockMineralEdit,
                props: true,
              },
              {
                path: '*',
                redirect: { name: 'RockMineral' },
              },
            ],
          },
          {
            path: 'storage-method',
            component: StorageMethod,
            name: 'StorageMethod',
            redirect: { name: 'StorageMethodList' },
            children: [
              {
                path: 'list',
                name: 'StorageMethodList',
                component: StorageMethodList,
              },
              {
                path: 'add',
                name: 'StorageMethodAdd',
                component: StorageMethodEdit,
              },
              {
                path: 'edit/:id',
                name: 'StorageMethodEdit',
                component: StorageMethodEdit,
                props: true,
              },
              {
                path: '*',
                redirect: { name: 'StorageMethod' },
              },
            ],
          },
          {
            path: 'texture',
            component: Texture,
            name: 'Texture',
            redirect: { name: 'TextureList' },
            children: [
              {
                path: 'list',
                name: 'TextureList',
                component: TextureList,
              },
              {
                path: 'add',
                name: 'TextureAdd',
                component: TextureEdit,
              },
              {
                path: 'edit/:id',
                name: 'TextureEdit',
                component: TextureEdit,
                props: true,
              },
              {
                path: '*',
                redirect: { name: 'Texture' },
              },
            ],
          },
          {
            path: 'weathering',
            component: Weathering,
            name: 'Weathering',
            redirect: { name: 'WeatheringList' },
            children: [
              {
                path: 'list',
                name: 'WeatheringList',
                component: WeatheringList,
              },
              {
                path: 'add',
                name: 'WeatheringAdd',
                component: WeatheringEdit,
              },
              {
                path: 'edit/:id',
                name: 'WeatheringEdit',
                component: WeatheringEdit,
                props: true,
              },
              {
                path: '*',
                redirect: { name: 'Weathering' },
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
