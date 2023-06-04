<template>
  <div class="m-2">

    <b-card title="Tokens" border-variant="dark" bg-variant="light">
      <b-card-sub-title>
        <p>
          Tokens are used to authenticate your client application to the GeoSamples API. You can generate as many tokens as you like. Each token has an alias that you can use to identify it. You can delete tokens at any time. If you delete a token, you will need to generate a new one to continue using the API.
        </p>
      </b-card-sub-title>
      <b-card-text>
        <b-list-group>
          <b-list-group-item v-for="value in tokenAliases" :key="value" class="list-item">
            <span class="mr-2">{{value}}</span>
            <b-button class="text-danger" variant="text" @click="showDeleteModal(value)">
              <b-icon icon="trash" class="mr-2" />Remove
            </b-button>
          </b-list-group-item>
          <b-list-group-item class="list-item">
            <b-button variant="text" class="text-primary" @click="showModal">
              <b-icon icon="plus" class="mr-2" />Add Token
            </b-button>
          </b-list-group-item>
        </b-list-group>
      </b-card-text>
    </b-card>
    <b-modal ref="delete-token" :title="`Delete Token - ${aliasToDelete}`">
      <p>Are you sure you want to delete this token?</p>
      <template #modal-footer>
        <b-button variant="danger" class="mb-2 mr-sm-2 mb-sm-0 mr-3" @click="doDelete(aliasToDelete)">
          <b-icon icon="trash" class="mr-2" />Delete
        </b-button>
        <b-button variant="secondary" class="mb-2 mr-sm-2 mb-sm-0 mr-3" @click="hideDeleteModal">
          <b-icon icon="x" class="mr-2" />Cancel
        </b-button>
      </template>
    </b-modal>
    <b-modal ref="create-token" hide-footer title="Create Token" size="lg">
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
          <b-button v-if="showSubmit" type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">
            <b-icon icon="plus" class="mr-2"/>Generate Token
          </b-button>
        </div>
      </b-form>
      <b-alert class="mt-3" v-if="lastUserToken" show>
        <p>
          {{ lastUserToken }}
          <b-button variant="text" class="text-secondary" @click="copyToken">
            <b-icon icon="clipboard"/>
          </b-button>
        </p>
      </b-alert>
    </b-modal>
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
      aliasToDelete: '',
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
    showDeleteModal(alias) {
      this.aliasToDelete = alias;
      this.$refs['delete-token'].show();
    },
    hideDeleteModal() {
      this.$refs['delete-token'].hide();
      this.aliasToDelete = null;
    },
    copyToken() {
      navigator.clipboard.writeText(this.lastUserToken);
    },
    showModal() {
      this.$refs['create-token'].show();
    },
    makeSuccessToast(message) {
      this.$bvToast.hide();
      this.$bvToast.toast(message, {
        title: 'Success',
        variant: 'success',
        solid: true,
        toaster: 'b-toaster-bottom-center',
        autoHideDelay: 10000,
      });
    },
    saveForm() {
      this.submit().then(
        (form) => this.generateToken(form).then(
          () => {
            this.makeSuccessToast('Token generated successfully. Copy this token to your clipboard and store it in a safe place. You will not be able to retrieve it again.');
          },
        ),
      );
    },
    doDelete(alias) {
      this.deleteToken({ alias }).then(
        () => {
          this.makeSuccessToast('Token deleted successfully.');
          this.hideDeleteModal();
          this.aliasToDelete = null;
        },
      );
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

<style scoped>
.list-item {
  background: none;
  border: none;
}
</style>
