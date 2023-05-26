<template>
  <div class="m-2">

    <b-breadcrumb :items=" isEdit ? [
      { text: 'Geosamples Ingest', to: { name: 'Home' } },
      { text: 'Ships/Platforms', to: { name: 'ProviderPlatformList' } },
      { text: 'Edit Ship/Platform', active: true },
    ] : [
      { text: 'Geosamples Ingest', to: { name: 'Home' } },
      { text: 'Ships/Platforms', to: { name: 'ProviderPlatformList' } },
      { text: 'Add Ship/Platform', active: true },
    ]"/>

    <div v-if="ready">

      <h1 v-if="isEdit" class="text-primary">Edit Ship/Platform - {{ getValue('platform') }}</h1>
      <h1 v-else class="text-primary">Add New Ship/Platform</h1>

      <b-form @submit.prevent="saveForm" @reset.prevent="reset">
        <b-card border-variant="dark" bg-variant="light" title="Ship/Platform Information" class="mb-4">
          <b-form-group label="Ship/Platform" :label-for="platformId">
            <b-form-input
              required
              :id="platformId"
              type="text" @blur="() => setTouched({path: 'platform', touched: true})"
              :value="getValue('platform')"
              @update="(value) => setValue({ path: 'platform', value })"
              :state="showError('platform')"
            />
            <b-form-invalid-feedback>{{ getError('platform') }}</b-form-invalid-feedback>
          </b-form-group>
          <b-form-group label="Master ID" :label-for="masterIdId">
            <b-form-input
              :id="masterIdId"
              type="text" @blur="() => setTouched({path: 'masterId', touched: true})"
              :value="getValue('masterId')"
              @update="(value) => setValue({ path: 'masterId', value })"
              :state="showError('masterId')"
            />
            <b-form-invalid-feedback>{{ getError('masterId') }}</b-form-invalid-feedback>
          </b-form-group>
          <b-form-group label="Prefix" :label-for="prefixId">
            <b-form-input
              :id="prefixId"
              type="text" @blur="() => setTouched({path: 'prefix', touched: true})"
              :value="getValue('prefix')"
              @update="(value) => setValue({ path: 'prefix', value })"
              :state="showError('prefix')"
            />
            <b-form-invalid-feedback>{{ getError('prefix') }}</b-form-invalid-feedback>
          </b-form-group>
          <b-form-group label="ICES Code" :label-for="icesCodeId">
            <b-form-input
              :id="icesCodeId"
              type="text" @blur="() => setTouched({path: 'icesCode', touched: true})"
              :value="getValue('icesCode')"
              @update="(value) => setValue({ path: 'icesCode', value })"
              :state="showError('icesCode')"
            />
            <b-form-invalid-feedback>{{ getError('icesCode') }}</b-form-invalid-feedback>
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
        </b-card>
        <div>
          <b-button v-if="showSubmit" type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">
            <b-icon icon="check" class="mr-2"></b-icon> Submit
          </b-button>
          <b-button v-if="formDirty" type="reset" variant="secondary" class="mb-2 mr-sm-2 mb-sm-0">
            <b-icon icon="arrow-counterclockwise" class="mr-2"></b-icon> Reset
          </b-button>
          <b-button v-if="isEdit" variant="danger" @click="showModal" class="mb-2 mr-sm-2 mb-sm-0">
            <b-icon icon="trash" class="mr-2"></b-icon> Delete
          </b-button>
          <b-modal ref="delete-modal" title="Delete Ship/Platform" ok-variant="danger" ok-title="Delete" @ok="doDelete">
            <p class="my-4">Are you sure you want to delete this ship/platform?</p>
          </b-modal>
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
      platformId: '',
      masterIdId: '',
      prefixId: '',
      icesCodeId: '',
      sourceUriId: '',
    };
  },
  beforeMount() {
    this.platformId = genId();
    this.masterIdId = genId();
    this.prefixId = genId();
    this.icesCodeId = genId();
    this.sourceUriId = genId();
  },
  methods: {
    ...mapMutations('platformForm',
      [
        'initialize',
        'setValue',
        'setTouched',
        'setError',
        'deleteFromArray',
        'addToArray',
      ]),
    ...mapActions('providerPlatform', ['load', 'save', 'delete']),
    ...mapActions('platformForm', ['submit', 'reset']),
    showModal() {
      this.$refs['delete-modal'].show();
    },
    hideModal() {
      this.$refs['delete-modal'].hide();
    },
    saveForm() {
      this.submit()
        .then((provider) => this.save({ provider, id: this.id }))
        .then(() => this.$router.push({ name: 'ProviderPlatformList' }));
    },
    doDelete() {
      this.delete(this.id).then(() => this.$router.push({ name: 'ProviderPlatformList' }));
    },
  },

  computed: {
    ...mapState('providerPlatform', ['deleting', 'loading', 'saving']),
    ...mapGetters('platformForm',
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
