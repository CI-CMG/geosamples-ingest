import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/device')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/device"!</div>
}
