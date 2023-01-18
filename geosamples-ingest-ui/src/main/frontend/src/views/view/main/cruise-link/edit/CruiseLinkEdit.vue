<template>
  <div class="m-2">

    <b-breadcrumb :items="[
      { text: 'Geosamples Ingest', to: { name: 'Home' } },
      { text: 'Cruise Link', to: { name: 'CruiseLinkList' } },
      { text: 'Edit Cruise Link', active: true },
    ]"/>

    <div v-if="ready">

      <h1 v-if="isEdit" class="text-primary">Edit Cruise Link - {{ getValue('id') }}</h1>
      <h1 v-else class="text-primary">Add New Cruise Link</h1>

      <b-button v-if="isEdit" type="button" variant="danger" @click="showModal" >Delete</b-button>
      <b-modal ref="delete-modal" title="Delete Cruise Link" ok-variant="danger" ok-title="Delete" @ok="doDelete">
        <p class="my-4">Are you sure you want to delete this Cruise Link?</p>
      </b-modal>

      <b-form @submit.prevent="saveForm" @reset.prevent="reset">

        <b-form-group label="Cruise Name" :label-for="cruiseNameId">
          <b-form-input
            :id="cruiseNameId"
            type="text" @blur="() => setTouched({path: 'cruiseNameId', touched: true})"
            :value="getValue('cruiseName')"
            @update="(value) => setValue({ path: 'cruiseName', value })"
            :state="showError('cruiseName')"
          />
          <b-form-invalid-feedback>{{ getError('cruiseName') }}</b-form-invalid-feedback>
        </b-form-group>
        <b-form-group label="Cruise Year" :label-for="cruiseYearId">
          <b-form-input
            :id="cruiseYearId"
            type="text" @blur="() => setTouched({path: 'cruiseYearId', touched: true})"
            :value="getValue('cruiseYear')"
            @update="(value) => setValue({ path: 'cruiseYear', value })"
            :state="showError('cruiseYear')"
          />
          <b-form-invalid-feedback>{{ getError('cruiseYear') }}</b-form-invalid-feedback>
        </b-form-group>
        <b-form-group label="Platform" :label-for="platformId">
          <b-form-input
            :id="platformId"
            type="text" @blur="() => setTouched({path: 'platformId', touched: true})"
            :value="getValue('platform')"
            @update="(value) => setValue({ path: 'platform', value })"
            :state="showError('leg')"
          />
          <b-form-invalid-feedback>{{ getError('platform') }}</b-form-invalid-feedback>
        </b-form-group>
        <b-form-group label="Leg Name" :label-for="legNameId">
          <b-form-input
            :id="legNameId"
            type="text" @blur="() => setTouched({path: 'legNameId', touched: true})"
            :value="getValue('legName')"
            @update="(value) => setValue({ path: 'legName', value })"
            :state="showError('legName')"
          />
          <b-form-invalid-feedback>{{ getError('legName') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Data Link" :label-for="dataLinkId">
          <b-form-input
            :id="dataLinkId"
            type="text" @blur="() => setTouched({path: 'dataLink', touched: true})"
            :value="getValue('dataLink')"
            @update="(value) => setValue({ path: 'dataLink', value })"
            :state="showError('dataLink')"
          />
          <b-form-invalid-feedback>{{ getError('dataLink') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Link Level" :label-for="linkLevelId">
          <b-form-input
            :id="linkLevelId"
            type="text" @blur="() => setTouched({path: 'linkLevel', touched: true})"
            :value="getValue('linkLevel')"
            @update="(value) => setValue({ path: 'linkLevel', value })"
            :state="showError('linkLevel')"
          />
          <b-form-invalid-feedback>{{ getError('linkLevel') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Link Source" :label-for="linkSourceId">
          <b-form-input
            :id="linkSourceId"
            type="text" @blur="() => setTouched({path: 'linkSource', touched: true})"
            :value="getValue('linkSource')"
            @update="(value) => setValue({ path: 'linkSource', value })"
            :state="showError('linkSource')"
          />
          <b-form-invalid-feedback>{{ getError('linkSource') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Link Type" :label-for="linkTypeId">
          <b-form-input
            :id="linkTypeId"
            type="text" @blur="() => setTouched({path: 'linkType', touched: true})"
            :value="getValue('linkType')"
            @update="(value) => setValue({ path: 'linkType', value })"
            :state="showError('linkType')"
          />
          <b-form-invalid-feedback>{{ getError('linkType') }}</b-form-invalid-feedback>
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
      cruisePlatformId: '',
      cruiseNameId: '',
      cruiseYearId: '',
      platformId: '',
      legId: '',
      legNameId: '',
      dataLinkId: '',
      linkLevelId: '',
      linkSourceId: '',
      linkTypeId: '',
      publishId: '',
    };
  },
  beforeMount() {
    this.cruisePlatformId = genId();
    this.cruiseNameId = genId();
    this.cruiseYearId = genId();
    this.platformId = genId();
    this.legId = genId();
    this.legNameId = genId();
    this.dataLinkId = genId();
    this.linkLevelId = genId();
    this.linkSourceId = genId();
    this.linkTypeId = genId();
    this.publishId = genId();
  },
  methods: {
    ...mapMutations('cruiseLinkForm',
      [
        'initialize',
        'setValue',
        'setTouched',
        'setError',
        'deleteFromArray',
        'addToArray',
      ]),
    ...mapActions('cruiseLink', ['load', 'save', 'delete']),
    ...mapActions('cruiseLinkForm', ['submit', 'reset']),
    showModal() {
      this.$refs['delete-modal'].show();
    },
    hideModal() {
      this.$refs['delete-modal'].hide();
    },
    saveForm() {
      this.submit()
        .then((provider) => this.save({ provider, id: this.id }))
        .then(() => this.$router.push({ name: 'CruiseLinkList' }));
    },
    doDelete() {
      this.delete(this.id).then(() => this.$router.push({ name: 'CruiseLinkList' }));
    },
  },

  computed: {
    ...mapState('cruiseLink', ['deleting', 'loading', 'saving']),
    ...mapGetters('cruiseLinkForm',
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
