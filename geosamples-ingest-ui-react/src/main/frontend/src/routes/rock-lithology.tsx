import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/rock-lithology')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/rock-lithology"!</div>
}
