package Util;

import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;

import java.util.Date;

public class Serializers {
    private static JSONSerializer getBasicJSONSerializer() {
        return new JSONSerializer().transform(
                new DateTransformer("yyyy-MM-dd'T'HH:mm:ss"), Date.class
        ).exclude("*.class", "*.initialHash");
    }

    public static JSONSerializer getForPresentationSet() {
        return Serializers.getBasicJSONSerializer().include(
                "id",
                "start",
                "cinemaHall.seatRows.seats.id",
                "cinemaHall.seatRows.seats.position",
                "cinemaHall.seatRows.seats.reservations.presentation.id"
        ).exclude("*");
    }

    public static JSONSerializer getForMoviesSet() {
        return Serializers.getBasicJSONSerializer().include(
                "id",
                "title",
                "imagePath",
                "shortDescription",
                "longDescription",
                "releasedDate",
                "duration",
                "category.*",
                "ageCategory.*",
                "is3D",
                "isDolbyAtmos"
        ).exclude("*");
    }
}

