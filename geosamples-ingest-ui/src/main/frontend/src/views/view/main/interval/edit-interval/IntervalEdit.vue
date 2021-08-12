<template>
  <div class="m-2">

<!--    <b-breadcrumb :items="items"/>-->

    <div v-if="ready">

      <h1 v-if="isEdit" class="text-primary">Edit Interval - {{ imlgs }}-{{ id }}</h1>

      <b-button type="button" variant="danger" @click="doDelete" >Delete</b-button>

      <b-form @submit.prevent="saveForm" @reset.prevent="reset">

        <b-form-group label="Depth Top" :label-for="depthTopId">
          <b-form-input
            :id="depthTopId"
            type="text" @blur="() => setTouched({path: 'depthTop', touched: true})"
            :value="getValue('depthTop')"
            @update="(value) => setValue({ path: 'depthTop', value })"
            :state="showError('depthTop')"
          />
          <b-form-invalid-feedback>{{ getError('depthTop') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Depth Bot" :label-for="depthBotId">
          <b-form-input
            :id="depthBotId"
            type="text" @blur="() => setTouched({path: 'depthBot', touched: true})"
            :value="getValue('depthBot')"
            @update="(value) => setValue({ path: 'depthBot', value })"
            :state="showError('depthBot')"
          />
          <b-form-invalid-feedback>{{ getError('depthBot') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="DH Core ID" :label-for="dhCoreIdId">
          <b-form-input
            :id="dhCoreIdId"
            type="text" @blur="() => setTouched({path: 'dhCoreId', touched: true})"
            :value="getValue('dhCoreId')"
            @update="(value) => setValue({ path: 'dhCoreId', value })"
            :state="showError('dhCoreId')"
          />
          <b-form-invalid-feedback>{{ getError('dhCoreId') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="DH Core Length" :label-for="dhCoreLengthId">
          <b-form-input
            :id="dhCoreLengthId"
            type="text" @blur="() => setTouched({path: 'dhCoreLength', touched: true})"
            :value="getValue('dhCoreLength')"
            @update="(value) => setValue({ path: 'dhCoreLength', value })"
            :state="showError('dhCoreLength')"
          />
          <b-form-invalid-feedback>{{ getError('dhCoreLength') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="DH Core Interval" :label-for="dhCoreIntervalId">
          <b-form-input
            :id="dhCoreIntervalId"
            type="text" @blur="() => setTouched({path: 'dhCoreInterval', touched: true})"
            :value="getValue('dhCoreInterval')"
            @update="(value) => setValue({ path: 'dhCoreInterval', value })"
            :state="showError('dhCoreInterval')"
          />
          <b-form-invalid-feedback>{{ getError('dhCoreInterval') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Top DH Core" :label-for="dTopInDhCoreId">
          <b-form-input
            :id="dTopInDhCoreId"
            type="text" @blur="() => setTouched({path: 'dTopInDhCore', touched: true})"
            :value="getValue('dTopInDhCore')"
            @update="(value) => setValue({ path: 'dTopInDhCore', value })"
            :state="showError('dTopInDhCore')"
          />
          <b-form-invalid-feedback>{{ getError('dTopInDhCore') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Bot DH Core" :label-for="dBotInDhCoreId">
          <b-form-input
            :id="dBotInDhCoreId"
            type="text" @blur="() => setTouched({path: 'dBotInDhCore', touched: true})"
            :value="getValue('dBotInDhCore')"
            @update="(value) => setValue({ path: 'dBotInDhCore', value })"
            :state="showError('dBotInDhCore')"
          />
          <b-form-invalid-feedback>{{ getError('dBotInDhCore') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Lithology Code 1" :label-for="lithCode1Id">
          <b-form-input
            :id="lithCode1Id"
            type="text" @blur="() => setTouched({path: 'lithCode1', touched: true})"
            :value="getValue('lithCode1')"
            @update="(value) => setValue({ path: 'lithCode1', value })"
            :state="showError('lithCode1')"
          />
          <b-form-invalid-feedback>{{ getError('lithCode1') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Texture Code 1" :label-for="textCode1Id">
          <b-form-input
            :id="textCode1Id"
            type="text" @blur="() => setTouched({path: 'textCode1', touched: true})"
            :value="getValue('textCode1')"
            @update="(value) => setValue({ path: 'textCode1', value })"
            :state="showError('textCode1')"
          />
          <b-form-invalid-feedback>{{ getError('textCode1') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Lithology Code 2" :label-for="lithCode2Id">
          <b-form-input
            :id="lithCode2Id"
            type="text" @blur="() => setTouched({path: 'lithCode2', touched: true})"
            :value="getValue('lithCode2')"
            @update="(value) => setValue({ path: 'lithCode2', value })"
            :state="showError('lithCode2')"
          />
          <b-form-invalid-feedback>{{ getError('lithCode2') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Texture Code 2" :label-for="textCode2Id">
          <b-form-input
            :id="textCode2Id"
            type="text" @blur="() => setTouched({path: 'textCode2', touched: true})"
            :value="getValue('textCode2')"
            @update="(value) => setValue({ path: 'textCode2', value })"
            :state="showError('textCode2')"
          />
          <b-form-invalid-feedback>{{ getError('textCode2') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Lithologic Composition Code 1" :label-for="compCode1Id">
          <b-form-input
            :id="compCode1Id"
            type="text" @blur="() => setTouched({path: 'compCode1', touched: true})"
            :value="getValue('compCode1')"
            @update="(value) => setValue({ path: 'compCode1', value })"
            :state="showError('compCode1')"
          />
          <b-form-invalid-feedback>{{ getError('compCode1') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Lithologic Composition Code 2" :label-for="compCode2Id">
          <b-form-input
            :id="compCode2Id"
            type="text" @blur="() => setTouched({path: 'compCode2', touched: true})"
            :value="getValue('compCode2')"
            @update="(value) => setValue({ path: 'compCode2', value })"
            :state="showError('compCode2')"
          />
          <b-form-invalid-feedback>{{ getError('compCode2') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Lithologic Composition Code 3" :label-for="compCode3Id">
          <b-form-input
            :id="compCode3Id"
            type="text" @blur="() => setTouched({path: 'compCode3', touched: true})"
            :value="getValue('compCode3')"
            @update="(value) => setValue({ path: 'compCode3', value })"
            :state="showError('compCode3')"
          />
          <b-form-invalid-feedback>{{ getError('compCode3') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Lithologic Composition Code 4" :label-for="compCode4Id">
          <b-form-input
            :id="compCode4Id"
            type="text" @blur="() => setTouched({path: 'compCode4', touched: true})"
            :value="getValue('compCode4')"
            @update="(value) => setValue({ path: 'compCode4', value })"
            :state="showError('compCode4')"
          />
          <b-form-invalid-feedback>{{ getError('compCode4') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Lithologic Composition Code 5" :label-for="compCode5Id">
          <b-form-input
            :id="compCode5Id"
            type="text" @blur="() => setTouched({path: 'compCode5', touched: true})"
            :value="getValue('compCode5')"
            @update="(value) => setValue({ path: 'compCode5', value })"
            :state="showError('compCode5')"
          />
          <b-form-invalid-feedback>{{ getError('compCode5') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Lithologic Composition Code 6" :label-for="compCode6Id">
          <b-form-input
            :id="compCode6Id"
            type="text" @blur="() => setTouched({path: 'compCode6', touched: true})"
            :value="getValue('compCode6')"
            @update="(value) => setValue({ path: 'compCode6', value })"
            :state="showError('compCode6')"
          />
          <b-form-invalid-feedback>{{ getError('compCode6') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Description" :label-for="descriptionId">
          <b-form-input
            :id="descriptionId"
            type="text" @blur="() => setTouched({path: 'description', touched: true})"
            :value="getValue('description')"
            @update="(value) => setValue({ path: 'description', value })"
            :state="showError('description')"
          />
          <b-form-invalid-feedback>{{ getError('description') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Age Code" :label-for="ageCodeId">
          <b-form-input
            :id="ageCodeId"
            type="text" @blur="() => setTouched({path: 'ageCode', touched: true})"
            :value="getValue('ageCode')"
            @update="(value) => setValue({ path: 'ageCode', value })"
            :state="showError('ageCode')"
          />
          <b-form-invalid-feedback>{{ getError('ageCode') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Absolute Age Top" :label-for="absoluteAgeTopId">
          <b-form-input
            :id="absoluteAgeTopId"
            type="text" @blur="() => setTouched({path: 'absoluteAgeTop', touched: true})"
            :value="getValue('absoluteAgeTop')"
            @update="(value) => setValue({ path: 'absoluteAgeTop', value })"
            :state="showError('absoluteAgeTop')"
          />
          <b-form-invalid-feedback>{{ getError('absoluteAgeTop') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Absolute Age Bot" :label-for="absoluteAgeBotId">
          <b-form-input
            :id="absoluteAgeBotId"
            type="text" @blur="() => setTouched({path: 'absoluteAgeBot', touched: true})"
            :value="getValue('absoluteAgeBot')"
            @update="(value) => setValue({ path: 'absoluteAgeBot', value })"
            :state="showError('absoluteAgeBot')"
          />
          <b-form-invalid-feedback>{{ getError('absoluteAgeBot') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Weight" :label-for="weightId">
          <b-form-input
            :id="weightId"
            type="text" @blur="() => setTouched({path: 'weight', touched: true})"
            :value="getValue('weight')"
            @update="(value) => setValue({ path: 'weight', value })"
            :state="showError('weight')"
          />
          <b-form-invalid-feedback>{{ getError('weight') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Rock Lithology Code" :label-for="rockLithCodeId">
          <b-form-input
            :id="rockLithCodeId"
            type="text" @blur="() => setTouched({path: 'rockLithCode', touched: true})"
            :value="getValue('rockLithCode')"
            @update="(value) => setValue({ path: 'rockLithCode', value })"
            :state="showError('rockLithCode')"
          />
          <b-form-invalid-feedback>{{ getError('rockLithCode') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Rock Mineralogy Code" :label-for="rockMinCodeId">
          <b-form-input
            :id="rockMinCodeId"
            type="text" @blur="() => setTouched({path: 'rockMinCode', touched: true})"
            :value="getValue('rockMinCode')"
            @update="(value) => setValue({ path: 'rockMinCode', value })"
            :state="showError('rockMinCode')"
          />
          <b-form-invalid-feedback>{{ getError('rockMinCode') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Weathering/Metamorphism Code" :label-for="weathMetaCodeId">
          <b-form-input
            :id="weathMetaCodeId"
            type="text" @blur="() => setTouched({path: 'weathMetaCode', touched: true})"
            :value="getValue('weathMetaCode')"
            @update="(value) => setValue({ path: 'weathMetaCode', value })"
            :state="showError('weathMetaCode')"
          />
          <b-form-invalid-feedback>{{ getError('weathMetaCode') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Rock Glass Remarks & Mn/Fe Oxide Code" :label-for="remarkCodeId">
          <b-form-input
            :id="remarkCodeId"
            type="text" @blur="() => setTouched({path: 'remarkCode', touched: true})"
            :value="getValue('remarkCode')"
            @update="(value) => setValue({ path: 'remarkCode', value })"
            :state="showError('remarkCode')"
          />
          <b-form-invalid-feedback>{{ getError('remarkCode') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Munsell Code" :label-for="munsellCodeId">
          <b-form-input
            :id="munsellCodeId"
            type="text" @blur="() => setTouched({path: 'munsellCode', touched: true})"
            :value="getValue('munsellCode')"
            @update="(value) => setValue({ path: 'munsellCode', value })"
            :state="showError('munsellCode')"
          />
          <b-form-invalid-feedback>{{ getError('munsellCode') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Exhausted" :label-for="exhaustedId">
          <b-form-checkbox
            :id="exhaustedId"
            :checked="getValue('exhausted')"
            @change="(value) => setValue({ path: 'exhausted', value })"
            :state="showError('exhausted')"
          />
          <b-form-invalid-feedback>{{ getError('exhausted') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Photo Link" :label-for="photoLinkId">
          <b-form-input
            :id="photoLinkId"
            type="text" @blur="() => setTouched({path: 'photoLink', touched: true})"
            :value="getValue('photoLink')"
            @update="(value) => setValue({ path: 'photoLink', value })"
            :state="showError('photoLink')"
          />
          <b-form-invalid-feedback>{{ getError('photoLink') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Lake" :label-for="lakeId">
          <b-form-input
            :id="lakeId"
            type="text" @blur="() => setTouched({path: 'lake', touched: true})"
            :value="getValue('lake')"
            @update="(value) => setValue({ path: 'lake', value })"
            :state="showError('lake')"
          />
          <b-form-invalid-feedback>{{ getError('lake') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Unit Number" :label-for="unitNumberId">
          <b-form-input
            :id="unitNumberId"
            type="text" @blur="() => setTouched({path: 'unitNumber', touched: true})"
            :value="getValue('unitNumber')"
            @update="(value) => setValue({ path: 'unitNumber', value })"
            :state="showError('unitNumber')"
          />
          <b-form-invalid-feedback>{{ getError('unitNumber') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Comments" :label-for="intCommentsId">
          <b-form-input
            :id="intCommentsId"
            type="text" @blur="() => setTouched({path: 'intComments', touched: true})"
            :value="getValue('intComments')"
            @update="(value) => setValue({ path: 'intComments', value })"
            :state="showError('intComments')"
          />
          <b-form-invalid-feedback>{{ getError('intComments') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="DH Device" :label-for="dhDeviceId">
          <b-form-input
            :id="dhDeviceId"
            type="text" @blur="() => setTouched({path: 'dhDevice', touched: true})"
            :value="getValue('dhDevice')"
            @update="(value) => setValue({ path: 'dhDevice', value })"
            :state="showError('dhDevice')"
          />
          <b-form-invalid-feedback>{{ getError('dhDevice') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="CD Top" :label-for="cdTopId">
          <b-form-input
            :id="cdTopId"
            type="text" @blur="() => setTouched({path: 'cdTop', touched: true})"
            :value="getValue('cdTop')"
            @update="(value) => setValue({ path: 'cdTop', value })"
            :state="showError('cdTop')"
          />
          <b-form-invalid-feedback>{{ getError('cdTop') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="CD Bot" :label-for="cdBotId">
          <b-form-input
            :id="cdBotId"
            type="text" @blur="() => setTouched({path: 'cdBot', touched: true})"
            :value="getValue('cdBot')"
            @update="(value) => setValue({ path: 'cdBot', value })"
            :state="showError('cdBot')"
          />
          <b-form-invalid-feedback>{{ getError('cdBot') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="IGSN" :label-for="igsnId">
          <b-form-input
            :id="igsnId"
            type="text" @blur="() => setTouched({path: 'igsn', touched: true})"
            :value="getValue('igsn')"
            @update="(value) => setValue({ path: 'igsn', value })"
            :state="showError('igsn')"
          />
          <b-form-invalid-feedback>{{ getError('igsn') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Publish" :label-for="publishId">
          <b-form-checkbox
            :id="publishId"
            :checked="getValue('publish')"
            @change="(value) => setValue({ path: 'publish', value })"
            :state="showError('publish')"
          />
          <b-form-invalid-feedback>{{ getError('publish') }}</b-form-invalid-feedback>
        </b-form-group>

        <div>
          <b-button v-if="showSubmit" type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">Save</b-button>
          <b-button v-if="formDirty" type="reset" variant="danger" class="mb-2 mr-sm-2 mb-sm-0">Reset</b-button>
        </div>

      </b-form>
    </div>
    <div v-else>
      <b-spinner/>
    </div>
  </div>
</template>

<script>
import {
  mapActions, mapGetters, mapMutations, mapState,
} from 'vuex';
import genId from '@/components/idGenerator';

export default {
  props: ['id', 'imlgs'],
  data() {
    return {
      intervalId: '',
      depthTopId: '',
      depthBotId: '',
      dhCoreIdId: '',
      dhCoreLengthId: '',
      dhCoreIntervalId: '',
      dTopInDhCoreId: '',
      dBotInDhCoreId: '',
      lithCode1Id: '',
      textCode1Id: '',
      lithCode2Id: '',
      textCode2Id: '',
      compCode1Id: '',
      compCode2Id: '',
      compCode3Id: '',
      compCode4Id: '',
      compCode5Id: '',
      compCode6Id: '',
      descriptionId: '',
      ageCodeId: '',
      absoluteAgeTopId: '',
      absoluteAgeBotId: '',
      weightId: '',
      rockLithCodeId: '',
      rockMinCodeId: '',
      weathMetaCodeId: '',
      remarkCodeId: '',
      munsellCodeId: '',
      exhaustedId: '',
      photoLinkId: '',
      lakeId: '',
      unitNumberId: '',
      intCommentsId: '',
      dhDeviceId: '',
      cdTopId: '',
      cdBotId: '',
      publishId: '',
      igsnId: '',
    };
  },
  beforeMount() {
    this.intervalId = genId();
    this.depthTopId = genId();
    this.depthBotId = genId();
    this.dhCoreIdId = genId();
    this.dhCoreLengthId = genId();
    this.dhCoreIntervalId = genId();
    this.dTopInDhCoreId = genId();
    this.dBotInDhCoreId = genId();
    this.lithCode1Id = genId();
    this.textCode1Id = genId();
    this.lithCode2Id = genId();
    this.textCode2Id = genId();
    this.compCode1Id = genId();
    this.compCode2Id = genId();
    this.compCode3Id = genId();
    this.compCode4Id = genId();
    this.compCode5Id = genId();
    this.compCode6Id = genId();
    this.descriptionId = genId();
    this.ageCodeId = genId();
    this.absoluteAgeTopId = genId();
    this.absoluteAgeBotId = genId();
    this.weightId = genId();
    this.rockLithCodeId = genId();
    this.rockMinCodeId = genId();
    this.weathMetaCodeId = genId();
    this.remarkCodeId = genId();
    this.munsellCodeId = genId();
    this.exhaustedId = genId();
    this.photoLinkId = genId();
    this.lakeId = genId();
    this.unitNumberId = genId();
    this.intCommentsId = genId();
    this.dhDeviceId = genId();
    this.cdTopId = genId();
    this.cdBotId = genId();
    this.publishId = genId();
    this.igsnId = genId();
  },
  methods: {
    ...mapMutations('intervalForm',
      [
        'initialize',
        'setValue',
        'setTouched',
        'setError',
        'deleteFromArray',
        'addToArray',
      ]),
    ...mapActions('interval', ['loadInterval', 'saveInterval', 'deleteInterval']),
    ...mapActions('intervalForm', ['submit', 'reset']),
    load({ id, imlgs }) {
      return this.loadInterval({ id, imlgs });
    },
    save(params) {
      return this.saveInterval(params);
    },
    delete() {
      return this.deleteInterval({ id: this.id, imlgs: this.imlgs });
    },
    saveForm() {
      this.submit()
        .then((provider) => this.save({ provider, id: this.id, imlgs: this.imlgs }))
        .then(() => this.$router.push({ name: 'IntervalList' }));
    },
    doDelete() {
      this.delete().then(() => this.$router.push({ name: 'IntervalList' }));
    },
  },

  computed: {
    ...mapState('interval', ['intervalLoading', 'intervalSaving']),
    ...mapGetters('intervalForm',
      [
        'getValue',
        'formDirty',
        'getError',
        'isTouched',
        'formHasUntouchedErrors',
      ]),
    loading() {
      return this.intervalLoading;
    },
    saving() {
      return this.intervalSaving;
    },
    ready() {
      return !this.isEdit || !this.loading;
    },
    showError() {
      return (path) => ((!this.isTouched(path) && this.getError(path)) ? false : null);
    },
    showSubmit() {
      return this.formDirty && !this.formHasUntouchedErrors;
    },
    isEdit() {
      return this.id || this.id === 0;
    },
  },
  watch: {
    id(oldId, newId) {
      if (newId != null) {
        this.load(newId).then(this.initialize);
      } else {
        this.initialize();
      }
    },
  },
  created() {
    if (this.id != null) {
      this.load({ id: this.id, imlgs: this.imlgs }).then(this.initialize);
    } else {
      this.initialize();
    }
  },

};
</script>
