import { createFileRoute } from '@tanstack/react-router'
import SampleList from "../../../pages/curator-data/sample/list/SampleList.tsx";

export const Route = createFileRoute('/curator-data/sample/list')({
  component: RouteComponent,
})

function RouteComponent() {
  return <SampleList/>
}
