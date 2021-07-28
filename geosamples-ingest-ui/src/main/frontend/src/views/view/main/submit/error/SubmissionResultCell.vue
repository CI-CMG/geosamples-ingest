<template>
  <div :class="cellClass">
    <div class="geo-submission-result-value">{{value}}</div>
    <div class="geo-submission-result-error-message" v-if="error">{{error}}</div>
  </div>
</template>

<script>
export default {
  props: ['value', 'error'],
  computed: {
    cellClass() {
      if (this.error) {
        return 'geo-submission-result-cell geo-submission-result-error';
      }
      return 'geo-submission-result-cell';
    },
  },
};
</script>
