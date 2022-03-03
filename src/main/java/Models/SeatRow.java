package Models;

import Database.Repository.SeatRowRepository;
import Util.Column;
import Util.Entity;
import Util.ManyToOneRelation;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "seat_row", repository = SeatRowRepository.class)
public class SeatRow extends AbstractEntity {
    @Column(name = "number")
    private final int number;

    @Column(name = "seat_row_type")
    private final SeatRowType seatRowType;

    @ManyToOneRelation(remoteField = "seat_row_id", targetClass = Seat.class)
    private final List<Seat> seats;

    public SeatRow(int number, SeatRowType seatRowType) {
        this.number = number;
        this.seatRowType = seatRowType;
        this.seats = new ArrayList<>();
    }

    public int getNumber() {
        return number;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public SeatRowType getSeatRowType() {
        return seatRowType;
    }
}
