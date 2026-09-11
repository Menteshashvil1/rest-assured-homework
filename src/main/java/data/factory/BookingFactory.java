package data.factory;

import data.models.requests.booker.Booking;
import data.models.requests.booker.BookingDates;
import net.datafaker.Faker;

import java.time.LocalDate;

public class BookingFactory {

    private static final Faker faker = new Faker();

    public static Booking randomBooking() {
        LocalDate checkin = LocalDate.now().plusDays(faker.number().numberBetween(1, 30));

        Booking booking = new Booking();
        booking.setFirstname(faker.name().firstName());
        booking.setLastname(faker.name().lastName());
        booking.setTotalprice(faker.number().numberBetween(100, 900));
        booking.setDepositpaid(true);
        booking.setBookingdates(new BookingDates(checkin.toString(), checkin.plusDays(4).toString()));
        booking.setAdditionalneeds("Breakfast");

        return booking;
    }

    public static Booking partialUpdate(String firstname, int totalprice) {
        Booking booking = new Booking();
        booking.setFirstname(firstname);
        booking.setTotalprice(totalprice);
        return booking;
    }
}
