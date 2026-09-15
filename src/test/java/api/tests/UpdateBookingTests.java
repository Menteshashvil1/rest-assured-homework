package api.tests;

import config.RestAssuredConfiguration;
import data.factory.BookingFactory;
import data.models.requests.booker.BookingDates;
import data.models.requests.booker.UpdateBookingRequest;
import data.models.responses.booker.UpdateBookingResponse;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import steps.BookerSteps;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;

@Epic("Advanced object mapping")
@Feature("Restful Booker update booking")
public class UpdateBookingTests {

    private static final String FIRSTNAME = "Nodar";
    private static final String LASTNAME = "Menteshashvili";
    private static final int TOTALPRICE = 480;
    private static final String ADDITIONAL_NEEDS = "Dinner";
    private static final BookingDates DATES = new BookingDates("2026-11-01", "2026-11-07");

    private final BookerSteps bookerSteps = new BookerSteps();

    private String token;
    private int bookingId;

    @BeforeClass(alwaysRun = true)
    public void createToken() {
        token = bookerSteps.createToken();
        assertThat(token, notNullValue());

        bookingId = bookerSteps.createBooking(BookingFactory.randomBooking()).getBookingid();
    }

    @Test
    public void updateBookingReplacesEveryField() {
        UpdateBookingResponse updated = bookerSteps.updateBooking(bookingId, token, fullUpdate());

        assertThat(updated, allOf(
                hasProperty("firstname", equalTo(FIRSTNAME)),
                hasProperty("lastname", equalTo(LASTNAME)),
                hasProperty("totalprice", equalTo(TOTALPRICE)),
                hasProperty("depositpaid", equalTo(false)),
                hasProperty("additionalneeds", equalTo(ADDITIONAL_NEEDS))));

        assertThat(updated.getBookingdates(), equalTo(DATES));
    }

    @Test(dependsOnMethods = "updateBookingReplacesEveryField")
    public void updatedBookingIsReturnedByGetBooking() {
        assertThat(bookerSteps.getBooking(bookingId), allOf(
                hasProperty("firstname", equalTo(FIRSTNAME)),
                hasProperty("lastname", equalTo(LASTNAME)),
                hasProperty("totalprice", equalTo(TOTALPRICE)),
                hasProperty("depositpaid", equalTo(false)),
                hasProperty("bookingdates", equalTo(DATES)),
                hasProperty("additionalneeds", equalTo(ADDITIONAL_NEEDS))));
    }

    @Test
    public void salepriceAndPassportNoAreNeverSerialized() throws Exception {
        UpdateBookingRequest request = fullUpdate();

        assertThat(request.getSaleprice(), equalTo(99));
        assertThat(request.getPassportNo(), equalTo("GE1234567"));

        String json = RestAssuredConfiguration.objectMapper().writeValueAsString(request);

        assertThat(json, not(containsString("saleprice")));
        assertThat(json, not(containsString("passportNo")));
        assertThat(json, containsString("firstname"));
    }

    @Test
    public void nullFieldsAreNotSerialized() throws Exception {
        UpdateBookingRequest request = UpdateBookingRequest.builder()
                .firstname(FIRSTNAME)
                .totalprice(TOTALPRICE)
                .build();

        String json = RestAssuredConfiguration.objectMapper().writeValueAsString(request);

        assertThat(json, containsString("firstname"));
        assertThat(json, containsString("totalprice"));
        assertThat(json, not(containsString("lastname")));
        assertThat(json, not(containsString("depositpaid")));
        assertThat(json, not(containsString("bookingdates")));
        assertThat(json, not(containsString("additionalneeds")));
    }

    @Test
    public void updateWithoutTokenIsForbidden() {
        bookerSteps.checkUpdateIsForbiddenWithoutToken(bookingId, fullUpdate());
    }

    private UpdateBookingRequest fullUpdate() {
        return UpdateBookingRequest.builder()
                .firstname(FIRSTNAME)
                .lastname(LASTNAME)
                .totalprice(TOTALPRICE)
                .depositpaid(false)
                .bookingdates(DATES)
                .additionalneeds(ADDITIONAL_NEEDS)
                .saleprice(99)
                .passportNo("GE1234567")
                .build();
    }
}
