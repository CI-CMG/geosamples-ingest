<template>
  <div class="m-2">

        <b-breadcrumb :items="[
          { text: 'Geosamples Ingest', to: { name: 'Home' } },
          { text: 'Cruise', to: { name: 'CruiseList' } },
          { text: 'Edit Cruise', active: false },
        ]"/>

    <div v-if="ready">

      <h1 v-if="isEdit" class="text-primary">Edit Cruise - {{ getValue('cruiseName') }} {{ getValue('year') }}</h1>
      <h1 v-else class="text-primary">Add New Cruise</h1>

      <b-button v-if="isEdit" type="button" variant="danger" @click="showModal" >Delete</b-button>
      <b-modal ref="delete-modal" title="Delete Cruise" ok-variant="danger" ok-title="Delete" @ok="doDelete">
        <p class="my-4">Are you sure you want to delete this cruise?</p>
      </b-modal>

      <b-form @submit.prevent="saveForm" @reset.prevent="reset">

        <b-form-group label="Cruise Name" :label-for="cruiseNameId">
          <b-form-input
            :id="cruiseNameId"
            type="text" @blur="() => setTouched({path: 'cruiseName', touched: true})"
            :value="getValue('cruiseName')"
            @update="(value) => setValue({ path: 'cruiseName', value })"
            :state="showError('cruiseName')"
          />
          <b-form-invalid-feedback>{{ getError('cruiseName') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Year" :label-for="yearId">
          <b-form-input
            :id="yearId"
            type="text" @blur="() => setTouched({path: 'year', touched: true})"
            :value="getValue('year')"
            @update="(value) => setValue({ path: 'year', value })"
            :state="showError('year')"
          />
          <b-form-invalid-feedback>{{ getError('facility') }}</b-form-invalid-feedback>
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

        <SearchCardCol title="Facility Codes" field="facilityCodes" module="cruiseForm"/>
        <SearchCardCol title="Platforms" field="platforms" module="cruiseForm"/>
        <SearchCardCol title="Legs" field="legs" module="cruiseForm"/>

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
import SearchCardCol from '@/components/SearchCardCol.vue';

export default {
  props: ['id'],
  components: {
    SearchCardCol,
  },
  data() {
    return {
      cruiseNameId: '',
      yearId: '',
      publishId: '',
    };
  },
  beforeMount() {
    this.cruiseNameId = genId();
    this.yearId = genId();
    this.publishId = genId();
  },
  methods: {
    ...mapMutations('cruiseForm',
      [
        'initialize',
        'setValue',
        'setTouched',
        'setError',
        'deleteFromArray',
        'addToArray',
      ]),
    ...mapActions('cruise', ['load', 'save', 'delete']),
    ...mapActions('cruiseForm', ['submit', 'reset']),
    showModal() {
      this.$refs['delete-modal'].show();
    },
    hideModal() {
      this.$refs['delete-modal'].hide();
    },
    saveForm() {
      this.submit()
        .then((provider) => this.save({ provider, id: this.id }))
        .then(() => this.$router.push({ name: 'CruiseList' }));
    },
    doDelete() {
      this.delete(this.id).then(() => this.$router.push({ name: 'CruiseList' }));
    },
  },

  computed: {
    ...mapState('cruise', ['deleting', 'loading', 'saving']),
    ...mapGetters('cruiseForm',
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
