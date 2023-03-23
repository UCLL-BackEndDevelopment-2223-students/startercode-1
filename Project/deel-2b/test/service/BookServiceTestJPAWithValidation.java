package be.book.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import be.book.model.Book;
import be.book.repo.BookRepository;

@ExtendWith(MockitoExtension.class)
public class BookServiceTestJPAWithValidation {

    @Mock
    BookRepository bookRepository;

    @InjectMocks
    BookService service;

    private Book aBookDonQuichot = new Book("Don Quichotte", 3, 20.7, true);
    private Book aBookHarryPotter = new Book("Harry Potter", 5, 20.07, false);
    private Book aBookJaneEyre = new Book("Jane Eyre", 4, 33.02, true);
    private Book aBookTheHobbit = new Book("The Hobbit", 1, 50.5, true);
    private Book aBookToKillAMockingbird = new Book("To kill a Mockingbird", 1, 35.03, false);
    private Book aBookTheSilentPatient = new Book("The Silent Patient", 7, 60, true);
    private Book anExtraBook = new Book("The Extra", 3, 22.00, false);

    private List<Book> giveListWithBooks() {
        List<Book> list = new ArrayList<>();
        list.add(aBookDonQuichot);
        list.add(aBookHarryPotter);
        list.add(aBookJaneEyre);
        list.add(aBookToKillAMockingbird);
        list.add(aBookTheHobbit);
        list.add(aBookTheSilentPatient);
        return list;
    }

    @Test
    public void givenBooksWithBookWithTitleDonQuichot_whenGetBookWithTitleDonQuichot_thenBookIsReturned()  throws ServiceException {
        // given
        when (bookRepository.findBookByTitle("Don Quichotte")).thenReturn(aBookDonQuichot);
        // when
        Book foundBook = service.getBookWithTitle("Don Quichotte");
        // then
        assertEquals(foundBook.getTitle(), "Don Quichotte");
    }

    @Test
    public void givenBooksWithNoBookWithTitleDonQuichot_whenGetBookWithTitleDonQuichot_thenNullIsReturned()  throws ServiceException{
         when (bookRepository.findBookByTitle("Don Quichotte")).thenReturn(null);

         Book foundBook = service.getBookWithTitle("Don Quichotte");

         assertNull(foundBook);
    }

    @Test
    public void givenBooks_whenGetBookWithTitleTooShort_thenServiceErrorIsThrown() {
        // given
        String shortTitle = "xy";
        // when
        ServiceException ex = Assertions.assertThrows(ServiceException.class,
                () -> service.getBookWithTitle(shortTitle));
        // then
        assertEquals("Title must have at least 3 chars", ex.getMessage());
        assertEquals("title-filter", ex.getField());
    }

    @Test
    public void givenNoBooks_whenNewBookWithNotAlreadyUsedTitleIsAdded_thenBookIsAdded()  throws ServiceException{
        // given
        when(bookRepository.save(anExtraBook)).thenReturn(anExtraBook);
        when(bookRepository.findBookByTitle(anExtraBook.getTitle())).thenReturn(null);
        // when
        Book addedBook = service.addBook(anExtraBook);
        // then
        assertEquals(addedBook.getTitle(),anExtraBook.getTitle());
    }

    @Test
    public void givenBooks_whenNewBookWithTitleAlreadyUsedIsAdded_thenServiceExceptionIsThrown() {
        // given
        when(bookRepository.findBookByTitle(anExtraBook.getTitle())).thenReturn(anExtraBook);
        // when
        ServiceException ex = Assertions.assertThrows(ServiceException.class, ()->service.addBook(anExtraBook));
        // then
        assertEquals("Title must be unique", ex.getMessage());
        assertEquals("title", ex.getField()); 
    }

    @Test
    public void givenBooks_whenNullBookIsAdded_thenExceptionIsThrown() {
        // given
        Book nullBook = null;
        // when
        ServiceException ex = Assertions.assertThrows(ServiceException.class, () -> service.addBook(nullBook));
        // then
        assertEquals("Can not add null book", ex.getMessage());
        assertEquals("book", ex.getField());
    }

