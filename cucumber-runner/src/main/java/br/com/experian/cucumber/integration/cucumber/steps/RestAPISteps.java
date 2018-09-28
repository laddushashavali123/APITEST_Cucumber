package br.com.experian.cucumber.integration.cucumber.steps;

import br.com.experian.cucumber.integration.cucumber.common.utils.*;
import br.com.experian.cucumber.integration.cucumber.common.utils.httpclient.HttpClient;
import br.com.experian.cucumber.integration.cucumber.definitions.BusinessAccountJson;
import br.com.experian.cucumber.integration.cucumber.definitions.Client;
import br.com.experian.cucumber.integration.cucumber.definitions.JWTJson;
import br.com.experian.cucumber.integration.cucumber.mongo.MongoRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.core.util.JsonUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static br.com.experian.cucumber.integration.cucumber.common.utils.PropertiesUtil.getProperty;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Assertions.not;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.*;

public class RestAPISteps extends MainSteps {

	@Then("I clean JWT Token")
	public void cleanJsonToken() throws Throwable {
		RestApi.setAccessToken(null);
		RestApi.setHeaders(new ArrayList<>());
		Hooks.scenario.write("Bearer " + RestApi.getAccessToken());
	}

	@Then("^I remove the business \"([^\"]*)\" from user \"([^\"]*)\"$")
	public void iRemoveTheBusinessFromUser(String businessId, String userId) throws Throwable {
		businessId = RestApi.replaceVariablesValues(businessId);
		userId = RestApi.replaceVariablesValues(userId);
		new MongoRepository().removeUserCompany(userId, businessId);
	}

	@When("^I save the random \"([^\"]*)\" as \"([^\"]*)\"$")
	public void saveRandomValue(String type, String userKey) throws Throwable {
		String value;
		if (type.toUpperCase().equals("NUMBER")) {
			value = generateRandomNumber();
		} else {
			value = generateRandomString();
		}
//        RestApi.getUserParameters().put("${" + userKey + "}", value);
		RestApi.saveVariable(userKey, value);
		Hooks.scenario.write(RestApi.getUserParameters().get("${" + userKey + "}"));
	}

	@Then("the JWT must have \"([^\"]*)\" as \"([^\"]*)\"$")
	public void valdiateStringJwt(String key, String value) throws Throwable {

		DecodedJWT jwt = JWT.decode(RestApi.getAccessToken().replaceAll("Bearer ", ""));
		String jwtValue = "";
		List<String> jwtListValue = new ArrayList<String>();
		switch (key) {
			case "exp":
			case "expires_in":
			case "iat":
				jwtValue = getLongClaim(jwt, key);
				break;
			case "scope":
			case "authorities":
				jwtListValue = getListClaim(jwt, key);
				break;
			default:
				jwtValue = getStringClaim(jwt, key);
		}

		if (value.contains("[")) {
			String[] listValues = value.replaceAll("\\[", "").replaceAll("\\]", "").split(",");
			for (String listValue : listValues) {
				assertEquals("Expected value \'" + listValue.trim() + "\' not found on " + jwtListValue.toString(), jwtListValue.contains(listValue.trim()), true);
			}
		} else {
			assertEquals(jwtValue, value);
		}
	}

	@Then("the JWT must NOT have \"([^\"]*)\" as \"([^\"]*)\"$")
	public void valdiateStringNotPresentJwt(String key, String value) throws Throwable {

		DecodedJWT jwt = JWT.decode(RestApi.getAccessToken().replaceAll("Bearer ", ""));
		String jwtValue = "";
		List<String> jwtListValue = new ArrayList<String>();
		switch (key) {
			case "exp":
			case "expires_in":
			case "iat":
				jwtValue = getLongClaim(jwt, key);
				break;
			case "scope":
			case "authorities":
				jwtListValue = getListClaim(jwt, key);
				break;
			default:
				jwtValue = getStringClaim(jwt, key);
		}

		if (value.contains("[")) {
			String[] listValues = value.replaceAll("\\[", "").replaceAll("\\]", "").split(",");
			for (String listValue : listValues) {
				assertNotEquals("Expected value \'" + listValue.trim() + "\' found on " + jwtListValue.toString(), jwtListValue.contains(listValue.trim()), true);
			}
		} else {
			assertNotEquals(jwtValue, value);
		}
	}

	@Then("the response JSON node \"([^\"]*)\" must \"([^\"]*)\"$")
	public void validateJsonNode(String key, String value) throws Throwable {
		if (!"".equals(RestApi.getResponseJson())) {
			JsonNode rootNode = new ObjectMapper().readTree(new StringReader(RestApi.getResponseJson()));
			if (value.equals("exist")) {
				Hooks.scenario.write("Value found: " + rootNode.at(key).asText());
				assertNotEquals("", rootNode.at(key).asText());
			} else {
				assertEquals("", rootNode.at(key).asText());
			}
		}
	}

	@Then("the response Header \"([^\"]*)\" must have \"([^\"]*)\"$")
	public void validateResponseHeader(String key, String value) throws Throwable {
		String[] listValues = value.split(",");
		String responseHeader = RestApi.getResponse().getHeaders(key)[0].getValue();
		for (String listValue : listValues) {
			assertTrue("Expected value \'" + listValue.trim() + "\' not found on " + responseHeader, responseHeader.contains(listValue.trim()));
		}
	}

	@Then("the response Header \"([^\"]*)\" must do not have \"([^\"]*)\"$")
	public void validateResponseHeaderDoNot(String key, String value) throws Throwable {
		String[] listValues = value.split(",");
		String responseHeader = RestApi.getResponse().getHeaders(key)[0].getValue();
		for (String listValue : listValues) {
			assertFalse("Expected value \'" + listValue.trim() + "\' not found on " + responseHeader, responseHeader.contains(listValue.trim()));
		}
	}

	@Given("^I set the Basic Authorization header with clientId \"([^\"]*)\" and clientSecret \"([^\"]*)\"$")
	public void setBasicAuthClientHeader(String user, String pwd) throws Throwable {
		setBasicAuthHeader(user, pwd);
	}

	@Given("^I clear all the headers$")
	public void clearAllHeaders() throws Throwable {
		RestApi.setHeaders(new ArrayList<>());
	}

	@Then("^I put this id \"([^\"]*)\" on group \"([^\"]*)\"$")
	public void putIdOnGroup(String id, String groupName) throws Throwable {
		new MongoRepository().addToGroup(groupName, id);
	}

	@Then("^I remove this id \"([^\"]*)\" from group \"([^\"]*)\"$")
	public void iRemoveThisIdFromGroup(String id, String groupName) throws Throwable {
		new MongoRepository().removeToGroup(groupName, id);
	}

