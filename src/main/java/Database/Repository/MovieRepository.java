package Database.Repository;

import Models.Movie;

public class MovieRepository extends AbstractRepository<Movie> {
    public MovieRepository() {
        super(Movie.class);
    }
}
