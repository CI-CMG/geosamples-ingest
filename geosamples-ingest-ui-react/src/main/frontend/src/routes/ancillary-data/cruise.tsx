import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/ancillary-data/cruise')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/cruise"!</div>
}
