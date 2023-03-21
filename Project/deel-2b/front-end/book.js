const books = [];
const errors = [];

const fetchBooks = async (url) => {
    errors.length = 0;
    const response = await fetch(url);
    const result = await response.json();

    if (response.status === 400) {
        errors.push(result);
    } else {
        books.length = 0;
        books.push(...result);
    }
};

const renderBooks = () => {
    clearTableRows({ tableBody: "booksTableBody" });

    if (errors.length > 0) {
        errors.forEach((error) => {
            const fieldName = Object.keys(error);
            document.getElementById(`${fieldName}-error`).innerText =
                error[fieldName];
        });
    }

    if (books.length === 0) {
        addTableError({
            tableID: "booksTable",
            text: "No books in the library.",
        });
    } else {
        removeTableError({ tableID: "booksTable" });
        books.forEach((book) => {
            const tableRow = createTableRow();
            addTableRow({ tableBody: "booksTableBody", tableRow });
            addTableCell({ tableRow, value: book.title });
            addTableCell({ tableRow, value: book.numberInStock });
            addTableCell({ tableRow, value: book.price });
            addTableCell({ tableRow, value: book.priceInDollar.toFixed(2) });
            let button = document.createElement("button");
            button.innerHTML = "Delete";
            button.addEventListener("click", () => deleteBook(book));
            tableRow.appendChild(button);
        });
    }
};

const getMostExpensive = async () => {
    const response = await fetch(
        "http://localhost:8080/api/book/mostExpensive"
    );
    const book = await response.json();
    return book.title;
};

const getTotalValue = async () => {
    const response = await fetch("http://localhost:8080/api/book/totalValue");
    const totalValue = await response.json();
    return totalValue.toFixed(2);
};

const fetchAndRenderAllBooks = async () => {
    await fetchBooks("http://localhost:8080/api/book/all");
    renderBooks();
    clearFilters();
    clearStatistics();
    addStatistic(
        `The most expensive book is: ${
            books.length ? await getMostExpensive() : "No books in the library."
        }`
    );
    addStatistic(
        `The total value of the collection is: ${
            books.length ? await getTotalValue() : "No books in the library."
        }`
    );
};

const deleteBook = async (book) => {
    const response = await fetch(
        `http://localhost:8080/api/book/remove/${book.title}`,
        {
            method: "DELETE",
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json",
            },
        }
    );
    const result = await response.json();
    if (result != null) {
        document.getElementById(
            "message"
        ).innerText = `Book with title ${result.title} is removed`;
    }
    fetchAndRenderAllBooks();
};

document.getElementById("filterPrice").addEventListener("click", async () => {
    document.getElementById("price-filter-error").innerHTML = "";
    const search = document.getElementById("price").value;
    await fetchBooks(
        `http://localhost:8080/api/book/search/priceMoreThen?price=${search}`
    );
    renderBooks();
});

document.getElementById("filterTitle").addEventListener("click", async () => {
    document.getElementById("title-filter-error").innerHTML = "";
    const search = document.getElementById("title").value;
    await fetchBooks(`http://localhost:8080/api/book/search/title/${search}`);
    renderBooks();
});

document.getElementById("color").addEventListener("click", async () => {
    if (document.getElementById("color").checked) {
        await fetchBooks("http://localhost:8080/api/book/search/inColor");
    } else {
        await fetchBooks("http://localhost:8080/api/book/all");
    }
    renderBooks();
});

document
    .getElementById("reset")
    .addEventListener("click", fetchAndRenderAllBooks);

fetchAndRenderAllBooks();
