import { createFileRoute } from '@tanstack/react-router'
import DeviceEdit from "../../../../pages/controlled-vocabulary/device/DeviceEdit.tsx";

export const Route = createFileRoute('/controlled-vocabulary/device/edit/$deviceId')({
  component: RouteComponent
})

function RouteComponent() {
  const { deviceId } = Route.useParams()
  console.log(deviceId)
  return <>
    <DeviceEdit path={deviceId}/>
  </>
}
