<template>
  <div ref="swagger"/>
</template>

<script>
import { mapState } from 'vuex';
import { BASE_PATH } from '@/basePath';

const SwaggerUIBundle = require('swagger-ui-dist').SwaggerUIBundle;
const SwaggerUIStandalonePreset = require('swagger-ui-dist').SwaggerUIStandalonePreset;

export default {
  computed: {
    ...mapState('userAuth', ['token']),
  },

  mounted() {
    SwaggerUIBundle({
      domNode: this.$refs.swagger,
      url: `${BASE_PATH}/docs/api/v1-admin`,
      deepLinking: true,
      requestInterceptor: (r) => {
        if (this.token) {
          r.headers.authorization = `Bearer ${this.token}`;
        }
        return r;
      },
      presets: [
        SwaggerUIBundle.presets.apis,
        SwaggerUIStandalonePreset,
      ],
      plugins: [
        SwaggerUIBundle.plugins.DownloadUrl,
      ],
      layout: 'StandaloneLayout',
      tagsSorter: 'alpha',
    });
  },
};
</script>
