import EditComponent from "../../../components/edit-add/EditComponent.tsx";

type Path = {
      path: string
}

function AgeEdit({path}: Path) {

  return (
      <>
        <EditComponent
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
                "breadcrumbLink": "/age/edit/$ageId",
                "breadcrumbTitle": "Edit Geologic Age"
              }
            ]}
            path = {path}
            title = "Geologic Age"
            fields={[
              {
                "label": "Geologic Age Code"
              },
              {
                "label": "Source URI"
              }
            ]}></EditComponent>
      </>
  )

}

export default AgeEdit;