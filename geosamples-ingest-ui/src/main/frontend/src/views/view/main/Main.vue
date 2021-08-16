<template>
  <div>
    <b-navbar fixed="top" variant="info">
      <b-navbar-toggle :target="navbarId" class="mr-2"></b-navbar-toggle>
      <b-collapse :id="navbarId" is-nav>
        <b-navbar-nav>
          <b-nav-item :to="{ name: 'Home'}">Home</b-nav-item>
          <b-nav-item-dropdown text="Controlled Vocabulary">
            <b-dropdown-item :to="{ name: 'Age'}">Geologic Age</b-dropdown-item>
            <b-dropdown-item :to="{ name: 'Device'}">Sampling Device</b-dropdown-item>
            <b-dropdown-item :to="{ name: 'Facility'}">Facility</b-dropdown-item>
            <b-dropdown-item :to="{ name: 'Lithology'}">Lithologic Composition</b-dropdown-item>
            <b-dropdown-item :to="{ name: 'Munsell'}">Munsell Color Code</b-dropdown-item>
            <b-dropdown-item :to="{ name: 'Platform'}">Ship/Platform</b-dropdown-item>
            <b-dropdown-item :to="{ name: 'Province'}">Physiographic Province</b-dropdown-item>
            <b-dropdown-item :to="{ name: 'Remark'}">Rock Glass Remarks & Mn/Fe Oxide</b-dropdown-item>
            <b-dropdown-item :to="{ name: 'RockLithology'}">Rock Lithology</b-dropdown-item>
            <b-dropdown-item :to="{ name: 'RockMineral'}">Rock Mineralogy</b-dropdown-item>
            <b-dropdown-item :to="{ name: 'StorageMethod'}">Storage Method</b-dropdown-item>
            <b-dropdown-item :to="{ name: 'Texture'}">Texture</b-dropdown-item>
            <b-dropdown-item :to="{ name: 'Weathering'}">Weathering/Metamorphism</b-dropdown-item>
          </b-nav-item-dropdown>
          <b-nav-item-dropdown text="Curator Data">
            <b-dropdown-item :to="{ name: 'NewSubmission'}">New Submission</b-dropdown-item>
            <b-dropdown-item :to="{ name: 'IntervalList'}">Sample + Interval</b-dropdown-item>
          </b-nav-item-dropdown>
        </b-navbar-nav>
      </b-collapse>

      <b-navbar-nav class="ml-auto">
        <b-nav-form @submit.stop.prevent="submit">
          <span v-if="user.username">Username: <b>{{user.username}}</b></span>
          <b-button size="sm" type="submit">{{user.username ? 'Log Out' : 'Log In'}}</b-button>
        </b-nav-form>
      </b-navbar-nav>
    </b-navbar>
    <div style="margin-top: 50px;" class="ml-2 mr-2">
      <router-view/>
    </div>
  </div>
</template>

<script>
import genId from '@/components/idGenerator';
import { mapState } from 'vuex';
import { logout } from '@/sessionMonster';

export default {
  data() {
    return {
      navbarId: '',
    };
  },
  beforeMount() {
    this.navbarId = genId();
  },
  computed: {
    ...mapState('user', ['user']),
  },
  methods: {
    submit() {
      if (this.user.username) {
        logout(this.$router, this.$store);
      } else {
        this.$router.push({ name: 'Login' });
      }
    },
  },
};
</script>
