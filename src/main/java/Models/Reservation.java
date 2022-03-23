package Models;

import Database.Repository.ReservationRepository;
import Util.Entity;
import Util.OneToManyRelation;

@Entity(name = "reservation", repository = ReservationRepository.class)
public class Reservation extends AbstractEntity {
    @OneToManyRelation(localField = "presentation_id")
    private final Presentation presentation;

    @OneToManyRelation(localField = "seat_id")
    private final Seat seat;

    public Reservation(Presentation presentation, Seat seat) {
        this.presentation = presentation;
        this.seat = seat;
    }

    public Presentation getPresentation() {
        return presentation;
    }

    public Seat getSeat() {
        return seat;
    }

    public int getPresentationId() {
        return this.presentation.getId();
    }
}
