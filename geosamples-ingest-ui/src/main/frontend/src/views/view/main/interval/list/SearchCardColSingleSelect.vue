<template>
  <b-col lg="4">
    <b-card :title="title">
      <b-spinner v-if="!options.length"/>
      <div v-else>
        <b-input-group >
          <b-form-select
            :onBlur="() => setTouched({path: field, touched: true})"
            :value="getValue(field)"
            @change="(value) => setValue({ path: field, value })"
            :state="showError(field)"
            :error="getError(field)"
            :options="options"
          />
        </b-input-group>
      </div>
    </b-card>
  </b-col>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex';

export default {
  props: ['title', 'field', 'options'],
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
