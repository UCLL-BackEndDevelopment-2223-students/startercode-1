package be.book.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.book.model.Book;

public class BookTest {

    private String validName = "The Hobbit";
    private int validNumberInStock = 1;
    private double validPrice = 25.5;

    private Book book_hobbit_inColor;
    private Book book_hobbit_notInColor;

    @BeforeEach
    public void setUp() {
        book_hobbit_inColor = new Book(validName, validNumberInStock, validPrice, true);
        book_hobbit_notInColor = new Book(validName, validNumberInStock, validPrice, false);
    }

    @Test
    public void givenValidNameNumberInStockPriceInColor_whenCreatingBook_thenBookIsCreatedWithThatNameNumberInStockPriceColor() {
        assertEquals(validName, book_hobbit_inColor.getTitle());
        assertEquals(validNumberInStock, book_hobbit_inColor.getNumberInStock());
        assertEquals(validPrice, book_hobbit_inColor.getPrice());
        assertTrue(book_hobbit_inColor.isInColor());
    }

    @Test
    public void givenValidNameNumberInStockPriceNotInColor_whenCreatingBook_thenBookIsCreatedWithThatNameNumberInStockPriceNotInColor() {
        assertEquals(validName, book_hobbit_notInColor.getTitle());
        assertEquals(validNumberInStock, book_hobbit_notInColor.getNumberInStock());
        assertEquals(validPrice, book_hobbit_notInColor.getPrice());
        assertFalse(book_hobbit_notInColor.isInColor());
    }

    @Test
    public void givenValidBook_whenTitleIsSet_thenBookHasNewTitle() {
        String newTitle = "Abraham";
        book_hobbit_inColor.setTitle(newTitle);
        assertEquals(newTitle, book_hobbit_inColor.getTitle());
    }

    @Test
    public void givenValidBook_whenPriceIsSet_thenBookHasNewPrice() {
        double newPrice = 999.99;
        book_hobbit_inColor.setPrice(newPrice);
        assertEquals(newPrice, book_hobbit_inColor.getPrice());
    }

    @Test
    public void givenValidBook_whenNumberInStockIsSet_thenBookHasNewNumberInStock() {
        int newNumberInStock = 99;
        book_hobbit_inColor.setNumberInStock(newNumberInStock);
        assertEquals(newNumberInStock, book_hobbit_inColor.getNumberInStock());
    }

    @Test
    public void givenValidBook_whenColorIsSetToTrue_thenBookIsInColor() {
        book_hobbit_inColor.setInColor(true);
        assertTrue(book_hobbit_inColor.isInColor());
        book_hobbit_inColor.setInColor(false);
        assertFalse(book_hobbit_inColor.isInColor());
        book_hobbit_notInColor.setInColor(true);
        assertTrue(book_hobbit_notInColor.isInColor());
        book_hobbit_notInColor.setInColor(false);
        assertFalse(book_hobbit_notInColor.isInColor());
    }

    @Test
    public void givePriceInDollar_calculates_correct_price() {
        assertEquals(validPrice * 1.06, book_hobbit_inColor.getPriceInDollar());
    }

    @Test
    public void toString_returns_correct_value_when_book_in_color() {
        String out = "The Hobbit costs €25.5. There are 1 items present. The book is in color.";
        assertEquals(out, book_hobbit_inColor.toString());
    }

    @Test
    public void toString_returns_correct_value_when_book_not_in_color() {
        String out = "The Hobbit costs €25.5. There are 1 items present. The book is not in color.";
        assertEquals(out, book_hobbit_notInColor.toString());
    }

}
