<template>
  <div class="m-2">
    <b-breadcrumb :items="bc"/>

    <div v-if="loading">
      <b-spinner/>
    </div>

    <div v-else>
      <b-form inline class="m-2" @submit="loadAudit">
        <b-button type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0">Refresh</b-button>
      </b-form>

      <br /><br />

      <b-table striped hover :items="audit" :fields="fields">
      </b-table>

    </div>

  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex';

export default {
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      vm.loadAudit();
      return true;
    });
  },
  computed: {
    ...mapState('audit', ['audit', 'loading']),
  },
  methods: {
    ...mapActions('audit', ['loadAudit']),
  },
  data() {
    return {
      fields: [
        'id',
        'type',
        'traceId',
        'provider',
        'uniqueId',
        'source',
        'event',
        'eventTime',
      ],

      bc: [
        {
          text: 'Crowbar',
          to: { name: 'Home' },
        },
        {
          text: 'Audit',
          active: true,
        },
      ],
    };
  },
};
</script>
