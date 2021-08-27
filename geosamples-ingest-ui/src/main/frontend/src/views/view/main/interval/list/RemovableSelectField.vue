<template>
  <b-input-group >
    <b-form-select
      :options="options"
      @blur="onBlur"
      :value="value"
      @change="onUpdate"
      :state="state"
    />
    <b-input-group-append>
      <b-button class="input-group-append" variant="outline-primary" @click="onRemove">
        <b-icon icon="x-circle" class="mr-2" />
      </b-button>
    </b-input-group-append>
  </b-input-group>
</template>

<script>
export default {
  props: ['onBlur', 'value', 'onUpdate', 'state', 'error', 'onRemove', 'options'],
};
</script>