	@Then("^I put this id \"([^\"]*)\" on business group \"([^\"]*)\" from this business id \"([^\"]*)\"$")
	public void iPutThisIdOnBusinessGroupFromThisBusinessId(String userId, String groupName, String businessId) throws Throwable {
		new MongoRepository().addBusinessGroup(businessId, groupName, userId);
	}

	@Given("^I use the id \"([^\"]*)\"$")
	public void setId(String id) throws Throwable{
		RestApi.setId(RestApi.replaceVariablesValues(id));
		Hooks.scenario.write(RestApi.getId());
	}

	@When("^I find the validation token by id$")
	public void findValidationToken() throws Throwable{
		MongoRepository mongoRepository = new MongoRepository();
		RestApi.setToken(mongoRepository.findTokenByUserAccountId(RestApi.getId()));
		Hooks.scenario.write(RestApi.getToken());
	}

	@When("^I find the validation token by userId$")
	public void findValidationTokenByUserId() throws Throwable{
		MongoRepository mongoRepository = new MongoRepository();
		RestApi.setToken(mongoRepository.findTokenByUserId(RestApi.getId()));
		Hooks.scenario.write(RestApi.getToken());
	}

	@Then("^I print the token$")
	public void printToken() throws Throwable{
		Hooks.scenario.write(RestApi.getToken());
	}

	@When("^I save the token value as \"([^\"]*)\"$")
	public void saveTokenValue(String userKey) throws Throwable {
		if (System.getProperty("environment.name") != null && (System.getProperty("environment.name").equals("LO")
				|| System.getProperty("environment.name").equals("ST")
				|| System.getProperty("environment.name").equals("QA"))) {
			RestApi.getUserParameters().put("${" + userKey + "}", "123456");
		} else {
			RestApi.getUserParameters().put("${" + userKey + "}", RestApi.getToken());
		}
		Hooks.scenario.write(RestApi.getUserParameters().get("${" + userKey + "}"));
	}

	@Given("I run the test case \"([^\"]*)\"$")
	public void setTestCase(String testCase) throws Throwable {
	}

	@Given("^I use the route \"([^\"]*)\"$")
	public void setRoute(String route) throws Throwable {
		RestApi.setPath(RestApi.replaceVariablesValues(route));
		pathToReport();
	}

	@Given("^I set queryparameter \"([^\"]*)\" as \"([^\"]*)\"$")
	public void setQueryParameter(String key, String value) throws Throwable {
		RestApi.getParameters().add(new BasicNameValuePair(key, value));
	}

	@Given("^I set pathparameter \"([^\"]*)\" as \"([^\"]*)\"$")
	public void setPathParameter(String key, String value) throws Throwable {
		RestApi.setPath(RestApi.getPath().replaceAll(key, value));
	}

	@Given("^I set header \"([^\"]*)\" as \"([^\"]*)\"$")
	public void setHeader(String key, String value) throws Throwable {
		for (Header header : RestApi.getHeaders()) {
			if (header.getName().equals(key)) {
				RestApi.getHeaders().remove(header);
				break;
			}
		}
		RestApi.getHeaders().add(new BasicHeader(key, value));
	}

	@Given("^I use the json body$")
	public void setJsonBody(String jsonBody) throws Throwable {

		// replace variable values
		RestApi.setJsonBodyString(RestApi.replaceVariablesValues(jsonBody));
		try {
			ObjectMapper mapper = new ObjectMapper();
			Object json = mapper.readValue(RestApi.getJsonBodyString(), Object.class);
			String prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
			Hooks.scenario.write(prettyJson);
		} catch (Exception e) {
			Hooks.scenario.write(RestApi.getJsonBodyString());
		}
	}

	@Given("^I use the collection \"([^\"]*)\"$")
	public void setCollection(String collection) throws Throwable {
		RestApi.setCollection(collection);
	}

	@Given("^I use the json document$")
	public void setJsonDocument(String jsonBody) throws Throwable {
		RestApi.setJsonDocument(RestApi.replaceVariablesValues(jsonBody));
		Hooks.scenario.write(RestApi.getJsonDocument());
	}

	@Given("^I use the column document \"([^\"]*)\"$")
	public void setColumn(String column) throws Throwable {
		RestApi.setColumn(RestApi.replaceVariablesValues(column));
		Hooks.scenario.write(RestApi.getColumn());
	}

	@Given("^I use the value document \"([^\"]*)\"$")
	public void setValueDocument(String value) throws Throwable {
		RestApi.setValue(RestApi.replaceVariablesValues(value));
		Hooks.scenario.write(RestApi.getValue());
	}

	@When("^I send the POST request$")
	public void postRequest() throws Throwable {
		HttpClient http = new HttpClient(RestApi.getDomain(), RestApi.getApiVersion());
		RestApi.setEntity(new StringEntity(RestApi.getJsonBodyString(), ContentType.APPLICATION_JSON));
		RestApi.setResponse(http.sendPost(RestApi.getPath(), RestApi.getParameters(), RestApi.getHeaders(), RestApi.getEntity()));
		extractJsonResponse();
		extractResponseTokens();
		responseToReport();
	}

	@When("^I send the GET request$")
	public void getRequest() throws Throwable {
		HttpClient http = new HttpClient(RestApi.getDomain(), RestApi.getApiVersion());
		RestApi.setResponse(http.sendGet(RestApi.getPath(), RestApi.getParameters(), RestApi.getHeaders()));
		extractJsonResponse();
		extractResponseTokens();
		responseToReport();
	}

	@When("^I send the PUT request$")
	public void putRequest() throws Throwable {
		HttpClient http = new HttpClient(RestApi.getDomain(), RestApi.getApiVersion());
		RestApi.setEntity(new StringEntity(RestApi.getJsonBodyString(), ContentType.APPLICATION_JSON));
		RestApi.setResponse(http.sendPut(RestApi.getPath(), RestApi.getParameters(), RestApi.getHeaders(), RestApi.getEntity()));
		extractJsonResponse();
		extractResponseTokens();
		responseToReport();
	}

	@When("^I send the DELETE request$")
	public void deleteRequest() throws Throwable {
		HttpClient http = new HttpClient(RestApi.getDomain(), RestApi.getApiVersion());
		RestApi.setResponse(http.sendDelete(RestApi.getPath(), RestApi.getParameters(), RestApi.getHeaders()));
		extractJsonResponse();
		extractResponseTokens();
		responseToReport();
	}

