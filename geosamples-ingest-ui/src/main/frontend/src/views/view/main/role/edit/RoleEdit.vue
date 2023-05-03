<template>
  <div class="m-2">
    <b-breadcrumb :items="[
      { text: 'Geosamples Ingest', to: { name: 'Home' } },
      { text: 'Roles', to: { name: 'RoleList' } },
      { text: 'Edit Role', active: true },
    ]"/>

    <div v-if="ready">
      <h1 v-if="isEdit" class="text-primary">Edit Role - {{ getValue('id') }}</h1>
      <h1 v-else class="text-primary">Add New Role</h1>

      <b-button v-if="isEdit" type="button" variant="danger" @click="showModal" >Delete</b-button>
      <b-modal ref="delete-modal" title="Delete User" ok-variant="danger" ok-title="Delete" @ok="doDelete">
        <p class="my-4">Are you sure you want to delete this user?</p>
      </b-modal>

      <b-form @submit.prevent="saveForm" @reset.prevent="reset">
        <b-form-group label="Role Name" :label-for="roleNameId">
          <b-form-input
            :id="roleNameId"
            type="text" @blur="() => setTouched({path: 'roleName', touched: true})"
            :value="getValue('roleName')"
            @update="(value) => setValue({ path: 'roleName', value })"
            :state="showError('roleName')"
          />
          <b-form-invalid-feedback>{{ getError('roleName') }}</b-form-invalid-feedback>
        </b-form-group>
        <b-form-group label="Authorities" :label-for="authoritiesId">
          <b-form-select
            :id="authoritiesId"
            @blur="() => setTouched({path: 'authorities', touched: true})"
            :value="selectedAuthorities"
            :options="authorities"
            @change="setSelectedAuthorities"
            multiple
          />
          <div class="mt-3">Selected Authorities: <strong>{{ selectedAuthorities }}</strong></div>
        </b-form-group>
        <div>
          <b-button v-if="showSubmit" type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">Save</b-button>
          <b-button v-if="formDirty" type="reset" variant="danger" class="mb-2 mr-sm-2 mb-sm-0">Reset</b-button>
        </div>
      </b-form>
    </div>
  </div>
</template>

<script>
import {
  mapActions,
  mapGetters,
  mapMutations,
  mapState,
} from 'vuex';
import genId from '@/components/idGenerator';

export default {
  props: ['id'],

  data() {
    return {
      roleNameId: null,
      authoritiesId: null,
    };
  },

  beforeMount() {
    this.roleNameId = genId();
    this.authoritiesId = genId();
  },

  methods: {
    ...mapActions('role', ['delete', 'save', 'load']),
    ...mapActions('roleForm', ['submit', 'reset']),
    ...mapActions('authority', ['loadAuthorities']),
    ...mapMutations('roleForm', ['setValue', 'setTouched', 'initialize', 'addToArray', 'deleteFromArray']),

    showModal() {
      this.$refs['delete-modal'].show();
    },

    saveForm() {
      this.submit()
        .then((role) => this.save({ role, roleId: this.id }))
        .then(() => this.$router.push({ name: 'RoleList' }, () => window.location.reload()));
    },

    doDelete() {
      this.delete(this.id).then(() => this.$router.push({ name: 'RoleList' }));
    },

    setSelectedAuthorities(values) {
      const existingSize = this.getValue('authorities').length;
      for (let i = 0; i < existingSize; i += 1) {
        this.deleteFromArray('authorities[0]');
      }
      for (let k = 0; k < values.length; k += 1) {
        this.addToArray({ path: 'authorities', value: values[k] });
      }
    },
  },

  computed: {
    ...mapState('role', ['loading']),
    ...mapState('authority', { authorities: 'authorities', loadingAuthorities: 'loading' }),
    ...mapGetters('roleForm', ['getValue', 'getError', 'formDirty', 'getError', 'formHasUntouchedErrors', 'isTouched']),

    ready() {
      return (!this.isEdit || !this.loading) && !this.loadingAuthorities;
    },

    isEdit() {
      return this.id || this.id === 0;
    },

    showSubmit() {
      return this.formDirty && !this.formHasUntouchedErrors;
    },

    showError() {
      return (path) => ((!this.isTouched(path) && this.getError(path)) ? false : null);
    },

    selectedAuthorities() {
      const authorities = this.getValue('authorities');
      if (!authorities) {
        return [];
      }
      return authorities.map((x) => x.value);
    },
  },

  watch: {
    id(oldId, newId) {
      if (newId != null) {
        this.load(newId).then(this.initialize);
      } else {
        this.initialize();
      }
    },
  },
  created() {
    this.loadAuthorities();
    if (this.id != null) {
      this.load(this.id).then(this.initialize);
    } else {
      this.initialize();
    }
  },
};
</script>
