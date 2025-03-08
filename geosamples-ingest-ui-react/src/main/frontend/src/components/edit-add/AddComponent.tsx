import Breadcrumb from "react-bootstrap/Breadcrumb";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";

type Breadcrumb = {
  breadcrumbLink: string;
  breadcrumbTitle: string;
  active: boolean
}

type Field = {
  label: string
}

type AddComponentProps = {
  breadcrumbs: Breadcrumb[],
  title: string,
  fields: Field[],
}

function AddComponent({breadcrumbs, title, fields} : AddComponentProps) {
  return (
      <>
        <Breadcrumb>
          {/*TODO: Set breadcrumb to active dynamically*/}
          {breadcrumbs.map((breadcrumb) =>
              <Breadcrumb.Item href={breadcrumb.breadcrumbLink}>{breadcrumb.breadcrumbTitle}</Breadcrumb.Item>
          )}
        </Breadcrumb>
  <h1>Add New {title}</h1>
  <Form>
    <Form.Group className="mb-3" controlId="formGeologicAge">
      <Form.Label>{fields[0].label}</Form.Label>
      <Form.Control/>
    </Form.Group>

    <Form.Group className="mb-3" controlId="formGeologicAgeCode">
      <Form.Label>{fields[1].label}</Form.Label>
      <Form.Control/>
    </Form.Group>

    <Form.Group className="mb-3" controlId="formSourceURI">
      <Form.Label>{fields[2].label}</Form.Label>
      <Form.Control/>
    </Form.Group>
    {/*TODO: Show buttons only on text edit*/}
    <Button variant="primary" type="submit">Save</Button>
    <Button variant="danger">Reset</Button>
  </Form>
      </>
  )
}

export default AddComponent;