	@When("^I send the DELETE request with body$")
	public void deleteRequestWithBody() throws Throwable {
		HttpClient http = new HttpClient(RestApi.getDomain(), RestApi.getApiVersion());
		RestApi.setEntity(new StringEntity(RestApi.getJsonBodyString(), ContentType.APPLICATION_JSON));
		RestApi.setResponse(http.sendDeleteWithBody(RestApi.getPath(), RestApi.getParameters(), RestApi.getHeaders(), RestApi.getEntity()));
		extractJsonResponse();
		extractResponseTokens();
		responseToReport();
	}

	@When("^I send the PATCH request$")
	public void patchRequest() throws Throwable {
		HttpClient http = new HttpClient(RestApi.getDomain(), RestApi.getApiVersion());
		RestApi.setEntity(new StringEntity(RestApi.getJsonBodyString(), ContentType.APPLICATION_JSON));
		RestApi.setResponse(http.sendPatch(RestApi.getPath(), RestApi.getParameters(), RestApi.getHeaders(), RestApi.getEntity()));
		extractJsonResponse();
		extractResponseTokens();
		responseToReport();
	}

	@When("^I send the OPTIONS request$")
	public void optionsRequest() throws Throwable {
		HttpClient http = new HttpClient(RestApi.getDomain(), RestApi.getApiVersion());
		RestApi.setResponse(http.sendOptions(RestApi.getPath(), RestApi.getParameters(), RestApi.getHeaders()));
		extractJsonResponse();
		extractResponseTokens();
		responseToReport();
	}

	@When("^I save the document$")
	public void saveDocument() throws Throwable {
		MongoRepository mongoRepository = new MongoRepository();
		mongoRepository.save(RestApi.getCollection(), RestApi.getJsonDocument());
	}

	@Then("the response JSON must have \"([^\"]*)\" as the String \"([^\"]*)\"$")
	public void valdiateStringJsonBody(String key, String value) throws Throwable {
		value = RestApi.replaceVariablesValues(value);
		if (!"".equals(RestApi.getResponseJson())) {
			JsonNode rootNode = new ObjectMapper().readTree(new StringReader(RestApi.getResponseJson()));

			if (!key.equals("size")) {
				Hooks.scenario.write("Value found: " + rootNode.at(key).asText());
				assertEquals(value.trim(), rootNode.at(key).asText().trim());
			} else {
				Hooks.scenario.write("Value found: " + value);

				if (rootNode == null) {
					assertEquals(Integer.parseInt(value), 0);
				} else {
					assertEquals(Integer.parseInt(value), rootNode.size());
				}
			}
		}
	}

	@Then("the response JSON Array \"([^\"]*)\" must contain \"([^\"]*)\" as \"([^\"]*)\" and the key \"([^\"]*)\" as the String \"([^\"]*)\"$")
	public void validateStringArrayJsonBody(String arrayKey, String containedKey, String containedValue, String key,
											String value) throws Throwable {

		if (!"".equals(RestApi.getResponseJson())) {

			JsonNode rootNode = new ObjectMapper().readTree(new StringReader(RestApi.getResponseJson()));

			JsonNode arrayNode = rootNode.at(arrayKey);
			if (arrayNode.isArray()) {

				for (JsonNode node : arrayNode) {

					String valueToCompare = node.path(containedKey).asText();

					if ("".equals(containedValue)) {
						fail("The value from " + containedKey + " cannot be empty!");
					}

					if ("".equals(valueToCompare)) {
						fail("The key " + containedKey + " doen't exists in the JSON Array Object!");
					}

					if (containedValue.contains(valueToCompare)) {

						String returnedValue = node.path(key).asText();

						Hooks.scenario
								.write("The value from key \"" + containedKey + "\" is \"" + containedValue + "\"");
						Hooks.scenario.write("The value from key \"" + key + "\" is \"" + returnedValue + "\"");

						assertEquals(value, returnedValue);
					}
				}
			} else {
				fail("The param " + arrayKey + " is not an Array!");
			}
		}
	}

	@When("^I save the response value \"([^\"]*)\" as \"([^\"]*)\"$")
	public void saveJsonValue(String jsonKey, String userKey) throws Throwable {
		JsonNode rootNode = new ObjectMapper().readTree(new StringReader(RestApi.getResponseJson()));
		String jsonValue = "";

		if (rootNode.at(jsonKey) != null){
			jsonValue = rootNode.at(jsonKey).asText();
		}

		RestApi.saveVariable(userKey, jsonValue);

		Hooks.scenario.write(RestApi.getUserParameters().get("${" + userKey + "}"));
	}

	@When("^I save userId as variable with aleatory value$")
	public void saveVariableValue() throws Throwable {
		Random rand = new Random();
		int value = rand.nextInt(999999999);
		String stValue = String.format("%09d", value);;
		RestApi.getUserParameters().put("${user_id}", stValue);
		Hooks.scenario.write(RestApi.getUserParameters().get("${user_id}"));
	}

	@When ("^I set the JSON list \"([^\"]*)\" with keys and values as$")
	public void registerUser(String userKey, DataTable table) throws Throwable {
		JSONArray jsonArray = new JSONArray();
		List<String> headers = table.raw().get(0);

		for (Map<String, String> item : table.asMaps(String.class, String.class)) {
			JSONObject line = new JSONObject();
			for (String header : headers) {
				if (header.endsWith(".toNumber")){
					line.put(header.replaceAll(".toNumber", ""), Long.parseLong(item.get(header)));
				}else {
					line.put(header, item.get(header));
				}
			}
			jsonArray.put(line);
		}

		RestApi.getUserParameters().put("${" + userKey + "}", jsonArray.toString());

		Hooks.scenario.write(RestApi.getUserParameters().get("${" + userKey + "}"));
	}

	@When("^I delete the document$")
	public void deleteDocument() throws Throwable{
		MongoRepository mongoRepository = new MongoRepository();

		if(RestApi.getColumn().equalsIgnoreCase("_id")){
			mongoRepository.deleteOne(RestApi.getCollection(), new Document(RestApi.getColumn(), new ObjectId(RestApi.getValue())));
		}else{
			mongoRepository.deleteMany(RestApi.getCollection(), new Document(RestApi.getColumn(), RestApi.getValue()));
		}
	}

	@And("^is an illegal GET request$")
	public void isIllegalRequest() throws Throwable {
		try {
			HttpClient http = new HttpClient(RestApi.getDomain(), RestApi.getApiVersion());
			RestApi.setResponse(http.sendGet(RestApi.getPath(), RestApi.getParameters(), RestApi.getHeaders()));
			extractJsonResponse();
		} catch (Exception e) {
			Hooks.add(e);
		}
	}

