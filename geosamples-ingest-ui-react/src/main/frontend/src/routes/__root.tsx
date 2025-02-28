import { createRootRoute, Link, Outlet } from '@tanstack/react-router'
import { TanStackRouterDevtools } from '@tanstack/router-devtools'
import Container from "react-bootstrap/Container";
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import NavDropdown from "react-bootstrap/NavDropdown";
import {NavLink} from "react-bootstrap";

export const Route = createRootRoute({
  component: () => (
      <>
        <Navbar expand="xl" className="bg-body-tertiary">
          <Container>
            <Navbar.Brand as={Link} to="/">Home</Navbar.Brand>
            <Navbar.Toggle aria-controls="basic-navbar-nav" />
            <Navbar.Collapse id="basic-navbar-nav">
              <Nav className="me-auto">
                {/*TODO: Dynamically populate navbar Based on authentication? Or not with eye toward two separate applications. */}
                {/*TODO: Add router */}
                <NavDropdown title="Controlled Vocabulary" id="controlled-vocabulary-dropdown">
                  <NavDropdown.Item as={Link} to="geologic-age">Geologic Age</NavDropdown.Item>
                  <NavDropdown.Item as={Link} to="sampling-device">Sampling Device</NavDropdown.Item>
                  <NavDropdown.Item as={Link} to="facility">Facility</NavDropdown.Item>
                  <NavDropdown.Item as={Link} to="lithologic-composition">Lithologic Composition</NavDropdown.Item>
                  <NavDropdown.Item as={Link} to="munsell-color-code">Munsell Color Code</NavDropdown.Item>
                  <NavDropdown.Item as={Link} to="ship-platform">Ship/Platform</NavDropdown.Item>
                  <NavDropdown.Item as={Link} to="physiographic-province">Physiographic Province</NavDropdown.Item>
                  <NavDropdown.Item as={Link} to="rock-glass-remarks">Rock Glass Remarks & Mn/Fe Oxide</NavDropdown.Item>
                  <NavDropdown.Item as={Link} to="rock-lithology">Rock Lithology</NavDropdown.Item>
                  <NavDropdown.Item as={Link} to="rock-mineralogy">Rock Mineralogy</NavDropdown.Item>
                  <NavDropdown.Item as={Link} to="storage-method">Storage Method</NavDropdown.Item>
                  <NavDropdown.Item as={Link} to="texture">Texture</NavDropdown.Item>
                  <NavDropdown.Item as={Link} to="weathering-metamorphism">Weathering/Metamorphism</NavDropdown.Item>
                </NavDropdown>

                <NavDropdown title="Administration" id="administration-dropdown">
                  <NavDropdown.Item as={Link} to="users">Users</NavDropdown.Item>
                  <NavDropdown.Item as={Link} to="roles">Roles</NavDropdown.Item>
                </NavDropdown>

                <NavDropdown title="Curator Data" id="curator-data-dropdown">
                  <NavDropdown.Item as={Link} to="new-submission">New Submission</NavDropdown.Item>
                  <NavDropdown.Item as={Link} to="sample-interval">Sample + Interval</NavDropdown.Item>
                  <NavDropdown.Item as={Link} to="sample">Sample</NavDropdown.Item>
                </NavDropdown>

                <NavDropdown title="Ancillary Data" id="ancillary-data-dropdown">
                  <NavDropdown.Item as={Link} to="cruise">Cruise</NavDropdown.Item>
                  <NavDropdown.Item as={Link} to="cruise-link">Cruise Link</NavDropdown.Item>
                  <NavDropdown.Item as={Link} to="sample-link">Sample Link</NavDropdown.Item>
                </NavDropdown>

                {/*TODO: Show for provider only, do when splitting app*/}
                <NavLink as={Link} to="unapproved-platforms" id="unapproved-platforms-nav-item">Unapproved Ships/Platforms</NavLink>

                <NavLink as={Link} to="cruises" id="cruises-nav-item">Cruises</NavLink>

                <NavLink as={Link} to="samples" id="samples-nav-item">Samples</NavLink>

                <NavLink as={Link} to="subsamples-intervals" id="subsamples-intervals-nav-item">Subsamples/Intervals</NavLink>

                <NavLink as={Link} to="api-docs" id="api-docs-nav-item">API Docs</NavLink>

                {/*TODO: Display only when logged in/out*/}
                <NavLink as={Link} to="profile"  id="profile-nav-item">Insert Username Here</NavLink>
                <NavLink as={Link} to="login-logout"  id="login-logout-nav-item">Login/Logout</NavLink>


              </Nav>
            </Navbar.Collapse>
          </Container>
        </Navbar>
        <hr />
        <Outlet />
        <TanStackRouterDevtools />
      </>
  ),
})
