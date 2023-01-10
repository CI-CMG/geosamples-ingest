<template>
  <InteractiveTable
    module="remark"
    read-authority="ROLE_REMARK_READ"
    :fields="[
      { label: 'Rock Glass Remarks & Mn/Fe Oxide', value: params.remark, set: setRemark },
      { label: 'Rock Glass Remarks & Mn/Fe Oxide Code', value: params.remarkCode, set: setRemarkCode },
    ]"
    :table-fields="tableFields"
    create-route="RemarkAdd"
    create-text="Add New Rock Glass"
    create-authority="ROLE_REMARK_CREATE"
    edit-field="remark"
    edit-parameter="remark"
    edit-route="RemarkEdit"
    edit-authority="ROLE_REMARK_UPDATE"
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
    ...mapMutations('remark', ['setRemark', 'setRemarkCode']),
  },

  computed: {
    ...mapState('remark', ['params']),
  },

  data() {
    return {
      tableFields: [
        {
          key: 'remark',
          label: 'Rock Glass Remarks & Mn/Fe Oxide',
          sortable: true,
        },
        {
          key: 'remarkCode',
          label: 'Rock Glass Remarks & Mn/Fe Oxide Code',
          sortable: true,
        },
      ],
    };
  },
};
</script>