	@And("^is an illegal POST request$")
	public void isIllegalPostRequest() throws Throwable {
		try {
			HttpClient http = new HttpClient(RestApi.getDomain(), RestApi.getApiVersion());
			RestApi.setEntity(new StringEntity(RestApi.getJsonBodyString(), ContentType.APPLICATION_JSON));
			RestApi.setResponse(http.sendPost(RestApi.getPath(), RestApi.getParameters(), RestApi.getHeaders(), RestApi.getEntity()));
			extractJsonResponse();
		}
		catch(Exception e) {
			Hooks.add(e);
		}
	}

	@Then("the response JSON must have \"([^\"]*)\" as the Integer (\\d+)$")
	public void valdiateIntegerJsonBody(String key, Integer value) throws Throwable {
		JsonNode rootNode = new ObjectMapper().readTree(new StringReader(RestApi.getResponseJson()));

		Hooks.scenario.write("Value found: " + rootNode.at(key).asInt());

		assertEquals(value.intValue(), rootNode.at(key).asInt());
	}

	@Then("^I print the path$")
	public void pathToReport() throws Throwable {
		Hooks.scenario.write(this.getCompletePath());
		Hooks.scenario.write(RestApi.getHeaders().toString());
		Hooks.scenario.write(RestApi.getParameters().toString());
	}

	@Then("^I print the response$")
	public void responseToReport() throws Throwable {

		Hooks.scenario.write(RestApi.getResponse().getStatusLine().toString());
		String headers = "[";
		for (Header header : RestApi.getResponse().getAllHeaders()) {
			headers += header.toString() + ", ";
		}
		Hooks.scenario.write(headers + "]");

		try {
			ObjectMapper mapper = new ObjectMapper();
			Object json = mapper.readValue(RestApi.getResponseJson(), Object.class);
			String prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
			Hooks.scenario.write(prettyJson);
		} catch (Exception e) {
			if ((RestApi.getResponseJson() != null) && (!"".equals(RestApi.getResponseJson()))) {
				Hooks.scenario.write(RestApi.getResponseJson());
			}
		}
	}

	@Then("^I print the error$")
	public void responseToError() throws Throwable {

		Hooks.scenario.write(Hooks.getExceptions().get(0).getMessage());

		try {
			ObjectMapper mapper = new ObjectMapper();
			Object json = mapper.readValue(RestApi.getResponseJson(), Object.class);
			String prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
			Hooks.scenario.write(prettyJson);
		} catch (Exception e) {

		}
	}

	@Then("^Http response should be (\\d+)$")
	public void validateResponse(Integer responseCode) throws Throwable {
		Integer statusCode = RestApi.getResponse().getStatusLine().getStatusCode();
		assertEquals(responseCode.toString(), statusCode.toString());
	}

	@Then("^user should see \"(.*)\" message$")
	public void userShouldSeeMessage(String message) throws Throwable {
		HashMap<String,Integer> stCode = new HashMap<>();
		stCode.put("success", 200);
		stCode.put("save with success", 201);
		stCode.put("no content", 204);
		stCode.put("bad request", 400);
		stCode.put("unauthorized", 401);
		stCode.put("forbidden", 403);
		stCode.put("precondition failed", 412);
		stCode.put("not found", 404);
		validateResponse(stCode.get(message));
	}



	@Then("^Http response should be \"([^\"]*)\"$")
	public void validateResponse(String responseCode) throws Throwable {
		validateResponse(Integer.parseInt(responseCode));
	}

	@When("A failure is expected")
	public void expectException() {
		Hooks.expectException();
	}

	@And("it fails")
	public void it_fails() {
		assertThat(Hooks.getExceptions(), is(not(empty())));
	}

	@Given("^I generate a milliseconds date \"([^\"]*)\" with \"([^\"]*)\" minutes$")
	public void generateMillisecondsWithMinutes(String userKey, String minutes) throws Throwable {
		Long dateInMilis = (new Date().getTime() / 1000) + (Integer.parseInt(minutes) * 60);

		RestApi.getUserParameters().put("${" + userKey + "}", dateInMilis.toString());
	}

	@Given("^I set the Bearer Authorization header$")
	public void setBearerAuthHeader() throws Throwable {
		for (Header header : RestApi.getHeaders()) {
			if (header.getName().equals("Authorization")) {
				RestApi.getHeaders().remove(header);
				break;
			}
		}
		RestApi.getHeaders().add(new BasicHeader("Authorization", "Bearer " + RestApi.getAccessToken()));
	}

	@When("^I print the JWT access token$")
	public void decodeJWT() throws Throwable {
		Hooks.scenario.write("token: " + RestApi.getAccessToken().replaceAll("Bearer ", ""));

		DecodedJWT jwt = JWT.decode(RestApi.getAccessToken().replaceAll("Bearer ", ""));

		Hooks.scenario.write("exp: " + getLongClaim(jwt, "exp"));
//    	Hooks.scenario.write("expires_in: " + getLongClaim(jwt, "expires_in"));
		Hooks.scenario.write("jti: " + getStringClaim(jwt, "jti"));

		Hooks.scenario.write("scope: " + getListClaim(jwt, "scope").toString());
		Hooks.scenario.write("authorities: " + getListClaim(jwt, "authorities").toString());
		Hooks.scenario.write("iat: " + getLongClaim(jwt, "iat"));
		//Hooks.scenario.write(Integer.toString(getIntClaim(jwt, "exp") - getIntClaim(jwt, "iat")) );
		Hooks.scenario.write("user_id: " + getStringClaim(jwt, "user_id"));
		Hooks.scenario.write("client_id: " + getStringClaim(jwt, "client_id"));
	}

	@Given("^I generate a miliseconds date \"([^\"]*)\" with \"([^\"]*)\" minutes$")
	public void generateMilisecondsWithMinutes(String userKey, String minutes) throws Throwable {
		Long dateInMilis = (new Date().getTime() / 1000) + (Integer.parseInt(minutes) * 60);

		RestApi.getUserParameters().put("${" + userKey + "}", dateInMilis.toString());
	}

	@Given("^I use the json and generate a JWT Token")
	public void setJsonToken(String jsonToken) throws Throwable {
		jsonToken = RestApi.replaceVariablesValues(jsonToken);
		Hooks.scenario.write(jsonToken);
		jsonToken = jsonToken.replace("\n", "").replace("\r", "").replaceAll(" ", "");
		RestApi.setAccessToken(new JwtUtil().createJwt(jsonToken));
	}

