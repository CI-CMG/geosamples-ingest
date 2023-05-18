<template>
  <div>
    <b-navbar fixed="top" variant="info">
      <b-navbar-toggle :target="navbarId" class="mr-2"></b-navbar-toggle>
      <b-collapse :id="navbarId" is-nav>
        <b-navbar-nav>
          <b-nav-item :to="{ name: 'Home'}">Home</b-nav-item>
          <AuthorizedContent :authorities="controlledVocabularyAuthorities">
            <b-nav-item-dropdown text="Controlled Vocabulary">
              <AuthorizedContent :authorities="['ROLE_AGE_READ']">
                <b-dropdown-item :to="{ name: 'Age'}">Geologic Age</b-dropdown-item>
              </AuthorizedContent>
              <AuthorizedContent :authorities="['ROLE_DEVICE_READ']">
                <b-dropdown-item :to="{ name: 'Device'}">Sampling Device</b-dropdown-item>
              </AuthorizedContent>
              <AuthorizedContent :authorities="['ROLE_FACILITY_READ']">
                <b-dropdown-item :to="{ name: 'Facility'}">Facility</b-dropdown-item>
              </AuthorizedContent>
              <AuthorizedContent :authorities="['ROLE_LITHOLOGY_READ']">
                <b-dropdown-item :to="{ name: 'Lithology'}">Lithologic Composition</b-dropdown-item>
              </AuthorizedContent>
              <AuthorizedContent :authorities="['ROLE_MUNSELL_READ']">
                <b-dropdown-item :to="{ name: 'Munsell'}">Munsell Color Code</b-dropdown-item>
              </AuthorizedContent>
              <AuthorizedContent :authorities="['ROLE_PLATFORM_READ']">
                <b-dropdown-item :to="{ name: 'Platform'}">Ship/Platform</b-dropdown-item>
              </AuthorizedContent>
              <AuthorizedContent :authorities="['ROLE_PROVINCE_READ']">
                <b-dropdown-item :to="{ name: 'Province'}">Physiographic Province</b-dropdown-item>
              </AuthorizedContent>
              <AuthorizedContent :authorities="['ROLE_REMARK_READ']">
                <b-dropdown-item :to="{ name: 'Remark'}">Rock Glass Remarks & Mn/Fe Oxide</b-dropdown-item>
              </AuthorizedContent>
              <AuthorizedContent :authorities="['ROLE_ROCK_LITHOLOGY_READ']">
                <b-dropdown-item :to="{ name: 'RockLithology'}">Rock Lithology</b-dropdown-item>
              </AuthorizedContent>
              <AuthorizedContent :authorities="['ROLE_ROCK_MINERAL_READ']">
                <b-dropdown-item :to="{ name: 'RockMineral'}">Rock Mineralogy</b-dropdown-item>
              </AuthorizedContent>
              <AuthorizedContent :authorities="['ROLE_STORAGE_METHOD_READ']">
                <b-dropdown-item :to="{ name: 'StorageMethod'}">Storage Method</b-dropdown-item>
              </AuthorizedContent>
              <AuthorizedContent :authorities="['ROLE_TEXTURE_READ']">
                <b-dropdown-item :to="{ name: 'Texture'}">Texture</b-dropdown-item>
              </AuthorizedContent>
              <AuthorizedContent :authorities="['ROLE_WEATHERING_READ']">
                <b-dropdown-item :to="{ name: 'Weathering'}">Weathering/Metamorphism</b-dropdown-item>
              </AuthorizedContent>
            </b-nav-item-dropdown>
          </AuthorizedContent>
          <AuthorizedContent :authorities="['ROLE_USER_READ', 'ROLE_USER_ROLE_READ']">
            <b-nav-item-dropdown text="Administration">
              <AuthorizedContent :authorities="['ROLE_USER_READ']">
                <b-dropdown-item :to="{ name: 'User'}">Users</b-dropdown-item>
              </AuthorizedContent>
              <AuthorizedContent :authorities="['ROLE_USER_ROLE_READ']">
                <b-dropdown-item :to="{ name: 'Role'}">Roles</b-dropdown-item>
              </AuthorizedContent>
            </b-nav-item-dropdown>
          </AuthorizedContent>
          <AuthorizedContent :authorities="curatorDataAuthorities">
            <b-nav-item-dropdown text="Curator Data">
              <AuthorizedContent :authorities="['ROLE_DATA_MANAGER_CREATE']">
                <b-dropdown-item :to="{ name: 'NewSubmission'}">New Submission</b-dropdown-item>
              </AuthorizedContent>
              <AuthorizedContent :authorities="['ROLE_DATA_MANAGER_READ']">
                <b-dropdown-item :to="{ name: 'IntervalList'}">Sample + Interval</b-dropdown-item>
              </AuthorizedContent>
              <AuthorizedContent :authorities="['ROLE_SAMPLE_READ']">
                <b-dropdown-item :to="{ name: 'SampleList'}">Sample</b-dropdown-item>
              </AuthorizedContent>
            </b-nav-item-dropdown>
          </AuthorizedContent>
          <AuthorizedContent :authorities="ancillaryDataAuthorities">
            <b-nav-item-dropdown text="Ancillary Data">
              <AuthorizedContent :authorities="['ROLE_CRUISE_READ']">
                <b-dropdown-item :to="{ name: 'CruiseList'}">Cruise</b-dropdown-item>
              </AuthorizedContent>
              <AuthorizedContent :authorities="['ROLE_CRUISE_LINK_READ']">
                <b-dropdown-item :to="{ name: 'CruiseLinkList'}">Cruise Link</b-dropdown-item>
              </AuthorizedContent>
              <AuthorizedContent :authorities="['ROLE_SAMPLE_LINK_READ']">
                <b-dropdown-item :to="{ name: 'SampleLinkList'}">Sample Link</b-dropdown-item>
              </AuthorizedContent>
            </b-nav-item-dropdown>
          </AuthorizedContent>
          <AuthorizedContent :authorities="['ROLE_PROVIDER_CRUISE_READ']">
            <b-nav-item :to="{ name: 'ProviderCruiseList' }">Cruises</b-nav-item>
          </AuthorizedContent>
          <AuthorizedContent :authorities="['ROLE_PROVIDER_SAMPLE_READ']">
            <b-nav-item :to="{ name: 'ProviderSampleList' }">Samples</b-nav-item>
          </AuthorizedContent>
        </b-navbar-nav>
      </b-collapse>

      <b-navbar-nav class="ml-auto">
        <b-nav-form @submit.stop.prevent="submit">
          <b-link v-if="loggedIn" :to="{ name: 'MeEdit' }">{{user.displayName}}</b-link>
          <b-button size="sm" type="submit" class="ml-2">{{loggedIn ? 'Log Out' : 'Log In'}}</b-button>
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
import { logout, redirectLogin } from '@/sessionMonster';
import AuthorizedContent from '@/components/AuthorizedContent.vue';

