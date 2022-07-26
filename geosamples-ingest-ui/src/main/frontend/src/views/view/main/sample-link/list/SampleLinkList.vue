<template>
<div>
  <b-form @submit.prevent="search" @reset.prevent="reset">
    <b-container fluid>
      <b-row>
        <b-col>
          <b-form-group label="Sample IMLGS" :label-for="imlgsId">
            <b-form-input :id="imlgsId" v-model="imlgs"/>
          </b-form-group>
        </b-col>
      </b-row>
    </b-container>

    <div v-if="!searching">
      <b-button type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">Search</b-button>
      <b-button type="reset" variant="danger" class="mb-2 mr-sm-2 mb-sm-0">Clear</b-button>
    </div>
  </b-form>
  <b-button :to="{ name: 'SampleLinkAdd' }" variant="secondary" class="m-3">Add New Sample Link</b-button>
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
    <template #cell(userName)="data">
      <b-link :to="{ name: 'SampleLinkEdit', params: { id: data.item.sampleLink }}">{{ data.item.sampleLink }}</b-link>
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
    this.sampleLinkId = genId();
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
    ...mapMutations('sampleLink', ['setSampleLink', 'clearParams', 'firstPage', 'setPage', 'setSortBy', 'setSortDesc', 'clearAll']),
    ...mapActions('sampleLink', ['search', 'reset', 'changePage']),
    sortChanged({ sortBy, sortDesc }) {
      this.setSortBy(sortBy);
      this.setSortDesc(sortDesc);
      this.search();
    },
  },

  computed: {
    ...mapState('sampleLink', ['searching', 'page', 'totalItems', 'totalPages', 'items', 'params', 'sortDesc', 'sortBy']),
    sampleLink: {
      get() {
        return this.params.sampleLink;
      },
      set(value) {
        this.setsampleLink(value);
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
      imlgsId: null,

      fields: [
        {
          key: 'IMLGS',
          label: 'IMLGS',
          sortable: true,
        },
      ],
    };
  },
};
</script>
