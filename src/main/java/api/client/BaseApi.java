package api.client;

import data.Constants;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public abstract class BaseApi {

    static {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL);
    }

    public static final RequestSpecification PLATZI_SPEC = specFor(Constants.PLATZI_URL);
    public static final RequestSpecification BOOKER_SPEC = specFor(Constants.BOOKER_URL);
    public static final RequestSpecification BOOKSTORE_SPEC = specFor(Constants.BOOKSTORE_URL);
    public static final RequestSpecification ERGAST_SPEC = specFor(Constants.ERGAST_URL);

    protected static RequestSpecification specFor(String baseUri) {
        return new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setContentType(ContentType.JSON)
                .setAccept(Constants.APPLICATION_JSON)
                .log(LogDetail.URI)
                .build();
    }
}
