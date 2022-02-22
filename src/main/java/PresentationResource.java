import Database.Repository.MovieRepository;
import Database.Repository.PresentationRepository;
import Models.Movie;
import Models.Presentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import java.util.List;

public class PresentationResource extends ServerResource {

    @Get("json")
    public List<Presentation> represent() {
        PresentationRepository presentationRepository = new PresentationRepository();
        return presentationRepository.findAll();
    }
}
