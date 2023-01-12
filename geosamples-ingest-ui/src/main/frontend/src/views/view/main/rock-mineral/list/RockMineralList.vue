<template>
  <InteractiveTable
    module="rockMineral"
    read-authority="ROLE_ROCK_MINERAL_READ"
    :fields="[
      { label: 'Rock Mineral', value: params.rockMineral, set: setRockMineral },
      { label: 'Rock Mineral Code', value: params.rockMineralCode, set: setRockMineralCode },
    ]"
    :table-fields="tableFields"
    create-route="RockMineralAdd"
    create-text="Add New Rock Mineralogy"
    create-authority="ROLE_ROCK_MINERAL_CREATE"
    edit-field="rockMineral"
    edit-parameter="rockMineral"
    edit-route="RockMineralEdit"
    edit-authority="ROLE_ROCK_MINERAL_UPDATE"
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
    ...mapMutations('rockMineral', ['setRockMineral', 'setRockMineralCode']),
  },

  computed: {
    ...mapState('rockMineral', ['params']),
  },

  data() {
    return {
      tableFields: [
        {
          key: 'rockMineral',
          label: 'Rock Mineral',
          sortable: true,
        },
        {
          key: 'rockMineralCode',
          label: 'Rock Mineral Code',
          sortable: true,
        },
      ],
    };
  },
};
</script>
