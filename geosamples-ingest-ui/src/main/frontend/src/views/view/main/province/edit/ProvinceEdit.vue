<template>
  <div class="m-2">

<!--    <b-breadcrumb :items="items"/>-->

    <div v-if="ready">

      <h1 v-if="isEdit" class="text-primary">Edit Province - {{ getValue('province') }}</h1>
      <h1 v-else class="text-primary">Add New Province</h1>

      <b-button v-if="isEdit" type="button" variant="danger" @click="doDelete" >Delete</b-button>

      <b-form @submit.prevent="saveForm" @reset.prevent="reset">

        <b-form-group v-if="!isEdit" label="Province" :label-for="provinceId">
          <b-form-input
            :id="provinceId"
            type="text" @blur="() => setTouched({path: 'province', touched: true})"
            :value="getValue('province')"
            @update="(value) => setValue({ path: 'province', value })"
            :state="showError('province')"
          />
          <b-form-invalid-feedback>{{ getError('province') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Province Code" :label-for="provinceCodeId">
          <b-form-input
            :id="provinceCodeId"
            type="text" @blur="() => setTouched({path: 'provinceCode', touched: true})"
            :value="getValue('provinceCode')"
            @update="(value) => setValue({ path: 'provinceCode', value })"
            :state="showError('provinceCode')"
          />
          <b-form-invalid-feedback>{{ getError('provinceCode') }}</b-form-invalid-feedback>
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

        <b-form-group label="Comment" :label-for="provinceCommentId">
          <b-form-input
            :id="provinceCommentId"
            type="text" @blur="() => setTouched({path: 'provinceComment', touched: true})"
            :value="getValue('provinceComment')"
            @update="(value) => setValue({ path: 'provinceComment', value })"
            :state="showError('provinceComment')"
          />
          <b-form-invalid-feedback>{{ getError('provinceComment') }}</b-form-invalid-feedback>
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
      provinceId: '',
      provinceCodeId: '',
      sourceUriId: '',
      provinceCommentId: '',
    };
  },
  beforeMount() {
    this.provinceId = genId();
    this.provinceCodeId = genId();
    this.sourceUriId = genId();
    this.provinceCommentId = genId();
  },
  methods: {
    ...mapMutations('provinceForm',
      [
        'initialize',
        'setValue',
        'setTouched',
        'setError',
        'deleteFromArray',
        'addToArray',
      ]),
    ...mapActions('province', ['load', 'save', 'delete']),
    ...mapActions('provinceForm', ['submit', 'reset']),

    saveForm() {
      this.submit()
        .then((provider) => this.save({ provider, id: this.id }))
        .then(() => this.$router.push({ name: 'ProvinceList' }));
    },
    doDelete() {
      this.delete(this.id).then(() => this.$router.push({ name: 'ProvinceList' }));
    },
  },

  computed: {
    ...mapState('province', ['deleting', 'loading', 'saving']),
    ...mapGetters('provinceForm',
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
