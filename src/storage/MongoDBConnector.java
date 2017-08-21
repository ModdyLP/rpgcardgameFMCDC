package storage;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collections;

public class MongoDBConnector {

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    private static MongoDBConnector instance;

    public static MongoDBConnector getInstance() {
        if (instance == null) {
            instance = new MongoDBConnector();
        }
        return instance;
    }
    public void connect() {
        ServerAddress serverlist = new ServerAddress("moddylp.de", 27017);
        MongoCredential credential = MongoCredential.createCredential("admin", "admin", "mango4567".toCharArray());
        mongoClient = new MongoClient(serverlist, Collections.singletonList(credential));
        mongoDatabase = mongoClient.getDatabase("code_camp");
    }
    public MongoClient getMongoClient() {
        return mongoClient;
    }
    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }
    public void close() {
        mongoClient.close();
    }
    public ArrayList<Document> getCollectionAsList(String collectionnname) {
        return mongoDatabase.getCollection(collectionnname).find().into(new ArrayList<>());
    }

}
