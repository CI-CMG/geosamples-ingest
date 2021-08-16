<template>
<div>
  <b-form @submit.prevent="search" @reset.prevent="reset">
    <b-container fluid>
      <b-row>
        <b-col>
          <b-form-group label="Storage Method" :label-for="storageMethodId">
            <b-form-input :id="storageMethodId" v-model="storageMethod"/>
          </b-form-group>
        </b-col>
        <b-col>
          <b-form-group label="Storage Method Code" :label-for="storageMethodCodeId">
            <b-form-input :id="storageMethodCodeId" v-model="storageMethodCode"/>
          </b-form-group>
        </b-col>
      </b-row>
    </b-container>

    <div v-if="!searching">
      <b-button type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">Search</b-button>
      <b-button type="reset" variant="danger" class="mb-2 mr-sm-2 mb-sm-0">Clear</b-button>
    </div>
  </b-form>
  <b-button :to="{ name: 'StorageMethodAdd' }" variant="secondary" class="m-3">Add New Storage Method</b-button>
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
    <template #cell(storageMethod)="data">
      <b-link :to="{ name: 'StorageMethodEdit', params: { id: data.item.storageMethod }}">{{ data.item.storageMethod }}</b-link>
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
    this.storageMethodId = genId();
    this.storageMethodCodeId = genId();
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
    ...mapMutations('storageMethod', ['setStorageMethod', 'clearParams', 'setStorageMethodCode', 'firstPage', 'setPage', 'setSortBy', 'setSortDesc', 'clearAll']),
    ...mapActions('storageMethod', ['search', 'reset', 'changePage']),
    sortChanged({ sortBy, sortDesc }) {
      this.setSortBy(sortBy);
      this.setSortDesc(sortDesc);
      this.search();
    },
  },

  computed: {
    ...mapState('storageMethod', ['searching', 'page', 'totalItems', 'totalPages', 'items', 'params', 'sortDesc', 'sortBy']),
    storageMethod: {
      get() {
        return this.params.storageMethod;
      },
      set(value) {
        this.setStorageMethod(value);
      },
    },
    storageMethodCode: {
      get() {
        return this.params.storageMethodCode;
      },
      set(value) {
        this.setStorageMethodCode(value);
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
      storageMethodId: null,
      storageMethodCodeId: null,

      fields: [
        {
          key: 'storageMethod',
          label: 'Storage Method',
          sortable: true,
        },
        {
          key: 'storageMethodCode',
          label: 'Storage Method Code',
          sortable: true,
        },
      ],
    };
  },
};
</script>
