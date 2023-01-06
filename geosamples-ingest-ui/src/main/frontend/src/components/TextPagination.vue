<template>
  <div>
    <b-row class="mb-3">
      <b-col sm="1">
        <label :for="pageInputId" class="mt-1">
          Current page:
        </label>
      </b-col>
      <b-col sm="1">
        <b-input type="text" v-model="page" :id="pageInputId"/>
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
  props: ['updated', 'page', 'totalItems', 'itemsPerPage'],
  data() {
    return {
      pageInputId: null,
    };
  },

  beforeMount() {
    this.pageInputId = genId();
  },
};
</script>
