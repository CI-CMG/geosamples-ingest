<template>
  <InteractiveTable
    :breadcrumbs="[
      { text: 'Geosamples Ingest', to: { name: 'Home' } },
      { text: 'Users', active: true },
    ]"
    module="user"
    read-authority="ROLE_USER_READ"
    :fields="[
      { label: 'Username Contains', value: params.userNameContains, set: setUserNameContains },
      { label: 'Username Equals', value: params.userNameEquals, set: setUserNameEquals },
      { label: 'Display Name Contains', value: params.displayNameContains, set: setDisplayNameContains },
      { label: 'Facility Code Equals', value: params.facilityCode, set: setFacilityCode, options: optionsFacilityCode },
      { label: 'Role Equals', value: params.role, set: setRole, options: optionsRole }
    ]"
    :table-fields="tableFields"
    create-route="UserAdd"
    create-text="Add New User"
    create-authority="ROLE_USER_CREATE"
    edit-field="userName"
    edit-parameter="userName"
    edit-route="UserEdit"
    edit-authority="ROLE_USER_UPDATE"
  />
</template>

<script>

import {
  mapActions,
  mapMutations, mapState,
} from 'vuex';
import InteractiveTable from '@/components/InteractiveTable.vue';

export default {
  components: {
    InteractiveTable,
  },

  methods: {
    ...mapMutations('user', ['setUserNameEquals', 'setUserNameContains', 'setDisplayNameContains', 'setFacilityCode', 'setRole']),
    ...mapActions('user', ['loadSearchOptions']),
  },

  computed: {
    ...mapState('user', ['params', 'options']),

    optionsFacilityCode() {
      const { facilityCode: field } = this.options;
      return field || [];
    },

    optionsRole() {
      const { role: field } = this.options;
      return field || [];
    },
  },

  data() {
    return {
      tableFields: [
        {
          key: 'userName',
          label: 'Username',
          sortable: true,
        },
        {
          key: 'displayName',
          label: 'Display Name',
          sortable: true,
        },
        {
          key: 'role',
          label: 'Role',
          sortable: true,
        },
        {
          key: 'facility.facilityCode',
          label: 'Facility',
          sortable: true,
        },
      ],
    };
  },

  created() {
    this.loadSearchOptions();
  },
};
</script>
