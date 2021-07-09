<template>
  <div class="m-2">

<!--    <b-breadcrumb :items="items"/>-->

    <div v-if="ready">

      <h1 v-if="isEdit" class="text-primary">Edit Texture - {{ getValue('texture') }}</h1>
      <h1 v-else class="text-primary">Add New Texture</h1>

      <b-button v-if="isEdit" type="button" variant="danger" @click="doDelete" >Delete</b-button>

      <b-form @submit.prevent="saveForm" @reset.prevent="reset">

        <b-form-group v-if="!isEdit" label="Texture" :label-for="textureId">
          <b-form-input
            :id="textureId"
            type="text" @blur="() => setTouched({path: 'texture', touched: true})"
            :value="getValue('texture')"
            @update="(value) => setValue({ path: 'texture', value })"
            :state="showError('texture')"
          />
          <b-form-invalid-feedback>{{ getError('texture') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Texture Code" :label-for="textureCodeId">
          <b-form-input
            :id="textureCodeId"
            type="text" @blur="() => setTouched({path: 'textureCode', touched: true})"
            :value="getValue('textureCode')"
            @update="(value) => setValue({ path: 'textureCode', value })"
            :state="showError('textureCode')"
          />
          <b-form-invalid-feedback>{{ getError('textureCode') }}</b-form-invalid-feedback>
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
      textureId: '',
      textureCodeId: '',
      sourceUriId: '',
    };
  },
  beforeMount() {
    this.textureId = genId();
    this.textureCodeId = genId();
    this.sourceUriId = genId();
  },
  methods: {
    ...mapMutations('textureForm',
      [
        'initialize',
        'setValue',
        'setTouched',
        'setError',
        'deleteFromArray',
        'addToArray',
      ]),
    ...mapActions('texture', ['load', 'save', 'delete']),
    ...mapActions('textureForm', ['submit', 'reset']),

    saveForm() {
      this.submit()
        .then((provider) => this.save({ provider, id: this.id }))
        .then(() => this.$router.push({ name: 'TextureList' }));
    },
    doDelete() {
      this.delete(this.id).then(() => this.$router.push({ name: 'TextureList' }));
    },
  },

  computed: {
    ...mapState('texture', ['deleting', 'loading', 'saving']),
    ...mapGetters('textureForm',
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
