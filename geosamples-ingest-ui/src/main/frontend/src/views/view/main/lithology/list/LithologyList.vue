<template>
  <InteractiveTable
    module="lithology"
    read-authority="ROLE_LITHOLOGY_READ"
    :fields="[
      { label: 'Lithologic Composition', value: params.lithology, set: setLithology },
      { label: 'Lithologic Composition Code', value: params.lithologyCode, set: setLithologyCode },
    ]"
    :table-fields="tableFields"
    create-route="LithologyAdd"
    create-text="Add New Lithologic Composition"
    create-authority="ROLE_LITHOLOGY_CREATE"
    edit-field="lithology"
    edit-parameter="lithology"
    edit-route="LithologyEdit"
    edit-authority="ROLE_LITHOLOGY_UPDATE"
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
    ...mapMutations('lithology', ['setLithology', 'setLithologyCode']),
  },

  computed: {
    ...mapState('lithology', ['params']),
  },

  data() {
    return {
      tableFields: [
        {
          key: 'lithology',
          label: 'Lithologic Composition',
          sortable: true,
        },
        {
          key: 'lithologyCode',
          label: 'Lithologic Composition Code',
          sortable: true,
        },
      ],
    };
  },
};
</script>
