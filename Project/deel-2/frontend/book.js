const books = [];

const renderBooks = (books) => {
    clearTableRows({ tableBody: "booksTableBody" });
    books
        .forEach(book => {
            const tableRow = createTableRow();
            addTableRow({ tableBody: "booksTableBody", tableRow });
            addTableCell({ tableRow, value: `${book.title}` });
            addTableCell({ tableRow, value: `${book.numberInStock}` });
            addTableCell({ tableRow, value: `${book.price}` });
            addTableCell({ tableRow, value: `${book.priceInDollar.toFixed(2)}` });
            let button = document.createElement("button");
            button.innerHTML = "Delete";
            button.addEventListener("click", () => deleteBook(book));
            tableRow.appendChild(button);
        });
}

const deleteBook = async (book) => {
    const response = await fetch(`http://localhost:8080/api/book/remove/${book.title}`,
        {
            method: "DELETE",
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json",
            },
        });
    const result = await response.json();
    if (result != null) {
        document.getElementById("message").innerText =
            `Book with title ${result.title} is removed`;
    }
    fetchAndRenderAllBooks();
}

const fetchBooks = async (url) => {
    const response = await fetch(url);
    const result = await response.json();
    books.length = 0;
    books.push(...result);
}

const fetchAndRenderAllBooks = async () => {
    await fetchBooks("http://localhost:8080/api/book/all");
    renderBooks(books);
}

fetchAndRenderAllBooks();

document.getElementById("filterPrice").addEventListener("click", async () => {
    const search = document.getElementById("price").value;
    await fetchBooks(`http://localhost:8080/api/book/search/priceMoreThen?price=${search}`);
    renderBooks(books);
})

document.getElementById("filterTitle").addEventListener("click", async () => {
    const search = document.getElementById("title").value;
    await fetchBooks(`http://localhost:8080/api/book/search/title/${search}`);
    renderBooks(books);
})

document.getElementById("color").addEventListener("click", async () => {
    if (document.getElementById("color").checked) {
        await fetchBooks("http://localhost:8080/api/book/search/inColor");
    }
    else {
        await fetchBooks("http://localhost:8080/api/book/all");
    }
    renderBooks(books);
}
)

document.getElementById("reset").addEventListener("click", fetchAndRenderAllBooks);

const showMostExpensive = async () => {
    const response = await fetch("http://localhost:8080/api/book/mostExpensive");
    const book = await response.json();
    document.getElementById("mostExpensive").innerHTML = book.title;
}

showMostExpensive();

const showTotalValue = async () => {
    const response = await fetch("http://localhost:8080/api/book/totalValue");
    const totalValue = await response.json();
    document.getElementById("totalValue").innerHTML = totalValue.toFixed(2);
}

showTotalValue();



