import org.restlet.Application;
import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.service.CorsService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Launch {
    public static void main(String[] args) throws Exception {
        // Create a new Component.
        Component component = new Component();

        // Add a new HTTP server listening on port 8182.
        component.getServers().add(Protocol.HTTP, 8182);

        Application application = new Server();
        component.getDefaultHost().attachDefault(application);

        CorsService corsService = new CorsService();
        corsService.setAllowedOrigins(new HashSet<>(List.of("*")));
        corsService.setAllowedCredentials(true);

        application.getServices().add(corsService);


        // Start the component.
        component.start();
    }

}
