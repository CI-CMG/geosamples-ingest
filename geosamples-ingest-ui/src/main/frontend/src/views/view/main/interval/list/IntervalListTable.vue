<template>
  <b-table
    sticky-header="500px"
    head-variant="dark"
    striped
    bordered
    small
    hover
    :items="items"
    :fields="fields">
    <template #head()="data">
      <span :style="{
        width: '100%',
      }">
        <span>{{ data.label }}</span>
        <b-icon-caret-up-fill class="ml-2" v-if="sorted(data.column).sorted === 'asc'"></b-icon-caret-up-fill>
        <b-icon-caret-down-fill class="ml-2" v-if="sorted(data.column).sorted === 'desc'"></b-icon-caret-down-fill>
        <span v-if="sorted(data.column).index >= 0">{{ sorted(data.column).index }}</span>
      </span>

    </template>
    <template #cell(selected)="data">
      <b-form-checkbox plain :checked="data.item.selected" @change="() => toggleSelect(data.index)"/>
    </template>
    <template #cell(publish)="data">
      <b-form-checkbox :disabled="true" plain :checked="data.item.publish"/>
    </template>
    <template #cell(imlgs)="data">
      <b-link :to="{ name: 'SampleEdit', params: { id: data.item.imlgs }}">{{ data.item.imlgs }}</b-link>
    </template>
    <template #cell(interval)="data">
      <b-link :to="{ name: 'IntervalEdit', params: { imlgs: data.item.imlgs, id: data.item.intervalId }}">{{ data.item.interval }}</b-link>
    </template>
    <template #cell(ages)="data">
      <span v-if="data.item.ages">{{ data.item.ages.join(', ') }}</span>
    </template>
  </b-table>
</template>

<script>
export default {
  props: [
    'sortChanged',
    'sortedColumns',
    'sortDesc',
    'items',
    'fields',
    'toggleSelect',
  ],
  computed: {
    indexes() {
      const indexMap = {};
      this.sortedColumns.forEach(({ key }, index) => {
        indexMap[key] = index;
      });
      return indexMap;
    },
    sorted() {
      return (key) => {
        if (!this.sortedColumns) {
          return { sorted: '', index: -1 };
        }
        const index = this.indexes[key];
        if (index != null) {
          const selected = this.sortedColumns[index];
          if (selected.asc) {
            return { sorted: 'asc', index };
          }
          return { sorted: 'desc', index };
        }
        return { sorted: '', index: -1 };
      };
    },
  },
  methods: {

  },
};
</script>
