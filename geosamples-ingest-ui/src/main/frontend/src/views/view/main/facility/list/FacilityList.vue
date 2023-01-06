<template>
<div>
  <b-form @submit.prevent="search" @reset.prevent="reset">
    <b-container fluid>
      <b-row>
        <b-col>
          <b-form-group label="Facility" :label-for="facilityId">
            <b-form-input :id="facilityId" v-model="facility"/>
          </b-form-group>
        </b-col>
        <b-col>
          <b-form-group label="Facility Code" :label-for="facilityCodeId">
            <b-form-input :id="facilityCodeId" v-model="facilityCode"/>
          </b-form-group>
        </b-col>
      </b-row>
    </b-container>

    <div v-if="!searching">
      <b-button type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">Search</b-button>
      <b-button type="reset" variant="danger" class="mb-2 mr-sm-2 mb-sm-0">Clear</b-button>
    </div>
  </b-form>
  <b-button :to="{ name: 'FacilityAdd' }" variant="secondary" class="m-3">Add New Facility</b-button>
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
    <template #cell(facilityCode)="data">
      <b-link :to="{ name: 'FacilityEdit', params: { id: data.item.id }}">{{ data.item.facilityCode }}</b-link>
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
    this.facilityId = genId();
    this.facilityCodeId = genId();
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
    ...mapMutations('facility', ['setFacility', 'clearParams', 'setId', 'setFacilityCode', 'firstPage', 'setPage', 'setSortBy', 'setSortDesc', 'clearAll']),
    ...mapActions('facility', ['search', 'reset', 'changePage']),
    sortChanged({ sortBy, sortDesc }) {
      this.setSortBy(sortBy);
      this.setSortDesc(sortDesc);
      this.search();
    },
  },

  computed: {
    ...mapState('facility', ['searching', 'page', 'totalItems', 'totalPages', 'items', 'params', 'sortDesc', 'sortBy']),
    id: {
      get() {
        return this.params.id;
      },
      set(value) {
        this.setId(value);
      },
    },
    facility: {
      get() {
        return this.params.facility;
      },
      set(value) {
        this.setFacility(value);
      },
    },
    facilityCode: {
      get() {
        return this.params.facilityCode;
      },
      set(value) {
        this.setFacilityCode(value);
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
      facilityId: null,
      facilityCodeId: null,

      fields: [
        {
          key: 'facilityCode',
          label: 'Facility Code',
          sortable: true,
        },
        {
          key: 'facility',
          label: 'Facility',
          sortable: true,
        },
      ],
    };
  },
};
</script>
