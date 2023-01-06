<template>
<div>
  <b-form @submit.prevent="search" @reset.prevent="reset">
    <b-container fluid>
      <b-row>
        <b-col>
          <b-form-group label="Physiographic Province" :label-for="provinceId">
            <b-form-input :id="provinceId" v-model="province"/>
          </b-form-group>
        </b-col>
        <b-col>
          <b-form-group label="Physiographic Province Code" :label-for="provinceCodeId">
            <b-form-input :id="provinceCodeId" v-model="provinceCode"/>
          </b-form-group>
        </b-col>
      </b-row>
    </b-container>

    <div v-if="!searching">
      <b-button type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">Search</b-button>
      <b-button type="reset" variant="danger" class="mb-2 mr-sm-2 mb-sm-0">Clear</b-button>
    </div>
  </b-form>
  <b-button :to="{ name: 'ProvinceAdd' }" variant="secondary" class="m-3">Add New Physiographic Province</b-button>
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
    <template #cell(province)="data">
      <b-link :to="{ name: 'ProvinceEdit', params: { id: data.item.province }}">{{ data.item.province }}</b-link>
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
    this.provinceId = genId();
    this.provinceCodeId = genId();
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
    ...mapMutations('province', ['setProvince', 'clearParams', 'setProvinceCode', 'firstPage', 'setPage', 'setSortBy', 'setSortDesc', 'clearAll']),
    ...mapActions('province', ['search', 'reset', 'changePage']),
    sortChanged({ sortBy, sortDesc }) {
      this.setSortBy(sortBy);
      this.setSortDesc(sortDesc);
      this.search();
    },
  },

  computed: {
    ...mapState('province', ['searching', 'page', 'totalItems', 'totalPages', 'items', 'params', 'sortDesc', 'sortBy']),
    province: {
      get() {
        return this.params.province;
      },
      set(value) {
        this.setProvince(value);
      },
    },
    provinceCode: {
      get() {
        return this.params.provinceCode;
      },
      set(value) {
        this.setProvinceCode(value);
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
      provinceId: null,
      provinceCodeId: null,

      fields: [
        {
          key: 'province',
          label: 'Physiographic Province',
          sortable: true,
        },
        {
          key: 'provinceCode',
          label: 'Physiographic Province Code',
          sortable: true,
        },
      ],
    };
  },
};
</script>
