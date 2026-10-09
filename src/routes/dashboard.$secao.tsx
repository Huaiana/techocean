import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/dashboard/$secao')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/dashboard/$secao"!</div>
}
