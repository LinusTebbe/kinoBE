package Models;

import Database.Repository.SeatRepository;
import Util.Column;
import Util.Entity;

@Entity(name = "seat", repository = SeatRepository.class)
public class Seat extends AbstractEntity {
    @Column(name = "position")
    private final int position;

    @Column(name = "seat_type")
    private final SeatType seatType;

    public Seat(int position, SeatType seatType) {
        this.position = position;
        this.seatType = seatType;
    }

    public int getPosition() {
        return position;
    }

    public SeatType getSeatType() {
        return seatType;
    }
}
