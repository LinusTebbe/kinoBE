package Models;

import Database.CustomDateSerializer;
import Database.Repository.PresentationRepository;
import Util.Column;
import Util.Entity;
import Util.ManyToOneRelation;
import Util.OneToManyRelation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @ManyToOneRelation(targetClass=Reservation.class, remoteField="presentation_id")
    private final List<Reservation> reservations;

    public Presentation(Date start, Movie movie, CinemaHall cinemaHall) {
        this.start = start;
        this.movie = movie;
        this.cinemaHall = cinemaHall;
        this.reservations = new ArrayList<>();
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

    public List<Reservation> getReservations() {
        return reservations;
    }
}
