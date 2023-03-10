const clearTableRows = ({ tableBody }) =>
    (document.getElementById(tableBody).innerHTML = "");

const createTableRow = () => document.createElement("tr");

const addTableRow = ({ tableBody, tableRow }) => {
    document.getElementById(tableBody).appendChild(tableRow);
};

const addTableCell = ({ tableRow, value }) => {
    const cell = document.createElement("td");

    cell.innerHTML = value;

    tableRow.appendChild(cell);
};
