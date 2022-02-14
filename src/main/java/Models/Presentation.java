package Models;

import Database.PresentationRepository;
import Util.Entity;

import java.time.LocalDateTime;

@Entity(name = "presentation", repository = PresentationRepository.class)
public class Presentation extends AbstractEntity {
    private final LocalDateTime start;
    private final Movie movie;
    private final CinemaHall cinemaHall;

    public Presentation(LocalDateTime start, Movie movie, CinemaHall cinemaHall) {
        this.start = start;
        this.movie = movie;
        this.cinemaHall = cinemaHall;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public Movie getMovie() {
        return movie;
    }

    public CinemaHall getCinemaHall() {
        return cinemaHall;
    }
}
