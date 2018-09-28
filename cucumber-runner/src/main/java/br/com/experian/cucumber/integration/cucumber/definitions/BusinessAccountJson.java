package br.com.experian.cucumber.integration.cucumber.definitions;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

import java.io.IOException;

public class BusinessAccountJson {

    public static String jsonRegisterEmail(){
        String stJson = "{\n" +
                        "  \"companyName\": \"${companyName}\",\n" +
                        "  \"companyDocument\": {\n" +
                        "    \"nationalRegistrationId\": \"${nationalRegistrationId}\",\n" +
                        "    \"nationalRegistrationCountry\": \"${nationalRegistrationCountry}\",\n" +
                        "    \"stateRegistrationId\": \"${stateRegistrationId}\",\n" +
                        "    \"state\": \"${state}\",\n" +
                        "    \"municipalRegistrationId\": \"${municipalRegistrationId}\",\n" +
                        "    \"city\": \"${city}\"\n" +
                        "  },\n" +
                        "  \"userAccount\": {\n" +
                        "    \"email\": \"${email}\",\n" +
                        "    \"fullName\": \"${fullName}\",\n" +
                        "    \"document\": {\n" +
                        "      \"documentId\": \"${documentId}\",\n" +
                        "      \"documentCountry\": \"${documentCountry}\"\n" +
                        "    }\n" +
                        "  }\n" +
                        "}";
        return stJson;
    }

    public static String jsonInviteUserMobile(){
        String stJson = "[{\n" +
                        "   \"mobile\": {\n" +
                        "     \"regionCode\": ${regionCode},\n" +
                        "     \"areaCode\": ${areaCode},\n" +
                        "     \"phoneNumber\": ${phoneNumber}\n" +
                        "   },\n" +
                        "   \"fullName\": \"${fullName}\"\n" +
                        "}]";
        return stJson;
    }

    public static String jsonAcceptInvitation(){
        String stJson = "{\n" +
                        "   \"token\": \"${token}\",\n" +
                        "   \"inviteId\": \"${inviteId}\"\n" +
                        "}";
        return stJson;
    }
}
