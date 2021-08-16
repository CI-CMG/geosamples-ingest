<template>
<div>
  <b-form @submit.prevent="search" @reset.prevent="reset">
    <b-container fluid>
      <b-row>
        <b-col>
          <b-form-group label="Munsell Color" :label-for="munsellId">
            <b-form-input :id="munsellId" v-model="munsell"/>
          </b-form-group>
        </b-col>
        <b-col>
          <b-form-group label="Munsell Color Code" :label-for="munsellCodeId">
            <b-form-input :id="munsellCodeId" v-model="munsellCode"/>
          </b-form-group>
        </b-col>
      </b-row>
    </b-container>

    <div v-if="!searching">
      <b-button type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">Search</b-button>
      <b-button type="reset" variant="danger" class="mb-2 mr-sm-2 mb-sm-0">Clear</b-button>
    </div>
  </b-form>
  <b-button :to="{ name: 'MunsellAdd' }" variant="secondary" class="m-3">Add New Munsell Color Code</b-button>
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
    <template #cell(munsellCode)="data">
      <b-link :to="{ name: 'MunsellEdit', params: { id: data.item.munsellCode }}">{{ data.item.munsellCode }}</b-link>
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
    this.munsellId = genId();
    this.munsellCodeId = genId();
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
    ...mapMutations('munsell', ['setMunsell', 'clearParams', 'setMunsellCode', 'firstPage', 'setPage', 'setSortBy', 'setSortDesc', 'clearAll']),
    ...mapActions('munsell', ['search', 'reset', 'changePage']),
    sortChanged({ sortBy, sortDesc }) {
      this.setSortBy(sortBy);
      this.setSortDesc(sortDesc);
      this.search();
    },
  },

  computed: {
    ...mapState('munsell', ['searching', 'page', 'totalItems', 'totalPages', 'items', 'params', 'sortDesc', 'sortBy']),
    munsell: {
      get() {
        return this.params.munsell;
      },
      set(value) {
        this.setMunsell(value);
      },
    },
    munsellCode: {
      get() {
        return this.params.munsellCode;
      },
      set(value) {
        this.setMunsellCode(value);
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
      munsellId: null,
      munsellCodeId: null,

      fields: [
        {
          key: 'munsellCode',
          label: 'Munsell Color Code',
          sortable: true,
        },
        {
          key: 'munsell',
          label: 'Munsell Color',
          sortable: true,
        },
      ],
    };
  },
};
</script>
