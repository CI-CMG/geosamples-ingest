<template>
  <h2>
    Welcome to the Geosamples API
    <strong style="color: grey; font-size: small">{{ version }}</strong>
  </h2>
</template>

<script>
import { VERSION } from '@/resourceBasePath';

export default {
  data() {
    return {
      version: VERSION,
    };
  },
};
</script>
