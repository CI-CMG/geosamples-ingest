<template>
  <div/>
</template>

<script>
import { exchangeCodeForToken } from '@/sessionMonster';

export default {
  mounted() {
    this.exchangeCodeAndRoute();
  },
  updated() {
    this.exchangeCodeAndRoute();
  },
  methods: {
    exchangeCodeAndRoute() {
      const { code, state } = this.$route.query;
      if (code && state) {
        exchangeCodeForToken({ code, state, router: this.$router });
      } else {
        this.$router.push({ name: 'view' });
      }
    },
  },
};
</script>
