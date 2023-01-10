<template>
  <InteractiveTable
    module="platform"
    read-authority="ROLE_PLATFORM_READ"
    :fields="[
      { label: 'Ship/Platform', value: params.platform, set: setPlatform },
      { label: 'Master ID', value: params.masterId, set: setMasterId },
      { label: 'ICES Code', value: params.icesCode, set: setIcesCode },
    ]"
    :table-fields="tableFields"
    create-route="PlatformAdd"
    create-text="Add New Ship/Platform"
    create-authority="ROLE_PLATFORM_CREATE"
    edit-field="platform"
    edit-parameter="id"
    edit-route="PlatformEdit"
    edit-authority="ROLE_PLATFORM_UPDATE"
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
    ...mapMutations('platform', ['setPlatform', 'setIcesCode', 'setMasterId']),
  },

  computed: {
    ...mapState('platform', ['params']),
  },

  data() {
    return {
      tableFields: [
        {
          key: 'platform',
          label: 'Platform',
          sortable: true,
        },
        {
          key: 'masterId',
          label: 'Master ID',
          sortable: true,
        },
        {
          key: 'prefix',
          label: 'Prefix',
        },
        {
          key: 'icesCode',
          label: 'ICES Code',
          sortable: true,
        },
        {
          key: 'sourceUri',
          label: 'Source URI',
        },
      ],
    };
  },
};
</script>
