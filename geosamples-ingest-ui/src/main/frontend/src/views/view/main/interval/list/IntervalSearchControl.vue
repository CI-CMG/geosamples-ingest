<template>
  <b-card>
    <b-modal ref="search-modal" title="Search" hide-footer size="xl">
      <b-form @submit.prevent="saveForm" @reset.prevent="reset">
        <b-container>
          <b-row>
            <SearchCardCol title="Cruise Contains" field="cruiseContains" module="intervalSearchForm"/>
            <SearchCardCol title="Sample Contains" field="sampleContains" module="intervalSearchForm"/>
            <SearchCardColSelect title="Facility" field="facilityCode" :options="optionsFacilityCode" module="intervalSearchForm"/>
          </b-row>
          <b-row>
            <SearchCardCol title="Platform Contains" field="platformContains" module="intervalSearchForm"/>
            <SearchCardColSelect title="Device" field="deviceCode" :options="optionsDeviceCode" module="intervalSearchForm"/>
            <SearchCardCol title="Date" field="date" module="intervalSearchForm"/>
          </b-row>
          <b-row>
            <SearchCardColSelect title="Storage Method" field="storageMethodCode" :options="optionsStorageMethodCode" module="intervalSearchForm"/>
            <SearchCardCol title="Principal Investigator Contains" field="piContains" module="intervalSearchForm"/>
            <SearchCardColSelect title="Physiographic Province" field="provinceCode" :options="optionsProvinceCode" module="intervalSearchForm"/>
          </b-row>
          <b-row>
            <SearchCardCol title="IGSN" field="igsn" module="intervalSearchForm"/>
            <SearchCardCol title="IMLGS" field="imlgs" module="intervalSearchForm"/>
            <SearchCardCol title="Interval" field="interval" module="intervalSearchForm"/>
          </b-row>
          <b-row>
            <SearchCardColSingleSelect title="Publish" field="publish" :options="['', 'true', 'false']" module="intervalSearchForm"/>
            <SearchCardColSelect title="Lithologic Composition" field="lithCode" :options="optionsLithologyCode" module="intervalSearchForm"/>
            <SearchCardColSelect title="Texture" field="textCode" :options="optionsTextureCode" module="intervalSearchForm"/>
          </b-row>
          <b-row>
            <SearchCardColSelect title="Age" field="ageCode" :options="optionsAgeCode" module="intervalSearchForm"/>
            <SearchCardColSelect title="Rock Lithology" field="rockLithCode" :options="optionsRockLithologyCode" module="intervalSearchForm"/>
            <SearchCardColSelect title="Rock Mineralogy" field="rockMinCode" :options="optionsRockMineralCode" module="intervalSearchForm"/>
          </b-row>
          <b-row>
            <SearchCardColSelect title="Weathering/Metamorphism" field="weathMetaCode" :options="optionsWeatheringCode" module="intervalSearchForm"/>
            <SearchCardColSelect title="Munsell Color" field="munsellCode" :options="optionsMunsellCode" module="intervalSearchForm"/>
            <SearchCardBoundingBox title="Within Area" module="intervalSearchForm"/>
          </b-row>
          <b-row>
            <SearchCardColSelect title="Rock Glass Remarks & Mn/Fe Oxide" field="remarkCode" :options="optionsRemarkCode" module="intervalSearchForm"/>
            <SearchCardColSelect title="Approval State" field="approvalState" :options="['PENDING', 'REJECTED', 'APPROVED']" module="intervalSearchForm"/>
          </b-row>
        </b-container>

        <div>
          <b-button v-if="showSubmit" type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">Search</b-button>
          <b-button v-if="formDirty" type="reset" variant="secondary" class="mb-2 mr-sm-2 mb-sm-0">Reset</b-button>
        </div>

      </b-form>
    </b-modal>
    <b-modal ref="sort-modal" title="Sort"  hide-footer size="xl">
      <SortModal
      :hideSort="hideSort"
      :onSort="onSort"
       module="intervalSortForm"
      />
    </b-modal>
    <b-button variant="secondary" class="mb-2 mr-sm-2 mb-sm-0 mr-3" @click="showSearch">
      <b-icon class="mr-2" icon="search"/>Search
    </b-button>
    <b-button variant="secondary" class="mb-2 mr-sm-2 mb-sm-0 mr-3" @click="showSort">
      <b-icon class="mr-2" icon="sort-up"/>Sort
    </b-button>
    <b-button variant="secondary" class="mb-2 mr-sm-2 mb-sm-0 mr-3" @click="download">
      <b-icon class="mr-2" icon="download"/>Download
    </b-button>
    <b-badge v-for="badge in badges" :key="badge" pill variant="info">{{badge}}</b-badge>
  </b-card>
