<template>
  <div class="m-2">

<!--    <b-breadcrumb :items="items"/>-->

    <div v-if="ready">

      <h1 v-if="isEdit" class="text-primary">Edit Sample Link - {{ getValue('sampleLink') }}</h1>
      <h1 v-else class="text-primary">Add New Sample Link</h1>

      <b-button v-if="isEdit" type="button" variant="danger" @click="showModal" >Delete</b-button>
      <b-modal ref="delete-modal" title="Delete Rock Mineralogy" ok-variant="danger" ok-title="Delete" @ok="doDelete">
        <p class="my-4">Are you sure you want to delete this Sample Link?</p>
      </b-modal>

      <b-form @submit.prevent="saveForm" @reset.prevent="reset">

        <b-form-group v-if="!isEdit" label="IMLGS" :label-for="imlgsId">
          <b-form-input
            :id="imlgsId"
            type="text" @blur="() => setTouched({path: 'imlgs', touched: true})"
            :value="getValue('imlgs')"
            @update="(value) => setValue({ path: 'imlgs', value })"
            :state="showError('imlgs')"
          />
          <b-form-invalid-feedback>{{ getError('imlgs') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Data Link" :label-for="dataLinkId">
          <b-form-input
            :id="dataLinkId"
            type="text" @blur="() => setTouched({path: 'dataLink', touched: true})"
            :value="getValue('dataLink')"
            @update="(value) => setValue({ path: 'dataLink', value })"
            :state="showError('dataLink')"
          />
          <b-form-invalid-feedback>{{ getError('dataLink') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Link Level" :label-for="linkLevelId">
          <b-form-input
            :id="linkLevelId"
            type="text" @blur="() => setTouched({path: 'linkLevel', touched: true})"
            :value="getValue('linkLevel')"
            @update="(value) => setValue({ path: 'linkLevel', value })"
            :state="showError('linkLevel')"
          />
          <b-form-invalid-feedback>{{ getError('linkLevel') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Link Source" :label-for="linkSourceId">
          <b-form-input
            :id="linkSourceId"
            type="text" @blur="() => setTouched({path: 'linkSource', touched: true})"
            :value="getValue('linkSource')"
            @update="(value) => setValue({ path: 'linkSource', value })"
            :state="showError('linkSource')"
          />
          <b-form-invalid-feedback>{{ getError('linkSource') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Link Type" :label-for="linkTypeId">
          <b-form-input
            :id="linkTypeId"
            type="text" @blur="() => setTouched({path: 'linkType', touched: true})"
            :value="getValue('linkType')"
            @update="(value) => setValue({ path: 'linkType', value })"
            :state="showError('linkType')"
          />
          <b-form-invalid-feedback>{{ getError('linkType') }}</b-form-invalid-feedback>
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
      imlgsId: '',
      dataLinkId: '',
      linkLevelId: '',
      linkSourceId: '',
      linkTypeId: '',
    };
  },
  beforeMount() {
    this.imlgsId = genId();
    this.dataLinkId = genId();
    this.linkLevelId = genId();
    this.linkSourceId = genId();
    this.linkTypeId = genId();
  },
  methods: {
    ...mapMutations('sampleLinkForm',
      [
        'initialize',
        'setValue',
        'setTouched',
        'setError',
        'deleteFromArray',
        'addToArray',
      ]),
    ...mapActions('sampleLink', ['load', 'save', 'delete']),
    ...mapActions('sampleLinkForm', ['submit', 'reset']),
    showModal() {
      this.$refs['delete-modal'].show();
    },
    hideModal() {
      this.$refs['delete-modal'].hide();
    },
    saveForm() {
      this.submit()
        .then((provider) => this.save({ provider, id: this.id }))
        .then(() => this.$router.push({ name: 'SampleLinkList' }));
    },
    doDelete() {
      this.delete(this.id).then(() => this.$router.push({ name: 'SampleLinkList' }));
    },
  },

  computed: {
    ...mapState('rockMineral', ['deleting', 'loading', 'saving']),
    ...mapGetters('rockMineralForm',
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
