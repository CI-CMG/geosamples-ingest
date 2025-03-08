import { createFileRoute } from '@tanstack/react-router'
import DeviceList from "../../pages/controlled-vocabulary/device/DeviceList.tsx";

export const Route = createFileRoute('/device/list')({
  component: RouteComponent,
})

function RouteComponent() {
  return <DeviceList/>
}
