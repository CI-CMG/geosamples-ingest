<template>
  <div class="m-2">

    <b-breadcrumb :items="[
      { text: 'Geosamples Ingest', to: { name: 'Home' } },
      { text: 'Sample + Interval', to: { name: 'IntervalList' } },
      { text: 'Edit Interval', active: true },
    ]"/>

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

        <b-form-group label="Primary Lithologic Composition Code" :label-for="lithCode1Id">
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

        <b-form-group label="Secondary Lithologic Composition Code" :label-for="lithCode2Id">
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

        <b-form-group label="Other Component Code 1" :label-for="compCode1Id">
          <b-form-input
            :id="compCode1Id"
            type="text" @blur="() => setTouched({path: 'compCode1', touched: true})"
            :value="getValue('compCode1')"
            @update="(value) => setValue({ path: 'compCode1', value })"
            :state="showError('compCode1')"
          />
          <b-form-invalid-feedback>{{ getError('compCode1') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Other Component Code 2" :label-for="compCode2Id">
          <b-form-input
            :id="compCode2Id"
            type="text" @blur="() => setTouched({path: 'compCode2', touched: true})"
            :value="getValue('compCode2')"
            @update="(value) => setValue({ path: 'compCode2', value })"
            :state="showError('compCode2')"
          />
          <b-form-invalid-feedback>{{ getError('compCode2') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Other Component Code 3" :label-for="compCode3Id">
          <b-form-input
            :id="compCode3Id"
            type="text" @blur="() => setTouched({path: 'compCode3', touched: true})"
            :value="getValue('compCode3')"
            @update="(value) => setValue({ path: 'compCode3', value })"
            :state="showError('compCode3')"
          />
          <b-form-invalid-feedback>{{ getError('compCode3') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Other Component Code 4" :label-for="compCode4Id">
          <b-form-input
            :id="compCode4Id"
            type="text" @blur="() => setTouched({path: 'compCode4', touched: true})"
            :value="getValue('compCode4')"
            @update="(value) => setValue({ path: 'compCode4', value })"
            :state="showError('compCode4')"
          />
          <b-form-invalid-feedback>{{ getError('compCode4') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Other Component Code 5" :label-for="compCode5Id">
          <b-form-input
            :id="compCode5Id"
            type="text" @blur="() => setTouched({path: 'compCode5', touched: true})"
            :value="getValue('compCode5')"
            @update="(value) => setValue({ path: 'compCode5', value })"
            :state="showError('compCode5')"
          />
          <b-form-invalid-feedback>{{ getError('compCode5') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Other Component Code 6" :label-for="compCode6Id">
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

        <b-form-group label="Geologic Age Codes" :label-for="ageCodeId">
          <b-spinner v-if="!optionsAgeCode.length"/>
          <div v-else>
            <div class="geo-search-card-list">
              <RemovableSelectField
                v-for="(value, index) in getValue('ageCodes')" :key="`v${index}`"
                :onBlur="() => setTouched({path: `ageCodes[${index}]`, touched: true})"
                :value="getValue(`ageCodes[${index}]`)"
                :onUpdate="(value) => setValue({ path: `ageCodes[${index}]`, value })"
                :state="showError(`ageCodes[${index}]`)"
                :error="getError(`ageCodes[${index}]`)"
                :onRemove="() => deleteFromArray(`ageCodes[${index}]`)"
                :options="optionsAgeCode"
              />
            </div>
            <b-button variant="outline-primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3" @click="addToArray({ path: 'ageCodes' })"><b-icon icon="plus-circle" class="mr-2"/></b-button>
          </div>
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

        <b-form-group label="Munsell Color Code" :label-for="munsellCodeId">
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
import RemovableSelectField from '@/components/RemovableSelectField.vue';

export default {
  components: { RemovableSelectField },
  props: ['id', 'imlgs'],
  data() {
    return {
      intervalId: '',
      depthTopId: '',
      depthBotId: '',
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
      weightId: '',
      rockLithCodeId: '',
      rockMinCodeId: '',
      weathMetaCodeId: '',
      remarkCodeId: '',
      munsellCodeId: '',
      exhaustedId: '',
      photoLinkId: '',
      intCommentsId: '',
      publishId: '',
      igsnId: '',
    };
  },
  beforeMount() {
    this.intervalId = genId();
    this.depthTopId = genId();
    this.depthBotId = genId();
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
    this.weightId = genId();
    this.rockLithCodeId = genId();
    this.rockMinCodeId = genId();
    this.weathMetaCodeId = genId();
    this.remarkCodeId = genId();
    this.munsellCodeId = genId();
    this.exhaustedId = genId();
    this.photoLinkId = genId();
    this.intCommentsId = genId();
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
    ...mapActions('interval', ['loadInterval', 'saveInterval', 'deleteInterval', 'loadOptions']),
    ...mapActions('intervalForm', ['submit', 'reset']),
    load({ id }) {
      return this.loadInterval({ id });
    },
    save(params) {
      return this.saveInterval(params);
    },
    delete() {
      return this.deleteInterval({ id: this.id });
    },
    saveForm() {
      this.submit()
        .then((provider) => this.save({ provider, id: this.id }))
        .then(() => this.$router.push({ name: 'IntervalList' }));
    },
    doDelete() {
      this.delete().then(() => this.$router.push({ name: 'IntervalList' }));
    },
  },

  computed: {
    ...mapState('interval', ['intervalLoading', 'intervalSaving', 'options']),
    ...mapGetters('intervalForm',
      [
        'getValue',
        'formDirty',
        'getError',
        'isTouched',
        'formHasUntouchedErrors',
      ]),
    optionsAgeCode() {
      const { ageCode: field } = this.options;
      return field || [];
    },
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
      this.load({ id: this.id }).then(this.initialize);
    } else {
      this.initialize();
    }
    this.loadOptions();
  },

};
</script>
