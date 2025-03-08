import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/submit')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/submit"!</div>
}
