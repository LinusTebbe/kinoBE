package Models;

import Database.Repository.PresentationRepository;
import Util.Column;
import Util.Entity;
import Util.OneToManyRelation;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.time.LocalDateTime;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Entity(name = "presentation", repository = PresentationRepository.class)
public class Presentation extends AbstractEntity {
    @Column(name = "start")
    private final LocalDateTime start;

    @OneToManyRelation(localField = "movie_id")
    private final Movie movie;

    @OneToManyRelation(localField = "cinema_hall_id")
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
