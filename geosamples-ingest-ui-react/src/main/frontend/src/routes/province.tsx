import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/province')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/province"!</div>
}
