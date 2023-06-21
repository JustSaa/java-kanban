package model.utils;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.TypeAdapter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    public static final DateTimeFormatter formatterWriter = DateTimeFormatter.ofPattern("dd.MM.yyyy;HH:mm");
    public static final DateTimeFormatter formatterReader = DateTimeFormatter.ofPattern("dd.MM.yyyy;HH:mm");

    @Override
    public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
        if (localDateTime != null) {
            jsonWriter.value(localDateTime.format(formatterWriter));
        } else {
            jsonWriter.nullValue();
        }
    }

    @Override
    public LocalDateTime read(JsonReader jsonReader) throws IOException {
        return LocalDateTime.parse(jsonReader.nextString(), formatterReader);
    }
}