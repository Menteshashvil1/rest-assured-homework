package api.tests;

import data.factory.BookingFactory;
import data.models.request.Booking;
import data.models.response.BookingResponse;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import steps.BookerSteps;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.samePropertyValuesAs;

public class BookerBookingTests {

    private static final String UPDATED_FIRSTNAME = "Nodar";
    private static final int UPDATED_TOTALPRICE = 777;

    private final BookerSteps bookerSteps = new BookerSteps();

    private String token;
    private Booking requestedBooking;
    private int bookingId;

    @BeforeClass(alwaysRun = true)
    public void createToken() {
        token = bookerSteps.createToken();
        assertThat(token, notNullValue());
    }

    @Test
    public void createdBookingIsReturnedByGetBooking() {
        requestedBooking = BookingFactory.randomBooking();

        BookingResponse created = bookerSteps.createBooking(requestedBooking);
        bookingId = created.getBookingid();

        assertThat(created, hasProperty("bookingid", greaterThan(0)));
        assertThat(created.getBooking(), samePropertyValuesAs(requestedBooking));

        assertThat(bookerSteps.getBooking(bookingId), equalTo(requestedBooking));
    }

    @Test(dependsOnMethods = "createdBookingIsReturnedByGetBooking")
    public void partialUpdateChangesOnlyProvidedFields() {
        Booking patch = BookingFactory.partialUpdate(UPDATED_FIRSTNAME, UPDATED_TOTALPRICE);

        Booking updated = bookerSteps.partialUpdateBooking(bookingId, token, patch);

        Booking expected = new Booking();
        expected.setFirstname(UPDATED_FIRSTNAME);
        expected.setLastname(requestedBooking.getLastname());
        expected.setTotalprice(UPDATED_TOTALPRICE);
        expected.setDepositpaid(requestedBooking.getDepositpaid());
        expected.setBookingdates(requestedBooking.getBookingdates());
        expected.setAdditionalneeds(requestedBooking.getAdditionalneeds());

        assertThat(updated, equalTo(expected));
        assertThat(bookerSteps.getBooking(bookingId), equalTo(expected));
    }

    @Test(dependsOnMethods = "partialUpdateChangesOnlyProvidedFields")
    public void deletedBookingIsNoLongerAvailable() {
        bookerSteps.deleteBooking(bookingId, token);
        bookerSteps.checkBookingIsGone(bookingId);
    }
}
