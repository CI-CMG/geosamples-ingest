<template>
  <div>
    <b-form @submit.prevent="search" @reset.prevent="reset">
      <b-container fluid>
        <b-row>
          <b-col>
            <b-form-group label="Name Contains" :label-for="cruiseNameContainsId">
              <b-form-input :id="cruiseNameContainsId" v-model="cruiseNameContains"/>
            </b-form-group>
          </b-col>
          <b-col>
            <b-form-group label="Name Equals" :label-for="cruiseNameEqualsId">
              <b-form-input :id="cruiseNameEqualsId" v-model="cruiseNameEquals"/>
            </b-form-group>
          </b-col>
          <b-col>
            <b-form-group label="Year" :label-for="yearId">
              <b-form-input :id="yearId" v-model="year"/>
            </b-form-group>
          </b-col>
          <b-col>
            <b-form-group label="Publish" :label-for="publishId">
              <b-form-input :id="publishId" v-model="publish"/>
            </b-form-group>
          </b-col>
        </b-row>
        <b-row>
          <b-col>
            <b-form-group label="Facility Code Equals" :label-for="facilityCodeEqualsId">
              <b-form-input :id="facilityCodeEqualsId" v-model="facilityCodeEquals"/>
            </b-form-group>
          </b-col>
          <b-col>
            <b-form-group label="Platform Equals" :label-for="platformEqualsId">
              <b-form-input :id="platformEqualsId" v-model="platformEquals"/>
            </b-form-group>
          </b-col>
          <b-col>
            <b-form-group label="ID" :label-for="idId">
              <b-form-input :id="idId" v-model="id"/>
            </b-form-group>
          </b-col>
        </b-row>
      </b-container>

      <div v-if="!searching">
        <b-button type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">Search</b-button>
        <b-button type="reset" variant="danger" class="mb-2 mr-sm-2 mb-sm-0">Clear</b-button>
      </div>
    </b-form>
    <b-button :to="{ name: 'CruiseAdd' }" variant="secondary" class="m-3">Add New Cruise</b-button>
    <b-table
      sticky-header="500px"
      head-variant="dark"
      striped
      bordered
      small
      hover
      :items="items"
      :fields="fields"
      no-local-sorting
      @sort-changed="sortChanged"
      :sort-by="sortBy"
      :sort-desc="sortDesc">
      <template #cell(cruiseName)="data">
        <b-link :to="{ name: 'CruiseEdit', params: { id: data.item.id }}">{{ data.item.cruiseName }}</b-link>
      </template>
    </b-table>
    <TextPagination :updated="changePage" :page="currentPage" :total-items="totalItems" items-per-page="50"/>
  </div>
</template>

<script>

import genId from '@/components/idGenerator';
import {
  mapActions, mapMutations, mapState,
} from 'vuex';
import TextPagination from '@/components/TextPagination.vue';

export default {
  components: {
    TextPagination,
  },

  beforeMount() {
    this.cruiseNameContainsId = genId();
    this.cruiseNameEqualsId = genId();
    this.yearId = genId();
    this.publishId = genId();
    this.facilityCodeEqualsId = genId();
    this.platformEqualsId = genId();
    this.idId = genId();
  },
  beforeRouteEnter(to, from, next) {
    next((self) => {
      self.search();
    });
  },
  beforeRouteUpdate(to, from, next) {
    this.search();
    next();
  },
  beforeRouteLeave(to, from, next) {
    this.clearAll();
    next();
  },
  methods: {
    ...mapMutations('cruise', [
      'setCruiseNameContains',
      'setPlatformEquals',
      'setYear',
      'setPublish',
      'setFacilityCodeEquals',
      'setPlatformEquals',
      'setId',
      'clearParams',
      'firstPage',
      'setPage',
      'setSortBy',
      'setSortDesc',
      'clearAll']),
    ...mapActions('cruise', ['search', 'reset', 'changePage']),
    sortChanged({ sortBy, sortDesc }) {
      this.setSortBy(sortBy);
      this.setSortDesc(sortDesc);
      this.search();
    },
  },

  computed: {
    ...mapState('cruise', ['searching', 'page', 'totalItems', 'totalPages', 'items', 'params', 'sortDesc', 'sortBy']),
    id: {
      get() {
        return this.params.id;
      },
      set(value) {
        this.setId(value);
      },
    },
    facilityCodeEquals: {
      get() {
        return this.params.facilityCodeEquals;
      },
      set(value) {
        this.setFacilityCodeEquals(value);
      },
    },
    platformEquals: {
      get() {
        return this.params.platformEquals;
      },
      set(value) {
        this.setPlatformEquals(value);
      },
    },
    year: {
      get() {
        return this.params.year;
      },
      set(value) {
        this.setYear(value);
      },
    },
    publish: {
      get() {
        return this.params.publish;
      },
      set(value) {
        this.setPublish(value);
      },
    },
    cruiseNameContains: {
      get() {
        return this.params.cruiseNameContains;
      },
      set(value) {
        this.setCruiseNameContains(value);
      },
    },
    cruiseNameEquals: {
      get() {
        return this.params.cruiseNameEquals;
      },
      set(value) {
        this.setCruiseNameEquals(value);
      },
    },
    currentPage: {
      get() {
        return this.page;
      },
      set(value) {
        this.setPage(value);
      },
    },
  },

  data() {
    return {
      cruiseNameContainsId: null,
      cruiseNameEqualsId: null,
      yearId: null,
      publishId: null,
      facilityCodeEqualsId: null,
      platformEqualsId: null,
      idId: null,

      fields: [
        {
          key: 'cruiseName',
          label: 'Cruise Name',
          sortable: true,
        },
        {
          key: 'year',
          label: 'Year',
          sortable: true,
        },
        {
          key: 'publish',
          label: 'Publish',
          sortable: true,
        },
        {
          key: 'facilityCodes',
          label: 'Facilities',
          sortable: true,
        },
        {
          key: 'platforms',
          label: 'Platforms',
          sortable: true,
        },
      ],
    };
  },
};
</script>
