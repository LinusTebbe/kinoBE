import Database.Repository.PresentationRepository;
import Models.Presentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import java.util.List;

public class PresentationResource extends ServerResource {

    @Get("json")
    public List<Presentation> represent() {
        PresentationRepository presentationRepository = new PresentationRepository();

        String movieId = this.getQuery().getValues("movie_id");

        if (movieId.isEmpty()) {
            return presentationRepository.findAll();
        }

        return presentationRepository.findBy("movie_id", Integer.valueOf(movieId));
    }
}
