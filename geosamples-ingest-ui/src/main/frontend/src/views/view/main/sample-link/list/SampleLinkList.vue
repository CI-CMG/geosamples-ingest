<template>
  <InteractiveTable
    module="sampleLink"
    read-authority="ROLE_SAMPLE_LINK_READ"
    :fields="[
        { label: 'IMLGS Equals', value: params.imlgs, set: setImlgs },
        { label: 'Datalink', value: params.dataLink, set: setDataLink },
        { label: 'Publish', value: params.publish, set: setPublish },
        { label: 'Link Level', value: params.linkLevel, set: setLinkLevel },
        { label: 'Link Source', value: params.linkSource, set: setLinkSource },
        { label: 'Link Type', value: params.linkType, set: setLinkType },
        { label: 'ID', value: params.id, set: setId },
      ]"
    :table-fields="tableFields"
    create-route="SampleLinkAdd"
    create-text="Add New Sample Link"
    create-authority="ROLE_SAMPLE_LINK_CREATE"
    edit-field="imlgs"
    edit-parameter="id"
    edit-route="SampleLinkEdit"
    edit-authority="ROLE_SAMPLE_LINK_UPDATE"
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
    ...mapMutations('sampleLink', [
      'setImlgs',
      'setDataLink',
      'setLinkLevel',
      'setLinkSource',
      'setLinkType',
      'setPublish',
      'setId',
    ]),
  },

  computed: {
    ...mapState('sampleLink', ['params']),
  },

  data() {
    return {
      tableFields: [
        {
          key: 'imlgs',
          label: 'IMLGS',
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
