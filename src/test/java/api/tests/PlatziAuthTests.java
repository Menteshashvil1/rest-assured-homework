package api.tests;

import data.Constants;
import data.factory.UserFactory;
import data.models.requests.platzi.CreateUserRequest;
import data.models.responses.platzi.LoginResponse;
import data.models.responses.platzi.UserResponse;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import steps.PlatziSteps;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;

public class PlatziAuthTests {

    private final PlatziSteps platziSteps = new PlatziSteps();

    private CreateUserRequest newUser;
    private UserResponse createdUser;
    private LoginResponse tokens;

    @BeforeClass(alwaysRun = true)
    public void createAndAuthenticateUser() {
        newUser = UserFactory.randomUser();
        createdUser = platziSteps.createUser(newUser);
        tokens = platziSteps.login(UserFactory.credentialsOf(newUser));
    }

    @Test
    public void createdUserMatchesRequest() {
        assertThat(createdUser, hasProperty("id", greaterThan(0)));
        assertThat(createdUser.getName(), equalTo(newUser.getName()));
        assertThat(createdUser.getEmail(), equalTo(newUser.getEmail()));
        assertThat(createdUser.getAvatar(), equalTo(newUser.getAvatar()));
        assertThat(createdUser.getRole(), equalTo(Constants.PLATZI_DEFAULT_ROLE));
    }

    @Test
    public void loginReturnsBothTokens() {
        assertThat(tokens.getAccessToken(), notNullValue());
        assertThat(tokens.getRefreshToken(), notNullValue());
        assertThat(tokens.getAccessToken(), startsWith("eyJ"));
        assertThat(tokens.getRefreshToken(), startsWith("eyJ"));
        assertThat(tokens.getAccessToken(), not(equalTo(tokens.getRefreshToken())));
    }

    @Test
    public void profileOfAuthenticatedUserMatchesCreatedUser() {
        UserResponse profile = platziSteps.getProfile(tokens.getAccessToken());

        assertThat(profile, hasProperty("id", equalTo(createdUser.getId())));
        assertThat(profile, hasProperty("email", equalTo(newUser.getEmail())));
        assertThat(profile, hasProperty("name", equalTo(newUser.getName())));
        assertThat(profile, hasProperty("role", equalTo(Constants.PLATZI_DEFAULT_ROLE)));
        assertThat(profile, hasProperty("avatar", equalTo(newUser.getAvatar())));
        assertThat(profile, hasProperty("creationAt", notNullValue()));
        assertThat(profile, hasProperty("updatedAt", notNullValue()));
    }

    @Test
    public void profileIsNotReachableWithoutAccessToken() {
        platziSteps.checkProfileRequiresToken();
    }
}
