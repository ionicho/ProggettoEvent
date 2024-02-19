package server.service;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.*;
import java.io.IOException;

public class IntegerTypeAdapter extends TypeAdapter<Integer> {
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
