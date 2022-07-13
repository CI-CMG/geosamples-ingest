<template>
<div>
  <b-form @submit.prevent="search" @reset.prevent="reset">
    <b-container fluid>
      <b-row>
        <b-col>
          <b-form-group label="Username Contains" :label-for="userNameContainsId">
            <b-form-input :id="userNameContainsId" v-model="userNameContains"/>
          </b-form-group>
        </b-col>
        <b-col>
          <b-form-group label="Username Equals" :label-for="userNameEqualsId">
            <b-form-input :id="userNameEqualsId" v-model="userNameEquals"/>
          </b-form-group>
        </b-col>
        <b-col>
          <b-form-group label="Display Name Contains" :label-for="displayNameContainsId">
            <b-form-input :id="displayNameContainsId" v-model="displayNameContains"/>
          </b-form-group>
        </b-col>
      </b-row>
    </b-container>

    <div v-if="!searching">
      <b-button type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">Search</b-button>
      <b-button type="reset" variant="danger" class="mb-2 mr-sm-2 mb-sm-0">Clear</b-button>
    </div>
  </b-form>
  <b-button :to="{ name: 'UserAdd' }" variant="secondary" class="m-3">Add New User</b-button>
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
      <b-link :to="{ name: 'UserEdit', params: { id: data.item.userName }}">{{ data.item.userName }}</b-link>
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
    this.userNameContainsId = genId();
    this.userNameEqualsId = genId();
    this.displayNameContainsId = genId();
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
    ...mapMutations('user', ['setUserNameEquals', 'setUserNameContains', 'setDisplayNameContains', 'clearParams', 'firstPage', 'setPage', 'setSortBy', 'setSortDesc', 'clearAll']),
    ...mapActions('user', ['search', 'reset', 'changePage']),
    sortChanged({ sortBy, sortDesc }) {
      this.setSortBy(sortBy);
      this.setSortDesc(sortDesc);
      this.search();
    },
  },

  computed: {
    ...mapState('user', ['searching', 'page', 'totalItems', 'totalPages', 'items', 'params', 'sortDesc', 'sortBy']),
    userNameEquals: {
      get() {
        return this.params.userNameEquals;
      },
      set(value) {
        this.setUserNameEquals(value);
      },
    },
    userNameContains: {
      get() {
        return this.params.userNameContains;
      },
      set(value) {
        this.setUserNameContains(value);
      },
    },
    displayNameContains: {
      get() {
        return this.params.displayNameContains;
      },
      set(value) {
        this.setDisplayNameContains(value);
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
      userNameContainsId: null,
      userNameEqualsId: null,
      displayNameContainsId: null,

      fields: [
        {
          key: 'userName',
          label: 'Username',
          sortable: true,
        },
        {
          key: 'displayName',
          label: 'Display Name',
          sortable: true,
        },
      ],
    };
  },
};
</script>
