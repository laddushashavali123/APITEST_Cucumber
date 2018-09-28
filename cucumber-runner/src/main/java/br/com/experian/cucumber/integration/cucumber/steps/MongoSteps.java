package br.com.experian.cucumber.integration.cucumber.steps;

import br.com.experian.cucumber.integration.cucumber.common.utils.RestApi;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DBRef;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import static br.com.experian.cucumber.integration.cucumber.common.utils.PropertiesUtil.getProperty;
import static br.com.experian.cucumber.integration.cucumber.common.utils.RestApi.replaceVariablesValues;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.set;
import static java.lang.Integer.parseInt;
import static java.util.Collections.singletonList;
import static org.junit.Assert.*;

public class MongoSteps extends MainSteps {

	private MongoClient mongoClient;
	private MongoDatabase mongoDatabase;
	
	public String token = "";
	
	public void init() {
		mongoClient = null;
		mongoDatabase = null;
	}

	@Given("^I connect with the database$")
	public void connectDatabase() throws Throwable {
		if (Objects.nonNull(mongoDatabase))
			return;

		ServerAddress serverAddress = new ServerAddress(getProperty("mongo.host"), parseInt(getProperty("mongo.port")));
		try {
			if ("localhost".equals(getProperty("mongo.host"))) {
				mongoClient = new MongoClient(serverAddress);
			} else {
				MongoCredential credential = MongoCredential.createCredential(getProperty("mongo.user"),
						getProperty("mongo.database"), getProperty("mongo.password").toCharArray());
				mongoClient = new MongoClient(serverAddress, singletonList(credential));
			}
			mongoClient.getAddress();
		} catch (Exception e) {
			Assert.fail("Error connecting mongo client: " + e.getMessage());
			closeDatabase();
		}
		mongoDatabase = mongoClient.getDatabase(getProperty("mongo.database"));
	}

	@Given("^I close database$")
	public void closeDatabase() throws Throwable {
		if (Objects.nonNull(mongoClient)) {
			mongoClient.close();
			mongoDatabase = null;
		}
	};

	@Then("^I insert document into \"([^\"]*)\" collection:$")
	public void insertDataCollection(String collectionName, DataTable table) {
		assertNotNull("You need to connect to the database", mongoDatabase);

		MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
		assertNotNull("No collections found with the name " + collectionName + "!", collection);

		for (Map<String, String> item : table.asMaps(String.class, String.class)) {
			Document document = Document.parse(item.get("document"));
			collection.insertOne(document);
			String variableId = item.get("variableId");
			RestApi.getUserParameters().put("${" + variableId + "}", document.getObjectId("_id").toHexString());
			Hooks.scenario.write("${" + variableId + "} = " + RestApi.getUserParameters().get("${" + variableId + "}"));
		}
	}

	@Then("^I set the field \"([^\"]*)\" to \"([^\"]*)\" on the collection \"([^\"]*)\" with key \"([^\"]*)\" as \"([^\"]*)\"$")
	public void modifiedValueInCollection(String field, String value, String collectionName, String key,
			String keyValue) throws Throwable {

		field = replaceVariablesValues(field);
		value = replaceVariablesValues(value);
		keyValue = replaceVariablesValues(keyValue);

		assertFalse("Field is required!", StringUtils.isEmpty(field));
		assertFalse("Value is required!", StringUtils.isEmpty(value));
		assertFalse("Collection name is required!", StringUtils.isEmpty(collectionName));
		assertFalse("Key is required!", StringUtils.isEmpty(key));
		assertFalse("Key Value is required!", StringUtils.isEmpty(keyValue));
		assertNotNull("You need to connect to the database", mongoDatabase);

		MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
		assertNotNull("No collections found with the name " + collectionName + "!", collection);

		Bson filter = eq(key, key.contains(".") ? keyValue : new ObjectId(keyValue));
		Bson setter = set(field, generateObjectValue(value));

		UpdateResult updateResult = collection.updateOne(filter, setter);

		assertNotEquals("No values were found with the " + key + " = " + keyValue + "!", 0,
				updateResult.getMatchedCount());
		Hooks.scenario.write(updateResult.getModifiedCount() == 0 ? "Não alterado" : "Alterado");
	}

