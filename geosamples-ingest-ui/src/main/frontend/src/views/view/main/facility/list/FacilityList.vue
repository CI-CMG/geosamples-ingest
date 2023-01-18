<template>
  <InteractiveTable
    :breadcrumbs="[
      { text: 'Geosamples Ingest', to: { name: 'Home' } },
      { text: 'Facility', active: true },
    ]"
    module="facility"
    read-authority="ROLE_FACILITY_READ"
    :fields="[
      { label: 'Facility', value: params.facility, set: setFacility },
      { label: 'Facility Code', value: params.facilityCode, set: setFacilityCode },
    ]"
    :table-fields="tableFields"
    create-route="FacilityAdd"
    create-text="Add New Facility"
    create-authority="ROLE_FACILITY_CREATE"
    edit-field="facilityCode"
    edit-parameter="id"
    edit-route="FacilityEdit"
    edit-authority="ROLE_FACILITY_UPDATE"
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
    ...mapMutations('facility', ['setFacility', 'setFacilityCode']),
  },

  computed: {
    ...mapState('facility', ['params']),
  },

  data() {
    return {
      tableFields: [
        {
          key: 'facilityCode',
          label: 'Facility Code',
          sortable: true,
        },
        {
          key: 'facility',
          label: 'Facility',
          sortable: true,
        },
      ],
    };
  },
};
</script>
