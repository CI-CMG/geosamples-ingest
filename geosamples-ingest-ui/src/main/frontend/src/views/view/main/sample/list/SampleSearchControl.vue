<template>
  <b-card>
    <b-modal ref="search-modal" title="Search" hide-footer size="xl">
      <b-form @submit.prevent="saveForm" @reset.prevent="reset">
        <b-container>
          <b-row>
            <SearchCardCol title="IMLGS" field="imlgs" module="sampleSearchForm"/>
            <SearchCardCol title="Cruise Contains" field="cruise" module="sampleSearchForm"/>
            <SearchCardCol title="Sample Contains" field="sample" module="sampleSearchForm"/>
          </b-row>
          <b-row>
            <SearchCardColSelect title="Facility" field="facilityCode" :options="optionsFacilityCode" module="sampleSearchForm"/>
            <SearchCardCol title="Platform Contains" field="platform" module="sampleSearchForm"/>
            <SearchCardColSelect title="Device" field="deviceCode" :options="optionsDeviceCode" module="sampleSearchForm"/>
          </b-row>
          <b-row>
            <SearchCardCol title="IGSN" field="igsn" module="sampleSearchForm"/>
            <SeachCardBoundingBox title="Within Area" module="sampleSearchForm"/>
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
      module="sampleSortForm"
      />
    </b-modal>
    <b-button variant="secondary" class="mb-2 mr-sm-2 mb-sm-0 mr-3" @click="showSearch">Search</b-button>
    <b-button variant="secondary" class="mb-2 mr-sm-2 mb-sm-0 mr-3" @click="showSort">Sort</b-button>
    <b-badge v-for="badge in badges" :key="badge" pill variant="info">{{badge}}</b-badge>
  </b-card>
</template>

<script>
import {
  mapActions, mapGetters, mapMutations, mapState,
} from 'vuex';
import SearchCardCol from '@/components/SearchCardCol.vue';
import SearchCardColSelect from '@/components/SearchCardColSelect.vue';
import SortModal from '@/components/SortModal.vue';
import SeachCardBoundingBox from '@/components/SearchCardBoundingBox.vue';

const concatQuoted = (list) => list.map((v) => `'${v}'`).join(',');
const concat = (list) => list.join(',');
const eq = (prefix, list) => (list && list.length ? `${prefix} = ${concat(list)}` : null);
const contains = (prefix, list) => (list && list.length ? `${prefix} Contains ${concatQuoted(list)}` : null);

export default {
  components: {
    SearchCardCol,
    SearchCardColSelect,
    SortModal,
    SeachCardBoundingBox,
  },
  props: ['onSearch', 'onSort'],
  computed: {
    ...mapGetters('sampleSearchForm',
      [
        'getValue',
        'formDirty',
        'getError',
        'isTouched',
        'formHasUntouchedErrors',
      ]),
    ...mapState('sample', ['searchParameters', 'sortParameters', 'options', 'sortParameters']),
    optionsDeviceCode() {
      const { deviceCode: field } = this.options;
      return field || [];
    },
    optionsFacilityCode() {
      const { facilityCode: field } = this.options;
      return field || [];
    },

    showError() {
      return (path) => ((!this.isTouched(path) && this.getError(path)) ? false : null);
    },
    showSubmit() {
      return this.formDirty && !this.formHasUntouchedErrors;
    },

    cruiseContainsText() {
      return contains('Cruise', this.searchParameters ? this.searchParameters.cruise : []);
    },
    sampleContainsText() {
      return contains('Sample', this.searchParameters ? this.searchParameters.sample : []);
    },
    facilityCodeText() {
      return eq('Facility', this.searchParameters ? this.searchParameters.facilityCode : []);
    },
    platformContainsText() {
      return contains('Platform', this.searchParameters ? this.searchParameters.platform : []);
    },
    deviceCodeText() {
      return eq('Device', this.searchParameters ? this.searchParameters.deviceCode : []);
    },
    igsnText() {
      return eq('IGSN', this.searchParameters ? this.searchParameters.igsn : []);
    },
    imlgsText() {
      return eq('IMLGS', this.searchParameters ? this.searchParameters.imlgs : []);
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
      if (this.igsnText) {
        badges.push(this.igsnText);
      }
      if (this.imlgsText) {
        badges.push(this.imlgsText);
      }
      return badges;
    },
  },
  methods: {
    ...mapMutations('sampleSearchForm',
      [
        'initialize',
        'setValue',
        'setTouched',
        'setError',
        'deleteFromArray',
        'addToArray',
      ]),
    ...mapMutations({
      sortInitialize: 'sampleSortForm/initialize',
    }),
    ...mapActions('sampleSearchForm', ['submit', 'reset']),
    ...mapActions('sample', ['loadOptions']),

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
  },
};
</script>
