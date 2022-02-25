package Models;

import Database.Repository.ReservationRepository;
import Util.Entity;
import Util.ManyToOneRelation;
import Util.OneToManyRelation;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "booking", repository = ReservationRepository.class)
public class Booking extends AbstractEntity {
    @ManyToOneRelation(targetClass = Reservation.class,remoteField = "booking_id")
    private final List<Reservation> presentation;

    public Booking() {
        this.presentation = new ArrayList<>();
    }

    public List<Reservation> getPresentation() {
        return presentation;
    }
}
