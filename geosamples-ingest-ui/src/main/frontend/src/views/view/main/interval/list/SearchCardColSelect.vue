<template>
  <b-col lg="4">
    <b-card :title="title">
      <b-spinner v-if="!options.length"/>
      <div v-else>
        <div class="geo-search-card-list">
          <RemovableSelectField
            v-for="(value, index) in getValue(field)" :key="`v${index}`"
            :onBlur="() => setTouched({path: `${field}[${index}]`, touched: true})"
            :value="getValue(`${field}[${index}]`)"
            :onUpdate="(value) => setValue({ path: `${field}[${index}]`, value })"
            :state="showError(`${field}[${index}]`)"
            :error="getError(`${field}[${index}]`)"
            :onRemove="() => deleteFromArray(`${field}[${index}]`)"
            :options="options"
          />
        </div>
        <b-button variant="outline-primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3" @click="addToArray({ path: field })"><b-icon icon="plus-circle" class="mr-2"/></b-button>
      </div>
    </b-card>
  </b-col>
</template>

<script>
import RemovableSelectField from '@/views/view/main/interval/list/RemovableSelectField.vue';
import { mapGetters, mapMutations } from 'vuex';

export default {
  props: ['title', 'field', 'options'],
  components: {
    RemovableSelectField,
  },
  computed: {
    ...mapGetters('intervalSearchForm',
      [
        'getValue',
        'formDirty',
        'getError',
        'isTouched',
        'formHasUntouchedErrors',
      ]),
    showError() {
      return (path) => ((!this.isTouched(path) && this.getError(path)) ? false : null);
    },
  },
  methods: {
    ...mapMutations('intervalSearchForm',
      [
        'initialize',
        'setValue',
        'setTouched',
        'setError',
        'deleteFromArray',
        'addToArray',
      ]),

  },
};
</script>
