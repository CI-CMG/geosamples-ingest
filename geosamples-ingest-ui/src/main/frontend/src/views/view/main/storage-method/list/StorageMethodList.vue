<template>
  <InteractiveTable
    module="storageMethod"
    read-authority="ROLE_STORAGE_METHOD_READ"
    :fields="[
      { label: 'Storage Method', value: params.storageMethod, set: setStorageMethod },
      { label: 'Storage Method Code', value: params.storageMethodCode, set: setStorageMethodCode },
    ]"
    :table-fields="tableFields"
    create-route="StorageMethodAdd"
    create-text="Add New Storage Method"
    create-authority="ROLE_STORAGE_METHOD_CREATE"
    edit-field="storageMethod"
    edit-parameter="storageMethod"
    edit-route="StorageMethodEdit"
    edit-authority="ROLE_STORAGE_METHOD_UPDATE"
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
    ...mapMutations('storageMethod', ['setStorageMethod', 'setStorageMethodCode']),
  },

  computed: {
    ...mapState('storageMethod', ['params']),
  },

  data() {
    return {
      tableFields: [
        {
          key: 'storageMethod',
          label: 'Storage Method',
          sortable: true,
        },
        {
          key: 'storageMethodCode',
          label: 'Storage Method Code',
          sortable: true,
        },
      ],
    };
  },
};
</script>
