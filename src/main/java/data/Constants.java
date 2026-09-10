package data;

public class Constants {
    public static final String ERGAST_URL = "https://api.jolpi.ca";
    public static final String DRIVERS_PATH = "/ergast/f1/2025/drivers/";
    public static final String PETSTORE_URL = "https://petstore.swagger.io/v2";

    public static final String PET_PATH = "/pet";
    public static final String PET_FIND_BY_STATUS_PATH = "/pet/findByStatus";
    public static final String PET_BY_ID_PATH = "/pet/{petId}";
    public static final String PET_UPLOAD_IMAGE_PATH = "/pet/{petId}/uploadImage";

    public static final String PET_STATUS = "pending";
    public static final String SOLD_STATUS = "sold";

    public static final String STATUS_PARAM = "status";
    public static final String NAME_PARAM = "name";
    public static final String PET_ID_PARAM = "petId";

    public static final String UPLOAD_METADATA = "uploaded-by-nodar";
    public static final String UPLOAD_FILE_NAME = "pet-photo.txt";

    public static final String PLATZI_URL = "https://api.escuelajs.co/api";
    public static final String PLATZI_USERS_PATH = "/v1/users/";
    public static final String PLATZI_LOGIN_PATH = "/v1/auth/login";
    public static final String PLATZI_PROFILE_PATH = "/v1/auth/profile";
    public static final String PLATZI_DEFAULT_ROLE = "customer";
    public static final String PLATZI_AVATAR = "https://picsum.photos/800";

    public static final String BOOKER_URL = "https://restful-booker.herokuapp.com";
    public static final String BOOKER_AUTH_PATH = "/auth";
    public static final String BOOKER_BOOKING_PATH = "/booking";
    public static final String BOOKER_BOOKING_BY_ID_PATH = "/booking/{id}";
    public static final String BOOKER_USERNAME = "admin";
    public static final String BOOKER_PASSWORD = "password123";

    public static final String BOOKSTORE_URL = "https://bookstore.toolsqa.com";
    public static final String BOOKS_PATH = "/BookStore/v1/Books";

    public static final int MAX_BOOK_PAGES = 1000;

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String TOKEN_COOKIE = "token";
    public static final String ID_PARAM = "id";
    public static final String APPLICATION_JSON = "application/json";
}
