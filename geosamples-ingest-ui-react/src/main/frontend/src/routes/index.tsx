import { createFileRoute } from '@tanstack/react-router'
import Homepage from "../pages/homepage/Homepage.tsx";

export const Route = createFileRoute('/')({
  component: Index,
})

function Index() {
  return (
      <>
        <Homepage/>
      </>
  )
}