package com.tbc.restassured;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class BookStoreTest extends BaseTest {

    private static final String BOOKS_PATH = "/BookStore/v1/Books";
    private static final String BOOK_PATH = "/BookStore/v1/Book";

    private String firstIsbn;
    private String firstAuthor;
    private String secondIsbn;
    private String secondAuthor;

    @Override
    protected String baseUri() {
        return BOOKSTORE_URL;
    }

    @BeforeClass(alwaysRun = true)
    public void loadBooks() {
        JsonPath books = given()
                .spec(spec)
                .when()
                .get(BOOKS_PATH)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath();

        firstIsbn = books.getString("books[0].isbn");
        firstAuthor = books.getString("books[0].author");
        secondIsbn = books.getString("books[1].isbn");
        secondAuthor = books.getString("books[1].author");
    }

    @Test
    public void getAllBooksReturnsNonEmptyCollection() {
        Response response = given()
                .spec(spec)
                .when()
                .get(BOOKS_PATH)
                .then()
                .statusCode(200)
                .body("books", notNullValue())
                .body("books.size()", greaterThan(0))
                .extract()
                .response();

        List<String> isbns = response.jsonPath().getList("books.isbn", String.class);
        System.out.println("Books returned: " + isbns.size() + " -> " + isbns);
    }

    @Test
    public void extractsIsbnAndAuthorOfFirstTwoBooks() {
        assertNotNull(firstIsbn, "first book isbn");
        assertNotNull(firstAuthor, "first book author");
        assertNotNull(secondIsbn, "second book isbn");
        assertNotNull(secondAuthor, "second book author");
        assertTrue(!firstIsbn.equals(secondIsbn), "first and second isbn must differ");

        System.out.println("Book 1 -> isbn=" + firstIsbn + ", author=" + firstAuthor);
        System.out.println("Book 2 -> isbn=" + secondIsbn + ", author=" + secondAuthor);
    }

    @DataProvider(name = "indexIsbnAuthor")
    public Object[][] indexIsbnAuthor() {
        JsonPath books = given()
                .spec(spec)
                .when()
                .get(BOOKS_PATH)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath();

        return new Object[][]{
                {0, books.getString("books[0].isbn"), books.getString("books[0].author")},
                {1, books.getString("books[1].isbn"), books.getString("books[1].author")}
        };
    }

    @Test(dataProvider = "indexIsbnAuthor")
    public void singleBookMatchesCollectionData(int index, String isbn, String expectedAuthor) {
        Response response = given()
                .spec(spec)
                .queryParam("ISBN", isbn)
                .when()
                .get(BOOK_PATH)
                .then()
                .statusCode(200)
                .body("isbn", equalTo(isbn))
                .body("title", notNullValue())
                .body("title", not(equalTo("")))
                .body("publish_date", notNullValue())
                .body("pages", greaterThan(0))
                .body("author", notNullValue())
                .body("publisher", notNullValue())
                .extract()
                .response();

        String actualAuthor = response.jsonPath().getString("author");
        assertEquals(actualAuthor, expectedAuthor, "author of book at index " + index);

        System.out.println("index=" + index
                + ", isbn=" + isbn
                + ", title=" + response.jsonPath().getString("title")
                + ", author=" + actualAuthor
                + ", publish_date=" + response.jsonPath().getString("publish_date")
                + ", pages=" + response.jsonPath().getInt("pages"));
    }

    @Test
    public void deleteBookWithoutAuthorizationIsRejected() {
        given()
                .spec(spec)
                .contentType("application/json")
                .body("{\"isbn\":\"" + firstIsbn + "\",\"userId\":\"9bb1b8a0-0000-0000-0000-000000000000\"}")
                .when()
                .delete(BOOK_PATH)
                .then()
                .statusCode(401)
                .body("message", equalTo("User not authorized!"));
    }
}
