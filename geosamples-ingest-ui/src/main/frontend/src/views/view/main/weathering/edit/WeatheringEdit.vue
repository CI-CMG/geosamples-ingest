<template>
  <div class="m-2">

<!--    <b-breadcrumb :items="items"/>-->

    <div v-if="ready">

      <h1 v-if="isEdit" class="text-primary">Edit Weathering/Metamorphism - {{ getValue('weathering') }}</h1>
      <h1 v-else class="text-primary">Add New Weathering/Metamorphism</h1>

      <b-button v-if="isEdit" type="button" variant="danger" @click="doDelete" >Delete</b-button>

      <b-form @submit.prevent="saveForm" @reset.prevent="reset">

        <b-form-group v-if="!isEdit" label="Weathering/Metamorphism" :label-for="weatheringId">
          <b-form-input
            :id="weatheringId"
            type="text" @blur="() => setTouched({path: 'weathering', touched: true})"
            :value="getValue('weathering')"
            @update="(value) => setValue({ path: 'weathering', value })"
            :state="showError('weathering')"
          />
          <b-form-invalid-feedback>{{ getError('weathering') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Weathering/Metamorphism Code" :label-for="weatheringCodeId">
          <b-form-input
            :id="weatheringCodeId"
            type="text" @blur="() => setTouched({path: 'weatheringCode', touched: true})"
            :value="getValue('weatheringCode')"
            @update="(value) => setValue({ path: 'weatheringCode', value })"
            :state="showError('weatheringCode')"
          />
          <b-form-invalid-feedback>{{ getError('weatheringCode') }}</b-form-invalid-feedback>
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
      weatheringId: '',
      weatheringCodeId: '',
      sourceUriId: '',
    };
  },
  beforeMount() {
    this.weatheringId = genId();
    this.weatheringCodeId = genId();
    this.sourceUriId = genId();
  },
  methods: {
    ...mapMutations('weatheringForm',
      [
        'initialize',
        'setValue',
        'setTouched',
        'setError',
        'deleteFromArray',
        'addToArray',
      ]),
    ...mapActions('weathering', ['load', 'save', 'delete']),
    ...mapActions('weatheringForm', ['submit', 'reset']),

    saveForm() {
      this.submit()
        .then((provider) => this.save({ provider, id: this.id }))
        .then(() => this.$router.push({ name: 'WeatheringList' }));
    },
    doDelete() {
      this.delete(this.id).then(() => this.$router.push({ name: 'WeatheringList' }));
    },
  },

  computed: {
    ...mapState('weathering', ['deleting', 'loading', 'saving']),
    ...mapGetters('weatheringForm',
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
