package Models;

import Database.Repository.ReservationRepository;
import Util.Entity;
import Util.ManyToOneRelation;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "booking", repository = ReservationRepository.class)
public class Booking extends AbstractEntity {
    @ManyToOneRelation(targetClass = Reservation.class,remoteField = "booking_id")
    private final List<Reservation> reservations;

    public Booking() {
        this.reservations = new ArrayList<>();
    }

    public List<Reservation> getReservations() {
        return reservations;
    }
}
