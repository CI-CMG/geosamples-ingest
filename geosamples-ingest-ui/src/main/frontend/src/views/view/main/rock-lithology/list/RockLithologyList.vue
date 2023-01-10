<template>
  <InteractiveTable
    module="rockLithology"
    read-authority="ROLE_ROCK_LITHOLOGY_READ"
    :fields="[
      { label: 'Rock Lithology', value: params.rockLithology, set: setRockLithology },
      { label: 'Rock Lithology Code', value: params.rockLithologyCode, set: setRockLithologyCode },
    ]"
    :table-fields="tableFields"
    create-route="RockLithologyAdd"
    create-text="Add New Rock Lithology"
    create-authority="ROLE_ROCK_LITHOLOGY_CREATE"
    edit-field="rockLithology"
    edit-parameter="rockLithology"
    edit-route="RockLithologyEdit"
    edit-authority="ROLE_ROCK_LITHOLOGY_UPDATE"
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
    ...mapMutations('rockLithology', ['setRockLithology', 'setRockLithologyCode']),
  },

  computed: {
    ...mapState('rockLithology', ['params']),
  },

  data() {
    return {
      tableFields: [
        {
          key: 'rockLithology',
          label: 'RockLithology',
          sortable: true,
        },
        {
          key: 'rockLithologyCode',
          label: 'RockLithology Code',
          sortable: true,
        },
      ],
    };
  },
};
</script>
