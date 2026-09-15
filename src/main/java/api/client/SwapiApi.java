package api.client;

import data.Constants;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class SwapiApi extends BaseApi {

    public Response getPlanets() {
        return given()
                .spec(SWAPI_SPEC)
                .queryParam(Constants.FORMAT_PARAM, Constants.JSON_FORMAT)
                .when()
                .get(Constants.PLANETS_PATH);
    }

    public Response getPlanetByUrl(String url) {
        return given()
                .spec(SWAPI_SPEC)
                .queryParam(Constants.FORMAT_PARAM, Constants.JSON_FORMAT)
                .when()
                .get(url);
    }
}
