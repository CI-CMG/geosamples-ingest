import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/munsell')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/munsell"!</div>
}
