import { createFileRoute } from '@tanstack/react-router'
import AgeAdd from "../../../pages/controlled-vocabulary/age/AgeAdd.tsx";

export const Route = createFileRoute('/controlled-vocabulary/age/add')({
  component: RouteComponent,
})

function RouteComponent() {
  return <AgeAdd/>
}
