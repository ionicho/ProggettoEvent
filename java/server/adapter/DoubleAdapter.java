package server.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.*;
import java.io.IOException;

/**
 * Classe che estende {@link com.google.gson.TypeAdapter} per la 
 * gestione della deserializzazione e serializzazione di oggetti 
 * di tipo {@link java.lang.Double}.
 */

public class DoubleAdapter extends TypeAdapter<Double> {
    @Override
    public void write(JsonWriter out, Double value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value);
        }
    }

    @Override
    public Double read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        try {
           return in.nextDouble();
        } catch (NumberFormatException e) {
            return null;
        }
    }
}