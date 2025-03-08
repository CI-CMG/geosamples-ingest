import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/texture')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/texture"!</div>
}
