package br.com.experian.cucumber.integration.cucumber.common.utils;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

import java.io.IOException;

public class JsonUtil {

    private static JsonNode node;
    private static String json = "";

    public static JsonNode getNode() {
        return node;
    }

    public static void setNode(JsonNode node) {
        JsonUtil.node = node;
    }

    public static String getJson() {
        return json;
    }

    public static void setJson(String json) {
        JsonUtil.json = json;
    }

    public static ObjectNode createNode(String stJson) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        setNode(objectMapper.readValue(stJson, JsonNode.class));
        return (ObjectNode) getNode();
    }
}
