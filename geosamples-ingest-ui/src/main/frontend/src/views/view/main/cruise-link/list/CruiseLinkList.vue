<template>
  <InteractiveTable
    :breadcrumbs="[
      { text: 'Geosamples Ingest', to: { name: 'Home' } },
      { text: 'Cruise Link', active: true },
    ]"
    module="cruiseLink"
    read-authority="ROLE_CRUISE_LINK_READ"
    :fields="[
        { label: 'Cruise Name Contains', value: params.cruiseNameContains, set: setCruiseNameContains },
        { label: 'Datalink', value: params.dataLink, set: setDataLink },
        { label: 'Publish', value: params.publish, set: setPublish },
        { label: 'Link level', value: params.linkLevel, set: setLinkLevel },
        { label: 'Link Source', value: params.linkSource, set: setLinkSource },
        { label: 'Link Type', value: params.linkType, set: setLinkType },
        { label: 'ID', value: params.id, set: setId },
      ]"
    :table-fields="tableFields"
    create-route="CruiseLinkAdd"
    create-text="Add New Cruise Link"
    create-authority="ROLE_CRUISE_LINK_CREATE"
    edit-field="id"
    edit-parameter="id"
    edit-route="CruiseLinkEdit"
    edit-authority="ROLE_CRUISE_LINK_UPDATE"
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
    ...mapMutations('cruiseLink', [
      'setCruiseNameContains',
      'setCruisePlatform',
      'setCruiseName',
      'setCruiseYear',
      'setPlatform',
      'setLeg',
      'setLegName',
      'setDataLink',
      'setLinkLevel',
      'setLinkSource',
      'setLinkType',
      'setPublish',
      'setId',
    ]),
  },

  computed: {
    ...mapState('cruiseLink', ['params']),
  },

  data() {
    return {
      tableFields: [
        {
          key: 'id',
          label: 'Id',
          sortable: true,
        },
        {
          key: 'cruiseName',
          label: 'Cruise Name',
          sortable: true,
        },
        {
          key: 'cruiseYear',
          label: 'Cruise Year',
          sortable: true,
        },
        {
          key: 'platform',
          label: 'Platform',
          sortable: true,
        },
        {
          key: 'legName',
          label: 'Leg Name',
          sortable: true,
        },
        {
          key: 'dataLink',
          label: 'Data Link',
          sortable: true,
        },
        {
          key: 'publish',
          label: 'Publish',
          sortable: true,
        },
        {
          key: 'linkLevel',
          label: 'Link Level',
          sortable: true,
        },
        {
          key: 'linkSource',
          label: 'Link Source',
          sortable: true,
        },
        {
          key: 'linkType',
          label: 'Link Type',
          sortable: true,
        },
      ],
    };
  },
};
</script>
