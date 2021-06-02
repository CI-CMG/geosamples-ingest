<template>
  <b-container fluid class="csb-expandable-row-list p-0 border rounded">
    <b-row
      v-for="row in rows"
      :key="row.id"
      class="csb-expandable-row-list-row">
      <b-col class="csb-expandable-row-list-row-col">
        <slot v-bind="row">{{ row.id }}</slot>
      </b-col>
    </b-row>
  </b-container>
</template>

<script>
export default {
  props: [
    'rows',
  ],
};
</script>
