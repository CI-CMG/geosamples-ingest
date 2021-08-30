<template>
  <b-list-group-item>
    <b-container fluid>
      <b-row>
        <b-col cols="6">
          <span>{{ text }}</span>
        </b-col>
        <b-col cols="2">
          <b-form-checkbox :checked="asc" @change="onToggle" switch>Asc.</b-form-checkbox>
        </b-col>
        <b-col cols="4">
          <b-button variant="outline-primary" :disabled="notDisplayUp" class="m-1" :class="{ 'geo-column-button-hidden': notDisplayUp }" @click="onUp"><b-icon icon="chevron-up"/></b-button>
          <b-button variant="outline-primary" :disabled="notDisplayDown" class="m-1" :class="{ 'geo-column-button-hidden': notDisplayDown }" @click="onDown"><b-icon icon="chevron-down"/></b-button>
          <b-button variant="outline-primary" class="m-1" @click="onRemove"><b-icon icon="x-circle"/></b-button>
        </b-col>
      </b-row>
    </b-container>

  </b-list-group-item>
</template>

<script>
export default {
  props: ['onRemove', 'text', 'onUp', 'onDown', 'asc', 'onToggle', 'index', 'length'],
  computed: {
    displayUp() {
      return this.index > 0 && this.length > 1;
    },
    displayDown() {
      return this.index < (this.length - 1);
    },
    notDisplayUp() {
      return !this.displayUp;
    },
    notDisplayDown() {
      return !this.displayDown;
    },
  },
};
</script>
