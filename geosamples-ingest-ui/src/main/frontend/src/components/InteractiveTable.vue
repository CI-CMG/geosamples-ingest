<template>
  <div>
    <b-breadcrumb :items="breadcrumbs"/>
    <AuthorizedContent :authorities="[readAuthority]">
      <b-form @submit.prevent="search" @reset.prevent="$store.dispatch(`${module}/reset`)">
        <b-row>
          <b-col v-for="(value, index) in fields" :key="`v${index}`">
            <b-form-group :label="value.label" :label-for="fieldIds[value.label]">
              <b-form-select
                v-if="value.options"
                :id="fieldIds[value.label]"
                :options="value.options"
                :value="value.value"
                @input="(v) => value.set(v)"
              />
              <b-form-input v-else :id="fieldIds[value.label]" :value="value.value" @input="(v) => value.set(v)"/>
            </b-form-group>
          </b-col>
        </b-row>

        <div v-if="!$store.state[module].searching" class="mb-3">
          <b-button type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">
            <b-icon icon="search" class="mr-2"/>Search
          </b-button>
          <b-button type="reset" variant="danger" class="mb-2 mr-sm-2 mb-sm-0">
            <b-icon icon="arrow-counterclockwise" class="mr-2"/>Clear
          </b-button>
          <AuthorizedContent :authorities="[createAuthority]">
            <b-button :to="{ name: createRoute }" variant="secondary" class="mb-2 mr-sm-2 mb-sm-0">
              <b-icon icon="plus" class="mr-2"/>{{ createText }}
            </b-button>
          </AuthorizedContent>
        </div>
      </b-form>
      <b-table
        sticky-header="500px"
        head-variant="dark"
        striped
        bordered
        small
        hover
        :items="$store.state[module].items"
        :fields="tableFields"
        no-local-sorting
        @sort-changed="sortChanged"
        :sort-by="sortBy"
        :sort-desc="sortDesc">
        <template :slot="`cell(${editField})`" slot-scope="data">
          <AuthorizedContent v-if="(isApproval && (data.item.approvalState !== 'APPROVED' || !isProviderTable)) || !isApproval" :authorities="[editAuthority]" :fallback-text="data.item[editField]">
            <b-link :to="{ name: editRoute, params: { id: data.item[editParameter] }}">{{ data.item[editField] }}</b-link>
          </AuthorizedContent>
          <div v-else>
            {{ data.item[editField] }}
          </div>
        </template>
        <template v-if="isApproval" slot="cell(approvalState)" slot-scope="data">
          <AuthorizedContent :authorities="[editAuthority]" :fallback-text="data.item.approvalState">
            <b-link @click="showApproval(data.item)">{{ data.item.approvalState }}</b-link>
          </AuthorizedContent>
        </template>
        <template v-for="field in tableFields.filter((f) => f.multivalued)" :slot="`cell(${field.key})`" slot-scope="data">
          {{ data.item[field.key].join(', ') }}
        </template>
      </b-table>
      <TextPagination
        :updated="(value) => $store.dispatch(`${module}/changePage`, value)"
        :page="currentPage"
        :total-items="$store.state[module].totalItems"
        items-per-page="50"
        :total-pages="$store.state[module].totalPages"
      />
    </AuthorizedContent>
    <b-modal ref="approval" :title="approvalText">
      <div v-if="loadingApproval">
        <b-spinner style="position: absolute; top: 50%; right: 50%"/>
      </div>
      <div v-else>
        <div v-if="!isProviderTable">
          <b-form-group label="Approval State">
            <b-form-select v-model="approvalState" :options="[
              { text: 'APPROVED', value: 'APPROVED' },
              { text: 'REJECTED', value: 'REJECTED' },
              { text: 'PENDING', value: 'PENDING' },
            ]"
                           :state="showError('approvalState')"
            />
            <b-form-invalid-feedback>{{ $store.getters['approvalForm/getError']('approvalState') }}</b-form-invalid-feedback>
          </b-form-group>
          <b-form-group label="Comment">
            <b-form-textarea v-model="comment" :state="showError('comment')"/>
            <b-form-invalid-feedback>{{ $store.getters['approvalForm/getError']('comment') }}</b-form-invalid-feedback>
          </b-form-group>
        </div>
        <div v-else>
          <strong>Approval State:</strong> {{ approvalState }}<br/><br/>
          <strong>Comment:</strong><br/>
          <b-textarea readonly :value="comment"/>
        </div>
      </div>
      <template slot="modal-footer">
        <b-button v-if="showSubmit && !isProviderTable" variant="primary" @click="saveApproval">
          <b-icon icon="check" class="mr-2"/>Submit
        </b-button>
        <b-button variant="secondary" @click="hideApproval">
          <b-icon icon="x" class="mr-2"/>Close
        </b-button>
        <b-button  v-if="$store.getters['approvalForm/formDirty'] && !isProviderTable" variant="danger" @click="resetApproval">
          <b-icon icon="arrow-counterclockwise" class="mr-2"/>Reset
        </b-button>
      </template>
    </b-modal>
  </div>
