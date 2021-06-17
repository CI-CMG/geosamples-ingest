<template>
  <b-form @submit.prevent="onSubmit" @reset.prevent="onReset">
    <b-form-group label="Username" :label-for="unId">
      <b-form-input
        :disabled="loading"
        :id="unId"
        @blur="() => setTouched({path: 'username', touched: true})"
        :value="getValue('username')"
        @update="(value) => setValue({ path: 'username', value })"
        :state="showError('username')"
      ></b-form-input>
    </b-form-group>

    <b-form-group label="Password" :label-for="pwId">
      <b-form-input
        :disabled="loading"
        :id="pwId"
        type="password"
        @blur="() => setTouched({path: 'password', touched: true})"
        :value="getValue('password')"
        @update="(value) => setValue({ path: 'password', value })"
        :state="showError('password')"
      ></b-form-input>
    </b-form-group>

    <div v-if="!loading">
      <b-button v-if="showSubmit" type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">Save</b-button>
      <b-button v-if="formDirty" type="reset" variant="danger" class="mb-2 mr-sm-2 mb-sm-0">Clear</b-button>
    </div>
  </b-form>
</template>

<script>
import {
  mapActions, mapGetters, mapMutations, mapState,
} from 'vuex';
import genId from '@/components/idGenerator';

export default {
  data() {
    return {
      unId: '',
      pwId: '',
      username: '',
      password: '',
    };
  },
  beforeMount() {
    this.unId = genId();
    this.pwId = genId();
  },
  beforeRouteEnter(to, from, next) {
    next((self) => {
      self.initialize();
    });
  },
  beforeRouteUpdate(to, from, next) {
    this.initialize();
    next();
  },
  beforeRouteLeave(to, from, next) {
    this.initialize();
    next();
  },
  computed: {
    ...mapGetters('loginForm',
      [
        'getValue',
        'formDirty',
        'getError',
        'isTouched',
        'formHasUntouchedErrors',
      ]),
    ...mapState('user', ['loading']),
    showSubmit() {
      return this.formDirty && !this.formHasUntouchedErrors;
    },
    showError() {
      return (path) => ((!this.isTouched(path) && this.getError(path)) ? false : null);
    },
  },
  methods: {
    ...mapMutations('loginForm',
      [
        'initialize',
        'setValue',
        'setTouched',
        'setError',
        'deleteFromArray',
        'addToArray',
      ]),
    ...mapActions('loginForm', ['submit', 'reset']),
    ...mapActions('user', ['login']),
    onSubmit() {
      this.submit().then(({ username, password }) => this.login({
        username, password, router: this.$router, store: this.$store,
      }));
    },
    onReset() {
      this.reset();
    },

  },
};
</script>
