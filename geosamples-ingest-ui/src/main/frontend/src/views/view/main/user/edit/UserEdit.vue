<template>
  <div class="m-2">

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

        <b-form-group label="Authorities" :label-for="authoritiesId">
          <b-form-select
            :id="authoritiesId"
            @blur="() => setTouched({path: 'displayName', touched: true})"
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
      authoritiesId: '',
    };
  },
  beforeMount() {
    this.userNameId = genId();
    this.displayNameId = genId();
    this.authoritiesId = genId();
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
    ...mapActions('user', ['load', 'save', 'delete']),
    ...mapActions('userForm', ['submit', 'reset']),
    ...mapActions('authority', { loadAuthorities: 'load' }),
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
    ...mapState('user', ['deleting', 'loading', 'saving']),
    ...mapGetters('userForm',
      [
        'getValue',
        'formDirty',
        'getError',
        'isTouched',
        'formHasUntouchedErrors',
      ]),
    ...mapState('authority', { authorities: 'authorities', loadingAuthorities: 'loading' }),
    ready() {
      return (!this.isEdit || !this.loading) && !this.loadingAuthorities;
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
