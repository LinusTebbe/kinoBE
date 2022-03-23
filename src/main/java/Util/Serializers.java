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
                "movie.title",
                "movie.priceLodge",
                "movie.priceParquet",
                "cinemaHall.seatRows.id",
                "cinemaHall.seatRows.number",
                "cinemaHall.seatRows.seatRowType",
                "cinemaHall.seatRows.seats.id",
                "cinemaHall.seatRows.seats.identifier",
                "cinemaHall.seatRows.seats.position",
                "cinemaHall.seatRows.seats.seatType",
                "cinemaHall.seatRows.seats.seatRowType",
                "cinemaHall.seatRows.seats.reservations.presentationId"
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

