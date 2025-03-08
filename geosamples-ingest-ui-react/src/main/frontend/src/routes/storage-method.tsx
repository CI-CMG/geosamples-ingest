import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/storage-method')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/storage-method"!</div>
}