</template>

<script>
import genId from '@/components/idGenerator';
import TextPagination from '@/components/TextPagination.vue';
import AuthorizedContent from '@/components/AuthorizedContent.vue';

export default {
  components: { TextPagination, AuthorizedContent },

  props: [
    'module',
    'breadcrumbs',
    'readAuthority',
    'fields',
    'tableFields',
    'createRoute',
    'createText',
    'createAuthority',
    'editField',
    'editParameter',
    'editAuthority',
    'editRoute',
    'isApproval',
    'approvalText',
    'isProviderTable',
  ],

  data() {
    return {
      fieldIds: {},
      currentItem: null,
    };
  },

  beforeMount() {
    for (let i = 0; i < this.fields.length; i++) {
      const field = this.fields[i];
      this.fieldIds[field.label] = genId();
    }
    this.search();
  },

  computed: {
    currentPage: {
      get() {
        return this.$store.state[this.module].page;
      },
      set(value) {
        return this.$store.commit(`${this.module}/setPage`, value);
      },
    },

    sortBy() {
      return this.$store.state[this.module].sortBy;
    },

    sortDesc() {
      return this.$store.state[this.module].sortDesc;
    },

    loadingApproval() {
      if (!this.isApproval) {
        return false;
      }
      return this.$store.state[this.module].loadingApproval;
    },

    approvalState: {
      get() {
        if (!this.isApproval) {
          return '';
        }
        return this.$store.getters['approvalForm/getValue']('approvalState');
      },
      set(value) {
        if (this.isApproval) {
          this.$store.commit('approvalForm/setValue', { path: 'approvalState', value });
        }
      },
    },

    comment: {
      get() {
        if (!this.isApproval) {
          return '';
        }
        return this.$store.getters['approvalForm/getValue']('comment');
      },
      set(value) {
        if (this.isApproval) {
          this.$store.commit('approvalForm/setValue', { path: 'comment', value });
        }
      },
    },

    showError() {
      return (path) => ((!this.$store.getters['approvalForm/isTouched'](path) && this.$store.getters['approvalForm/getError'](path)) ? false : null);
    },

    showSubmit() {
      return this.$store.getters['approvalForm/formDirty'] && !this.$store.getters['approvalForm/formHasUntouchedErrors'];
    },
  },

  methods: {
    search() {
      this.$store.dispatch(`${this.module}/searchPage`).then(
        (response) => {
          if (response.items.length === 0) {
            this.$store.dispatch(`${this.module}/search`);
          }
        },
      );
    },

    sortChanged({ sortBy, sortDesc }) {
      if (sortBy) {
        this.$store.commit(`${this.module}/setSortBy`, sortBy);
        this.$store.commit(`${this.module}/setSortDesc`, sortDesc);
        this.search();
      }
    },

    showApproval(item) {
      this.currentItem = item;
      this.$refs.approval.show();
      this.$store.dispatch(`${this.module}/loadApproval`, item[this.editParameter]).then(
        (data) => {
          this.$store.commit('approvalForm/initialize', {
            approvalState: data.approvalState,
            comment: data.comment,
          });
        },
      );
    },

    hideApproval() {
      this.$refs.approval.hide();
      this.currentItem = null;
    },

    saveApproval() {
      this.$store.dispatch('approvalForm/submit').then(
        (approval) => {
          this.$store.dispatch(`${this.module}/saveApproval`, { id: this.currentItem.id, approval }).then(
            () => {
              this.hideApproval();
              this.$store.dispatch(`${this.module}/searchPage`);
            },
          );
        },
      );
    },

    resetApproval() {
      this.$store.commit('approvalForm/reset');
    },
  },
};
</script>
