const addBook = async () => {
    const title = document.getElementById("title").value;
    const numberInStock = document.getElementById("numberInStock").value;
    const price = document.getElementById("price").value;
    const inColor = document.getElementById("inColor").checked;
    const book = { title, numberInStock, price, inColor };
    document.getElementById("message").innerHTML = "";
    const response = await fetch("http://localhost:8080/api/book/add",
        {
            method: "POST",
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json",
            },
            body: JSON.stringify(book),
        });
    let message = 'Book is not added';
    try {
        const result = await response.json();
        message = `Book ${book.title} is added.`
    }
    catch (error) {
        // something went wrong in response.json()
        // probably because book with given title already exists
        console.log("Error when adding book");
    }

    document.getElementById("message").innerHTML = message;

}

document.getElementById("addBook")
    .addEventListener("submit", (event) => {
        event.preventDefault();
        addBook();
    });