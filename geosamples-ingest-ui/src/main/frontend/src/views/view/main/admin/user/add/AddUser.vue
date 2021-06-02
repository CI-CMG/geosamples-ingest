<template>
  <div class="m-2">
    <b-breadcrumb :items="bc"/>

    <div v-if="fetchedAuthorities">
      <b-form @submit.prevent="submit" @reset.prevent="reset"><br />

        <hr />
        <br />

        <div>
          <b-card>

            <b-col sm="6">
              <label for="input-user-name">User Name:</label>
              <b-form-input
                id="input-user-name"
                type="text"
                @blur="() => setTouched({path: 'userName', touched: true})"
                :value="getValue('userName')"
                @update="(value) => setValue({ path: 'userName', value })"
                :state="showError('userName')"
              />
              <b-form-invalid-feedback>{{ getError('userName') }}</b-form-invalid-feedback>
            </b-col><br />

            <b-col sm="6">
              <label for="input-display-name">Display Name:</label>
              <b-form-input
                id="input-display-name"
                type="text"
                @blur="() => setTouched({path: 'displayName', touched: true})"
                :value="getValue('displayName')"
                @update="(value) => setValue({ path: 'displayName', value })"
                :state="showError('displayName')"
              />
              <b-form-invalid-feedback>{{ getError('displayName') }}</b-form-invalid-feedback>
            </b-col><br />

            <b-col sm="6">
              <label for="input-enabled">Enabled:</label>
              <b-form-select
                :value="getValue('enabled')"
                :options="[{ item: true, name: 'Enabled' }, { item: false, name: 'Disabled' }]"
                @change="(value) => setValue({ path: 'enabled', value })"
                @blur.native="() => setTouched({path: 'enabled', touched: true})"
                value-field="item"
                text-field="name"
              ></b-form-select>
            </b-col><br />

            <b-col sm="6">
              <label for="input-authorities">Authorities ("CMD + Click" to select multiple):</label>
              <b-form-select
                :value="getSelectedAuthorities"
                :options="authorities"
                @change="setSelectedAuthorities"
                multiple
                :select-size="6"
              ></b-form-select>
              <div class="mt-3">selectedAuthorities: <strong>{{ getSelectedAuthorities }}</strong></div>
            </b-col>
            <!--                @blur.native="arraySetTouched"-->

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
// import { mapActions, mapGetters, mapMutations } from 'vuex';
import { mapActions, mapGetters, mapMutations } from 'vuex';

export default {

  props: ['id'],

  components: {

  },

  data() {
    return {
      bc: [
        {
          text: 'Gazetteer',
          to: { name: 'view' },
        },
        {
          text: 'Admin',
          to: { name: 'admin' },
        },
        {
          text: 'User',
          to: { name: 'user-list' },
        },
        {
          text: 'Edit',
          active: true,
        },
      ],
    };
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
    ...mapActions('userAdmin', ['loadUser', 'loadAuthorities']),
    ...mapActions('userAdminForm', ['submit', 'reset']),
    setSelectedAuthorities(values) {
      const existingSize = this.getValue('authorities').length;
      for (let i = 0; i < existingSize; i += 1) {
        this.deleteFromArray('authorities[0]');
      }
      for (let i = 0; i < values.length; i += 1) {
        this.addToArray({ path: 'authorities', value: values[i] });
      }
    },
  },

  watch: {
    // id(oldId, newId) {
    //   if (newId != null) {
    //     this.load(newId).then(this.initialize);
    //   } else {
    //     this.initialize();
    //   }
    // },
  },

  created() {
    if (this.id != null) {
      this.loadAuthorities();
      this.loadUser(this.id)
        .then(this.initialize);
    } else {
      this.loadAuthorities();
      this.initialize();
    }
  },

};
</script>
