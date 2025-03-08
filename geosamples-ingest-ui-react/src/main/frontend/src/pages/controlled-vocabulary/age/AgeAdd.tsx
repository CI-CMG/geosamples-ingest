import AddComponent from "../../../components/edit-add/AddComponent.tsx";

function AgeAdd() {
  return  (
      <>
        <AddComponent
            breadcrumbs={[
              {
                "active": false,
                "breadcrumbLink": "/",
                "breadcrumbTitle": "Geosamples Ingest Home"
              },
              {
                "active": false,
                "breadcrumbLink": "/age/list",
                "breadcrumbTitle": "Geologic Age"
              },
              {
                "active": false,
                "breadcrumbLink": "/age/add",
                "breadcrumbTitle": "Add Geologic Age"
              }
            ]}
            title = "Geologic Age"
            fields={[
              {
                "label": "Geologic Age"
              },
              {
                "label": "Geologic Age Code"
              },
              {
                "label": "Source URI"
              }
            ]}></AddComponent>
      </>
  )
}

export default AgeAdd;