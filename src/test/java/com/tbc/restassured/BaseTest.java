package com.tbc.restassured;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

public abstract class BaseTest {

    protected static final String BOOKSTORE_URL = "https://bookstore.toolsqa.com";
    protected static final String PETSTORE_URL = "https://petstore.swagger.io/v2";
    protected static final String OPENLIBRARY_URL = "https://openlibrary.org";

    protected RequestSpecification spec;

    protected abstract String baseUri();

    @BeforeClass(alwaysRun = true)
    public void setUpSpec() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL);
        spec = new RequestSpecBuilder()
                .setBaseUri(baseUri())
                .setAccept(ContentType.JSON)
                .build();
    }
}
