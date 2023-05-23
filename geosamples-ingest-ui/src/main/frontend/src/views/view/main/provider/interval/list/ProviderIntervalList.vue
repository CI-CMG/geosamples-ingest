<template>
  <InteractiveTable
    :breadcrumbs="[
      { text: 'Geosamples Ingest', to: { name: 'Home' } },
      { text: 'Intervals', active: true },
    ]"
    module="providerInterval"
    read-authority="ROLE_PROVIDER_INTERVAL_READ"
    :fields="[]"
    :table-fields="tableFields"
    create-route="ProviderIntervalAdd"
    create-text="Add New Interval"
    create-authority="ROLE_PROVIDER_INTERVAL_CREATE"
    edit-field="interval"
    edit-authority="ROLE_PROVIDER_INTERVAL_UPDATE"
    edit-parameter="id"
    edit-route="ProviderIntervalEdit"
    :is-approval="true"
    approval-text="Interval Review"
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
        { key: 'approvalState', label: 'Approval State' },
        { key: 'publish', label: 'Publish' },
        { key: 'imlgs', label: 'IMLGS' },
        { key: 'igsn', label: 'IGSN' },
        { key: 'interval', label: 'Interval' },
        { key: 'depthTop', label: 'Depth to Top of Interval' },
        { key: 'depthBot', label: 'Depth to Bottom of Interval' },
        { key: 'lithCode1', label: 'Primary Lithology Code' },
        { key: 'lithCode2', label: 'Secondary Lithology Code' },
        { key: 'textCode1', label: 'Primary Texture Code' },
        { key: 'textCode2', label: 'Secondary Lithology Code' },
        { key: 'compCode1', label: 'Lithologic Composition Code 1' },
        { key: 'compCode2', label: 'Lithologic Composition Code 2' },
        { key: 'compCode3', label: 'Lithologic Composition Code 3' },
        { key: 'compCode4', label: 'Lithologic Composition Code 4' },
        { key: 'compCode5', label: 'Lithologic Composition Code 5' },
        { key: 'compCode6', label: 'Lithologic Composition Code 6' },
        { key: 'ageCodes', label: 'Geologic Ages' },
        { key: 'weight', label: 'Weight' },
        { key: 'rockLithCode', label: 'Rock Lithology Code' },
        { key: 'rockMinCode', label: 'Rock Mineralogy Code' },
        { key: 'weathMetaCode', label: 'Weathering/Metamorphism Code' },
        { key: 'remarkCode', label: 'Remark Code' },
        { key: 'munsellCode', label: 'Munsell Code' },
        { key: 'exhausted', label: 'Exhausted' },
        { key: 'intComments', label: 'Interval Comments' },
      ],
    };
  },
};
</script>
