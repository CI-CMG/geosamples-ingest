import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/administration/role')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/role"!</div>
}
