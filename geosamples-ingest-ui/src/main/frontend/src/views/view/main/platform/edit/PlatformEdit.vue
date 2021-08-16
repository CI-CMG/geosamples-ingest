private String sourceUri;

<template>
  <div class="m-2">

<!--    <b-breadcrumb :items="items"/>-->

    <div v-if="ready">

      <h1 v-if="isEdit" class="text-primary">Edit Ship/Platform - {{ getValue('platform') }}</h1>
      <h1 v-else class="text-primary">Add New Ship/Platform</h1>

      <b-button v-if="isEdit" type="button" variant="danger" @click="doDelete" >Delete</b-button>

      <b-form @submit.prevent="saveForm" @reset.prevent="reset">

        <b-form-group v-if="!isEdit" label="Ship/Platform" :label-for="platformId">
          <b-form-input
            :id="platformId"
            type="text" @blur="() => setTouched({path: 'platform', touched: true})"
            :value="getValue('platform')"
            @update="(value) => setValue({ path: 'platform', value })"
            :state="showError('platform')"
          />
          <b-form-invalid-feedback>{{ getError('platform') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Master ID" :label-for="masterIdId">
          <b-form-input
            :id="masterIdId"
            type="text" @blur="() => setTouched({path: 'masterId', touched: true})"
            :value="getValue('masterId')"
            @update="(value) => setValue({ path: 'masterId', value })"
            :state="showError('masterId')"
          />
          <b-form-invalid-feedback>{{ getError('masterId') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Prefix" :label-for="prefixId">
          <b-form-input
            :id="prefixId"
            type="text" @blur="() => setTouched({path: 'prefix', touched: true})"
            :value="getValue('prefix')"
            @update="(value) => setValue({ path: 'prefix', value })"
            :state="showError('prefix')"
          />
          <b-form-invalid-feedback>{{ getError('prefix') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="ICES Code" :label-for="icesCodeId">
          <b-form-input
            :id="icesCodeId"
            type="text" @blur="() => setTouched({path: 'icesCode', touched: true})"
            :value="getValue('icesCode')"
            @update="(value) => setValue({ path: 'icesCode', value })"
            :state="showError('icesCode')"
          />
          <b-form-invalid-feedback>{{ getError('icesCode') }}</b-form-invalid-feedback>
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
      platformId: '',
      masterIdId: '',
      prefixId: '',
      icesCodeId: '',
      sourceUriId: '',
    };
  },
  beforeMount() {
    this.platformId = genId();
    this.masterIdId = genId();
    this.prefixId = genId();
    this.icesCodeId = genId();
    this.sourceUriId = genId();
  },
  methods: {
    ...mapMutations('platformForm',
      [
        'initialize',
        'setValue',
        'setTouched',
        'setError',
        'deleteFromArray',
        'addToArray',
      ]),
    ...mapActions('platform', ['load', 'save', 'delete']),
    ...mapActions('platformForm', ['submit', 'reset']),

    saveForm() {
      this.submit()
        .then((provider) => this.save({ provider, id: this.id }))
        .then(() => this.$router.push({ name: 'PlatformList' }));
    },
    doDelete() {
      this.delete(this.id).then(() => this.$router.push({ name: 'PlatformList' }));
    },
  },

  computed: {
    ...mapState('platform', ['deleting', 'loading', 'saving']),
    ...mapGetters('platformForm',
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
