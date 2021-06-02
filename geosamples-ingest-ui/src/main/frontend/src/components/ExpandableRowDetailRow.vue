<template>
  <b-row no-gutters>
    <b-col sm="auto">{{ title }}:</b-col>
    <b-col class="pl-3">{{ value }}</b-col>
  </b-row>
</template>

<script>
export default {
  props: ['title', 'value'],
};
</script>
