package server.service;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;

import server.model.*;

public class StateDateTypeAdapter extends TypeAdapter<StateDate> {

    @Override
    public void write(JsonWriter out, StateDate stateDate) throws IOException {
        out.beginObject();
        out.name("data").value(stateDate.getData().toString());
        out.name("stato").value(stateDate.getStato().toString());
        out.endObject();
    }

    @Override
    public StateDate read(JsonReader in) throws IOException {
        LocalDate data = null;
        State stato = null;

        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "data":
                    data = LocalDate.parse(in.nextString());
                    break;
                case "stato":
                    stato = State.valueOf(in.nextString());
                    break;
            }
        }
        in.endObject();

        return new StateDate(data, stato);
    }
}
