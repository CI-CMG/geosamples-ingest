import Vue from 'vue';
import Vuex from 'vuex';
import age from '@/store/modules/ageModule';
import ageForm from '@/store/modules/ageFormModule';
import device from '@/store/modules/deviceModule';
import deviceForm from '@/store/modules/deviceFormModule';
import facility from '@/store/modules/facilityModule';
import facilityForm from '@/store/modules/facilityFormModule';
import lithology from '@/store/modules/lithologyModule';
import lithologyForm from '@/store/modules/lithologyFormModule';
import munsell from '@/store/modules/munsellModule';
import munsellForm from '@/store/modules/munsellFormModule';
import province from '@/store/modules/provinceModule';
import provinceForm from '@/store/modules/provinceFormModule';
import remark from '@/store/modules/remarkModule';
import remarkForm from '@/store/modules/remarkFormModule';
import rockLithology from '@/store/modules/rockLithologyModule';
import rockLithologyForm from '@/store/modules/rockLithologyFormModule';
import rockMineral from '@/store/modules/rockMineralModule';
import rockMineralForm from '@/store/modules/rockMineralFormModule';
import storageMethod from '@/store/modules/storageMethodModule';
import storageMethodForm from '@/store/modules/storageMethodFormModule';
import texture from '@/store/modules/textureModule';
import textureForm from '@/store/modules/textureFormModule';
import weathering from '@/store/modules/weatheringModule';
import weatheringForm from '@/store/modules/weatheringFormModule';
import platform from '@/store/modules/platformModule';
import platformForm from '@/store/modules/platformFormModule';
import app from './modules/appModule';
import user from './modules/userModule';
import loginForm from './modules/loginFormModule';
import submission from './modules/submissionModule';
import submissionForm from './modules/submissionFormModule';

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    app,
    user,
    loginForm,
    platform,
    platformForm,
    age,
    ageForm,
    device,
    deviceForm,
    facility,
    facilityForm,
    lithology,
    lithologyForm,
    munsell,
    munsellForm,
    province,
    provinceForm,
    remark,
    remarkForm,
    rockLithology,
    rockLithologyForm,
    rockMineral,
    rockMineralForm,
    storageMethod,
    storageMethodForm,
    texture,
    textureForm,
    weathering,
    weatheringForm,
    submission,
    submissionForm,
  },
});
