package Models;

import Database.Repository.CinemaHallRepository;
import Util.Entity;
import Util.ManyToOneRelation;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "cinema_hall", repository = CinemaHallRepository.class)
public class CinemaHall extends AbstractEntity {
    @ManyToOneRelation(remoteField = "cinema_hall_id", targetClass = Seat.class)
    private final List<Seat> seats;

    public CinemaHall() {
        this.seats = new ArrayList<>();
    }

    public List<Seat> getSeats() {
        return this.seats;
    }
}
