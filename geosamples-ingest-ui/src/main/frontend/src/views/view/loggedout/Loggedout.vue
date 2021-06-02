<template>
  <h2>You have been logged out.</h2>
</template>

<script>
import { mapGetters } from 'vuex';

export default {
  mounted() {
    this.checkUser();
  },
  updated() {
    this.checkUser();
  },
  computed: {
    ...mapGetters('user', ['user']),
  },
  methods: {
    checkUser() {
      if (this.user.userName) {
        this.$router.push({ name: 'view' });
      }
    },
  },
};
</script>
