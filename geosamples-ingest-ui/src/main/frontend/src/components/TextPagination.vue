<template>
  <div>
    <b-row class="mb-3">
      <b-col sm="1">
        <label :for="pageInputId" class="mt-1">
          Current page:
        </label>
      </b-col>
      <b-col sm="1">
        <b-input type="number" :value="page" @input="updatePage" :id="pageInputId" @onemptied="updatePage(1)" min="1" :max="totalPages"/>
      </b-col>
    </b-row>
    <b-row>
      <b-col>
        <b-pagination v-model="page" @input="updated" :total-rows="totalItems" :per-page="itemsPerPage"></b-pagination>
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
      if (value !== '') {
        this.updated(value);
      }
    },
  },
};
</script>