    @Test
    public void getTotalValueOfCollection_returns_correct_value() {
        List<Book> books = giveListWithBooks();
        // given
        when(bookRepository.findAll()).thenReturn(books);
        // when
        double totalValue = service.getTotalValueOfCollection();
        // then
        assertEquals(800.06, totalValue);
    }

    @Test
    public void getMostExpensiveBook_returns_most_expensive_when_service_contains_books() {
        List<Book> books = giveListWithBooks();
        // given
        when(bookRepository.findAll()).thenReturn(books);
        // when
        Book mostExpensive = service.getMostExpensiveBook();
        // then
        assertEquals(aBookTheSilentPatient, mostExpensive);
    }

    @Test
    public void getMostExpensiveBook_returns_null_when_no_books_in_service() {
        // given
        when(bookRepository.findAll()).thenReturn(new ArrayList<>());
        // when
        Book mostExpensive = service.getMostExpensiveBook();
        // then    
        assertNull(mostExpensive);

}

    @Test
    public void getBooksWithPriceMoreThen_returns_list_when_price_positive_and_books_are_found()
            throws ServiceException {
        // given
        List<Book> booksWithPriceMoreThen = new ArrayList<>();
        booksWithPriceMoreThen.add(aBookTheSilentPatient);
        when(bookRepository.findBooksByPriceGreaterThan(50)).thenReturn(booksWithPriceMoreThen);
        // when
        List<Book> result = service.getBooksWithPriceMoreThan(50);
        // then
        assertEquals(booksWithPriceMoreThen.size(), result.size());
        assertTrue(result.contains(aBookTheSilentPatient));
        assertFalse(result.contains(aBookTheHobbit));
    }

    @Test
    public void getBooksWithPriceMoreThan_whenPriceIsNegative_thenServiceExceptionIsThrown() {
        // when
        ServiceException ex = Assertions.assertThrows(ServiceException.class,
                () -> service.getBooksWithPriceMoreThan(Integer.MIN_VALUE));
        // then
        assertEquals("price-filter", ex.getField());
        assertEquals("Price must be a non-negative number", ex.getMessage());

    }

    @Test
    public void getBooksWithPriceMoreThan_whenPriceIs0AndBooksInList_thenAllBooksAreReturned() throws ServiceException {
        List<Book> books = giveListWithBooks();
        // given -> replace if necessary
        when(bookRepository.findBooksByPriceGreaterThan(0)).thenReturn(books);
        // when
        List<Book> result = service.getBooksWithPriceMoreThan(0);
        // then
        assertEquals(result.size(), books.size());
    }

    @Test
    public void getBooksWithPriceMoreThen_returns_empty_list_when_price_positive_and_no_books_are_found()  throws ServiceException{
        // given -> replace if necessary
        when(bookRepository.findBooksByPriceGreaterThan(300)).thenReturn(new ArrayList<>());
        // when
        List<Book> result = service.getBooksWithPriceMoreThan(300);
        // then
        assertTrue(result.isEmpty());
    }

    @Test
    public void getBooksInColor_returns_all_books_when_colored_books_are_present() {
        List<Book> books_in_color = new ArrayList<>();
        books_in_color.add(aBookDonQuichot);
        // given -> replace if necessary
        when(bookRepository.findBooksByinColorIsTrue()).thenReturn(books_in_color);
        // when
        List<Book> result = service.getBooksInColor();
        // then
        assertTrue(result.contains(aBookDonQuichot));
        assertFalse(books_in_color.contains(aBookTheHobbit));
    }

    @Test
    public void
    getBooksInColor_returns_empty_list_when_no_colored_books_are_present() {
        // given -> replace if necessary
        when(bookRepository.findBooksByinColorIsTrue()).thenReturn(new ArrayList<>());
         // when
         List<Book> result = service.getBooksInColor();
         // then
         assertEquals(0,result.size());
    }

