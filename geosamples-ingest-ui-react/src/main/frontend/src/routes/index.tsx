import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/')({
  component: Index,
})

function Index() {
  return (
      <>
        <h2> Welcome to the Geosamples API </h2>
        {/*TODO: Dynamic link to version here*/}
        <p>Version X.x</p>
      </>
  )
}