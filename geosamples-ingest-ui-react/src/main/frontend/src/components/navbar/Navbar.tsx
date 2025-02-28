import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';
import {NavItem, NavLink} from "react-bootstrap";

function BasicExample() {
  return (
      <Navbar expand="lg" className="bg-body-tertiary">
        <Container>
          <Navbar.Brand href="#home">Home</Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="me-auto">
              {/*TODO: Dynamically populate navbar Based on authentication? Or not with eye toward two separate applications. */}

              <NavDropdown title="Controlled Vocabulary" id="controlled-vocabulary-dropdown">
                <NavDropdown.Item href="">Geologic Age</NavDropdown.Item>
                <NavDropdown.Item href="">Sampling Device</NavDropdown.Item>
                <NavDropdown.Item href="">Facility</NavDropdown.Item>
                <NavDropdown.Item href="">Lithologic Composition</NavDropdown.Item>
                <NavDropdown.Item href="">Munsell Color Code</NavDropdown.Item>
                <NavDropdown.Item href="">Ship/Platform</NavDropdown.Item>
                <NavDropdown.Item href="">Physiographic Province</NavDropdown.Item>
                <NavDropdown.Item href="">Rock Glass Remarks & Mn/Fe Oxide</NavDropdown.Item>
                <NavDropdown.Item href="">Rock Lithology</NavDropdown.Item>
                <NavDropdown.Item href="">Rock Mineralogy</NavDropdown.Item>
                <NavDropdown.Item href="">Storage Method</NavDropdown.Item>
                <NavDropdown.Item href="">Texture</NavDropdown.Item>
                <NavDropdown.Item href="">Weathering/Metamorphism</NavDropdown.Item>
              </NavDropdown>

              <NavDropdown title="Administration" id="administration-dropdown">
                <NavDropdown.Item href="">Users</NavDropdown.Item>
                <NavDropdown.Item href="">Roles</NavDropdown.Item>
              </NavDropdown>

              <NavDropdown title="Curator Data" id="curator-data-dropdown">
                <NavDropdown.Item href="">New Submission</NavDropdown.Item>
                <NavDropdown.Item href="">Sample + Interval</NavDropdown.Item>
                <NavDropdown.Item href="">Sample</NavDropdown.Item>
              </NavDropdown>

              <NavDropdown title="Ancillary Data" id="ancillary-data-dropdown">
                <NavDropdown.Item href="">Cruise</NavDropdown.Item>
                <NavDropdown.Item href="">Cruise Link</NavDropdown.Item>
                <NavDropdown.Item href="">Sample Link</NavDropdown.Item>
              </NavDropdown>

              {/*TODO: Show for provider only, do when splitting app*/}
              <NavLink href="" id="unapproved-platforms-nav-item">Unapproved Ships/Platforms</NavLink>

              <NavLink href="" id="cruises-nav-item">Cruises</NavLink>

              <NavLink href="" id="samples-nav-item">Samples</NavLink>

              <NavLink href="" id="subsamples-intervals-nav-item">Subsamples/Intervals</NavLink>

              <NavLink href="" id="api-docs-nav-item">API Docs</NavLink>

              {/*TODO: Display only when logged in/out*/}
              <NavLink href="" id="profile-nav-item">Insert Username Here</NavLink>
              <NavLink href="" id="login-logout-nav-item">Login/Logout</NavLink>


            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>
  );
}

export default BasicExample;