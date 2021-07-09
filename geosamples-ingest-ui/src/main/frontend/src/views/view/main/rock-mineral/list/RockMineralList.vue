<template>
<div>
  <b-form @submit.prevent="search" @reset.prevent="reset">
    <b-container fluid>
      <b-row>
        <b-col>
          <b-form-group label="RockMineral" :label-for="deviceId">
            <b-form-input :id="deviceId" v-model="device"/>
          </b-form-group>
        </b-col>
        <b-col>
          <b-form-group label="RockMineral Code" :label-for="deviceCodeId">
            <b-form-input :id="deviceCodeId" v-model="deviceCode"/>
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
      <b-link :to="{ name: 'RockMineralEdit', params: { id: data.item.device }}">{{ data.item.device }}</b-link>
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
    this.deviceId = genId();
    this.deviceCodeId = genId();
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
    ...mapMutations('device', ['setRockMineral', 'clearParams', 'setRockMineralCode', 'firstPage', 'setPage', 'setSortBy', 'setSortDesc', 'clearAll']),
    ...mapActions('device', ['search', 'reset', 'changePage']),
    sortChanged({ sortBy, sortDesc }) {
      this.setSortBy(sortBy);
      this.setSortDesc(sortDesc);
      this.search();
    },
  },

  computed: {
    ...mapState('device', ['searching', 'page', 'totalItems', 'totalPages', 'items', 'params', 'sortDesc', 'sortBy']),
    device: {
      get() {
        return this.params.device;
      },
      set(value) {
        this.setRockMineral(value);
      },
    },
    deviceCode: {
      get() {
        return this.params.deviceCode;
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
      deviceId: null,
      deviceCodeId: null,

      fields: [
        {
          key: 'device',
          label: 'RockMineral',
          sortable: true,
        },
        {
          key: 'deviceCode',
          label: 'RockMineral Code',
          sortable: true,
        },
      ],
    };
  },
};
</script>
