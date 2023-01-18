<template>
  <InteractiveTable
    :breadcrumbs="[
      { text: 'Geosamples Ingest', to: { name: 'Home' } },
      { text: 'Cruise', active: true },
    ]"
    module="cruise"
    read-authority="ROLE_CRUISE_READ"
    :fields="[
        { label: 'Name Contains', value: params.cruiseNameContains, set: setCruiseNameContains },
        { label: 'Name Equals', value: params.cruiseNameEquals, set: setCruiseNameEquals },
        { label: 'Year', value: params.year, set: setYear },
        { label: 'Publish', value: params.publish, set: setPublish },
        { label: 'Facility Code Equals', value: params.facilityCodeEquals, set: setFacilityCodeEquals },
        { label: 'Platform Equals', value: params.platformEquals, set: setPlatformEquals },
        { label: 'ID', value: params.id, set: setId },
      ]"
    :table-fields="tableFields"
    create-route="CruiseAdd"
    create-text="Add New Cruise"
    create-authority="ROLE_CRUISE_CREATE"
    edit-field="cruiseName"
    edit-authority="ROLE_CRUISE_UPDATE"
    edit-parameter="id"
    edit-route="CruiseEdit"
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
    ...mapMutations('cruise', [
      'setCruiseNameContains',
      'setCruiseNameEquals',
      'setPlatformEquals',
      'setYear',
      'setPublish',
      'setFacilityCodeEquals',
      'setPlatformEquals',
      'setId',
    ]),
  },

  computed: {
    ...mapState('cruise', ['params']),
    currentPage: {
      get() {
        return this.page;
      },
      set(value) {
        this.setPage(value);
      },
    },
  },

  data() {
    return {
      tableFields: [
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
          label: 'Publish',
          sortable: true,
        },
        {
          key: 'facilityCodes',
          label: 'Facilities',
          sortable: true,
        },
        {
          key: 'platforms',
          label: 'Platforms',
          sortable: true,
        },
      ],
    };
  },
};
</script>
