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
import RemovableSelectField from './RemovableSelectField.vue';

export default {
  props: ['title', 'field', 'options', 'module'],
  components: {
    RemovableSelectField,
  },
  computed: {
    getValue() {
      return this.$store.getters[`${this.module}/getValue`];
    },
    formDirty() {
      return this.$store.getters[`${this.module}/formDirty`];
    },
    getError() {
      return this.$store.getters[`${this.module}/getError`];
    },
    isTouched() {
      return this.$store.getters[`${this.module}/isTouched`];
    },
    formHasUntouchedErrors() {
      return this.$store.getters[`${this.module}/formHasUntouchedErrors`];
    },
    showError() {
      return (path) => ((!this.isTouched(path) && this.getError(path)) ? false : null);
    },
  },
  methods: {
    initialize(value) {
      this.$store.commit(`${this.module}/initialize`, value);
    },
    setValue(value) {
      this.$store.commit(`${this.module}/setValue`, value);
    },
    setTouched(value) {
      this.$store.commit(`${this.module}/setTouched`, value);
    },
    setError(value) {
      this.$store.commit(`${this.module}/setError`, value);
    },
    deleteFromArray(value) {
      this.$store.commit(`${this.module}/deleteFromArray`, value);
    },
    addToArray(value) {
      this.$store.commit(`${this.module}/addToArray`, value);
    },
  },
};
</script>
