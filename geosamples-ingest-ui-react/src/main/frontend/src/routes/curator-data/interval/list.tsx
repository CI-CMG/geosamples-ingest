import { createFileRoute } from '@tanstack/react-router'
import IntervalList from "../../../pages/curator-data/interval/list/IntervalList.tsx";

export const Route = createFileRoute('/curator-data/interval/list')({
  component: RouteComponent,
})

function RouteComponent() {
  return <IntervalList/>
}
