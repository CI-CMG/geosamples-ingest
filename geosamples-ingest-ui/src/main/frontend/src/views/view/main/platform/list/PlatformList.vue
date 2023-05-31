<template>
  <InteractiveTable
    :breadcrumbs="[
      { text: 'Geosamples Ingest', to: { name: 'Home' } },
      { text: 'Ship/Platform', active: true },
    ]"
    module="platform"
    read-authority="ROLE_PLATFORM_READ"
    :fields="[
      { label: 'Ship/Platform', value: params.platform, set: setPlatform },
      { label: 'Master ID', value: params.masterId, set: setMasterId },
      { label: 'ICES Code', value: params.icesCode, set: setIcesCode },
      { label: 'Approval State', value: params.approvalState, set: setApprovalState, options: [
          'APPROVED', 'PENDING', 'REJECTED'
        ] }
    ]"
    :table-fields="user.authorities.includes('ROLE_PLATFORM_UPDATE') ? adminTableFields : providerTableFields"
    create-route="PlatformAdd"
    create-text="Add New Ship/Platform"
    create-authority="ROLE_PLATFORM_CREATE"
    edit-field="platform"
    edit-parameter="id"
    edit-route="PlatformEdit"
    edit-authority="ROLE_PLATFORM_UPDATE"
    :is-approval="true"
    approval-text="Review Ship/Platform"
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
    ...mapMutations('platform', ['setPlatform', 'setIcesCode', 'setMasterId', 'setApprovalState']),
  },

  computed: {
    ...mapState('platform', ['params']),
    ...mapState('userAuth', ['user']),
  },

  data() {
    return {
      providerTableFields: [
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
      adminTableFields: [
        {
          key: 'approvalState',
          label: 'Approval State',
          sortable: true,
        },
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
