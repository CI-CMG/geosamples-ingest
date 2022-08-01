<template>
  <div>
    <b-form @submit.prevent="search" @reset.prevent="reset">
      <b-container fluid>
        <b-row>
          <b-col>
            <b-form-group label="IMLGS Equals" :label-for="imlgsId">
              <b-form-input :id="imlgsId" v-model="imlgs"/>
            </b-form-group>
          </b-col>
          <b-col>
            <b-form-group label="Datalink" :label-for="dataLinkId">
              <b-form-input :id="dataLinkId" v-model="dataLink"/>
            </b-form-group>
          </b-col>
          <b-col>
            <b-form-group label="Publish" :label-for="publishId">
              <b-form-input :id="publishId" v-model="publish"/>
            </b-form-group>
          </b-col>
        </b-row>
        <b-row>
          <b-col>
            <b-form-group label="Link Level" :label-for="linkLevelId">
              <b-form-input :id="linkLevelId" v-model="linkLevel"/>
            </b-form-group>
          </b-col>
          <b-col>
            <b-form-group label="Link Source" :label-for="linkSourceId">
              <b-form-input :id="linkSourceId" v-model="linkSource"/>
            </b-form-group>
          </b-col>
          <b-col>
            <b-form-group label="Link Type" :label-for="linkTypeId">
              <b-form-input :id="linkTypeId" v-model="linkType"/>
            </b-form-group>
          </b-col>
          <b-col>
            <b-form-group label="ID" :label-for="idId">
              <b-form-input :id="idId" v-model="id"/>
            </b-form-group>
          </b-col>
        </b-row>
      </b-container>

      <div v-if="!searching">
        <b-button type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">Search</b-button>
        <b-button type="reset" variant="danger" class="mb-2 mr-sm-2 mb-sm-0">Clear</b-button>
      </div>
    </b-form>
    <b-button :to="{ name: 'SampleLinkAdd' }" variant="secondary" class="m-3">Add New Sample Link</b-button>
    <b-table
      sticky-header="500px"
      head-variant="dark"
      striped
      bordered
      small
      hover
      :items="items"
      :fields="fields"
      no-local-sorting
      @sort-changed="sortChanged"
      :sort-by="sortBy"
      :sort-desc="sortDesc">
      <template #cell(imlgs)="data">
        <b-link :to="{ name: 'SampleLinkEdit', params: { id: data.item.id }}">{{ data.item.imlgs }}</b-link>
      </template>
    </b-table>
    <b-pagination v-model="currentPage" @input="changePage" :total-rows="totalItems" per-page="50"></b-pagination>
  </div>
</template>

<script>

import genId from '@/components/idGenerator';
import {
  mapActions, mapMutations, mapState,
} from 'vuex';

export default {
  beforeMount() {
    this.imlgsId = genId();
    this.dataLinkId = genId();
    this.publishId = genId();
    this.linkLevelId = genId();
    this.linkSourceId = genId();
    this.linkTypeId = genId();
    this.idId = genId();
  },
  beforeRouteEnter(to, from, next) {
    next((self) => {
      self.search();
    });
  },
  beforeRouteUpdate(to, from, next) {
    this.search();
    next();
  },
  beforeRouteLeave(to, from, next) {
    this.clearAll();
    next();
  },
  methods: {
    ...mapMutations('sampleLink', [
      'setImlgs',
      'setDataLink',
      'setLinkLevel',
      'setLinkSource',
      'setLinkType',
      'setPublish',
      'setId',
      'clearParams',
      'firstPage',
      'setPage',
      'setSortBy',
      'setSortDesc',
      'clearAll']),
    ...mapActions('sampleLink', ['search', 'reset', 'changePage']),
    sortChanged({ sortBy, sortDesc }) {
      this.setSortBy(sortBy);
      this.setSortDesc(sortDesc);
      this.search();
    },
  },

  computed: {
    ...mapState('sampleLink', ['searching', 'page', 'totalItems', 'totalPages', 'items', 'params', 'sortDesc', 'sortBy']),
    id: {
      get() {
        return this.params.id;
      },
      set(value) {
        this.setId(value);
      },
    },
    dataLink: {
      get() {
        return this.params.dataLink;
      },
      set(value) {
        this.setdataLink(value);
      },
    },
    publish: {
      get() {
        return this.params.publish;
      },
      set(value) {
        this.setPublish(value);
      },
    },
    imlgs: {
      get() {
        return this.params.imlgs;
      },
      set(value) {
        this.setimlgs(value);
      },
    },
    linkLevel: {
      get() {
        return this.params.linkLevel;
      },
      set(value) {
        this.setLinkLevel(value);
      },
    },
    linkSource: {
      get() {
        return this.params.linkSource;
      },
      set(value) {
        this.setLinkSource(value);
      },
    },
    linkType: {
      get() {
        return this.params.linkType;
      },
      set(value) {
        this.setLinkType(value);
      },
    },
    currentPage: {
      get() {
        return this.page;
      },
      set(value) {
        this.setPage(value);
      },
    },
  },

  data() {
    return {
      imlgsId: null,
      dataLinkId: null,
      publishId: null,
      linkLevelId: null,
      linkSourceId: null,
      linkTypeId: null,
      idId: null,

      fields: [
        {
          key: 'imlgs',
          label: 'IMLGS',
          sortable: true,
        },
        {
          key: 'dataLink',
          label: 'Data Link',
          sortable: true,
        },
        {
          key: 'publish',
          label: 'Publish',
          sortable: true,
        },
        {
          key: 'linkLevel',
          label: 'Link Level',
          sortable: true,
        },
        {
          key: 'linkSource',
          label: 'Link Source',
          sortable: true,
        },
        {
          key: 'linkType',
          label: 'Link Type',
          sortable: true,
        },
      ],
    };
  },
};
</script>
