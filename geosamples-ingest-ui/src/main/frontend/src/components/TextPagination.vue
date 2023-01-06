<template>
  <div>
    <b-row class="mb-3">
      <b-col sm="1">
        <label :for="pageInputId" class="mt-1">
          Current page:
        </label>
      </b-col>
      <b-col sm="1">
        <b-input type="number" :value="page" @input="updatePage" :id="pageInputId" min="1" :max="totalPages"/>
      </b-col>
    </b-row>
    <b-row>
      <b-col>
        <b-pagination :value="page" @input="updatePage" :total-rows="totalItems" :per-page="itemsPerPage"></b-pagination>
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
    };
  },

  beforeMount() {
    this.pageInputId = genId();
  },

  methods: {
    updatePage(value) {
      if (value !== '' && Number(value) <= Number(this.totalPages) && Number(this.page) !== Number(value)) {
        this.updated(value);
      }
    },
  },
};
</script>
