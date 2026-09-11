package steps;

import api.client.BookerApi;
import data.Constants;
import data.models.request.AuthRequest;
import data.models.request.Booking;
import data.models.response.AuthResponse;
import data.models.response.BookingResponse;
import org.testng.Assert;

public class BookerSteps {

    private static final int OK = 200;
    private static final int CREATED = 201;
    private static final int NOT_FOUND = 404;

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
