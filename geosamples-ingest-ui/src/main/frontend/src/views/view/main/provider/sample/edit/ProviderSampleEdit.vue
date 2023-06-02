<template>
  <div class="m-2">
    <b-breadcrumb v-if="!withinModal" :items="isEdit ? [
          { text: 'Geosamples Ingest', to: { name: 'Home' } },
          { text: 'Samples', to: { name: 'ProviderSampleList' } },
          { text: 'Edit Sample', active: false },
        ] : [
          { text: 'Geosamples Ingest', to: { name: 'Home' } },
          { text: 'Samples', to: { name: 'ProviderSampleList' } },
          { text: 'Add Sample', active: false },
        ]"/>
    <div>
      <h1 v-if="isEdit" class="text-primary">Edit Sample - {{ getValue('imlgs') }}</h1>
      <h1 v-else class="text-primary">Add New Sample</h1>
      <b-form @submit.prevent="saveForm" @reset.prevent="reset">
        <b-card title="Sample Information" border-variant="dark" bg-variant="light" class="mb-4">
          <b-form-group :label-for="sampleId">
            <template #label>
              Sample ID<span><strong style="color: red"> *</strong></span>
            </template>
            <b-form-input
              required
              type="text"
              :id="sampleId"
              :value="getValue('sample')"
              @input="(value) => setValue({ path: 'sample', value })"
              :state="showError('sample')"
            />
            <b-form-invalid-feedback>{{ getError('sample') }}</b-form-invalid-feedback>
          </b-form-group>
          <b-form-group :label-for="platformId">
            <template #label>
              Ship Name<span><strong style="color: red"> *</strong></span>
            </template>
            <v-select
              style="background: white"
              :options="platformOptions"
              :value="getValue('platform')"
              label="text"
              @input="(option) => selectPlatform({ path: 'platform', value:  option.value })"
              @search="(name) => searchPlatform(name)"
              :clearable="false"
            >
              <template #spinner>
                <b-spinner v-if="loadingPlatformOptions" class="align-middle" style="margin-left: 10px;" small variant="primary"/>
              </template>
              <template #open-indicator="{ attributes }">
                <b-icon v-bind="attributes" icon="caret-down-fill" class="align-middle" style="margin-left: 10px;"/>
              </template>
            </v-select>
            <b-form-invalid-feedback>{{ getError('platform') }}</b-form-invalid-feedback>
          </b-form-group>
          <b-row>
            <b-col>
              <b-form-group :label-for="cruiseId">
                <template #label>
                  Cruise ID<span><strong style="color: red"> *</strong></span>
                </template>
                <v-select
                  :disabled="Boolean(cruise) || !getValue('platform')"
                  style="background: white"
                  :options="cruiseOptions"
                  :value="this.currentItem ? `${this.currentItem.cruiseName} (${this.currentItem.year})` : null"
                  label="text"
                  @input="(option) => selectCruise(option.value)"
                  @search="(name) => loadCruiseOptions({ platformName: getValue('platform'), cruiseName: name })"
                  :clearable="false"
                >
                  <template #spinner>
                    <b-spinner v-if="loadingCruises" class="align-middle" style="margin-left: 10px;" small variant="primary"/>
                  </template>
                  <template #open-indicator="{ attributes }">
                    <b-icon v-bind="attributes" icon="caret-down-fill" class="align-middle" style="margin-left: 10px;"/>
                  </template>
                </v-select>
                <b-form-invalid-feedback>{{ getError('cruise') }}</b-form-invalid-feedback>
              </b-form-group>
            </b-col>
            <b-col>
              <b-form-group label="Alternate Cruise/Leg" :label-for="legId">
                <b-form-select
                  v-if="!loadingLegs"
                  :disabled="!getValue('cruise') || legOptions.length === 0"
                  :id="legId"
                  :options="legOptions"
                  :value="getValue('leg')"
                  @change="(value) => setValue({ path: 'leg', value })"
                  :state="showError('leg')"
                />
                <b-skeleton v-else/>
                <b-form-invalid-feedback>{{ getError('leg') }}</b-form-invalid-feedback>
              </b-form-group>
            </b-col>
          </b-row>
          <b-form-group :label-for="deviceCodeId">
            <template #label>
              Sampling Device<span><strong style="color: red"> *</strong></span>
            </template>
            <b-form-select
              required
              v-if="!loadingOptions"
              :id="deviceCodeId"
              :options="optionsDeviceCode"
              :value="getValue('deviceCode')"
              @input="(value) => setValue({ path: 'deviceCode', value })"
              :state="showError('deviceCode')"
            />
            <b-skeleton v-else/>
            <b-form-invalid-feedback>{{ getError('deviceCode') }}</b-form-invalid-feedback>
          </b-form-group>
          <b-form-group label="Storage Method" :label-for="storageMethCodeId">
            <b-form-select
              v-if="!loadingOptions"
              :id="storageMethCodeId"
              :options="optionsStorageMethCode"
              :value="getValue('storageMethCode')"
              @input="(value) => setValue({ path: 'storageMethCode', value })"
              :state="showError('storageMethCode')"
            />
            <b-skeleton v-else/>
            <b-form-invalid-feedback>{{ getError('storageMethCode') }}</b-form-invalid-feedback>
          </b-form-group>
          <b-row>
            <b-col>
              <b-form-group label="Core Length (cm)" :label-for="coredLengthId">
                <b-form-input
                  type="number"
                  min="0"
                  :id="coredLengthId"
                  :value="getValue('coredLength')"
                  @input="(value) => setValue({ path: 'coredLength', value })"
                  :state="showError('coredLength')"
                />
                <b-form-invalid-feedback>{{ getError('coredLength') }}</b-form-invalid-feedback>
              </b-form-group>
            </b-col>
            <b-col>
              <b-form-group label="Core Diameter (cm)" :label-for="coredDiamId">
                <b-form-input
                  type="number"
                  min="0"
                  :id="coredDiamId"
                  :value="getValue('coredDiam')"
                  @input="(value) => setValue({ path: 'coredDiam', value })"
                  :state="showError('coredDiam')"
                />
                <b-form-invalid-feedback>{{ getError('coredDiam') }}</b-form-invalid-feedback>
              </b-form-group>
            </b-col>
          </b-row>
        </b-card>
        <b-card title="Sample Collection Date" border-variant="dark" bg-variant="light" class="mb-4">
          <b-row>
            <b-col>
              <b-form-group label="Date Collected" :label-for="beginDateId">
                <b-form-input
                  type="text"
                  :id="beginDateId"
                  :value="getValue('beginDate')"
                  @input="(value) => setValue({ path: 'beginDate', value })"
                  :state="showError('beginDate') || isValidDate(getValue('beginDate'))"
                  placeholder="YYYYMMDD"
                />
                <b-form-invalid-feedback>{{ getError('beginDate') }}</b-form-invalid-feedback>
              </b-form-group>
            </b-col>
            <b-col>
              <b-form-group label="End Date" :label-for="endDateId">
                <b-form-input
                  type="text"
                  :id="endDateId"
                  :value="getValue('endDate')"
                  @input="(value) => setValue({ path: 'endDate', value })"
                  :state="showError('endDate') || isValidDate(getValue('endDate'))"
                  placeholder="YYYYMMDD"
                />
                <b-form-invalid-feedback>{{ getError('endDate') }}</b-form-invalid-feedback>
              </b-form-group>
            </b-col>
          </b-row>
        </b-card>
        <b-card title="Sample Location" border-variant="dark" bg-variant="light" class="mb-4">
          <b-row>
            <b-col>
              <b-form-group :label-for="latId">
                <template #label>
                  Beginning Latitude (-99.99999, south is negative)<span><strong style="color: red"> *</strong></span>
                </template>
                <b-form-input
                  required
                  type="number"
                  min="-90"
                  max="90"
                  :id="latId"
                  :value="getValue('lat')"
                  @input="(value) => setValue({ path: 'lat', value })"
                  :state="showError('lat')"
                />
                <b-form-invalid-feedback>{{ getError('lat') }}</b-form-invalid-feedback>
              </b-form-group>
            </b-col>
            <b-col>
              <b-form-group label="Ending Latitude (-99.99999, south is negative)" :label-for="endLatId">
                <b-form-input
                  type="number"
                  min="-90"
                  max="90"
                  :id="endLatId"
                  :value="getValue('endLat')"
                  @input="(value) => setValue({ path: 'endLat', value })"
                  :state="showError('endLat')"
                />
                <b-form-invalid-feedback>{{ getError('endLat') }}</b-form-invalid-feedback>
              </b-form-group>
            </b-col>
          </b-row>
          <b-row>
            <b-col>
              <b-form-group :label-for="lonId">
                <template #label>
                  Beginning Longitude (-99.99999, west is negative)<span><strong style="color: red"> *</strong></span>
                </template>
                <b-form-input
                  required
                  type="number"
                  min="-180"
                  max="180"
                  :id="lonId"
                  :value="getValue('lon')"
                  @input="(value) => setValue({ path: 'lon', value })"
                  :state="showError('lon')"
                />
                <b-form-invalid-feedback>{{ getError('lon') }}</b-form-invalid-feedback>
              </b-form-group>
            </b-col>
            <b-col>
              <b-form-group label="Ending Longitude (-99.99999, west is negative)" :label-for="endLonId">
                <b-form-input
                  type="number"
                  min="-180"
                  max="180"
                  :id="endLonId"
                  :value="getValue('endLon')"
                  @input="(value) => setValue({ path: 'endLon', value })"
                  :state="showError('endLon')"
                />
                <b-form-invalid-feedback>{{ getError('endLon') }}</b-form-invalid-feedback>
              </b-form-group>
            </b-col>
          </b-row>
          <b-form-group label="Physiographic Province" :label-for="provinceCodeId">
            <b-form-select
              v-if="!loadingOptions"
              :id="provinceCodeId"
              :options="optionsProvinceCode"
              :value="getValue('provinceCode')"
              @input="(value) => setValue({ path: 'provinceCode', value })"
              :state="showError('provinceCode')"
            />
            <b-skeleton v-else/>
            <b-form-invalid-feedback>{{ getError('provinceCode') }}</b-form-invalid-feedback>
          </b-form-group>
          <b-row>
            <b-col>
              <b-form-group label="Beginning Water Depth (m)" :label-for="waterDepthId">
                <b-form-input
                  type="number"
                  min="0"
                  :id="waterDepthId"
                  :value="getValue('waterDepth')"
                  @input="(value) => setValue({ path: 'waterDepth', value })"
                  :state="showError('waterDepth')"
                />
                <b-form-invalid-feedback>{{ getError('waterDepth') }}</b-form-invalid-feedback>
              </b-form-group>
            </b-col>
            <b-col>
              <b-form-group label="Ending Water Depth (m)" :label-for="endWaterDepthId">
                <b-form-input
                  type="number"
                  min="0"
                  :id="endWaterDepthId"
                  :value="getValue('endWaterDepth')"
                  @input="(value) => setValue({ path: 'endWaterDepth', value })"
                  :state="showError('endWaterDepth')"
                />
                <b-form-invalid-feedback>{{ getError('endWaterDepth') }}</b-form-invalid-feedback>
              </b-form-group>
            </b-col>
          </b-row>
          <b-form-group label="Lake (if applicable)" :label-for="lakeId">
            <b-form-input
              type="text"
              :id="lakeId"
              :value="getValue('lake')"
              @input="(value) => setValue({ path: 'lake', value })"
              :state="showError('lake')"
            />
          </b-form-group>
        </b-card>
        <b-card title="Additional Sample Information" border-variant="dark" bg-variant="light" class="mb-4">
          <b-form-group label="Principal Investigator" :label-for="piId">
            <b-form-input
              type="text"
              :id="piId"
              :value="getValue('pi')"
              @input="(value) => setValue({ path: 'pi', value })"
              :state="showError('pi')"
            />
            <b-form-invalid-feedback>{{ getError('pi') }}</b-form-invalid-feedback>
          </b-form-group>
          <b-form-group label="Sample (Parent) IGSN" :label-for="igsnId">
            <b-form-input
              type="text"
              :id="igsnId"
              :value="getValue('igsn')"
              @input="(value) => setValue({ path: 'igsn', value })"
              :state="showError('igsn')"
            />
            <b-form-invalid-feedback>{{ getError('igsn') }}</b-form-invalid-feedback>
          </b-form-group>
          <b-form-group :label-for="sampleCommentsId">
            <template #label>
              Ancillary Comments<span style="color: gray"> (limit 2000 characters)</span>
            </template>
            <b-form-textarea
              :id="sampleCommentsId"
              :value="getValue('sampleComments')"
              @input="(value) => setValue({ path: 'sampleComments', value })"
              :state="showError('sampleComments') || (!getValue('sampleComments') || getValue('sampleComments').length < 2000 ? null : false)"
            />
            <b-form-invalid-feedback>{{ getError('sampleComment') }}</b-form-invalid-feedback>
          </b-form-group>
        </b-card>
        <b-card title="Subsamples/Intervals" border-variant="dark" bg-variant="light" class="mb-4">
          <b-list-group horizontal v-if="!loadingIntervals">
            <b-row>
              <b-col v-for="(interval, i) in intervals" :key="i">
                <b-list-group-item class="interval-button">
                  <b-button pill variant="primary" @click="showEditIntervalModal(i)">
                    {{ interval.interval }}
                  </b-button>
                </b-list-group-item>
              </b-col>
            </b-row>
          </b-list-group>
          <div v-else class="text-center">
            <b-spinner/>
          </div>
          <b-row v-if="!loadingIntervals && (sampleIntervalPage === sampleIntervalTotalPages || sampleIntervalTotalItems === 0)">
            <b-col>
              <b-list-group-item class="interval-button">
                <b-button pill variant="secondary" @click="showAddIntervalModal" :disabled="!isEdit">
                  <b-icon icon="plus"/>
                </b-button>
              </b-list-group-item>
            </b-col>
          </b-row>
          <template #footer>
            <b-pagination
              class="mt-2"
              :value="sampleIntervalPage"
              :total-rows="sampleIntervalTotalItems"
              :per-page="sampleIntervalItemsPerPage"
              @input="updateSampleIntervalPage"
              pills
              size="med"
              align="center"
            />
          </template>
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
          <b-modal ref="delete-modal" title="Delete Sample" ok-variant="danger" ok-title="Delete" @ok="doDelete">
            <p class="my-4">Are you sure you want to delete this sample?</p>
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
      <b-modal ref="edit-interval-modal" size="xl" hide-header hide-footer>
        <ProviderIntervalEdit v-if="intervals[currentInterval]" :id="intervals[currentInterval].id" :within-modal="true" :post-save="closeEditAndRefreshIntervals" :post-delete="closeEditAfterDeleteAndRefreshIntervals" :imlgs="id" :interval-number="intervals[currentInterval].interval"/>
      </b-modal>
      <b-modal ref="add-interval-modal" size="xl" hide-header hide-footer>
        <ProviderIntervalEdit :within-modal="true" :post-save="closeAddAndRefreshIntervals" :post-delete="closeAddAfterDeleteAndRefreshIntervals" :imlgs="id" :interval-number="intervals.length === 0 ? 1 : intervals[intervals.length - 1].interval + 1"/>
      </b-modal>
    </div>
  </div>
