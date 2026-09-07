package com.tbc.restassured;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;

public class OpenLibraryTest extends BaseTest {

    private static final String KEYWORD = "Harry Potter";

    @Override
    protected String baseUri() {
        return OPENLIBRARY_URL;
    }

    @Test
    public void searchReturnsExpectedFirstBook() {
        Response response = given()
                .spec(spec)
                .queryParam("q", KEYWORD)
                .queryParam("fields", "title,author_name")
                .queryParam("limit", 10)
                .when()
                .get("/search.json")
                .then()
                .statusCode(200)
                .body("numFound", greaterThan(0))
                .body("docs", notNullValue())
                .body("docs.size()", greaterThan(0))
                .body("docs[0].title", equalTo("Harry Potter and the Philosopher's Stone"))
                .body("docs[0].author_name", hasItem("J. K. Rowling"))
                .extract()
                .response();

        System.out.println("numFound: " + response.jsonPath().getInt("numFound"));
        System.out.println("First doc: " + response.jsonPath().getString("docs[0].title")
                + " by " + response.jsonPath().getList("docs[0].author_name"));
    }
}
