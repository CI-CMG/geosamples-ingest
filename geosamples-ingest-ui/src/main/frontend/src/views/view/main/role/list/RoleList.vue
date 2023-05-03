<template>
  <InteractiveTable
    :breadcrumbs="[
      { text: 'Geosamples Ingest', to: { name: 'Home' } },
      { text: 'Roles', active: true },
    ]"
    module="role"
    read-authority="ROLE_USER_ROLE_READ"
    :fields="[
      { label: 'Role Name Contains', value: params.roleNameContains, set: setRoleNameContains },
    ]"
    :table-fields="tableFields"
    create-route="RoleAdd"
    create-text="Add New Role"
    create-authority="ROLE_USER_ROLE_CREATE"
    edit-field="roleName"
    edit-parameter="id"
    edit-route="RoleEdit"
    edit-authority="ROLE_USER_ROLE_UPDATE"
  />
</template>
<script>
import InteractiveTable from '@/components/InteractiveTable.vue';
import { mapMutations, mapState } from 'vuex';

export default {
  components: { InteractiveTable },

  methods: {
    ...mapMutations('role', ['setRoleNameContains']),
  },

  computed: {
    ...mapState('role', ['params']),
  },

  data() {
    return {
      tableFields: [
        {
          key: 'roleName',
          label: 'Role Name',
          sortable: true,
        },
      ],
    };
  },
};
</script>
