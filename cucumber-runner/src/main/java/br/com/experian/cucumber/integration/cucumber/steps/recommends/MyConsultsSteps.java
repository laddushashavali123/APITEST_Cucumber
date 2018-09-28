package br.com.experian.cucumber.integration.cucumber.steps.recommends;

import br.com.experian.cucumber.integration.cucumber.definitions.RecommendsJson;
import br.com.experian.cucumber.integration.cucumber.steps.MainSteps;
import br.com.experian.cucumber.integration.cucumber.steps.RestAPISteps;
import cucumber.api.java.en.And;

import java.util.List;

public class MyConsultsSteps extends MainSteps {

    RestAPISteps restAPISteps = new RestAPISteps();

    @And("^user informs the following data for my consults into recommends:$")
    public void userInformsTheFollowingDataForMyConsultsIntoRecommends(List<List<String>> dataTable) throws Throwable {
        restAPISteps.fillVariablesAndSetBody(dataTable, RecommendsJson.jsonMyConsults());
    }
}
