import { createFileRoute } from '@tanstack/react-router'
import NewSubmission from "../../../pages/curator-data/submit/new/NewSubmission.tsx";

export const Route = createFileRoute('/curator-data/submit/new')({
  component: RouteComponent,
})

function RouteComponent() {
  return <NewSubmission/>
}
