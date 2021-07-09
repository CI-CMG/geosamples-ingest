<template>
<div>
  <b-form @submit.prevent="search" @reset.prevent="reset">
    <b-container fluid>
      <b-row>
        <b-col>
          <b-form-group label="Remark" :label-for="remarkId">
            <b-form-input :id="remarkId" v-model="remark"/>
          </b-form-group>
        </b-col>
        <b-col>
          <b-form-group label="Remark Code" :label-for="remarkCodeId">
            <b-form-input :id="remarkCodeId" v-model="remarkCode"/>
          </b-form-group>
        </b-col>
      </b-row>
    </b-container>

    <div v-if="!searching">
      <b-button type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">Search</b-button>
      <b-button type="reset" variant="danger" class="mb-2 mr-sm-2 mb-sm-0">Clear</b-button>
    </div>
  </b-form>
  <b-button :to="{ name: 'RemarkAdd' }" variant="secondary" class="m-3">Add New Remark</b-button>
  <b-table
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
    <template #cell(remark)="data">
      <b-link :to="{ name: 'RemarkEdit', params: { id: data.item.remark }}">{{ data.item.remark }}</b-link>
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
    this.remarkId = genId();
    this.remarkCodeId = genId();
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
    ...mapMutations('remark', ['setRemark', 'clearParams', 'setRemarkCode', 'firstPage', 'setPage', 'setSortBy', 'setSortDesc', 'clearAll']),
    ...mapActions('remark', ['search', 'reset', 'changePage']),
    sortChanged({ sortBy, sortDesc }) {
      this.setSortBy(sortBy);
      this.setSortDesc(sortDesc);
      this.search();
    },
  },

  computed: {
    ...mapState('remark', ['searching', 'page', 'totalItems', 'totalPages', 'items', 'params', 'sortDesc', 'sortBy']),
    remark: {
      get() {
        return this.params.remark;
      },
      set(value) {
        this.setRemark(value);
      },
    },
    remarkCode: {
      get() {
        return this.params.remarkCode;
      },
      set(value) {
        this.setRemarkCode(value);
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
      remarkId: null,
      remarkCodeId: null,

      fields: [
        {
          key: 'remark',
          label: 'Remark',
          sortable: true,
        },
        {
          key: 'remarkCode',
          label: 'Remark Code',
          sortable: true,
        },
      ],
    };
  },
};
</script>
