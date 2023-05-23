<template>
  <div class="m-2">
    <b-breadcrumb :items="[
      { text: 'Geosamples Ingest', to: { name: 'Home' } },
      { text: 'Intervals', to: { name: 'ProviderIntervalList' } },
      { text: 'Edit Interval', active: false }
    ]"/>
    <div v-if="ready">
      <h1 v-if="isEdit" class="text-primary">Edit Interval - {{ getValue('id') }}</h1>
      <h1 v-else class="text-primary">Add Interval</h1>
      <b-button v-if="isEdit" type="button" variant="danger" class="mb-3" @click="showModal">Delete</b-button>
      <b-modal ref="delete-modal" title="Delete Sample" ok-variant="danger" ok-title="Delete" @ok="doDelete">
        <p class="my-4">Are you sure you want to delete this interval?</p>
      </b-modal>
      <b-form @submit.prevent="saveForm" @reset.prevent="reset">
        <b-row>
          <b-col>
            <b-card title="Geologic Descriptive Information" border-variant="dark" bg-variant="light" class="mb-4">
              <b-row>
                <b-col>
                  <b-form-group label="Sample ID" :label-for="imlgsId">
                    <b-form-select
                      required
                      :id="imlgsId"
                      :options="optionsIMLGS"
                      :value="getValue('imlgs')"
                      @change="(value) => setValue({ path: 'imlgs', value })"
                      :state="showError('imlgs')"
                    />
                    <b-form-invalid-feedback>{{ getError('imlgs') }}</b-form-invalid-feedback>
                  </b-form-group>
                </b-col>
                <b-col>
                  <b-form-group label="Interval #" :label-for="intervalId">
                    <b-form-input
                      required
                      type="number"
                      min="1"
                      :id="intervalId"
                      :value="getValue('interval')"
                      @input="(value) => setValue({ path: 'interval', value })"
                      :state="showError('interval')"
                    />
                    <b-form-invalid-feedback>{{ getError('interval') }}</b-form-invalid-feedback>
                  </b-form-group>
                </b-col>
              </b-row>
              <b-row>
                <b-col>
                  <b-form-group label="Depth to Top of Interval (cm)" :label-for="depthTopId">
                    <b-form-input
                      type="number"
                      step="any"
                      min="0"
                      :id="depthTopId"
                      :value="getValue('depthTop')"
                      @input="(value) => setValue({ path: 'depthTop', value })"
                      :state="showError('depthTop')"
                    />
                    <b-form-invalid-feedback>{{ getError('depthTop') }}</b-form-invalid-feedback>
                  </b-form-group>
                </b-col>
                <b-col>
                  <b-form-group label="Depth to Bottom of Interval (cm)" :label-for="depthBotId">
                    <b-form-input
                      type="number"
                      step="any"
                      min="0"
                      :id="depthBotId"
                      :value="getValue('depthBot')"
                      @input="(value) => setValue({ path: 'depthBot', value })"
                      :state="showError('depthBot')"
                    />
                    <b-form-invalid-feedback>{{ getError('depthBot') }}</b-form-invalid-feedback>
                  </b-form-group>
                </b-col>
              </b-row>
              <b-row>
                <b-col>
                  <b-form-group label="Primary Lithologic Composition" :label-for="lithCode1Id">
                    <b-form-select
                      type="text"
                      :id="lithCode1Id"
                      :options="optionsLithologyCode"
                      :value="getValue('lithCode1')"
                      @change="(value) => setValue({ path: 'lithCode1', value })"
                      :state="showError('lithCode1')"
                    />
                    <b-form-invalid-feedback>{{ getError('lithCode1') }}</b-form-invalid-feedback>
                  </b-form-group>
                </b-col>
                <b-col>
                  <b-form-group label="Secondary Lithologic Composition" :label-for="lithCode2Id">
                    <b-form-select
                      type="text"
                      :id="lithCode2Id"
                      :options="optionsLithologyCode"
                      :value="getValue('lithCode2')"
                      @change="(value) => setValue({ path: 'lithCode2', value })"
                      :state="showError('lithCode2')"
                    />
                    <b-form-invalid-feedback>{{ getError('lithCode2') }}</b-form-invalid-feedback>
                  </b-form-group>
                </b-col>
              </b-row>
              <b-row>
                <b-col>
                  <b-form-group label="Primary Texture" :label-for="textCode1Id">
                    <b-form-select
                      type="text"
                      :id="textCode1Id"
                      :options="optionsTextureCode"
                      :value="getValue('textCode1')"
                      @change="(value) => setValue({ path: 'textCode1', value })"
                      :state="showError('textCode1')"
                    />
                    <b-form-invalid-feedback>{{ getError('textCode1') }}</b-form-invalid-feedback>
                  </b-form-group>
                </b-col>
                <b-col>
                  <b-form-group label="Secondary Texture" :label-for="textCode2Id">
                    <b-form-select
                      type="text"
                      :id="textCode2Id"
                      :options="optionsTextureCode"
                      :value="getValue('textCode2')"
                      @change="(value) => setValue({ path: 'textCode2', value })"
                      :state="showError('textCode2')"
                    />
                    <b-form-invalid-feedback>{{ getError('textCode2') }}</b-form-invalid-feedback>
                  </b-form-group>
                </b-col>
              </b-row>
              <b-row>
                <b-col>
                  <b-form-group label="Other Component 1" :label-for="compCode1Id">
                    <b-form-select
                      type="text"
                      :id="compCode1Id"
                      :options="optionsLithologyCode"
                      :value="getValue('compCode1')"
                      @change="(value) => setValue({ path: 'compCode1', value })"
                      :state="showError('compCode1')"
                    />
                    <b-form-invalid-feedback>{{ getError('compCode1') }}</b-form-invalid-feedback>
                  </b-form-group>
                </b-col>
                <b-col>
                  <b-form-group label="Other Component 2" :label-for="compCode2Id">
                    <b-form-select
                      type="text"
                      :id="compCode2Id"
                      :options="optionsLithologyCode"
                      :value="getValue('compCode2')"
                      @change="(value) => setValue({ path: 'compCode2', value })"
                      :state="showError('compCode2')"
                    />
                    <b-form-invalid-feedback>{{ getError('compCode2') }}</b-form-invalid-feedback>
                  </b-form-group>
                </b-col>
                <b-col>
                  <b-form-group label="Other Component 3" :label-for="compCode3Id">
                    <b-form-select
                      type="text"
                      :id="compCode3Id"
                      :options="optionsLithologyCode"
                      :value="getValue('compCode3')"
                      @change="(value) => setValue({ path: 'compCode3', value })"
                      :state="showError('compCode3')"
                    />
                    <b-form-invalid-feedback>{{ getError('compCode3') }}</b-form-invalid-feedback>
                  </b-form-group>
                </b-col>
                <b-col>
                  <b-form-group label="Other Component 4" :label-for="compCode4Id">
                    <b-form-select
                      type="text"
                      :id="compCode4Id"
                      :options="optionsLithologyCode"
                      :value="getValue('compCode4')"
                      @change="(value) => setValue({ path: 'compCode4', value })"
                      :state="showError('compCode4')"
                    />
                    <b-form-invalid-feedback>{{ getError('compCode4') }}</b-form-invalid-feedback>
                  </b-form-group>
                </b-col>
                <b-col>
                  <b-form-group label="Other Component 5" :label-for="compCode5Id">
                    <b-form-select
                      type="text"
                      :id="compCode5Id"
                      :options="optionsLithologyCode"
                      :value="getValue('compCode5')"
                      @change="(value) => setValue({ path: 'compCode5', value })"
                      :state="showError('compCode5')"
                    />
                    <b-form-invalid-feedback>{{ getError('compCode5') }}</b-form-invalid-feedback>
                  </b-form-group>
                </b-col>
                <b-col>
                  <b-form-group label="Other Component 6" :label-for="compCode6Id">
                    <b-form-select
                      type="text"
                      :id="compCode6Id"
                      :options="optionsLithologyCode"
                      :value="getValue('compCode6')"
                      @change="(value) => setValue({ path: 'compCode6', value })"
                      :state="showError('compCode6')"
                    />
                    <b-form-invalid-feedback>{{ getError('compCode6') }}</b-form-invalid-feedback>
                  </b-form-group>
                </b-col>
              </b-row>
              <b-row>
                <b-col>
                  <b-form-group label="Geologic Age" :label-for="ageCodesId">
                    <b-form-select
                      :id="ageCodesId"
                      @blur="() => setTouched({path: 'ageCodes', touched: true})"
                      :value="selectedAges"
                      :options="optionsAgeCode"
                      @change="setGeologicAgeValues"
                      multiple
                    />
                    <b-form-invalid-feedback>{{ getError('ageCodes') }}</b-form-invalid-feedback>
                  </b-form-group>
                </b-col>
              </b-row>
              <b-row>
                <b-col>
                  <b-form-group label="Rock Lithology" :label-for="rockLithCodeId">
                    <b-form-select
                      type="text"
                      :id="rockLithCodeId"
                      :options="optionsLithologyCode"
                      :value="getValue('rockLithCode')"
                      @change="(value) => setValue({ path: 'rockLithCode', value })"
                      :state="showError('rockLithCode')"
                    />
                    <b-form-invalid-feedback>{{ getError('rockLithCode') }}</b-form-invalid-feedback>
                  </b-form-group>
                </b-col>
                <b-col>
                  <b-form-group label="Rock Mineralogy" :label-for="rockMinCodeId">
                    <b-form-select
                      type="text"
                      :id="rockMinCodeId"
                      :options="optionsRockMineralCode"
                      :value="getValue('rockMinCode')"
                      @change="(value) => setValue({ path: 'rockMinCode', value })"
                      :state="showError('rockMinCode')"
                    />
                    <b-form-invalid-feedback>{{ getError('rockMinCode') }}</b-form-invalid-feedback>
                  </b-form-group>
                </b-col>
                <b-col>
                  <b-form-group label="Rock Weathering/Metamorphism" :label-for="weathMetaCodeId">
                    <b-form-select
                      type="text"
                      :id="weathMetaCodeId"
                      :options="optionsWeathMetaCode"
                      :value="getValue('weathMetaCode')"
                      @change="(value) => setValue({ path: 'weathMetaCode', value })"
                      :state="showError('weathMetaCode')"
                    />
                    <b-form-invalid-feedback>{{ getError('weathMetaCode') }}</b-form-invalid-feedback>
                  </b-form-group>
                </b-col>
              </b-row>
              <b-row>
                <b-col>
                  <b-form-group label="Rock Glass Remarks & Mn/Fe Oxide" :label-for="remarkCodeId">
                    <b-form-select
                      type="text"
                      :id="remarkCodeId"
                      :options="optionsRemarkCode"
                      :value="getValue('remarkCode')"
                      @change="(value) => setValue({ path: 'remarkCode', value })"
                      :state="showError('remarkCode')"
                    />
                    <b-form-invalid-feedback>{{ getError('remarkCode') }}</b-form-invalid-feedback>
                  </b-form-group>
                </b-col>
                <b-col>
                  <b-form-group label="Munsell Color" :label-for="munsellCodeId">
                    <b-form-select
                      type="text"
                      :id="munsellCodeId"
                      :options="optionsMunsellCode"
                      :value="getValue('munsellCode')"
                      @change="(value) => setValue({ path: 'munsellCode', value })"
                      :state="showError('munsellCode')"
                    />
                    <b-form-invalid-feedback>{{ getError('munsellCode') }}</b-form-invalid-feedback>
                  </b-form-group>
                </b-col>
              </b-row>
              <b-row>
                <b-col>
                  <b-form-group label="Bulk Weight (kg)" :label-for="weightId">
                    <b-form-input
                      type="number"
                      step="any"
                      :id="weightId"
                      min="0"
                      :value="getValue('weight')"
                      @input="(value) => setValue({ path: 'weight', value })"
                      :state="showError('weight')"
                    />
                    <b-form-invalid-feedback>{{ getError('weight') }}</b-form-invalid-feedback>
                  </b-form-group>
                </b-col>
              </b-row>
              <b-row>
                <b-col>
                  <b-form-group label="Free-Form Description of Composition" :label-for="descriptionId">
                    <b-form-textarea
                      type="text"
                      :id="descriptionId"
                      :value="getValue('description')"
                      @input="(value) => setValue({ path: 'description', value })"
                      :state="showError('description')"
                    />
                    <b-form-invalid-feedback>{{ getError('description') }}</b-form-invalid-feedback>
                  </b-form-group>
                </b-col>
              </b-row>
            </b-card>
          </b-col>
          <b-col>
            <b-row>
              <b-col>
                <b-card title="Core Information" border-variant="dark" bg-variant="light" class="mb-4">
                  <b-row>
                    <b-col>
                      <b-form-group label="Down Hole Core ID" :label-for="dhCoreIdId">
                        <b-form-input
                          type="text"
                          :id="dhCoreIdId"
                          :value="getValue('dhCoreId')"
                          @input="(value) => setValue({ path: 'dhCoreId', value })"
                          :state="showError('dhCoreId')"
                        />
                        <b-form-invalid-feedback>{{ getError('dhCoreId') }}</b-form-invalid-feedback>
                      </b-form-group>
                    </b-col>
                    <b-col>
                      <b-form-group label="Down Hole Core Interval" :label-for="dhCoreIntervalId">
                        <b-form-input
                          type="number"
                          min="1"
                          :id="dhCoreIntervalId"
                          :value="getValue('dhCoreInterval')"
                          @input="(value) => setValue({ path: 'dhCoreInterval', value })"
                          :state="showError('dhCoreInterval')"
                        />
                        <b-form-invalid-feedback>{{ getError('dhCoreInterval') }}</b-form-invalid-feedback>
                      </b-form-group>
                    </b-col>
                  </b-row>
                  <b-row>
                    <b-col>
                      <b-form-group label="Down Hole Core Length" :label-for="dhCoreLengthId">
                        <b-form-input
                          type="number"
                          step="any"
                          min="0"
                          :id="dhCoreLengthId"
                          :value="getValue('dhCoreLength')"
                          @input="(value) => setValue({ path: 'dhCoreLength', value })"
                          :state="showError('dhCoreLength')"
                        />
                        <b-form-invalid-feedback>{{ getError('dhCoreLength') }}</b-form-invalid-feedback>
                      </b-form-group>
                    </b-col>
                    <b-col>
                      <b-form-group label="Depth to Top in Down Hole Core" :label-for="dTopInDhCoreId">
                        <b-form-input
                          type="number"
                          step="any"
                          min="0"
                          :id="dTopInDhCoreId"
                          :value="getValue('dTopInDhCore')"
                          @input="(value) => setValue({ path: 'dTopInDhCore', value })"
                          :state="showError('dTopInDhCore')"
                        />
                        <b-form-invalid-feedback>{{ getError('dTopInDhCore') }}</b-form-invalid-feedback>
                      </b-form-group>
                    </b-col>
                    <b-col>
                      <b-form-group label="Depth to Bottom in Down Hole Core" :label-for="dBotInDhCoreId">
                        <b-form-input
                          type="number"
                          step="any"
                          min="0"
                          :id="dBotInDhCoreId"
                          :value="getValue('dBotInDhCore')"
                          @input="(value) => setValue({ path: 'dBotInDhCore', value })"
                          :state="showError('dBotInDhCore')"
                        />
                        <b-form-invalid-feedback>{{ getError('dBotInDhCore') }}</b-form-invalid-feedback>
                      </b-form-group>
                    </b-col>
                  </b-row>
                  <b-row>
                    <b-col>
                      <b-form-group label="Core Depth (Top)" :label-for="cdTopId">
                        <b-form-input
                          type="number"
                          step="any"
                          :id="cdTopId"
                          min="0"
                          :value="getValue('cdTop')"
                          @input="(value) => setValue({ path: 'cdTop', value })"
                          :state="showError('cdTop')"
                        />
                        <b-form-invalid-feedback>{{ getError('cdTop') }}</b-form-invalid-feedback>
                      </b-form-group>
                    </b-col>
                  </b-row>
                </b-card>
              </b-col>
            </b-row>
            <b-row>
              <b-col>
                <b-card title="Additional Descriptive Information" border-variant="dark" bg-variant="light" class="mb-4">
                  <b-form-group label="Subsample/Interval Not Available (exhausted)" :label-for="exhaustedId">
                    <b-form-checkbox
                      :id="exhaustedId"
                      :checked="getValue('exhausted')"
                      @change="(value) => setValue({ path: 'exhausted', value })"
                      :state="showError('exhausted')"
                    />
                    <b-form-invalid-feedback>{{ getError('exhausted') }}</b-form-invalid-feedback>
                  </b-form-group>
                  <b-form-group :label-for="intCommentsId">
                    <template #label>
                      Comments on Subsample/Interval<span style="color: gray"> (limit 2000 characters)</span>
                    </template>
                    <b-form-textarea
                      :id="intCommentsId"
                      :value="getValue('intComments')"
                      @input="(value) => setValue({ path: 'intComments', value })"
                      :state="showError('intComments')"
                    />
                    <b-form-invalid-feedback>{{ getError('intComments') }}</b-form-invalid-feedback>
                  </b-form-group>
                  <b-form-group label="Subsample/Interval (Child) IGSN" :label-for="igsnId">
                    <b-form-input
                      type="text"
                      :id="igsnId"
                      :value="getValue('igsn')"
                      @input="(value) => setValue({ path: 'igsn', value })"
                      :state="showError('igsn')"
                    />
                    <b-form-invalid-feedback>{{ getError('igsn') }}</b-form-invalid-feedback>
                  </b-form-group>
                </b-card>
              </b-col>
            </b-row>
          </b-col>
        </b-row>
        <div>
          <b-button v-if="showSubmit" type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">
            <b-icon icon="check" class="mr-2"/>Submit
          </b-button>
          <b-button v-if="formDirty" type="reset" variant="danger" class="mb-2 mr-sm-2 mb-sm-0">
            <b-icon icon="arrow-counterclockwise" class="mr-2"/>Reset
          </b-button>
        </div>
      </b-form>
    </div>
    <div v-else>
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
      imlgsId: null,
      intervalId: null,
      depthTopId: null,
      depthBotId: null,
      dhCoreIdId: null,
      dhCoreLengthId: null,
      dhCoreIntervalId: null,
      dTopInDhCoreId: null,
      dBotInDhCoreId: null,
      lithCode1Id: null,
      lithCode2Id: null,
      textCode1Id: null,
      textCode2Id: null,
      compCode1Id: null,
      compCode2Id: null,
      compCode3Id: null,
      compCode4Id: null,
      compCode5Id: null,
      compCode6Id: null,
      descriptionId: null,
      ageCodesId: null,
      weightId: null,
      rockLithCodeId: null,
      rockMinCodeId: null,
      weathMetaCodeId: null,
      remarkCodeId: null,
      munsellCodeId: null,
      exhaustedId: null,
      intCommentsId: null,
      cdTopId: null,
      igsnId: null,
    };
  },

  beforeMount() {
    this.imlgsId = genId();
    this.intervalId = genId();
    this.depthTopId = genId();
    this.depthBotId = genId();
    this.dhCoreIdId = genId();
    this.dhCoreLengthId = genId();
    this.dhCoreIntervalId = genId();
    this.dTopInDhCoreId = genId();
    this.dBotInDhCoreId = genId();
    this.lithCode1Id = genId();
    this.lithCode2Id = genId();
    this.textCode1Id = genId();
    this.textCode2Id = genId();
    this.compCode1Id = genId();
    this.compCode2Id = genId();
    this.compCode3Id = genId();
    this.compCode4Id = genId();
    this.compCode5Id = genId();
    this.compCode6Id = genId();
    this.descriptionId = genId();
    this.ageCodesId = genId();
    this.weightId = genId();
    this.rockLithCodeId = genId();
    this.rockMinCodeId = genId();
    this.weathMetaCodeId = genId();
    this.remarkCodeId = genId();
    this.munsellCodeId = genId();
    this.exhaustedId = genId();
    this.intCommentsId = genId();
    this.cdTopId = genId();
    this.igsnId = genId();
  },

  created() {
    this.loadOptions();
    if (this.id) {
      this.load(this.id).then(
        (sample) => {
          this.initialize(sample);
        },
      );
    } else {
      this.initialize();
    }
  },

  methods: {
    ...mapActions('providerInterval', ['delete', 'save', 'loadOptions', 'load']),
    ...mapActions('providerIntervalForm', ['submit', 'reset']),
    ...mapMutations('providerIntervalForm', ['initialize', 'setValue', 'deleteFromArray', 'addToArray', 'setTouched']),

    showModal() {
      this.$refs['delete-modal'].show();
    },

    doDelete() {
      this.delete(this.id).then(() => this.$router.push({ name: 'ProviderIntervalList' }));
    },

    saveForm() {
      this.submit().then(
        (sample) => this.save({ provider: sample, id: this.id }).then(
          () => this.$router.push({ name: 'ProviderIntervalList' }),
        ),
      );
    },

    setGeologicAgeValues(values) {
      const existingSize = this.getValue('ageCodes').length;
      for (let i = 0; i < existingSize; i += 1) {
        this.deleteFromArray('ageCodes[0]');
      }
      for (let k = 0; k < values.length; k += 1) {
        this.addToArray({ path: 'ageCodes', value: values[k] });
      }
    },
  },

  computed: {
    ...mapState('providerInterval', ['loading', 'saving', 'loadingOptions', 'options']),
    ...mapGetters('providerIntervalForm', ['getValue', 'isTouched', 'getError', 'formDirty', 'formHasUntouchedErrors']),
    ready() {
      return !this.loading && !this.loadingOptions && !this.saving;
    },

    isEdit() {
      return this.id || this.id === 0;
    },

    showError() {
      return (path) => ((!this.isTouched(path) && this.getError(path)) ? false : null);
    },

    showSubmit() {
      return this.formDirty && !this.formHasUntouchedErrors;
    },

    optionsLithologyCode() {
      const { lithologyCode: field } = this.options;
      return field || [];
    },

    optionsIMLGS() {
      const { imlgs: field } = this.options;
      return field || [];
    },

    optionsTextureCode() {
      const { textureCode: field } = this.options;
      return field || [];
    },

    optionsAgeCode() {
      const { ageCode: field } = this.options;
      return field || [];
    },

    optionsRockMineralCode() {
      const { rockMineralCode: field } = this.options;
      return field || [];
    },

    optionsWeathMetaCode() {
      const { weathMetaCode: field } = this.options;
      return field || [];
    },

    optionsRemarkCode() {
      const { remarkCode: field } = this.options;
      return field || [];
    },

    optionsMunsellCode() {
      const { munsellCode: field } = this.options;
      return field || [];
    },

    optionsDeviceCode() {
      const { deviceCode: field } = this.options;
      return field || [];
    },

    selectedAges() {
      const ages = this.getValue('ageCodes');
      if (!ages) {
        return [];
      }
      return ages.map((x) => x.value);
    },
  },
};
</script>