	@Then("^I get the field \"([^\"]*)\" to \"([^\"]*)\" on the collection \"([^\"]*)\" with key \"([^\"]*)\" as \"([^\"]*)\"$")
	public void getValueInCollection(String field, String result, String collectionName, String key, String keyValue)
			throws Throwable {

		field = replaceVariablesValues(field);
		keyValue = replaceVariablesValues(keyValue);

		assertFalse("Field is required!", StringUtils.isEmpty(field));
		assertFalse("Collection name is required!", StringUtils.isEmpty(collectionName));
		assertFalse("Key is required!", StringUtils.isEmpty(key));
		assertFalse("Key Value is required!", StringUtils.isEmpty(keyValue));
		assertNotNull("You need to connect to the database", mongoDatabase);
		
		MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
		assertNotNull("No collections found with the name " + collectionName + "!", collection);

		Bson filter = eq(key, keyValue);

		if ("_id".equals(key)) {
			filter = eq(key, new ObjectId(keyValue));
		}
		
		if (field.contains(".")) {
			List<String> fiedJson = Arrays.asList(field.replace(".", "#").split("#"));
			Document document = collection.find(filter).first();

			for (String jsonField : fiedJson.subList(0, fiedJson.size() - 1))
				document = (Document) document.get(jsonField);
			
			if (Objects.nonNull(document)) {
				RestApi.getUserParameters().put("${" + result + "}", document.get(fiedJson.get(fiedJson.size() - 1)).toString());
				Hooks.scenario.write(RestApi.getUserParameters().get("${" + result + "}"));
			}
			
		} else {
			
			Document find = collection.find(filter).first();
			
			assertTrue("No values were found with the " + key + " = " + keyValue + "!", find != null);
			
			if (Objects.nonNull(find)) {
				RestApi.getUserParameters().put("${" + result + "}", find.get(field).toString());
				Hooks.scenario.write(RestApi.getUserParameters().get("${" + result + "}"));
			}
		}

	}

	@Then("^I get the field \"([^\"]*)\" to \"([^\"]*)\" on the collection \"([^\"]*)\" with key \"([^\"]*)\" not existent")
	public void getValueInCollection(String field, String result, String collectionName, String key) throws Throwable {

		field = replaceVariablesValues(field);

		assertFalse("Field is required!", StringUtils.isEmpty(field));
		assertFalse("Collection name is required!", StringUtils.isEmpty(collectionName));
		assertFalse("Key is required!", StringUtils.isEmpty(key));
		assertNotNull("You need to connect to the database", mongoDatabase);

		MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
		assertNotNull("No collections found with the name " + collectionName + "!", collection);

		Bson filter = not(exists(key));

		CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry());
		final DocumentCodec codec = new DocumentCodec(codecRegistry, new BsonTypeClassMap());

		MongoCursor<Document> cursor = collection.find(filter).iterator();
		JSONObject document = null;
		while (cursor.hasNext()) {
			document = new JSONObject(cursor.next().toJson(codec));
		}

		if (document != null) {
			String resultField = null;
			if ("_id".equals(field)) {
				JSONObject id = (JSONObject) document.get(field);
				resultField = (String) id.get("$oid");
			} else {
				resultField = (String) document.get(field);
			}
			RestApi.getUserParameters().put("${" + result + "}", resultField);
			Hooks.scenario.write(RestApi.getUserParameters().get("${" + result + "}"));
		}

