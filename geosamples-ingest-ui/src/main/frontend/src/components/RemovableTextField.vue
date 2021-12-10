<template>
  <b-input-group >
    <b-form-input
      type="text"
      @blur="onBlur"
      :value="value"
      @update="onUpdate"
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
  props: ['onBlur', 'value', 'onUpdate', 'state', 'error', 'onRemove'],
};
</script>
