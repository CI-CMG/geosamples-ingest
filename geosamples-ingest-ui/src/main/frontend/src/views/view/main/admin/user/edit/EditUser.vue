<template>
  <div class="m-2">
    <b-breadcrumb :items="bc"/>

    <div v-if="ready">
      <b-form @submit.prevent="save" @reset.prevent="reset"><br />

        <hr />
        <h1 class="text-primary">ID: "{{ getValue('userName') }}" (version: {{ getValue('version') }})</h1>
        <br />

        <div>
          <b-card>

            <b-col sm="6">
              <label :for="userNameId">User Name:</label>
              <b-form-input
                :id="userNameId"
                type="text"
                @blur="() => setTouched({path: 'userName', touched: true})"
                :value="getValue('userName')"
                @update="(value) => setValue({ path: 'userName', value })"
                :state="showError('userName')"
              />
              <b-form-invalid-feedback>{{ getError('userName') }}</b-form-invalid-feedback>
            </b-col><br />

            <b-col sm="6">
              <label :for="displayNameId">Display Name:</label>
              <b-form-input
                :id="displayNameId"
                type="text"
                @blur="() => setTouched({path: 'displayName', touched: true})"
                :value="getValue('displayName')"
                @update="(value) => setValue({ path: 'displayName', value })"
                :state="showError('displayName')"
              />
              <b-form-invalid-feedback>{{ getError('displayName') }}</b-form-invalid-feedback>
            </b-col><br />

            <b-col sm="6">
              <label :for="enabledId">Enabled:</label>
              <b-form-select
                :id="enabledId"
                :value="getValue('enabled')"
                :options="[{ item: true, name: 'Enabled' }, { item: false, name: 'Disabled' }]"
                @change="(value) => setValue({ path: 'enabled', value })"
                @blur.native="() => setTouched({path: 'enabled', touched: true})"
                value-field="item"
                text-field="name"
              ></b-form-select>
            </b-col><br />

            <b-col sm="6">
              <label :for="authoritiesId">Authorities ("CMD + Click" to select multiple):</label>
              <b-form-select
                :id="authoritiesId"
                :value="getSelectedAuthorities"
                :options="authorities"
                @change="setSelectedAuthorities"
                multiple
                :select-size="6"
              ></b-form-select>
              <div class="mt-3">Selected Authorities: <strong>{{ getSelectedAuthorities }}</strong></div>
            </b-col>

            <b-col sm="6">
              <label :for="passwordId">Password:</label>
              <b-form-input
                :id="passwordId"
                type="password"
                @blur="() => setTouched({path: 'password', touched: true})"
                :value="getValue('password')"
                @update="(value) => setValue({ path: 'password', value })"
                :state="showError('password')"
              />
              <b-form-invalid-feedback>{{ getError('password') }}</b-form-invalid-feedback>
            </b-col><br />

            <b-col sm="6">
              <label :for="confirmPasswordId">Confirm Password:</label>
              <b-form-input
                :id="confirmPasswordId"
                type="password"
                @blur="() => setTouched({path: 'confirmPassword', touched: true})"
                :value="getValue('confirmPassword')"
                @update="(value) => setValue({ path: 'confirmPassword', value })"
                :state="showError('confirmPassword')"
              />
              <b-form-invalid-feedback>{{ getError('confirmPassword') }}</b-form-invalid-feedback>
            </b-col><br />

            <br />
          </b-card>
        </div>
        <br />
        <div>
          <b-button v-if="showSubmit" type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">Save</b-button>
          <b-button v-if="formDirty" type="reset" variant="danger" class="mb-2 mr-sm-2 mb-sm-0">Reset</b-button>
        </div>
        <hr /><br />
      </b-form>
    </div>
    <div v-else>
      <b-spinner/>
    </div>
  </div>
</template>

<script>
import { mapActions, mapGetters, mapMutations } from 'vuex';
import genId from '@/components/idGenerator';
// import ShowHidePassword from '@/components/ShowHidePassword.vue';

export default {

  props: ['id'],

  components: {
    // ShowHidePassword,
  },

  data() {
    return {
      userNameId: '',
      displayNameId: '',
      enabledId: '',
      authoritiesId: '',
      passwordId: '',
      confirmPasswordId: '',
      bc: [
        {
          text: 'Crowbar',
          to: { name: 'Home' },
        },
        {
          text: 'User',
          to: { name: 'User' },
        },
        {
          text: 'Edit',
          active: true,
        },
      ],
    };
  },

  beforeMount() {
    this.userNameId = genId();
    this.displayNameId = genId();
    this.enabledId = genId();
    this.authoritiesId = genId();
    this.passwordId = genId();
    this.confirmPasswordId = genId();
  },

  computed: {
    ...mapGetters('userAdminForm',
      [
        'getValue',
        'formDirty',
        'getError',
        'isTouched',
        'formHasUntouchedErrors',
      ]),
    ...mapGetters('userAdmin',
      [
        'page',
        'totalPages',
        'totalItems',
        'itemsPerPage',
        'loading',
        'users',
        'fetchedUser',
        'authorities',
        'fetchedAuthorities',
      ]),
    isEdit() {
      return this.id || this.id === 0;
    },
    ready() {
      return (!this.isEdit || this.fetchedUser) && this.fetchedAuthorities;
    },
    getSelectedAuthorities() {
      const authorities = this.getValue('authorities');
      if (!authorities) {
        return [];
      }
      return authorities.map((x) => x.value);
    },
    showError() {
      return (path) => ((!this.isTouched(path) && this.getError(path)) ? false : null);
    },
    showSubmit() {
      return this.formDirty && !this.formHasUntouchedErrors;
    },
  },

  methods: {
    ...mapMutations('userAdminForm',
      [
        'initialize',
        'setValue',
        'setTouched',
        'setError',
        'deleteFromArray',
        'addToArray',
      ]),
    ...mapActions('userAdmin', ['loadUser', 'loadAuthorities', 'saveUser']),
    ...mapActions('userAdminForm', ['submit', 'reset']),
    setSelectedAuthorities(values) {
      const existingSize = this.getValue('authorities').length;
      for (let i = 0; i < existingSize; i += 1) {
        this.deleteFromArray('authorities[0]');
      }
      for (let k = 0; k < values.length; k += 1) {
        this.addToArray({ path: 'authorities', value: values[k] });
      }
    },
    save() {
      this.submit().then((user) => this.saveUser({ user, id: this.id }));
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
      this.loadUser(this.id)
        .then(this.initialize);
    } else {
      this.initialize();
    }
  },

};
</script>
