package data.factory;

import data.Constants;
import data.models.request.CreateUserRequest;
import data.models.request.LoginRequest;
import net.datafaker.Faker;

public class UserFactory {

    private static final Faker faker = new Faker();

    public static CreateUserRequest randomUser() {
        return new CreateUserRequest(
                faker.name().fullName(),
                faker.internet().username() + faker.number().digits(6) + "@mail.com",
                faker.letterify("????") + faker.number().digits(4),
                Constants.PLATZI_AVATAR);
    }

    public static LoginRequest credentialsOf(CreateUserRequest user) {
        return new LoginRequest(user.getEmail(), user.getPassword());
    }
}
