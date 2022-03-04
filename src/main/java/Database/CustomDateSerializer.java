package Database;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CustomDateSerializer extends StdSerializer<Date> {

    private static final DateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public CustomDateSerializer() {
        super(Date.class);
    }

    @Override
    public void serialize (
            Date value,
            JsonGenerator generator,
            SerializerProvider provider
    ) throws IOException {
        generator.writeString(CustomDateSerializer.dateTimeFormatter.format(value));
    }
}