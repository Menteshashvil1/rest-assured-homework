package steps;

import api.client.BookerApi;
import data.Constants;
import data.models.requests.booker.AuthRequest;
import data.models.requests.booker.Booking;
import data.models.requests.booker.UpdateBookingRequest;
import data.models.responses.booker.AuthResponse;
import data.models.responses.booker.BookingResponse;
import data.models.responses.booker.UpdateBookingResponse;
import io.qameta.allure.Step;
import org.testng.Assert;

public class BookerSteps {

    private static final int OK = 200;
    private static final int CREATED = 201;
    private static final int NOT_FOUND = 404;
    private static final int FORBIDDEN = 403;

    private final BookerApi bookerApi = new BookerApi();

    public String createToken() {
        AuthResponse auth = bookerApi
                .createToken(new AuthRequest(Constants.BOOKER_USERNAME, Constants.BOOKER_PASSWORD))
                .then()
                .statusCode(OK)
                .extract()
                .as(AuthResponse.class);

        Assert.assertNotNull(auth.getToken(), "token");

        return auth.getToken();
    }

    public BookingResponse createBooking(Booking booking) {
        return bookerApi.createBooking(booking)
                .then()
                .statusCode(OK)
                .extract()
                .as(BookingResponse.class);
    }

    public Booking getBooking(int bookingId) {
        return bookerApi.getBooking(bookingId)
                .then()
                .statusCode(OK)
                .extract()
                .as(Booking.class);
    }

    public Booking partialUpdateBooking(int bookingId, String token, Booking booking) {
        return bookerApi.partialUpdateBooking(bookingId, token, booking)
                .then()
                .statusCode(OK)
                .extract()
                .as(Booking.class);
    }

    @Step("Update the whole booking")
    public UpdateBookingResponse updateBooking(int bookingId, String token, UpdateBookingRequest booking) {
        return bookerApi.updateBooking(bookingId, token, booking)
                .then()
                .statusCode(OK)
                .extract()
                .as(UpdateBookingResponse.class);
    }

    @Step("Update the booking without a token")
    public void checkUpdateIsForbiddenWithoutToken(int bookingId, UpdateBookingRequest booking) {
        bookerApi.updateBookingWithoutToken(bookingId, booking)
                .then()
                .statusCode(FORBIDDEN);
    }

    public void deleteBooking(int bookingId, String token) {
        bookerApi.deleteBooking(bookingId, token)
                .then()
                .statusCode(CREATED);
    }

    public void checkBookingIsGone(int bookingId) {
        bookerApi.getBooking(bookingId)
                .then()
                .statusCode(NOT_FOUND);
    }
}
