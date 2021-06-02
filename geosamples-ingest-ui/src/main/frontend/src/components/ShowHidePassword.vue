<template>
  <b-col sm="6">
    <label :for="id">Token:</label>

    <b-input-group >
      <b-form-input
        :id="id"
        :type="visible ? 'text' : 'password'"
        @blur="onBlur"
        :value="value"
        @update="onUpdate"
        :state="state"
      />
      <b-input-group-append>
        <b-button class="input-group-append" variant="outline-primary" @click="generateToken">
          <b-icon icon="arrow-clockwise" class="mr-2"></b-icon> Generate
        </b-button>
        <b-button class="input-group-append" variant="outline-primary" @click="toggle">
          <b-icon :icon="visible ? 'eye-slash' : 'eye'" class="mr-2"></b-icon> {{visible ? 'Hide' : 'Show'}}
        </b-button>
      </b-input-group-append>
    </b-input-group>

    <b-form-invalid-feedback>{{error}}</b-form-invalid-feedback>
  </b-col>
</template>

<script>
export default {
  props: ['id', 'onBlur', 'value', 'onUpdate', 'state', 'error', 'generateToken'],
  data() {
    return {
      visible: false,
    };
  },
  methods: {
    toggle() {
      this.visible = !this.visible;
    },
  },
};
</script>
