<template>
<div>
  <b-modal ref="delete-modal" title="Delete Samples and Intervals" ok-variant="danger" ok-title="Delete" @ok="doDelete">
    <p class="my-4">Are you sure you want to delete the selected samples and associated intervals?</p>
  </b-modal>
  <SampleSearchControl :onSearch="routeSearch" :onSort="routeSort" />
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
        <b-button variant="danger" class="mb-2 mr-sm-2 mb-sm-0 mr-3" @click="showModal">Delete Selected Samples and Intervals</b-button>
      </div>
      </b-card>
      <div class="geo-temp-samples-table">
        <SampleListTable
          :items="items"
          :fields="fields"
          :toggleSelect="toggleSelect"
          :sortedColumns="sortParameters.selected"
        />
      </div>
      <b-pagination class="mt-3" :value="page" @change="changePage" :total-rows="totalItems" :per-page="itemsPerPage"></b-pagination>
    </div>
  </b-card>

</div>
</template>

<script>
import {
  mapActions, mapMutations, mapState,
} from 'vuex';
import SampleListTable from './SampleListTable.vue';
import SampleSearchControl from './SampleSearchControl.vue';

const route = (self, to) => {
  self.updateSearchParameters(to.query);
  self.updateSortParameters(to.query);
  self.setPage(to.query.page);
  self.setSort(to.query.sort);
  self.searchPage();
};

export default {
  components: {
    SampleListTable,
    SampleSearchControl,
  },
  beforeRouteEnter(to, from, next) {
    next((self) => {
      route(self, to);
    });
  },
  beforeRouteUpdate(to, from, next) {
    // noinspection JSDeepBugsSwappedArgs
    route(this, to);
    next();
  },
  beforeRouteLeave(to, from, next) {
    this.clearAll();
    next();
  },

  methods: {
    ...mapMutations('sample', ['updateSortParameters', 'firstPage', 'setPage', 'setSortBy', 'setSortDesc', 'clearAll', 'setSort', 'updateSearchParameters', 'updateSortParameters']),
    ...mapActions('sample', ['searchPage', 'accept', 'delete']),
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
      this.$router.push({ name: 'SampleList', query });
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
        this.firstPage();
        this.search();
      });
    },
    doDelete() {
      this.delete().then(() => {
        this.firstPage();
        this.search();
      });
    },
  },

  computed: {
    ...mapState('sample', ['searchParameters', 'sortParameters', 'searching', 'page', 'totalItems', 'totalPages', 'items', 'sortDesc', 'sortBy', 'itemsPerPage']),
  },

  data() {
    return {
      fields: [
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
          key: 'igsn',
          label: 'Sample IGSN',
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
          key: 'facilityCode',
          label: 'Facility',
          sortable: false,
        },
        {
          key: 'platform',
          label: 'Platform',
          sortable: false,
        },
        {
          key: 'deviceCode',
          label: 'Sampling Device',
        },
        {
          key: 'shipCode',
          label: 'Ship Code',
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
          key: 'latLonOrig',
          label: 'Lat. Lon. Orig.',
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
          key: 'storageMethCode',
          label: 'Storage Meth.',
        },
        {
          key: 'coredLength',
          label: 'Cored Length',
        },
        {
          key: 'coredDiam',
          label: 'Cored Diam.',
        },
        {
          key: 'pi',
          label: 'PI',
        },
        {
          key: 'provinceCode',
          label: 'Province',
        },
        {
          key: 'lake',
          label: 'Sample Lake',
        },
        {
          key: 'otherLink',
          label: 'Other Link',
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
          key: 'showSampl',
          label: 'Show Sample',
        },
      ],
    };
  },
};
</script>
