import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/interval')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/interval"!</div>
}
