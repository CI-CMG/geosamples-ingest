<template>
  <InteractiveTable
    :breadcrumbs="[
      { text: 'Geosamples Ingest', to: { name: 'Home' } },
      { text: 'Geologic Age', active: true }
    ]"
    module="age"
    read-authority="ROLE_AGE_READ"
    :fields="[
      { label: 'Geologic Age', value: params.age, set: setAge },
      { label: 'Geologic Age Code', value: params.ageCode, set: setAgeCode },
    ]"
    :table-fields="tableFields"
    create-route="AgeAdd"
    create-text="Add New Geologic Age"
    create-authority="ROLE_AGE_CREATE"
    edit-field="age"
    edit-parameter="age"
    edit-authority="ROLE_AGE_UPDATE"
    edit-route="AgeEdit"
  />
</template>

<script>
import InteractiveTable from '@/components/InteractiveTable.vue';
import { mapMutations, mapState } from 'vuex';

export default {
  components: {
    InteractiveTable,
  },

  computed: {
    ...mapState('age', ['params']),
  },

  methods: {
    ...mapMutations('age', ['setAge', 'setAgeCode']),
  },

  data() {
    return {
      tableFields: [
        {
          key: 'age',
          label: 'Geologic Age',
          sortable: true,
        },
        {
          key: 'ageCode',
          label: 'Geologic Age Code',
          sortable: true,
        },
      ],
    };
  },
};
</script>
