<template>
  <div class="m-2">

<!--    <b-breadcrumb :items="items"/>-->

    <div v-if="ready">

      <h1 v-if="isEdit" class="text-primary">Edit Facility - {{ getValue('facilityCode') }} : {{ getValue('facility') }}</h1>
      <h1 v-else class="text-primary">Add New Facility</h1>

      <b-button v-if="isEdit" type="button" variant="danger" @click="showModal" >Delete</b-button>
      <b-modal ref="delete-modal" title="Delete Facility" ok-variant="danger" ok-title="Delete" @ok="doDelete">
        <p class="my-4">Are you sure you want to delete this facility?</p>
      </b-modal>

      <b-form @submit.prevent="saveForm" @reset.prevent="reset">

        <b-form-group v-if="!isEdit" label="Facility Code" :label-for="facilityCodeId">
          <b-form-input
            :id="facilityCodeId"
            type="text" @blur="() => setTouched({path: 'facilityCode', touched: true})"
            :value="getValue('facilityCode')"
            @update="(value) => setValue({ path: 'facilityCode', value })"
            :state="showError('facilityCode')"
          />
          <b-form-invalid-feedback>{{ getError('facilityCode') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Facility" :label-for="facilityId">
          <b-form-input
            :id="facilityId"
            type="text" @blur="() => setTouched({path: 'facility', touched: true})"
            :value="getValue('facility')"
            @update="(value) => setValue({ path: 'facility', value })"
            :state="showError('facility')"
          />
          <b-form-invalid-feedback>{{ getError('facility') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Institution Code" :label-for="instCodeId">
          <b-form-input
            :id="instCodeId"
            type="text" @blur="() => setTouched({path: 'instCode', touched: true})"
            :value="getValue('instCode')"
            @update="(value) => setValue({ path: 'instCode', value })"
            :state="showError('instCode')"
          />
          <b-form-invalid-feedback>{{ getError('instCode') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Address 1" :label-for="addr1Id">
          <b-form-input
            :id="addr1Id"
            type="text" @blur="() => setTouched({path: 'addr1', touched: true})"
            :value="getValue('addr1')"
            @update="(value) => setValue({ path: 'addr1', value })"
            :state="showError('addr1')"
          />
          <b-form-invalid-feedback>{{ getError('addr1') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Address 2" :label-for="addr2Id">
          <b-form-input
            :id="addr2Id"
            type="text" @blur="() => setTouched({path: 'addr2', touched: true})"
            :value="getValue('addr2')"
            @update="(value) => setValue({ path: 'addr2', value })"
            :state="showError('addr2')"
          />
          <b-form-invalid-feedback>{{ getError('addr2') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Address 3" :label-for="addr3Id">
          <b-form-input
            :id="addr3Id"
            type="text" @blur="() => setTouched({path: 'addr3', touched: true})"
            :value="getValue('addr3')"
            @update="(value) => setValue({ path: 'addr3', value })"
            :state="showError('addr3')"
          />
          <b-form-invalid-feedback>{{ getError('addr3') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Address 4" :label-for="addr4Id">
          <b-form-input
            :id="addr4Id"
            type="text" @blur="() => setTouched({path: 'addr4', touched: true})"
            :value="getValue('addr4')"
            @update="(value) => setValue({ path: 'addr4', value })"
            :state="showError('addr4')"
          />
          <b-form-invalid-feedback>{{ getError('addr4') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Email" :label-for="emailLinkId">
          <b-form-input
            :id="emailLinkId"
            type="text" @blur="() => setTouched({path: 'emailLink', touched: true})"
            :value="getValue('emailLink')"
            @update="(value) => setValue({ path: 'emailLink', value })"
            :state="showError('emailLink')"
          />
          <b-form-invalid-feedback>{{ getError('emailLink') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="URL" :label-for="urlLinkId">
          <b-form-input
            :id="urlLinkId"
            type="text" @blur="() => setTouched({path: 'urlLink', touched: true})"
            :value="getValue('urlLink')"
            @update="(value) => setValue({ path: 'urlLink', value })"
            :state="showError('urlLink')"
          />
          <b-form-invalid-feedback>{{ getError('urlLink') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="FTP Link" :label-for="ftpLinkId">
          <b-form-input
            :id="ftpLinkId"
            type="text" @blur="() => setTouched({path: 'ftpLink', touched: true})"
            :value="getValue('ftpLink')"
            @update="(value) => setValue({ path: 'ftpLink', value })"
            :state="showError('ftpLink')"
          />
          <b-form-invalid-feedback>{{ getError('ftpLink') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="DOI Link" :label-for="doiLinkId">
          <b-form-input
            :id="doiLinkId"
            type="text" @blur="() => setTouched({path: 'doiLink', touched: true})"
            :value="getValue('doiLink')"
            @update="(value) => setValue({ path: 'doiLink', value })"
            :state="showError('doiLink')"
          />
          <b-form-invalid-feedback>{{ getError('doiLink') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Comment" :label-for="facilityCommentId">
          <b-form-input
            :id="facilityCommentId"
            type="text" @blur="() => setTouched({path: 'facilityComment', touched: true})"
            :value="getValue('facilityComment')"
            @update="(value) => setValue({ path: 'facilityComment', value })"
            :state="showError('facilityComment')"
          />
          <b-form-invalid-feedback>{{ getError('facilityComment') }}</b-form-invalid-feedback>
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
      facilityCodeId: '',
      instCodeId: '',
      facilityId: '',
      addr1Id: '',
      addr2Id: '',
      addr3Id: '',
      addr4Id: '',
      emailLinkId: '',
      urlLinkId: '',
      ftpLinkId: '',
      doiLinkId: '',
      facilityCommentId: '',
    };
  },
  beforeMount() {
    this.facilityCodeId = genId();
    this.instCodeId = genId();
    this.facilityId = genId();
    this.addr1Id = genId();
    this.addr2Id = genId();
    this.addr3Id = genId();
    this.addr4Id = genId();
    this.emailLinkId = genId();
    this.urlLinkId = genId();
    this.ftpLinkId = genId();
    this.doiLinkId = genId();
    this.facilityCommentId = genId();
  },
  methods: {
    ...mapMutations('facilityForm',
      [
        'initialize',
        'setValue',
        'setTouched',
        'setError',
        'deleteFromArray',
        'addToArray',
      ]),
    ...mapActions('facility', ['load', 'save', 'delete']),
    ...mapActions('facilityForm', ['submit', 'reset']),
    showModal() {
      this.$refs['delete-modal'].show();
    },
    hideModal() {
      this.$refs['delete-modal'].hide();
    },
    saveForm() {
      this.submit()
        .then((provider) => this.save({ provider, id: this.id }))
        .then(() => this.$router.push({ name: 'FacilityList' }));
    },
    doDelete() {
      this.delete(this.id).then(() => this.$router.push({ name: 'FacilityList' }));
    },
  },

  computed: {
    ...mapState('facility', ['deleting', 'loading', 'saving']),
    ...mapGetters('facilityForm',
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
