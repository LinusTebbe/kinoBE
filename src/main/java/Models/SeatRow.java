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

    @ManyToOneRelation(remoteField = "seat_row_id", targetClass = Seat.class)
    private final List<Seat> seats;

    public SeatRow(int number) {
        this.number = number;
        this.seats = new ArrayList<>();
    }

    public int getNumber() {
        return number;
    }

    public List<Seat> getSeats() {
        return seats;
    }
}
