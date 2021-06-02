<template>
  <div class="m-2">
    <b-breadcrumb :items="items"/>

    <div v-if="loading">
      <b-spinner/>
    </div>

    <div v-else>
      <b-form inline class="m-2" @submit="onSubmit">
        <b-button type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0">Refresh</b-button>
        <b-button :to="{ name: 'AddProvider' }" variant="secondary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">Add New Provider</b-button>
      </b-form>

      <ExpandableRowList :rows="providers" v-slot="row">
        <ExpanableRow :row="row" :fields="['website', 'uniqueIdPrefix', 'organizationName', 'enabled']">
          <template v-slot:header-right>
            <b-button-group>
              <b-button variant="outline-primary" v-bind:href="'edit/' + row.id">
                <b-icon icon="pencil"></b-icon> Edit
              </b-button>

              <b-button variant="outline-primary">
                <b-icon icon="trash" class="ml-2"></b-icon> Delete
              </b-button>
            </b-button-group>
          </template>
        </ExpanableRow>
      </ExpandableRowList>
    </div>

  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex';
import ExpanableRow from '@/components/ExpanableRow.vue';
import ExpandableRowList from '@/components/ExpandableRowList.vue';

export default {
  components: {
    ExpanableRow,
    ExpandableRowList,
  },
  data() {
    return {
      items: [
        {
          text: 'Crowbar',
          to: { name: 'Home' },
        },
        {
          text: 'Provider',
          active: true,
        },
      ],
    };
  },
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      vm.loadProviders();
      return true;
    });
  },
  methods: {
    ...mapActions('provider', ['loadProviders']),
    // rowClicked(item) {
    //   const {
    //     platform,
    //     survey,
    //     instrument,
    //   } = item;
    //   this.$router.push({
    //     name: 'DatasetDetail',
    //     params: {
    //       platform,
    //       survey,
    //       instrument,
    //     },
    //   });
    // },
    onSubmit(evt) {
      evt.preventDefault();
      this.loadProviders();
    },
  },
  computed: {
    ...mapState('provider', ['providers', 'loading']),
  },
};
</script>
