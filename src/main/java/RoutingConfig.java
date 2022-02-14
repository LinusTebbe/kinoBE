import org.restlet.resource.ServerResource;

import java.util.Map;

public class RoutingConfig {
    public static Map<String, Class<? extends ServerResource>> ROUTES = Map.ofEntries(
            Map.entry("/movies", MoviesResource.class)
    );
}
