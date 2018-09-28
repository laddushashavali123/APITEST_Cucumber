package br.com.experian.cucumber.integration.cucumber.mongo;

import static br.com.experian.cucumber.integration.cucumber.common.utils.PropertiesUtil.getProperty;
import static java.lang.Integer.parseInt;
import static java.util.Collections.singletonList;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

/**
 * Created by Kaio Gonzaga on 15/12/2017.
 */
public class MongoConnection {

    private static MongoClient mongoClient = new MongoClient();

    static {
        ServerAddress serverAddress = new ServerAddress(getProperty("mongo.host"), parseInt(getProperty("mongo.port")));

        if (!"localhost".equals(getProperty("mongo.host"))) {
            MongoCredential credential = MongoCredential.createCredential(
                    getProperty("mongo.user"),
                    getProperty("mongo.database"),
                    getProperty("mongo.password").toCharArray());
            mongoClient = new MongoClient(serverAddress, singletonList(credential));
        }
        mongoClient = new MongoClient(serverAddress);
    }

    public MongoDatabase getDatabase() {
        return mongoClient.getDatabase(getProperty("mongo.database"));
    }
}