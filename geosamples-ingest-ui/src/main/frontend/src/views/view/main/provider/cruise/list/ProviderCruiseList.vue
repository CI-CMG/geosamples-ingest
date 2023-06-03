<template>
  <InteractiveTable
    :breadcrumbs="[
      { text: 'Geosamples Ingest', to: { name: 'Home' } },
      { text: 'Cruises', active: true },
    ]"
    module="providerCruise"
    read-authority="ROLE_PROVIDER_CRUISE_READ"
    :fields="[
        { label: 'Name Contains', value: params.cruiseNameContains, set: setCruiseNameContains },
        { label: 'Name Equals', value: params.cruiseNameEquals, set: setCruiseNameEquals },
        { label: 'Year', value: params.year, set: setYear },
        { label: 'Published', value: params.publish, set: setPublish, options: ['true', 'false'] },
        { label: 'Platform Equals', value: params.platformEquals, set: setPlatformEquals },
        { label: 'Approval State', value: params.approvalState, set: setApprovalState, options: [
          'APPROVED', 'PENDING', 'REJECTED'
        ] }
      ]"
    :table-fields="tableFields"
    create-route="ProviderCruiseAdd"
    create-text="Add New Cruise"
    create-authority="ROLE_PROVIDER_CRUISE_CREATE"
    edit-field="cruiseName"
    edit-authority="ROLE_PROVIDER_CRUISE_UPDATE"
    edit-parameter="id"
    edit-route="ProviderCruiseEdit"
    :is-approval="true"
    approval-text="Cruise Review"
    :is-provider-table="true"
  />
</template>

<script>
import InteractiveTable from '@/components/InteractiveTable.vue';
import { mapMutations, mapState } from 'vuex';

export default {
  components: { InteractiveTable },

  data() {
    return {
      tableFields: [
        {
          key: 'approvalState',
          label: 'Approval State',
          sortable: true,
        },
        {
          key: 'cruiseName',
          label: 'Cruise Name',
          sortable: true,
        },
        {
          key: 'year',
          label: 'Year',
          sortable: true,
        },
        {
          key: 'publish',
          label: 'Published',
          sortable: true,
        },
        {
          key: 'platforms',
          label: 'Platforms',
          multivalued: true,
        },
      ],
    };
  },

  computed: {
    ...mapState('providerCruise', ['params']),
  },

  methods: {
    ...mapMutations('providerCruise', [
      'setCruiseNameContains',
      'setCruiseNameEquals',
      'setPlatformEquals',
      'setYear',
      'setPlatformEquals',
      'setPublish',
      'setApprovalState',
    ]),
  },
};
</script>
