const clearErrors = () => {
    document.getElementById("price-error").innerHTML = "";
    document.getElementById("title-error").innerHTML = "";
    document.getElementById("numberInStock-error").innerHTML = "";
};

const addBook = async () => {
    const title = document.getElementById("title").value;
    const numberInStock = document.getElementById("numberInStock").value;
    const price = document.getElementById("price").value;
    const inColor = document.getElementById("inColor").checked;
    const book = { title, numberInStock, price, inColor };

    clearStatus();
    clearErrors();

    const response = await fetch("http://localhost:8080/api/book/add", {
        method: "POST",
        headers: {
            Accept: "application/json",
            "Content-Type": "application/json",
        },
        body: JSON.stringify(book),
    });

    const result = await response.json();
    if (response.status === 400) {
        Object.keys(result).forEach((fieldName) => {
            document.getElementById(`${fieldName}-error`).innerText =
                result[fieldName];
        });
        addStatusError("Book is not added.");
    } else {
        addStatusSuccess(`Book ${book.title} is added.`);
    }
};

document.getElementById("addBook").addEventListener("submit", (event) => {
    event.preventDefault();
    addBook();
});
