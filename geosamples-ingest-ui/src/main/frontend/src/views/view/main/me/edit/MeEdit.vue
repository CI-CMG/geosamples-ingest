<template>
  <div class="m-2">

      <b-card title="Tokens">
        <b-list-group>
          <b-list-group-item v-for="value in tokenAliases" :key="value">
            <span>{{value}}</span>
            <b-button type="button" variant="danger" @click="doDelete(value)" >Delete</b-button>
          </b-list-group-item>
        </b-list-group>
      </b-card>

      <b-form @submit.prevent="saveForm" @reset.prevent="reset">

        <b-form-group label="Alias" :label-for="aliasId">
          <b-form-input
            :id="aliasId"
            type="text" @blur="() => setTouched({path: 'alias', touched: true})"
            :value="getValue('alias')"
            @update="(value) => setValue({ path: 'alias', value })"
            :state="showError('alias')"
          />
          <b-form-invalid-feedback>{{ getError('alias') }}</b-form-invalid-feedback>
        </b-form-group>

        <div>
          <b-button v-if="showSubmit" type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">Generate Token</b-button>
          <b-button v-if="formDirty" type="reset" variant="danger" class="mb-2 mr-sm-2 mb-sm-0">Reset</b-button>
        </div>
      </b-form>
      <b-alert v-if="lastUserToken" show>{{lastUserToken}}</b-alert>

  </div>
</template>

<script>
import {
  mapActions, mapGetters, mapMutations, mapState,
} from 'vuex';
import genId from '@/components/idGenerator';

export default {
  data() {
    return {
      aliasId: '',
    };
  },
  beforeMount() {
    this.aliasId = genId();
  },
  methods: {
    ...mapMutations('tokenForm',
      [
        'initialize',
        'setValue',
        'setTouched',
        'setError',
        'deleteFromArray',
        'addToArray',
      ]),
    ...mapActions('userAuth', ['generateToken', 'deleteToken']),
    ...mapActions('tokenForm', ['submit', 'reset']),
    saveForm() {
      this.submit().then((form) => this.generateToken(form));
    },
    doDelete(alias) {
      this.deleteToken({ alias });
    },
  },

  computed: {
    ...mapState('userAuth', ['lastUserToken', 'user']),
    ...mapGetters('tokenForm',
      [
        'getValue',
        'formDirty',
        'getError',
        'isTouched',
        'formHasUntouchedErrors',
      ]),
    showError() {
      return (path) => ((!this.isTouched(path) && this.getError(path)) ? false : null);
    },
    showSubmit() {
      return this.formDirty && !this.formHasUntouchedErrors;
    },
    isEdit() {
      return this.id || this.id === 0;
    },
    tokenAliases() {
      return this.user.tokenAliases ? this.user.tokenAliases : [];
    },
  },
  created() {
    this.initialize();
  },
};
</script>
