import { createRootRoute, Outlet } from '@tanstack/react-router'
import { TanStackRouterDevtools } from '@tanstack/router-devtools'
import NavigationBar from "../components/navbar/Navbar.tsx"

export const Route = createRootRoute({
  component: () => (
      <>
        <NavigationBar/>
        <hr />
        <Outlet />
        <TanStackRouterDevtools />
      </>
  ),
})
