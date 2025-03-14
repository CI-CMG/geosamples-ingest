import IntervalTable from "./IntervalTable.tsx";

function IntervalList() {

  return <>
    <IntervalTable fields={[
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
        key: 'interval',
        label: 'Interval',
      },
      {
        key: 'igsn',
        label: 'Sample IGSN',
      },
      {
        key: 'intervalIgsn',
        label: 'Interval IGSN',
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
        key: 'facility',
        label: 'Facility',
      },
      {
        key: 'platform',
        label: 'Platform',
      },
      {
        key: 'device',
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
        key: 'storageMeth',
        label: 'Storage Meth.',
      },
      {
        key: 'dhCoreId',
        label: 'DH Core ID',
      },
      {
        key: 'dhCoreLength',
        label: 'DH Core Length',
      },
      {
        key: 'dhCoreLengthMm',
        label: 'DH Core Length (mm)',
      },
      {
        key: 'dhCoreInterval',
        label: 'DH Core Interval',
      },
      {
        key: 'dTopInDhCore',
        label: 'D Top In DH Core',
      },
      {
        key: 'dTopMmInDhCore',
        label: 'D Top In DH Core (mm)',
      },
      {
        key: 'dBotInDhCore',
        label: 'D Bot In DH Core',
      },
      {
        key: 'dBotMmInDhCore',
        label: 'D Bot In DH Core (mm)',
      },
      {
        key: 'dhDevice',
        label: 'DH Device',
      },
      {
        key: 'cmcdTop',
        label: 'CD Top (cm)',
      },
      {
        key: 'mmcdTop',
        label: 'CD Top (mm)',
      },
      {
        key: 'cmcdBot',
        label: 'CD Bot (cm)',
      },
      {
        key: 'mmcdBot',
        label: 'CD Bot (mm)',
      },
      {
        key: 'absoluteAgeTop',
        label: 'Absolute Age Top',
      },
      {
        key: 'absoluteAgeBot',
        label: 'Absolute Age Bottom',
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
        key: 'province',
        label: 'Province',
      },
      {
        key: 'sampleLake',
        label: 'Sample Lake',
      },
      {
        key: 'lastUpdate',
        label: 'Sample Last Update',
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
        key: 'sampleComments',
        label: 'Sample Comments',
      },
      {
        key: 'showSampl',
        label: 'Show Sample',
      },
      {
        key: 'depthTop',
        label: 'Depth Top',
      },
      {
        key: 'depthTopMm',
        label: 'Depth Top (mm)',
      },
      {
        key: 'depthBot',
        label: 'Depth Bot.',
      },
      {
        key: 'depthBotMm',
        label: 'Depth Bot. (mm)',
      },
      {
        key: 'lith1',
        label: 'Primary Lithologic Composition',
      },
      {
        key: 'text1',
        label: 'Text. 1',
      },
      {
        key: 'lith2',
        label: 'Secondary Lithologic Composition',
      },
      {
        key: 'text2',
        label: 'Text. 2',
      },
      {
        key: 'comp1',
        label: 'Other Component 1',
      },
      {
        key: 'comp2',
        label: 'Other Component 2',
      },
      {
        key: 'comp3',
        label: 'Other Component 3',
      },
      {
        key: 'comp4',
        label: 'Other Component 4',
      },
      {
        key: 'comp5',
        label: 'Other Component 5',
      },
      {
        key: 'comp6',
        label: 'Other Component 6',
      },
      {
        key: 'description',
        label: 'Description',
      },
      {
        key: 'ages',
        label: 'Geologic Ages',
      },
      {
        key: 'weight',
        label: 'Weight',
      },
      {
        key: 'rockLith',
        label: 'Rock Lith.',
      },
      {
        key: 'rockMin',
        label: 'Rock Min.',
      },
      {
        key: 'weathMeta',
        label: 'Weath./Meta.',
      },
      {
        key: 'remark',
        label: 'Rock Glass Remarks & Mn/Fe Oxide',
      },
      {
        key: 'munsellCode',
        label: 'Munsell Color Code',
      },
      {
        key: 'munsell',
        label: 'Munsell Color',
      },
      {
        key: 'exhaustCode',
        label: 'Exhaust Code',
      },
      {
        key: 'photoLink',
        label: 'Photo Link',
      },
      {
        key: 'intComments',
        label: 'Int. Comments',
      },
    ]}></IntervalTable>
  </>
}

export default IntervalList;

