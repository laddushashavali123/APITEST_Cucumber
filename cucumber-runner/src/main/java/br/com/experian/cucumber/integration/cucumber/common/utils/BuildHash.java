package br.com.experian.cucumber.integration.cucumber.common.utils;

import br.com.experian.cucumber.integration.cucumber.steps.RestAPISteps;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class BuildHash {
    static RestAPISteps restAPISteps = new RestAPISteps();

    public static LinkedHashMap<String,String> dataTableToHashLine(List<List<String>> dataTable) throws Throwable {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        List name = new ArrayList();

        List<String> row = dataTable.get(0);
        for (String col: row) {
            name.add(col);
        }

        row = dataTable.get(1);
        int i = 0;
        for (String col: row) {
            map.put(name.get(i).toString(), RestApi.replaceVariablesValues(col));
            i++;
        }
        return map;
    }

    public static LinkedHashMap<String,String> dataTableToHashColumn(List<List<String>> dataTable) {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

        for (List<String> row: dataTable){
            map.put(row.get(0),row.get(1));
        }

        return map;
    }

    public static void dataTableToParameter(List<List<String>> dataTable) throws Throwable {
        for (List<String> row: dataTable){
            RestApi.getUserParameters().put("${" + row.get(0) + "}", RestApi.replaceVariablesValues(row.get(1)));
        }
    }
}