    @Test
    public void givenBookInRepo_whenBookRemovedByTitle_thenRemovedBookIsReturned()  throws ServiceException{
        // given
        when(bookRepository.findBookByTitle("Harry Potter")).thenReturn(aBookHarryPotter);
        // when
        Book result = service.removeBook(aBookHarryPotter.getTitle());
        // then
        assertEquals(aBookHarryPotter.getTitle(), result.getTitle());
    }

    @Test
    public void givenBookNotInRepo_whenBookRemovedByTitle_thenServiceExceptionIsThrown() {
        // given
        when(bookRepository.findBookByTitle("Harry Potter")).thenReturn(null);
        // when
        ServiceException ex = Assertions.assertThrows(ServiceException.class, ()-> service.removeBook("Harry Potter"));
        // then
        assertEquals("title", ex.getField());       
        assertEquals("Book with given title does not exist", ex.getMessage());

    }

    @Test
    public void givenBookInRepo_whenBookSearchedById_thenBookIsReturned() {
        // given
        when(bookRepository.findBookById(0)).thenReturn(aBookDonQuichot);
        // when
        Book result = service.getBookWithId(0);
        // then
        assertEquals(result.getTitle(), aBookDonQuichot.getTitle());
    }

    @Test
    public void givenBookNotInRepo_whenBookSearchedById_thenNullIsReturned() {
        // given
        when(bookRepository.findBookById(0)).thenReturn(null);
        // when
        Book result = service.getBookWithId(0);
        // then
        assertNull(result);
    }

    @Test
    public void givenBookInRepo_whenBookRemovedById_thenRemovedBookIsReturned()  throws ServiceException{
        // given
        when(bookRepository.findBookById(0)).thenReturn(aBookDonQuichot);
        // when
        Book result = service.removeBook(0);
        // then
        assertEquals(aBookDonQuichot.getTitle(), result.getTitle());
    }

    @Test
    public void givenBookNotInRepo_whenBookRemovedById_thenServiceExceptionIsThrown() {
        // given
        when(bookRepository.findBookById(0)).thenReturn(null);
        // when
        ServiceException ex = Assertions.assertThrows(ServiceException.class, ()-> service.removeBook(0));
        // then
        assertEquals("id", ex.getField());
        assertEquals("Book with id 0 does not exist.", ex.getMessage());

    }

    @Test
    public void givenBooksInRepo_whenBooksAreOrderedAscByTitle_thenCorrectListIsReturned() throws ServiceException {
        List<Book> books = giveListWithBooks();
        // given -> replace if necessary
        when(bookRepository.findAll(Sort.by(Sort.Direction.ASC, "title"))).thenReturn(books);
        // when
        List<Book> result = service.orderBooks("asc", "title");
        // then
        assertEquals(books, result);

    }

    @Test
    public void givenBooksInRepo_whenBooksAreOrderedDescByPrice_thenCorrectListIsReturned() throws ServiceException {
        List<Book> books = giveListWithBooks();
        // given -> replace if necessary
        when(bookRepository.findAll(Sort.by(Sort.Direction.DESC, "price"))).thenReturn(books);
        // when
        List<Book> result = service.orderBooks("desc", "price");
        // then
        assertEquals(books, result);

    }

    @Test
    public void givenBooksInRepo_whenBooksAreOrderedUnknownByTitle_thenBooksAreOrderedDesc() throws ServiceException {
        List<Book> books = giveListWithBooks();
        // given -> replace if necessary
        when(bookRepository.findAll(Sort.by(Sort.Direction.DESC, "title"))).thenReturn(books);
        // when
        List<Book> result = service.orderBooks("unknown", "title");
        // then
        assertEquals(books, result);
    }

    @Test
    public void givenBooksInRepo_whenBooksAreOrderedDescByUnknownColumn_thenServiceExceptionIsThrown() {
        // when
        ServiceException ex = Assertions.assertThrows(ServiceException.class,
                () -> service.orderBooks("desc", "unknown"));
        // then
        assertEquals("column", ex.getField());
        assertEquals("Invalid column order", ex.getMessage());

    }

}
