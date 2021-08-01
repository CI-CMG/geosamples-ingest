<template>
<div>
  <b-card title="Search">
    <b-form @submit.prevent="doSearch" @reset.prevent="reset">
      <b-container fluid>
        <b-row>
          <b-col>
            <b-form-group label="Cruise" :label-for="cruiseId">
              <b-form-input :id="cruiseId" :value="cruise" @change="setCruise"/>
            </b-form-group>
          </b-col>
          <b-col>
            <b-form-group label="Facility Code" :label-for="facilityCodeId">
              <b-form-input :id="facilityCodeId" :value="facilityCode" @change="setFacilityCode"/>
            </b-form-group>
          </b-col>
          <b-col>
            <b-form-group label="Platform" :label-for="platformId">
              <b-form-input :id="platformId" :value="platform" @change="setPlatform"/>
            </b-form-group>
          </b-col>
        </b-row>
      </b-container>
      <div v-if="!searching">
        <b-button type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">Search</b-button>
        <b-button type="reset" variant="danger" class="mb-2 mr-sm-2 mb-sm-0">Clear</b-button>
      </div>
    </b-form>
  </b-card>
  <b-card title="Results">
    <div v-if="searching"><b-spinner/></div>
    <div v-else>
      <b-pagination :value="page" @change="changePage" :total-rows="totalItems" per-page="1000"></b-pagination>
      <div class="geo-temp-samples-table">
