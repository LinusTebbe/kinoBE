package Models;

import Database.Repository.SeatRepository;
import Util.Column;
import Util.Entity;
import Util.ManyToOneRelation;
import Util.OneToManyRelation;

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

    @OneToManyRelation(localField = "seat_row_id")
    private final SeatRow seatRow;

    public Seat(int position, SeatType seatType, SeatRow seatRow) {
        this.position = position;
        this.seatType = seatType;
        this.reservations = new ArrayList<>();
        this.seatRow = seatRow;
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

    public SeatRowType getSeatRowType() {
        return this.seatRow.getSeatRowType();
    }

    public String getIdentifier() {
        return String.format(
                "Reihe %s / Sitz %s",
                this.seatRow.getIdentifier(),
                this.getPosition()
        );
    }
}
