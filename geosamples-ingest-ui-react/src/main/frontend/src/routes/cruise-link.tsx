import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/cruise-link')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/cruise-link"!</div>
}
