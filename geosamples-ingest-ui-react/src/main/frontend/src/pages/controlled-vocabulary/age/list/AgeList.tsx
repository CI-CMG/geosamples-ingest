import InteractiveTable from "../../../../components/table/InteractiveTable.tsx";


function AgeList() {
  return <>
    <InteractiveTable
        breadcrumbs={[
          {
            "breadcrumbLink": "/",
            "breadcrumbTitle": "Geosamples Ingest Home",
            "active": false
          },
          {
            "breadcrumbLink": "age",
            "breadcrumbTitle": "Geologic Age",
            "active": true
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
        createText={"Add New Geologic Age"}/>
  </>
}

export default AgeList;