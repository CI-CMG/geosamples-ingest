<template>
  <div class="m-2">

    <b-breadcrumb :items="isEdit ? [
          { text: 'Geosamples Ingest', to: { name: 'Home' } },
          { text: 'Cruises', to: { name: 'ProviderCruiseList' } },
          { text: 'Edit Cruise', active: false },
        ] : [
          { text: 'Geosamples Ingest', to: { name: 'Home' } },
          { text: 'Cruises', to: { name: 'ProviderCruiseList' } },
          { text: 'Add Cruise', active: false },
        ]"/>

    <div>

      <h1 v-if="isEdit" class="text-primary">Edit Cruise - {{ getValue('cruiseName') }} {{ getValue('year') }}</h1>
      <h1 v-else class="text-primary">Add New Cruise</h1>

      <b-form @submit.prevent="saveForm" @reset.prevent="reset">
        <b-card title="Cruise Information" border-variant="dark" bg-variant="light" class="mb-4">
          <b-form-group :label-for="cruiseNameId">
            <template #label>
              Cruise Name<span><strong style="color: red"> *</strong></span>
            </template>
            <b-form-input
              required
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
              required
              :id="yearId"
              type="text" @blur="() => setTouched({path: 'year', touched: true})"
              :value="getValue('year')"
              @update="(value) => setValue({ path: 'year', value })"
              :state="showError('year')"
            />
            <b-form-invalid-feedback>{{ getError('year') }}</b-form-invalid-feedback>
          </b-form-group>

          <b-form-group label="Platforms" :label-for="platformsId">
            <div v-if="!showSkeleton">
              <b-input-group v-for="(platform, index) in getValue('platforms')" :key="index" class="list-item mb-2">
                <b-form-select
                  type="text"
                  :value="platform.value"
                  @blur="() => setTouched({path: 'platforms', touched: true})"
                  :options="platformOptions"
                  @change="(value) => setPlatform(index, value)"
                />
                <b-input-group-append>
                  <b-button class="input-group-append text-danger" variant="text" @click="deletePlatform(index)">
                    <b-icon icon="trash" class="mr-2" />Remove
                  </b-button>
                </b-input-group-append>
              </b-input-group>
              <b-input-group-addon>
                <b-button @click="addPlatform" variant="text" class="text-primary">
                  <b-icon icon="plus" class="mr-2"/>Add Platform
                </b-button>
              </b-input-group-addon>
            </div>
            <div v-else>
              <b-skeleton width="85%"/>
              <b-skeleton width="50%"/>
              <b-skeleton width="95%"/>
              <b-skeleton width="35%"/>
              <b-skeleton width="60%"/>
            </div>
            <b-form-invalid-feedback>{{ getError('platforms') }}</b-form-invalid-feedback>
          </b-form-group>

          <b-form-group label="Legs" :label-for="legsId">
            <b-list-group>
              <b-input-group v-for="(leg, index) in getValue('legs')" :key="index" class="list-item mb-2">
                <b-form-input
                  type="text"
                  :value="leg.value"
                  @update="(value) => setLeg(index, value)"
                />
                <b-input-group-append>
                  <b-button class="input-group-append text-danger" variant="text" @click="deleteLeg(index)">
                    <b-icon icon="trash" class="mr-2" />Remove
                  </b-button>
                </b-input-group-append>
              </b-input-group>
              <b-input-group-addon>
                <b-button @click="addLeg" variant="text" class="text-primary">
                  <b-icon icon="plus" class="mr-2"/>Add Leg
                </b-button>
              </b-input-group-addon>
            </b-list-group>
          </b-form-group>
        </b-card>
        <b-card title="Samples" border-variant="dark" bg-variant="light" class="mb-4">
          <b-list-group horizontal v-if="!loadingCruiseSamples">
            <b-row>
              <b-col v-for="(sample, index) in samples" :key="index">
                <b-list-group-item class="list-item">
                  <b-button pill variant="primary" @click="showEditSampleModal(index)">
                    <nobr>{{ `${sample.sample} (${sample.imlgs})` }}</nobr>
                  </b-button>
                </b-list-group-item>
              </b-col>
              <b-col>
                <b-list-group-item class="list-item">
                  <b-button pill variant="secondary" @click="showAddSampleModal" :disabled="!isEdit">
                    <b-icon icon="plus"/>
                  </b-button>
                </b-list-group-item>
              </b-col>
            </b-row>
          </b-list-group>
          <div v-else>
            <b-spinner style="position:absolute; top: 50%; right: 50%"/>
          </div>
        </b-card>
        <div>
          <b-button v-if="showSubmit" type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">
            <b-icon icon="check" class="mr-2"/>Submit
          </b-button>
          <b-button v-if="formDirty" type="reset" variant="secondary" class="mb-2 mr-sm-2 mb-sm-0">
            <b-icon icon="arrow-counterclockwise" class="mr-2"/>Reset
          </b-button>
          <b-button v-if="isEdit" variant="danger" class="mb-2 mr-sm-2 mb-sm-0" @click="showModal">
            <b-icon icon="trash" class="mr-2"/>Delete
          </b-button>
          <b-modal ref="delete-modal" title="Delete Cruise" ok-variant="danger" ok-title="Delete" @ok="doDelete">
            <p class="my-4">Are you sure you want to delete this cruise?</p>
            <template #modal-footer>
              <b-button variant="secondary" class="mb-2 mr-sm-2 mb-sm-0" @click="hideModal">
                <b-icon icon="x" class="mr-2"/>Cancel
              </b-button>
              <b-button variant="danger" class="mb-2 mr-sm-2 mb-sm-0" @click="doDelete">
                <b-icon icon="trash" class="mr-2"/>Delete
              </b-button>
            </template>
          </b-modal>
        </div>

      </b-form>
      <b-modal ref="edit-sample-modal" size="xl" hide-header hide-footer>
        <ProviderSampleEdit v-if="samples[currentSample] && initialCruise" :id="samples[currentSample].imlgs" :within-modal="true" :post-save="refreshSamples" :post-delete="refreshSamplesAndCloseEdit" :cruise="initialCruise"/>
      </b-modal>
      <b-modal ref="add-sample-modal" size="xl" hide-header hide-footer>
        <ProviderSampleEdit v-if="initialCruise" :id="samples[currentSample] ? samples[currentSample].imlgs : null" :within-modal="true" :post-save="refreshSamples"  :post-delete="refreshSamplesAndCloseAdd" :cruise="initialCruise"/>
      </b-modal>
    </div>
  </div>
