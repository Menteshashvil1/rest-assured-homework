package api.tests;

import data.Constants;
import data.models.response.Book;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import steps.BookStoreSteps;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.notNullValue;

public class BookStoreObjectMappingTests {

    private static final String[] EXPECTED_LAST_AUTHORS = {"Marijn Haverbeke", "Nicholas C. Zakas"};

    private final BookStoreSteps bookStoreSteps = new BookStoreSteps();

    private List<Book> books;

    @BeforeClass(alwaysRun = true)
    public void fetchBooks() {
        books = bookStoreSteps.getBooks();
    }

    @Test
    public void booksAreDeserializedIntoPojos() {
        assertThat(books, hasSize(greaterThan(0)));
        assertThat(books, everyItem(hasProperty("isbn", notNullValue())));
        assertThat(books, everyItem(hasProperty("title", notNullValue())));
        assertThat(books, everyItem(hasProperty("author", notNullValue())));
        assertThat(books, everyItem(hasProperty("publishDate", notNullValue())));
        assertThat(books, everyItem(hasProperty("pages", notNullValue())));
    }

    @Test
    public void everyBookHasFewerPagesThanTheLimit() {
        assertThat(books, everyItem(hasProperty("pages", lessThan(Constants.MAX_BOOK_PAGES))));
        assertThat(books, everyItem(hasProperty("pages", greaterThan(0))));
    }

    @Test
    public void lastTwoAuthorsMatchExpectedNames() {
        List<String> lastTwoAuthors = bookStoreSteps.getLastBooks(EXPECTED_LAST_AUTHORS.length)
                .stream()
                .map(Book::getAuthor)
                .toList();

        assertThat(lastTwoAuthors, contains(EXPECTED_LAST_AUTHORS));
    }

    @Test
    public void bookIsbnsAreUnique() {
        List<String> isbns = books.stream().map(Book::getIsbn).toList();
        assertThat(isbns.stream().distinct().toList(), hasSize(books.size()));
    }
}
