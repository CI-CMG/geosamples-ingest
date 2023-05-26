<template>
  <InteractiveTable
    :breadcrumbs="[
      { text: 'Geosamples Ingest', to: { name: 'Home' } },
      { text: 'Ships/Platforms', active: true },
    ]"
    module="providerPlatform"
    read-authority="ROLE_PROVIDER_PLATFORM_READ"
    :fields="[
      { label: 'Ship/Platform', value: params.platform, set: setPlatform },
      { label: 'Master ID', value: params.masterId, set: setMasterId },
      { label: 'ICES Code', value: params.icesCode, set: setIcesCode },
      { label: 'Approval State', value: params.approvalState, set: setApprovalState, options: [
          'PENDING', 'REJECTED'
        ] }
    ]"
    :table-fields="tableFields"
    create-route="ProviderPlatformAdd"
    create-text="Add New Ship/Platform"
    create-authority="ROLE_PROVIDER_PLATFORM_CREATE"
    edit-field="platform"
    edit-authority="ROLE_PROVIDER_PLATFORM_UPDATE"
    edit-parameter="id"
    edit-route="ProviderPlatformEdit"
    :is-approval="true"
    approval-text="Ship/Platform Review"
    :is-provider-table="true"
  />
</template>
<script>
import InteractiveTable from '@/components/InteractiveTable.vue';
import { mapMutations, mapState } from 'vuex';

export default {
  components: { InteractiveTable },

  methods: {
    ...mapMutations('providerPlatform', ['setPlatform', 'setIcesCode', 'setMasterId', 'setApprovalState']),
  },

  computed: {
    ...mapState('providerPlatform', ['params']),
  },

  data() {
    return {
      tableFields: [
        {
          key: 'approvalState',
          label: 'Approval State',
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