</template>

<script>
import {
  mapActions,
  mapGetters,
  mapMutations,
  mapState,
} from 'vuex';
import genId from '@/components/idGenerator';
import ProviderIntervalEdit
  from '@/views/view/main/provider/interval/edit/ProviderIntervalEdit.vue';

export default {
  components: { ProviderIntervalEdit },
  props: ['id', 'withinModal', 'postSave', 'postDelete', 'cruise'],

  data() {
    return {
      cruiseId: '',
      sampleId: '',
      platformId: '',
      deviceCodeId: '',
      beginDateId: '',
      endDateId: '',
      latId: '',
      endLatId: '',
      lonId: '',
      endLonId: '',
      waterDepthId: '',
      endWaterDepthId: '',
      storageMethCodeId: '',
      coredLengthId: '',
      coredDiamId: '',
      piId: '',
      provinceCodeId: '',
      lakeId: '',
      igsnId: '',
      legId: '',
      sampleCommentsId: '',
      currentItem: null,
      currentInterval: null,
      intervals: [],
    };
  },

  beforeMount() {
    this.cruiseId = genId();
    this.sampleId = genId();
    this.platformId = genId();
    this.deviceCodeId = genId();
    this.beginDateId = genId();
    this.endDateId = genId();
    this.latId = genId();
    this.endLatId = genId();
    this.lonId = genId();
    this.endLonId = genId();
    this.waterDepthId = genId();
    this.endWaterDepthId = genId();
    this.storageMethCodeId = genId();
    this.coredLengthId = genId();
    this.coredDiamId = genId();
    this.piId = genId();
    this.provinceCodeId = genId();
    this.lakeId = genId();
    this.igsnId = genId();
    this.legId = genId();
    this.sampleCommentsId = genId();
  },

  methods: {
    ...mapActions('providerSample', ['delete', 'save', 'loadOptions', 'loadLegOptions', 'loadCruiseOptions', 'load', 'searchPlatformsByName']),
    ...mapActions('providerInterval', ['searchByImlgs']),
    ...mapActions('providerSampleForm', ['reset', 'submit']),
    ...mapMutations('providerSampleForm', ['setValue', 'initialize']),
    ...mapMutations('providerSample', ['updateCruiseOptions']),
    ...mapMutations('providerInterval', ['setSampleIntervalPage']),

    searchPlatform(name) {
      if (this.cruise) {
        this.searchPlatformsByName(this.cruise.platforms.join(','));
      } else {
        this.searchPlatformsByName(name);
      }
    },

    updateSampleIntervalPage(page) {
      this.setSampleIntervalPage(page);
      this.refreshIntervals();
    },

    isValidDate(value) {
      if (value) {
        const month = Number(value.substring(4, 6).replace(/^0+/, '') - 1);
        const day = Number(value.substring(6, 8).replace(/^0+/, ''));
        const year = Number(value.substring(0, 4));

        const date = new Date(year, month, day);
        return date.getFullYear() === year && date.getMonth() === month && date.getDate() === day && date.getTime() < Date.now() ? null : false;
      }
      return null;
    },

    showEditIntervalModal(index) {
      this.$refs['edit-interval-modal'].show();
      this.currentInterval = index;
    },

    showAddIntervalModal() {
      this.$refs['add-interval-modal'].show();
      this.currentInterval = this.intervals.length;
    },

    showModal() {
      this.$refs['delete-modal'].show();
    },

    hideModal() {
      this.$refs['delete-modal'].hide();
    },

    doDelete() {
      this.delete(this.id).then(
        () => {
          if (!this.withinModal) {
            this.$router.push({ name: 'ProviderSampleList' });
          } else if (this.postDelete) {
            this.postDelete();
          }
        },
      );
    },

    makeSuccessToast() {
      this.$bvToast.hide();
      this.$bvToast.toast('Sample saved successfully', {
        title: 'Success',
        variant: 'success',
        solid: true,
        toaster: 'b-toaster-bottom-center',
      });
    },

    makeIntervalSuccessToast() {
      this.$bvToast.hide();
      this.$bvToast.toast('Subsample/Interval saved successfully', {
        title: 'Success',
        variant: 'success',
        solid: true,
        toaster: 'b-toaster-bottom-center',
      });
    },

    makeIntervalDeleteToast() {
      this.$bvToast.hide();
      this.$bvToast.toast('Subsample/Interval deleted successfully', {
        title: 'Success',
        variant: 'success',
        solid: true,
        toaster: 'b-toaster-bottom-center',
      });
    },

    saveForm() {
      this.submit().then(
        (sample) => this.save({ provider: sample, id: this.id }).then(
          (s) => {
            if (!this.withinModal) {
              this.$router.push({ name: 'ProviderSampleList' });
            } else if (this.postSave) {
              this.postSave(s);
              this.$forceUpdate();
              this.initialize({
                ...s,
                platform: s.platform.toUpperCase(),
              });
              this.makeSuccessToast();
            }
          },
        ),
      );
    },

    selectCruise(value) {
      this.currentItem = value;
      this.setValue({ path: 'cruise', value: value.cruiseName });
      this.loadLegOptions(value);
    },

    selectPlatform(value) {
      if (this.isTouched('platform')) {
        if (!this.cruise) {
          this.setValue({ path: 'cruise', value: null });
          this.setValue({ path: 'leg', value: [] });
          this.currentItem = null;
        }
      }
      this.setValue({ path: 'platform', value: value.value });
      if (!this.cruise) {
        this.setValue({ path: 'cruise', value: null });
        this.setValue({ path: 'leg', value: [] });
        this.loadCruiseOptions({ platformName: value.value, cruiseName: null });
      }
    },

    refreshIntervals() {
      this.searchByImlgs(this.id).then(
        (data) => {
          this.intervals = data.items.map((i) => ({ id: i.id, interval: i.interval }));
        },
      );
    },

    closeAddAndRefreshIntervals() {
      this.$refs['add-interval-modal'].hide();
      this.refreshIntervals();
      this.makeIntervalSuccessToast();
    },

    closeEditAndRefreshIntervals() {
      this.$refs['edit-interval-modal'].hide();
      this.refreshIntervals();
      this.makeIntervalSuccessToast();
    },

    closeEditAfterDeleteAndRefreshIntervals() {
      this.$refs['edit-interval-modal'].hide();
      this.refreshIntervals();
      this.makeIntervalDeleteToast();
    },

    closeAddAfterDeleteAndRefreshIntervals() {
      this.$refs['add-interval-modal'].hide();
      this.refreshIntervals();
      this.makeIntervalDeleteToast();
    },
  },

  computed: {
    ...mapState('providerSample', ['loading', 'options', 'legOptions', 'loadingOptions', 'loadingLegs', 'saving', 'cruiseOptions', 'loadingCruises', 'loadingPlatformOptions', 'platformOptions']),
    ...mapState({
      loadingIntervals: (state) => state.providerInterval.loadingSampleIntervals,
    }),
    ...mapGetters('providerSampleForm', ['getValue', 'formDirty', 'formHasUntouchedErrors', 'getError', 'isTouched']),
    ...mapState('providerInterval', ['sampleIntervalPage', 'sampleIntervalTotalItems', 'sampleIntervalItemsPerPage', 'sampleIntervalTotalPages']),

    ready() {
      return !this.loading && !this.loadingOptions && !this.saving && !this.loadingIntervals;
    },

    isEdit() {
      return this.id || this.id === 0;
    },

    showSubmit() {
      return this.formDirty && !this.formHasUntouchedErrors;
    },

    showError() {
      return (path) => ((!this.isTouched(path) && this.getError(path)) ? false : null);
    },

    optionsDeviceCode() {
      const { deviceCode: field } = this.options;
      return field || [];
    },

    optionsStorageMethCode() {
      const { storageMethCode: field } = this.options;
      return field || [];
    },

    optionsProvinceCode() {
      const { provinceCode: field } = this.options;
      return field || [];
    },
  },

  created() {
    this.loadOptions();
    this.searchPlatform(null);
    if (this.id) {
      this.load(this.id).then(
        (sample) => {
          this.currentItem = { cruiseName: sample.cruise, year: sample.cruiseYear };
          this.initialize(sample);
          this.loadCruiseOptions(sample.platform, null);
          this.selectPlatform({ value: sample.platform.toUpperCase() });
          this.selectCruise(this.currentItem);
          this.searchByImlgs(sample.imlgs).then(
            (data) => {
              this.intervals = data.items.map((i) => ({ id: i.id, interval: i.interval }));
            },
          );
          this.submit().then(
            (s) => {
              this.initialize(s);
            },
          );
        },
      );
    } else {
      this.initialize();
      if (this.cruise) {
        this.currentItem = { cruiseName: this.cruise.cruiseName, year: this.cruise.year };
        this.updateCruiseOptions([{ text: `${this.cruise.cruiseName} (${this.cruise.year})`, value: { cruiseName: this.cruise.cruiseName, year: this.cruise.year } }]);
        this.selectPlatform({ value: this.cruise.platforms ? this.cruise.platforms[0] : null });
        this.selectCruise(this.cruise);
      }
      this.submit().then(
        (sample) => {
          this.initialize(sample);
        },
      );
    }
  },
};
</script>

<style scoped>
.interval-button {
  background: none;
  border: none;
}
</style>