	@Given("^I generate a JWT Token$")
	public void generateJwtToken() throws Throwable {
		String jsonToken = "{\n" +
				"   \"jti\": \"9e74d1f6-7095-4718-8f11-47dc9ec99c63\",\n" +
				"   \"iat\": ${iat},\n" +
				"   \"scope\": [ ${scope} ],\n" +
				"   \"user_id\": \"${user_id}\",\n" +
				"   \"business_id\": \"${business_id}\",\n" +
				"   \"client_id\": \"${client_id}\",\n" +
				"   \"authorities\": [ ${authorities} ],\n" +
				"   \"exp\": ${exp}\n" +
				"}";
		//default values
		//iat
		generateMilisecondsWithMinutes("iat", "0");
		//scope
		RestApi.getUserParameters().put("${scope}", "\"read\",\"write\"");
		//user_id
		RestApi.getUserParameters().put("${user_id}", "0000001");
		//business_id
		RestApi.getUserParameters().put("${business_id}", "0000001");
		//client_id
		RestApi.getUserParameters().put("${client_id}", "0000001");
		//authorities
		RestApi.getUserParameters().put("${authorities}", "\"ROLE_CLI-AUTH-IDENTIFIED\", \"ROLE_CLI-AUTH-BASIC\", \"ROLE_CLI-1STPARTY\", \"ROLE_CLI-3RDPARTY\", \"ROLE_AUTH-IDENTIFIED\", \"ROLE_AUTH-BASIC\", \"ROLE_AUTH-MFA\", \"ROLE_SECADMIN\", \"ROLE_SYSADMIN\", \"ROLE_ADMIN\", \"ROLE_USER\", \"ROLE_BUSINESSOWNER\", \"ROLE_BUSINESSADMIN\", \"ROLE_BUSINESSUSER\"");
		//exp
		generateMilisecondsWithMinutes("exp", "15");

		setJsonToken(jsonToken);
	}

	@Given("^I generate a JWT Token with$")
	public void generateJwtToken(DataTable table) throws Throwable {

		String jsonToken = "{\n" +
				"   \"jti\": \"9e74d1f6-7095-4718-8f11-47dc9ec99c63\",\n" +
				"   \"iat\": ${iat},\n" +
				"   \"scope\": [ ${scope} ],\n" +
				"   \"user_id\": \"${user_id}\",\n" +
				"   \"business_id\": \"${business_id}\",\n" +
				"   \"client_id\": \"${client_id}\",\n" +
				"   \"authorities\": [ ${authorities} ],\n" +
				"   \"exp\": ${exp}\n" +
				"}";
		//default values
		//iat
		if (RestApi.getUserParameters().get("${iat}") == null || RestApi.getUserParameters().get("${iat}").isEmpty()) {
			generateMilisecondsWithMinutes("iat", "0");
		}
		//scope
		if (RestApi.getUserParameters().get("${scope}") == null || RestApi.getUserParameters().get("${scope}").isEmpty()) {
			RestApi.getUserParameters().put("${scope}", "\"read\",\"write\"");
		}
		//user_id
		if (RestApi.getUserParameters().get("${user_id}") == null || RestApi.getUserParameters().get("${user_id}").isEmpty()){
			RestApi.getUserParameters().put("${user_id}", "0000001");
		}
		//business_id
		if (RestApi.getUserParameters().get("${business_id}") == null || RestApi.getUserParameters().get("${business_id}").isEmpty()) {
			RestApi.getUserParameters().put("${business_id}", "0000001");
		}
		//client_id
		if (RestApi.getUserParameters().get("${client_id}") == null || RestApi.getUserParameters().get("${client_id}").isEmpty()) {
			RestApi.getUserParameters().put("${client_id}", "0000001");
		}
		//authorities
		if (RestApi.getUserParameters().get("${authorities}") == null || RestApi.getUserParameters().get("${authorities}").isEmpty()) {
			RestApi.getUserParameters().put("${authorities}", "\"ROLE_CLI-AUTH-IDENTIFIED\", \"ROLE_CLI-AUTH-BASIC\", \"ROLE_CLI-1STPARTY\", \"ROLE_CLI-3RDPARTY\", \"ROLE_AUTH-IDENTIFIED\", \"ROLE_AUTH-BASIC\", \"ROLE_AUTH-MFA\", \"ROLE_SECADMIN\", \"ROLE_SYSADMIN\", \"ROLE_ADMIN\", \"ROLE_USER\", \"ROLE_BUSINESSOWNER\", \"ROLE_BUSINESSADMIN\", \"ROLE_BUSINESSUSER\"");
		}
		//exp
		if (RestApi.getUserParameters().get("${exp}") == null || RestApi.getUserParameters().get("${exp}").isEmpty()) {
			generateMilisecondsWithMinutes("exp", "15");
		}

		List<String> headers = table.raw().get(0);
		for (Map<String, String> item : table.asMaps(String.class, String.class)) {
			for (String header : headers) {
				switch (header) {
					case "iat":
					case "exp":
						generateMilisecondsWithMinutes(header, item.get(header));
						break;
					default:
						RestApi.getUserParameters().put("${" + header + "}", item.get(header));
				}

			}
		}
		setJsonToken(jsonToken);

	}

	@Given("^I clear the Authorization header$")
	public void clearAuthHeader() throws Throwable {
		removeHeader("Authorization");
	}

	@Then("Stop \"([^\"]*)\" seconds$")
	public void stopSeconds(String second) throws Throwable {
		TimeUnit.SECONDS.sleep(Long.valueOf(second));
	}

	@Then("^The variable \"([^\"]*)\" equals \"?([^\"]*)\"?$")
	public void a(String variable, String value) throws Throwable{
		variable = RestApi.replaceVariablesValues(variable);

		assertEquals(variable, value);
	}

	@Given("^I use the environment \"([^\"]*)\"$")
	public void setEnvironment(String environment) throws Throwable {
		RestApi.setDomain(RestApi.replaceVariablesValues(environment));
	}

	@Then("^I wait untill the GET response JSON field \"([^\"]*)\" is not an empty String$")
	public void waitGetRequestResponse(String key) throws Throwable {
		int i = 0;
		int nRetries = 30;

		while (i < nRetries){
			getRequest();
			i++;
			if (!"".equals(RestApi.getResponseJson())) {
				JsonNode rootNode = new ObjectMapper().readTree(new StringReader(RestApi.getResponseJson()));
				String valueReturned = rootNode.at(key).asText();

				if(StringUtils.isEmpty(valueReturned)) {
					stopSeconds("1");
				}else{
					Hooks.scenario.write("Value found: " + valueReturned);
					break;
				}
			}else{
				stopSeconds("1");
			}
		}
	}

