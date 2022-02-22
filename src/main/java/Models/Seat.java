package Models;

import Database.Repository.SeatRepository;
import Util.Column;
import Util.Entity;

@Entity(name = "seat", repository = SeatRepository.class)
public class Seat extends AbstractEntity {
    @Column(name = "position_x")
    private final int positionX;

    @Column(name = "position_y")
    private final int positionY;

    @Column(name = "seat_type_id")
    private final SeatType seatType;

    public Seat(int positionX, int positionY, SeatType seatType) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.seatType = seatType;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public SeatType getSeatType() {
        return seatType;
    }
}
