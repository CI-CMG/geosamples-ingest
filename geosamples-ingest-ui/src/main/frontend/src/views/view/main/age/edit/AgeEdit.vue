<template>
  <div class="m-2">

    <b-breadcrumb :items="[
      { text: 'Geosamples Ingest', to: { name: 'Home' } },
      { text: 'Geologic Age', to: { name: 'AgeList' } },
      { text: 'Edit Geologic Age', active: true },
    ]"/>

    <div v-if="ready">

      <h1 v-if="isEdit" class="text-primary">Edit Geologic Age - {{ getValue('age') }}</h1>
      <h1 v-else class="text-primary">Add New Geologic Age</h1>

      <b-button v-if="isEdit" type="button" variant="danger" @click="showModal" >Delete</b-button>
      <b-modal ref="delete-modal" title="Delete Geologic Age" ok-variant="danger" ok-title="Delete" @ok="doDelete">
        <p class="my-4">Are you sure you want to delete this geologic age?</p>
      </b-modal>

      <b-form @submit.prevent="saveForm" @reset.prevent="reset">

        <b-form-group v-if="!isEdit" label="Geologic Age" :label-for="ageId">
          <b-form-input
            :id="ageId"
            type="text" @blur="() => setTouched({path: 'age', touched: true})"
            :value="getValue('age')"
            @update="(value) => setValue({ path: 'age', value })"
            :state="showError('age')"
          />
          <b-form-invalid-feedback>{{ getError('age') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Geologic Age Code" :label-for="ageCodeId">
          <b-form-input
            :id="ageCodeId"
            type="text" @blur="() => setTouched({path: 'ageCode', touched: true})"
            :value="getValue('ageCode')"
            @update="(value) => setValue({ path: 'ageCode', value })"
            :state="showError('ageCode')"
          />
          <b-form-invalid-feedback>{{ getError('ageCode') }}</b-form-invalid-feedback>
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
      ageId: '',
      ageCodeId: '',
      sourceUriId: '',
    };
  },
  beforeMount() {
    this.ageId = genId();
    this.ageCodeId = genId();
    this.sourceUriId = genId();
  },
  methods: {
    ...mapMutations('ageForm',
      [
        'initialize',
        'setValue',
        'setTouched',
        'setError',
        'deleteFromArray',
        'addToArray',
      ]),
    ...mapActions('age', ['load', 'save', 'delete']),
    ...mapActions('ageForm', ['submit', 'reset']),
    showModal() {
      this.$refs['delete-modal'].show();
    },
    hideModal() {
      this.$refs['delete-modal'].hide();
    },
    saveForm() {
      this.submit()
        .then((provider) => this.save({ provider, id: this.id }))
        .then(() => this.$router.push({ name: 'AgeList' }));
    },
    doDelete() {
      this.delete(this.id).then(() => this.$router.push({ name: 'AgeList' }));
    },
  },

  computed: {
    ...mapState('age', ['deleting', 'loading', 'saving']),
    ...mapGetters('ageForm',
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