</template>

<script>
import {
  mapActions, mapGetters, mapMutations, mapState,
} from 'vuex';
import SearchCardCol from '@/components/SearchCardCol.vue';
import SearchCardColSelect from '@/components/SearchCardColSelect.vue';
import SearchCardColSingleSelect from '@/components/SearchCardColSingleSelect.vue';
import SortModal from '@/components/SortModal.vue';
import SearchCardBoundingBox from '@/components/SearchCardBoundingBox.vue';

const concatQuoted = (list) => list.map((v) => `'${v}'`).join(',');
const concat = (list) => list.join(',');
const eq = (prefix, list) => (list && list.length ? `${prefix} = ${concat(list)}` : null);
const contains = (prefix, list) => (list && list.length ? `${prefix} Contains ${concatQuoted(list)}` : null);

export default {
  components: {
    SearchCardCol,
    SearchCardColSelect,
    SearchCardColSingleSelect,
    SortModal,
    SearchCardBoundingBox,
  },
  props: ['onSearch', 'onSort'],
  computed: {
    ...mapGetters('intervalSearchForm',
      [
        'getValue',
        'formDirty',
        'getError',
        'isTouched',
        'formHasUntouchedErrors',
      ]),
    ...mapState('interval', ['searchParameters', 'sortParameters', 'options', 'sortParameters']),
    optionsAgeCode() {
      const { ageCode: field } = this.options;
      return field || [];
    },
    optionsDeviceCode() {
      const { deviceCode: field } = this.options;
      return field || [];
    },
    optionsFacilityCode() {
      const { facilityCode: field } = this.options;
      return field || [];
    },
    optionsLithologyCode() {
      const { lithCode: field } = this.options;
      return field || [];
    },
    optionsMunsellCode() {
      const { munsellCode: field } = this.options;
      return field || [];
    },
    optionsProvinceCode() {
      const { provinceCode: field } = this.options;
      return field || [];
    },
    optionsRemarkCode() {
      const { remarkCode: field } = this.options;
      return field || [];
    },
    optionsRockLithologyCode() {
      const { rockLithCode: field } = this.options;
      return field || [];
    },
    optionsRockMineralCode() {
      const { rockMinCode: field } = this.options;
      return field || [];
    },
    optionsStorageMethodCode() {
      const { storageMethodCode: field } = this.options;
      return field || [];
    },
    optionsTextureCode() {
      const { textCode: field } = this.options;
      return field || [];
    },
    optionsWeatheringCode() {
      const { weathMetaCode: field } = this.options;
      return field || [];
    },
    showError() {
      return (path) => ((!this.isTouched(path) && this.getError(path)) ? false : null);
    },
    showSubmit() {
      return this.formDirty && !this.formHasUntouchedErrors;
    },
    cruiseContainsText() {
      return contains('Cruise', this.searchParameters ? this.searchParameters.cruiseContains : []);
    },
    sampleContainsText() {
      return contains('Sample', this.searchParameters ? this.searchParameters.sampleContains : []);
    },
    facilityCodeText() {
      return eq('Facility', this.searchParameters ? this.searchParameters.facilityCode : []);
    },
    platformContainsText() {
      return contains('Platform', this.searchParameters ? this.searchParameters.platformContains : []);
    },
    deviceCodeText() {
      return eq('Device', this.searchParameters ? this.searchParameters.deviceCode : []);
    },
    dateText() {
      return eq('Date', this.searchParameters ? this.searchParameters.date : []);
    },
    storageMethodCodeText() {
      return eq('Storage Method', this.searchParameters ? this.searchParameters.storageMethodCode : []);
    },
    piText() {
      return contains('Principal Investigator', this.searchParameters ? this.searchParameters.piContains : []);
    },
    provinceCodeText() {
      return eq('Physiographic Province', this.searchParameters ? this.searchParameters.provinceCode : []);
    },
    igsnText() {
      return eq('IGSN', this.searchParameters ? this.searchParameters.igsn : []);
    },
    imlgsText() {
      return eq('IMLGS', this.searchParameters ? this.searchParameters.imlgs : []);
    },
    intervalText() {
      return eq('Interval', this.searchParameters ? this.searchParameters.interval : []);
    },
    publishText() {
      const publish = [];
      if (this.searchParameters && this.searchParameters.publish != null) {
        publish.push(this.searchParameters.publish);
      }
      return eq('Publish', publish);
    },
    lithCodeText() {
      return eq('Lithologic Composition', this.searchParameters ? this.searchParameters.lithCode : []);
    },
    textCodeText() {
      return eq('Texture', this.searchParameters ? this.searchParameters.textCode : []);
    },
    ageCodeText() {
      return eq('Age', this.searchParameters ? this.searchParameters.ageCode : []);
    },
    rockLithCodeText() {
      return eq('Rock Lithology', this.searchParameters ? this.searchParameters.rockLithCode : []);
    },
    rockMinCodeText() {
      return eq('Rock Mineralogy', this.searchParameters ? this.searchParameters.rockMinCode : []);
    },
    weathMetaCodeText() {
      return eq('Weathering/Metamorphism', this.searchParameters ? this.searchParameters.weathMetaCode : []);
    },
    remarkCodeText() {
      return eq('Rock Glass Remarks & Mn/Fe Oxide', this.searchParameters ? this.searchParameters.remarkCode : []);
    },
    munsellCodeText() {
      return eq('Munsell Color', this.searchParameters ? this.searchParameters.munsellCode : []);
    },

    badges() {
      const badges = [];
      if (this.cruiseContainsText) {
        badges.push(this.cruiseContainsText);
      }
      if (this.sampleContainsText) {
        badges.push(this.sampleContainsText);
      }
      if (this.facilityCodeText) {
        badges.push(this.facilityCodeText);
      }
      if (this.platformContainsText) {
        badges.push(this.platformContainsText);
      }
      if (this.deviceCodeText) {
        badges.push(this.deviceCodeText);
      }
      if (this.dateText) {
        badges.push(this.dateText);
      }
      if (this.storageMethodCodeText) {
        badges.push(this.storageMethodCodeText);
      }
      if (this.piText) {
        badges.push(this.piText);
      }
      if (this.provinceCodeText) {
        badges.push(this.provinceCodeText);
      }
      if (this.igsnText) {
        badges.push(this.igsnText);
      }
      if (this.imlgsText) {
        badges.push(this.imlgsText);
      }
      if (this.intervalText) {
        badges.push(this.intervalText);
      }
      if (this.publishText) {
        badges.push(this.publishText);
      }
      if (this.lithCodeText) {
        badges.push(this.lithCodeText);
      }
      if (this.textCodeText) {
        badges.push(this.textCodeText);
      }
      if (this.ageCodeText) {
        badges.push(this.ageCodeText);
      }
      if (this.rockLithCodeText) {
        badges.push(this.rockLithCodeText);
      }
      if (this.rockMinCodeText) {
        badges.push(this.rockMinCodeText);
      }
      if (this.weathMetaCodeText) {
        badges.push(this.weathMetaCodeText);
      }
      if (this.remarkCodeText) {
        badges.push(this.remarkCodeText);
      }
      if (this.munsellCodeText) {
        badges.push(this.munsellCodeText);
      }

      return badges;
    },
  },
  methods: {
    ...mapMutations('intervalSearchForm',
      [
        'initialize',
        'setValue',
        'setTouched',
        'setError',
        'deleteFromArray',
        'addToArray',
      ]),
    ...mapMutations({
      sortInitialize: 'intervalSortForm/initialize',
    }),
    ...mapActions('intervalSearchForm', ['submit', 'reset']),
    ...mapActions('interval', ['loadOptions', 'downloadSamplesIntervals']),
    ...mapActions('userAuth', ['generateJwt']),
    showSearch() {
      this.loadOptions();
      this.initialize(this.searchParameters);
      this.$refs['search-modal'].show();
    },
    hideSearch() {
      this.$refs['search-modal'].hide();
    },
    showSort() {
      this.sortInitialize(this.sortParameters);
      this.$refs['sort-modal'].show();
    },
    hideSort() {
      this.$refs['sort-modal'].hide();
    },
    saveForm() {
      this.submit()
        .then((searchParameters) => {
          this.hideSearch();
          this.onSearch(searchParameters);
        });
    },
    download() {
      this.generateJwt().then(
        (response) => this.downloadSamplesIntervals(response.token),
      );
    },
  },
};
</script>
