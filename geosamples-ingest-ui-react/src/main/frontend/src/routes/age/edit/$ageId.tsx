import { createFileRoute } from '@tanstack/react-router'
import AgeEdit from "../../../pages/controlled-vocabulary/age/AgeEdit.tsx";

export const Route = createFileRoute('/age/edit/$ageId')({
  component: RouteComponent
})

function RouteComponent() {
  const { ageId } = Route.useParams()
  console.log(ageId)
  return <>
    <AgeEdit path={ageId}/>
  </>
}
