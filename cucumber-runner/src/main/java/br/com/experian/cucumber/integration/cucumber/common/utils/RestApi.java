package br.com.experian.cucumber.integration.cucumber.common.utils;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RestApi {

    private static String id;
    private static String token;
    private static String refreshToken;
    private static String accessToken;

    private static String path = "";
    private static List<Header> headers = new ArrayList<>();
    private static List<NameValuePair> parameters = new ArrayList<>();
    private static String jsonBodyString = null;
    private static StringEntity entity = null;

    private static HttpResponse response;
    private static String responseJson;

    private static LinkedHashMap<String, String> userParameters = new LinkedHashMap<String, String>();
    private static String domain = "";

    private static String collection = "";
    private static String jsonDocument = "";
    private static String column = "";
    private static String value = "";
    private static String apiVersion = "";

    //Dados MongoDB
    private static String host;
    private static int port;
    private static String database;

    public static String getId() {
        return id;
    }

    public static void setId(String idAux) {
        RestApi.id = idAux;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String tokenAux) {
        RestApi.token = tokenAux;
    }

    public static String getRefreshToken() {
        return refreshToken;
    }

    public static void setRefreshToken(String refreshTokenAux) {
        RestApi.refreshToken = refreshTokenAux;
    }

    public static String getAccessToken() {
        return accessToken;
    }

    public static void setAccessToken(String accessTokenAux) {
        RestApi.accessToken = accessTokenAux;
    }

    public static String getPath() {
        return path;
    }

    public static void setPath(String path) {
        RestApi.path = path;
    }

    public static List<Header> getHeaders() {
        return headers;
    }

    public static void setHeaders(List<Header> headers) {
        RestApi.headers = headers;
    }

    public static List<NameValuePair> getParameters() {
        return parameters;
    }

    public static void setParameters(List<NameValuePair> parameters) {
        RestApi.parameters = parameters;
    }

    public static String getJsonBodyString() {
        return jsonBodyString;
    }

    public static void setJsonBodyString(String jsonBodyString) {
        RestApi.jsonBodyString = jsonBodyString;
    }

    public static StringEntity getEntity() {
        return entity;
    }

    public static void setEntity(StringEntity entity) {
        RestApi.entity = entity;
    }

    public static HttpResponse getResponse() {
        return response;
    }

    public static void setResponse(HttpResponse response) {
        RestApi.response = response;
    }

    public static String getResponseJson() {
        return responseJson;
    }

    public static void setResponseJson(String responseJson) {
        RestApi.responseJson = responseJson;
    }

    public static LinkedHashMap<String, String> getUserParameters() {
        return userParameters;
    }

    public static void setUserParameters(LinkedHashMap<String, String> userParameters) {
        RestApi.userParameters = userParameters;
    }

    public static String getDomain() {
        return domain;
    }

    public static void setDomain(String domain) {
        RestApi.domain = domain;
    }

    public static String getCollection() {
        return collection;
    }

    public static void setCollection(String collection) {
        RestApi.collection = collection;
    }

    public static String getJsonDocument() {
        return jsonDocument;
    }

    public static void setJsonDocument(String jsonDocument) {
        RestApi.jsonDocument = jsonDocument;
    }

    public static String getColumn() {
        return column;
    }

    public static void setColumn(String column) {
        RestApi.column = column;
    }

    public static String getValue() {
        return value;
    }

    public static void setValue(String value) {
        RestApi.value = value;
    }

    public static String getApiVersion() {
        return apiVersion;
    }

    public static void setApiVersion(String apiVersion) {
        RestApi.apiVersion = apiVersion;
    }

    public static String getHost() {
        return host;
    }

    public static void setHost(String host) {
        RestApi.host = host;
    }

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        RestApi.port = port;
    }

    public static String getDatabase() {
        return database;
    }

    public static void setDatabase(String database) {
        RestApi.database = database;
    }

    public static String replaceVariablesValues(String text) throws Throwable {
        String rx = "(\\$\\{[^}]+\\})";

        StringBuffer sb = new StringBuffer();
        Pattern p = Pattern.compile(rx);
        Matcher m = p.matcher(text);

        while (m.find()) {
            // Avoids throwing a NullPointerException in the case that you
            // Don't have a replacement defined in the map for the match
            String repString = getUserParameters().get(m.group(1));
            if (repString != null)
                m.appendReplacement(sb, repString);
        }
        m.appendTail(sb);

        return sb.toString();
    }

    public static void saveVariable(String userKey, String value) {
        RestApi.getUserParameters().put("${" + userKey + "}", value);
    }

    public static String loadVariable(String userKey) {
        return RestApi.getUserParameters().get("${" + userKey + "}");
    }

    public static Boolean existVariable(String userKey) {
        return RestApi.getUserParameters().containsKey("${" + userKey + "}");
    }
}