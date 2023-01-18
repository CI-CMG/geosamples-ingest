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
  mapMutations, mapState,
} from 'vuex';
import InteractiveTable from '@/components/InteractiveTable.vue';

export default {
  components: {
    InteractiveTable,
  },

  methods: {
    ...mapMutations('user', ['setUserNameEquals', 'setUserNameContains', 'setDisplayNameContains']),
  },

  computed: {
    ...mapState('user', ['params']),
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
      ],
    };
  },
};
</script>
