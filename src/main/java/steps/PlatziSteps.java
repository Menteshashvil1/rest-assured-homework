package steps;

import api.client.PlatziApi;
import data.models.requests.platzi.CreateUserRequest;
import data.models.requests.platzi.LoginRequest;
import data.models.responses.platzi.LoginResponse;
import data.models.responses.platzi.UserResponse;
import org.testng.Assert;

public class PlatziSteps {

    private static final int CREATED = 201;
    private static final int OK = 200;
    private static final int UNAUTHORIZED = 401;

    private final PlatziApi platziApi = new PlatziApi();

    public UserResponse createUser(CreateUserRequest user) {
        return platziApi.createUser(user)
                .then()
                .statusCode(CREATED)
                .extract()
                .as(UserResponse.class);
    }

    public LoginResponse login(LoginRequest credentials) {
        LoginResponse tokens = platziApi.login(credentials)
                .then()
                .statusCode(CREATED)
                .extract()
                .as(LoginResponse.class);

        Assert.assertNotNull(tokens.getAccessToken(), "access_token");
        Assert.assertNotNull(tokens.getRefreshToken(), "refresh_token");

        return tokens;
    }

    public UserResponse getProfile(String accessToken) {
        return platziApi.getProfile(accessToken)
                .then()
                .statusCode(OK)
                .extract()
                .as(UserResponse.class);
    }

    public void checkProfileRequiresToken() {
        platziApi.getProfileWithoutToken()
                .then()
                .statusCode(UNAUTHORIZED);
    }
}
