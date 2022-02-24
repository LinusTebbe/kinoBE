package Database;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomDateSerializer extends StdSerializer<LocalDateTime> {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;

    public CustomDateSerializer() {
        super(LocalDateTime.class);
    }

    @Override
    public void serialize (
            LocalDateTime value,
            JsonGenerator generator,
            SerializerProvider provider
    ) throws IOException {
        generator.writeString(CustomDateSerializer.dateTimeFormatter.format(value));
    }
}