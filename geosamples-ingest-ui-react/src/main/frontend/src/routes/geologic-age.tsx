import { createFileRoute } from '@tanstack/react-router'
import AgeList from '../pages/controlled-vocabulary/age/list/AgeList.tsx'

export const Route = createFileRoute('/geologic-age')({
  component: RouteComponent,
})

function RouteComponent() {
  return <AgeList/>
}
