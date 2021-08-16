<template>
<div>
  <b-form @submit.prevent="search" @reset.prevent="reset">
    <b-container fluid>
      <b-row>
        <b-col>
          <b-form-group label="Rock Mineral" :label-for="rockMineralId">
            <b-form-input :id="rockMineralId" v-model="rockMineral"/>
          </b-form-group>
        </b-col>
        <b-col>
          <b-form-group label="Rock Mineral Code" :label-for="rockMineralCodeId">
            <b-form-input :id="rockMineralCodeId" v-model="rockMineralCode"/>
          </b-form-group>
        </b-col>
      </b-row>
    </b-container>

    <div v-if="!searching">
      <b-button type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">Search</b-button>
      <b-button type="reset" variant="danger" class="mb-2 mr-sm-2 mb-sm-0">Clear</b-button>
    </div>
  </b-form>
  <b-button :to="{ name: 'RockMineralAdd' }" variant="secondary" class="m-3">Add New Rock Mineral</b-button>
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
    <template #cell(rockMineral)="data">
      <b-link :to="{ name: 'RockMineralEdit', params: { id: data.item.rockMineral }}">{{ data.item.rockMineral }}</b-link>
    </template>
  </b-table>
  <b-pagination v-model="currentPage" @input="changePage" :total-rows="totalItems" per-page="50"></b-pagination>
</div>
</template>

<script>

import genId from '@/components/idGenerator';
import {
  mapActions, mapMutations, mapState,
} from 'vuex';

export default {
  beforeMount() {
    this.rockMineralId = genId();
    this.rockMineralCodeId = genId();
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
    ...mapMutations('rockMineral', ['setRockMineral', 'clearParams', 'setRockMineralCode', 'firstPage', 'setPage', 'setSortBy', 'setSortDesc', 'clearAll']),
    ...mapActions('rockMineral', ['search', 'reset', 'changePage']),
    sortChanged({ sortBy, sortDesc }) {
      this.setSortBy(sortBy);
      this.setSortDesc(sortDesc);
      this.search();
    },
  },

  computed: {
    ...mapState('rockMineral', ['searching', 'page', 'totalItems', 'totalPages', 'items', 'params', 'sortDesc', 'sortBy']),
    rockMineral: {
      get() {
        return this.params.rockMineral;
      },
      set(value) {
        this.setRockMineral(value);
      },
    },
    rockMineralCode: {
      get() {
        return this.params.rockMineralCode;
      },
      set(value) {
        this.setRockMineralCode(value);
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
      rockMineralId: null,
      rockMineralCodeId: null,

      fields: [
        {
          key: 'rockMineral',
          label: 'Rock Mineral',
          sortable: true,
        },
        {
          key: 'rockMineralCode',
          label: 'Rock Mineral Code',
          sortable: true,
        },
      ],
    };
  },
};
</script>
