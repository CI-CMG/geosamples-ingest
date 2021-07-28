<template>
  <b-card title="Submission Errors - Correct And Resubmit">
    <b-table
      striped
      bordered
      small
      hover
      :items="form.rows"
      :fields="fields">
        <template #cell(rowNum)="data">
          {{data.index + 2}}
        </template>
        <template #cell()="data">
          <SubmissionResultCell :value="getValue(`rows[${data.index}].${data.field.key}`)" :error="getError(`rows[${data.index}].${data.field.key}`)"/>
        </template>
    </b-table>
  </b-card>

</template>

<script>
import { mapGetters, mapMutations, mapState } from 'vuex';
import SubmissionResultCell from './SubmissionResultCell.vue';

export default {
  components: {
    SubmissionResultCell,
  },
  data() {
    return {
      fields: [
        {
          key: 'rowNum',
          label: 'Row Number',
        },
        {
          key: 'facilityCode',
          label: 'Facility Code',
        },
        {
          key: 'shipName',
          label: 'Ship Name',
        },
        {
          key: 'cruiseId',
          label: 'Cruise ID',
        },
        {
          key: 'sampleId',
          label: 'Sample ID',
        },
        {
          key: 'dateCollected',
          label: 'Date Collected',
        },
        {
          key: 'endDate',
          label: 'End Date',
        },
        {
          key: 'beginningLatitude',
          label: 'Beginning Latitude',
        },
        {
          key: 'beginningLongitude',
          label: 'Beginning Longitude',
        },
        {
          key: 'endingLatitude',
          label: 'Ending Latitude',
        },
        {
          key: 'endingLongitude',
          label: 'Ending Longitude',
        },
        {
          key: 'beginningWaterDepth',
          label: 'Beginning Water Depth',
        },
        {
          key: 'endingWaterDepth',
          label: 'Ending Water Depth',
        },
        {
          key: 'samplingDeviceCode',
          label: 'Sampling Device Code',
        },
        {
          key: 'storageMethodCode',
          label: 'Storage Method Code',
        },
        {
          key: 'coreLength',
          label: 'Core Length',
        },
        {
          key: 'coreDiameter',
          label: 'Core Diameter',
        },
        {
          key: 'depthToTopOfInterval',
          label: 'Depth to Top of Interval',
        },
        {
          key: 'depthToBottomOfInterval',
          label: 'Depth to Bottom of Interval',
        },
        {
          key: 'primaryLithologicCompositionCode',
          label: 'Primary Lithologic Composition Code',
        },
        {
          key: 'primaryTextureCode',
          label: 'Primary Texture Code',
        },
        {
          key: 'secondaryLithologicCompositionCode',
          label: 'Secondary Lithologic Composition Code',
        },
        {
          key: 'secondaryTextureCode',
          label: 'Secondary Texture Code',
        },
        {
          key: 'otherComponentCodes[0]',
          label: 'Other Component Code',
        },
        {
          key: 'otherComponentCodes[1]',
          label: 'Other Component Code',
        },
        {
          key: 'otherComponentCodes[2]',
          label: 'Other Component Code',
        },
        {
          key: 'otherComponentCodes[3]',
          label: 'Other Component Code',
        },
        {
          key: 'otherComponentCodes[4]',
          label: 'Other Component Code',
        },
        {
          key: 'otherComponentCodes[5]',
          label: 'Other Component Code',
        },
        {
          key: 'geologicAgeCode',
          label: 'Geologic Age Code',
        },
        {
          key: 'intervalNumber',
          label: 'Interval #',
        },
        {
          key: 'bulkWeight',
          label: 'Bulk Weight',
        },
        {
          key: 'physiographicProvinceCode',
          label: 'Physiographic Province Code',
        },
        {
          key: 'sampleLithologyCode',
          label: 'Sample Lithology Code',
        },
        {
          key: 'sampleMineralogyCode',
          label: 'Sample Mineralogy Code',
        },
        {
          key: 'sampleWeatheringOrMetamorphismCode',
          label: 'Sample Weathering or Metamorphism Code',
        },
        {
          key: 'glassRemarksCode',
          label: 'Glass Remarks Code',
        },
        {
          key: 'munsellColor',
          label: 'Munsell Color',
        },
        {
          key: 'principalInvestigator',
          label: 'Principal Investigator',
        },
        {
          key: 'sampleNotAvailable',
          label: 'Sample Not Available',
        },
        {
          key: 'igsn',
          label: 'IGSN',
        },
        {
          key: 'alternateCruise',
          label: 'Alternate Cruise/Leg',
        },
        {
          key: 'description',
          label: 'Free-form Description of Composition',
        },
        {
          key: 'comments',
          label: 'Comments on Subsample/Interval',
        },
      ],
    };
  },
  computed: {
    ...mapState('submissionForm', ['deleting', 'loading', 'saving', 'form']),
    ...mapGetters('submissionForm',
      [
        'getValue',
        'formDirty',
        'getError',
        'isTouched',
        'formHasUntouchedErrors',
      ]),
    ...mapState('submission', ['errorData', 'dataRows']),
    showError() {
      return (path) => ((!this.isTouched(path) && this.getError(path)) ? false : null);
    },
    rows() {
      return this.form.rows.map((item, index) => ({ ...item, index }));
    },
  },
  methods: {
    ...mapMutations('submissionForm',
      [
        'initialize',
        'setValue',
        'setTouched',
        'setError',
        'deleteFromArray',
        'addToArray',
      ]),
    ...mapMutations('submission', ['setErrorData']),
  },
  // beforeRouteEnter(to, from, next) {
  //   next((self) => {
  //     self.initialize(self.dataRows);
  //   });
  // },
  // beforeRouteUpdate(to, from, next) {
  //   this.initialize(this.dataRows);
  //   next();
  // },
  beforeRouteLeave(to, from, next) {
    this.setErrorData(null);
    this.initialize();
    next();
  },
};
</script>
