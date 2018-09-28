package br.com.experian.cucumber.integration.cucumber.definitions;

import java.io.IOException;

public class UserAccountJson {

    public static String jsonRegisterEmail() throws IOException {
        String stJson = "{\n" +
                        "  \"mobile\": {\n" +
                        "    \"regionCode\": ${regionCode},\n" +
                        "    \"areaCode\": ${areaCode},\n" +
                        "    \"phoneNumber\": ${phoneNumber}\n" +
                        "  },\n" +
                        "  \"email\": \"${email}\",\n" +
                        "  \"fullName\": \"${fullName}\",\n" +
                        "  \"document\": {\n" +
                        "    \"documentId\": \"${documentId}\",\n" +
                        "    \"documentCountry\": \"${documentCountry}\"\n" +
                        "  }\n" +
                        "}";
        return stJson;
    }
}
