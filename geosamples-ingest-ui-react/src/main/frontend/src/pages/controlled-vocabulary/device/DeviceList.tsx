import InteractiveTable from "../../../components/table/InteractiveTable.tsx";


function DeviceList() {
  return <>
    <InteractiveTable
        breadcrumbs={[
          {
            "active": false,
            "breadcrumbLink": "/",
            "breadcrumbTitle": "Geosamples Ingest Home"
          },
          {
            "active": true,
            "breadcrumbLink": "/device/list",
            "breadcrumbTitle": "Sampling Device"
          }
        ]}
        fields={[
          {
            "label": "Sampling Device"
          },
          {
            "label": "Sampling Device Code"
          }
        ]}
        createText={"Sampling Device"}></InteractiveTable>
  </>
}

export default DeviceList;