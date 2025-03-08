import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import {BsSearch} from "react-icons/bs";
import {BsArrowCounterclockwise} from "react-icons/bs";
import {BsPlusLg} from "react-icons/bs";
import Table from 'react-bootstrap/Table';
import TextPagination from "../pagination/TextPagination.tsx";
import Breadcrumb from 'react-bootstrap/Breadcrumb';
import {Link} from "@tanstack/react-router";

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
      {/*TODO: Set breadcrumb to active dynamically*/}
      {breadcrumbs.map((breadcrumb) =>
          <Breadcrumb.Item href={breadcrumb.breadcrumbLink}>{breadcrumb.breadcrumbTitle}</Breadcrumb.Item>
      )}
    </Breadcrumb>

    {/*TODO: Sort table*/}
    <Form>
      <Form.Group className="mb-3" controlId="formBasicEmail">
        {fields.map((field) =>
            <>
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
      <Link to={"/age/add"}>
      <Button variant="secondary">
        <BsPlusLg/>
        Add New {createText}
      </Button>
      </Link>
    </Form>

    {/*TODO: Insert table DATA dynamically, currently just mapping off of fields*/}
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
            <td>
              <Link to={"/age/edit/$ageId"} params={{ageId: "Cambrian"}}>
                Cambrian
              </Link>
            </td>
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

