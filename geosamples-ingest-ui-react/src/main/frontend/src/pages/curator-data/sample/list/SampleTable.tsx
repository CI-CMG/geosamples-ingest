import Breadcrumb from "react-bootstrap/Breadcrumb";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import {BsDownload, BsSearch, BsSortUp} from "react-icons/bs";
import Table from "react-bootstrap/Table";
import TextPagination from "../../../../components/pagination/TextPagination.tsx";

type Field = {
  key: string,
  label: string
}

type IntervalListProps = {
  fields: Field[],
}

function SampleTable({fields} : IntervalListProps) {
  return <p>
    <Breadcrumb>
      <Breadcrumb.Item href={"/"}>Geosamples Ingest</Breadcrumb.Item>
      <Breadcrumb.Item href={"/"} active={true}>Sample</Breadcrumb.Item>
    </Breadcrumb>

    {/*TODO: Sort table*/}
    <Form>
      <Button variant="secondary">
        <BsSearch/>
        Search
      </Button>
      <Button variant="secondary">
        <BsSortUp/>
        Sort
      </Button>
      <Button variant="secondary">
        <BsDownload />
        Download
      </Button>
    </Form>

    {/*TODO: Insert table DATA dynamically, currently just mapping off of fields*/}
    <Button variant="secondary">
      Select All
    </Button>
    <Button variant="secondary">
      Deselect All
    </Button>
    <Button variant="primary">
      Publish Selected
    </Button>
    <Button variant="primary">
      Unpublish Selected
    </Button>
    <Button variant="danger">
      Delete Selected Intervals
    </Button>
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
        {fields.map(() =>
            <td>
            </td>
        )}
      </tr>
      </tbody>
    </Table>

    {/*TODO: Pagination*/}
    <TextPagination/>

  </p>
}

export default SampleTable;

