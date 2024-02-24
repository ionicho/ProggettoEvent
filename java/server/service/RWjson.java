package server.service;

import com.google.gson.Gson;
import java.lang.reflect.Type;
import java.io.*;
import java.util.*;

public interface RWjson <T>{

    public default List<T> caricadaDB(String DBname, Gson myGson, Type typeOfT) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(DBname));
            return myGson.fromJson(br, typeOfT);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public default void salvaNelDB(String DBname, Gson myGson, List<T> myList) {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(DBname));
            myList.removeAll(Collections.singleton(null));
            pw.println(myGson.toJson(myList));
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public default void salvaNelDB(String DBname, Gson myGson, T myObj) {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(DBname));
            pw.println(myGson.toJson(myObj));
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}