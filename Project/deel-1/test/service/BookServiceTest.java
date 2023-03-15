package be.book.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.book.domain.Book;

public class BookServiceTest {
    private BookService service;
    private Book aBookDonQuichot = new Book("Don Quichotte", 3, 20.7, true);
    private Book aBookHarryPotter = new Book("Harry Potter", 5, 20.07, false);
    private Book aBookJaneEyre = new Book("Jane Eyre", 4, 33.02, true);
    private Book aBookTheHobbit = new Book("The Hobbit", 1, 50.5, true);
    private Book aBookToKillAMockingbird = new Book("To kill a Mockingbird", 1, 35.03, false);
    private Book aBookTheSilentPatient = new Book("The Silent Patient", 7, 60, true);
    private Book anExtraBook = new Book("The Extra", 3, 22.00, false);

    @BeforeEach
    public void initService() {
        // create new Bookservice
        service = new BookService();
        // and delete all books if any
        service.getBooks().clear();
    }

    public void fillServiceWithBooks() {
        // given
        service.addBook(aBookDonQuichot);
        service.addBook(aBookHarryPotter);
        service.addBook(aBookJaneEyre);
        service.addBook(aBookToKillAMockingbird);
        service.addBook(aBookTheHobbit);
        service.addBook(aBookTheSilentPatient);

    }

    @Test
    public void given6Books_whenNewBookWithNotAlreadyUsedTitleIsAdded_thenBookIsAdded() {
        // given
        fillServiceWithBooks();
        assertEquals(6, service.getBooks().size());
        // when
        boolean added = service.addBook(anExtraBook);
        // then
        assertTrue(added);
        assertEquals(7, service.getBooks().size());
        assertTrue(service.getBooks().contains(anExtraBook));
        assertTrue(service.getBooks().contains(aBookTheHobbit));
    }

    @Test
    public void given6Books_whenNewBookWithTitleAlreadyUsedIsAdded_thenBookIsNotAdded() {
        // given
        fillServiceWithBooks();
        assertEquals(6, service.getBooks().size());
        // when
        Book anOtherHobbit = new Book("The Hobbit", 10, 225.5, false);
        boolean added = service.addBook(anOtherHobbit);
        // then
        assertFalse(added);
        assertEquals(6, service.getBooks().size());
        assertTrue(service.getBooks().contains(aBookTheHobbit));
        assertFalse(service.getBooks().contains(anOtherHobbit));
    }

    @Test
    public void given6Books_whenNullBookIsAdded_thenBookIsNotAdded() {
        // given
        fillServiceWithBooks();
        assertEquals(6, service.getBooks().size());
        // when
        boolean added = service.addBook(null);
        // then
        assertFalse(added);
        assertEquals(6, service.getBooks().size());
    }

    @Test
    public void getTotalValueOfCollection_returns_correct_value() {
        fillServiceWithBooks();
        assertEquals(800.06, service.getTotalValueOfCollection());
    }

    @Test
    public void getMostExpensiveBook_returns_most_expensive_when_service_contains_books() {
        fillServiceWithBooks();
        assertEquals(aBookTheSilentPatient, service.getMostExpensiveBook());
    }

    @Test
    public void getMostExpensiveBook_returns_null_when_no_books_in_service() {
        assertNull(service.getMostExpensiveBook());
    }

    @Test
    public void getBooksWithPriceMoreThen_returns_list_when_price_positive_and_books_are_found() {
        // given
        fillServiceWithBooks();
        // when
        List<Book> books = service.getBooksWithPriceMoreThen(50);
        // then
        assertEquals(2, books.size());
        assertTrue(books.contains(aBookTheSilentPatient));
        assertTrue(books.contains(aBookTheHobbit));
    }

    @Test
    public void getBooksWithPriceMoreThen_returns_all_books_when_price_negative_and_books_in_list() {
        fillServiceWithBooks();
        List<Book> books = service.getBooksWithPriceMoreThen(Integer.MIN_VALUE);
        assertEquals(service.getBooks().size(), books.size());
    }

    @Test
    public void getBooksWithPriceMoreThen_returns_empty_list_when_price_positive_and_no_books_are_found() {
        fillServiceWithBooks();
        List<Book> books = service.getBooksWithPriceMoreThen(300);
        assertTrue(books.isEmpty());
    }

    @Test
    public void getBooksWithPriceMoreThen_returns_empty_list_when_service_is_empty() {
        List<Book> books = service.getBooksWithPriceMoreThen(0);
        assertTrue(books.isEmpty());
    }

    @Test
    public void getBookWithTitle_returns_book_when_book_in_service() {
        fillServiceWithBooks();
        assertEquals(aBookTheHobbit, service.getBookWithTitle(aBookTheHobbit.getTitle()));
        assertEquals(aBookTheSilentPatient, service.getBookWithTitle(aBookTheSilentPatient.getTitle()));
    }

    @Test
    public void getBookWithTitle_returns_null_when_book_not_in_service() {
        fillServiceWithBooks();
        assertNull(service.getBookWithTitle("abc"));
    }

    @Test
    public void getBookWithTitle_returns_null_when_title_is_null() {
        fillServiceWithBooks();
        assertNull(service.getBookWithTitle(null));
    }

    @Test
    public void getBooksInColor_returns_all_books_when_colored_books_are_present() {
        fillServiceWithBooks();
        List<Book> books_in_color = service.getBooksInColor();
        assertEquals(4, books_in_color.size());
        assertTrue(books_in_color.contains(aBookTheSilentPatient));
        assertTrue(books_in_color.contains(aBookTheHobbit));
    }

    @Test
    public void getBooksInColor_returns_empty_list_when_no_colored_books_are_present() {
        service.addBook(aBookHarryPotter);
        List<Book> books_in_color = service.getBooksInColor();
        assertTrue(books_in_color.isEmpty());
    }

    @Test
    public void removeBook_book_returns_deleted_book_when_present() {
        fillServiceWithBooks();

        assertEquals(aBookHarryPotter, service.removeBook(aBookHarryPotter));
        assertEquals(5, service.getBooks().size());
        assertNull(service.getBookWithTitle(aBookHarryPotter.getTitle()));
    }

    @Test
    public void removeBook_book_returns_null_when_book_not_in_service() {
        fillServiceWithBooks();

        assertNull(service.removeBook(anExtraBook));
        assertEquals(6, service.getBooks().size());
    }

    @Test
    public void removeBook_book_returns_null_when_empty_service() {
        assertNull(service.removeBook(anExtraBook));
    }

    @Test
    public void removeBook_title_returns_deleted_book_when_present() {
        fillServiceWithBooks();

        assertEquals(aBookHarryPotter, service.removeBookWithTitle(aBookHarryPotter.getTitle()));
        assertEquals(5, service.getBooks().size());
        assertNull(service.getBookWithTitle(aBookHarryPotter.getTitle()));
    }

    @Test
    public void removeBook_title_returns_null_when_book_not_in_service() {
        fillServiceWithBooks();

        assertNull(service.removeBookWithTitle(anExtraBook.getTitle()));
        assertEquals(6, service.getBooks().size());
    }

    @Test
    public void removeBook_title_returns_null_when_empty_service() {
        assertNull(service.removeBookWithTitle(anExtraBook.getTitle()));
    }
}
