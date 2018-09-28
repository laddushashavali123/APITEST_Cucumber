package br.com.experian.cucumber.integration.cucumber.mongo;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;

import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.pull;
import static com.mongodb.client.model.Updates.push;
import static org.bson.Document.parse;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by Kaio Gonzaga on 15/12/2017.
 */
public class MongoRepository {
    private MongoDatabase mongoDatabase;

    public MongoRepository() {
        if(mongoDatabase == null)
        	this.mongoDatabase = new MongoConnection().getDatabase();
    }

    public void save(String collection, String document) {
        mongoDatabase.getCollection(collection).insertOne(parse(document));
    }

    public void deleteOne(String collection, Document document) {
        mongoDatabase.getCollection(collection).deleteOne(document);
    }

    public void deleteMany(String collection, Document document) {
        mongoDatabase.getCollection(collection).deleteMany(document);
    }

    public void addToGroup(String groupName, String id) {
        mongoDatabase.getCollection("Group").updateOne(eq("name", groupName), push("ids", id));
    }

    public void removeToGroup(String groupName, String id) {
        mongoDatabase.getCollection("Group").updateOne(eq("name", groupName), pull("ids", id));
    }

    public void addBusinessGroup(String businessId, String groupName, String userId) {
        Document document = mongoDatabase.getCollection("UserCredential").find(eq("userId", userId)).first();

        Document userDocument = new Document();
        userDocument.put("$ref", "UserCredential");
        userDocument.put("$id", document.get("_id"));

        mongoDatabase.getCollection("BusinessGroup").updateOne(and(eq("businessId", businessId),
                                eq("groupId", groupName)), push("users", userDocument));
    }
    
    public void removeUserCompany(String userId, String companyId){
    	mongoDatabase.getCollection("UserAccount").updateOne(eq("_id", new ObjectId(userId)),
    			pull("companies", new Document("$id", new ObjectId(companyId))));
    }
    
    public String findTokenByUserAccountId(String id){
    	String token = null;
    	Document userAccount = mongoDatabase.getCollection("UserAccount")
    			.find(eq("_id", new ObjectId(id)))
    			.first();
    	
    	if(userAccount != null && userAccount.get("validationCodes") != null){
    		List<Document> validationTokens = (List<Document>) userAccount.get("validationCodes");
    		if(!validationTokens.isEmpty()){
    			Optional<String> oToken = validationTokens.stream()
    					.filter(v -> v.get("used").equals(false))
    					.map(v -> v.getString("token"))
    					.findFirst();
    			
    			if(oToken.isPresent())
    				token = oToken.get();
    		}	
    	}
    	
    	return token;
    }
    
    public String findTokenByUserId(String userId){
    	Document userAccount = mongoDatabase.getCollection("UserCredential")
    			.find(and(eq("userId", userId),
                        eq("enabled", true),
                        eq("tokenValidation.used", false)))
    			.first();

    	String token = null;
        if(userAccount != null && userAccount.get("tokenValidation") != null){
            List<Document> validationTokens = (List<Document>) userAccount.get("tokenValidation");
            if(!validationTokens.isEmpty()){
                Optional<String> oToken = validationTokens.stream()
                        .filter(v -> v.get("used").equals(false))
                        .map(v -> v.getString("token"))
                        .findFirst();

                if(oToken.isPresent())
                    token = oToken.get();
            }
        }
    	return token;
    }
}