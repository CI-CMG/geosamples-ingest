import AddComponent from "../../../components/edit-add/AddComponent.tsx";

function DeviceAdd() {
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
                "breadcrumbLink": "/device/list",
                "breadcrumbTitle": "Sampling Device"
              },
              {
                "active": false,
                "breadcrumbLink": "/device/add",
                "breadcrumbTitle": "Add Sampling Device"
              }
            ]}
            title = "Sampling Device"
            fields={[
              {
                "label": "Sampling Device"
              },
              {
                "label": "Sampling Device Code"
              },
              {
                "label": "Source URI"
              }
            ]}></AddComponent>
      </>
  )
}

export default DeviceAdd;