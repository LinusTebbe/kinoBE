package Models;

import Database.Repository.SeatRepository;
import Util.Column;
import Util.Entity;
import Util.ManyToOneRelation;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "seat", repository = SeatRepository.class)
public class Seat extends AbstractEntity {
    @Column(name = "position")
    private final int position;

    @Column(name = "seat_type")
    private final SeatType seatType;

    @ManyToOneRelation(targetClass=Reservation.class, remoteField="seat_id")
    private final List<Reservation> reservations;

    public Seat(int position, SeatType seatType) {
        this.position = position;
        this.seatType = seatType;
        this.reservations = new ArrayList<>();
    }

    public int getPosition() {
        return position;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }
}
