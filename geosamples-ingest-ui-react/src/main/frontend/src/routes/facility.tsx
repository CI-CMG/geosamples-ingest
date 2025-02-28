import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/facility')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/facility"!</div>
}
