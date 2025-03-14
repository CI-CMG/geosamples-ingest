import { createFileRoute } from '@tanstack/react-router'
import DeviceAdd from "../../../pages/controlled-vocabulary/device/DeviceAdd.tsx";

export const Route = createFileRoute('/controlled-vocabulary/device/add')({
  component: RouteComponent,
})

function RouteComponent() {
  return <DeviceAdd/>
}
