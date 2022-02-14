import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.engine.Engine;
import org.restlet.ext.jackson.JacksonConverter;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Router;

import java.util.Map;

public class Server extends Application {

    @Override
    public synchronized Restlet createInboundRoot() {
        Router router = new Router(getContext());

        Engine.getInstance().getRegisteredConverters().add(new JacksonConverter());

        for (Map.Entry<String, Class<? extends ServerResource>> route: RoutingConfig.ROUTES.entrySet()) {
            router.attach(route.getKey(), route.getValue());
        }

        return router;
    }

}