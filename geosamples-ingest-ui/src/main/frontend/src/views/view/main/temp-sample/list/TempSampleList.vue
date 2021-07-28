<template>
<div>
  <b-form @submit.prevent="search" @reset.prevent="reset">
    <b-container fluid>
      <b-row>
        <b-col>
          <b-form-group label="Cruise" :label-for="cruiseId">
            <b-form-input :id="cruiseId" v-model="cruise"/>
          </b-form-group>
        </b-col>
        <b-col>
          <b-form-group label="Facility Code" :label-for="facilityCodeId">
            <b-form-input :id="facilityCodeId" v-model="facilityCode"/>
          </b-form-group>
        </b-col>
        <b-col>
          <b-form-group label="Platform" :label-for="platformId">
            <b-form-input :id="platformId" v-model="platform"/>
          </b-form-group>
        </b-col>
      </b-row>
    </b-container>

    <div v-if="!searching">
      <b-button type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">Search</b-button>
      <b-button type="reset" variant="danger" class="mb-2 mr-sm-2 mb-sm-0">Clear</b-button>
    </div>
  </b-form>
  <b-table
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
  </b-table>
  <b-pagination v-model="currentPage" @input="changePage" :total-rows="totalItems" per-page="50"></b-pagination>
</div>
</template>

<script>

import genId from '@/components/idGenerator';
import {
  mapActions, mapMutations, mapState,
} from 'vuex';

const route = (self) => {
  self.setPlatform(self.$route.query.platform);
  self.setCruise(self.$route.query.cruise);
  self.setFacilityCode(self.$route.query.facilityCode);
  self.searchPage();
};

export default {
  beforeMount() {
    this.cruiseId = genId();
    this.facilityCodeId = genId();
    this.platformId = genId();
  },
  beforeRouteEnter(to, from, next) {
    next((self) => {
      route(self);
    });
  },
  beforeRouteUpdate(to, from, next) {
    route(this);
    next();
  },
  beforeRouteLeave(to, from, next) {
    this.clearAll();
    next();
  },

  methods: {
    ...mapMutations('tempSampleInterval', ['setPlatform', 'setCruise', 'setFacilityCode', 'clearParams', 'firstPage', 'setPage', 'setSortBy', 'setSortDesc', 'clearAll']),
    ...mapActions('tempSampleInterval', ['searchPage', 'changePage']),
    sortChanged({ sortBy, sortDesc }) {
      this.setSortBy(sortBy);
      this.setSortDesc(sortDesc);
      this.search();
    },
    search() {
      this.firstPage();
      this.$router.push({ name: 'TempSampleList', query: { platform: this.platform, cruise: this.cruise, facilityCode: this.facilityCode } });
    },
    reset() {
      this.clearParams();
    },
  },

  computed: {
    ...mapState('tempSampleInterval', ['searching', 'page', 'totalItems', 'totalPages', 'items', 'params', 'sortDesc', 'sortBy']),
    platform: {
      get() {
        return this.params.platform;
      },
      set(value) {
        this.setPlatform(value);
      },
    },
    cruise: {
      get() {
        return this.params.cruise;
      },
      set(value) {
        this.setCruise(value);
      },
    },
    facilityCode: {
      get() {
        return this.params.facilityCode;
      },
      set(value) {
        this.setFacilityCode(value);
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
      cruiseId: null,
      facilityCodeId: null,
      platformId: null,

      fields: [
        {
          key: 'cruise',
          label: 'Cruise',
          sortable: true,
        },
        {
          key: 'sample',
          label: 'Sample',
          sortable: true,
        },
        {
          key: 'facility',
          label: 'Facility',
          sortable: true,
        },
        {
          key: 'platform',
          label: 'Platform',
          sortable: true,
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
          key: 'imlgs',
          label: 'IMLGS',
        },
        {
          key: 'interval',
          label: 'Interval',
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
        {
          key: 'intervalImlgs',
          label: 'Interval IMLGS',
        },
        {
          key: 'intervalParentIgsn',
          label: 'Int. Parent IGSN',
        },
      ],
    };
  },
};
</script>
