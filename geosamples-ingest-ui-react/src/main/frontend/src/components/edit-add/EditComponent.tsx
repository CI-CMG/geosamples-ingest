import Breadcrumb from "react-bootstrap/Breadcrumb";
import Button from "react-bootstrap/Button";
import Form from 'react-bootstrap/Form';

type Breadcrumb = {
  breadcrumbLink: string;
  breadcrumbTitle: string;
  active: boolean
}

type Field = {
  label: string
}

type EditComponentProps = {
  breadcrumbs: Breadcrumb[],
  title: string,
  fields: Field[],
  path: string
}

function AgeEdit({breadcrumbs, title, fields, path}: EditComponentProps) {

  return (
      <>
        <Breadcrumb>
          {/*TODO: Set breadcrumb to active dynamically*/}
          {breadcrumbs.map((breadcrumb) =>
              <Breadcrumb.Item href={breadcrumb.breadcrumbLink}>{breadcrumb.breadcrumbTitle}</Breadcrumb.Item>
          )}
        </Breadcrumb>
        <h1>Edit {title} - {path}</h1>
        <Button variant="danger">Delete</Button>
        <Form>
          <Form.Group className="mb-3">
            <Form.Label>{fields[0].label}</Form.Label>
            <Form.Control/>
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>{fields[1].label}</Form.Label>
            <Form.Control/>
          </Form.Group>
          {/*TODO: Show buttons only on text edit*/}
          <Button variant="primary" type="submit">Save</Button>
          <Button variant="danger">Reset</Button>
        </Form>
      </>
  )

}

export default AgeEdit;