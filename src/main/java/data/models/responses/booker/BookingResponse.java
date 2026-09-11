package data.models.responses.booker;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import data.models.requests.booker.Booking;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingResponse {
    private Integer bookingid;
    private Booking booking;

    public BookingResponse() {}

    public Integer getBookingid() { return bookingid; }
    public void setBookingid(Integer bookingid) { this.bookingid = bookingid; }
    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }
}
