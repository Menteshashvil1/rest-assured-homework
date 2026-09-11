package steps;

import api.client.BookStoreApi;
import data.models.response.Book;
import data.models.response.BooksResponse;

import java.util.List;

public class BookStoreSteps {

    private static final int OK = 200;

    private final BookStoreApi bookStoreApi = new BookStoreApi();

    public List<Book> getBooks() {
        return bookStoreApi.getBooks()
                .then()
                .statusCode(OK)
                .extract()
                .as(BooksResponse.class)
                .getBooks();
    }

    public List<Book> getLastBooks(int count) {
        List<Book> books = getBooks();
        return books.subList(books.size() - count, books.size());
    }
}
