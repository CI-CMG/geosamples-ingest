import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/sample-link')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/sample-link"!</div>
}
