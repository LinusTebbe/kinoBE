package Models;

import Database.CustomDateSerializer;
import Database.Repository.PresentationRepository;
import Util.Column;
import Util.Entity;
import Util.OneToManyRelation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

@Entity(name = "presentation", repository = PresentationRepository.class)
public class Presentation extends AbstractEntity {
    @Column(name = "start")
    @JsonSerialize(using = CustomDateSerializer.class)
    private final Date start;

    @JsonIgnoreProperties("presentations")
    @OneToManyRelation(localField = "movie_id")
    private final Movie movie;

    @OneToManyRelation(localField = "cinema_hall_id")
    private final CinemaHall cinemaHall;

    public Presentation(Date start, Movie movie, CinemaHall cinemaHall) {
        this.start = start;
        this.movie = movie;
        this.cinemaHall = cinemaHall;
    }

    public Date getStart() {
        return start;
    }

    public Movie getMovie() {
        return movie;
    }

    public CinemaHall getCinemaHall() {
        return cinemaHall;
    }
}