<!--        <div v-for="item in items" :key="`${item.imlgs}_${item.interval}`">-->
<!--          <div style="display: inline-block;">{{item.imlgs}}</div>-->
<!--          <div style="display: inline-block;">{{item.interval}}</div>-->
<!--          <div style="display: inline-block;">{{item.cruise}}</div>-->
<!--          <div style="display: inline-block;">{{item.sample}}</div>-->
<!--          <div style="display: inline-block;">{{item.facility}}</div>-->
<!--          <div style="display: inline-block;">{{item.platform}}</div>-->
<!--          <div style="display: inline-block;">{{item.device}}</div>-->
<!--          <div style="display: inline-block;">{{item.shipCode}}</div>-->
<!--          <div style="display: inline-block;">{{item.beginDate}}</div>-->
<!--          <div style="display: inline-block;">{{item.endDate}}</div>-->
<!--          <div style="display: inline-block;">{{item.lat}}</div>-->
<!--          <div style="display: inline-block;">{{item.latDeg}}</div>-->
<!--          <div style="display: inline-block;">{{item.latMin}}</div>-->
<!--          <div style="display: inline-block;">{{item.ns}}</div>-->
<!--          <div style="display: inline-block;">{{item.endLat}}</div>-->
<!--          <div style="display: inline-block;">{{item.endLatDeg}}</div>-->
<!--          <div style="display: inline-block;">{{item.endLatMin}}</div>-->
<!--          <div style="display: inline-block;">{{item.endNs}}</div>-->
<!--          <div style="display: inline-block;">{{item.lon}}</div>-->
<!--          <div style="display: inline-block;">{{item.lonDeg}}</div>-->
<!--          <div style="display: inline-block;">{{item.lonMin}}</div>-->
<!--          <div style="display: inline-block;">{{item.ew}}</div>-->
<!--          <div style="display: inline-block;">{{item.endLon}}</div>-->
<!--          <div style="display: inline-block;">{{item.endLonDeg}}</div>-->
<!--          <div style="display: inline-block;">{{item.endLonMin}}</div>-->
<!--          <div style="display: inline-block;">{{item.endEw}}</div>-->
<!--          <div style="display: inline-block;">{{item.latLonOrig}}</div>-->
<!--          <div style="display: inline-block;">{{item.waterDepth}}</div>-->
<!--          <div style="display: inline-block;">{{item.endWaterDepth}}</div>-->
<!--          <div style="display: inline-block;">{{item.storageMeth}}</div>-->
<!--          <div style="display: inline-block;">{{item.coredLength}}</div>-->
<!--          <div style="display: inline-block;">{{item.coredLengthMm}}</div>-->
<!--          <div style="display: inline-block;">{{item.coredDiam}}</div>-->
<!--          <div style="display: inline-block;">{{item.coredDiamMm}}</div>-->
<!--          <div style="display: inline-block;">{{item.pi}}</div>-->
<!--          <div style="display: inline-block;">{{item.province}}</div>-->
<!--          <div style="display: inline-block;">{{item.sampleLake}}</div>-->
<!--          <div style="display: inline-block;">{{item.otherLink}}</div>-->
<!--          <div style="display: inline-block;">{{item.lastUpdate}}</div>-->
<!--          <div style="display: inline-block;">{{item.igsn}}</div>-->
<!--          <div style="display: inline-block;">{{item.leg}}</div>-->
<!--          <div style="display: inline-block;">{{item.sampleComments}}</div>-->
<!--          <div style="display: inline-block;">{{item.samplePublish}}</div>-->
<!--          <div style="display: inline-block;">{{item.objectId}}</div>-->
<!--          <div style="display: inline-block;">{{item.sampleComments}}</div>-->
<!--          <div style="display: inline-block;">{{item.showSampl}}</div>-->
<!--          <div style="display: inline-block;">{{item.depthTop}}</div>-->
<!--          <div style="display: inline-block;">{{item.depthTopMm}}</div>-->
<!--          <div style="display: inline-block;">{{item.depthBot}}</div>-->
<!--          <div style="display: inline-block;">{{item.depthBotMm}}</div>-->
<!--          <div style="display: inline-block;">{{item.dhCoreId}}</div>-->
<!--          <div style="display: inline-block;">{{item.dhCoreLength}}</div>-->
<!--          <div style="display: inline-block;">{{item.dhCoreLengthMm}}</div>-->
<!--          <div style="display: inline-block;">{{item.dhCoreInterval}}</div>-->
<!--          <div style="display: inline-block;">{{item.dTopInDhCore}}</div>-->
<!--          <div style="display: inline-block;">{{item.dTopMmInDhCore}}</div>-->
<!--          <div style="display: inline-block;">{{item.dBotInDhCore}}</div>-->
<!--          <div style="display: inline-block;">{{item.dBotMmInDhCore}}</div>-->
<!--          <div style="display: inline-block;">{{item.lith1}}</div>-->
<!--          <div style="display: inline-block;">{{item.text1}}</div>-->
<!--          <div style="display: inline-block;">{{item.lith2}}</div>-->
<!--          <div style="display: inline-block;">{{item.text2}}</div>-->
<!--          <div style="display: inline-block;">{{item.comp1}}</div>-->
<!--          <div style="display: inline-block;">{{item.comp2}}</div>-->
<!--          <div style="display: inline-block;">{{item.comp3}}</div>-->
<!--          <div style="display: inline-block;">{{item.comp4}}</div>-->
<!--          <div style="display: inline-block;">{{item.comp5}}</div>-->
<!--          <div style="display: inline-block;">{{item.comp6}}</div>-->
<!--          <div style="display: inline-block;">{{item.description}}</div>-->
<!--          <div style="display: inline-block;">{{item.age}}</div>-->
<!--          <div style="display: inline-block;">{{item.absoluteAgeTop}}</div>-->
<!--          <div style="display: inline-block;">{{item.absoluteAgeBot}}</div>-->
<!--          <div style="display: inline-block;">{{item.weight}}</div>-->
<!--          <div style="display: inline-block;">{{item.rockLith}}</div>-->
<!--          <div style="display: inline-block;">{{item.rockMin}}</div>-->
<!--          <div style="display: inline-block;">{{item.weathMeta}}</div>-->
<!--          <div style="display: inline-block;">{{item.remark}}</div>-->
<!--          <div style="display: inline-block;">{{item.munsellCode}}</div>-->
<!--          <div style="display: inline-block;">{{item.munsell}}</div>-->
<!--          <div style="display: inline-block;">{{item.exhaustCode}}</div>-->
<!--          <div style="display: inline-block;">{{item.photoLink}}</div>-->
<!--          <div style="display: inline-block;">{{item.intervalLake}}</div>-->
<!--          <div style="display: inline-block;">{{item.unitNumber}}</div>-->
<!--          <div style="display: inline-block;">{{item.intComments}}</div>-->
<!--          <div style="display: inline-block;">{{item.dhDevice}}</div>-->
<!--          <div style="display: inline-block;">{{item.cmcdTop}}</div>-->
<!--          <div style="display: inline-block;">{{item.mmcdTop}}</div>-->
<!--          <div style="display: inline-block;">{{item.cmcdBot}}</div>-->
<!--          <div style="display: inline-block;">{{item.mmcdBot}}</div>-->
<!--          <div style="display: inline-block;">{{item.intervalPublish}}</div>-->
<!--          <div style="display: inline-block;">{{item.intervalIgsn}}</div>-->
<!--        </div>-->
        <IntervalListTable
          :sortChanged="sortChanged"
          :sortBy="sortBy"
          :sortDesc="sortDesc"
          :items="items"
          :fields="fields"
          :sortableColumns="['cruise', 'sample', 'facility', 'platform']"
        />
      </div>
      <b-pagination class="mt-3" :value="page" @change="changePage" :total-rows="totalItems" per-page="1000"></b-pagination>
    </div>
  </b-card>

</div>
</template>

<script>

import genId from '@/components/idGenerator';
import {
  mapActions, mapMutations, mapState,
} from 'vuex';
import IntervalListTable from './IntervalListTable.vue';

const route = (self, to) => {
  self.setPlatform(to.query.platform);
  self.setCruise(to.query.cruise);
  self.setFacilityCode(to.query.facilityCode);
  self.setPage(to.query.page);
  self.setSort(to.query.sort);
  self.searchPage();
};

