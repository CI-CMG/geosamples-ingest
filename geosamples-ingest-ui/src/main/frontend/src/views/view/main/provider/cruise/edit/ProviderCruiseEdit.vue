<template>
  <div class="m-2">

    <b-breadcrumb :items="[
          { text: 'Geosamples Ingest', to: { name: 'Home' } },
          { text: 'Cruises', to: { name: 'ProviderCruiseList' } },
          { text: 'Edit Cruise', active: false },
        ]"/>

    <div v-if="ready">

      <h1 v-if="isEdit" class="text-primary">Edit Cruise - {{ getValue('cruiseName') }} {{ getValue('year') }}</h1>
      <h1 v-else class="text-primary">Add New Cruise</h1>

      <b-button v-if="isEdit" type="button" variant="danger" class="mb-2" @click="showModal" >Delete</b-button>
      <b-modal ref="delete-modal" title="Delete Cruise" ok-variant="danger" ok-title="Delete" @ok="doDelete">
        <p class="my-4">Are you sure you want to delete this cruise?</p>
      </b-modal>

      <b-form @submit.prevent="saveForm" @reset.prevent="reset">
        <b-card title="Cruise Information" border-variant="dark" bg-variant="light" class="mb-4">
          <b-form-group :label-for="cruiseNameId">
            <template #label>
              Cruise Name<span><strong style="color: red"> *</strong></span>
            </template>
            <b-form-input
              :id="cruiseNameId"
              type="text" @blur="() => setTouched({path: 'cruiseName', touched: true})"
              :value="getValue('cruiseName')"
              @update="(value) => setValue({ path: 'cruiseName', value })"
              :state="showError('cruiseName')"
            />
            <b-form-invalid-feedback>{{ getError('cruiseName') }}</b-form-invalid-feedback>
          </b-form-group>

          <b-form-group :label-for="yearId">
            Year<span><strong style="color: red"> *</strong></span>
            <b-form-input
              :id="yearId"
              type="text" @blur="() => setTouched({path: 'year', touched: true})"
              :value="getValue('year')"
              @update="(value) => setValue({ path: 'year', value })"
              :state="showError('year')"
            />
            <b-form-invalid-feedback>{{ getError('year') }}</b-form-invalid-feedback>
          </b-form-group>

          <b-form-group label="Platforms" :label-for="platformsId">
            <b-form-select
              :id="platformsId"
              @blur="() => setTouched({path: 'platforms', touched: true})"
              :value="selectedPlatforms"
              :options="platformOptions"
              @change="setPlatforms"
              multiple
            />
            <b-form-invalid-feedback>{{ getError('platforms') }}</b-form-invalid-feedback>
          </b-form-group>

          <b-form-group label="Legs" :label-for="legsId">
            <b-list-group>
              <b-list-group-item v-for="(leg, index) in getValue('legs')" :key="index" class="leg-item">
                <b-row>
                  <b-col cols="10">
                    <b-form-input
                      type="text"
                      :value="leg.value"
                      @update="(value) => setLeg(index, value)"
                    />
                  </b-col>
                  <b-col cols="2">
                    <b-button @click="deleteLeg(index)" variant="text" class="text-danger">
                      <b-icon icon="trash" class="mr-2" />Remove
                    </b-button>
                  </b-col>
                </b-row>
              </b-list-group-item>
              <b-list-group-item class="leg-item">
                <b-button @click="addLeg" variant="text" class="text-primary">
                  <b-icon icon="plus" class="mr-2"/>Add Leg
                </b-button>
              </b-list-group-item>
            </b-list-group>
          </b-form-group>
        </b-card>

        <div>
          <b-button v-if="showSubmit" type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">Save</b-button>
          <b-button v-if="formDirty" type="reset" variant="danger" class="mb-2 mr-sm-2 mb-sm-0">Reset</b-button>
        </div>

      </b-form>
    </div>
    <div v-else-if="saving || !ready">
      <b-spinner style="position:absolute; top: 50%; right: 50%"/>
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
      cruiseNameId: '',
      yearId: '',
      platformsId: '',
      legsId: '',
    };
  },
  beforeMount() {
    this.cruiseNameId = genId();
    this.yearId = genId();
    this.platformsId = genId();
    this.legsId = genId();
  },
  methods: {
    ...mapMutations('providerCruiseForm',
      [
        'initialize',
        'setValue',
        'setTouched',
        'setError',
        'deleteFromArray',
        'addToArray',
      ]),
    ...mapActions('providerCruise', ['load', 'save', 'delete', 'loadOptions']),
    ...mapActions('providerCruiseForm', ['submit', 'reset']),
    showModal() {
      this.$refs['delete-modal'].show();
    },
    hideModal() {
      this.$refs['delete-modal'].hide();
    },
    saveForm() {
      this.submit()
        .then((provider) => this.save({ provider, id: this.id }))
        .then(() => this.$router.push({ name: 'ProviderCruiseList' }));
    },
    doDelete() {
      this.delete(this.id).then(() => this.$router.push({ name: 'ProviderCruiseList' }));
    },
    setPlatforms(values) {
      const existing = this.getValue('platforms');
      if (existing) {
        const existingSize = existing.length;
        for (let i = 0; i < existingSize; i += 1) {
          this.deleteFromArray('platforms[0]');
        }
      }
      for (let k = 0; k < values.length; k += 1) {
        this.addToArray({ path: 'platforms', value: values[k] });
      }
    },
    setLeg(index, value) {
      this.setValue({ path: `legs[${index}]`, value });
    },
    deleteLeg(index) {
      this.deleteFromArray(`legs[${index}]`);
    },
    addLeg() {
      this.addToArray({ path: 'legs', value: '' });
    },
  },

  computed: {
    ...mapState('providerCruise', ['deleting', 'loading', 'saving', 'options', 'loadingOptions']),
    ...mapGetters('providerCruiseForm',
      [
        'getValue',
        'formDirty',
        'getError',
        'isTouched',
        'formHasUntouchedErrors',
      ]),
    ready() {
      return !this.loading && !this.loadingOptions && !this.saving;
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
    platformOptions() {
      const { platform: field } = this.options;
      return field || [];
    },
    selectedPlatforms() {
      const ages = this.getValue('platforms');
      if (!ages) {
        return [];
      }
      return ages.map((x) => x.value);
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
    this.loadOptions();
    if (this.id != null) {
      this.load(this.id).then(this.initialize);
    } else {
      this.initialize();
    }
  },

};
</script>

<style scoped>
.leg-item {
  background: none;
  border: none;
}
</style>
