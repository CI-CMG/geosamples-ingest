<template>
  <span v-if="hasAuthority">
    <slot/>
  </span>
  <span v-else>
    {{ fallbackText }}
  </span>
</template>

<script>
import { mapState } from 'vuex';

export default {
  props: ['authorities', 'fallbackText'],

  computed: {
    ...mapState('userAuth', ['user']),

    hasAuthority() {
      const authorities = this.user.authorities;
      if (authorities === undefined) {
        return false;
      }
      return authorities.filter((a) => this.authorities.includes(a)).length > 0;
    },
  },
};
</script>
