<template>
  <b-col lg="4">
    <b-card :title="title">
      <b-button v-b-toggle:collapse variant="outline-primary" @click="toggleCollapseOpen">
        <div v-if="!collapseOpen">
          <b-icon icon="arrow-down-circle"/>
        </div>
        <div v-else>
          <b-icon icon="arrow-up-circle"/>
        </div>
      </b-button>
      <b-collapse id="collapse" class="mt-3">
        <div class="geo-search-card-list">
          <b-row class="mb-2">
            <b-col class="my-auto" cols="5">
              <label :for="swCoordinateId" class="mr-2">SW Coordinate:</label>
            </b-col>
            <b-col>
              <b-input
                :id="swCoordinateId"
                placeholder="latitude, longitude"
                type="text"
                @blur="() => setTouched({ path: 'swCoordinate', touched: true })"
                :value="getValue('swCoordinate')"
                @update="(value) => setValue({ path: 'swCoordinate', value })"
                :state="validCoordinates() && !showError('swCoordinate')"
              />
            </b-col>
          </b-row>
          <b-row>
            <b-col class="my-auto" cols="5">
              <label :for="neCoordinateId" class="mr-2">NE Coordinate:</label>
            </b-col>
            <b-col>
              <b-input
                :id="neCoordinateId"
                placeholder="latitude, longitude"
                type="text"
                @blur="() => setTouched({ path: 'neCoordinate', touched: true })"
                :value="getValue('neCoordinate')"
                @update="(value) => setValue({ path: 'neCoordinate', value })"
                :state="validCoordinates() && !showError('neCoordinate')"
              />
            </b-col>
          </b-row>
        </div>
      </b-collapse>
    </b-card>
  </b-col>
</template>

<script>
import genId from '@/components/idGenerator';

export default {
  props: ['title', 'module'],

  data() {
    return {
      swCoordinateId: null,
      neCoordinateId: null,

      collapseOpen: false,
      coordinateRegex: /^[-+]?([1-8]?\d(\.\d+)?|90(\.0+)?),\s*[-+]?(180(\.0+)?|((1[0-7]\d)|([1-9]?\d))(\.\d+)?)$/g,
    };
  },

  beforeMount() {
    this.swCoordinateId = genId();
    this.neCoordinateId = genId();
  },

  computed: {
    getError() {
      return this.$store.getters[`${this.module}/getError`];
    },
    isTouched() {
      return this.$store.getters[`${this.module}/isTouched`];
    },

    showError() {
      return (path) => ((!this.isTouched(path) && this.getError(path)) ? false : null);
    },

    getValue() {
      return this.$store.getters[`${this.module}/getValue`];
    },
  },

  methods: {
    setTouched(value) {
      this.$store.commit(`${this.module}/setTouched`, value);
    },

    setValue(value) {
      this.$store.commit(`${this.module}/setValue`, value);
    },

    toggleCollapseOpen() {
      this.collapseOpen = !this.collapseOpen;
    },

    validCoordinates() {
      const swValid = this.validCoordinate(this.getValue('swCoordinate'));
      const neValid = this.validCoordinate(this.getValue('neCoordinate'));
      if (swValid === 'N/A' && neValid === 'N/A') {
        return null;
      }
      if (swValid === 'N/A' || neValid === 'N/A') {
        return false;
      }
      return swValid && neValid;
    },

    validCoordinate(coordinate) {
      if (coordinate === '') {
        return 'N/A';
      }
      return coordinate.match(this.coordinateRegex) != null;
    },
  },
};
</script>
