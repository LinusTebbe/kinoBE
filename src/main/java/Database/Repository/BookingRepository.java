package Database.Repository;

import Models.Booking;

public class BookingRepository extends AbstractRepository<Booking> {
    public BookingRepository() {
        super(Booking.class);
    }
}
