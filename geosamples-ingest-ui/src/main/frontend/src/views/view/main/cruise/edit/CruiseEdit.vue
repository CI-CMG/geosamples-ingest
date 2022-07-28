<template>
  <div class="m-2">

<!--    <b-breadcrumb :items="items"/>-->

    <div v-if="ready">

      <h1 v-if="isEdit" class="text-primary">Edit Sample - {{ getValue('imlgs') }}</h1>

      <b-button type="button" variant="danger" @click="doDelete" >Delete</b-button>

      <b-form @submit.prevent="saveForm" @reset.prevent="reset">

        <b-form-group label="Cruise" :label-for="cruiseId">
          <b-form-input
            :id="cruiseId"
            type="text" @blur="() => setTouched({path: 'cruise', touched: true})"
            :value="getValue('cruise')"
            @update="(value) => setValue({ path: 'cruise', value })"
            :state="showError('cruise')"
          />
          <b-form-invalid-feedback>{{ getError('cruise') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Sample" :label-for="sampleId">
          <b-form-input
            :id="sampleId"
            type="text" @blur="() => setTouched({path: 'sample', touched: true})"
            :value="getValue('sample')"
            @update="(value) => setValue({ path: 'sample', value })"
            :state="showError('sample')"
          />
          <b-form-invalid-feedback>{{ getError('sample') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Facility Code" :label-for="facilityCodeId">
          <b-form-input
            :id="facilityCodeId"
            type="text" @blur="() => setTouched({path: 'facilityCode', touched: true})"
            :value="getValue('facilityCode')"
            @update="(value) => setValue({ path: 'facilityCode', value })"
            :state="showError('facilityCode')"
          />
          <b-form-invalid-feedback>{{ getError('facilityCode') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Ship/Platform" :label-for="platformId">
          <b-form-input
            :id="platformId"
            type="text" @blur="() => setTouched({path: 'platform', touched: true})"
            :value="getValue('platform')"
            @update="(value) => setValue({ path: 'platform', value })"
            :state="showError('platform')"
          />
          <b-form-invalid-feedback>{{ getError('platform') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Sampling Device Code" :label-for="deviceCodeId">
          <b-form-input
            :id="deviceCodeId"
            type="text" @blur="() => setTouched({path: 'deviceCode', touched: true})"
            :value="getValue('deviceCode')"
            @update="(value) => setValue({ path: 'deviceCode', value })"
            :state="showError('deviceCode')"
          />
          <b-form-invalid-feedback>{{ getError('deviceCode') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Ship Code" :label-for="shipCodeId">
          <b-form-input
            :id="shipCodeId"
            type="text" @blur="() => setTouched({path: 'shipCode', touched: true})"
            :value="getValue('shipCode')"
            @update="(value) => setValue({ path: 'shipCode', value })"
            :state="showError('shipCode')"
          />
          <b-form-invalid-feedback>{{ getError('shipCode') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Begin Date" :label-for="beginDateId">
          <b-form-input
            :id="beginDateId"
            type="text" @blur="() => setTouched({path: 'beginDate', touched: true})"
            :value="getValue('beginDate')"
            @update="(value) => setValue({ path: 'beginDate', value })"
            :state="showError('beginDate')"
          />
          <b-form-invalid-feedback>{{ getError('beginDate') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="End Date" :label-for="endDateId">
          <b-form-input
            :id="endDateId"
            type="text" @blur="() => setTouched({path: 'endDate', touched: true})"
            :value="getValue('endDate')"
            @update="(value) => setValue({ path: 'endDate', value })"
            :state="showError('endDate')"
          />
          <b-form-invalid-feedback>{{ getError('endDate') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Latitude" :label-for="latId">
          <b-form-input
            :id="latId"
            type="text" @blur="() => setTouched({path: 'lat', touched: true})"
            :value="getValue('lat')"
            @update="(value) => setValue({ path: 'lat', value })"
            :state="showError('lat')"
          />
          <b-form-invalid-feedback>{{ getError('lat') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="End Latitude" :label-for="endLatId">
          <b-form-input
            :id="endLatId"
            type="text" @blur="() => setTouched({path: 'endLat', touched: true})"
            :value="getValue('endLat')"
            @update="(value) => setValue({ path: 'endLat', value })"
            :state="showError('endLat')"
          />
          <b-form-invalid-feedback>{{ getError('endLat') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Longitude" :label-for="lonId">
          <b-form-input
            :id="lonId"
            type="text" @blur="() => setTouched({path: 'lon', touched: true})"
            :value="getValue('lon')"
            @update="(value) => setValue({ path: 'lon', value })"
            :state="showError('lon')"
          />
          <b-form-invalid-feedback>{{ getError('lon') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="End Longitude" :label-for="endLonId">
          <b-form-input
            :id="endLonId"
            type="text" @blur="() => setTouched({path: 'endLon', touched: true})"
            :value="getValue('endLon')"
            @update="(value) => setValue({ path: 'endLon', value })"
            :state="showError('endLon')"
          />
          <b-form-invalid-feedback>{{ getError('endLon') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Lat / Lon Original Type" :label-for="latLonOrigId">
          <b-form-input
            :id="latLonOrigId"
            type="text" @blur="() => setTouched({path: 'latLonOrig', touched: true})"
            :value="getValue('latLonOrig')"
            @update="(value) => setValue({ path: 'latLonOrig', value })"
            :state="showError('latLonOrig')"
          />
          <b-form-invalid-feedback>{{ getError('latLonOrig') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Water Depth" :label-for="waterDepthId">
          <b-form-input
            :id="waterDepthId"
            type="text" @blur="() => setTouched({path: 'waterDepth', touched: true})"
            :value="getValue('waterDepth')"
            @update="(value) => setValue({ path: 'waterDepth', value })"
            :state="showError('waterDepth')"
          />
          <b-form-invalid-feedback>{{ getError('waterDepth') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="End Water Depth" :label-for="endWaterDepthId">
          <b-form-input
            :id="endWaterDepthId"
            type="text" @blur="() => setTouched({path: 'endWaterDepth', touched: true})"
            :value="getValue('endWaterDepth')"
            @update="(value) => setValue({ path: 'endWaterDepth', value })"
            :state="showError('endWaterDepth')"
          />
          <b-form-invalid-feedback>{{ getError('endWaterDepth') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Storage Method Code" :label-for="storageMethCodeId">
          <b-form-input
            :id="storageMethCodeId"
            type="text" @blur="() => setTouched({path: 'storageMethCode', touched: true})"
            :value="getValue('storageMethCode')"
            @update="(value) => setValue({ path: 'storageMethCode', value })"
            :state="showError('storageMethCode')"
          />
          <b-form-invalid-feedback>{{ getError('storageMethCode') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Cored Length" :label-for="coredLengthId">
          <b-form-input
            :id="coredLengthId"
            type="text" @blur="() => setTouched({path: 'coredLength', touched: true})"
            :value="getValue('coredLength')"
            @update="(value) => setValue({ path: 'coredLength', value })"
            :state="showError('coredLength')"
          />
          <b-form-invalid-feedback>{{ getError('coredLength') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Cored Diameter" :label-for="coredDiamId">
          <b-form-input
            :id="coredDiamId"
            type="text" @blur="() => setTouched({path: 'coredDiam', touched: true})"
            :value="getValue('coredDiam')"
            @update="(value) => setValue({ path: 'coredDiam', value })"
            :state="showError('coredDiam')"
          />
          <b-form-invalid-feedback>{{ getError('coredDiam') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Principal Investigator" :label-for="piId">
          <b-form-input
            :id="piId"
            type="text" @blur="() => setTouched({path: 'pi', touched: true})"
            :value="getValue('pi')"
            @update="(value) => setValue({ path: 'pi', value })"
            :state="showError('pi')"
          />
          <b-form-invalid-feedback>{{ getError('pi') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Physiographic Province Code" :label-for="provinceCodeId">
          <b-form-input
            :id="provinceCodeId"
            type="text" @blur="() => setTouched({path: 'provinceCode', touched: true})"
            :value="getValue('provinceCode')"
            @update="(value) => setValue({ path: 'provinceCode', value })"
            :state="showError('provinceCode')"
          />
          <b-form-invalid-feedback>{{ getError('provinceCode') }}</b-form-invalid-feedback>
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

        <b-form-group label="Other Link" :label-for="otherLinkId">
          <b-form-input
            :id="otherLinkId"
            type="text" @blur="() => setTouched({path: 'otherLink', touched: true})"
            :value="getValue('otherLink')"
            @update="(value) => setValue({ path: 'otherLink', value })"
            :state="showError('otherLink')"
          />
          <b-form-invalid-feedback>{{ getError('otherLink') }}</b-form-invalid-feedback>
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

        <b-form-group label="Show Sample" :label-for="showSamplId">
          <b-form-input
            :id="showSamplId"
            type="text" @blur="() => setTouched({path: 'showSampl', touched: true})"
            :value="getValue('showSampl')"
            @update="(value) => setValue({ path: 'showSampl', value })"
            :state="showError('showSampl')"
          />
          <b-form-invalid-feedback>{{ getError('showSampl') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Leg" :label-for="legId">
          <b-form-input
            :id="legId"
            type="text" @blur="() => setTouched({path: 'leg', touched: true})"
            :value="getValue('leg')"
            @update="(value) => setValue({ path: 'leg', value })"
            :state="showError('leg')"
          />
          <b-form-invalid-feedback>{{ getError('leg') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Comments" :label-for="sampleCommentsId">
          <b-form-textarea
            :id="sampleCommentsId"
            @blur="() => setTouched({path: 'sampleComments', touched: true})"
            :value="getValue('sampleComments')"
            @update="(value) => setValue({ path: 'sampleComments', value })"
            :state="showError('sampleComments')"
          />
          <b-form-invalid-feedback>{{ getError('sampleComments') }}</b-form-invalid-feedback>
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
  props: ['id'],
  data() {
    return {
      cruiseId: '',
      sampleId: '',
      facilityCodeId: '',
      platformId: '',
      deviceCodeId: '',
      shipCodeId: '',
      beginDateId: '',
      endDateId: '',
      latId: '',
      endLatId: '',
      lonId: '',
      endLonId: '',
      latLonOrigId: '',
      waterDepthId: '',
      endWaterDepthId: '',
      storageMethCodeId: '',
      coredLengthId: '',
      coredDiamId: '',
      piId: '',
      provinceCodeId: '',
      lakeId: '',
      otherLinkId: '',
      igsnId: '',
      legId: '',
      sampleCommentsId: '',
      publishId: '',
      showSamplId: '',
    };
  },
  beforeMount() {
    this.cruiseId = genId();
    this.sampleId = genId();
    this.facilityCodeId = genId();
    this.platformId = genId();
    this.deviceCodeId = genId();
    this.shipCodeId = genId();
    this.beginDateId = genId();
    this.endDateId = genId();
    this.latId = genId();
    this.endLatId = genId();
    this.lonId = genId();
    this.endLonId = genId();
    this.latLonOrigId = genId();
    this.waterDepthId = genId();
    this.endWaterDepthId = genId();
    this.storageMethCodeId = genId();
    this.coredLengthId = genId();
    this.coredDiamId = genId();
    this.piId = genId();
    this.provinceCodeId = genId();
    this.lakeId = genId();
    this.otherLinkId = genId();
    this.igsnId = genId();
    this.legId = genId();
    this.sampleCommentsId = genId();
    this.publishId = genId();
    this.showSamplId = genId();
  },
  methods: {
    ...mapMutations('sampleForm',
      [
        'initialize',
        'setValue',
        'setTouched',
        'setError',
        'deleteFromArray',
        'addToArray',
      ]),
    ...mapActions('interval', ['loadSample', 'saveSample', 'deleteSample']),
    ...mapActions('sampleForm', ['submit', 'reset']),
    load(id) {
      return this.loadSample(id);
    },
    save(params) {
      return this.saveSample(params);
    },
    delete(id) {
      return this.deleteSample(id);
    },
    saveForm() {
      this.submit()
        .then((provider) => this.save({ provider, id: this.id }))
        .then(() => this.$router.push({ name: 'IntervalList' }));
    },
    doDelete() {
      this.delete(this.id).then(() => this.$router.push({ name: 'IntervalList' }));
    },
  },

  computed: {
    ...mapState('interval', ['sampleLoading', 'sampleSaving']),
    ...mapGetters('sampleForm',
      [
        'getValue',
        'formDirty',
        'getError',
        'isTouched',
        'formHasUntouchedErrors',
      ]),
    loading() {
      return this.sampleLoading;
    },
    saving() {
      return this.sampleSaving;
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
      this.load(this.id).then(this.initialize);
    } else {
      this.initialize();
    }
  },

};
</script>
