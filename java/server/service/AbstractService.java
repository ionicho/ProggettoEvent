package server.service;

import com.google.gson.Gson;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import com.mongodb.client.result.*;
import org.bson.conversions.Bson;
import org.bson.Document;

import java.lang.reflect.Type;
import java.io.*;
import java.util.*;
import server.AppConfig;
import server.model.*;

/**
 * Superclasse per i service che gestiscono le risorse e rispondono ai controller.
 * @param <T> tipo della risorsa
 * @param <Type> tipo della lista di risorse
 * getRisorse() restituisce la lista delle risorse
 * getRisorsa(String id) restituisce la risorsa con l'id specificato
 * deleteRisorsa(String id) elimina la risorsa con l'id specificato
 * updateRisorsa(T nuova) aggiorna la risorsa con i dati della risorsa passata
 * caricadaDB(String myDBname, Gson myGson, Type typeOfT) carica la lista di risorse dal DB
 * salvaNelDB(String myDBname, Gson myGson, List<T> myList) salva la lista di risorse nel DB
 * salvaNelDB(String myDBname, Gson myGson, T myObj) salva la risorsa nel DB
 */

public abstract class AbstractService <T extends HasName>{

    protected String myDBname;
    protected String myColl;
    protected List<T> risorse;
    protected Gson myGson;
    protected MongoTemplate myMongoDB;
    protected boolean useMongoDB;
    private String errorMsg = "Risorsa non esistente";
    private Type typeOfT;

    protected AbstractService(String myName, Gson myGson, MongoTemplate myMongoDB, Type typeOfT) {
    	this.myDBname = AppConfig.DATABASE_ROOT_PATH+ myName + ".json";
    	this.myColl = myName;
        this.myGson = myGson;
        this.myMongoDB = myMongoDB;
        this.useMongoDB = AppConfig.useMongoDB();
        this.typeOfT = typeOfT;
        this.risorse = caricadaDB(myDBname, myGson, typeOfT);
    }

    public List<T> getRisorse() {
        return risorse;
    }

    public T getRisorsa(String id) {
        for (T curr : risorse) {
            if (curr.getNome().compareTo(id)==0) {
                return curr;
            }
        }
        return null;
    }
    
    public void deleteRisorsa(String id) {
        if (useMongoDB) {
            MongoCollection<Document> collection = myMongoDB.getCollection(myColl);         
            Document filter = new Document("nome", id);
            DeleteResult result = collection.deleteOne(filter);
            if (result.getDeletedCount() == 0) {
                throw new IllegalArgumentException(errorMsg);
            }
        } else {
            T toRemove = null;
            for (T curr : risorse) {
                if (curr.getNome().compareTo(id) == 0) {
                    toRemove = curr;
                    break;
                }
            }
            if (toRemove != null) {
                risorse.remove(toRemove);
                salvaNelDB(myDBname, myGson, risorse);
            } else {
                throw new IllegalArgumentException(errorMsg);
            }
        }
    }
   
    public T updateRisorsa(T nuova) {
        if (useMongoDB) {
            MongoCollection<Document> collection = myMongoDB.getCollection(myColl);              
            Bson filter = Filters.eq("nome", nuova.getNome());
            Document newDocument = Document.parse(myGson.toJson(nuova));
            Bson update = new Document("$set", newDocument);  
            UpdateResult result = collection.updateOne(filter, update);         
            if (result.getMatchedCount() == 0) {
                throw new IllegalArgumentException(errorMsg);
            }
            this.risorse = caricadaDB(myDBname, myGson, typeOfT);
            return nuova;
        } else {
            for (T curr : risorse) {
                if (curr.getNome().compareTo(nuova.getNome()) == 0) {
                    risorse.remove(curr);
                    risorse.add(nuova);
                    salvaNelDB(myDBname, myGson, risorse);
                    this.risorse = caricadaDB(myDBname, myGson, typeOfT);
                    return nuova;
                }
            }
            throw new IllegalArgumentException(errorMsg);
        }
    }
    
    public List<T>updateRisorse(List<T> nuove) {
        risorse = nuove;
        if (useMongoDB) {
            MongoCollection<Document> collection = myMongoDB.getCollection(myColl);                collection.deleteMany(new Document()); // Elimina tutti i documenti esistenti
            for (T risorsa : risorse) {
                Document doc = Document.parse(myGson.toJson(risorsa));
                collection.insertOne(doc);
            }
        } else {
            salvaNelDB(myDBname, myGson, risorse);
        }
        risorse = caricadaDB(myDBname, myGson, typeOfT);
        return risorse;
    }

    public List<T> caricadaDB(String myDBname, Gson myGson, Type typeOfT) {
    	if (useMongoDB) {
    	    List<T> ris = new ArrayList<>();
    	    MongoCollection<Document> collection = myMongoDB.getCollection(myColl);
    	    FindIterable<Document> documents = collection.find();
    	    for (Document doc : documents) {
    	        T risorsa = myGson.fromJson(doc.toJson(), typeOfT);
    	        ris.add(risorsa);
    	    }
    	    return ris;	
    	} else {
	        try {
	            BufferedReader br = new BufferedReader(new FileReader(myDBname));
	            return myGson.fromJson(br, typeOfT);
	        } catch (IOException e) {
	            return new ArrayList<>();
	        }
    	}
    }

    public void salvaNelDB(String myDBname, Gson myGson, List<T> myList) {
        if (useMongoDB) {
            MongoCollection<Document> collection = myMongoDB.getCollection(myColl);                collection.drop(); // Opzionale: Pulisce la collezione esistente
            for (T risorsa : myList) {
                Document doc = Document.parse(myGson.toJson(risorsa));
                collection.insertOne(doc);
            }
        } else {
            try (PrintWriter pw = new PrintWriter(new FileWriter(myDBname))) {
                myList.removeAll(Collections.singleton(null));
                pw.println(myGson.toJson(myList));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void salvaNelDB(String myDBname, Gson myGson, T myObj) {
        if (useMongoDB) {
            MongoCollection<Document> collection = myMongoDB.getCollection(myColl);                // Converti l'oggetto in JSON e poi in Document
            Document doc = Document.parse(myGson.toJson(myObj));
            // Inserisci o aggiorna il documento
            collection.replaceOne(
                new Document("nome", myObj.getNome()), // Filtra per il campo identificativo
                doc,
                new ReplaceOptions().upsert(true) // Usa l'upsert per inserire o aggiornare
            );
        } else {
            try (PrintWriter pw = new PrintWriter(new FileWriter(myDBname))) {
                pw.println(myGson.toJson(myObj));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}