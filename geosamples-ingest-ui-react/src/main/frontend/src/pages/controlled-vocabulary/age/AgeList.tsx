import InteractiveTable from "../../../components/table/InteractiveTable.tsx";


function AgeList() {
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
            "breadcrumbLink": "/age/list",
            "breadcrumbTitle": "Geologic Age"
          }
        ]}
        fields={[
          {
            "label": "Geologic Age"
          },
          {
            "label": "Geologic Age Code"
          }
        ]}
        createText={"Geologic Age"}></InteractiveTable>
  </>
}

export default AgeList;