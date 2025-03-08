import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/rock-mineral')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/rock-mineral"!</div>
}
