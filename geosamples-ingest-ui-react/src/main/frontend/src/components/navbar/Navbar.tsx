import Container from "react-bootstrap/Container";
import {Link} from "@tanstack/react-router";
import Nav from 'react-bootstrap/Nav';
import NavLink from 'react-bootstrap/NavLink';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';

function NavigationBar() {
  return (
      <>
        <Navbar expand="xl" className="bg-body-tertiary">
          <Container>
            <Navbar.Brand as={Link} to="/">Home</Navbar.Brand>
            <Navbar.Toggle aria-controls="basic-navbar-nav"/>
            <Navbar.Collapse id="basic-navbar-nav">
              <Nav className="me-auto">
                {/*TODO: Dynamically populate navbar Based on authentication? Or not with eye toward two separate applications. */}
                {/*TODO: Add router */}
                <NavDropdown title="Controlled Vocabulary" id="controlled-vocabulary-dropdown">
                  <NavDropdown.Item as={Link} to="controlled-vocabulary/age/list">Geologic Age</NavDropdown.Item>
                  <NavDropdown.Item as={Link} to="controlled-vocabulary/device/list">Sampling Device</NavDropdown.Item>
                  <NavDropdown.Item as={Link} to="controlled-vocabulary/facility/list">Facility</NavDropdown.Item>
                  <NavDropdown.Item as={Link} to="controlled-vocabulary/lithology/list">Lithologic Composition</NavDropdown.Item>
                  <NavDropdown.Item as={Link} to="controlled-vocabulary/munsell/list">Munsell Color Code</NavDropdown.Item>
                  <NavDropdown.Item as={Link} to="controlled-vocabulary/platform/list">Ship/Platform</NavDropdown.Item>
                  <NavDropdown.Item as={Link} to="controlled-vocabulary/province/list">Physiographic Province</NavDropdown.Item>
                  <NavDropdown.Item as={Link} to="controlled-vocabulary/remark/list">Rock Glass Remarks & Mn/Fe Oxide</NavDropdown.Item>
                  <NavDropdown.Item as={Link} to="controlled-vocabulary/rock-lithology/list">Rock Lithology</NavDropdown.Item>
                  <NavDropdown.Item as={Link} to="controlled-vocabulary/rock-mineral/list">Rock Mineralogy</NavDropdown.Item>
                  <NavDropdown.Item as={Link} to="controlled-vocabulary/storage-method/list">Storage Method</NavDropdown.Item>
                  <NavDropdown.Item as={Link} to="controlled-vocabulary/texture/list">Texture</NavDropdown.Item>
                  <NavDropdown.Item as={Link} to="controlled-vocabulary/weathering/list">Weathering/Metamorphism</NavDropdown.Item>
                </NavDropdown>

                <NavDropdown title="Administration" id="administration-dropdown">
                  <NavDropdown.Item as={Link} to="administration/user">Users</NavDropdown.Item>
                  <NavDropdown.Item as={Link} to="administration/role">Roles</NavDropdown.Item>
                </NavDropdown>

                <NavDropdown title="Curator Data" id="curator-data-dropdown">
                  <NavDropdown.Item as={Link} to="curator-data/submit/new">New Submission</NavDropdown.Item>
                  <NavDropdown.Item as={Link} to="curator-data/interval/list">Sample + Interval</NavDropdown.Item>
                  <NavDropdown.Item as={Link} to="curator-data/sample/list">Sample</NavDropdown.Item>
                </NavDropdown>

                <NavDropdown title="Ancillary Data" id="ancillary-data-dropdown">
                  <NavDropdown.Item as={Link} to="ancillary-data/cruise">Cruise</NavDropdown.Item>
                  <NavDropdown.Item as={Link} to="ancillary-data/cruise-link">Cruise Link</NavDropdown.Item>
                  <NavDropdown.Item as={Link} to="ancillary-data/sample-link">Sample Link</NavDropdown.Item>
                </NavDropdown>

                {/*TODO: Show for provider only, do when splitting app*/}
                <NavLink as={Link} to="unapproved-platforms" id="unapproved-platforms-nav-item">Unapproved Ships/Platforms</NavLink>

                <NavLink as={Link} to="cruises" id="cruises-nav-item">Cruises</NavLink>

                <NavLink as={Link} to="samples" id="samples-nav-item">Samples</NavLink>

                <NavLink as={Link} to="subsamples-intervals" id="subsamples-intervals-nav-item">Subsamples/Intervals</NavLink>

                <NavLink as={Link} to="api-docs" id="api-docs-nav-item">API Docs</NavLink>

                {/*TODO: Display only when logged in/out*/}
                <NavLink as={Link} to="profile" id="profile-nav-item">Insert Username Here</NavLink>
                <NavLink as={Link} to="login-logout" id="login-logout-nav-item">Login/Logout</NavLink>
              </Nav>
            </Navbar.Collapse>
          </Container>
        </Navbar>
      </>
  )
}

export default NavigationBar;
