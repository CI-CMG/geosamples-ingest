<template>
  <div class="m-2">

<!--    <b-breadcrumb :items="items"/>-->

    <div v-if="ready">

      <h1 v-if="isEdit" class="text-primary">Edit RockMineral - {{ getValue('rockMineral') }}</h1>
      <h1 v-else class="text-primary">Add New RockMineral</h1>

      <b-button v-if="isEdit" type="button" variant="danger" @click="doDelete" >Delete</b-button>

      <b-form @submit.prevent="saveForm" @reset.prevent="reset">

        <b-form-group v-if="!isEdit" label="RockMineral" :label-for="rockMineralId">
          <b-form-input
            :id="rockMineralId"
            type="text" @blur="() => setTouched({path: 'rockMineral', touched: true})"
            :value="getValue('rockMineral')"
            @update="(value) => setValue({ path: 'rockMineral', value })"
            :state="showError('rockMineral')"
          />
          <b-form-invalid-feedback>{{ getError('rockMineral') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="RockMineral Code" :label-for="rockMineralCodeId">
          <b-form-input
            :id="rockMineralCodeId"
            type="text" @blur="() => setTouched({path: 'rockMineralCode', touched: true})"
            :value="getValue('rockMineralCode')"
            @update="(value) => setValue({ path: 'rockMineralCode', value })"
            :state="showError('rockMineralCode')"
          />
          <b-form-invalid-feedback>{{ getError('rockMineralCode') }}</b-form-invalid-feedback>
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
      rockMineralId: '',
      rockMineralCodeId: '',
      sourceUriId: '',
    };
  },
  beforeMount() {
    this.rockMineralId = genId();
    this.rockMineralCodeId = genId();
    this.sourceUriId = genId();
  },
  methods: {
    ...mapMutations('rockMineralForm',
      [
        'initialize',
        'setValue',
        'setTouched',
        'setError',
        'deleteFromArray',
        'addToArray',
      ]),
    ...mapActions('rockMineral', ['load', 'save', 'delete']),
    ...mapActions('rockMineralForm', ['submit', 'reset']),

    saveForm() {
      this.submit()
        .then((provider) => this.save({ provider, id: this.id }))
        .then(() => this.$router.push({ name: 'RockMineralList' }));
    },
    doDelete() {
      this.delete(this.id).then(() => this.$router.push({ name: 'RockMineralList' }));
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
