<template>
  <b-table
    head-variant="dark"
    striped
    bordered
    small
    hover
    :items="items"
    :fields="fields"
    no-sort-reset
    no-local-sorting
    :sort-by="sortBy"
    :sort-desc="sortDesc">
    <template #head()="data">
      <span @click="() => sortChangedInt(data)" :style="headerStyle(data)">
        <span>{{ data.label }}</span>
        <b-icon-arrow-up v-if="sortBy === data.column && !sortDesc"></b-icon-arrow-up>
        <b-icon-arrow-down v-if="sortBy === data.column && sortDesc"></b-icon-arrow-down>
        <b-icon-arrow-down-up variant="secondary" v-if="sortable[data.column] && sortBy !== data.column"></b-icon-arrow-down-up>
      </span>

    </template>
    <!--          <template #cell(imlgs)="data">-->
    <!--            <b-link :to="{ name: 'SampleEdit', params: { id: data.item.imlgs }}">{{ data.item.imlgs }}</b-link>-->
    <!--          </template>-->
    <!--          <template #cell(interval)="data">-->
    <!--            <b-link :to="{ name: 'IntervalEdit', params: { id: data.item.interval }}">{{ data.item.interval }}</b-link>-->
    <!--          </template>-->
  </b-table>
</template>

<script>
export default {
  props: [
    'sortChanged',
    'sortBy',
    'sortDesc',
    'items',
    'fields',
    'sortableColumns',
  ],
  computed: {
    sortable() {
      const sortable = {};
      this.sortableColumns.forEach((s) => {
        sortable[s] = true;
      });
      return sortable;
    },
  },
  methods: {
    sortChangedInt({ column }) {
      let sortDesc = false;
      if (column === this.sortBy) {
        sortDesc = !this.sortDesc;
      }
      this.sortChanged({ sortBy: column, sortDesc });
    },
    headerStyle({ column }) {
      const style = {
        width: '100%',
      };
      if (this.sortable[column]) {
        style.cursor = 'pointer';
      }
      return style;
    },

  },
};
</script>
