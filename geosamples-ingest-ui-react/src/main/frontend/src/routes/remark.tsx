import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/remark')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/remark"!</div>
}