export default {
  components: {
    AuthorizedContent,
  },

  data() {
    return {
      navbarId: '',

      controlledVocabularyAuthorities: [
        'ROLE_AGE_READ',
        'ROLE_DEVICE_READ',
        'ROLE_FACILITY_READ',
        'ROLE_LITHOLOGY_READ',
        'ROLE_MUNSELL_READ',
        'ROLE_PLATFORM_READ',
        'ROLE_PROVINCE_READ',
        'ROLE_REMARK_READ',
        'ROLE_ROCK_LITHOLOGY_READ',
        'ROLE_ROCK_MINERAL_READ',
        'ROLE_STORAGE_METHOD_READ',
        'ROLE_TEXTURE_READ',
        'ROLE_WEATHERING_READ',
      ],

      curatorDataAuthorities: [
        'ROLE_DATA_MANAGER_CREATE',
        'ROLE_DATA_MANAGER_READ',
        'ROLE_SAMPLE_READ',
      ],

      ancillaryDataAuthorities: [
        'ROLE_CRUISE_READ',
        'ROLE_CRUISE_LINK_READ',
        'ROLE_SAMPLE_LINK_READ',
      ],
    };
  },
  beforeMount() {
    this.navbarId = genId();
  },
  computed: {
    ...mapState('userAuth', ['user']),
    loggedIn() {
      return !!this.user.userName;
    },
  },
  methods: {
    submit() {
      if (this.loggedIn) {
        logout(this.$router, this.$store);
      } else {
        redirectLogin(this.$router);
      }
    },
  },
};
</script>
