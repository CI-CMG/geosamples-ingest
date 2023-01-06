<template>
  <div>
    <b-form @submit.prevent="search" @reset.prevent="reset">
      <b-container fluid>
        <b-row>
          <b-col>
            <b-form-group label="Cruise Name Contains" :label-for="cruiseNameContainsId">
              <b-form-input :id="cruiseNameContainsId" v-model="cruiseNameContains"/>
            </b-form-group>
          </b-col>
<!--          <b-col>-->
<!--            <b-form-group label="Cruise Year" :label-for="cruiseYearId">-->
<!--                <b-form-input :id="cruiseYearId" v-model="cruiseYear"/>-->
<!--            </b-form-group>-->
<!--          </b-col>-->
<!--          <b-col>-->
<!--            <b-form-group label="Platform" :label-for="platformId">-->
<!--              <b-form-input :id="platformId" v-model="platform"/>-->
<!--            </b-form-group>-->
<!--          </b-col>-->
<!--          <b-col>-->
<!--            <b-form-group label="Leg Name" :label-for="legNameId">-->
<!--              <b-form-input :id="legNameId" v-model="legName"/>-->
<!--            </b-form-group>-->
<!--          </b-col>-->
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
          <b-col>
            <b-form-group label="Link Level" :label-for="linkLevelId">
              <b-form-input :id="linkLevelId" v-model="linkLevel"/>
            </b-form-group>
          </b-col>
        </b-row>
        <b-row>
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
    <b-button :to="{ name: 'CruiseLinkAdd' }" variant="secondary" class="m-3">Add New Cruise Link</b-button>
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
      <template #cell(id)="data">
        <b-link :to="{ name: 'CruiseLinkEdit', params: { id: data.item.id }}">{{ data.item.id }}</b-link>
      </template>
    </b-table>
    <TextPagination :updated="changePage" :page="currentPage" :total-items="totalItems" items-per-page="50"/>
  </div>
</template>

<script>

import genId from '@/components/idGenerator';
import {
  mapActions, mapMutations, mapState,
} from 'vuex';
import TextPagination from '@/components/TextPagination.vue';

export default {
  components: {
    TextPagination,
  },

  beforeMount() {
    this.idId = genId();
    this.cruiseNameContainsId = genId();
    this.cruisePlatformId = genId();
    this.cruiseNameId = genId();
    this.cruiseYearId = genId();
    this.platformId = genId();
    this.legId = genId();
    this.legNameId = genId();
    this.dataLinkId = genId();
    this.publishId = genId();
    this.linkLevelId = genId();
    this.linkSourceId = genId();
    this.linkTypeId = genId();
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
    ...mapMutations('cruiseLink', [
      'setCruiseNameContains',
      'setCruisePlatform',
      'setCruiseName',
      'setCruiseYear',
      'setPlatform',
      'setLeg',
      'setLegName',
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
    ...mapActions('cruiseLink', ['search', 'reset', 'changePage']),
    sortChanged({ sortBy, sortDesc }) {
      this.setSortBy(sortBy);
      this.setSortDesc(sortDesc);
      this.search();
    },
  },

  computed: {
    ...mapState('cruiseLink', ['searching', 'page', 'totalItems', 'totalPages', 'items', 'params', 'sortDesc', 'sortBy']),
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
        this.setDataLink(value);
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
    cruisePlatform: {
      get() {
        return this.params.cruisePlatform;
      },
      set(value) {
        this.setCruisePlatform(value);
      },
    },
    cruiseNameContains: {
      get() {
        return this.params.cruiseNameContains;
      },
      set(value) {
        this.setCruiseNameContains(value);
      },
    },
    cruiseName: {
      get() {
        return this.params.cruiseName;
      },
      set(value) {
        this.setCruiseName(value);
      },
    },
    cruiseYear: {
      get() {
        return this.params.cruiseYear;
      },
      set(value) {
        this.setCruiseYear(value);
      },
    },
    platform: {
      get() {
        return this.params.platform;
      },
      set(value) {
        this.setPlatform(value);
      },
    },
    leg: {
      get() {
        return this.params.leg;
      },
      set(value) {
        this.setLeg(value);
      },
    },
    legName: {
      get() {
        return this.params.legName;
      },
      set(value) {
        this.setLegName(value);
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
      idId: null,
      cruisePlatformId: null,
      cruiseNameContainsId: null,
      cruiseNameId: null,
      cruiseYearId: null,
      platformId: null,
      legId: null,
      legNameId: null,
      dataLinkId: null,
      publishId: null,
      linkLevelId: null,
      linkSourceId: null,
      linkTypeId: null,

      fields: [
        {
          key: 'id',
          label: 'Id',
          sortable: true,
        },
        {
          key: 'cruiseName',
          label: 'Cruise Name',
          sortable: true,
        },
        {
          key: 'cruiseYear',
          label: 'Cruise Year',
          sortable: true,
        },
        {
          key: 'platform',
          label: 'Platform',
          sortable: true,
        },
        {
          key: 'legName',
          label: 'Leg Name',
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
