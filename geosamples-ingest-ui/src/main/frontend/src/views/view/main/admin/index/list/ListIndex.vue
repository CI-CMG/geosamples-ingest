<template>
  <div class="m-2">
    <b-breadcrumb :items="bc"/>

    <div v-if="loading">
      <b-spinner/>
    </div>

    <div v-else>
      <b-form inline class="m-2" @submit="loadIndex">
        <b-button type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0">Refresh</b-button>
      </b-form>

      <br /><br />

      <b-table striped hover :items="index" :fields="fields">
      </b-table>

    </div>

  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex';

export default {
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      vm.loadIndex();
      return true;
    });
  },
  computed: {
    ...mapState('index', ['index', 'loading']),
  },
  methods: {
    ...mapActions('index', ['loadIndex']),
  },
  data() {
    return {
      fields: [
        'traceId',
        'publish',
        'externalId',
        'provider',
        'platform',
        'instrument',
        'startTime',
        'endTime',
        'fileName',
        'fileSize',
        'lastUpdated',
      ],

      bc: [
        {
          text: 'Crowbar',
          to: { name: 'Home' },
        },
        {
          text: 'Index',
          active: true,
        },
      ],
    };
  },
};
</script>
