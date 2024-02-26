package server.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.*;
import java.io.IOException;
import java.time.LocalDate;

/**
 * Classe che estende {@link com.google.gson.TypeAdapter} per la 
 * gestione della deserializzazione e serializzazione di oggetti 
 * di tipo {@link java.time.LocalDate}.
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
