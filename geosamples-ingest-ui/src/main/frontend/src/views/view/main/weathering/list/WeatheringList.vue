<template>
<div>
  <b-form @submit.prevent="search" @reset.prevent="reset">
    <b-container fluid>
      <b-row>
        <b-col>
          <b-form-group label="Weathering" :label-for="weatheringId">
            <b-form-input :id="weatheringId" v-model="weathering"/>
          </b-form-group>
        </b-col>
        <b-col>
          <b-form-group label="Weathering Code" :label-for="weatheringCodeId">
            <b-form-input :id="weatheringCodeId" v-model="weatheringCode"/>
          </b-form-group>
        </b-col>
      </b-row>
    </b-container>

    <div v-if="!searching">
      <b-button type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">Search</b-button>
      <b-button type="reset" variant="danger" class="mb-2 mr-sm-2 mb-sm-0">Clear</b-button>
    </div>
  </b-form>
  <b-button :to="{ name: 'WeatheringAdd' }" variant="secondary" class="m-3">Add New Weathering</b-button>
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
    <template #cell(weathering)="data">
      <b-link :to="{ name: 'WeatheringEdit', params: { id: data.item.weathering }}">{{ data.item.weathering }}</b-link>
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
    this.weatheringId = genId();
    this.weatheringCodeId = genId();
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
    ...mapMutations('weathering', ['setWeathering', 'clearParams', 'setWeatheringCode', 'firstPage', 'setPage', 'setSortBy', 'setSortDesc', 'clearAll']),
    ...mapActions('weathering', ['search', 'reset', 'changePage']),
    sortChanged({ sortBy, sortDesc }) {
      this.setSortBy(sortBy);
      this.setSortDesc(sortDesc);
      this.search();
    },
  },

  computed: {
    ...mapState('weathering', ['searching', 'page', 'totalItems', 'totalPages', 'items', 'params', 'sortDesc', 'sortBy']),
    weathering: {
      get() {
        return this.params.weathering;
      },
      set(value) {
        this.setWeathering(value);
      },
    },
    weatheringCode: {
      get() {
        return this.params.weatheringCode;
      },
      set(value) {
        this.setWeatheringCode(value);
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
      weatheringId: null,
      weatheringCodeId: null,

      fields: [
        {
          key: 'weathering',
          label: 'Weathering',
          sortable: true,
        },
        {
          key: 'weatheringCode',
          label: 'Weathering Code',
          sortable: true,
        },
      ],
    };
  },
};
</script>
