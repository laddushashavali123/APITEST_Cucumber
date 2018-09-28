package br.com.experian.cucumber.integration.cucumber.definitions;

import br.com.experian.cucumber.integration.cucumber.common.utils.RestApi;

public class JWTJson {

    public static String jsonJWT(String businessId, String userId) {
        String stBusiness = "";
        String stUser = "";
        String stApp = "";

        if (!businessId.isEmpty()) {
            RestApi.getUserParameters().put("${business_id}"  , businessId);
            stBusiness = "   \"business_id\": \"${business_id}\",\n";
        }

        if (!userId.isEmpty()) {
            RestApi.getUserParameters().put("${user_id}"  , userId);
            stUser = "   \"user_id\": \"${user_id}\",\n";
        }

        if (RestApi.existVariable("app_id")){
            stApp = "   \"app_id\": \"${app_id}\",\n";
        }

        String jsonToken = "{\n" +
                "   \"jti\": \"${jti}\",\n" +
                "   \"iat\": ${iat},\n" +
                "   \"scope\": [ ${scope} ],\n" +
                "   \"client_id\": \"${client_id}\",\n" +
                stBusiness +
                stUser +
                stApp +
                "   \"authorities\": [ ${authorities} ],\n" +
                "   \"exp\": ${exp}\n" +
                "}";

        return jsonToken;
    }
}
