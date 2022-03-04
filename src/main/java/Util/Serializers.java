package Util;

import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;

import java.util.Date;

public class Serializers {
    public static final JSONSerializer presentationsGet = Serializers.getForPresentationSet();

    private static JSONSerializer getBasicJSONSerializer() {
        return new JSONSerializer().transform(
                new DateTransformer("yyyy-MM-dd'T'HH:mm:ss"), Date.class
        ).exclude("*.class");
    }

    public static JSONSerializer getForPresentationSet() {
        return Serializers.getBasicJSONSerializer().include(
                "start",
                "cinemaHall",
                "cinemaHall.seatRows.seats.position"
        );
    }
}

