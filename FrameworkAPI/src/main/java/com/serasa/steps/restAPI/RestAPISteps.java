package com.serasa.steps.restAPI;

	
import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serasa.common.utils.httpclient.HttpClient;
import com.serasa.steps.Hooks;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class RestAPISteps {
	
	private static Boolean allowAll = true;
	private static String hostschema = "https";
	private static String apigeeVersion = "";
	private static Integer hostport = null;
	private static String path = "";
	private static List<Header> headers = new ArrayList<>();
	private static List<NameValuePair> parameters = new ArrayList<>();
	private static String jsonBodyString;
	private static StringEntity entity;
	private static HttpResponse response;
	private static LinkedHashMap<String, String> userParameters = new LinkedHashMap<String, String>();
	
//--------------------------------------------------COMMON FUNCTIONS----------------------------------------------------------	
	@Given("I run the test case \"([^\"]*)\"$")
	public void setTestCase(String testCase) throws Throwable {
	}
	
	
	@Given("^I use the route \"([^\"]*)\"$")
	public static void setRoute(String route) throws Throwable {
		path = apigeeVersion + replaceVariablesValues(route);
		path = path.replaceAll(" ", "%20");
		path = StringUtils.stripAccents(RestAPISteps.path);
		Hooks.scenario.write(getRequestUrl());
	}

	@Given("^I set queryparameter \"([^\"]*)\" as \"([^\"]*)\"$")
	public void setQueryParameter(String key, String value) throws Throwable {
		parameters.add(new BasicNameValuePair(key, value));
	}
	
	@Given("^I set pathparameter \"([^\"]*)\" as \"([^\"]*)\"$")
	public void setPathParameter(String key, String value) throws Throwable {
		path = path.replaceAll(key, value);
	}	
	
	@Given("^I set header \"([^\"]*)\" as \"([^\"]*)\"$")
	public static void setHeader(String key, String value) throws Throwable {
		headers.add(new BasicHeader(key, value));
	}

	@Given("^I clear all headers$")
	public void clearHeader() throws Throwable {
		RestAPISteps.headers = new ArrayList<>();
	}
	
	public static void setJsonBody(String jsonBody) throws Throwable {
		
		//replace variable values
		RestAPISteps.jsonBodyString = replaceVariablesValues(jsonBody);
		Hooks.scenario.write(jsonBodyString); 
	}
	
	@When("^I send the POST request$")
	public static void postRequest() throws Throwable{
		HttpClient http = new HttpClient();
		//headers.add(new BasicHeader("Content-Type", "application/json"));
		//populate jsonString on previous step
		entity = new StringEntity(jsonBodyString, ContentType.APPLICATION_JSON);
		response = http.sendPost(hostschema, Hooks.hostname, hostport, path, parameters, headers, entity, allowAll);
		extractJsonResponse();
	}
	
	@When("^I send the GET request$")
	public static void getRequest() throws Throwable{
		HttpClient http = new HttpClient();
		//headers.add(new BasicHeader("Content-Type", "application/json"));

		response = http.sendGet(hostschema, Hooks.hostname, hostport, path, parameters, headers, allowAll);
		extractJsonResponse();
	}
	
	@When("^I send the PUT request$")
	public void putRequest() throws Throwable{
		HttpClient http = new HttpClient();
		//headers.add(new BasicHeader("Content-Type", "application/json"));
		//populate jsonString on previous step
		entity = new StringEntity(jsonBodyString, ContentType.APPLICATION_JSON);
		response = http.sendPut(hostschema, Hooks.hostname, hostport, path, parameters, headers, entity, allowAll);
		extractJsonResponse();
	}
	
	@When("^I send the DELETE request$")
	public void deleteRequest() throws Throwable{
		HttpClient http = new HttpClient();
		//headers.add(new BasicHeader("Content-Type", "application/json"));
		response = http.sendDelete(hostschema, Hooks.hostname, hostport, path, parameters, headers, allowAll);
		extractJsonResponse();
	}

	@Then("^I print the path$")
	public static void pathToReport() throws Throwable {
		if (hostport == null){
			Hooks.scenario.write(hostschema + "://" + Hooks.hostname + path);
		}else{
			Hooks.scenario.write(hostschema + "://" + Hooks.hostname + ":" + hostport + path);
		}
		Hooks.scenario.write(headers.toString());
		Hooks.scenario.write(parameters.toString());
	}

	@Then("^I print the response$")
	public static void responseToReport() throws Throwable {
				
		Hooks.scenario.write(response.getStatusLine().toString());
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			Object json = mapper.readValue(Hooks.responseJson, Object.class);
			String prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
			Hooks.scenario.write(prettyJson);
		} catch (Exception e) {

		}
	}

	@Then("^Http response should be (\\d+)$")
	public static void validateResponse(Integer responseCode) throws Throwable {
		Integer statusCode = response.getStatusLine().getStatusCode();
		assertEquals(responseCode.toString(), statusCode.toString());
	}

	private static void extractJsonResponse() throws Throwable{
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		Hooks.responseJson = result.toString();
	}
	
	private static String replaceVariablesValues(String text) throws Throwable { 
		String rx = "(\\$\\{[^}]+\\})";

		StringBuffer sb = new StringBuffer();
		Pattern p = Pattern.compile(rx);
		Matcher m = p.matcher(text);

		while (m.find()){
		    // Avoids throwing a NullPointerException in the case that you
		    // Don't have a replacement defined in the map for the match
		    String repString = userParameters.get(m.group(1));
		    if (repString != null)    
		        m.appendReplacement(sb, repString);
		}
		m.appendTail(sb);
		
		return sb.toString();
	}
	
	private static String getRequestUrl(){
		
		if (RestAPISteps.hostport == null) {
			return RestAPISteps.hostschema + "://" + Hooks.hostname + RestAPISteps.path;
		}else{
			return RestAPISteps.hostschema + "://" + Hooks.hostname + ":" + RestAPISteps.hostport.toString() + RestAPISteps.path;
		}
	}
}