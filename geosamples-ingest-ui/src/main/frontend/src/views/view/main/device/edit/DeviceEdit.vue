<template>
  <div class="m-2">

    <b-breadcrumb :items="[
      { text: 'Geosamples Ingest', to: { name: 'Home' } },
      { text: 'Sampling Device', to: { name: 'DeviceList' } },
      { text: 'Edit Sampling Device', active: true },
    ]"/>

    <div v-if="ready">

      <h1 v-if="isEdit" class="text-primary">Edit Sampling Device - {{ getValue('device') }}</h1>
      <h1 v-else class="text-primary">Add New Sampling Device</h1>

      <b-button v-if="isEdit" type="button" variant="danger" @click="showModal" >Delete</b-button>
      <b-modal ref="delete-modal" title="Delete Sampling Device" ok-variant="danger" ok-title="Delete" @ok="doDelete">
        <p class="my-4">Are you sure you want to delete this sampling device?</p>
      </b-modal>

      <b-form @submit.prevent="saveForm" @reset.prevent="reset">

        <b-form-group v-if="!isEdit" label="Sampling Device" :label-for="deviceId">
          <b-form-input
            :id="deviceId"
            type="text" @blur="() => setTouched({path: 'device', touched: true})"
            :value="getValue('device')"
            @update="(value) => setValue({ path: 'device', value })"
            :state="showError('device')"
          />
          <b-form-invalid-feedback>{{ getError('device') }}</b-form-invalid-feedback>
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

        <b-form-group label="Source URI" :label-for="sourceUriId">
          <b-form-input
            :id="sourceUriId"
            type="text" @blur="() => setTouched({path: 'sourceUri', touched: true})"
            :value="getValue('sourceUri')"
            @update="(value) => setValue({ path: 'sourceUri', value })"
            :state="showError('sourceUri')"
          />
          <b-form-invalid-feedback>{{ getError('sourceUri') }}</b-form-invalid-feedback>
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
      deviceId: '',
      deviceCodeId: '',
      sourceUriId: '',
    };
  },
  beforeMount() {
    this.deviceId = genId();
    this.deviceCodeId = genId();
    this.sourceUriId = genId();
  },
  methods: {
    ...mapMutations('deviceForm',
      [
        'initialize',
        'setValue',
        'setTouched',
        'setError',
        'deleteFromArray',
        'addToArray',
      ]),
    ...mapActions('device', ['load', 'save', 'delete']),
    ...mapActions('deviceForm', ['submit', 'reset']),
    showModal() {
      this.$refs['delete-modal'].show();
    },
    hideModal() {
      this.$refs['delete-modal'].hide();
    },
    saveForm() {
      this.submit()
        .then((provider) => this.save({ provider, id: this.id }))
        .then(() => this.$router.push({ name: 'DeviceList' }));
    },
    doDelete() {
      this.delete(this.id).then(() => this.$router.push({ name: 'DeviceList' }));
    },
  },

  computed: {
    ...mapState('device', ['deleting', 'loading', 'saving']),
    ...mapGetters('deviceForm',
      [
        'getValue',
        'formDirty',
        'getError',
        'isTouched',
        'formHasUntouchedErrors',
      ]),
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
