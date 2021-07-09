<template>
  <div class="m-2">

<!--    <b-breadcrumb :items="items"/>-->

    <div v-if="ready">

      <h1 v-if="isEdit" class="text-primary">Edit Lithology - {{ getValue('lithology') }}</h1>
      <h1 v-else class="text-primary">Add New Lithology</h1>

      <b-button v-if="isEdit" type="button" variant="danger" @click="doDelete" >Delete</b-button>

      <b-form @submit.prevent="saveForm" @reset.prevent="reset">

        <b-form-group v-if="!isEdit" label="Lithology" :label-for="lithologyId">
          <b-form-input
            :id="lithologyId"
            type="text" @blur="() => setTouched({path: 'lithology', touched: true})"
            :value="getValue('lithology')"
            @update="(value) => setValue({ path: 'lithology', value })"
            :state="showError('lithology')"
          />
          <b-form-invalid-feedback>{{ getError('lithology') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Lithology Code" :label-for="lithologyCodeId">
          <b-form-input
            :id="lithologyCodeId"
            type="text" @blur="() => setTouched({path: 'lithologyCode', touched: true})"
            :value="getValue('lithologyCode')"
            @update="(value) => setValue({ path: 'lithologyCode', value })"
            :state="showError('lithologyCode')"
          />
          <b-form-invalid-feedback>{{ getError('lithologyCode') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group v-if="!isEdit" label="Old Lithology" :label-for="oldLithologyId">
          <b-form-input
            :id="oldLithologyId"
            type="text" @blur="() => setTouched({path: 'oldLithology', touched: true})"
            :value="getValue('oldLithology')"
            @update="(value) => setValue({ path: 'oldLithology', value })"
            :state="showError('oldLithology')"
          />
          <b-form-invalid-feedback>{{ getError('oldLithology') }}</b-form-invalid-feedback>
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
      lithologyId: '',
      lithologyCodeId: '',
      sourceUriId: '',
      oldLithologyId: '',
    };
  },
  beforeMount() {
    this.lithologyId = genId();
    this.lithologyCodeId = genId();
    this.oldLithologyId = genId();
    this.sourceUriId = genId();
  },
  methods: {
    ...mapMutations('lithologyForm',
      [
        'initialize',
        'setValue',
        'setTouched',
        'setError',
        'deleteFromArray',
        'addToArray',
      ]),
    ...mapActions('lithology', ['load', 'save', 'delete']),
    ...mapActions('lithologyForm', ['submit', 'reset']),

    saveForm() {
      this.submit()
        .then((provider) => this.save({ provider, id: this.id }))
        .then(() => this.$router.push({ name: 'LithologyList' }));
    },
    doDelete() {
      this.delete(this.id).then(() => this.$router.push({ name: 'LithologyList' }));
    },
  },

  computed: {
    ...mapState('lithology', ['deleting', 'loading', 'saving']),
    ...mapGetters('lithologyForm',
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
