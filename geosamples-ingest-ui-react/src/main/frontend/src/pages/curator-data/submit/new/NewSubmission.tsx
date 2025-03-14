import Form from 'react-bootstrap/Form';
import Button from "react-bootstrap/Button";
import {BsArrowCounterclockwise, BsSearch} from "react-icons/bs";

function NewSubmission() {
  return <p>

    <Form>
      <Form.Group controlId="formFile" className="mb-3">
        <Form.Label>Import Submission For Preview</Form.Label>
        <Form.Control type="file" />
      </Form.Group>
    </Form>
    {/*TODO: Dynamic show/hide Upload/Cancel Buttons*/}
    <Button variant="primary">
      <BsSearch/>
      Upload
    </Button>
    <Button variant="danger">
      <BsArrowCounterclockwise/>
      Cancel
    </Button>
  </p>
}

export default NewSubmission;

