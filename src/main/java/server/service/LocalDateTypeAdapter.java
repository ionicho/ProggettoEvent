package server.service;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;

public class LocalDateTypeAdapter extends TypeAdapter<LocalDate> {

    @Override
    public LocalDate read(JsonReader reader) throws IOException {
        String text = reader.nextString();
        return LocalDate.parse(text);
    }

    @Override
    public void write(JsonWriter writer, LocalDate date) throws IOException {
        writer.value(date.toString());
    }
}
