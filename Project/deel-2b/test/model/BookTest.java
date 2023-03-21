package be.book.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class BookTest {

    private String validTitle = "The Hobbit";
    private int validNumberInStock = 1;
    private double validPrice = 25.5;

    private Book book_hobbit_inColor;
    private Book book_hobbit_notInColor;

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    @BeforeEach
    public void setUp() {
        book_hobbit_inColor = new Book(validTitle, validNumberInStock, validPrice, true);
        book_hobbit_notInColor = new Book(validTitle, validNumberInStock, validPrice, false);
    }

    // constructor happy cases

    @Test
    public void givenValidNameNumberInStockPriceInColor_whenCreatingBook_thenBookIsCreatedWithThatNameNumberInStockPriceColor() {
        assertNotNull(book_hobbit_inColor);
        assertEquals(validTitle, book_hobbit_inColor.getTitle());
        assertEquals(validNumberInStock, book_hobbit_inColor.getNumberInStock());
        assertEquals(validPrice, book_hobbit_inColor.getPrice());
        assertTrue(book_hobbit_inColor.isInColor());
        Set<ConstraintViolation<Book>> violations = validator.validate(book_hobbit_inColor);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void givenValidNameNumberInStockPriceNotInColor_whenCreatingBook_thenBookIsCreatedWithThatNameNumberInStockPriceNotInColor() {
        assertNotNull(book_hobbit_notInColor);
        assertEquals(validTitle, book_hobbit_notInColor.getTitle());
        assertEquals(validNumberInStock, book_hobbit_notInColor.getNumberInStock());
        assertEquals(validPrice, book_hobbit_notInColor.getPrice());
        assertFalse(book_hobbit_notInColor.isInColor());
        Set<ConstraintViolation<Book>> violations = validator.validate(book_hobbit_inColor);
        assertTrue(violations.isEmpty());
    }

    // constructor unhappy cases

    // empty title
    @Test
    public void givenEmptyTitle_whenCreatingBook_thenTitleViolationMessageIsThrown() {
        // when
        String emptyTitle = "   ";
        Book book = new Book(emptyTitle, validNumberInStock, validPrice);

        // then
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertEquals(violations.size(), 1);
        ConstraintViolation<Book> violation = violations.iterator().next();
        assertEquals("Title is required", violation.getMessage());
        assertEquals("title", violation.getPropertyPath().toString());
        assertEquals(emptyTitle, violation.getInvalidValue());

    }

    // short title
    @Test
    public void givenShortTitle_whenCreatingBook_thenTitleViolationMessageIsThrown() {
        // when
        String shortTitle = "a";
        Book book = new Book(shortTitle, validNumberInStock, validPrice);

        // then
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertEquals(violations.size(), 1);
        ConstraintViolation<Book> violation = violations.iterator().next();
        assertEquals("Title must have at least 3 chars", violation.getMessage());
        assertEquals("title", violation.getPropertyPath().toString());
        assertEquals(shortTitle, violation.getInvalidValue());
    }

    // number in Stock out of range
    @Test
    public void givenNumberInStockLessThan1_whenCreatingBook_thenTitleViolationMessageIsThrown() {
        // when
        int invalidValue = 0;
        Book book = new Book(validTitle, invalidValue, validPrice);
        // then
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertEquals(violations.size(), 1);
        ConstraintViolation<Book> violation = violations.iterator().next();
        assertEquals("Number in stock must be between 1 and 5", violation.getMessage());
        assertEquals("numberInStock", violation.getPropertyPath().toString());
        assertEquals(invalidValue, violation.getInvalidValue());
    }

    @Test
    public void givenNumberInStockMoreThan5_whenCreatingBook_thenTitleViolationMessageIsThrown() {
        // when
        int invalidValue = 10;
        Book book = new Book(validTitle, invalidValue, validPrice);
        // then
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertEquals(violations.size(), 1);
        ConstraintViolation<Book> violation = violations.iterator().next();
        assertEquals("Number in stock must be between 1 and 5", violation.getMessage());
        assertEquals("numberInStock", violation.getPropertyPath().toString());
        assertEquals(invalidValue, violation.getInvalidValue());
    }

    // negative price
    @Test
    public void givenNegativePrice_whenCreatingBook_thenTitleViolationMessageIsThrown() {
        // when
        double invalidValue = -10;
        Book book = new Book(validTitle, validNumberInStock, invalidValue);
        // then
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertEquals(violations.size(), 1);
        ConstraintViolation<Book> violation = violations.iterator().next();
        assertEquals("Price must be positive", violation.getMessage());
        assertEquals("price", violation.getPropertyPath().toString());
        assertEquals(invalidValue, violation.getInvalidValue());
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
