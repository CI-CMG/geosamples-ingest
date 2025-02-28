import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/weathering')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/weathering"!</div>
}
