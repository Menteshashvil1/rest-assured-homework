package com.tbc.restassured;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.notNullValue;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class PetStoreTest extends BaseTest {

    private static final long ORDER_ID = 7_654_321L;
    private static final long PET_ID = 7_654_322L;
    private static final Pattern SESSION_ID = Pattern.compile("\\d{10,}");

    @Override
    protected String baseUri() {
        return PETSTORE_URL;
    }

    @Test
    public void createsStoreOrder() {
        Map<String, Object> order = new LinkedHashMap<>();
        order.put("id", ORDER_ID);
        order.put("petId", PET_ID);
        order.put("quantity", 3);
        order.put("shipDate", "2026-09-05T10:15:30.000+0000");
        order.put("status", "placed");
        order.put("complete", true);

        given()
                .spec(spec)
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post("/store/order")
                .then()
                .statusCode(200)
                .body("id", equalTo((int) ORDER_ID))
                .body("petId", equalTo((int) PET_ID))
                .body("quantity", equalTo(3))
                .body("status", equalTo("placed"))
                .body("complete", equalTo(true))
                .body("shipDate", notNullValue());
    }

    @BeforeClass(alwaysRun = true)
    public void createPetUnderTest() {
        Map<String, Object> pet = new LinkedHashMap<>();
        pet.put("id", PET_ID);
        pet.put("name", "Buddy");
        pet.put("status", "available");

        given()
                .spec(spec)
                .contentType(ContentType.JSON)
                .body(pet)
                .when()
                .post("/pet")
                .then()
                .statusCode(200)
                .body("id", equalTo((int) PET_ID));
    }

    @Test
    public void updatesPetWithFormParameters() {
        given()
                .spec(spec)
                .contentType(ContentType.URLENC)
                .formParam("petId", PET_ID)
                .formParam("name", "Rex")
                .formParam("status", "sold")
                .when()
                .post("/pet/{petId}", PET_ID)
                .then()
                .statusCode(200)
                .body("code", notNullValue())
                .body("type", notNullValue())
                .body("message", equalTo(String.valueOf(PET_ID)));
    }

    @Test
    public void invalidPetIdReturnsNotFoundInBody() {
        given()
                .spec(spec)
                .when()
                .get("/pet/{petId}", "not-a-number")
                .then()
                .statusCode(404)
                .body("code", equalTo(404));
    }

    @Test
    public void loginReturnsSessionIdWithTenOrMoreDigits() {
        Response response = given()
                .spec(spec)
                .queryParam("username", "tbc-student")
                .queryParam("password", "tbc-password")
                .when()
                .get("/user/login")
                .then()
                .statusCode(200)
                .body("code", equalTo(200))
                .body("message", matchesPattern(".*\\d{10,}.*"))
                .extract()
                .response();

        String message = response.jsonPath().getString("message");
        Matcher matcher = SESSION_ID.matcher(message);
        assertTrue(matcher.find(), "message must contain a numeric session id: " + message);

        String sessionId = matcher.group();
        assertTrue(sessionId.length() >= 10, "session id must have at least 10 digits: " + sessionId);
        assertEquals(sessionId.replaceAll("\\d", "").length(), 0, "session id must be digits only");

        System.out.println("Login message: " + message);
        System.out.println("Extracted session id: " + sessionId);
    }

    @Test
    public void inventoryIsNotEmpty() {
        given()
                .spec(spec)
                .when()
                .get("/store/inventory")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }
}
