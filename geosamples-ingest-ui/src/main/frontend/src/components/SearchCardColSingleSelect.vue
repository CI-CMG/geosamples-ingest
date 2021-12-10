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

export default {
  props: [
    'title',
    'field',
    'options',
    'module',
  ],
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
    addToArray(value) {
      this.$store.commit(`${this.module}/addToArray`, value);
    },
    deleteFromArray(value) {
      this.$store.commit(`${this.module}/deleteFromArray`, value);
    },
    setError(value) {
      this.$store.commit(`${this.module}/setError`, value);
    },
    setTouched(value) {
      this.$store.commit(`${this.module}/setTouched`, value);
    },
    setValue(value) {
      this.$store.commit(`${this.module}/setValue`, value);
    },
    initialize(value) {
      this.$store.commit(`${this.module}/initialize`, value);
    },
  },
};
</script>
