<template>
<div>
  <b-breadcrumb :items="[
    { text: 'Geosamples Ingest', to: { name: 'Home' } },
    { text: 'Sample + Interval', active: true },
  ]"/>
  <b-modal ref="delete-modal" title="Delete Intervals" ok-variant="danger" ok-title="Delete" @ok="doDelete">
    <p class="my-4">Are you sure you want to delete the selected intervals?</p>
  </b-modal>
  <IntervalSearchControl :onSearch="routeSearch" :onSort="routeSort" />
  <b-card title="Results">
    <div v-if="searching"><b-spinner/></div>
    <div v-else>
      <b-pagination :value="page" @change="changePage" :total-rows="totalItems" :per-page="itemsPerPage"></b-pagination>
      <b-card>
      <div class="mb-3">
        <b-button variant="secondary" class="mb-2 mr-sm-2 mb-sm-0 mr-3" @click="() => selectAll(true)">Select All</b-button>
        <b-button variant="secondary" class="mb-2 mr-sm-2 mb-sm-0 mr-3" @click="() => selectAll(false)">Deselect All</b-button>
        <b-button variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3" @click="() => doAccept(true)">Publish Selected</b-button>
        <b-button variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3" @click="() => doAccept(false)">Unpublish Selected</b-button>
        <b-button variant="danger" class="mb-2 mr-sm-2 mb-sm-0 mr-3" @click="showModal">Delete Selected Intervals</b-button>
      </div>
      </b-card>
      <div class="geo-temp-samples-table">
        <IntervalListTable
          :items="items"
          :fields="fields"
          :toggleSelect="toggleSelect"
          :sortedColumns="sortParameters.selected"
        />
      </div>
    </div>
    <TextPagination :updated="changePage" :page="page" :total-items="totalItems" :items-per-page="itemsPerPage" :total-pages="totalPages"/>
  </b-card>

</div>
</template>

<script>
import {
  mapActions, mapMutations, mapState,
} from 'vuex';
import TextPagination from '@/components/TextPagination.vue';
import IntervalListTable from './IntervalListTable.vue';
import IntervalSearchControl from './IntervalSearchControl.vue';

const route = (self, to) => {
  self.updateSearchParameters(to.query);
  self.updateSortParameters(to.query);
  self.setPage(to.query.page);
  self.setSort(to.query.sort);
  self.searchPage();
};

