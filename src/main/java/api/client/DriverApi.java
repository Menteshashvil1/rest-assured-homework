package api.client;

import data.Constants;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class DriverApi extends BaseApi {

    private static final int PAGE_LIMIT = 100;

    public Response getDrivers() {
        return given()
                .spec(ERGAST_SPEC)
                .queryParam("limit", PAGE_LIMIT)
                .when()
                .get(Constants.DRIVERS_PATH);
    }
}
