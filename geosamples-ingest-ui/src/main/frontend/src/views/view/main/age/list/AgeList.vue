<template>
<div>
  <b-form @submit.prevent="search" @reset.prevent="reset">
    <b-container fluid>
      <b-row>
        <b-col>
          <b-form-group label="Geologic Age" :label-for="ageId">
            <b-form-input :id="ageId" v-model="age"/>
          </b-form-group>
        </b-col>
        <b-col>
          <b-form-group label="Geologic Age Code" :label-for="ageCodeId">
            <b-form-input :id="ageCodeId" v-model="ageCode"/>
          </b-form-group>
        </b-col>
      </b-row>
    </b-container>

    <div v-if="!searching">
      <b-button type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">Search</b-button>
      <b-button type="reset" variant="danger" class="mb-2 mr-sm-2 mb-sm-0">Clear</b-button>
    </div>
  </b-form>
  <b-button :to="{ name: 'AgeAdd' }" variant="secondary" class="m-3">Add New Geologic Age</b-button>
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
    <template #cell(age)="data">
      <b-link :to="{ name: 'AgeEdit', params: { id: data.item.age }}">{{ data.item.age }}</b-link>
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
    this.ageId = genId();
    this.ageCodeId = genId();
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
    ...mapMutations('age', ['setAge', 'clearParams', 'setAgeCode', 'firstPage', 'setPage', 'setSortBy', 'setSortDesc', 'clearAll']),
    ...mapActions('age', ['search', 'reset', 'changePage']),
    sortChanged({ sortBy, sortDesc }) {
      this.setSortBy(sortBy);
      this.setSortDesc(sortDesc);
      this.search();
    },
  },

  computed: {
    ...mapState('age', ['searching', 'page', 'totalItems', 'totalPages', 'items', 'params', 'sortDesc', 'sortBy']),
    age: {
      get() {
        return this.params.age;
      },
      set(value) {
        this.setAge(value);
      },
    },
    ageCode: {
      get() {
        return this.params.ageCode;
      },
      set(value) {
        this.setAgeCode(value);
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
      ageId: null,
      ageCodeId: null,

      fields: [
        {
          key: 'age',
          label: 'Geologic Age',
          sortable: true,
        },
        {
          key: 'ageCode',
          label: 'Geologic Age Code',
          sortable: true,
        },
      ],
    };
  },
};
</script>
