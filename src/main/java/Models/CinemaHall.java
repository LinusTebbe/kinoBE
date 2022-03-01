package Models;

import Database.Repository.CinemaHallRepository;
import Util.Entity;
import Util.ManyToOneRelation;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "cinema_hall", repository = CinemaHallRepository.class)
public class CinemaHall extends AbstractEntity {
    @ManyToOneRelation(remoteField = "cinema_hall_id", targetClass = SeatRow.class)
    private final List<SeatRow> seatRows;

    public CinemaHall() {
        this.seatRows = new ArrayList<>();
    }

    public List<SeatRow> getSeatRows() {
        return this.seatRows;
    }
}
