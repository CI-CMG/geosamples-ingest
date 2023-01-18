<template>
  <InteractiveTable
    :breadcrumbs="[
      { text: 'Geosamples Ingest', to: { name: 'Home' } },
      { text: 'Sampling Device', active: true },
    ]"
    module="device"
    read-authority="ROLE_DEVICE_READ"
    :fields="[
      { label: 'Sampling Device', value: params.device, set: setDevice },
      { label: 'Sampling Device Code', value: params.deviceCode, set: setDeviceCode },
    ]"
    :table-fields="tableFields"
    create-route="DeviceAdd"
    create-text="Add New Sampling Device"
    create-authority="ROLE_DEVICE_CREATE"
    edit-field="device"
    edit-parameter="device"
    edit-route="DeviceEdit"
    edit-authority="ROLE_DEVICE_UPDATE"
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
    ...mapMutations('device', ['setDevice', 'setDeviceCode']),
  },

  computed: {
    ...mapState('device', ['params']),
  },

  data() {
    return {
      deviceId: null,
      deviceCodeId: null,

      tableFields: [
        {
          key: 'device',
          label: 'Sampling Device',
          sortable: true,
        },
        {
          key: 'deviceCode',
          label: 'Sampling Device Code',
          sortable: true,
        },
      ],
    };
  },
};
</script>