export default {
  components: {
    IntervalListTable,
    IntervalSearchControl,
    TextPagination,
  },

  beforeRouteUpdate(to, from, next) {
    // noinspection JSDeepBugsSwappedArgs
    route(this, to);
    next();
  },

  created() {
    if (this.items) {
      if (this.items.length === 0) {
        this.searchPage();
      }
    } else {
      this.searchPage();
    }
  },

  methods: {
    ...mapMutations('interval', ['updateSortParameters', 'firstPage', 'setPage', 'setSortBy', 'setSortDesc', 'clearAll', 'setSort', 'updateSearchParameters', 'updateSortParameters']),
    ...mapActions('interval', ['searchPage', 'accept', 'delete']),
    showModal() {
      this.$refs['delete-modal'].show();
    },
    hideModal() {
      this.$refs['delete-modal'].hide();
    },
    changePage(page) {
      this.setPage(page);
      this.search();
    },
    routeSort(sortParameters) {
      const sort = [];
      const { selected } = sortParameters;
      if (selected) {
        selected.forEach(({ key, asc }) => {
          sort.push(`${key}:${asc ? 'asc' : 'desc'}`);
        });
      }
      this.updateSortParameters({ sort: sort.join(',') });
      this.search();
    },
    routeSearch(searchParameters) {
      this.firstPage();
      this.updateSearchParameters(searchParameters);
      this.search();
    },
    search() {
      const query = this.searchParameters ? this.searchParameters : {};
      query.page = this.page;
      const sorts = [];
      this.sortParameters.selected.forEach(({ key, asc }) => {
        sorts.push(`${key}:${asc ? 'asc' : 'desc'}`);
      });
      if (sorts.length) {
        query.sort = sorts.join(',');
      }
      query.t = Date.now(); // force refresh
      this.$router.push({ name: 'IntervalList', query });
    },
    doSearch() {
      this.firstPage();
      this.search();
    },
    reset() {
      this.clearParams();
    },
    toggleSelect(index) {
      this.items[index].selected = !this.items[index].selected;
    },
    selectAll(select) {
      this.items.forEach((i) => { i.selected = select; });
    },
    doAccept(publish) {
      this.accept({ publish }).then(() => {
        this.search();
      });
    },
    doDelete() {
      this.delete().then(() => {
        this.search();
      });
    },
  },

  computed: {
    ...mapState('interval', ['searchParameters', 'sortParameters', 'searching', 'page', 'totalItems', 'totalPages', 'items', 'sortDesc', 'sortBy', 'itemsPerPage']),
  },

  data() {
    return {
      fields: [
        {
          key: 'approvalState',
          label: 'Approval State',
        },
        {
          key: 'selected',
          label: 'Selected',
        },
        {
          key: 'publish',
          label: 'Publish',
        },
        {
          key: 'imlgs',
          label: 'IMLGS',
        },
        {
          key: 'interval',
          label: 'Interval',
        },
        {
          key: 'igsn',
          label: 'Sample IGSN',
        },
        {
          key: 'intervalIgsn',
          label: 'Interval IGSN',
        },
        {
          key: 'cruise',
          label: 'Cruise',
          sortable: false,
        },
        {
          key: 'sample',
          label: 'Sample',
          sortable: false,
        },
        {
          key: 'facility',
          label: 'Facility',
          sortable: false,
        },
        {
          key: 'platform',
          label: 'Platform',
          sortable: false,
        },
        {
          key: 'device',
          label: 'Sampling Device',
        },
        {
          key: 'beginDate',
          label: 'Begin Date',
        },
        {
          key: 'endDate',
          label: 'End Date',
        },
        {
          key: 'lat',
          label: 'Latitude',
        },
        {
          key: 'endLat',
          label: 'End Latitude',
        },
        {
          key: 'lon',
          label: 'Longitude',
        },
        {
          key: 'endLon',
          label: 'End Longitude',
        },
        {
          key: 'waterDepth',
          label: 'Water Depth',
        },
        {
          key: 'endWaterDepth',
          label: 'End Water Depth',
        },
        {
          key: 'storageMeth',
          label: 'Storage Meth.',
        },
        {
          key: 'coredLength',
          label: 'Cored Length',
        },
        {
          key: 'coredLengthMm',
          label: 'Cored Length (mm)',
        },
        {
          key: 'coredDiam',
          label: 'Cored Diam.',
        },
        {
          key: 'coredDiamMm',
          label: 'Cored Diam. (mm)',
        },
        {
          key: 'pi',
          label: 'PI',
        },
        {
          key: 'province',
          label: 'Province',
        },
        {
          key: 'sampleLake',
          label: 'Sample Lake',
        },
        {
          key: 'lastUpdate',
          label: 'Sample Last Update',
        },
        {
          key: 'leg',
          label: 'Leg',
        },
        {
          key: 'sampleComments',
          label: 'Sample Comments',
        },
        {
          key: 'sampleComments',
          label: 'Sample Comments',
        },
        {
          key: 'showSampl',
          label: 'Show Sample',
        },
        {
          key: 'depthTop',
          label: 'Depth Top',
        },
        {
          key: 'depthTopMm',
          label: 'Depth Top (mm)',
        },
        {
          key: 'depthBot',
          label: 'Depth Bot.',
        },
        {
          key: 'depthBotMm',
          label: 'Depth Bot. (mm)',
        },
        {
          key: 'dhCoreId',
          label: 'DH Core Id',
        },
        {
          key: 'dhCoreLength',
          label: 'DH Core Length',
        },
        {
          key: 'dhCoreLengthMm',
          label: 'DH Core Length (mm)',
        },
        {
          key: 'dhCoreInterval',
          label: 'DH Core Interval',
        },
        {
          key: 'dTopInDhCore',
          label: 'D Top In DH Core',
        },
        {
          key: 'dTopMmInDhCore',
          label: 'D Top In DH Core (mm)',
        },
        {
          key: 'dBotInDhCore',
          label: 'D Bot. In DH Core',
        },
        {
          key: 'dBotMmInDhCore',
          label: 'D Bot. In DH Core (mm)',
        },
        {
          key: 'lith1',
          label: 'Primary Lithologic Composition',
        },
        {
          key: 'text1',
          label: 'Text. 1',
        },
        {
          key: 'lith2',
          label: 'Secondary Lithologic Composition',
        },
        {
          key: 'text2',
          label: 'Text. 2',
        },
        {
          key: 'comp1',
          label: 'Other Component 1',
        },
        {
          key: 'comp2',
          label: 'Other Component 2',
        },
        {
          key: 'comp3',
          label: 'Other Component 3',
        },
        {
          key: 'comp4',
          label: 'Other Component 4',
        },
        {
          key: 'comp5',
          label: 'Other Component 5',
        },
        {
          key: 'comp6',
          label: 'Other Component 6',
        },
        {
          key: 'description',
          label: 'Description',
        },
        {
          key: 'ages',
          label: 'Geologic Ages',
        },
        {
          key: 'weight',
          label: 'Weight',
        },
        {
          key: 'rockLith',
          label: 'Rock Lith.',
        },
        {
          key: 'rockMin',
          label: 'Rock Min.',
        },
        {
          key: 'weathMeta',
          label: 'Weath./Meta.',
        },
        {
          key: 'remark',
          label: 'Rock Glass Remarks & Mn/Fe Oxide',
        },
        {
          key: 'munsellCode',
          label: 'Munsell Color Code',
        },
        {
          key: 'munsell',
          label: 'Munsell Color',
        },
        {
          key: 'exhaustCode',
          label: 'Exhaust Code',
        },
        {
          key: 'photoLink',
          label: 'Photo Link',
        },
        {
          key: 'intervalLake',
          label: 'Interval Lake',
        },
        {
          key: 'Unit Number',
          label: 'Unit Number',
        },
        {
          key: 'intComments',
          label: 'Int. Comments',
        },
        {
          key: 'dhDevice',
          label: 'DH Device',
        },
        {
          key: 'cmcdTop',
          label: 'cmcdTop',
        },
        {
          key: 'mmcdTop',
          label: 'mmcdTop',
        },
        {
          key: 'cmcdBot',
          label: 'cmcdBot',
        },
        {
          key: 'mmcdBot',
          label: 'mmcdBot',
        },
      ],
    };
  },
};
</script>
