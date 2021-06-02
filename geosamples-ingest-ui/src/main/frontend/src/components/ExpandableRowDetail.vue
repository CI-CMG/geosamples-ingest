<template>
  <b-container fluid>
    <ExpandableRowDetailRow v-for="field in fields" :key="field" :title="field" :value="row[field]"/>
  </b-container>
</template>

<script>
import ExpandableRowDetailRow from '@/components/ExpandableRowDetailRow.vue';

export default {
  props: ['row', 'fields'],
  components: {
    ExpandableRowDetailRow,
  },
};
</script>
