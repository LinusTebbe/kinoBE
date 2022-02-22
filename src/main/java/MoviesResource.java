import Database.Repository.MovieRepository;
import Models.Movie;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import java.util.List;

public class MoviesResource extends ServerResource {

    @Get("json")
    public List<Movie> represent() {
        MovieRepository movieRepository = new MovieRepository();
        return movieRepository.findAll();
    }
}
