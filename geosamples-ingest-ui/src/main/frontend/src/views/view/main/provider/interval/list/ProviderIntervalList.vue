<template>
  <InteractiveTable
    :breadcrumbs="[
      { text: 'Geosamples Ingest', to: { name: 'Home' } },
      { text: 'Subsamples/Intervals', active: true },
    ]"
    module="providerInterval"
    read-authority="ROLE_PROVIDER_INTERVAL_READ"
    :fields="[]"
    :table-fields="tableFields"
    create-route="ProviderIntervalAdd"
    create-text="Add New Subsample/Interval"
    create-authority="ROLE_PROVIDER_INTERVAL_CREATE"
    edit-field="interval"
    edit-authority="ROLE_PROVIDER_INTERVAL_UPDATE"
    edit-parameter="id"
    edit-route="ProviderIntervalEdit"
    :is-approval="true"
    approval-text="Subsample/Interval Review"
    :is-provider-table="true"
  />
</template>

<script>
import InteractiveTable from '@/components/InteractiveTable.vue';

export default {
  components: { InteractiveTable },

  data() {
    return {
      tableFields: [
        { key: 'approvalState', label: 'Approval State', sortable: true },
        { key: 'publish', label: 'Published' },
        { key: 'imlgs', label: 'IMLGS' },
        { key: 'igsn', label: 'IGSN' },
        { key: 'interval', label: 'Interval' },
        { key: 'depthTop', label: 'Depth to Top of Interval' },
        { key: 'depthBot', label: 'Depth to Bottom of Interval' },
        { key: 'lithCode1', label: 'Primary Lithology Code' },
        { key: 'lithCode2', label: 'Secondary Lithology Code' },
        { key: 'textCode1', label: 'Primary Texture Code' },
        { key: 'textCode2', label: 'Secondary Lithology Code' },
        { key: 'compCode1', label: 'Other Component Code 1' },
        { key: 'compCode2', label: 'Other Component Code 2' },
        { key: 'compCode3', label: 'Other Component Code 3' },
        { key: 'compCode4', label: 'Other Component Code 4' },
        { key: 'compCode5', label: 'Other Component Code 5' },
        { key: 'compCode6', label: 'Other Component Code 6' },
        { key: 'ageCodes', label: 'Geologic Ages', multivalued: true },
        { key: 'weight', label: 'Weight' },
        { key: 'rockLithCode', label: 'Rock Lithology Code' },
        { key: 'rockMinCode', label: 'Rock Mineralogy Code' },
        { key: 'weathMetaCode', label: 'Weathering/Metamorphism Code' },
        { key: 'remarkCode', label: 'Remark Code' },
        { key: 'munsellCode', label: 'Munsell Code' },
        { key: 'exhausted', label: 'Exhausted' },
        { key: 'intComments', label: 'Comments on Subsample/Interval' },
      ],
    };
  },
};
</script>
