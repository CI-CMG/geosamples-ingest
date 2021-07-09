<template>
  <div class="m-2">

<!--    <b-breadcrumb :items="items"/>-->

    <div v-if="ready">

      <h1 v-if="isEdit" class="text-primary">Edit StorageMethod - {{ getValue('storageMethod') }}</h1>
      <h1 v-else class="text-primary">Add New StorageMethod</h1>

      <b-button v-if="isEdit" type="button" variant="danger" @click="doDelete" >Delete</b-button>

      <b-form @submit.prevent="saveForm" @reset.prevent="reset">

        <b-form-group v-if="!isEdit" label="StorageMethod" :label-for="storageMethodId">
          <b-form-input
            :id="storageMethodId"
            type="text" @blur="() => setTouched({path: 'storageMethod', touched: true})"
            :value="getValue('storageMethod')"
            @update="(value) => setValue({ path: 'storageMethod', value })"
            :state="showError('storageMethod')"
          />
          <b-form-invalid-feedback>{{ getError('storageMethod') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="StorageMethod Code" :label-for="storageMethodCodeId">
          <b-form-input
            :id="storageMethodCodeId"
            type="text" @blur="() => setTouched({path: 'storageMethodCode', touched: true})"
            :value="getValue('storageMethodCode')"
            @update="(value) => setValue({ path: 'storageMethodCode', value })"
            :state="showError('storageMethodCode')"
          />
          <b-form-invalid-feedback>{{ getError('storageMethodCode') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Source URI" :label-for="sourceUriId">
          <b-form-input
            :id="sourceUriId"
            type="text" @blur="() => setTouched({path: 'sourceUri', touched: true})"
            :value="getValue('sourceUri')"
            @update="(value) => setValue({ path: 'sourceUri', value })"
            :state="showError('sourceUri')"
          />
          <b-form-invalid-feedback>{{ getError('sourceUri') }}</b-form-invalid-feedback>
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
      storageMethodId: '',
      storageMethodCodeId: '',
      sourceUriId: '',
    };
  },
  beforeMount() {
    this.storageMethodId = genId();
    this.storageMethodCodeId = genId();
    this.sourceUriId = genId();
  },
  methods: {
    ...mapMutations('storageMethodForm',
      [
        'initialize',
        'setValue',
        'setTouched',
        'setError',
        'deleteFromArray',
        'addToArray',
      ]),
    ...mapActions('storageMethod', ['load', 'save', 'delete']),
    ...mapActions('storageMethodForm', ['submit', 'reset']),

    saveForm() {
      this.submit()
        .then((provider) => this.save({ provider, id: this.id }))
        .then(() => this.$router.push({ name: 'StorageMethodList' }));
    },
    doDelete() {
      this.delete(this.id).then(() => this.$router.push({ name: 'StorageMethodList' }));
    },
  },

  computed: {
    ...mapState('storageMethod', ['deleting', 'loading', 'saving']),
    ...mapGetters('storageMethodForm',
      [
        'getValue',
        'formDirty',
        'getError',
        'isTouched',
        'formHasUntouchedErrors',
      ]),
    ready() {
      return !this.isEdit || !this.loading;
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
    if (this.id != null) {
      this.load(this.id).then(this.initialize);
    } else {
      this.initialize();
    }
  },

};
</script>
