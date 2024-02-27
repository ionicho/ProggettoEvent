package server.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.*;
import java.io.IOException;
import java.time.LocalTime;

/**
 * Classe che estende {@link com.google.gson.TypeAdapter} per la 
 * gestione della deserializzazione e serializzazione di oggetti 
 * di tipo {@link java.time.LocalTime}.
 */

public class LocalTimeAdapter extends TypeAdapter<LocalTime> {

    @Override
    public LocalTime read(JsonReader reader) throws IOException {
        String text = reader.nextString();
        if (text.length() == 4) {
            text = "0" + text;  // aggiunge uno zero iniziale se mancante
        }
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

