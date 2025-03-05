import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import {BsSearch} from "react-icons/bs";
import {BsArrowCounterclockwise} from "react-icons/bs";
import {BsPlusLg} from "react-icons/bs";
import Table from 'react-bootstrap/Table';
import TextPagination from "../pagination/TextPagination.tsx";
import Breadcrumb from 'react-bootstrap/Breadcrumb';

type Breadcrumb = {
  breadcrumbLink: string;
  breadcrumbTitle: string;
  active: boolean
}

type Field = {
  label: string
}
type InteractiveTableProps = {
  breadcrumbs: Breadcrumb[],
  fields: Field[],
  createText: string
}

function InteractiveTable({breadcrumbs, fields, createText}: InteractiveTableProps) {
  return <p>
    <Breadcrumb>
      {breadcrumbs.map((breadcrumb) =>
          <Breadcrumb.Item href={breadcrumb.breadcrumbLink}>{breadcrumb.breadcrumbTitle}</Breadcrumb.Item>
      )}
    </Breadcrumb>

    {/*TODO: Sort table*/}
    <Form>
      <Form.Group className="mb-3" controlId="formBasicEmail">
        {fields.map((field) =>
            <>
              {/*TODO: Set breadcrumb to active dynamically*/}
            <Form.Label>{field.label}</Form.Label>
            <Form.Control type="insert-type"/>
            </>
          )}
      </Form.Group>
      <Button variant="primary">
        <BsSearch/>
        Search
      </Button>
      <Button variant="danger">
        <BsArrowCounterclockwise/>
        Clear
      </Button>
      <Button variant="secondary">
        <BsPlusLg/>
        {createText}
      </Button>
    </Form>

    {/*TODO: Insert table DATA dynamically*/}
    <Table striped bordered hover>
      <thead>
      <tr>
        {fields.map((field) =>
            <th>{field.label}</th>
        )}
      </tr>
      </thead>
      <tbody>
      <tr>
        {fields.map((field) =>
            <td>(Insert Column Data)</td>
        )}
      </tr>
      </tbody>
    </Table>

    {/*TODO: Pagination*/}
    <TextPagination/>

    {/* TODO: Approval (?)*/}
  </p>
}

export default InteractiveTable;

