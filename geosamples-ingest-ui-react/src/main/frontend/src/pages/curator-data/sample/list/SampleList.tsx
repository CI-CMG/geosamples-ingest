import SampleTable from "./SampleTable.tsx";

function SampleList() {

  return <>
    <SampleTable fields={[
      {
        key: 'approvalState',
        label: 'Approval State',
      },
      {
        key: 'selected',
        label: 'Selected',
      },
      {
        key: 'publish',
        label: 'Publish',
      },
      {
        key: 'imlgs',
        label: 'IMLGS',
      },
      {
        key: 'igsn',
        label: 'Sample IGSN',
      },
      {
        key: 'cruise',
        label: 'Cruise',
      },
      {
        key: 'sample',
        label: 'Sample',
      },
      {
        key: 'facilityCode',
        label: 'Facility',
      },
      {
        key: 'platform',
        label: 'Platform',
      },
      {
        key: 'deviceCode',
        label: 'Sampling Device',
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
        key: 'endLat',
        label: 'End Latitude',
      },
      {
        key: 'lon',
        label: 'Longitude',
      },
      {
        key: 'endLon',
        label: 'End Longitude',
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
        key: 'storageMethCode',
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
        key: 'provinceCode',
        label: 'Province',
      },
      {
        key: 'lake',
        label: 'Sample Lake',
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
        key: 'showSampl',
        label: 'Show Sample',
      }
    ]}></SampleTable>
  </>

}

export default SampleList;