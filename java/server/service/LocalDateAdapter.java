package server.service;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.*;
import java.io.IOException;
import java.time.LocalDate;

/**
 * Classe di servizio per la serializzazione e deserializzazione delle date.
 * in formato LocalDate
 */

public class LocalDateAdapter extends TypeAdapter<LocalDate> {

    @Override
    public LocalDate read(JsonReader reader) throws IOException {
        String text = reader.nextString();
        return LocalDate.parse(text);
    }

    @Override
    public void write(JsonWriter writer, LocalDate date) throws IOException {
        if (date == null) {
            writer.nullValue();
        } else {
            writer.value(date.toString());
        }
    }

}
