<template>
  <InteractiveTable
    :breadcrumbs="[
      { text: 'Geosamples Ingest', to: { name: 'Home' } },
      { text: 'Texture', active: true },
    ]"
    module="texture"
    read-authority="ROLE_TEXTURE_READ"
    :fields="[
      { label: 'Texture', value: params.texture, set: setTexture },
      { label: 'Texture Code', value: params.textureCode, set: setTextureCode },
    ]"
    :table-fields="tableFields"
    create-route="TextureAdd"
    create-text="Add New Texture"
    create-authority="ROLE_TEXTURE_CREATE"
    edit-field="texture"
    edit-parameter="texture"
    edit-route="TextureEdit"
    edit-authority="ROLE_TEXTURE_UPDATE"
  />
</template>

<script>

import {
  mapMutations, mapState,
} from 'vuex';
import InteractiveTable from '@/components/InteractiveTable.vue';

export default {
  components: {
    InteractiveTable,
  },

  methods: {
    ...mapMutations('texture', ['setTexture', 'setTextureCode']),
  },

  computed: {
    ...mapState('texture', ['params']),
  },

  data() {
    return {
      tableFields: [
        {
          key: 'texture',
          label: 'Texture',
          sortable: true,
        },
        {
          key: 'textureCode',
          label: 'Texture Code',
          sortable: true,
        },
      ],
    };
  },
};
</script>
