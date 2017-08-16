package storage;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import java.util.Arrays;
import java.util.Collections;

public class MongoDBConnector {

    private MongoClient mongoClient;

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
    }
    public MongoClient getMongoClient() {
        return mongoClient;
    }
    public void close() {
        mongoClient.close();
    }

}
