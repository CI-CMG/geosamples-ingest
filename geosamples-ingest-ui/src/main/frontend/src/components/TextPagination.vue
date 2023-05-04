<template>
  <div>
    <b-row class="mb-3">
      <b-col>
        <span>{{ `Total records: ${ totalItems }` }}</span>
      </b-col>
    </b-row>
    <b-row class="mb-3">
      <b-col sm="1">
        <label :for="pageInputId" class="mt-1">
          Current page:
        </label>
      </b-col>
      <b-col sm="1">
        <b-input type="number" v-model="currentPage" @keydown.enter.native="updatePageFromText" :id="pageInputId" min="1" :max="totalPages"/>
      </b-col>
    </b-row>
    <b-row>
      <b-col>
        <b-pagination :value="page" @input="updatePageFromPagination" :total-rows="totalItems" :per-page="itemsPerPage"></b-pagination>
      </b-col>
    </b-row>
  </div>
</template>

<script>
import genId from '@/components/idGenerator';

export default {
  props: ['updated', 'page', 'totalItems', 'itemsPerPage', 'totalPages'],
  data() {
    return {
      pageInputId: null,
      currentPage: 1,
    };
  },

  beforeMount() {
    this.pageInputId = genId();
  },

  methods: {
    updatePageFromText() {
      const value = this.currentPage;
      if (value !== '' && Number(value) <= Number(this.totalPages) && Number(this.page) !== Number(value)) {
        this.updated(value);
      }
    },

    updatePageFromPagination(value) {
      this.currentPage = value;
      if (value !== '' && Number(value) <= Number(this.totalPages) && Number(this.page) !== Number(value)) {
        this.updated(value);
      }
    },
  },

  created() {
    this.currentPage = this.page;
  },
};
</script>
