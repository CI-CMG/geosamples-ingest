<template>
<div>
  <b-form @submit.prevent="search" @reset.prevent="reset">
    <b-container fluid>
      <b-row>
        <b-col>
          <b-form-group label="Platform" :label-for="platformId">
            <b-form-input :id="platformId" v-model="platform"/>
          </b-form-group>
        </b-col>
        <b-col>
          <b-form-group label="Master ID" :label-for="masterIdId">
            <b-form-input :id="masterIdId" v-model="masterId"/>
          </b-form-group>
        </b-col>
        <b-col>
          <b-form-group label="ICES Code" :label-for="icesCodeId">
            <b-form-input :id="icesCodeId" v-model="icesCode"/>
          </b-form-group>
        </b-col>
      </b-row>
    </b-container>

    <div v-if="!searching">
      <b-button type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">Search</b-button>
      <b-button type="reset" variant="danger" class="mb-2 mr-sm-2 mb-sm-0">Clear</b-button>
    </div>
  </b-form>
  <b-button :to="{ name: 'PlatformAdd' }" variant="secondary" class="m-3">Add New Platform</b-button>
  <b-table
    striped
    bordered
    small
    hover
    :items="platforms"
    :fields="fields"
    no-local-sorting
    @sort-changed="sortChanged"
    :sort-by="sortBy"
    :sort-desc="sortDesc"
  />
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
    this.platformId = genId();
    this.masterIdId = genId();
    this.icesCodeId = genId();
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
    ...mapMutations('platform', ['setPlatform', 'clearParams', 'setIcesCode', 'setMasterId', 'firstPage', 'setPage', 'setSortBy', 'setSortDesc', 'clearAll']),
    ...mapActions('platform', ['search', 'reset', 'changePage']),
    sortChanged({ sortBy, sortDesc }) {
      this.setSortBy(sortBy);
      this.setSortDesc(sortDesc);
      this.search();
    },
  },

  computed: {
    ...mapState('platform', ['searching', 'page', 'totalItems', 'totalPages', 'platforms', 'params', 'sortDesc', 'sortBy']),
    platform: {
      get() {
        return this.params.platform;
      },
      set(value) {
        this.setPlatform(value);
      },
    },
    icesCode: {
      get() {
        return this.params.icesCode;
      },
      set(value) {
        this.setIcesCode(value);
      },
    },
    masterId: {
      get() {
        return this.params.masterId;
      },
      set(value) {
        this.setMasterId(value);
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
      platformId: null,
      masterIdId: null,
      icesCodeId: null,

      fields: [
        {
          key: 'masterId',
          label: 'Master ID',
          sortable: true,
        },
        {
          key: 'prefix',
          label: 'Prefix',
        },
        {
          key: 'platform',
          label: 'Platform',
          sortable: true,
        },
        {
          key: 'icesCode',
          label: 'ICES Code',
          sortable: true,
        },
        {
          key: 'sourceUri',
          label: 'Source URI',
        },
      ],
    };
  },
};
</script>
