import EditComponent from "../../../components/edit-add/EditComponent.tsx";

type Path = {
      path: string
}

function DeviceEdit({path}: Path) {

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
                "breadcrumbLink": "/device/list",
                "breadcrumbTitle": "Sampling Device"
              },
              {
                "active": false,
                "breadcrumbLink": "/device/edit/$deviceId",
                "breadcrumbTitle": "Edit Sampling Device"
              }
            ]}
            path = {path}
            title = "Sampling Device"
            fields={[
              {
                "label": "Sampling Device Code"
              },
              {
                "label": "Source URI"
              }
            ]}></EditComponent>
      </>
  )

}

export default DeviceEdit;