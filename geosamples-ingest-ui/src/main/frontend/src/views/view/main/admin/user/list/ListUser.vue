<template>
  <div class="m-2">
    <b-breadcrumb :items="bc"/>

    <div v-if="loading">
      <b-spinner/>
    </div>

    <div v-else>
      <b-form inline class="m-2" @submit="onSubmit">
        <b-button type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0">Refresh</b-button>
        <b-button :to="{ name: 'AddUser' }" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">Add New User</b-button>
      </b-form>

      <br /><br />

      <b-table striped hover :items="users" :fields="fields">
        <template #cell(userName)="data">
          <a v-bind:href="'edit/' + data.item.userName">{{ data.item.userName }}</a>
        </template>
      </b-table>

      <b-pagination :value="page" @input="loadSortedPage" :total-rows="totalItems" :per-page="itemsPerPage"></b-pagination>

    </div>

  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex';

export default {
  components: {

  },

  mounted() {
    this.loadSortedPage(1);
  },

  computed: {
    ...mapGetters('userAdmin', ['users', 'page', 'totalPages', 'totalItems', 'itemsPerPage', 'loading']),
  },

  methods: {
    ...mapActions('userAdmin', ['loadPage']),
    loadSortedPage(page) {
      this.loadPage({
        page,
        itemsPerPage: this.itemsPerPage,
        order: ['username:asc'],
      });
    },
    onSubmit(evt) {
      // v-bind:href="'edit/
      console.log(evt);
    },
  },
  data() {
    return {
      fields: [
        // 'index', // virtual column
        'userName',
        'displayName',
        'enabled',
        // { key: 'edit', label: 'Edit' }, // virtual column
      ],

      bc: [
        {
          text: 'Crowbar',
          to: { name: 'Home' },
        },
        {
          text: 'User',
          active: true,
        },
      ],
    };
  },
};
</script>
