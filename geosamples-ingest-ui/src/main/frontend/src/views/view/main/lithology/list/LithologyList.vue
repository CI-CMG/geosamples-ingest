<template>
<div>
  <b-form @submit.prevent="search" @reset.prevent="reset">
    <b-container fluid>
      <b-row>
        <b-col>
          <b-form-group label="Lithologic Composition" :label-for="lithologyId">
            <b-form-input :id="lithologyId" v-model="lithology"/>
          </b-form-group>
        </b-col>
        <b-col>
          <b-form-group label="Lithologic Composition Code" :label-for="lithologyCodeId">
            <b-form-input :id="lithologyCodeId" v-model="lithologyCode"/>
          </b-form-group>
        </b-col>
      </b-row>
    </b-container>

    <div v-if="!searching">
      <b-button type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">Search</b-button>
      <b-button type="reset" variant="danger" class="mb-2 mr-sm-2 mb-sm-0">Clear</b-button>
    </div>
  </b-form>
  <b-button :to="{ name: 'LithologyAdd' }" variant="secondary" class="m-3">Add New Lithologic Composition</b-button>
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
    <template #cell(lithology)="data">
      <b-link :to="{ name: 'LithologyEdit', params: { id: data.item.lithology }}">{{ data.item.lithology }}</b-link>
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
    this.lithologyId = genId();
    this.lithologyCodeId = genId();
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
    ...mapMutations('lithology', ['setLithology', 'clearParams', 'setLithologyCode', 'firstPage', 'setPage', 'setSortBy', 'setSortDesc', 'clearAll']),
    ...mapActions('lithology', ['search', 'reset', 'changePage']),
    sortChanged({ sortBy, sortDesc }) {
      this.setSortBy(sortBy);
      this.setSortDesc(sortDesc);
      this.search();
    },
  },

  computed: {
    ...mapState('lithology', ['searching', 'page', 'totalItems', 'totalPages', 'items', 'params', 'sortDesc', 'sortBy']),
    lithology: {
      get() {
        return this.params.lithology;
      },
      set(value) {
        this.setLithology(value);
      },
    },
    lithologyCode: {
      get() {
        return this.params.lithologyCode;
      },
      set(value) {
        this.setLithologyCode(value);
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
      lithologyId: null,
      lithologyCodeId: null,

      fields: [
        {
          key: 'lithology',
          label: 'Lithologic Composition',
          sortable: true,
        },
        {
          key: 'lithologyCode',
          label: 'Lithologic Composition Code',
          sortable: true,
        },
      ],
    };
  },
};
</script>
