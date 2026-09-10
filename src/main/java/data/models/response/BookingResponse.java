package data.models.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import data.models.request.Booking;

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
