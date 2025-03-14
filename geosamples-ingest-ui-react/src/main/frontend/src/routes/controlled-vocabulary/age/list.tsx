import { createFileRoute } from '@tanstack/react-router'
import AgeList from "../../../pages/controlled-vocabulary/age/AgeList.tsx";

export const Route = createFileRoute('/controlled-vocabulary/age/list')({
  component: RouteComponent,
})

function RouteComponent() {
  return <AgeList/>
}
