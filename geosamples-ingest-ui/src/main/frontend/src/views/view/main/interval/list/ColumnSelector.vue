<template>
  <b-list-group-item>
    <b-container fluid>
      <b-row>
        <b-col cols="10">
          <span>{{ text }}</span>
        </b-col>
        <b-col cols="2">
          <b-button variant="outline-primary" class="m-1" @click="onAdd"><b-icon icon="plus-circle"/></b-button>
        </b-col>
      </b-row>
    </b-container>

  </b-list-group-item>
</template>

<script>
export default {
  props: ['onAdd', 'text'],
};
</script>
