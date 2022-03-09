package Resources;

import Database.Repository.MovieRepository;
import Util.Serializers;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class MoviesResource extends ServerResource {

    @Get("json")
    public String represent() {
        MovieRepository movieRepository = new MovieRepository();
        return Serializers.getForMoviesSet().deepSerialize(movieRepository.findAll());
    }
}
