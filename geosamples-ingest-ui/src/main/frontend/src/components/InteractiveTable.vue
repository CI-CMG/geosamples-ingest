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
          <AuthorizedContent :authorities="[editAuthority]" :fallback-text="data.item[editField]">
            <b-link :to="{ name: editRoute, params: { id: data.item[editParameter] }}">{{ data.item[editField] }}</b-link>
          </AuthorizedContent>
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
  ],

  data() {
    return {
      fieldIds: {},
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
      this.$store.commit(`${this.module}/setSortBy`, sortBy);
      this.$store.commit(`${this.module}/setSortDesc`, sortDesc);
      this.search();
    },
  },
};
</script>