	@Then("the response JSON must have \"([^\"]*)\" as a not empty String$")
	public void validateNotEmptyStringJsonBody(String key) throws Throwable {

		if (!"".equals(RestApi.getResponseJson())) {
			JsonNode rootNode = new ObjectMapper().readTree(new StringReader(RestApi.getResponseJson()));

			String valueReturned = rootNode.at(key).asText();
			Hooks.scenario.write("Value found: " + valueReturned);

			if(StringUtils.isEmpty(valueReturned)) {
				fail("The value from " + key + " must not be empty!");
			}
		}
	}

	@Then("the response JSON must have \"([^\"]*)\" as a not null Object$")
	public void validateNotNullObjectJsonBody(String key) throws Throwable {

		if (!"".equals(RestApi.getResponseJson())) {
			JsonNode rootNode = new ObjectMapper().readTree(new StringReader(RestApi.getResponseJson()));

			boolean valueReturned = rootNode.at(key).isNull();
			Hooks.scenario.write("Value found: " + valueReturned);

			if(valueReturned) {
				fail("The value from " + key + " must not be empty!");
			}
		}
	}

	@Given("^I set the Bearer Authorization header as \"([^\"]*)\"$")
	public void setBearerAuthHeaderAs(String accessToken) throws Throwable {

		for (Header header: RestApi.getHeaders()) {
			if (header.getName().equals("Authorization")) {
				RestApi.getHeaders().remove(header);
				break;
			}
		}

		RestApi.getHeaders().add(new BasicHeader("Authorization", "Bearer " + accessToken));
	}

	@Given("^I set the Basic Authorization header with user \"([^\"]*)\" and password \"([^\"]*)\"$")
	public void setBasicAuthHeader(String user, String pwd) throws Throwable {
		user = RestApi.replaceVariablesValues(user);
		pwd = RestApi.replaceVariablesValues(pwd);
		String tokenValue = "Basic " + Base64.getEncoder().encodeToString((user + ":" + pwd).getBytes());
		removeHeader("Authorization");
		RestApi.getHeaders().add(new BasicHeader("Authorization", tokenValue));
	}

	@Given("^I use new url request \"([^\"]*)\"$")
	public void newUrl(String apiVersion) throws Throwable {
		RestApi.setApiVersion(apiVersion);
	}

	@Given("^I use the domain \"([^\"]*)\"$")
	public void setDomain(String domain) throws Throwable {
		RestApi.setDomain(domain);
	}

	@When("^I save the variable \"([^\"]*)\" with value \"([^\"]*)\"$")
	public void saveValue(String userKey, String value) throws Throwable {
		RestApi.saveVariable(userKey, value);
		Hooks.scenario.write(RestApi.getUserParameters().get("${" + userKey + "}"));
	}

	@When("^I save the variable \"([^\"]*)\" with value \"([^\"]*)\" as Base64$")
	public void saveBase64Value(String userKey, String value) throws Throwable {
//        RestApi.getUserParameters().put("${" + userKey + "}", Base64.getEncoder().encodeToString((value).getBytes()));
		RestApi.saveVariable(userKey, Base64.getEncoder().encodeToString((value).getBytes()));
		Hooks.scenario.write(RestApi.getUserParameters().get("${" + userKey + "}"));
	}

