import Pagination from 'react-bootstrap/Pagination';

function TextPagination() {

  let activePage: number = 2;
  let itemsPerPage: number = 50;
  let totalItems: number = 101;
  let totalPages: number = Math.ceil(totalItems/itemsPerPage);
  let items = [];
  items.push()
  for (let currentPage = 1; currentPage <= totalPages; currentPage++) {
    items.push(
        <Pagination.Item key={currentPage} active={currentPage === activePage}>
          {currentPage}
        </Pagination.Item>,
    );
    // Logic for ellipses
    // Next/last disabled if at last page
  }
  return (
      <>
        <Pagination size="sm">{items}</Pagination>
      </>
)
  ;

}

export default TextPagination;