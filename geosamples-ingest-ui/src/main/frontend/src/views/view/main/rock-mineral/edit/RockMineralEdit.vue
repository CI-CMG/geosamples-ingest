<template>
  <div class="m-2">

    <b-breadcrumb :items="[
      { text: 'Geosamples Ingest', to: { name: 'Home' } },
      { text: 'Rock Mineralogy', to: { name: 'RockMineralList' } },
      { text: 'Edit Rock Mineralogy', active: true },
    ]"/>

    <div v-if="ready">

      <h1 v-if="isEdit" class="text-primary">Edit Rock Mineralogy - {{ getValue('rockMineral') }}</h1>
      <h1 v-else class="text-primary">Add New Rock Mineralogy</h1>

      <b-button v-if="isEdit" type="button" variant="danger" @click="showModal" >Delete</b-button>
      <b-modal ref="delete-modal" title="Delete Rock Mineralogy" ok-variant="danger" ok-title="Delete" @ok="doDelete">
        <p class="my-4">Are you sure you want to delete this rock M=mineralogy?</p>
      </b-modal>

      <b-form @submit.prevent="saveForm" @reset.prevent="reset">

        <b-form-group v-if="!isEdit" label="Rock Mineralogy" :label-for="rockMineralId">
          <b-form-input
            :id="rockMineralId"
            type="text" @blur="() => setTouched({path: 'rockMineral', touched: true})"
            :value="getValue('rockMineral')"
            @update="(value) => setValue({ path: 'rockMineral', value })"
            :state="showError('rockMineral')"
          />
          <b-form-invalid-feedback>{{ getError('rockMineral') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Rock Mineralogy Code" :label-for="rockMineralCodeId">
          <b-form-input
            :id="rockMineralCodeId"
            type="text" @blur="() => setTouched({path: 'rockMineralCode', touched: true})"
            :value="getValue('rockMineralCode')"
            @update="(value) => setValue({ path: 'rockMineralCode', value })"
            :state="showError('rockMineralCode')"
          />
          <b-form-invalid-feedback>{{ getError('rockMineralCode') }}</b-form-invalid-feedback>
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
      rockMineralId: '',
      rockMineralCodeId: '',
      sourceUriId: '',
    };
  },
  beforeMount() {
    this.rockMineralId = genId();
    this.rockMineralCodeId = genId();
    this.sourceUriId = genId();
  },
  methods: {
    ...mapMutations('rockMineralForm',
      [
        'initialize',
        'setValue',
        'setTouched',
        'setError',
        'deleteFromArray',
        'addToArray',
      ]),
    ...mapActions('rockMineral', ['load', 'save', 'delete']),
    ...mapActions('rockMineralForm', ['submit', 'reset']),
    showModal() {
      this.$refs['delete-modal'].show();
    },
    hideModal() {
      this.$refs['delete-modal'].hide();
    },
    saveForm() {
      this.submit()
        .then((provider) => this.save({ provider, id: this.id }))
        .then(() => this.$router.push({ name: 'RockMineralList' }));
    },
    doDelete() {
      this.delete(this.id).then(() => this.$router.push({ name: 'RockMineralList' }));
    },
  },

  computed: {
    ...mapState('rockMineral', ['deleting', 'loading', 'saving']),
    ...mapGetters('rockMineralForm',
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