	@Then("^The response JSON can not contain the value \"([^\"]*)\"$")
	public void theResponseJSONCanNotContainTheValue(String value) throws Throwable {
		if (!"".equals(RestApi.getResponseJson())) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(new StringReader(RestApi.getResponseJson()));
			if(rootNode.isArray()){
				Iterator<JsonNode> elementsIterator = ((ArrayNode) rootNode).elements();
				while (elementsIterator.hasNext()) {
					JsonNode node = elementsIterator.next();
					boolean hasService = false;
					Iterator<String> elementsName = node.fieldNames();

					while(elementsName.hasNext()){
						if(node.get(elementsName.next()).equals(value)){
							hasService = true;
						}
					}
					assertFalse(hasService);
				}
			}else{
				assertFalse(rootNode.toString().contains(value));
			}
		}
	}

	@Then("^The response JSON must contain the value \"([^\"]*)\" on field \"([^\"]*)\"$")
	public void theResponseJSONMustContainTheValue(String value, String field) throws Throwable {
		if (!"".equals(RestApi.getResponseJson())) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(new StringReader(RestApi.getResponseJson()));
			if(rootNode.isArray()){
				boolean hasfound = false;
				for (JsonNode jsonNode : rootNode) {
					if(jsonNode.get(field).asText().equals(value)){
						hasfound = true;
						break;
					}
				}
				assertTrue(hasfound);
			}else{
				assertTrue(rootNode.toString().contains(value));
			}
		}
	}

	@Then("^The response JSON must contain the key \"([^\"]*)\" and value \"([^\"]*)\" in the list node \"([^\"]*)\"$")
	public void theResponseJSONMustContainKeyValue(String key, String value, String listNodeName) throws Throwable {
		key = RestApi.replaceVariablesValues(key);
		value = RestApi.replaceVariablesValues(value);
		if (!"".equals(RestApi.getResponseJson())) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(new StringReader(RestApi.getResponseJson()));
			JsonNode list = rootNode.at(listNodeName);

			if(list.isArray()){
				Iterator<JsonNode> elementsIterator = ((ArrayNode) list).elements();
				while (elementsIterator.hasNext()) {
					JsonNode node = elementsIterator.next();
					//"id": "100226",
					//"value": "1519349306"
					String idReturned = node.at("/id").asText();
					if (key.equals(idReturned)) {
						String valueReturned = node.at("/value").asText();
						assertEquals(value, valueReturned);
					}
				}
			}else{
				fail("The value from " + key + " must not be empty!");
			}
		}
	}

	@Then("^The response JSON must contain the key \"([^\"]*)\" with value not null in the list node \"([^\"]*)\"$")
	public void theResponseJSONMustContainKeyValue(String key, String listNodeName) throws Throwable {
		key = RestApi.replaceVariablesValues(key);
		RestApi.setValue(RestApi.replaceVariablesValues(RestApi.getValue()));
		if (!"".equals(RestApi.getResponseJson())) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(new StringReader(RestApi.getResponseJson()));
			JsonNode list = rootNode.at(listNodeName);
			Boolean found = false;

			if(list.isArray()){
				Iterator<JsonNode> elementsIterator = ((ArrayNode) list).elements();
				while (elementsIterator.hasNext()) {
					JsonNode node = elementsIterator.next();
					//"id": "100226",
					//"value": "1519349306"

					String idReturned = node.at("/id").asText();
					if (key.equals(idReturned)) {
						String valueReturned = node.at("/value").asText();
						Hooks.scenario.write("Value found: " + valueReturned);
						found = true;
						if (StringUtils.isEmpty(valueReturned)) {
							fail("The value from " + key + " must not be empty!");
							break;
						}
					}
				}
			}else{
				fail("The value from " + key + " must not be empty!");
			}
			assertEquals("the key '" + key + "' was not found on the response", found, true);
		}
	}

	@Then("the response JSON must have \"([^\"]*)\" not equals \"([^\"]*)\"$")
	public void validateDifferentStringJsonBody(String key, String value) throws Throwable {
		value = RestApi.replaceVariablesValues(value);
		if (!"".equals(RestApi.getResponseJson())) {
			JsonNode rootNode = new ObjectMapper().readTree(new StringReader(RestApi.getResponseJson()));

			Hooks.scenario.write("Value found: " + rootNode.at(key).asText());
			assertNotEquals(value, rootNode.at(key).asText());
		}
	}

	@Then("the response JSON must not have the field \"([^\"]*)\"$")
	public void validateFieldNotPresent(String key) throws Throwable {

		if (!"".equals(RestApi.getResponseJson())) {
			JsonNode rootNode = new ObjectMapper().readTree(new StringReader(RestApi.getResponseJson()));

			JsonNode keyNode = rootNode.get(key);

			Hooks.scenario.write("Key Used: " + key);
			assertNull(keyNode);
		}
	}

	@Then("^the response JSON must have the property \"([^\"]*)\" with values \"([^\"]*)\"$")
	public void theResponseJSONMustHaveThePropertyWithValues(String attribute, String values) throws Throwable {

		List<String> strings = Arrays.asList(values.split(","));

		if (!"".equals(RestApi.getResponseJson())) {
			JsonNode rootNode = new ObjectMapper().readTree(new StringReader(RestApi.getResponseJson()));

			strings.forEach(value -> {
				assertTrue(rootNode.at(attribute).toString().contains(value));
			});
		}
	}

	@Then("^I extract the JWT access token$")
	public void extractJwtAccessToken() throws Throwable {
		extractAccessToken();
		setBearerAuthHeader();
	}

	@When("^I download the response File as \"([^\"]*)\"$")
	public void downloadFileFromResponse(String filepath) throws Throwable {
		HttpClient http = new HttpClient(RestApi.getDomain(), RestApi.getApiVersion());
		http.sendGetAndDownload(RestApi.getPath(), RestApi.getParameters(), RestApi.getHeaders(), filepath);
	}

	@Given("^I have a JWT token with the following data:$")
	public void iHaveAJWTTokenWithTheFollowingData(List<List<String>> dataTable) throws Throwable {
		iHaveATokenWithTheFollowingData(dataTable);
	}

	@When("^I extract the date of \"([^\"]*)\" as \"([^\"]*)\"$")
	public void extractDate(String dateTimeParameter, String targetParameter) {
		String from = "${"+ dateTimeParameter +"}";
		String target = "${"+ targetParameter +"}";
		RestApi.getUserParameters().put(target, DateUtils.extractDate(RestApi.getUserParameters().get(from)));
	}

	private String getStringClaim(DecodedJWT jwt, String claimName) {
		if (jwt.getClaim(claimName).isNull()) {
			return "";
		} else {
			return jwt.getClaim(claimName).asString();
		}
	}

	private String getLongClaim(DecodedJWT jwt, String claimName) {
		if (jwt.getClaim(claimName).isNull()) {
			return "0";
		} else {
			return jwt.getClaim(claimName).asLong().toString();
		}
	}

	private List<String> getListClaim(DecodedJWT jwt, String claimName) {
		if (jwt.getClaim(claimName).isNull()) {
			return new ArrayList<String>();
		} else {
			return jwt.getClaim(claimName).asList(String.class);
		}
	}

	private void extractAccessToken() {
		if (RestApi.getResponse().getHeaders("Authorization").length > 0) {
			RestApi.setAccessToken(RestApi.getResponse().getHeaders("Authorization")[0].getValue());
		} else {
			RestApi.setAccessToken("");
		}

		if ("".equals(RestApi.getAccessToken())) {
			if (!"".equals(RestApi.getResponseJson())) {
				JsonNode rootNode = null;
				try {
					rootNode = new ObjectMapper().readTree(new StringReader(RestApi.getResponseJson()));

					if (rootNode != null) {
						RestApi.setAccessToken(rootNode.at("/accessToken").asText());
						RestApi.setRefreshToken(rootNode.at("/refreshToken").asText());
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void extractResponseTokens() {
		List<String> domainsExtractAccessToken = new ArrayList<>();

		if(getProperty("domains.extract.access.token") != null)
			domainsExtractAccessToken = Arrays.asList(getProperty("domains.extract.access.token").split(","));

		if(RestApi.getApiVersion() == null || RestApi.getApiVersion().isEmpty()) {
			if (RestApi.getDomain() != null && !RestApi.getDomain().isEmpty()) {
				RestApi.setApiVersion(getProperty(RestApi.getDomain() + ".api.version"));
				if(RestApi.getApiVersion() == null || RestApi.getApiVersion().isEmpty())
					RestApi.setApiVersion(getProperty("api.version"));
			}else {
				RestApi.setApiVersion(getProperty("api.version"));
			}
		}

		if (RestApi.getApiVersion() != null && domainsExtractAccessToken.contains(RestApi.getApiVersion())) {
			extractAccessToken();
		}
	}

	private String generateRandomString() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	private String generateRandomNumber() {
		return Long.toString(Instant.now().toEpochMilli());
		//Random randomGenerator = new Random();
		//String randomInt = Integer.toString(randomGenerator.nextInt(1000000));
	}

	private String getCompletePath() {
		String completePath = getProperty("host.schema") + "://" + getProperty("host.name") + ":"
				+ getProperty("host.port") + getProperty("api.version") + RestApi.getPath();
		if (RestApi.getApiVersion() != null && !RestApi.getApiVersion().isEmpty()) {
			completePath = getProperty("host.schema") + "://" + getProperty("host.name") + ":"
					+ getProperty("host.port") + RestApi.getApiVersion() + RestApi.getPath();
		}
		if (RestApi.getDomain() != null && !RestApi.getDomain().isEmpty()) {
			if (getProperty(RestApi.getDomain() + ".host.name") != null && !getProperty(RestApi.getDomain() + ".host.name").isEmpty()) {
				completePath = getProperty(RestApi.getDomain() + ".host.schema") + "://" + getProperty(RestApi.getDomain() + ".host.name") + ":"
						+ getProperty(RestApi.getDomain() + ".host.port") + getProperty(RestApi.getDomain() + ".api.version") + RestApi.getPath();
			}
		}

		return completePath;
	}

	private void createNewBusinessAndUser(String authorities) throws Throwable {
		String domainOld = RestApi.getDomain();
		String apiVersionOld = RestApi.getApiVersion();
		DocumentsBrazil documents = new DocumentsBrazil();
		String docCNPJ = documents.cnpj(false);
		String docCPF =  documents.cpf(false);
		String emailCPF = docCPF + "@gmail.com";

		createToken("","", authorities);

		setEnvironment("account");
		setRoute("/business-accounts/register-email?clientId=" + RestApi.getUserParameters().get("${client_id}"));
		RestApi.saveVariable("companyName","Empresa Testes QA");
		RestApi.saveVariable("nationalRegistrationId",docCNPJ);
		RestApi.saveVariable("nationalRegistrationCountry","BRA");
		RestApi.saveVariable("stateRegistrationId","0001");
		RestApi.saveVariable("state","01");
		RestApi.saveVariable("municipalRegistrationId","00001");
		RestApi.saveVariable("city","");
		RestApi.saveVariable("email",emailCPF);
		RestApi.saveVariable("fullName","Nome A" + docCPF);
		RestApi.saveVariable("documentId",docCPF);
		RestApi.saveVariable("","BRA");

		setJsonBody(BusinessAccountJson.jsonRegisterEmail());
		postRequest();
		pathToReport();
		responseToReport();
		saveJsonValue("/businessId","business_id");
		saveJsonValue("/userId","user_id");

		setEnvironment(domainOld);
		RestApi.setApiVersion(apiVersionOld);
	}

	private void createToken(String businessId, String userId, String authorities) throws Throwable {
		String jsonToken = JWTJson.jsonJWT(businessId, userId);

		generateMilisecondsWithMinutes("iat","0");
		generateMilisecondsWithMinutes("exp","15");

		RestApi.getUserParameters().put("${jti}"        , "9e74d1f6-7095-4718-8f11-47dc9ec99c63");
		RestApi.getUserParameters().put("${authorities}", authorities);
		RestApi.getUserParameters().put("${scope}"      , "\"read\",\"write\"");

		setHeader("Content-Type", "application/json");
		setJsonToken(jsonToken);
		setBearerAuthHeader();
	}

	@And("^I have a token with the following data:$")
	public void iHaveATokenWithTheFollowingData(List<List<String>> dataTable) throws Throwable {
		LinkedHashMap<String, String> tableHash = BuildHash.dataTableToHashLine(dataTable);

		if (tableHash.containsKey("client_id")) {
			RestApi.saveVariable("client_id", tableHash.get("client_id"));
		} else {
			Client.setPropertyId();
		}

		if (tableHash.containsKey("app_id")) {
			RestApi.saveVariable("app_id", tableHash.get("app_id"));
		} else {
			Client.setPropertyAppId();
		}

		if (tableHash.containsKey("business_id")) {
			if (!tableHash.get("business_id").equals("NEW")) {
				verifyUserId(tableHash);
				RestApi.saveVariable("business_id", tableHash.get("business_id"));
			} else {
				createNewBusinessAndUser(tableHash.get("authorities"));
			}
		}


		if ((tableHash.containsKey("user_id")) && (tableHash.containsKey("business_id"))){
			createToken(tableHash.get("business_id"),tableHash.get("user_id") , tableHash.get("authorities"));
		} else if (tableHash.containsKey("user_id")) {
			createToken("",tableHash.get("user_id") , tableHash.get("authorities"));
		} else if (tableHash.containsKey("business_id")) {
			createToken(tableHash.get("business_id"),"" , tableHash.get("authorities"));
		} else {
			createToken("","" , tableHash.get("authorities"));
		}
	}

	private void verifyUserId(LinkedHashMap<String, String> tableHash) {
		if (tableHash.containsKey("user_id")){
			if (!RestApi.getUserParameters().containsKey("${user_id}")) {
				RestApi.saveVariable("user_id", tableHash.get("user_id"));
			} else if (!tableHash.get("user_id").equals("${user_id}")) {
				RestApi.saveVariable("user_id", tableHash.get("user_id"));
			}
		}
	}

	public void fillVariablesAndSetBody(List<List<String>> dataTable, String stJson) throws Throwable {
		BuildHash.dataTableToParameter(dataTable);
		setJsonBody(stJson);
	}

	@And("^I clean queryparameter$")
	public void iCleanQueryparameter() throws Throwable {
		RestApi.getParameters().clear();
	}

	@And("^I set queryparameter \"([^\"]*)\" as today$")
	public void iSetQueryparameterAsToday(String key) throws Throwable {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime now = LocalDateTime.now();

		String value = dtf.format(now);
		value = RestApi.replaceVariablesValues(value);
		if (!"".equals(RestApi.getResponseJson())) {
			JsonNode rootNode = new ObjectMapper().readTree(new StringReader(RestApi.getResponseJson()));

			if (!key.equals("size")) {
				Hooks.scenario.write("Value found: " + rootNode.at(key).asText());
				assertEquals(value.trim(), rootNode.at(key).asText().trim());
			} else {
				Hooks.scenario.write("Value found: " + value);

				if (rootNode == null) {
					assertEquals(Integer.parseInt(value), 0);
				} else {
					assertEquals(Integer.parseInt(value), rootNode.size());
				}
			}
		}
	}

	@Then("^the response JSON must have \"([^\"]*)\" as tomorrow$")
	public void theResponseJSONMustHaveAsTomorrow(String key) throws Throwable {
		//DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		//LocalDateTime now = LocalDateTime.now();
		DateFormat dtf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, +1);
		Date yesterday = calendar.getTime();


		String value = dtf.format(yesterday);
		value = RestApi.replaceVariablesValues(value);
		if (!"".equals(RestApi.getResponseJson())) {
			JsonNode rootNode = new ObjectMapper().readTree(new StringReader(RestApi.getResponseJson()));

			if (!key.equals("size")) {
				Hooks.scenario.write("Value found: " + rootNode.at(key).asText());
				assertEquals(value.trim(), rootNode.at(key).asText().trim());
			} else {
				Hooks.scenario.write("Value found: " + value);

				if (rootNode == null) {
					assertEquals(Integer.parseInt(value), 0);
				} else {
					assertEquals(Integer.parseInt(value), rootNode.size());
				}
			}
		}
	}

	/*@And("^I create jsonBody invoice \"([^\"]*)\"$")
	public void validarAthenas1()throws JsonParseException, JsonMappingException, IOException {
		NewBankSlip newBankSlip = new NewBankSlip();
		newBankSlip.setBarCode("11111.11111 222222.22222");
		newBankSlip.setLinkInvoice("www.invoice.com/get?id=WAOBYA21ZI");

		String json = JsonUtils.converterObjetoParaJson(newBankSlip);

	}*/
}