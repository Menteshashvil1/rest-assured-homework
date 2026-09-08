package com.tbc.restassured;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;

public class BookStoreTest extends BaseTest {

    private static final String BOOKS_PATH = "/BookStore/v1/Books";
    private static final String BOOK_PATH = "/BookStore/v1/Book";
    private static final int MAX_PAGES = 1000;
    private static final String EXPECTED_FIRST_AUTHOR = "Richard E. Silverman";
    private static final String EXPECTED_SECOND_AUTHOR = "Addy Osmani";

    private String firstIsbn;
    private String firstAuthor;
    private String secondIsbn;
    private String secondAuthor;

    @Override
    protected String baseUri() {
        return BOOKSTORE_URL;
    }

    @BeforeClass(alwaysRun = true, dependsOnMethods = "setUpSpec")
    public void loadBooks() {
        JsonPath books = fetchBooks().jsonPath();

        firstIsbn = books.getString("books[0].isbn");
        firstAuthor = books.getString("books[0].author");
        secondIsbn = books.getString("books[1].isbn");
        secondAuthor = books.getString("books[1].author");
    }

    @Test
    public void getAllBooksReturnsNonEmptyCollection() {
        given()
                .spec(spec)
                .when()
                .get(BOOKS_PATH)
                .then()
                .statusCode(200)
                .body("books", notNullValue())
                .body("books.size()", greaterThan(0));
    }

    @Test
    public void allBooksHaveLessThan1000Pages() {
        given()
                .spec(spec)
                .when()
                .get(BOOKS_PATH)
                .then()
                .statusCode(200)
                .body("books.pages", everyItem(lessThan(MAX_PAGES)));
    }

    @Test
    public void firstAndSecondBookAuthorsAreCorrect() {
        given()
                .spec(spec)
                .when()
                .get(BOOKS_PATH)
                .then()
                .statusCode(200)
                .body("books[0].author", equalTo(EXPECTED_FIRST_AUTHOR))
                .body("books[1].author", equalTo(EXPECTED_SECOND_AUTHOR));
    }

    @Test
    public void extractsIsbnAndAuthorOfFirstTwoBooks() {
        assertNotNull(firstIsbn, "first book isbn");
        assertNotNull(firstAuthor, "first book author");
        assertNotNull(secondIsbn, "second book isbn");
        assertNotNull(secondAuthor, "second book author");
        assertNotEquals(firstIsbn, secondIsbn, "first and second isbn must differ");
    }

    @DataProvider(name = "indexIsbnAuthor")
    public Object[][] indexIsbnAuthor() {
        JsonPath books = fetchBooks().jsonPath();

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
                .body("title", not(emptyOrNullString()))
                .body("publish_date", notNullValue())
                .body("pages", greaterThan(0))
                .body("author", notNullValue())
                .body("publisher", notNullValue())
                .extract()
                .response();

        assertEquals(response.jsonPath().getString("author"), expectedAuthor,
                "author of book at index " + index);
    }

    @Test
    public void deleteBookWithoutAuthorizationIsRejected() {
        given()
                .spec(spec)
                .contentType(ContentType.JSON)
                .body("{\"isbn\":\"" + firstIsbn + "\",\"userId\":\"9bb1b8a0-0000-0000-0000-000000000000\"}")
                .when()
                .delete(BOOK_PATH)
                .then()
                .statusCode(401)
                .body("message", equalTo("User not authorized!"));
    }

    private Response fetchBooks() {
        return given()
                .spec(spec)
                .when()
                .get(BOOKS_PATH)
                .then()
                .statusCode(200)
                .extract()
                .response();
    }
}