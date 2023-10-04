import React, { useState } from 'react'
import './Paging.css';
import Pagination from "react-js-pagination";

function Paging({page, count, setPage}) {
    console.log("current page",page);
  return (
    <Pagination
      activePage={page}
      itemsCountPerPage={5}
      totalItemsCount={count}
      pageRangeDisplayed={5}
      prevPageText={"‹"}
      nextPageText={"›"}
      onChange={setPage}
    />
  )
}

export default Paging