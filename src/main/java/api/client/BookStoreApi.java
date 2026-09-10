package api.client;

import data.Constants;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class BookStoreApi extends BaseApi {

    public Response getBooks() {
        return given()
                .spec(BOOKSTORE_SPEC)
                .when()
                .get(Constants.BOOKS_PATH);
    }
}
