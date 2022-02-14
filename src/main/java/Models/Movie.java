package Models;

import Database.MovieRepository;
import Util.Column;
import Util.Entity;
import Util.OneToManyRelation;

@Entity(name = "movies", repository = MovieRepository.class)
public class Movie extends AbstractEntity {

    @Column(name = "name")
    private final String title;

    @OneToManyRelation(relatedEntity = Presentation.class, localField = "presentation_id")
    private final Presentation presentation;

    public Movie(String title, Presentation presentation) {
        this.title = title;
        this.presentation = presentation;
    }

    public String getTitle() {
        return title;
    }

    public Presentation getPresentation() {
        return presentation;
    }
}
