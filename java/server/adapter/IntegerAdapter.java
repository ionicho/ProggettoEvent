package server.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.*;
import java.io.IOException;

/**
 * Classe che estende {@link com.google.gson.TypeAdapter} per la 
 * gestione della deserializzazione e serializzazione di oggetti 
 * di tipo {@link java.lang.Integer}.
 */

public class IntegerAdapter extends TypeAdapter<Integer> {
    @Override
    public void write(JsonWriter out, Integer value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value);
        }
    }

    @Override
    public Integer read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        try {
           return in.nextInt();
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
