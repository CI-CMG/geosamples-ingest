<template>
  <InteractiveTable
    module="munsell"
    read-authority="ROLE_MUNSELL_READ"
    :fields="[
      { label: 'Munsell Color', value: params.munsell, set: setMunsell },
      { label: 'Munsell Color Code', value: params.munsellCode, set: setMunsellCode },
    ]"
    :table-fields="tableFields"
    create-route="MunsellAdd"
    create-text="Add New Munsell Color Code"
    create-authority="ROLE_MUNSELL_CREATE"
    edit-field="munsellCode"
    edit-parameter="munsellCode"
    edit-route="MunsellEdit"
    edit-authority="ROLE_MUNSELL_UPDATE"
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
    ...mapMutations('munsell', ['setMunsell', 'setMunsellCode']),
  },

  computed: {
    ...mapState('munsell', ['params']),
  },

  data() {
    return {
      tableFields: [
        {
          key: 'munsellCode',
          label: 'Munsell Color Code',
          sortable: true,
        },
        {
          key: 'munsell',
          label: 'Munsell Color',
          sortable: true,
        },
      ],
    };
  },
};
</script>
