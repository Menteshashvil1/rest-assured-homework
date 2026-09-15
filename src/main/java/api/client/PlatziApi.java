package api.client;

import data.Constants;
import data.models.requests.platzi.CreateUserRequest;
import data.models.requests.platzi.LoginRequest;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class PlatziApi extends BaseApi {

    public Response createUser(CreateUserRequest user) {
        return given()
                .spec(PLATZI_SPEC)
                .body(user)
                .when()
                .post(Constants.PLATZI_USERS_PATH);
    }

    public Response login(LoginRequest credentials) {
        return given()
                .spec(PLATZI_SPEC)
                .body(credentials)
                .when()
                .post(Constants.PLATZI_LOGIN_PATH);
    }

    public Response getProfile(String accessToken) {
        return given()
                .spec(PLATZI_SPEC)
                .header(Constants.AUTHORIZATION_HEADER, Constants.BEARER_PREFIX + accessToken)
                .when()
                .get(Constants.PLATZI_PROFILE_PATH);
    }

    public Response getProfileWithoutToken() {
        return given()
                .spec(PLATZI_SPEC)
                .when()
                .get(Constants.PLATZI_PROFILE_PATH);
    }
}
