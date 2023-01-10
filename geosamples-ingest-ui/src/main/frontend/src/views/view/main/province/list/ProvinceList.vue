<template>
  <InteractiveTable
    module="province"
    read-authority="ROLE_PROVINCE_READ"
    :fields="[
      { label: 'Physiographic Province', value: params.province, set: setProvince },
      { label: 'Physiographic Province Code', value: params.provinceCode, set: setProvinceCode },
    ]"
    :table-fields="tableFields"
    create-route="ProvinceAdd"
    create-text="Add New Physiographic Province"
    create-authority="ROLE_PROVINCE_CREATE"
    edit-field="province"
    edit-parameter="province"
    edit-route="ProvinceEdit"
    edit-authority="ROLE_PROVINCE_UPDATE"
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
    ...mapMutations('province', ['setProvince', 'setProvinceCode']),
  },

  computed: {
    ...mapState('province', ['params']),
  },

  data() {
    return {
      tableFields: [
        {
          key: 'province',
          label: 'Physiographic Province',
          sortable: true,
        },
        {
          key: 'provinceCode',
          label: 'Physiographic Province Code',
          sortable: true,
        },
      ],
    };
  },
};
</script>
