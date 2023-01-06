<template>
<div>
  <b-form @submit.prevent="search" @reset.prevent="reset">
    <b-container fluid>
      <b-row>
        <b-col>
          <b-form-group label="RockLithology" :label-for="rockLithologyId">
            <b-form-input :id="rockLithologyId" v-model="rockLithology"/>
          </b-form-group>
        </b-col>
        <b-col>
          <b-form-group label="RockLithology Code" :label-for="rockLithologyCodeId">
            <b-form-input :id="rockLithologyCodeId" v-model="rockLithologyCode"/>
          </b-form-group>
        </b-col>
      </b-row>
    </b-container>

    <div v-if="!searching">
      <b-button type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">Search</b-button>
      <b-button type="reset" variant="danger" class="mb-2 mr-sm-2 mb-sm-0">Clear</b-button>
    </div>
  </b-form>
  <b-button :to="{ name: 'RockLithologyAdd' }" variant="secondary" class="m-3">Add New Rock Lithology</b-button>
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
    <template #cell(rockLithology)="data">
      <b-link :to="{ name: 'RockLithologyEdit', params: { id: data.item.rockLithology }}">{{ data.item.rockLithology }}</b-link>
    </template>
  </b-table>
  <TextPagination :updated="changePage" :page="currentPage" :total-items="totalItems" items-per-page="50" :total-pages="totalPages"/>
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
    this.rockLithologyId = genId();
    this.rockLithologyCodeId = genId();
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
    ...mapMutations('rockLithology', ['setRockLithology', 'clearParams', 'setRockLithologyCode', 'firstPage', 'setPage', 'setSortBy', 'setSortDesc', 'clearAll']),
    ...mapActions('rockLithology', ['search', 'reset', 'changePage']),
    sortChanged({ sortBy, sortDesc }) {
      this.setSortBy(sortBy);
      this.setSortDesc(sortDesc);
      this.search();
    },
  },

  computed: {
    ...mapState('rockLithology', ['searching', 'page', 'totalItems', 'totalPages', 'items', 'params', 'sortDesc', 'sortBy']),
    rockLithology: {
      get() {
        return this.params.rockLithology;
      },
      set(value) {
        this.setRockLithology(value);
      },
    },
    rockLithologyCode: {
      get() {
        return this.params.rockLithologyCode;
      },
      set(value) {
        this.setRockLithologyCode(value);
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
      rockLithologyId: null,
      rockLithologyCodeId: null,

      fields: [
        {
          key: 'rockLithology',
          label: 'RockLithology',
          sortable: true,
        },
        {
          key: 'rockLithologyCode',
          label: 'RockLithology Code',
          sortable: true,
        },
      ],
    };
  },
};
</script>
