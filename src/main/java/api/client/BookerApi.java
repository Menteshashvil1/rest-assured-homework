package api.client;

import data.Constants;
import data.models.request.AuthRequest;
import data.models.request.Booking;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class BookerApi extends BaseApi {

    public Response createToken(AuthRequest credentials) {
        return given()
                .spec(BOOKER_SPEC)
                .body(credentials)
                .when()
                .post(Constants.BOOKER_AUTH_PATH);
    }

    public Response createBooking(Booking booking) {
        return given()
                .spec(BOOKER_SPEC)
                .body(booking)
                .when()
                .post(Constants.BOOKER_BOOKING_PATH);
    }

    public Response getBooking(int bookingId) {
        return given()
                .spec(BOOKER_SPEC)
                .pathParam(Constants.ID_PARAM, bookingId)
                .when()
                .get(Constants.BOOKER_BOOKING_BY_ID_PATH);
    }

    public Response partialUpdateBooking(int bookingId, String token, Booking booking) {
        return given()
                .spec(BOOKER_SPEC)
                .cookie(Constants.TOKEN_COOKIE, token)
                .pathParam(Constants.ID_PARAM, bookingId)
                .body(booking)
                .when()
                .patch(Constants.BOOKER_BOOKING_BY_ID_PATH);
    }

    public Response deleteBooking(int bookingId, String token) {
        return given()
                .spec(BOOKER_SPEC)
                .cookie(Constants.TOKEN_COOKIE, token)
                .pathParam(Constants.ID_PARAM, bookingId)
                .when()
                .delete(Constants.BOOKER_BOOKING_BY_ID_PATH);
    }
}
