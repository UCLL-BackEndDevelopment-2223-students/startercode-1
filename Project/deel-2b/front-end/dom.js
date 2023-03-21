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

const clearStatus = () => (document.getElementById("status").innerHTML = "");

const addStatusSuccess = (status) =>
    (document.getElementById("status").innerHTML =
        document.getElementById("status").innerHTML +
        "<p class='success'>" +
        status +
        "</p>");

const isStatusErrorPresent = () =>
    document.querySelectorAll("#status p.error").length > 0;

const addStatusError = (status) => {
    if (!isStatusErrorPresent())
        document.getElementById("status").innerHTML +=
            "<p class='error'>" + status + "</p>";
};

const removeStatusErrors = () =>
    document
        .querySelectorAll("#status p.error")
        .forEach((error) =>
            document.getElementById("status").removeChild(error)
        );

const addTableError = ({ tableID, text }) => {
    const table = document.getElementById(tableID);
    table.style.display = "none";
    addStatusError(text);
};

const removeTableError = ({ tableID }) => {
    const table = document.getElementById(tableID);
    table.style.display = "table";
    removeStatusErrors(table);
};

const clearStatistics = () => (document.getElementById("stats").innerHTML = "");

const clearFilters = () => {
    document.getElementById("price-filter-error").innerHTML = "";
    document.getElementById("title-filter-error").innerHTML = "";
    document.getElementById("title").value = "";
    document.getElementById("price").value = "";
    document.getElementById("color").checked = false;
};

const addStatistic = (stat) =>
    (document.getElementById("stats").innerHTML =
        document.getElementById("stats").innerHTML + "<p>" + stat + "</p>");
