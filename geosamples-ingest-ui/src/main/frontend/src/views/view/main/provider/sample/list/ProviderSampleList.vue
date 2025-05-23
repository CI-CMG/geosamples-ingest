<template>
  <InteractiveTable
    :breadcrumbs="[
      { text: 'Geosamples Ingest', to: { name: 'Home' } },
      { text: 'Samples', active: true },
    ]"
    module="providerSample"
    read-authority="ROLE_PROVIDER_SAMPLE_READ"
    :fields="[
      { label: 'IMLGS', value: params.imlgs, set: setImlgs },
      { label: 'Cruise Contains', value: params.cruise, set: setCruise },
      { label: 'Sample Contains', value: params.sample, set: setSample },
      { label: 'Platform Contains', value: params.platform, set: setPlatform },
      { label: 'Device', value: params.device, set: setDeviceCode },
      { label: 'IGSN', value: params.igsn, set: setIgsn },
      { label: 'Approval State', value: params.approvalState, set: setApprovalState, options: [
        'PENDING',
        'APPROVED',
        'REJECTED',
      ] },
      { label: 'Published', value: params.publish, set: setPublish, options: [
        'true',
        'false',
      ]}
    ]"
    :table-fields="tableFields"
    create-route="ProviderSampleAdd"
    create-text="Add New Sample"
    create-authority="ROLE_PROVIDER_SAMPLE_CREATE"
    edit-field="imlgs"
    edit-authority="ROLE_PROVIDER_SAMPLE_UPDATE"
    edit-parameter="imlgs"
    edit-route="ProviderSampleEdit"
    :is-approval="true"
    approval-text="Sample Review"
    :is-provider-table="true"
  />
</template>

<script>
import InteractiveTable from '@/components/InteractiveTable.vue';
import {
  mapMutations,
  mapState,
} from 'vuex';

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
          key: 'publish',
          label: 'Published',
        },
        {
          key: 'imlgs',
          label: 'IMLGS',
          sortable: true,
        },
        {
          key: 'igsn',
          label: 'Sample IGSN',
        },
        {
          key: 'cruise',
          label: 'Cruise',
          sortable: true,
        },
        {
          key: 'sample',
          label: 'Sample',
          sortable: true,
        },
        {
          key: 'facilityCode',
          label: 'Facility',
          sortable: false,
        },
        {
          key: 'platform',
          label: 'Platform',
          sortable: true,
        },
        {
          key: 'deviceCode',
          label: 'Sampling Device',
          sortable: true,
        },
        {
          key: 'beginDate',
          label: 'Begin Date',
        },
        {
          key: 'endDate',
          label: 'End Date',
        },
        {
          key: 'lat',
          label: 'Latitude',
        },
        {
          key: 'endLat',
          label: 'End Latitude',
        },
        {
          key: 'lon',
          label: 'Longitude',
        },
        {
          key: 'endLon',
          label: 'End Longitude',
        },
        {
          key: 'waterDepth',
          label: 'Water Depth',
        },
        {
          key: 'endWaterDepth',
          label: 'End Water Depth',
        },
        {
          key: 'storageMethCode',
          label: 'Storage Meth.',
        },
        {
          key: 'coredLength',
          label: 'Cored Length',
        },
        {
          key: 'coredDiam',
          label: 'Cored Diam.',
        },
        {
          key: 'pi',
          label: 'PI',
        },
        {
          key: 'provinceCode',
          label: 'Province',
        },
        {
          key: 'lake',
          label: 'Sample Lake',
        },
        {
          key: 'leg',
          label: 'Leg',
        },
        {
          key: 'sampleComments',
          label: 'Sample Comments',
        },

        {
          key: 'showSampl',
          label: 'Show Sample',
        },
      ],
    };
  },

  computed: {
    ...mapState('providerSample', ['params']),
  },

  methods: {
    ...mapMutations('providerSample', [
      'setImlgs',
      'setCruise',
      'setSample',
      'setPlatform',
      'setDeviceCode',
      'setIgsn',
      'setApprovalState',
      'setPublish',
    ]),
  },
};
</script>
