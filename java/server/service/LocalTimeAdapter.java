package server.service;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.*;
import java.io.IOException;
import java.time.LocalTime;

/**
 * Classe di servizio per la serializzazione e deserializzazione delle ore.
 * in formato LocalTime
 */

public class LocalTimeTypeAdapter extends TypeAdapter<LocalTime> {

    @Override
    public LocalTime read(JsonReader reader) throws IOException {
        String text = reader.nextString();
        return LocalTime.parse(text);
    }

    @Override
    public void write(JsonWriter writer, LocalTime time) throws IOException {
        if (time == null) {
            writer.nullValue();
        } else {
            writer.value(time.toString());
        }
    }
}

