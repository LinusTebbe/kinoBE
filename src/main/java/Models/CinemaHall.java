package Models;

import Database.Repository.CinemaHallRepository;
import Util.Entity;

import java.util.List;

@Entity(name = "cinema_hall", repository = CinemaHallRepository.class)
public class CinemaHall extends AbstractEntity{
    private List<Seat> seats;
}
