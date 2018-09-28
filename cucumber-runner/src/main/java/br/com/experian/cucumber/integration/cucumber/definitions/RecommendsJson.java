package br.com.experian.cucumber.integration.cucumber.definitions;

public class RecommendsJson {

    public static String jsonMyConsults() {
        String jsonToken =  "{\n" +
                            "   \"document\": \"{$document}\"\n" +
                            "}";
        return jsonToken;
    }
}