export default {
  components: {
    IntervalListTable,
  },
  beforeMount() {
    this.cruiseId = genId();
    this.facilityCodeId = genId();
    this.platformId = genId();
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
    ...mapMutations('interval', ['setPlatform', 'setCruise', 'setFacilityCode', 'clearParams', 'firstPage', 'setPage', 'setSortBy', 'setSortDesc', 'clearAll', 'setSort']),
    ...mapActions('interval', ['accept', 'searchPage']),
    sortChanged({ sortBy, sortDesc }) {
      this.setSortBy(sortBy);
      this.setSortDesc(sortDesc);
      this.firstPage();
      this.search();
    },
    changePage(page) {
      this.setPage(page);
      this.search();
    },
    search() {
      const query = {};
      if (this.platform) {
        query.platform = this.platform;
      }
      if (this.cruise) {
        query.cruise = this.cruise;
      }
      if (this.facilityCode) {
        query.facilityCode = this.facilityCode;
      }
      query.page = this.page;
      query.sort = `${this.sortBy}:${this.sortDesc ? 'desc' : 'asc'}`;
      this.$router.push({ name: 'IntervalList', query });
    },
    doSearch() {
      this.firstPage();
      this.search();
    },
    reset() {
      this.clearParams();
    },
    toggle(index) {
      this.items[index].selected = !this.items[index].selected;
    },
    selectAll(select) {
      this.items.forEach((i) => { i.selected = select; });
    },
    doAccept() {
      this.accept().then(() => {
        this.firstPage();
        this.searchPage();
      });
    },
  },

  computed: {
    ...mapState('interval', ['platform', 'cruise', 'facilityCode', 'searching', 'page', 'totalItems', 'totalPages', 'items', 'sortDesc', 'sortBy']),
    // platform: {
    //   get() {
    //     return this.params.platform;
    //   },
    //   set(value) {
    //     this.setPlatform(value);
    //   },
    // },
    // cruise2: {
    //   get() {
    //     return this.cruise;
    //   },
    //   set(value) {
    //     this.setCruise(value);
    //   },
    // },
    // facilityCode2: {
    //   get() {
    //     return this.facilityCode;
    //   },
    //   set(value) {
    //     this.setFacilityCode(value);
    //   },
    // },
    // currentPage: {
    //   get() {
    //     return this.page;
    //   },
    //   set(value) {
    //     this.setPage(value);
    //   },
    // },
  },

  data() {
    return {
      cruiseId: null,
      facilityCodeId: null,
      platformId: null,

      fields: [
        {
          key: 'imlgs',
          label: 'IMLGS',
        },
        {
          key: 'interval',
          label: 'Interval',
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
          label: 'Device',
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
          key: 'latDeg',
          label: 'Lat. Deg.',
        },
        {
          key: 'latMin',
          label: 'Lat. Min.',
        },
        {
          key: 'ns',
          label: 'N/S',
        },
        {
          key: 'endLat',
          label: 'End Latitude',
        },
        {
          key: 'endLatDeg',
          label: 'End Lat. Deg.',
        },
        {
          key: 'endLatMin',
          label: 'End Lat. Min.',
        },
        {
          key: 'endNs',
          label: 'End N/S',
        },
        {
          key: 'lon',
          label: 'Longitude',
        },
        {
          key: 'lonDeg',
          label: 'Lon. Deg.',
        },
        {
          key: 'lonMin',
          label: 'Lon. Min.',
        },
        {
          key: 'ew',
          label: 'E/W',
        },
        {
          key: 'endLon',
          label: 'End Longitude',
        },
        {
          key: 'endLonDeg',
          label: 'End Lon. Deg.',
        },
        {
          key: 'endLonMin',
          label: 'End Lon. Min.',
        },
        {
          key: 'endEw',
          label: 'End E/W',
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
          key: 'otherLink',
          label: 'Other Link',
        },
        {
          key: 'lastUpdate',
          label: 'Sample Last Update',
        },
        {
          key: 'igsn',
          label: 'Sample IGSN',
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
          key: 'samplePublish',
          label: 'Sample Publish',
        },
        {
          key: 'objectId',
          label: 'Object Id',
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
          label: 'Lith. 1',
        },
        {
          key: 'text1',
          label: 'Text. 1',
        },
        {
          key: 'lith2',
          label: 'Lith. 2',
        },
        {
          key: 'text2',
          label: 'Text. 2',
        },
        {
          key: 'comp1',
          label: 'Comp. 1',
        },
        {
          key: 'comp2',
          label: 'Comp. 2',
        },
        {
          key: 'comp3',
          label: 'Comp. 3',
        },
        {
          key: 'comp4',
          label: 'Comp. 4',
        },
        {
          key: 'comp5',
          label: 'Comp. 5',
        },
        {
          key: 'comp6',
          label: 'Comp. 6',
        },
        {
          key: 'description',
          label: 'Description',
        },
        {
          key: 'age',
          label: 'Age',
        },
        {
          key: 'absoluteAgeTop',
          label: 'Absolute Age Top',
        },
        {
          key: 'absoluteAgeBot',
          label: 'Absolute Age Bot.',
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
          label: 'Remark',
        },
        {
          key: 'munsellCode',
          label: 'Munsell Code',
        },
        {
          key: 'munsell',
          label: 'Munsell',
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
        {
          key: 'intervalPublish',
          label: 'Interval Publish',
        },
        {
          key: 'intervalIgsn',
          label: 'Interval IGSN',
        },
      ],
    };
  },
};
</script>
