import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/lithology')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/lithology"!</div>
}
