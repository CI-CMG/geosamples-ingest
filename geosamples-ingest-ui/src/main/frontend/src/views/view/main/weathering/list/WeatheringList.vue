<template>
  <InteractiveTable
    :breadcrumbs="[
      { text: 'Geosamples Ingest', to: { name: 'Home' } },
      { text: 'Weathering/Metamorphism', active: true },
    ]"
    module="weathering"
    read-authority="ROLE_WEATHERING_READ"
    :fields="[
      { label: 'Weathering/Metamorphism', value: params.weathering, set: setWeathering },
      { label: 'Weathering/Metamorphism Code', value: params.weatheringCode, set: setWeatheringCode },
    ]"
    :table-fields="tableFields"
    create-route="WeatheringAdd"
    create-text="Add New Weathering/Metamorphism"
    create-authority="ROLE_WEATHERING_CREATE"
    edit-field="weathering"
    edit-parameter="weathering"
    edit-route="WeatheringEdit"
    edit-authority="ROLE_WEATHERING_UPDATE"
  />
</template>

<script>

import {
  mapMutations, mapState,
} from 'vuex';
import InteractiveTable from '@/components/InteractiveTable.vue';

export default {
  components: {
    InteractiveTable,
  },

  methods: {
    ...mapMutations('weathering', ['setWeathering', 'setWeatheringCode']),
  },

  computed: {
    ...mapState('weathering', ['params']),
  },

  data() {
    return {
      tableFields: [
        {
          key: 'weathering',
          label: 'Weathering/Metamorphism',
          sortable: true,
        },
        {
          key: 'weatheringCode',
          label: 'Weathering/Metamorphism Code',
          sortable: true,
        },
      ],
    };
  },
};
</script>
