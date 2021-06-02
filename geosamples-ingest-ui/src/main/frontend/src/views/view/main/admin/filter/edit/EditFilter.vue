<template>
    <b-container fluid="">
      <b-row class="justify-content-md-center">
        <b-col cols="3">
          <b-card title="Tile Actions">
            <div v-if="uploading">
              <b-spinner/> Processing
            </div>
            <b-form v-else @submit.prevent="clearTileStatusCache">
              <b-button type="submit" variant="primary">Clear Tile Status Cache</b-button>
            </b-form>
            <div v-if="running">
              <b-spinner/> Processing
            </div>
            <b-form v-else @submit.prevent="generateTiles"><br />
              <b-button type="submit" variant="primary">Regenerate Tiles</b-button>
            </b-form>
          </b-card>
          <b-card title="Import Shapefile">
            <div v-if="running">
              <b-spinner/> Processing
            </div>
            <b-form v-else @submit.prevent="uploadShapeFile" @reset.prevent="() => setFile(null)"><br />
              <b-form-file
                v-model="fileModel"
                :state="Boolean(file)"
                placeholder="Choose a shapefile or drop it here..."
                drop-placeholder="Drop shapefile here..."
              ></b-form-file>
              <div class="mt-2">
                <b-button v-if="Boolean(file)" type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">Upload</b-button>
                <b-button v-if="Boolean(file)" type="reset" variant="danger" class="mb-2 mr-sm-2 mb-sm-0">Cancel</b-button>
              </div>
            </b-form>
          </b-card>
          <b-card title="Search">
            <b-form @submit.prevent="search" @reset.prevent="resetSearch"><br />
              <div style="height: 200px;">Search stuff here</div>
              <div class="mt-2">
                <b-button type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">Search</b-button>
                <b-button type="reset" variant="danger" class="mb-2 mr-sm-2 mb-sm-0">Clear</b-button>
              </div>
            </b-form>
          </b-card>
        </b-col>
        <b-col>
          <div style="width: 100%; height: 625px;">
            <CesiumVue />
          </div>
        </b-col>
      </b-row>
      <b-row>
        <b-col>
          <b-card title="Region Details">
            <div style="height: 200px;">Region detail stuff here</div>
          </b-card>
        </b-col>
      </b-row>
    </b-container>
</template>

<script>
import CesiumVue from '@/components/cesium/CesiumVue.vue';
import {
  mapActions, mapMutations, mapState,
} from 'vuex';

export default {

  components: {
    CesiumVue,
  },

  computed: {
    ...mapState('filter', ['file', 'uploading', 'tilesGenerating']),
    running() {
      return this.uploading || this.tilesGenerating;
    },
    fileModel: {
      get() {
        return this.file;
      },
      set(value) {
        console.log(value);
        this.setFile(value);
      },
    },
  },

  methods: {
    ...mapActions('filter', ['uploadShapeFile', 'startGenChecker', 'stopGenChecker', 'generateTiles']),
    ...mapMutations('filter', ['setFile']),
    clearTileStatusCache() {

    },
    search() {

    },
    resetSearch() {

    },

  },

  mounted() {
    this.startGenChecker();
  },

  beforeDestroy() {
    this.stopGenChecker();
  },

};

</script>