</template>

<script>
import {
  mapActions, mapGetters, mapMutations, mapState,
} from 'vuex';
import genId from '@/components/idGenerator';
import ProviderSampleEdit from '@/views/view/main/provider/sample/edit/ProviderSampleEdit.vue';

export default {
  components: { ProviderSampleEdit },
  props: ['id'],
  data() {
    return {
      cruiseNameId: '',
      yearId: '',
      platformsId: '',
      legsId: '',
      samples: [],
      currentSample: null,
      initialCruise: null,
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
    ...mapActions('providerSample', ['searchByCruiseNameAndCruiseYear']),
    ...mapActions('providerCruiseForm', ['submit', 'reset']),
    makeSuccessToast() {
      this.$bvToast.hide();
      this.$bvToast.toast('Cruise saved successfully', {
        title: 'Success',
        variant: 'success',
        solid: true,
        toaster: 'b-toaster-bottom-center',
      });
    },
    showModal() {
      this.$refs['delete-modal'].show();
    },
    hideModal() {
      this.$refs['delete-modal'].hide();
    },
    saveForm() {
      this.submit()
        .then((provider) => {
          this.save({ provider, id: this.id }).then(
            (c) => {
              this.$router.push({ name: 'ProviderCruiseEdit', params: { id: c.id } });
              sessionStorage.setItem('saveSuccess', 'true');
              window.location.reload();
            },
          );
        });
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
    setPlatform(index, value) {
      this.setValue({ path: `platforms[${index}]`, value });
    },
    deletePlatform(index) {
      this.deleteFromArray(`platforms[${index}]`);
    },
    addPlatform() {
      this.addToArray({ path: 'platforms', value: '' });
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
    showAddSampleModal() {
      this.$refs['add-sample-modal'].show();
      this.currentSample = this.samples.length;
    },
    showEditSampleModal(index) {
      this.$refs['edit-sample-modal'].show();
      this.currentSample = index;
    },
    refreshSamples(updatedSample) {
      this.searchByCruiseNameAndCruiseYear({ cruiseName: this.initialCruise.cruiseName, cruiseYear: this.initialCruise.year }).then(
        (samples) => {
          this.samples = samples;
          if (updatedSample) {
            this.currentSample = this.samples.findIndex((s) => s.imlgs === updatedSample.imlgs);
          }
        },
      );
    },
    makeDeleteSampleToast() {
      this.$bvToast.hide();
      this.$bvToast.toast('Sample deleted successfully', {
        title: 'Success',
        variant: 'success',
        solid: true,
        toaster: 'b-toaster-bottom-center',
      });
    },
    refreshSamplesAndCloseAdd() {
      this.refreshSamples();
      this.$refs['add-sample-modal'].hide();
      this.makeDeleteSampleToast();
    },
    refreshSamplesAndCloseEdit() {
      this.refreshSamples();
      this.$refs['edit-sample-modal'].hide();
      this.makeDeleteSampleToast();
    },
  },

  computed: {
    ...mapState('providerCruise', ['deleting', 'loading', 'saving', 'options', 'loadingOptions']),
    ...mapState('providerSample', ['loadingCruiseSamples']),
    ...mapGetters('providerCruiseForm',
      [
        'getValue',
        'formDirty',
        'getError',
        'isTouched',
        'formHasUntouchedErrors',
      ]),
    showSkeleton() {
      return this.saving || !this.ready;
    },
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
    if (sessionStorage.getItem('saveSuccess')) {
      this.makeSuccessToast();
      sessionStorage.removeItem('saveSuccess');
    }
    this.loadOptions();
    if (this.id != null) {
      this.load(this.id).then(
        (existing) => {
          this.initialize(existing);
          this.initialCruise = existing;
          this.searchByCruiseNameAndCruiseYear({ cruiseName: existing.cruiseName, cruiseYear: existing.year }).then(
            (samples) => {
              this.samples = samples;
            },
          );
        },
      );
    } else {
      this.initialize();
    }
  },

};
</script>

<style scoped>
.list-item {
  background: none;
  border: none;
}
</style>