		assertTrue("No values were found with the " + key + " not existent!", document != null);
	}

	@Then("^I get the field \"([^\"]*)\" to \"([^\"]*)\" on the collection \"([^\"]*)\" with key \"([^\"]*)\" existent")
	public void getValueInCollectionExistent(String field, String result, String collectionName, String key)
			throws Throwable {

		field = replaceVariablesValues(field);

		assertFalse("Field is required!", StringUtils.isEmpty(field));
		assertFalse("Collection name is required!", StringUtils.isEmpty(collectionName));
		assertFalse("Key is required!", StringUtils.isEmpty(key));
		assertNotNull("You need to connect to the database", mongoDatabase);

		MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
		assertNotNull("No collections found with the name " + collectionName + "!", collection);

		Bson filter = Filters.and(exists(key), Filters.not(Filters.eq("")));

		CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry());
		final DocumentCodec codec = new DocumentCodec(codecRegistry, new BsonTypeClassMap());

		MongoCursor<Document> cursor = collection.find(filter).iterator();
		JSONObject document = null;
		while (cursor.hasNext()) {
			document = new JSONObject(cursor.next().toJson(codec));
		}

		if (document != null) {
			String resultField = null;
			if ("_id".equals(field)) {
				JSONObject id = (JSONObject) document.get(field);
				resultField = (String) id.get("$oid");
			} else {
				resultField = (String) document.get(field);
			}
			RestApi.getUserParameters().put("${" + result + "}", resultField);
			Hooks.scenario.write(RestApi.getUserParameters().get("${" + result + "}"));
		}

		assertTrue("No values were found with the " + key + " existent!", document != null);
	}

	@Then("^I get by id the field \"([^\"]*)\" to \"([^\"]*)\" on the collection \"([^\"]*)\" with key \"([^\"]*)\" as \"([^\"]*)\"$")
	public void getValueIdInCollection(String field, String result, String collectionName, String key, String keyValue)
			throws Throwable {

		field = replaceVariablesValues(field);
		keyValue = replaceVariablesValues(keyValue);

		assertFalse("Field is required!", StringUtils.isEmpty(field));
		assertFalse("Collection name is required!", StringUtils.isEmpty(collectionName));
		assertFalse("Key is required!", StringUtils.isEmpty(key));
		assertFalse("Key Value is required!", StringUtils.isEmpty(keyValue));
		assertNotNull("You need to connect to the database", mongoDatabase);

		MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);

		assertNotNull("No collections found with the name " + collectionName + "!", collection);

		Bson filter = eq(key, new DBRef("Lender", new ObjectId(keyValue)));

		CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry());
		final DocumentCodec codec = new DocumentCodec(codecRegistry, new BsonTypeClassMap());

		MongoCursor<Document> cursor = collection.find(filter).iterator();
		JSONObject document = null;
		while (cursor.hasNext()) {
			document = new JSONObject(cursor.next().toJson(codec));
		}

		if (document != null) {
			String[] fields = field.split(Pattern.quote("."));
			JSONObject subDocument = document;
			String resultField = null;
			for (int i = 0; i < fields.length; i++) {
				if (subDocument.has(fields[i]) && subDocument.get(fields[i]) instanceof JSONArray) {
					JSONArray listObjects = (JSONArray) subDocument.get(fields[i]);
					String[] keys = key.split(Pattern.quote("."));
					for (int y = 0; y < listObjects.length(); y++) {
						JSONObject jsonObject = listObjects.getJSONObject(y);
						for (int a = 0; a < keys.length; a++) {
							if (jsonObject.has(keys[a])) {
								key = keys[a];
								break;
							}
						}
						if (jsonObject.has(key) && jsonObject.get(key) instanceof JSONObject) {
							JSONObject subObject = (JSONObject) jsonObject.get(key);
							// Verifica se é DBRef
							if (subObject.has("$id")) {
								JSONObject idObject = (JSONObject) subObject.get("$id");
								if (keyValue.equals(idObject.get("$oid"))) {
									subDocument = jsonObject;
									break;
								}
							}
						}
						if (jsonObject.has(key)
								&& (jsonObject.get(key) instanceof String || jsonObject.get(key) instanceof Integer)) {
							if (keyValue.equals((String) jsonObject.get(key))) {
								subDocument = jsonObject;
								break;
							}
						}
					}
				}
				if (subDocument.has(fields[i]) && subDocument.get(fields[i]) instanceof JSONObject) {
					subDocument = (JSONObject) document.get(fields[i]);
				}
				if (subDocument.has(fields[i]) && (subDocument.get(fields[i]) instanceof String
						|| subDocument.get(fields[i]) instanceof Integer)) {
					if ("_id".equals(fields[i])) {
						if (subDocument.has("$oid")) {
							resultField = (String) subDocument.get("$oid");
						} else {
							resultField = (String) subDocument.get(fields[i]);
						}
					} else {
						resultField = (String) subDocument.get(fields[i]);
					}
				}
			}
			RestApi.getUserParameters().put("${" + result + "}", resultField);
			Hooks.scenario.write(RestApi.getUserParameters().get("${" + result + "}"));
		}

		assertTrue("No values were found with the " + key + " = " + keyValue + "!", document != null);
	}
	
    @Given("^I use the database host \"([^\"]*)\" with port \"([^\"]*)\" and database \"([^\"]*)\"$")
    public void setDatabaseConnection(String host, String port, String database) throws Throwable {
		RestApi.setHost(host);
		RestApi.setDatabase(database);
		RestApi.setPort(Integer.parseInt(port));
        
        if (mongoDatabase == null) {
        	mongoClient = new MongoClient(RestApi.getHost() , RestApi.getPort());
       		mongoDatabase = mongoClient.getDatabase(RestApi.getDatabase());
       	}
    }
    
	@Then("^I find value in Collection \\\"([^\\\"]*)\\\" by field \\\"([^\\\"]*)\\\" with value equals \\\"([^\\\"]*)\\\" and save value by field \\\"([^\\\"]*)\\\" in variable \\\"([^\\\"]*)\\\"$")
	public void findValueInCollectionByFieldAndSaveByFieldInVariable(String collectionName, String fieldSearchCollecction, String valueInCollecction, String valueSave, String nameVariable) {
		Document document = mongoDatabase.getCollection(collectionName).find(eq(fieldSearchCollecction, valueInCollecction)).first();
		if (Objects.nonNull(document)) {
			RestApi.getUserParameters().put("${" + nameVariable + "}", document.get(valueSave).toString());
			Hooks.scenario.write(RestApi.getUserParameters().get("${" + nameVariable + "}"));
		}
	}
	
	@Then("^I remove in Document \"([^\"]*)\" by collum \"([^\"]*)\" where value equals \"([^\"]*)\"$") 
	public void removeInCollection(String collectionName, String fieldSearchCollecction, String valueInCollecction) {
		DeleteResult delete = mongoDatabase.getCollection(collectionName).deleteMany(eq(fieldSearchCollecction, valueInCollecction));
	};

    @Then("^I check if the field \"([^\"]*)\" is \"([^\"]*)\" on the collection \"([^\"]*)\" where the key \"([^\"]*)\" contains \"([^\"]*)\"$")
    public void findValueInCollectionWithContains(String field, String value, String collectionName, String key, String keyValue) throws Throwable {
    	
    	field = replaceVariablesValues(field);
    	value = replaceVariablesValues(value);
    	keyValue = replaceVariablesValues(keyValue);
    	
    	assertFalse("Field is required!", StringUtils.isEmpty(field));
    	assertFalse("Value is required!", StringUtils.isEmpty(value));
    	assertFalse("Collection name is required!", StringUtils.isEmpty(collectionName));
    	assertFalse("Key is required!", StringUtils.isEmpty(key));
    	assertFalse("Key Value is required!", StringUtils.isEmpty(keyValue));
    	
    	try(MongoClient mongo = new MongoClient(RestApi.getHost() , RestApi.getPort())) {
    		
    		MongoDatabase database = mongo.getDatabase(RestApi.getDatabase());
    		
    		MongoCollection<Document> collection = null;
    		for(String name : database.listCollectionNames()) {
    			if(collectionName.equalsIgnoreCase(name)) {
    				collection = database.getCollection(collectionName);
    			}
    		}
    		
    		assertNotNull("No collections found with the name " + collectionName + "!", collection);
    		
    		Bson filter = regex(key, keyValue);
    		
    		Document document = collection.find(filter).first();

    		assertNotNull("No document were found with the " + key + " value containing " + keyValue + "!", document);
    		assertNotNull("No values were found with the " + key + " value containing " + keyValue + "!", document.get(field));
    		assertEquals("The value found doesn't match the searched value " + value, value, document.get(field).toString());
    		Hooks.scenario.write("_id = " + document.getObjectId("_id").toHexString());
    		Hooks.scenario.write(field + " = " + value);
    	}
    }

	@Then("^I save the field \"([^\"]*)\" of the collection \"([^\"]*)\" where the key \"([^\"]*)\" contains \"([^\"]*)\" as \"([^\"]*)\"$")
	public void saveValueOfCollectionWithContains(String field, String collectionName, String key, String keyValue, String saveKey) throws Throwable {

		field = replaceVariablesValues(field);
		keyValue = replaceVariablesValues(keyValue);

		assertFalse("Field is required!", StringUtils.isEmpty(field));
		assertFalse("Collection name is required!", StringUtils.isEmpty(collectionName));
		assertFalse("Key is required!", StringUtils.isEmpty(key));
		assertFalse("Key Value is required!", StringUtils.isEmpty(keyValue));

		try(MongoClient mongo = new MongoClient(RestApi.getHost() , RestApi.getPort())) {

			MongoDatabase database = mongo.getDatabase(RestApi.getDatabase());

			MongoCollection<Document> collection = null;
			for(String name : database.listCollectionNames()) {
				if(collectionName.equalsIgnoreCase(name)) {
					collection = database.getCollection(collectionName);
				}
			}

			assertNotNull("No collections found with the name " + collectionName + "!", collection);

			Bson filter = regex(key, keyValue);

			Document document = collection.find(filter).first();

			assertNotNull("No document were found with the " + key + " value containing " + keyValue + "!", document);

			if (field.contains(".")) {
				List<String> fiedJson = Arrays.asList(field.replace(".", "#").split("#"));

				for (String jsonField : fiedJson.subList(0, fiedJson.size() - 1))
					document = (Document) document.get(jsonField);

				if (Objects.nonNull(document)) {
					RestApi.getUserParameters().put("${" + saveKey + "}", document.get(fiedJson.get(fiedJson.size() - 1)).toString());
					Hooks.scenario.write(RestApi.getUserParameters().get("${" + saveKey + "}"));
				}

			}else {

				assertNotNull("No values were found with the " + key + " value containing " + keyValue + "!", document.get(field));

				RestApi.getUserParameters().put("${" + saveKey + "}", document.get(field).toString());
				Hooks.scenario.write(RestApi.getUserParameters().get("${" + saveKey + "}"));
			}

		}
	}

	@Then("^I check if the field \"([^\"]*)\" has \"([^\"]*)\" on the collection \"([^\"]*)\" where the key \"([^\"]*)\" contains \"([^\"]*)\"$")
	public void checkIfValueExistsInCollectionWithContains(String field, String value, String collectionName, String key, String keyValue) throws Throwable {

		field = replaceVariablesValues(field);
		value = replaceVariablesValues(value);
		keyValue = replaceVariablesValues(keyValue);

		assertFalse("Field is required!", StringUtils.isEmpty(field));
		assertFalse("Value is required!", StringUtils.isEmpty(value));
		assertFalse("Collection name is required!", StringUtils.isEmpty(collectionName));
		assertFalse("Key is required!", StringUtils.isEmpty(key));
		assertFalse("Key Value is required!", StringUtils.isEmpty(keyValue));

		try(MongoClient mongo = new MongoClient(RestApi.getHost() , RestApi.getPort())) {

			MongoDatabase database = mongo.getDatabase(RestApi.getDatabase());

			MongoCollection<Document> collection = null;
			for(String name : database.listCollectionNames()) {
				if(collectionName.equalsIgnoreCase(name)) {
					collection = database.getCollection(collectionName);
				}
			}

			assertNotNull("No collections found with the name " + collectionName + "!", collection);

			Bson filter = regex(key, keyValue);

			FindIterable<Document> documents = collection.find(filter);
			Document document = null;
			for (Document d : documents) {
				document = d.get(field).equals(value) ? d : null;
			}

			assertNotNull("No document were found with the " + key + " value containing " + keyValue + "!", document);
			assertNotNull("No values were found with the " + key + " value containing " + keyValue + "!", document.get(field));
			assertEquals("The value found doesn't match the searched value " + value, value, document.get(field).toString());
			Hooks.scenario.write("_id = " + document.getObjectId("_id").toHexString());
			Hooks.scenario.write(field + " = " + value);
		}
	}

    @Then("^I save the Eloqua payload of the last entry of the collection \"([^\"]*)\"$")
    public void checkIfValueFromLastDocumentIsEquals( String collectionName) throws Throwable {


        assertFalse("Collection name is required!", StringUtils.isEmpty(collectionName));

        try(MongoClient mongo = new MongoClient(RestApi.getHost() , RestApi.getPort())) {

            MongoDatabase database = mongo.getDatabase(RestApi.getDatabase());

            MongoCollection<Document> collection = null;
            for(String name : database.listCollectionNames()) {
                if(collectionName.equalsIgnoreCase(name)) {
                    collection = database.getCollection(collectionName);
                }
            }

            assertNotNull("No collections found with the name " + collectionName + "!", collection);

            FindIterable<Document> documents = collection.find().sort(Sorts.descending("_id"));
            Document document = documents.first();

            assertNotNull(document.get("message",Document.class));

            RestApi.getUserParameters().put("${eloqua_event}", document.toJson());

            Hooks.scenario.write("_id = " + document.getObjectId("_id").toHexString());
        }
    }

    @Then("^I check if the field \"([^\"]*)\" of the Eloqua payload is \"([^\"]*)\"$")
    public void checkIfValueFromLastDocumentIsEquals(String key, String value) throws Throwable {

        key = replaceVariablesValues(key);
        value = replaceVariablesValues(value);

        assertFalse("Field is required!", StringUtils.isEmpty(key));
        assertFalse("Value is required!", StringUtils.isEmpty(value));

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(RestApi.getUserParameters().get("${eloqua_event}"));

        assertTrue(rootNode.at(key).asText().equals(value));

        Hooks.scenario.write("Event value = " + rootNode.at(key).asText());

        }
}