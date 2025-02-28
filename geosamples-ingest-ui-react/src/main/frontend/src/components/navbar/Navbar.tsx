import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';
import {NavLink} from "react-bootstrap";

function BasicExample() {
  return (
      <Navbar expand="lg" className="bg-body-tertiary">
        <Container>
          <Navbar.Brand href="/">Home</Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="me-auto">
              {/*TODO: Dynamically populate navbar Based on authentication? Or not with eye toward two separate applications. */}
              {/*TODO: Add router */}
              <NavDropdown title="Controlled Vocabulary" id="controlled-vocabulary-dropdown">
                <NavDropdown.Item href="geologic-age">Geologic Age</NavDropdown.Item>
                <NavDropdown.Item href="sampling-device">Sampling Device</NavDropdown.Item>
                <NavDropdown.Item href="facility">Facility</NavDropdown.Item>
                <NavDropdown.Item href="lithologic-composition">Lithologic Composition</NavDropdown.Item>
                <NavDropdown.Item href="munsell-color-code">Munsell Color Code</NavDropdown.Item>
                <NavDropdown.Item href="ship-platform">Ship/Platform</NavDropdown.Item>
                <NavDropdown.Item href="physiographic-province">Physiographic Province</NavDropdown.Item>
                <NavDropdown.Item href="rock-glass-remarks">Rock Glass Remarks & Mn/Fe Oxide</NavDropdown.Item>
                <NavDropdown.Item href="rock-lithology">Rock Lithology</NavDropdown.Item>
                <NavDropdown.Item href="rock-mineralogy">Rock Mineralogy</NavDropdown.Item>
                <NavDropdown.Item href="storage-method">Storage Method</NavDropdown.Item>
                <NavDropdown.Item href="texture">Texture</NavDropdown.Item>
                <NavDropdown.Item href="weathering-metamorphism">Weathering/Metamorphism</NavDropdown.Item>
              </NavDropdown>

              <NavDropdown title="Administration" id="administration-dropdown">
                <NavDropdown.Item href="users">Users</NavDropdown.Item>
                <NavDropdown.Item href="roles">Roles</NavDropdown.Item>
              </NavDropdown>

              <NavDropdown title="Curator Data" id="curator-data-dropdown">
                <NavDropdown.Item href="new-submission">New Submission</NavDropdown.Item>
                <NavDropdown.Item href="sample-interval">Sample + Interval</NavDropdown.Item>
                <NavDropdown.Item href="sample">Sample</NavDropdown.Item>
              </NavDropdown>

              <NavDropdown title="Ancillary Data" id="ancillary-data-dropdown">
                <NavDropdown.Item href="cruise">Cruise</NavDropdown.Item>
                <NavDropdown.Item href="cruise-link">Cruise Link</NavDropdown.Item>
                <NavDropdown.Item href="sample-link">Sample Link</NavDropdown.Item>
              </NavDropdown>

              {/*TODO: Show for provider only, do when splitting app*/}
              <NavLink href="unapproved-platforms" id="unapproved-platforms-nav-item">Unapproved Ships/Platforms</NavLink>

              <NavLink href="cruises" id="cruises-nav-item">Cruises</NavLink>

              <NavLink href="samples" id="samples-nav-item">Samples</NavLink>

              <NavLink href="subsamples-intervals" id="subsamples-intervals-nav-item">Subsamples/Intervals</NavLink>

              <NavLink href="api-docs" id="api-docs-nav-item">API Docs</NavLink>

              {/*TODO: Display only when logged in/out*/}
              <NavLink href="profile"  id="profile-nav-item">Insert Username Here</NavLink>
              <NavLink href="login-logout"  id="login-logout-nav-item">Login/Logout</NavLink>


            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>
  );
}

export default BasicExample;