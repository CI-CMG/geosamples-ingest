<template>
  <div class="m-2">

    <b-breadcrumb :items="[
      { text: 'Geosamples Ingest', to: { name: 'Home' } },
      { text: 'Users', to: { name: 'UserList' } },
      { text: 'Edit User', active: true },
    ]"/>

    <div v-if="ready">

      <h1 v-if="isEdit" class="text-primary">Edit User - {{ getValue('userName') }}</h1>
      <h1 v-else class="text-primary">Add New User</h1>

      <b-button v-if="isEdit" type="button" variant="danger" @click="showModal" >Delete</b-button>
      <b-modal ref="delete-modal" title="Delete User" ok-variant="danger" ok-title="Delete" @ok="doDelete">
        <p class="my-4">Are you sure you want to delete this user?</p>
      </b-modal>

      <b-form @submit.prevent="saveForm" @reset.prevent="reset">

        <b-form-group v-if="!isEdit" label="Username" :label-for="userNameId">
          <b-form-input
            :id="userNameId"
            type="text" @blur="() => setTouched({path: 'userName', touched: true})"
            :value="getValue('userName')"
            @update="(value) => setValue({ path: 'userName', value })"
            :state="showError('userName')"
          />
          <b-form-invalid-feedback>{{ getError('userName') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Display Name" :label-for="displayNameId">
          <b-form-input
            :id="displayNameId"
            type="text" @blur="() => setTouched({path: 'displayName', touched: true})"
            :value="getValue('displayName')"
            @update="(value) => setValue({ path: 'displayName', value })"
            :state="showError('displayName')"
          />
          <b-form-invalid-feedback>{{ getError('displayName') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Role" :label-for="roleId">
          <b-form-select
            :id="roleId"
            @blur="() => setTouched({path: 'role', touched: true})"
            :value="getValue('role')"
            :options="roles"
            @change="(value) => setValue({ path: 'role', value })"
          />
          <b-form-invalid-feedback>{{ getError('role') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Facility" :label-for="facilityId">
          <b-form-select
            :id="facilityId"
            :disabled="!loggedInUserHasReadFacilityAuthority"
            @blur="() => setTouched({path: 'facility', touched: true})"
            :value="currentFacility"
            :options="optionsFacilityCode"
            @change="(value) => setFacility(value)"
          />
          <b-form-invalid-feedback>{{ getError('facility') }}</b-form-invalid-feedback>
        </b-form-group>

        <div>
          <b-button v-if="showSubmit" type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">Save</b-button>
          <b-button v-if="formDirty" type="reset" variant="danger" class="mb-2 mr-sm-2 mb-sm-0">Reset</b-button>
        </div>

      </b-form>
    </div>
    <div v-else>
      <b-spinner/>
    </div>
  </div>
</template>

<script>
import {
  mapActions, mapGetters, mapMutations, mapState,
} from 'vuex';
import genId from '@/components/idGenerator';

export default {
  props: ['id'],
  data() {
    return {
      userNameId: '',
      displayNameId: '',
      roleId: '',
      facilityId: '',
    };
  },
  beforeMount() {
    this.userNameId = genId();
    this.displayNameId = genId();
    this.roleId = genId();
    this.facilityId = genId();
  },
  methods: {
    ...mapMutations('userForm',
      [
        'initialize',
        'setValue',
        'setTouched',
        'setError',
        'deleteFromArray',
        'addToArray',
      ]),
    ...mapActions('user', ['load', 'save', 'delete', 'loadOptions']),
    ...mapActions('userForm', ['submit', 'reset']),
    ...mapActions('authority', { loadRoles: 'loadRoles' }),
    showModal() {
      this.$refs['delete-modal'].show();
    },
    hideModal() {
      this.$refs['delete-modal'].hide();
    },
    saveForm() {
      this.submit()
        .then((user) => this.save({ user, userName: this.id }))
        .then(() => this.$router.push({ name: 'UserList' }, () => window.location.reload()));
    },
    doDelete() {
      this.delete(this.id).then(() => this.$router.push({ name: 'UserList' }));
    },
    setFacility(value) {
      this.setValue({ path: 'facility.id', value: value.id });
      this.setValue({ path: 'facility.facilityCode', value: value.facilityCode });
    },
  },

  computed: {
    ...mapState('user', ['deleting', 'loading', 'saving', 'options']),
    ...mapState('userAuth', { loggedInUser: 'user', loadingLoggedInUser: 'loading' }),
    ...mapGetters('userForm',
      [
        'getValue',
        'formDirty',
        'getError',
        'isTouched',
        'formHasUntouchedErrors',
      ]),
    ...mapState('authority', { roles: 'roles', loadingRoles: 'loading' }),
    currentFacility() {
      const currentFacilityId = this.getValue('facility.id');
      if (!currentFacilityId) {
        return null;
      }

      const option = this.optionsFacilityCode.find((f) => String(f.value.id) === currentFacilityId);
      return option ? option.value : null;
    },
    ready() {
      return (!this.isEdit || !this.loading) && !this.loadingRoles && !this.loadingLoggedInUser;
    },
    showError() {
      return (path) => ((!this.isTouched(path) && this.getError(path)) ? false : null);
    },
    showSubmit() {
      return this.formDirty && !this.formHasUntouchedErrors;
    },
    isEdit() {
      return this.id || this.id === 0;
    },
    loggedInUserHasReadFacilityAuthority() {
      return this.loggedInUser.authorities.some((a) => a === 'ROLE_FACILITY_READ');
    },
    optionsFacilityCode() {
      const { facilityCode: field } = this.options;
      return field || [];
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
    this.loadRoles();
    this.loadOptions();
    if (this.id != null) {
      this.load(this.id).then(this.initialize);
    } else {
      this.initialize();
    }
  },

};
</script>
