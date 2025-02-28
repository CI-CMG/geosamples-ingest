import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/platform')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/platform"!</div>
}
