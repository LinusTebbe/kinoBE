package Resources;

import Database.Repository.PresentationRepository;
import Models.Presentation;
import Util.Serializers;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import java.util.List;

public class PresentationResource extends ServerResource {

    @Get("json")
    public String represent() {
        return Serializers.getForPresentationSet().deepSerialize(this.getPresentations());
    }

    private List<Presentation> getPresentations() {
        PresentationRepository presentationRepository = new PresentationRepository();

        String movieId = this.getQuery().getValues("movie_id");

        if (movieId == null || movieId.isEmpty()) {
            return presentationRepository.findAll();
        }

        return presentationRepository.findBy("movie_id", Integer.valueOf(movieId));
    }
}
