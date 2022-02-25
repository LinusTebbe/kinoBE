package Database.Repository;

import Models.Reservation;

public class ReservationRepository extends AbstractRepository<Reservation> {
    public ReservationRepository() {
        super(Reservation.class);
    }
}
