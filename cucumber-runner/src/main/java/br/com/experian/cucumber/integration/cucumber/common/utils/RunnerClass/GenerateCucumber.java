package br.com.experian.cucumber.integration.cucumber.common.utils.RunnerClass;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.models.*;
import io.swagger.models.parameters.*;
import io.swagger.models.properties.*;
import io.swagger.parser.SwaggerParser;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenerateCucumber {

    private static String jsonBody = "";
    private static String examples = "| ";
    private static String validationExamples = "| ";
    private static String requiredFields = "| ";
    private static String enums = "";
    private static Swagger swagger;

    public static void main(String[] args) {

        // read a swagger description
        swagger = new SwaggerParser().read("C:\\Users\\cwg0660\\Documents\\Athenas\\CORP-App-invoice-BR-v1.yaml");
        generateCucumber(swagger);

        //http swagger example
        //swagger = new SwaggerParser().read("http://petstore.swagger.io/v2/swagger.json");
        //generateCucumber(swagger);

    }

    private static void generateCucumber(Swagger swagger){
        System.out.println("Feature: Test swagger " + swagger.getInfo().getTitle());

        //loop endpoints
        Map<String, Path> paths = swagger.getPaths();
        paths.forEach((key, path)-> {
            System.out.println("# - " + key);

            //POST
            if (path.getPost() != null) {
                generateScenarioRoute(key, "POST");
                generateScenarioParameters(path.getPost().getParameters());
                generateScenarioAction("POST");
                generateAssertions(path.getPost().getResponses());
                generateScenarioExamples();
            }

            //PUT
            if (path.getPut() != null) {
                generateScenarioRoute(key, "PUT");
                generateScenarioParameters(path.getPut().getParameters());
                generateScenarioAction("PUT");
                generateAssertions(path.getPut().getResponses());
                generateScenarioExamples();
            }
            //PATCH
            if (path.getPatch() != null) {
                generateScenarioRoute(key, "PATCH");
                generateScenarioParameters(path.getPatch().getParameters());
                generateScenarioAction("PATCH");
                generateAssertions(path.getPatch().getResponses());
                generateScenarioExamples();
            }
            //GET
            if (path.getGet() != null) {
                generateScenarioRoute(key, "GET");
                generateScenarioParameters(path.getGet().getParameters());
                generateScenarioAction("GET");
                generateAssertions(path.getGet().getResponses());
                generateScenarioExamples();
            }
            //DELETE
            if (path.getDelete() != null) {
                generateScenarioRoute(key, "DELETE");
                generateScenarioParameters(path.getDelete().getParameters());
                generateScenarioAction("DELETE");
                generateAssertions(path.getDelete().getResponses());
                generateScenarioExamples();
            }

        });

    }

    // Recursive method to generate assertions Steps according to the reposenses Map
    private static void generateAssertions(Map<String, Response> responses){

        if (responses != null) {

            responses.forEach((responseKey, response) -> {

                if (response.getDescription() != null ){
                    System.out.println("#    Validating - " + response.getDescription());
                }

                // checks response code
                if (responseKey.contains("default")) {
                    System.out.println("#    Negative default test response validation");
                } else {
                    if (responseKey.startsWith("20")) {

                        System.out.println("#    Positive responses validation");
                    }else{
                        System.out.println("#    Negatives responses validation");
                    }
                    System.out.println("    Then Http response should be " + responseKey);
                }

                if (response.getSchema() != null) {
                    //Gets assertions from a Reference
                    if (response.getSchema() instanceof RefProperty) {
                        RefProperty refResponse = (RefProperty) response.getSchema();
                        String simpleRef = refResponse.getSimpleRef();
                        generateAssertions("", simpleRef);

                    //Gets assertions from an Array of properties
                    }else if (response.getSchema() instanceof ArrayProperty) {
                        ArrayProperty arrayResponse = (ArrayProperty) response.getSchema();
                        if (arrayResponse.getItems() != null){
                            generateResponseAssert("", "0", arrayResponse.getItems());
                        }
                    }else{
                        //System.out.println(response.getSchema().toString());
                    }
                }
            });
        }
    }

    // Recursive method to generate assertions given a swagger Reference
    private static void generateAssertions(String nodeName, String simpleRef) {

        Map<String, Property> properties = swagger.getDefinitions().get(simpleRef).getProperties();
        if (properties != null) {
            properties.forEach((key, propertyValue) -> {
                generateResponseAssert(nodeName, key, propertyValue);
            });
        }else{
            Model model = swagger.getDefinitions().get(simpleRef);

            if (model instanceof ArrayModel) {
                ArrayModel arrayModel = (ArrayModel) model;
                Property property = arrayModel.getItems();
                if (property instanceof StringProperty) {
                    //generateResponseAssert(nodeName + "/0", property.getName(), property);
                    generateResponseAssert(nodeName, property.getName(), property);

                }else if (property instanceof ObjectProperty) {
                    ObjectProperty items = (ObjectProperty) property;
                    items.getProperties().forEach((key, propertyValue) -> {
                        generateResponseAssert(nodeName + "/0", key, propertyValue);
                    });
                }else if (property instanceof RefProperty) {
                    RefProperty ref = (RefProperty) property;
                    String simpleRefChild = ref.getSimpleRef();
                    generateAssertions(nodeName + "/0", simpleRefChild);
                }
            }
        }
    }

    // Recursive method to generate assertions given a swagger Property
    private static void generateResponseAssert(String nodeName, String key, Property propertyValue){

        String finalNodeName = nodeName + "/" + key;

        // Regular properties
        if ( "integer".equals(propertyValue.getType())
                || "number".equals(propertyValue.getType())
                || "string".equals(propertyValue.getType())
                || "boolean".equals(propertyValue.getType())) {

            if (propertyValue instanceof StringProperty) {
                StringProperty stringProperty = (StringProperty) propertyValue;
                if (stringProperty.getEnum() != null) {
                    System.out.println("#    Possible \"" + key + "\" values must be : " + stringProperty.getEnum().toString());
                }
            }

            if ("accessToken".equals(key)){
                //Steps to validate a JWT

                System.out.println("    When I print the JWT access token");
                System.out.println("# To set a JWT token for the next rote");
                System.out.println("#    Given I set the Bearer Authorization header");
                System.out.println("# Validating generated JWT token");
                System.out.println("#    Then the JWT must have \"scope\" as \"[write, read]\"");
                System.out.println("#    Then the JWT must have \"user_id\" as \"<userId>\"");
                System.out.println("#    Then the JWT must have \"client_id\" as \"<clientId>\"");
                System.out.println("#    Then the JWT must have \"business_id\" as \"<businessId>\"");
                System.out.println("#    Then the JWT must have \"authorities\" as \"[ROLE_CLI-AUTH-IDENTIFIED, ROLE_CLI-1STPARTY, ROLE_USER]\"");
                System.out.println("#    Then the JWT must NOT have \"authorities\" as \"[ROLE_AUTH-BASIC, ROLE_AUTH-IDENTIFIED, ROLE_AUTH-MFA]\"");

            }else{
                System.out.println("    Then the response JSON must have \"" + finalNodeName + "\" as the String \"<" + key + ">\"");
                validationExamples += key + " | ";
            }


        }

        // Recursive properties
        if (propertyValue instanceof RefProperty) {
            RefProperty ref = (RefProperty) propertyValue;
            String simpleRef = ref.getSimpleRef();

            generateAssertions(finalNodeName, simpleRef);

        } else if (propertyValue instanceof ArrayProperty) {
            ArrayProperty array = (ArrayProperty) propertyValue;

            if (array.getItems() instanceof RefProperty) {
                generateResponseAssert(finalNodeName, "0", array.getItems());

            } else if (array.getItems() instanceof ObjectProperty) {
                ObjectProperty items = (ObjectProperty) array.getItems();
                items.getProperties().forEach((objectKey, objectProperty) -> {
                    generateResponseAssert(finalNodeName + "/0", objectKey, objectProperty);
                });

            }else {
                // single lis array
                System.out.println("    Then the response JSON must have the property \"" + finalNodeName + "\" with values \"<" + key + ">\"");
            }
            validationExamples += key + " | ";

        }else if (propertyValue instanceof ObjectProperty) {

            ObjectProperty items = (ObjectProperty) propertyValue;
            items.getProperties().forEach((objectKey, objectProperty) -> {
                generateResponseAssert(finalNodeName, objectKey, objectProperty);
            });
        }
    }



    //recursive generates json body givven a refName
    private static void generateJsonBody(String refName) {

        //String swaggerString = Json.pretty(swagger.getDefinitions().get(refName).getProperties());
        Map<String, Property> properties = swagger.getDefinitions().get(refName).getProperties();

        //single properties
        if (properties != null) {
            jsonBody += "{";
            properties.forEach((key, propertyValue) -> {
                generatePropertyJson(key, propertyValue, true);
            });
            jsonBody += "},";
        }else {
            //Array properties
            Model model = swagger.getDefinitions().get(refName);
            if (model instanceof ArrayModel) {
                //jsonBody += "[{";
                jsonBody += "[";
                ArrayModel arrayModel = (ArrayModel) model;
                Property property = arrayModel.getItems();

                if (property instanceof StringProperty) {
                    generatePropertyJson("", property, false);

                }else if (property instanceof ObjectProperty) {
                    jsonBody += "{";
                    ObjectProperty items = (ObjectProperty) property;
                    items.getProperties().forEach((key, propertyValue) -> {
                        generatePropertyJson(key, propertyValue, true);
                    });
                    jsonBody += "}";
                }else if (property instanceof RefProperty) {
                    jsonBody += "{";
                    RefProperty ref = (RefProperty) property;
                    String simpleRefChild = ref.getSimpleRef();
                    generateJsonBody(simpleRefChild);
                    jsonBody += "}";
                }
                //jsonBody += "}],";
                jsonBody += "],";
            }
        }
    }

    //generates a json part given a property and key.
    // if addKeyToJSon is false, the key will not be added to the json string
    private static void generatePropertyJson(String key, Property propertyValue, boolean addKeyToJSon){

        if (propertyValue.getRequired()){
            requiredFields += key + " | ";
        }

        if ( "integer".equals(propertyValue.getType()) || "number".equals(propertyValue.getType()) ) {
            if (addKeyToJSon) {
                jsonBody += "\"" + key + "\": 0,";
            }else {
                jsonBody += "0,";
            }
            examples += key + " | ";
        } else if ("string".equals(propertyValue.getType())) {

            if (propertyValue instanceof StringProperty) {
                StringProperty stringProperty = (StringProperty) propertyValue;
                if (stringProperty.getEnum() != null) {
                    enums += "\n#    Possible \"" + key + "\" values must be : " + stringProperty.getEnum().toString();
                }
            }

            if (addKeyToJSon) {
                jsonBody += "\"" + key + "\": \"string\",";
            }else {
                jsonBody += "\"string\",";
            }
            examples += key + " | ";
        } else if ("boolean".equals(propertyValue.getType())) {
            BooleanProperty boolProp = (BooleanProperty) propertyValue;
            String value = "false";

            if (boolProp.getDefault() != null) {
                value = boolProp.getDefault().toString();
            }

            if (addKeyToJSon) {
                jsonBody += "\"" + key + "\": " + value + ",";
            }else {
                jsonBody += value + ",";
            }
            examples += key + " | ";
        }

        //LongProperty;
        //RefProperty;
        //ArrayProperty;
        if (propertyValue instanceof RefProperty) {
            RefProperty ref = (RefProperty) propertyValue;
            String simpleRef = ref.getSimpleRef();
            if (addKeyToJSon) {
                jsonBody += "\"" + key + "\": ";
            }
            generateJsonBody(simpleRef);

        } else if (propertyValue instanceof ArrayProperty) {
            ArrayProperty array = (ArrayProperty) propertyValue;

            if (addKeyToJSon) {
                jsonBody += "\"" + key + "\": [";
            }else{
                jsonBody += "[";
            }
            generatePropertyJson(key, array.getItems(),false);
            jsonBody += "],";

        }else if (propertyValue instanceof ObjectProperty) {
            if (addKeyToJSon) {
                jsonBody += "\"" + key + "\": {";
            }else {
                jsonBody += "{";
            }

            ObjectProperty items = (ObjectProperty) propertyValue;
            items.getProperties().forEach((objectKey, objectProperty) -> {
                generatePropertyJson(objectKey, objectProperty, true);
            });
            jsonBody += "},";
        }
    }

    private static void removeWrongComma(){
        jsonBody = jsonBody.replaceAll(",$", "");
        jsonBody = jsonBody.replaceAll(",}", "}");
        jsonBody = jsonBody.replaceAll(",]", "]");
    }

    // regex to replace json values to cucumber variables
    private static String addCucumberVariables(String prettyJson){
        // add string parameters as "<string>"
        Pattern p = Pattern.compile("\"(\\w+)\" : \"(\\w+)\"");
        Matcher m = p.matcher(prettyJson);
        if (m.find()) {
            prettyJson = m.replaceAll("\"$1\": \"<$1>\"");
        }

        // add numeric parameters as "<string>"
        p = Pattern.compile("\"(\\w+)\" : (\\d+)");
        m = p.matcher(prettyJson);
        if (m.find()) {
            prettyJson = m.replaceAll("\"$1\": <$1>");
        }

        // add list parameters as [ "<string>" ]
        p = Pattern.compile("\"(\\w+)\" : \\[ \"(\\w+)\" \\]");
        m = p.matcher(prettyJson);
        if (m.find()) {
            prettyJson = m.replaceAll("\"$1\": \\[ \"<$1>\" \\]");
        }

        // ident
        p = Pattern.compile("(^.)", Pattern.MULTILINE);
        m = p.matcher(prettyJson);
        if (m.find()) {
            prettyJson = m.replaceAll("          $1");
        }

        return prettyJson;
    }

    //Set Scenario, json header and route
    private static void generateScenarioRoute(String route, String requestVerb){
        System.out.println("Scenario Outline: " + requestVerb + " - " + route);
        System.out.println("    Given I use the route \"" + route + "\"");
        System.out.println("      And I set header \"Content-Type\" as \"application/json\"");
    }

    //Generate the scenario params (Header, query, path and json body params
    private static void generateScenarioParameters(List<Parameter> params){

        for (Parameter param : params) {

            //Header
            if (param instanceof HeaderParameter) {

                //Set custom atuthorization headers
                if ("Authorization".equals(param.getName())) {
                    //Check if it´s Bearer or Basic
                    if (param.getDescription() != null) {
                        String description = param.getDescription();
                        System.out.println("      # " + description);

                        if (description.contains("Basic") && description.contains("Bearer")) {
                            printBearerToken();
                            System.out.println("#      YOU MUST TEST WITH A Basic Authorization too!!!!!!");
                            System.out.println("#      And I set the Basic Authorization header with user \"<user>\" and password \"<password>\"");
                            System.out.println("#      ####################################################");

                        } else if (description.contains("Basic")) {
                            System.out.println("      And I set the Basic Authorization header with user \"<user>\" and password \"<password>\"");
                            examples += "user | password | ";
                            requiredFields += "user | password | ";

                        } else if (description.contains("Bearer")) {
                            printBearerToken();
                        }
                    } else {
                        // Default header step
                        System.out.println("      And I set header \"" + param.getName() + "\" as \"<" + param.getName() + ">\"");
                        examples += param.getName() + " | ";
                        if (param.getRequired()) {
                            requiredFields += param.getName() + " | ";
                        }
                    }

                } else {
                    // Default header step
                    System.out.println("      And I set header \"" + param.getName() + "\" as \"<" + param.getName() + ">\"");
                    examples += param.getName() + " | ";
                    if (param.getRequired()) {
                        requiredFields += param.getName() + " | ";
                    }
                }

            //Query parameters
            } else if (param instanceof QueryParameter) {
                System.out.println("      And I set queryparameter \"" + param.getName() + "\" as \"<" + param.getName() + ">\"");
                examples += param.getName() + " | ";
                if (param.getRequired()) {
                    requiredFields += param.getName() + " | ";
                }

            //Path parameters
            } else if (param instanceof PathParameter) {
                System.out.println("      And I set pathparameter \"{" + param.getName() + "}\" as \"<" + param.getName() + ">\"");
                examples += param.getName() + " | ";
                if (param.getRequired()) {
                    requiredFields += param.getName() + " | ";
                }

            //Json Body
            } else if (param instanceof BodyParameter) {
                BodyParameter bodyParam = (BodyParameter) param;

                if (bodyParam.getSchema() != null && bodyParam.getSchema().getReference() != null) {
                    System.out.println("      And I use the json body");
                    System.out.println("        \"\"\"");
                    String[] refPath = bodyParam.getSchema().getReference().split("/");
                    String refName = refPath[refPath.length - 1];

                    String prettyJson = "";
                    //create the json body
                    generateJsonBody(refName);
                    //clean some ","s
                    removeWrongComma();
                    //pretty print json
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        Object json = mapper.readValue(jsonBody, Object.class);
                        prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
                    } catch (Exception e) {
                        prettyJson = jsonBody;
                    }
                    //enerate cucumber variables
                    prettyJson = addCucumberVariables(prettyJson);

                    System.out.println(prettyJson);
                    System.out.println("        \"\"\"");
                }

            } else {
                System.out.println("# PARAM NOT IMPLEMENTED: " + param.toString());
            }
        }
    }

    //Generate the Scenario Example: lines
    private static void generateScenarioExamples(){

        if (!"".equals(enums)) {
            System.out.println(enums);
        }

        if (!"| ".equals(examples)) {
            System.out.println("Examples:");
            System.out.println("    " + examples + "\n\n");
        }

        if (!"| ".equals(requiredFields)) {
            System.out.println("# Only required fields");
            System.out.println("#    " + requiredFields + "\n\n");
        }

        if (!"| ".equals(validationExamples)) {
            System.out.println("# Only validation fields");
            System.out.println("#    " + validationExamples + "\n\n");
        }

        jsonBody = "";
        examples = "| ";
        requiredFields  = "| ";
        validationExamples = "| ";
        enums = "";

    }

    // Generates the Scenario HTTP Action steps
    private static void generateScenarioAction(String requestVerb){
        System.out.println("      And I print the path");
        System.out.println("    When I send the " + requestVerb + " request");
        System.out.println("    Then I print the response");
    }

    // prints a Bearer token steps
    private static void printBearerToken(){

        System.out.println("      And I generate a miliseconds date \"iat\" with \"0\" minutes");
        System.out.println("      And I generate a miliseconds date \"exp\" with \"15\" minutes");
        System.out.println("      And I use the json and generate a JWT Token");
        System.out.println("        \"\"\"");
        System.out.println("          {");
        System.out.println("            \"jti\": \"9e74d1f6-7095-4718-8f11-47dc9ec99c63\",");
        System.out.println("            \"iat\": ${iat},");
        System.out.println("            \"scope\": [ \"read\",\"write\"],");
        System.out.println("            \"user_id\": \"<tokenUserId>\",");
        System.out.println("            \"business_id\": \"<tokenBusinessAccountId>\",");
        System.out.println("            \"client_id\": \"<tokenClientId>\",");
        System.out.println("            \"authorities\": [ <authorities> ],");
        System.out.println("            \"exp\": ${exp}");
        System.out.println("          }");
        System.out.println("          \"\"\"");
        System.out.println("      And I set the Bearer Authorization header");

        examples += "tokenUserId | tokenBusinessAccountId | tokenClientId | authorities | ";
        requiredFields += "tokenUserId | tokenBusinessAccountId | tokenClientId | authorities | ";

    }

}

