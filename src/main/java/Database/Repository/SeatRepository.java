package Database.Repository;

import Models.Seat;

public class SeatRepository extends AbstractRepository<Seat> {
    public SeatRepository() {
        super(Seat.class);
    }
}
